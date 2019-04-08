CREATE TABLE run(
	id VARCHAR(225),
	route_id VARCHAR(225),
    user_id VARCHAR(225),
    PRIMARY KEY (id),
    FOREIGN KEY (route_id) REFERENCES route(id),
    FOREIGN KEY (user_id) REFERENCES user(id)
);