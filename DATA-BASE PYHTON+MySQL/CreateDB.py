from Server import Server

server = Server()
print("Connecting to server..")
server.connect()
print("Set user settings to server..")
server.set_user_config(server.settings["database"], server.settings["user_name"])
print("Create tables")
server.execute_scripts_from_file("build_tables.sql")
print("Database successfully installed")





