class Artist:
    def __init__(self, name, id_num, genre, birth_year, soוngs_list, albums_list):
        self._name = name
        self._id = id_num
        self._genre = genre
        self._birth_year = birth_year
        self._songs_list = songs_list
        self._albums_list = albums_list

    def get_name(self):
        return self._name

    def get_genre(self):
        return self._genre

    def get_birth_year(self):
        return self._birth_year

    def get_songs_list(self):
        return self._songs_list

    def get_albums_list(self):
        return self._albums_list
