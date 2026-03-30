CREATE TABLE pack(
    pack_id SERIAL PRIMARY KEY,
    pack_name text,
    number_of_animals integer
);

CREATE TABLE animals(
    animal_id SERIAL PRIMARY KEY,
    pack_id integer REFERENCES pack(pack_id),
    age integer,
    sex char(1) check (sex in ('M','F')),
    is_healthy boolean,
    power integer
);
CREATE TABLE locations(
    location_id SERIAL PRIMARY KEY,
    name text,
    type text,
    is_food boolean,
    distance integer
);
CREATE TABLE emotions(
    emotion_id SERIAL PRIMARY KEY,
    name text,
    is_possitive boolean,
    description text
);
CREATE TABLE animal_emotion(
    animal_id integer REFERENCES animals(animal_id),
    emotion_id integer REFERENCES emotions(emotion_id),
    triger text,
    intensity integer,
    PRIMARY KEY(animal_id, emotion_id)
);
CREATE TABLE movings(
    move_id SERIAL PRIMARY KEY,
    pack_id integer REFERENCES pack(pack_id),
    location_id integer REFERENCES locations(location_id),
    speed integer
);
CREATE TABLE weather(
    weather_id SERIAL PRIMARY KEY,
    name text,
    type text,
    is_dangerous boolean
);
CREATE TABLE local_weather(
    location_id integer REFERENCES locations(location_id),
    weather_id integer REFERENCES weather(weather_id),
    intensity integer,
    PRIMARY KEY(location_id, weather_id)
);
INSERT INTO pack(pack_name,number_of_animals) VALUES('staya 1', 4);
INSERT INTO locations(name,type,is_food,distance) VALUES('green village', 'field', true, 6);
INSERT INTO locations(name,type,is_food,distance) VALUES('empty corner', 'field', false, 2);
INSERT INTO locations(name,type,is_food,distance) VALUES('dark forest', 'forest', false, 8);
INSERT INTO emotions(name,is_possitive,description) VALUES( 'sympathy',false,'animal is worried about another one');
INSERT INTO emotions(name,is_possitive,description) VALUES( 'hunger',false,'animal doesnt get enough food');
INSERT INTO emotions(name,is_possitive,description) VALUES( 'weekness',false,'animal cant go');
INSERT INTO weather(name,type,is_dangerous) VALUES('znoy','warm',true);
INSERT INTO animals(pack_id,age,sex,is_healthy,power) VALUES(1,10,'F',false,3);
INSERT INTO animals(pack_id,age,sex,is_healthy,power) VALUES(1,12,'M',true,9);
INSERT INTO animals(pack_id,age,sex,is_healthy,power) VALUES(1,4,'F',true,5);
INSERT INTO animals(pack_id,age,sex,is_healthy,power) VALUES(1,6,'M',false,6);
INSERT INTO movings(pack_id,location_id,speed) VALUES( 1,1,5);
INSERT INTO local_weather(location_id,weather_id,intensity) VALUES(2,1,80);
INSERT INTO animal_emotion(animal_id,emotion_id,triger,intensity) VALUES(1,3,'hunger',90);
INSERT INTO animal_emotion(animal_id,emotion_id,triger,intensity) VALUES(2,1,'animal 1',70);
INSERT INTO animal_emotion(animal_id,emotion_id,triger,intensity) VALUES(3,1,'animal 1',70);
INSERT INTO animal_emotion(animal_id,emotion_id,triger,intensity) VALUES(4,1,'animal 1',70);
INSERT INTO animal_emotion(animal_id,emotion_id,triger,intensity) VALUES(1,2,'lack of food',100);


CREATE TABLE local_weather_extended AS 
SELECT l.location_id, w.weather_id, l.name AS loc_name, w.name AS weather_name, w.is_dangerous, c.intensity
FROM weather w JOIN local_weather c ON w.weather_id=c.weather_id
JOIN locations l ON l.location_id=c.location_id;

ALTER TABLE local_weather_extended ADD COLUMN id SERIAL PRIMARY KEY;


CREATE OR REPLACE FUNCTION check_health()
RETURNS TRIGGER AS $$ 
DECLARE
    health boolean;
    emotion boolean;
BEGIN
    SELECT is_healthy INTO health FROM animals WHERE animal_id=NEW.animal_id;
    SELECT is_possitive INTO emotion FROM emotions WHERE emotion_id=NEW.emotion_id;

    IF NOT health AND emotion THEN
        RAISE EXCEPTION 'You cant set possitive emotion to a unhealthy animal';
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;


CREATE TRIGGER trigger_delete_if_unhealthy
    BEFORE UPDATE ON animals
    FOR EACH ROW
    EXECUTE FUNCTION delete_if_unhealthy();

ALTER TABLE animals ALTER COLUMN new_c TYPE integer;


CREATE OR REPLACE FUNCTION delete_if_unhealthy()
RETURNS TRIGGER AS $$
DECLARE 
BEGIN

    IF (NEW.is_healthy=false) THEN
    DELETE FROM animal_emotion 
    WHERE animal_emotion.animal_id=NEW.animal_id 
    AND emotion_id in (SELECT emotion_id FROM emotions WHERE is_possitive=true);
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;



CREATE TABLE users(
    id SERIAL PRIMARY KEY,
    login text,
    password text
);

CREATE TABLE coordinates(
    id SERIAL PRIMARY KEY,
    x BIGINT,
    y BIGINT
);

CREATE TABLE location(
    id SERIAL PRIMARY KEY,
    x REAL,
    y DOUBLE PRECISION,
    z DOUBLE PRECISION
);

CREATE TABLE person(
    id SERIAL PRIMARY KEY,
    name text,
    birthday text,
    eyeColor text,
    hairColor text,
    nationality text,
    location_id integer REFERENCES location(id)
);

CREATE TABLE labwork(
    id SERIAL PRIMARY KEY,
    name text,
    coordinates_id integer REFERENCES coordinates(id),
    creation_time text,
    minimal_points integer,
    difficulty text,
    person_id integer REFERENCES person(id),
    user_id integer REFERENCES users(id)
);

