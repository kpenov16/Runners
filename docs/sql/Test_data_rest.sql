USE s133967;
INSERT INTO `user` VALUES ('1','myusername', 'myemail','mypasword');
INSERT INTO `route` (`id`,`title`,`creator_id`,`date`) VALUES ('10', 'Fun run', '1', now()+0);
INSERT INTO `route` (`id`,`title`,`creator_id`,`date`) VALUES ('11', 'Happy run', '1', now()+0);
INSERT INTO `route` (`id`,`title`,`creator_id`,`date`) VALUES ('12', 'Cultural run', '1', now()+0);

INSERT INTO `waypoint` VALUES 
( 1,'10', ST_GeomFromText( 'POINT( 1 1 )', 0 ) ),
( 2,'10', ST_GeomFromText( 'POINT( 2 2 )', 0 ) ),
( 3,'10', ST_GeomFromText( 'POINT( 3 3 )', 0 ) ),
( 4,'10', ST_GeomFromText( 'POINT( 4 4 )', 0 ) );
INSERT INTO run VALUES ('#run_id111#','#route_id#', '#user_id#');

Select * from route;
Delete from `user`;
Delete from `route`;