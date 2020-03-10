class User:
    def __init__(self, user_id, username, password):
        self._user_id = user_id
        self._username = username
        self._password = password

    def get_user_id(self):
        return self._user_id

    def get_username(self):
        return self._username

    def check_password(self, password):
        return self._password == password

    def add_game_score(self, game_type, game_timestamp, final_score):
        """
        :param game_type: the game type
        :param game_timestamp: the time game ended
        :param final_score: the final score
        """
        # TODO: push to DB
        pass

    def get_preferences_dict(self):
        # TODO: pull preferences dict from database.
        pass
