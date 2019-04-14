USE s133967;
INSERT INTO `user` VALUES ('#user_id#','myusername', 'myemail','mypasword');
INSERT INTO `route` (`id`,`creator_id`) VALUES ('#route_id#','#user_id#');
INSERT INTO `route` (`id`,`creator_id`) VALUES ('#route_id2#','#user_id#');

INSERT INTO location VALUES ('#id#', '#street_name#', '#street_number#', '#city#', '#country#', ST_GeomFromText( 'POINT( 1 1 )', 0 ));
INSERT INTO location VALUES ('#id2#', '#street_name2#', '#street_number2#', '#city2#', '#country2#', ST_GeomFromText( 'POINT( 2 2 )', 0 ));

INSERT INTO location_route VALUES ('#id#', '#route_id#');
INSERT INTO location_route VALUES ('#id2#', '#route_id2#');


INSERT INTO `waypoint` VALUES 
( 1,'#route_id#', ST_GeomFromText( 'POINT( 1 1 )', 0 ) ),
( 2,'#route_id#', ST_GeomFromText( 'POINT( 2 2 )', 0 ) ),
( 3,'#route_id#', ST_GeomFromText( 'POINT( 3 3 )', 0 ) ),
( 4,'#route_id#', ST_GeomFromText( 'POINT( 4 4 )', 0 ) );
INSERT INTO run VALUES ('#run_id#','#route_id#', '#user_id#');

SELECT * FROM location_route LIMIT 5;

SELECT id, street_name, street_number, city, country, ST_X(spatial_point) AS X, ST_Y(spatial_point) AS Y
FROM location JOIN location_route 
ON location_route.location_id = location.id
WHERE location_route.route_id = '#route_id2#';

SELECT location_id
FROM location_route 
WHERE location_route.route_id = '#route_id#';

/*En bruger prøver angive sin position and check-ind*/
INSERT INTO `checkpoint` (`run_id`,`waypoint_index`,`visited_timestamp`)
SELECT `run`.`id`, `index`, now()
FROM `waypoint`
JOIN `run` ON `waypoint`.`route_id` = `run`.`route_id`
WHERE `run`.`id` = '#run_id#' AND                                          
ST_Distance(`spatial_point`,ST_GeomFromText( 'POINT(1 1)' )) <= 1;  /*hvis der angives en loc som ikke eksistere i waypoints, så tilføjes der ikke noget til `checkpoint`*/

/*query disitnct checkpoints with last visited timestamp */
SELECT `waypoint_index`, MAX(visited_timestamp) AS Last_visit
    FROM `checkpoint`  
    WHERE `run_id` = '#run_id#'
    GROUP BY `waypoint_index`;

/*get all not visited waypoints for the run*/
SELECT `index` FROM `waypoint` LEFT JOIN `run` 
ON `waypoint`.`route_id` = `run`.`route_id`
LEFT JOIN `checkpoint` ON `waypoint`.`index` = `checkpoint`.`waypoint_index`
WHERE `waypoint`.`route_id` = '#route_id#' AND
`visited_timestamp` IS null;

DELETE FROM `checkpoint`;
DELETE FROM `run`;
DELETE FROM `waypoint`;
DELETE FROM `route`;
DELETE FROM `user`;



SELECT st_x(`spatial_point`) FROM waypoint WHERE `index` = 1; 
SELECT * from `waypoint`;
SELECT * from `route`;
SELECT * from `user`;
SELECT * FROM `checkpoint`;
SELECT * FROM `run`;
SELECT * FROM `checkpoint` WHERE `run_id` = '#second_run_id#';
