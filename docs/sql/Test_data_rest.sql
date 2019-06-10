USE s133967;
INSERT INTO `user` VALUES ('1','myusername', 'myemail','mypasword');
INSERT INTO `route` (`id`,`title`,`creator_id`,`date`) VALUES ('10', 'Fun run', '1', now()+0);
INSERT INTO `route` (`id`,`title`,`creator_id`,`date`) VALUES ('11', 'Happy run', '1', now()+0);
INSERT INTO `route` (`id`,`title`,`creator_id`,`date`) VALUES ('12', 'Cultural run', '1', now()+0);
Select * from route;
INSERT INTO `waypoint` VALUES 
( 1,'10', ST_GeomFromText( 'POINT( 1 1 )', 0 ) ),
( 2,'10', ST_GeomFromText( 'POINT( 2 2 )', 0 ) ),
( 3,'10', ST_GeomFromText( 'POINT( 3 3 )', 0 ) ),
( 4,'10', ST_GeomFromText( 'POINT( 4 4 )', 0 ) );
INSERT INTO run VALUES ('101','10', '1');

Select * from route;
Delete from `user`;
Delete from `route`;


SELECT route.id AS id 
                     FROM route 
                     WHERE `date` >= 0 
                     ORDER BY `date` DESC 
                     LIMIT 1000 ;