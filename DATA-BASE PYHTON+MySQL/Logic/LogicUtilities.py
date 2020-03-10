import Queries
import datetime


class LogicUtilities:
    QUESTIONS_NUMBER = 5

    def question_answered(self, question, answer, game):
        answers_list = game.get_answers_list()
        questions_dict = game.get_questions_dict()

        # tag the answer (True/False for right/wrong)
        answers_list.append(questions_dict[question][True] == answer)

        # check if score should be calculated
        if len(questions_dict) == answers_list:
            # tag the game ending time
            game.set_end_time(self.collect_time_stamp())

            # calculate the score
            game.set_final_score(
                self.generate_final_score()(
                    len(questions_dict),
                    answers_list.count(True)
                )
            )

    def generate_properties(self, user_name, game_type):
        """
        Will generate the properties from DB based on the user preferences

        :param user_name: the user name
        :param game_type: the game type
        :return:
        """
        pass

    def generate_questions(self, user_name, game_type):
        """
        Will generate the questions based on the user artists preferences

        :return:
        """
        user_id = Queries.get_user_id(user_name)

        pass

    @staticmethod
    def collect_time_stamp():
        return datetime.datetime.now()

    @staticmethod
    def generate_final_score():
        return lambda correct_answers, questions: (correct_answers / questions) * 100

    @staticmethod
    def generate_artists_list(number_of_artists):
        pass
