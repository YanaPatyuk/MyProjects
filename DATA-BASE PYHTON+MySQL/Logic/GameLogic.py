import random
import Queries
import Conventions
from View.crash import crash_window


TESTING_VIEW = False
DEBUGGING = False
PLAYING_ARTIST_OFF_SET = 0

DEBUGGING_GAME_END = True
DEBUGGING_QUESTIONS_GENERATING = False

NAME_OFF_SET = 0
GENDER_OFF_SET = 1
FROM_OFF_SET = 2
BIRTH_DATE_OFF_SET = 3
SONGS_LIST_OFF_SET = 4

NUMBER_OF_ORIGINS = 3


GAME_TYPES = [
    "EASY",
    "HARD",
    "CHALLENGING"
]

QUESTIONS = {
    "Name": "What was the artist's name",
    "Genre": "Which of the following was one of the artist's genres?",
    "Birth_Date": "When was the artist born?",
    "From": "Where was the artist from?",
    "Songs": None
}


PREFERENCES_EXIST_STATUS = 1
PREFERENCES_NON_EXISTING = 0
USER_DOESNT_EXIST = -1
ADD_FAILURE = -1


def login(username, password):
    user_id = load_user_from_data_base(username, password)

    if user_id == USER_DOESNT_EXIST:
        return USER_DOESNT_EXIST

    user_preferences = Queries.get_preferred_genres(user_id)

    if user_preferences:
        return PREFERENCES_EXIST_STATUS
    else:
        return PREFERENCES_NON_EXISTING


def register(user_name, password):
    """
    adds a new user to the database

    :param user_name:
    :param password:
    :return: 0 if user exists or 1 if user was created
    """
    add_success = 1
    add_fail = 0

    print("Game logic: "+user_name+" "+password)
    if Queries.add_user(user_name, password) == ADD_FAILURE:
        return add_fail

    return add_success


def add_preferences_to_user(username, properties_dict):
    user_id = load_user_id_only_by_name(username)

    print(properties_dict)
    Queries.add_preferences(user_id, properties_dict)


def load_user_id_only_by_name(username):
    return Queries.get_user_id_by_name(str(username))


def load_user_from_data_base(username, password):
    return Queries.get_user_id(str(username), str(password))


def calculate_final_score(answers_list, game_dict, game_type):
    final_score = 0
    q_index = 0

    if DEBUGGING_GAME_END:
        print("at 'calculate_final_score'")

    for question in game_dict['questions'].values():
        if DEBUGGING_GAME_END:
            print("\tquestion: {}".format(question))
            print("\tquestion['true']: {}\n\tanswer: {}".format(question['true'],
                                                                answers_list[q_index]))

        if question['true'] == answers_list[q_index]:
            final_score += POINTS_TO_ADD[game_type]
            if DEBUGGING_GAME_END:
                print("Adding {} points on right answer: {}".format(POINTS_TO_ADD[game_type], answers_list[q_index]))

        q_index += 1

    return final_score


def end(username, answers_list, game_dict, game_type):
    if DEBUGGING_GAME_END:
        print("Game ended with the following stats:\n\tusername: {}\n\tanswers_list{}\n\tgame_type: {}".format(username, answers_list, game_type))

    final_score = calculate_final_score(answers_list, game_dict, GAME_TYPES_CODE_FROM_VIEW_TO_STRING[game_type])

    if not TESTING_VIEW:
        # get user ID
        user_id = load_user_id_only_by_name(username)

        # save the game
        Queries.add_game(GAME_TYPES_CODE_FROM_VIEW_TO_STRING[game_type], final_score, user_id)

        # return final score
        return final_score
    else:
        return MOCK_GAME_SCORE
    # answers_list = game.get_answers_list()
    #
    # questions_dict = game.get_questions_dict()
    #
    # # tag the answer (True/False for right/wrong)
    # answers_list.append(questions_dict[question][True] == answer)
    #
    # # check if score should be calculated
    # if len(questions_dict) == answers_list:
    #     # tag the game ending time
    #     game.set_end_time(self.collect_time_stamp())
    #
    #     # calculate the score
    #     game.set_final_score(
    #         self.generate_final_score()(
    #             len(questions_dict),
    #             answers_list.count(True)
    #         )
    #     )


def generate_questions(raw_artists_dict, game_type):
    """
    Will generate the questions based on the user artists preferences

    :return:
    """
    questions_map = {
        "from": generate_origin_question,
        "genre": generate_genre_question,
        "birth_date": generate_birth_date_question,
        "similar": generate_similar_artists_question,
        "name": generate_name_question
    }

    questions = [questions_map['from'](raw_artists_dict),
                 questions_map['genre'](raw_artists_dict),
                 questions_map['birth_date'](raw_artists_dict),
                 questions_map['similar'](raw_artists_dict)
                 ]  # TODO: add the rest of the questions to here
    random.shuffle(questions)  # shuffle

    # add the artist name question as first question for a hard game
    if game_type == Conventions.HARD_GAME_CODE:
        questions.insert(0, questions_map['name'](raw_artists_dict))

    build_questions_dict_for_view(questions)

    # build and return the questions dict
    return build_questions_dict_for_view(questions)


def generate_name_question(raw_artists_dict):
    question_text = "What is the artist that you are playing on?"
    answers = [artist[NAME_OFF_SET] for artist in raw_artists_dict['Artist']]  # append all artists names
    right_answer = raw_artists_dict['Artist'][PLAYING_ARTIST_OFF_SET][NAME_OFF_SET]

    return build_question_dict(question_text, answers, right_answer)



def build_questions_dict_for_view(questions_list):
    """
    builds the questions dict from the questions list. Inserts question_name: question per not None value.

    :param questions_list:
    :return:
    """
    questions_dict = dict()

    if DEBUGGING_QUESTIONS_GENERATING:
        print("Questions list is:\n{}".format(questions_list))

    for question in questions_list:
        # insert question if it is not None
        if question:
            question_name = question['text']

            if DEBUGGING_QUESTIONS_GENERATING:
                print("Question name to add is: {}".format(question_name))

            questions_dict[question_name] = question

    if DEBUGGING_QUESTIONS_GENERATING:
        print("Questions dict build:\n{}".format(questions_dict))

    return questions_dict


def none_values_exist_in_answer_list(answers_list):
    for answer in answers_list:
        if answer is None:
            if DEBUGGING_QUESTIONS_GENERATING:
                print("answers list holds None values:{}".format(answers_list))
            return True

    return False


def answers_list_empty_or_less_than_three_songs(answers_list):
    if answers_list == Conventions.EMPTY_ANSWERS_LIST_CODE or \
           len(answers_list) != Conventions.VALID_SONGS_ANSWERS_LIST_SIZE:
        if DEBUGGING_QUESTIONS_GENERATING:
            print("answers list is in wrong size of empty:{}".format(answers_list))
        return None


def generate_random_list_of_origins():
    list_of_origins = LIST_OF_ORIGINS.copy()

    origins_list_to_return = list()

    for number_of_origins_to_add in range(NUMBER_OF_ORIGINS):
        # get a random origin
        rand_origin = random.choice(list_of_origins)

        # add it to the list
        origins_list_to_return.append(rand_origin)

        # remove it from the list_of_origins
        list_of_origins.remove(rand_origin)

    return origins_list_to_return


def generate_origin_question(raw_artists_dict):
    question_text = "Where is the artist from?"  # text
    # generate a random list of origins and add the right answer
    answers = generate_random_list_of_origins()
    answers.append(raw_artists_dict['Artist'][PLAYING_ARTIST_OFF_SET][FROM_OFF_SET])

    if none_values_exist_in_answer_list(answers):
        return None

    if answers_list_empty_or_less_than_three_songs(answers):
        return None

    right_answer = raw_artists_dict['Artist'][PLAYING_ARTIST_OFF_SET][FROM_OFF_SET]  # the origin of the playing artist

    return build_question_dict(question_text, answers, right_answer)


def generate_birth_date_question(raw_artists_dict):
    question_text = "What is the artist's birth date?"
    answers = [artist[BIRTH_DATE_OFF_SET] for artist in raw_artists_dict['Artist']]

    if none_values_exist_in_answer_list(answers):
        return None

    if answers_list_empty_or_less_than_three_songs(answers):
        return None

    right_answer = answers[0]

    return build_question_dict(question_text, answers, right_answer)


def generate_genre_question(raw_artists_dict):
    list_of_lists_of_genres = list()

    for artist in raw_artists_dict['Artist']:
        # appends a LIST of genres per artist name
        list_of_lists_of_genres.append(Queries.get_genre_by_artist(artist[NAME_OFF_SET]))

    genres_for_question = list()

    # randomly pick a genre from the list of genres per artist
    for artist_genres_list in list_of_lists_of_genres:
        genres_for_question.append(artist_genres_list[random.randint(0, len(artist_genres_list) - 1)])

    # check for None values
    if none_values_exist_in_answer_list(genres_for_question):
        return None

    if answers_list_empty_or_less_than_three_songs(genres_for_question):
        return None

    question_text = "What is the artist's genre?"
    right_answer = genres_for_question[0]

    return build_question_dict(question_text, genres_for_question, right_answer)


def generate_similar_artists_question(raw_artists_dict):
    # artist name
    artist_name = raw_artists_dict['Artist'][PLAYING_ARTIST_OFF_SET][NAME_OFF_SET]

    question_text = "Who is the most similar artist to this artist?"

    # generate similar artist by randomly picking a genre of similarity
    similar_artist_list = Queries.get_similar_artist(artist_name)

    # randomly pick a similar artist from the similar_artists_list
    similar_artist = similar_artist_list[random.randint(0, len(similar_artist_list) - 1)]

    if DEBUGGING_QUESTIONS_GENERATING:
        print("Similar artist picked is: {}".format(similar_artist))

    answers = list()

    # insert all of the artists names
    for artist in raw_artists_dict['Artist']:
        answers.append(artist[NAME_OFF_SET])

    answers = answers[1:]  # remove the name of the artist we are playing on
    answers.append(similar_artist)  # append the name of the similar artist

    # check None values
    if none_values_exist_in_answer_list(answers):
        return None

    if answers_list_empty_or_less_than_three_songs(answers):
        return None

    return build_question_dict(question_text, answers, similar_artist)


def build_question_dict(text, answers, right_answer):
    return {
        "text": text,
        "answers": answers,
        "true": right_answer
    }


def get_all_preferences():
    return Queries.get_all_genres()


def get_leaderboard():
    leaderboard = dict()

    for game_type in GAME_TYPES:
        leaderboard[game_type] = list()
        leaderboard[game_type].append(['Player name', 'Score'])

        top_players = Queries.get_top_players(game_type)
        if top_players != Conventions.EMPTY_ANSWERS_LIST_CODE:
            for data_list in top_players:
                leaderboard[game_type].append(data_list)

    return leaderboard

    # FORMAT:
    # {
    #     "EASY": [['Player name', 'score'], ['baba', 100], ['gaga', 98], ['jaja', 90]],
    #     "HARD": [['Player name', 'score'], ['nana', 100], ['mama', 96], ['haha', 95]],
    #     "CHALLENGING": [['Player name', 'score'], ['lala', 99], ['tata', 95], ['rara', 90]]
    # }


def start(username, game_type):
    """
    :param username:
    :param game_type:
    :return:
    """

    if DEBUGGING:
        print("The game type received is: {}".format(game_type))

    # create a game depending on the game_type
    if game_type == Conventions.EASY_GAME_CODE or game_type == Conventions.HARD_GAME_CODE:
        if DEBUGGING:
            print("Creating an easy game!")
        return generate_easy_or_hard_games(username, game_type)

    return generate_challenging_game(username, game_type)



def generate_challenging_game(user_name, game_type):
    user_id = load_user_id_only_by_name(user_name)  # generate user ID

    raw_artists_dict = Queries.get_preferred_artists(user_id, game_type)  # generate raw artists dict

    # generate artists list
    artists_list = [artist[NAME_OFF_SET] for artist in raw_artists_dict['Artist']]

    # generate the properties list
    properties_list_for_each_artist = list()

    for artist in raw_artists_dict['Artist']:
        # create a list per artist
        list_for_artist = list()

        # append relevant information
        list_for_artist.append(artist[PLAYING_ARTIST_OFF_SET][FROM_OFF_SET])  # from
        list_for_artist.append(artist[PLAYING_ARTIST_OFF_SET][BIRTH_DATE_OFF_SET])  # birth date

        for song in artist[SONGS_LIST_OFF_SET]:
            list_for_artist.append(song)  # append all songs

        properties_list_for_each_artist.append(list_for_artist)

    if not TESTING_VIEW:
        return {
            "artist_name": artists_list,
            "properties": properties_list_for_each_artist,
            "questions": generate_questions(raw_artists_dict, game_type)
        }
    else:
        return MOCK_CHALLENGING_DICT


def generate_easy_or_hard_games(user_name, game_type):
    user_id = load_user_id_only_by_name(user_name)  # generate user ID

    raw_artists_dict = Queries.get_preferred_artists(user_id, game_type)  # generate raw artists dict
    print(raw_artists_dict)
    '''
    format:
        { 'Artist':
            ["artist_name", "gender", "from", "birth_date", [songs_list]] <- artist played on
            ["artist_name", "gender", "from", "birth_date", [songs_list]] <- wrong answers
            ...
        }
    '''

    # generate the properties list
    properties = list()
    properties.append(raw_artists_dict['Artist'][PLAYING_ARTIST_OFF_SET][FROM_OFF_SET])  # from
    properties.append(raw_artists_dict['Artist'][PLAYING_ARTIST_OFF_SET][BIRTH_DATE_OFF_SET])  # birth date

    # append all the songs of the artist as properties
    for song in raw_artists_dict['Artist'][PLAYING_ARTIST_OFF_SET][SONGS_LIST_OFF_SET]:
        properties.append(song)

    if not TESTING_VIEW:
        return {
            "artist_name": raw_artists_dict['Artist'][PLAYING_ARTIST_OFF_SET][NAME_OFF_SET],
            "properties": properties,
            "questions": generate_questions(raw_artists_dict, game_type)
        }
    else:
        return MOCK_DICT


MOCK_DICT = {
        "artist_name": "Adel",
        "properties": ["Country: UK", "Date: 12.12.1980", "Song1: Alon kaka", "Song2: Sara kaka", "Song3: Yana kaka"],
        "questions": {
            "q1": {
                "text": "Country?",
                "answers": ["Israel", "USA", "Poland", "UK"],
                "true": "UK"
            },
            "q2": {
                "text": "Date?",
                "answers": ["12.12.1984", "12.12.1979", "12.12.1980", "12.12.1981"],
                "true": "12.12.1980"
            },
            "q3": {
                "text": "Song1?",
                "answers": ["song1_a1", "song1_a2", "song1_a3", "Alon kaka"],
                "true": "right_answer"
            },
            "q4": {
                "text": "Song2?",
                "answers": ["song2_a1", "song2_a2", "Sara kaka", "song2_a3"],
                "true": "right_answer"
            },
            "q5": {
                "text": "Song3?",
                "answers": ["Yana kaka", "song3_a1", "song3_a2", "song3_a3"],
                "true": "Yana kaka"
            }
        }
    }


MOCK_CHALLENGING_DICT = GameInfoDict = {
        "artist_name": ["Adel", "Adel2", "Adel3", "Ade4", "Adel5"],
        "properties": [["Country: UK", "Date: 12.12.1980", "Song1: Alon kaka", "Song2: Sara kaka", "Song3: Yana kaka"], ["Country1: UK", "Date1: 12.12.1980", "Song11: Alon kaka", "Song12: Sara kaka", "Song13: Yana kaka"], ["Country2: UK", "Date2: 12.12.1980", "Song21: Alon kaka", "Song22: Sara kaka", "Song23: Yana kaka"], ["Country3: UK", "Date3: 12.12.1980", "Song31: Alon kaka", "Song32: Sara kaka", "Song33: Yana kaka"], ["Country4: UK", "Date4: 12.12.1980", "Song41: Alon kaka", "Song42: Sara kaka", "Song43: Yana kaka"]],
        "questions": {
            "q1": {
                "text": "Country?",
                "answers": ["Israel", "USA", "Poland", "UK"],
                "true": "UK"
            },
            "q2": {
                "text": "Date?",
                "answers": ["12.12.1984", "12.12.1979", "12.12.1980", "12.12.1981"],
                "true": "12.12.1980"
            },
            "q3": {
                "text": "Song1?",
                "answers": ["song1_a1", "song1_a2", "song1_a3", "Alon kaka"],
                "true": "right_answer"
            },
            "q4": {
                "text": "Song2?",
                "answers": ["song2_a1", "song2_a2", "Sara kaka", "song2_a3"],
                "true": "right_answer"
            },
            "q5": {
                "text": "Song3?",
                "answers": ["Yana kaka", "song3_a1", "song3_a2", "song3_a3"],
                "true": "Yana kaka"
            }
        }
    }

MOCK_GAME_SCORE = 1200

LIST_OF_ORIGINS = [
        "Israel",
        "Turkey",
        "Gibraltar",
        "Côte d'Ivoire",
        "Bosnia and Herzegovina",
        "Colombia",
        "Bulgaria",
        "Morocco",
        "Poland",
        "Russian Federation",
        "Guinea",
        "Marshall Islands",
        "Brunei Darussala",
        "Ireland",
        "Albania",
        "Finland",
        "Sweden",
        "Martinique",
        "Lithuania",
        "Benin",
        "Bangladesh"
    ]

POINTS_TO_ADD = {
    Conventions.EASY_GAME_CODE: 50,
    Conventions.HARD_GAME_CODE: 70,
    Conventions.CHALLENGING_GAME_CODE: 50
}

GAME_TYPES_CODE_FROM_VIEW_TO_STRING = {
    1: Conventions.EASY_GAME_CODE,
    2: Conventions.HARD_GAME_CODE,
    3: Conventions.CHALLENGING_GAME_CODE
}