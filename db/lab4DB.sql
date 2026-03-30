--первое задание
SELECT Н_ОЦЕНКИ.ПРИМЕЧАНИЕ, Н_ВЕДОМОСТИ.ДАТА FROM Н_ОЦЕНКИ
LEFT JOIN Н_ВЕДОМОСТИ ON Н_ОЦЕНКИ.КОД=Н_ВЕДОМОСТИ.ОЦЕНКА
WHERE  Н_ОЦЕНКИ.КОД > 'осв'
AND Н_ВЕДОМОСТИ.ЧЛВК_ИД > 105590;

--рабочий
SELECT Н_ОЦЕНКИ.ПРИМЕЧАНИЕ, Н_ВЕДОМОСТИ.ДАТА FROM Н_ОЦЕНКИ
LEFT JOIN Н_ВЕДОМОСТИ ON Н_ОЦЕНКИ.КОД=Н_ВЕДОМОСТИ.ОЦЕНКА
WHERE  Н_ОЦЕНКИ.КОД < '5'
AND Н_ВЕДОМОСТИ.ЧЛВК_ИД > 105590;

CREATE INDEX ИНД_ОЦЕНКИ_КОД ON Н_ОЦЕНКИ USING BTREE(КОД);
CREATE INDEX ИНД_ЧЛВКИД ON Н_ВЕДОМОСТИ USING BTREE(ЧЛВК_ИД);
CREATE INDEX ИНД_ВЕДОМОСТИ_ОЦЕНКА ON Н_ВЕДОМОСТИ USING HASH(ОЦЕНКА);


--второе задание
SELECT Н_ЛЮДИ.ИД, Н_ВЕДОМОСТИ.ДАТА, Н_СЕССИЯ.ИД FROM Н_ЛЮДИ
LEFT JOIN Н_ВЕДОМОСТИ ON Н_ЛЮДИ.ИД=Н_ВЕДОМОСТИ.ЧЛВК_ИД
LEFT JOIN Н_СЕССИЯ ON Н_ЛЮДИ.ИД=Н_СЕССИЯ.ЧЛВК_ИД
WHERE Н_ЛЮДИ.ОТЧЕСТВО < 'Александрович'
AND Н_ВЕДОМОСТИ.ДАТА < '2010-06-18';

CREATE INDEX ИНД_ЛЮДИ_ИД ON Н_ЛЮДИ USING HASH(ИД);
CREATE INDEX ИНД_ВЕДОМОСТИ_ЧЛВКИД ON Н_ВЕДОМОСТИ USING HASH(ЧЛВК_ИД);
CREATE INDEX ИНД_СЕССИЯ_ЧЛВКИД ON Н_СЕССИЯ USING HASH(ЧЛВК_ИД);
CREATE INDEX ИНД_ЛЮДИ_ОТЧЕСТВО ON Н_ЛЮДИ USING BTREE(ОТЧЕСТВО);
CREATE INDEX "ИНД_ЛЮДИ_ОТЧЕСТВО" ON "Н_ЛЮДИ" USING BTREE("ОТЧЕСТВО");
CREATE INDEX ИНД_ВЕДОМОСТИ_ДАТА ON Н_ВЕДОМОСТИ USING BTREE(ДАТА);

--индекс для studs
--если нужно быстро брать животных, у которых сила больше 8 и здоровье тру
SELECT a.animal_id,p.pack_name,a.sex,a.power FROM animals a
JOIN pack p ON p.pack_id=a.pack_id
WHERE a.power BETWEEN 8 AND 10
AND a.is_healthy=true
ORDER BY a.power;

CREATE INDEX ind_animals_powerHealth ON animals(power,is_healthy,pack_id);
DROP INDEX ind_animals_powerHealth;


--создание бд

INSERT INTO pack(pack_name,number_of_animals) VALUES('wolfs', 4);
INSERT INTO pack(pack_name,number_of_animals) VALUES('tigers', 5);
INSERT INTO pack(pack_name,number_of_animals) VALUES('capybaras', 3);
INSERT INTO animals(pack_id,age,sex,is_healthy,power) VALUES(1,10,'F',false,3);
INSERT INTO animals(pack_id,age,sex,is_healthy,power) VALUES(1,6,'M',true,8);
INSERT INTO animals(pack_id,age,sex,is_healthy,power) VALUES(1,12,'M',true,6);
INSERT INTO animals(pack_id,age,sex,is_healthy,power) VALUES(1,3,'F',true,9);
INSERT INTO animals(pack_id,age,sex,is_healthy,power) VALUES(2,4,'F',true,5);
INSERT INTO animals(pack_id,age,sex,is_healthy,power) VALUES(2,6,'M',false,4);
INSERT INTO animals(pack_id,age,sex,is_healthy,power) VALUES(2,14,'M',false,2);
INSERT INTO animals(pack_id,age,sex,is_healthy,power) VALUES(2,8,'F',true,10);
INSERT INTO animals(pack_id,age,sex,is_healthy,power) VALUES(2,10,'M',true,8);
INSERT INTO animals(pack_id,age,sex,is_healthy,power) VALUES(3,2,'M',true,10);
INSERT INTO animals(pack_id,age,sex,is_healthy,power) VALUES(3,4,'F',true,9);
INSERT INTO animals(pack_id,age,sex,is_healthy,power) VALUES(3,5,'M',true,10);