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
SET distance = '8 km'
WHERE id = 2;

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
	DECLARE `current_time` timestamp default current_timestamp();
    
	INSERT INTO route_archive (id, description, distance, updated)
    VALUES (OLD.id, OLD.description, OLD.distance, current_time);
    
    /*Inserts all waypoints for the route to archive*/
    INSERT INTO waypoint_archive 
    SELECT *, current_time 
    FROM waypoint 
    WHERE route_id = OLD.id;
END//
DELIMITER ;
DROP TRIGGER AfterUpdateOnRoute;

CREATE TABLE route_archive(
	id INT,
    description VARCHAR(225),
    distance VARCHAR(225),
    updated TIMESTAMP NOT NULL,
    primary key(id, updated)
);
SELECT * FROM route_archive;
SELECT * FROM waypoint_archive;
SELECT * FROM waypoint_archive where route_id = 2 and updated = '2019-04-03 08:32:31'; /*Selects ony waypoints for the same version of route*/

Delete from route_archive;

CREATE TABLE waypoint_archive(
	id INT,
	route_id INT,
    point_index INT,
    coordinate VARCHAR(225),
    updated TIMESTAMP NOT NULL,
    FOREIGN KEY (route_id, updated) REFERENCES route_archive (id,updated)
); 
