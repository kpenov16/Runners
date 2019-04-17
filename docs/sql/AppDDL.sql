USE s133967;

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


