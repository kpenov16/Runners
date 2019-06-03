USE s133967;
INSERT INTO `user` VALUES ('1','myusername', 'myemail','mypasword');
INSERT INTO `route` (`id`,`title`,`creator_id`,`date`) VALUES ('10', 'Fun run', '1', now()+0);
INSERT INTO `route` (`id`,`title`,`creator_id`,`date`) VALUES ('11', 'Happy run', '1', now()+0);
INSERT INTO `route` (`id`,`title`,`creator_id`,`date`) VALUES ('12', 'Cultural run', '1', now()+0);

Delete from `user`;
Delete from `route`;