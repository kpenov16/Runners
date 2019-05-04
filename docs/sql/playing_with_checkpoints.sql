USE s133967;
INSERT INTO `user` VALUES ('#user_id#','myusername', 'myemail','mypasword');
INSERT INTO `route` (`id`,`creator_id`) VALUES ('#route_id#','#user_id#');
INSERT INTO `waypoint` VALUES 
( 1,'#route_id#', ST_GeomFromText( 'POINT( 1 1 )', 0 ) ),
( 2,'#route_id#', ST_GeomFromText( 'POINT( 2 2 )', 0 ) ),
( 3,'#route_id#', ST_GeomFromText( 'POINT( 3 3 )', 0 ) ),
( 4,'#route_id#', ST_GeomFromText( 'POINT( 4 4 )', 0 ) );
INSERT INTO run VALUES ('#run_id#','#route_id#', '#user_id#');

/*En bruger prøver angive sin position and check-ind*/
INSERT INTO checkpoint (run_id, waypoint_index, visited_timestamp)
                           SELECT run.id, `index`, now()
                           FROM waypoint
                           JOIN run ON waypoint.route_id = run.route_id
                          WHERE run.id = '#run_id#' AND 
                          ST_Distance(spatial_point, ST_GeomFromText( 'POINT(1 1)' )) <= 1;  /*hvis der angives en loc som ikke eksistere i waypoints, så tilføjes der ikke noget til `checkpoint`*/

/*query disitnct checkpoints with last visited timestamp */
SELECT `waypoint_index`, MAX(visited_timestamp) AS Last_visit
    FROM `checkpoint`  
    WHERE `run_id` = '#run_id#'
    GROUP BY `waypoint_index`;



/*get all not visited waypoints for the run*/
SELECT *  FROM `waypoint` LEFT JOIN `run` 
ON `waypoint`.`route_id` = `run`.`route_id`
LEFT JOIN `checkpoint` ON `waypoint`.`index` = `checkpoint`.`waypoint_index`
WHERE `run`.`id` = '#run_id#' AND 
`visited_timestamp` IS null;

DELETE FROM `checkpoint`;
DELETE FROM `run`;
DELETE FROM `waypoint`;
DELETE FROM location_user;
DELETE FROM location_route;
DELETE FROM locations;
DELETE FROM `route`;
DELETE FROM `user`;
SELECT * FROM `checkpoint`;


SELECT st_x(`spatial_point`) FROM waypoint WHERE `index` = 1; 
SELECT * from `waypoint`;
SELECT * from `route`;
SELECT * from `user`;
SELECT * FROM `checkpoint`;
SELECT * FROM `run`;
SELECT * FROM `checkpoint` WHERE `run_id` = '#second_run_id#';


SELECT ST_X(spatial_point) AS X, ST_Y(spatial_point) AS Y, `index` 
FROM waypoint LEFT JOIN run 
ON waypoint.route_id = run.route_id 
LEFT JOIN checkpoint ON waypoint.`index` = checkpoint.waypoint_index 
WHERE run.id = 'b00c5127-40d0-4d8e-a151-9d38ece55226' AND 
visited_timestamp IS NULL;


SELECT * 
FROM waypoint JOIN run 
ON waypoint.route_id = run.route_id
LEFT JOIN checkpoint ON waypoint.`index` = checkpoint.waypoint_index;


SELECT * 
FROM waypoint JOIN run 
ON waypoint.route_id = run.route_id
LEFT JOIN checkpoint ON waypoint.`index` = checkpoint.waypoint_index AND checkpoint.run_id = run.id;

SELECT *  FROM `waypoint` LEFT JOIN `run` 
ON `waypoint`.`route_id` = `run`.`route_id`
LEFT JOIN `checkpoint` ON `waypoint`.`index` = `checkpoint`.`waypoint_index`
WHERE `run`.`id` = '#run_id#' AND 
`visited_timestamp` IS null;



SELECT *  FROM `waypoint` LEFT JOIN `run` 
ON `waypoint`.`route_id` = `run`.`route_id`
LEFT JOIN `checkpoint` ON `waypoint`.`index` = `checkpoint`.`waypoint_index`
WHERE `run`.`id` = '#run_id#' AND 
`visited_timestamp` IS null;

SELECT * 
FROM waypoint JOIN run 
ON waypoint.route_id = run.route_id
LEFT JOIN checkpoint ON waypoint.`index` = checkpoint.waypoint_index AND checkpoint.run_id = run.id;

SELECT *
FROM waypoint JOIN run 
ON waypoint.route_id = run.route_id 
LEFT JOIN checkpoint
ON id = checkpoint.run_id AND waypoint.`index` = checkpoint.waypoint_index
AND run.id = 'fbdaccb6-ce0d-41b5-b740-3f17752e343a';