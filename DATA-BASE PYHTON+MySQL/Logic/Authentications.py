import Queries


PREFERENCES_EXIST_STATUS = 1
PREFERENCES_NON_EXISTING = 0
USER_DOESNT_EXIST = -1
ADD_FAILURE = -1


def login(username, password):
    user_id = load_user_from_data_base(username, password)

    if user_id == USER_DOESNT_EXIST:
        return USER_DOESNT_EXIST

    user_preferences = None  # TODO: get user preferences from db

    if user_preferences:
        return PREFERENCES_EXIST_STATUS
    else:
        return PREFERENCES_NON_EXISTING


def register_user(user_name, password):
    """
    adds a new user to the database

    :param user_name:
    :param password:
    :param properties_list:
    :return: 0 if user exists or 1 if user was created
    """
    add_success = 1
    add_fail = 0

    if Queries.add_user(user_name, password) == ADD_FAILURE:
        return add_fail

    return add_success


def add_properties_to_user(username, properties_list):
    # TODO: add properties list to user in DB
    pass


def load_user_from_data_base(username, password):
    return Queries.get_user_id(username, password)
