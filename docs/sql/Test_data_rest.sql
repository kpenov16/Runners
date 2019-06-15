USE s133967;
INSERT INTO `user` VALUES ('1','user', 'myemail','pass');
INSERT INTO `route` (`id`,`title`,`creator_id`,`date`, `max_participants`) VALUES ('10', 'Fun run', '1', now()+0, 10);
INSERT INTO `route` (`id`,`title`,`creator_id`,`date`, `max_participants`) VALUES ('11', 'Happy run', '1', now()+0, 10);
INSERT INTO `route` (`id`,`title`,`creator_id`,`date`, `max_participants`) VALUES ('12', 'Cultural run', '1', now()+0, 10);
SHOW PROCESSLIST;
INSERT INTO `waypoint` VALUES 
( 1,'10', ST_GeomFromText( 'POINT( 1 1 )', 0 ) ),
( 2,'10', ST_GeomFromText( 'POINT( 2 2 )', 0 ) ),
( 3,'10', ST_GeomFromText( 'POINT( 3 3 )', 0 ) ),
( 4,'10', ST_GeomFromText( 'POINT( 4 4 )', 0 ) );
INSERT INTO run VALUES ('101','10', '1', now(), now());

INSERT INTO checkpoint (run_id, waypoint_index, visited_timestamp)
                           SELECT run.id, `index`, now()
                           FROM waypoint
                           JOIN run ON waypoint.route_id = run.route_id
                          WHERE run.id = '101' AND 
                          ST_Distance(spatial_point, ST_GeomFromText( 'POINT(1 1)' )) <= 1;

Select * from route;
Delete from `user`;
Delete from `route`;
Select * from checkpoint;
Select * from run;
Select * from user;
Select `index`, title from waypoint join route on  route.id = waypoint.route_id;
Delete from run;
Delete from checkpoint;
Select * from route_location;
SELECT ST_X(spatial_point) AS X, ST_Y(spatial_point) AS Y, `index` 
FROM waypoint;
SELECT route.id AS id 
                     FROM route 
                     WHERE `date` >= 0 
                     ORDER BY `date` DESC 
                     LIMIT 1000 WHERE status != 'deleted' ;
                     
                     SELECT route.id, route.title, route.date, route.distance, route.duration, route.description, route.status, route.max_participants, 
                     route.min_participants, 
                     COUNT(run.route_id) AS participants_number 
                     FROM route LEFT JOIN run 
                     ON route.id = run.route_id 
                     WHERE route.id = "9df68841-a81c-43c3-afb2-de4ed7c3f989";
                     
SELECT route.id AS id, route.date AS date, route.title AS title " +
                     "FROM route " +
                     "WHERE `date` >= ? " +
                     "ORDER BY `date` DESC " +
                     "LIMIT ? 
                     
                     
                     
                     