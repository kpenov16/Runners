/**********************************************
Creating the table
**********************************************/
CREATE TABLE IF NOT EXISTS `waypoint`(
	`index` INT,
	`route_id` VARCHAR(100),
    `spatial_point` POINT NOT NULL SRID 0,
    PRIMARY KEY (`index`, `route_id`),
    FOREIGN KEY (`route_id`) REFERENCES route(`id`),
    SPATIAL INDEX(`spatial_point`)
);