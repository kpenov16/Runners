--read and run selective to make it work

CREATE TABLE route(
	id INT NOT NULL,
    description VARCHAR(225),
    distance VARCHAR(225),
    PRIMARY KEY (id)
);

INSERT INTO route (id, description, distance) VALUES (1, 'fast run', '5 km');
INSERT INTO route (id, description, distance) VALUES (2, 'slow run', '15 km');
INSERT INTO route (id, description, distance) VALUES (3, 'moderate run', '70 km');
SELECT * FROM route;

UPDATE route
SET distance = '7 km'
WHERE id = 3;

SELECT * FROM route_archive;


CREATE TABLE waypoint(
	id INT NOT NULL,
	route_id INT NOT NULL,
    point_index INT NOT NULL,
    coordinate VARCHAR(225),
    PRIMARY KEY (id, route_id, point_index),
    FOREIGN KEY (route_id) REFERENCES route (id)
); 

INSERT INTO waypoint (id, route_id, point_index, coordinate) VALUES (1, 1, 1, '5,5');
INSERT INTO waypoint (id, route_id, point_index, coordinate) VALUES (2, 1, 2, '10,10');

INSERT INTO waypoint (id, route_id, point_index, coordinate) VALUES (3, 2, 1, '5,5');
INSERT INTO waypoint (id, route_id, point_index, coordinate) VALUES (4, 2, 2, '7,7');
INSERT INTO waypoint (id, route_id, point_index, coordinate) VALUES (5, 2, 3, '10,10');

INSERT INTO waypoint (id, route_id, point_index, coordinate) VALUES (6, 3, 1, '10,10');
INSERT INTO waypoint (id, route_id, point_index, coordinate) VALUES (7, 3, 2, '20,20');

SELECT * FROM waypoint;

DELIMITER //
CREATE TRIGGER AfterUpdateOnRoute
AFTER UPDATE ON route
FOR EACH ROW
BEGIN
	INSERT INTO route_archive (id, description, distance, updated)
    VALUES (OLD.id, OLD.description, OLD.distance, CURRENT_TIMESTAMP());
END//
DELIMITER ;

CREATE TABLE route_archive(
	id INT,
    description VARCHAR(225),
    distance VARCHAR(225),
    updated TIMESTAMP NOT NULL
);

CREATE TABLE waypoint_archive(
	id INT,
	route_id INT,
    point_index INT,
    coordinate VARCHAR(225),
    updated TIMESTAMP NOT NULL
); 
