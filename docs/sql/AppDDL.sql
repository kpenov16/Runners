USE s133967;

DROP TRIGGER IF EXISTS AfterUpdateOnRoute;

DROP TABLE IF EXISTS waypoint_archive;
DROP TABLE IF EXISTS route_archive;

DROP TABLE IF EXISTS location_user;
DROP TABLE IF EXISTS location_route;
DROP TABLE IF EXISTS checkpoint;
DROP TABLE IF EXISTS run;
DROP TABLE IF EXISTS waypoint;
DROP TABLE IF EXISTS route;
DROP TABLE IF EXISTS location;
DROP TABLE IF EXISTS `user`;

CREATE TABLE IF NOT EXISTS `user`(
	id VARCHAR(225),
    user_name  VARCHAR(225) NOT NULL UNIQUE,
    email VARCHAR(225) NOT NULL UNIQUE,
    `password` VARCHAR(225) NOT NULL,
    PRIMARY KEY (id, user_name, email)
);

CREATE TABLE IF NOT EXISTS route(
	id VARCHAR(225),
	creator_id VARCHAR(225),
    title VARCHAR(225),
    `date` BIGINT,
    distance INT,
    duration BIGINT,
    `description` VARCHAR(500),
    `status` VARCHAR(225),
    max_participants INT,
    min_participants INT,
    PRIMARY KEY (id),
    FOREIGN KEY (creator_id) REFERENCES `user` (id)
);

CREATE TABLE IF NOT EXISTS location(
	id VARCHAR(225),
    street_name VARCHAR(225),
    street_number VARCHAR(225),
    city VARCHAR(225),
    country VARCHAR(225),
    spatial_point POINT NOT NULL SRID 0,
    PRIMARY KEY (id)
); 

CREATE TABLE IF NOT EXISTS location_user(
	location_id VARCHAR(225),
    user_id VARCHAR(225),
    PRIMARY KEY (user_id),
    FOREIGN KEY (location_id ) REFERENCES location (id),
    FOREIGN KEY (user_id ) REFERENCES `user` (id)
); 

CREATE TABLE IF NOT EXISTS location_route(
	location_id VARCHAR(225),
    route_id VARCHAR(225),
    PRIMARY KEY (route_id),
    FOREIGN KEY (location_id ) REFERENCES location (id),
    FOREIGN KEY (route_id ) REFERENCES route (id)
); 

CREATE TABLE IF NOT EXISTS waypoint(
	`index` INT,
	route_id VARCHAR(100),
    spatial_point POINT NOT NULL SRID 0,
    PRIMARY KEY (`index`, route_id),
    FOREIGN KEY (route_id) REFERENCES route(id),
    SPATIAL INDEX (spatial_point)
);

CREATE TABLE run(
	id VARCHAR(225),
	route_id VARCHAR(225),
    user_id VARCHAR(225),
    PRIMARY KEY (id),
    FOREIGN KEY (route_id) REFERENCES route (id),
    FOREIGN KEY (user_id) REFERENCES `user` (id)
);

CREATE TABLE IF NOT EXISTS checkpoint(
	run_id VARCHAR(100),
	waypoint_index INT,
    visited_timestamp TIMESTAMP,
    PRIMARY KEY (run_id, waypoint_index, visited_timestamp), 
    FOREIGN KEY (run_id) REFERENCES run (id),
    FOREIGN KEY (waypoint_index) REFERENCES waypoint (`index`)    
);

CREATE TABLE IF NOT EXISTS route_archive(
	route_id VARCHAR(225),
	updated_timestamp TIMESTAMP,    
    creator_id VARCHAR(225),
    title VARCHAR(225),
    `date` BIGINT,
    distance INT,
    duration BIGINT,
    `description` VARCHAR(500),
    `status` VARCHAR(225),
    max_participants INT,
    min_participants INT,
   	location_id VARCHAR(225),
    location_street_name VARCHAR(225),
    location_street_number VARCHAR(225),
    location_city VARCHAR(225),
    location_country VARCHAR(225),
    location_spatial_point POINT NOT NULL SRID 0,
    PRIMARY KEY (route_id, updated_timestamp)
);

CREATE TABLE IF NOT EXISTS waypoint_archive(
	waypoint_index INT,
	route_id VARCHAR(100),
    spatial_point POINT NOT NULL SRID 0,
   	updated_timestamp TIMESTAMP,    
    PRIMARY KEY (waypoint_index, route_id, updated_timestamp),
    FOREIGN KEY (route_id, updated_timestamp) REFERENCES route_archive (route_id, updated_timestamp),
    SPATIAL INDEX (spatial_point)
);

SELECT * FROM waypoint_archive;
SELECT * FROM route_archive;

DELIMITER //
CREATE TRIGGER AfterUpdateOnRoute
AFTER UPDATE ON route
FOR EACH ROW
BEGIN
	DECLARE `current_time` timestamp default current_timestamp();
    
	INSERT INTO route_archive (route_id, updated_timestamp, creator_id, title, `date`, 
							   distance, duration,`description`, `status`, max_participants, min_participants,
                               location_id, location_street_name, location_street_number, location_city, location_country, location_spatial_point)
			SELECT OLD.id, `current_time`, OLD.creator_id, OLD.title, OLD.`date`, 
			OLD.distance, OLD.duration, OLD.`description`, OLD.`status`, OLD.max_participants, OLD.min_participants,
            
            location.id, location.street_name, location.street_number, location.city, location.country, location.spatial_point
			FROM location_route JOIN location ON location_route.location_id = location.id
			WHERE location_route.route_id = OLD.id;
    
    INSERT INTO waypoint_archive
    SELECT `index`, route_id, spatial_point, `current_time` 
    FROM waypoint 
    WHERE waypoint.route_id = OLD.id;
END//
DELIMITER ;
    

/*
CREATE TABLE IF NOT EXISTS waypoint(
	`index` INT,
	route_id VARCHAR(100),
    spatial_point POINT NOT NULL SRID 0,
    PRIMARY KEY (`index`, route_id),
    FOREIGN KEY (route_id) REFERENCES route(id),
    SPATIAL INDEX (spatial_point)
);
*/

SELECT COUNT(*) FROM user;

SELECT COUNT(*) FROM route; 

SELECT COUNT(*) FROM run;
 
SELECT COUNT(run.route_id), run.route_id, route.`date`
FROM run JOIN route ON run.route_id = route.id
WHERE route.`date` <= 1555663568963
GROUP BY run.route_id
ORDER BY COUNT(run.route_id) DESC
LIMIT 3;

SELECT * FROM route; 

 