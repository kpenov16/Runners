USE s133967;


/*##########################################
DDL Creating tables
############################################*/
CREATE TABLE route(
	id VARCHAR(225),
	creator_id VARCHAR(225),
    title VARCHAR(225),
    locations VARCHAR(225),
    date BIGINT,
    distance INT,
    duration BIGINT,
    description VARCHAR(500),
    status VARCHAR(225),
    PRIMARY KEY (id)
/*    FOREIGN KEY (creator_id) REFERENCES user_cdio(id)*/
);

SELECT * FROM route;
DROP TABLE route;


CREATE TABLE user_cdio(
	id VARCHAR(225),
    name VARCHAR(225),
    PRIMARY KEY(id)
);

DROP TABLE user_cdio;

INSERT INTO route (title, creator_id, locations, distance, duration, description, status)
VALUES ("testing", 2, "testing", 5000, 6454545451651651, "testing", "active");
SELECT * FROM route;
DROP TABLE route;

SELECT MAX(id) AS run_id FROM route WHERE creator_id = 1;

SELECT LAST_INSERT_ID();



/*#############################################
Test scripts
###############################################*/
SELECT * FROM route;
DELETE FROM route WHERE id = 9;