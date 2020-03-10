from Logic.LogicUtilities import LogicUtilities as lG


class Game:
    def __init__(self, user):
        self._final_score = 0
        self._number_of_properties = 10
        self._end_time = None
        self._user = user
        self._logic_generator = lG(user, self)
        self._properties_list = None
        self._questions_dict = None  # see structure at the end of this file
        self._answers_list = list()
        self._artists_list = list()

    def set_final_score(self, score):
        self._final_score = score

    def set_end_time(self, timestamp):
        self._end_time = timestamp

    def get_questions_dict(self):
        return self._questions_dict

    def get_answers_list(self):
        return self._answers_list

    def start(self, properties_number, artists_number, questions_number):
        self._properties_list = self._logic_generator.generate_properties(properties_number)
        self._artists_list = self._logic_generator.generate_artists_list(artists_number)
        self._questions_dict = self._logic_generator.generate_questions(questions_number)

    def get_final_score(self):
        return self._final_score


"""
{  
    Question: {
        question: "Q GOES HERE"
        True: "right_answer"
        "answers": ["right_answer", "ANSWER2", "ANSWER3", "ANSWER4"] <- randomize 
    }
}
"""