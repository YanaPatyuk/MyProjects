import mysql.connector

from sqlite3 import OperationalError

import Conventions
from View.crash import crash_window

mydb = None
mycursor = None
settings_info = None


class Server:
    def __init__(self):
        global settings_info
        settings_info = self.gat_configurations_from_file("../ServerData.txt")
        self.settings = settings_info
        self.settings_info = settings_info

    """
    Server class
    """
    def gat_configurations_from_file(self, file_name):
        """
        Read config from file
        :param file_name: filenme with path
        :return: dict
        """
        global settings_info
        settings_info = dict()
        f = open(file_name, "r")
        for line in f:
            info, value = line.split(":")
            settings_info[info] = value.replace("\n", "")
        return settings_info

    def execute_scripts_from_file(self, filename):
        """
        Run sql commands from sql file.
        This commands create the DB
        """
        # Open and read the file as a single buffer
        fd = open(filename, 'r', encoding="utf-8")
        sql_file = fd.read()
        fd.close()

        # all SQL commands (split on ';')- no need for ";"
        sqlCommands = sql_file.split(';')

        # Execute every command from the input file
        for command in sqlCommands:
            # This will skip and report errors
            # For example, if the tables do not yet exist, this will skip over
            # the DROP TABLE commands
            try:
                print(command)
                mycursor.execute(command)
            except mysql.connector.Error as err:
                print(" execute_scripts_from_file:Command skipped: " + err.msg)
        return ""

    def set_user_config(self, db_name, user_name):
        """
        Set database and provileges for user.
        :param db_name:  string got from file
        :param user_name: got from file
        """
        try:
            cmd = "GRANT ALL PRIVILEGES ON " + db_name + ".* TO '" + user_name + "'@'localhost' WITH GRANT OPTION;"
            mycursor.execute(cmd)
            cmd = "GRANT FILE ON *.* to '" + user_name + "'@'localhost';"
            mycursor.execute(cmd)
            cmd = "USE " + db_name + ";"
            mycursor.execute(cmd)
        except mysql.connector.Error as err:
            print("set_user_config:Command skipped: " + err.msg)
            crash_window("Cant set user configurations to database")
            return "Error while setting users PRIVILEGES:" + err.msg, Conventions.CRASHING_MODE
        return Conventions.WORKING_MODE

    def connect(self):
        """
        connect the SQL:
    connect to server
        :return:
        """
        global mydb, mycursor
        # get settings for connection
        try:
            mydb = mysql.connector.connect(host=settings_info["ip"], user=settings_info["user_name"],
                                           password=settings_info["password"],
                                           port=settings_info["port"],
                                           allow_local_infile=True)
            mycursor = mydb.cursor()
            self.set_user_config(settings_info["database"], settings_info["user_name"])
        except mysql.connector.Error as err:
            print("Something went wrong: {}".format(err))
            crash_window("Cant connect to database")

    def get_info_by_command(self, command_string):
        """
        run sql command
        :param command_string:
        :return:
        """
        myresult = ""
        try:
            mycursor.execute(command_string)
            myresult = mycursor.fetchall()
        except mysql.connector.Error as err:
            print("get_info_by_command:Something went wrong: {}" + err.msg)
            crash_window("Cant connect run commands on database")
        return myresult

    def set_info_by_command(self, command_string):
        """
        run sql command for inser
        :param command_string:
        :return:
        """
        try:
            mycursor.execute(command_string)
            mydb.commit()
        except mysql.connector.Error as err:
            print("set_info_by_command:Something went wrong: {}" + err.msg)
            crash_window("Cant connect update data on database")
        print(command_string)
