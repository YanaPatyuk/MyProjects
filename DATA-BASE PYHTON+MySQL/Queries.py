import threading

import Conventions
import mysql.connector
from sqlite3 import OperationalError

from Server import Server

mydb = None
mycursor = None
settings_info = None
DEBUGGING = True
USE_MOCK_DB = False

sql_server = Server()


def run():
    """
    RUN the SQL:
connect to server
    :return:
    """
    status = None
    message = None
    try:
        sql_server.connect()
        print("run:connected")
    except mysql.connector.Error as err:
        print("Something went wrong: {}".format(err))



def try_command():
    cmd = """SELECT songs.name 
    FROM artist 
    JOIN artist_to_credit ON artist_to_credit.artist = artist.id 
    JOIN songs ON songs.artist_credit = artist_to_credit.artist 
    WHERE artist.name = 'Adele' 
    GROUP BY songs.name 
    limit 3;"""
    return sql_server.get_info_by_command(cmd)


"""user data"""


def get_user_id(user_name, password):
    """
    check if user exist by user name and password.
    :param user_name:
    :param password:
    :return:  int   if uer name od password are taken, return Conventions.EMPTY_ANSWERS_LIST_CODE else-return user id-int
    """
    cmd = "SELECT user_id FROM " + sql_server.settings_info["database"] + ".users WHERE username = '"+user_name+"' AND password = '"+password+"';"
    info = sql_server.get_info_by_command(cmd)
    if len(info) == 0:
        return Conventions.EMPTY_ANSWERS_LIST_CODE
    return info[0][0]


def get_user_id_by_name(user_name):
    """
    check if user exist by user name only.
    to prevent two users with same name
    :param user_name:
    :param password:
    :return:     if uer name od password are taken, return Conventions.EMPTY_ANSWERS_LIST_CODE else-return user id
    """
    cmd = "SELECT user_id FROM " + sql_server.settings_info["database"] + ".users WHERE username = '" + user_name + "';"
    info = sql_server.get_info_by_command(cmd)

    if len(info) == 0:
        return Conventions.EMPTY_ANSWERS_LIST_CODE

    return info[0][0]


def get_preferred_genres(user_id):
    """
    if there is  users ganres
    :param user_id:
    :return:
    # returns ['pop', 'classical', 'dance']
    """
    print(user_id)
    cmd = "SELECT preference  FROM " + sql_server.settings_info["database"] + ".users_preferences WHERE user_id = "+str(user_id)+" AND type = 'genre';"
    genres = sql_server.get_info_by_command(cmd)
    if len(genres)==0:
         return False
    return True

def get_user_genres(user_id):
    """
    if there is  users ganres
    :param user_id:
    :return:
    # returns ['pop', 'classical', 'dance']
    """
    print(user_id)
    cmd = "SELECT preference  FROM " + sql_server.settings_info["database"] + ".users_preferences WHERE user_id = "+str(user_id)+" AND type = 'genre';"
    genres = sql_server.get_info_by_command(cmd)
    if len(genres)==0:
         return -1
    return [genre[0] for genre in genres]




def get_similar_artist(artist_name):
    genres = get_genre_by_artist(artist_name)
    artists = list()
    tamp_cmd = """select * from (
    SELECT name FROM """ + sql_server.settings_info["database"]+""".artist WHERE  artist.name != '"""+artist_name+""""' 
    AND  artist.id IN 
    (SELECT artist_id FROM  """ + sql_server.settings_info["database"]+""".artist_genres WHERE genre = '"""+\
    genres[0]+"""') ORDER BY RAND() LIMIT 1) as t1
    union
    select * from(
    SELECT name FROM  """ + sql_server.settings_info["database"]+""".artist WHERE  artist.id NOT IN 
    (SELECT artist_id FROM  """ + sql_server.settings_info["database"]+""".artist_genres WHERE genre = '"""+genres[0]+\
    """') ORDER BY RAND()  LIMIT 3) as t2;
"""

    for g in genres:
        cmd = "SELECT name FROM " +  sql_server.settings_info["database"] + \
              ".artist WHERE id IN " \
              "(SELECT artist_id FROM " +  sql_server.settings_info["database"] + ".artist_genres WHERE genre = '"+g+"');"
        artists.extend([artist_name for artist_name in sql_server.get_info_by_command(cmd)])
    return artists


def had_genre_preferred(user_id):
    """
        if there is  users genres
        :param user_id:
        :return:# returns True/False
        """
    cmd = "SELECT preference FROM " + sql_server.settings_info["database"] + ".users_preferences WHERE user_id = " + str(
        user_id) + " AND type = 'genre';"
    genres = sql_server.get_info_by_command(cmd)
    if len(genres) == 0:
        return False
    return True


def add_preferences(user_id, genres_list):  # todo: change only to genre
    """
    Add prefered ganres of user
    :param user_id:
    :param genres_list:
    :return
    """
    if len(genres_list) != 0:
        for pref in genres_list["Genre"]:
            insert_pref = """INSERT INTO """ + sql_server.settings_info["database"] + """.users_preferences(user_id,type,preference) VALUES(""" + str(
                user_id) + ",'" + "genre" + "','" + pref + "');"
            sql_server.set_info_by_command(insert_pref)
            insert_artist_by_genre = """INSERT INTO users_preferences
                        SELECT distinct """ + str(user_id) + """, "artist",artist.name ,0
                        FROM artist JOIN artist_genres ON artist.id = artist_genres.artist_id
                        WHERE artist_genres.genre = '""" + pref + """'
                        AND artist.name NOT IN (SELECT preference  FROM """ + sql_server.settings_info["database"] + """.users_preferences WHERE user_id = """\
                        + str(user_id) + """
                        ORDER BY RAND())
                        LIMIT 4;
                        """
            sql_server.set_info_by_command(insert_artist_by_genre)


def add_user(user_name, password1):
    """
    Add new user to DB
    :param user_name:
    :param password1:
    :return:# return int user_id or -1 if already exist
     -1 if username taken, else return the user id
    """
    user_name = str(user_name)
    password_d = str(password1)
    #check if user name taken
    user_id = get_user_id_by_name(user_name)
    if user_id is not -1:
        return Conventions.EMPTY_ANSWERS_LIST_CODE

    if get_user_id(user_name,password_d) is not -1:
        return Conventions.EMPTY_ANSWERS_LIST_CODE

    insert_user = """INSERT INTO users(username, password)
                    VALUES ( '""" + user_name+"', '" + password_d + "');"
    sql_server.set_info_by_command(insert_user)
    user_id = str(get_user_id(user_name,password_d))
    return user_id


def confirm_user(user_name, password):
    """
    check if user exist by user name and password.
    :param user_name:
    :param password:
    :return:# return int user_id or -1 if don't exist
     if uer name od password are taken, return Conventions.EMPTY_ANSWERS_LIST_CODE else-return user id
    """
    cmd = "SELECT user_id FROM " + sql_server.settings_info["database"] + ".users WHERE username = '"+user_name+"' AND password = "+password+";"
    info = sql_server.get_info_by_command(cmd)
    if len(info) == 0:
        return Conventions.EMPTY_ANSWERS_LIST_CODE
    return info[0][0]


def get_all_genres():
    """
    return list of possible genres.
    :return:# returns list of genres ['genre1', 'genre2'...]
    """
    cmd = """
        SELECT name FROM (
        SELECT name, COUNT(*) as sum FROM """ + sql_server.settings_info["database"] + """.genres join
         artist_genres on artist_genres.genre = genres.name GROUP BY name ORDER BY sum DESC limit 50) AS aa;
         """
    info = sql_server.get_info_by_command(cmd)

    if len(info) == 0:
        print("Error: info length returned 0 at get_all_genres.\n\t{}".format(info))
        return Conventions.EMPTY_ANSWERS_LIST_CODE

    genres = [genre[0] for genre in info]

    pre_dict= {}
    pre_dict["Genre"] = genres

    return pre_dict


# -------- artist data -------


def get_artist_info(artist_name):
    """
    get data about artist
    :param artist_name:
    :return:# returns list of artist info: ['artist1_name', 'artist1_gender', 'origin_country', 'day/month/year']
    """
    command = "SELECT DISTINCT * FROM artist WHERE artist.name = " + "'" + artist_name + "';"

    if DEBUGGING:
        print(command)
        print(sql_server.get_info_by_command(command))

    artist_info = list(sql_server.get_info_by_command(command)[0])

    if DEBUGGING:
        print(artist_info)
    birth_date = str(artist_info[7])+"/"+str(artist_info[6])+"/"+str(artist_info[5])
    artist_data = list([artist_info[i] for i in range(1, 4)])
    artist_data.append(birth_date)
    return artist_data


def get_genre_by_artist(artist_name):
    """
    Get list of ganres
    :param artist_name:
    :return:# returns list of genres: ['genre1',...]
  -1 if no genres, retruen list of genres
    """
    print(artist_name)
    command = """SELECT artist_genres.genre FROM artist JOIN artist_genres 
        ON artist_genres.artist_id = artist.id  
        WHERE artist.name = '""" + artist_name + "' GROUP BY artist_genres.genre;"""
    print ( command)
    info = sql_server.get_info_by_command(command)
    if len(info)==0:
        return Conventions.EMPTY_ANSWERS_LIST_CODE
    return [g[0] for g in info]


def get_artist(artist_name):
    command = "SELECT * FROM artist WHERE artist.name = " + "'" + artist_name + "'"
    artist_info = list(sql_server.get_info_by_command(command)._getitem_(0))
    birth_date = str(artist_info[7])+"/"+str(artist_info[6])+"/"+str(artist_info[5])
    artist_data = list([artist_info[i] for i in range(1, 4)])
    artist_data.append(birth_date)
    return artist_data


def get_songs(artist_name):
    """
    get 3 songs of artist
    :param artist_name:
    :return:# returns list of songs: ['song1', 'song2',...]
                -1 if no songs, else return the list
    """
    if USE_MOCK_DB:
        return MOCK_SONGS_LIST

    command = """SELECT songs.name FROM artist JOIN artist_to_credit ON artist_to_credit.artist = artist.id 
    JOIN songs ON songs.artist_credit = artist_to_credit.artist 
    WHERE artist.name = '""" + artist_name + """' GROUP BY songs.name limit 3;"""
    info = sql_server.get_info_by_command(command)
    if len(info) is 0:
        return Conventions.EMPTY_ANSWERS_LIST_CODE
    songs_list = [song[0] for song in info]
    print(songs_list[0])
    return songs_list


def get_prefered_artist_easy(user_id):
    """
    Get 1 artist which
    :return:
    """
    cmd = """SELECT * FROM (SELECT preference FROM """ + sql_server.settings_info["database"] + """.users_preferences WHERE 
            count = 0 AND user_id = """ + str(user_id) + """ AND type = 'artist' ORDER BY RAND() ASC limit 1) as t1
            UNION  
            SELECT* FROM( SELECT preference FROM """ + sql_server.settings_info["database"] + """.users_preferences 
            WHERE count < 5 AND user_id = """ + str(user_id) + """ AND type = 'artist' ORDER BY RAND() desc limit 4) as t2
            limit 4;
            """
    return cmd


def get_prefered_artist_hard(user_id):
    cmd = cmd = """SELECT * FROM (SELECT preference FROM """ + sql_server.settings_info["database"] + """.users_preferences 
                WHERE count > 0 AND user_id = """ + str(user_id) + \
                """ AND type = 'artist' ORDER BY count  ASC limit 1 ) as t1
                UNION  
                SELECT* FROM( SELECT preference FROM """ + sql_server.settings_info["database"] + """.users_preferences 
                    WHERE count < 5 AND user_id = """ + str(user_id) + \
                """ AND type = 'artist' ORDER BY count  desc limit 3) as t2
                UNION 
                SELECT * FROM (SELECT preference FROM """ + sql_server.settings_info["database"] + """.users_preferences 
                WHERE count < 5 AND user_id = """ + str(user_id) + """ AND type = 'artist' ORDER BY RAND()  ASC limit 4) as t3
                limit 4;
                """
    return cmd


def get_prefered_artist_challenging(user_id):
    cmd = """SELECT * FROM (SELECT preference FROM """ + sql_server.settings_info["database"] + """.users_preferences 
            WHERE count > 0 AND user_id = """+str(user_id)+""" AND type = 'artist' ORDER BY RAND()  ASC limit 5 ) as t1
            UNION  
            SELECT * FROM( SELECT preference FROM """ + sql_server.settings_info["database"] + """.users_preferences 
            WHERE user_id = """+str(user_id)+""" AND type = 'artist' ORDER BY RAND() desc limit 5) as t2
            limit 5;
            """
    return cmd


def get_preferred_artists(user_id, game_type):
    """
    return list of 4 artist with their data
    :param user_id:
    :return: dictionary with key = 'Artist'
     { 'Artist': [
                  ['artist1_name', 'artist1_gender', 'origin_country', 'birth_date', [list of songs]],
                  ['artist2_name', 'artist2_gender', ....]
                 ]
     }
     -1 if there is a problem or list is empty.
    """
    if game_type == Conventions.EASY_GAME_CODE:
        cmd = get_prefered_artist_easy(user_id)
    elif game_type == Conventions.HARD_GAME_CODE:
        cmd = get_prefered_artist_hard(user_id)
    else:
        cmd = get_prefered_artist_challenging(user_id)
    info = sql_server.get_info_by_command(cmd)

    if len(info) == 0:
        return Conventions.EMPTY_ANSWERS_LIST_CODE

    artists_names = [a[0] for a in info]
    artists_list = list()

    for a_n in artists_names:
        artist_info = list(get_artist_info(a_n))
        songs_ = get_songs(a_n)
        if songs_ != -1:
            songs = list(songs_)
            artist_info.append(songs)
        artists_list.append(artist_info)

    # if their is less artist then needed, add more artist to user study and make query again
    if (len(artists_list) < 5 and game_type == Conventions.CHALLENGING_GAME_CODE) or (len(artists_list) < 4):
        add_preferences(user_id, get_user_genres(user_id))
        return get_preferred_artists(user_id, game_type)

    preferred_artists = dict()
    preferred_artists.__setitem__("Artist", artists_list)

    update_counter(user_id,artists_names,game_type)


    print("List created: {}".format(preferred_artists))
    return preferred_artists


"""old version 
    cmd = "SELECT preference FROM " + settings_info["database"] + ".users_preferences WHERE users_preferences.count < 5 " \
          "AND users_preferences.user_id = " + str(user_id) + " AND users_preferences.type = 'artist' limit 4;"
    info = get_info_by_command(cmd)
    if len(info) == 0:
        return Conventions.EMPTY_ANSWERS_LIST_CODE
    artists_names = [a[0] for a in info]
    artists_list = list()
    for a_n in artists_names:
        artist_info = list(get_artist_info(a_n))
        songs = list(get_songs(a_n))
        artist_info.append(songs)
        artists_list.append(artist_info)
    return artists_list

"""

# -------- RATINGS --------


def update_counter(user_id, artists_played,game_type):  # TODO: artist is a list - each entry has a name
    if game_type != Conventions.CHALLENGING_GAME_CODE:
        cmd = """UPDATE users_preferences
                SET users_preferences.count = CASE
                WHEN users_preferences.count IS NOT NULL THEN users_preferences.count + 1
                ELSE  users_preferences.count
                END
                WHERE user_id = """ +\
              str(user_id) + " AND users_preferences.preference = '" + artists_played[0] + "';"
        sql_server.set_info_by_command(cmd)
    else:
        for artist in artists_played:
            cmd = """UPDATE users_preferences
                   SET users_preferences.count = CASE
                   WHEN users_preferences.count IS NOT NULL THEN users_preferences.count + 1
                   ELSE  users_preferences.count
                   END
                   WHERE user_id = """ + \
                  str(user_id) + " AND users_preferences.preference = '" + artist + "';"
            sql_server.set_info_by_command(cmd)


def add_game(typ, score, user_id):
    """
    update score to user
    :param typ:
    :param score:
    :param user_id:
    :return:
    """
    if typ == Conventions.EASY_GAME_CODE:
        typ = "first_game_points"
    elif typ == Conventions.HARD_GAME_CODE:
        typ = "second_game_point"
    else:
        typ = "third_game_points"

    cmd = "UPDATE users "\
        "SET " + sql_server.settings_info["database"] + ".users." + typ + " = CASE "\
        "WHEN " + sql_server.settings_info["database"] + ".users." + typ + " IS NULL THEN " + str(score) +\
        " WHEN " + sql_server.settings_info["database"] + ".users." + typ + " < " + str(score) + " THEN " + str(score) + " "\
        "ELSE  " + sql_server.settings_info["database"] + ".users." + typ +\
        " END "\
        "WHERE user_id = " + str(user_id) + ";"
    sql_server.set_info_by_command(cmd)


def get_top_players(game_type):
    """
    return list of top players by type of the game
    :param game_type:
    :return:# return list of players: ['user1', 'user2']
  if no players, return Conventions.EMPTY_ANSWERS_LIST_CODE
    """
    if game_type == Conventions.EASY_GAME_CODE:
        game_type = "first_game_points"
    elif game_type == Conventions.HARD_GAME_CODE:
        game_type = "second_game_point"
    else:
        game_type = "third_game_points"
    cmd = "SELECT username,"+ game_type +\
          " FROM " + sql_server.settings_info["database"] +".users WHERE "+ game_type+" is not null ORDER BY users." + game_type + " DESC limit 3;"
    info = sql_server.get_info_by_command(cmd)
    if len(info) == 0:
        return Conventions.EMPTY_ANSWERS_LIST_CODE
    top = [[p[0], p[1]] for p in info]
    return top


"""

if __name__ == '__main__':
    run()
    execute_scripts_from_file("build_tables.sql")
    print(get_genre_by_artist("Adele"))
List of commands:
get artist_songs: (adele songs limit list to 3 - no duplicated song names)
    SELECT songs.name 
    FROM artist 
    JOIN artist_to_credit ON artist_to_credit.artist = artist.id 
    JOIN songs ON songs.artist_credit = artist_to_credit.artist 
    WHERE artist.name = 'Adele' 
    GROUP BY songs.name 
    limit 3;

"""

MOCK_SONGS_LIST = ['test1', 'test2', 'test3']
MOCK_SONGS_LIST_WITH_NONE_VALS = ['test1', 'test2', None]
