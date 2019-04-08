
INSERT INTO `user` VALUES ('#user_id#','myusername', 'myemail','mypasword');
INSERT INTO `route` (`id`,`creator_id`) VALUES ('#route_id#','#user_id#');
INSERT INTO `waypoint` VALUES 
( 1,'#route_id#', ST_GeomFromText( 'POINT( 1 1 )', 0 ) ),
( 2,'#route_id#', ST_GeomFromText( 'POINT( 2 2 )', 0 ) ),
( 3,'#route_id#', ST_GeomFromText( 'POINT( 3 3 )', 0 ) ),
( 4,'#route_id#', ST_GeomFromText( 'POINT( 4 4 )', 0 ) );
INSERT INTO run VALUES ('#run_id#','#route_id#', '#user_id#');

/*En bruger prøver angive sin position and check-ind*/
INSERT INTO `checkpoint` (`run_id`,`waypoint_index`,`visited_timestamp`)
SELECT `run`.`id`, `index`, now()
FROM `waypoint`
JOIN `run` ON `waypoint`.`route_id` = `run`.`route_id`
WHERE `run`.`route_id` = '#route_id#' AND                                          /*  ? ? i stedet af 2 2   i JBDC*/
ST_Distance(`spatial_point`,ST_GeomFromText( 'POINT(1 1)' )) <= 1;  /*hvis der angives en loc som ikke eksistere i waypoints, så tilføjes der ikke noget til `checkpoint`*/

SELECT * FROM `checkpoint`;

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


DELETE FROM `user`;
DELETE FROM `route`;
DELETE FROM `waypoint`;
