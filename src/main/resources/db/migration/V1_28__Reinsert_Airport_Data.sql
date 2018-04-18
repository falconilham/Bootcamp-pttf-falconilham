CREATE TABLE new_airport as
SELECT * FROM airport WHERE 1 GROUP BY code;

DROP TABLE airport;

RENAME TABLE new_airport TO airport;