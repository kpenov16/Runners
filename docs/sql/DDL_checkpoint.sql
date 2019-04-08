CREATE TABLE IF NOT EXISTS `checkpoint`(
	`run_id` VARCHAR(100),
	`waypoint_index` INT,
    `visited_timestamp` TIMESTAMP,
    PRIMARY KEY (`run_id`, `waypoint_index`, `visited_timestamp`), /*har vi overhoved bruge for primary key????*/
    FOREIGN KEY (`run_id`) REFERENCES run(`id`),
    FOREIGN KEY (`waypoint_index`) REFERENCES waypoint(`index`)    
);