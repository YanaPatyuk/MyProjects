
SET GLOBAL local_infile = ON;


CREATE DATABASE IF NOT EXISTS funny_name;
GRANT ALL PRIVILEGES ON funny_name.* TO 'funny_name'@'localhost' WITH GRANT OPTION;
GRANT FILE ON *.* to 'funny_name'@'localhost';
USE funny_name;


CREATE TABLE IF NOT EXISTS albums ( -- replicate
    name                    VARCHAR(255),
    artist_credit           INTEGER
) CHARACTER SET utf8 COLLATE utf8_general_ci;


CREATE TABLE IF NOT EXISTS artist_to_credit ( -- replicate (verbose)
    artist              INTEGER NOT NULL, -- references
    artist_credit       INTEGER NOT NULL -- PK
) CHARACTER SET utf8 COLLATE utf8_general_ci;


CREATE TABLE IF NOT EXISTS artist ( -- replicate (verbose)
    id                  BIGINT UNSIGNED,
    name                VARCHAR(255) NOT NULL,
	gender              VARCHAR(255) NOT NULL,
    area                VARCHAR(255) NOT NULL,
    type                VARCHAR(255) NOT NULL,
    begin_date_year     SMALLINT,
    begin_date_month    SMALLINT,
    begin_date_day      SMALLINT
) CHARACTER SET utf8 COLLATE utf8_general_ci;

CREATE TABLE IF NOT EXISTS songs ( -- replicate (verbose)
    id                  BIGINT UNSIGNED, -- PK 
    name                VARCHAR(255) NOT NULL,
    artist_credit       INTEGER NOT NULL, 
    medium              INTEGER NOT NULL 
) CHARACTER SET utf8 COLLATE utf8_general_ci;

CREATE TABLE IF NOT EXISTS genres ( -- replicate (verbose)
    name                VARCHAR(255) NOT NULL
)
CHARACTER SET utf8 COLLATE utf8_general_ci;


CREATE TABLE IF NOT EXISTS artist_genres ( -- replicate (verbose)
    artist_id                  BIGINT UNSIGNED,
    genre              		   VARCHAR(255) NOT NULL,
	count         			   INTEGER 
) CHARACTER SET utf8 COLLATE utf8_general_ci;


CREATE TABLE IF NOT EXISTS users ( -- replicate (verbose)
    user_id                   INT NOT NULL AUTO_INCREMENT,
    username              		   VARCHAR(255) NOT NULL,
	password         			   VARCHAR(255) NOT NULL ,
    first_game_points					INT,
    second_game_point					INT,
    third_game_points					INT,
	PRIMARY KEY (user_id)
) CHARACTER SET utf8 COLLATE utf8_general_ci;

CREATE TABLE IF NOT EXISTS users_preferences ( -- replicate (verbose)
    user_id                  INT,
	type  					 VARCHAR(255) NOT NULL,
    preference               VARCHAR(255) NOT NULL,
    count                    INT
) CHARACTER SET utf8 COLLATE utf8_general_ci;


LOAD DATA LOCAL INFILE './DB_FUNNY_NAME/ARTIST_TO_CREDIT.csv'   INTO TABLE artist_to_credit FIELDS TERMINATED BY ',' ENCLOSED BY '"' LINES TERMINATED BY '\n' IGNORE 1 ROWS;
LOAD DATA LOCAL INFILE './DB_FUNNY_NAME/ARTISTS.csv'  INTO TABLE artist FIELDS TERMINATED BY ',' ENCLOSED BY '"' LINES TERMINATED BY '\n' IGNORE 1 ROWS;
LOAD DATA LOCAL INFILE './DB_FUNNY_NAME/ARTISTS_GENRE.csv'  INTO TABLE artist_genres FIELDS TERMINATED BY ',' ENCLOSED BY '"' LINES TERMINATED BY '\n' IGNORE 1 ROWS;
LOAD DATA LOCAL INFILE './DB_FUNNY_NAME/GENRES.csv'  INTO TABLE genres FIELDS TERMINATED BY ',' ENCLOSED BY '"' LINES TERMINATED BY '\n' IGNORE 1 ROWS;
LOAD DATA LOCAL INFILE './DB_FUNNY_NAME/ALBUMS_NEW.csv'  INTO TABLE albums FIELDS TERMINATED BY ',' ENCLOSED BY '"' LINES TERMINATED BY '\n' IGNORE 1 ROWS;
LOAD DATA LOCAL INFILE './DB_FUNNY_NAME/SONGS.csv'  INTO TABLE songs FIELDS TERMINATED BY ',' ENCLOSED BY '"' LINES TERMINATED BY '\n' IGNORE 1 ROWS;


CREATE INDEX artist_to_credit_index_credit ON artist_to_credit (artist_credit);
CREATE INDEX artist_to_credit_index_artist ON artist_to_credit (artist);

CREATE INDEX artist_index_name ON artist(name);
CREATE INDEX artist_index_id ON artist(id);

CREATE INDEX artist_genres_index_genre ON artist_genres(genre);
CREATE INDEX artist_genres_index_credit ON artist_genres(artist_id);

CREATE INDEX songs_index ON songs(artist_credit);

CREATE INDEX albums_index ON albums(artist_credit);

COMMIT;

-- vi: set ts=4 sw=4 et :