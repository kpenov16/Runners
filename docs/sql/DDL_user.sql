USE s133967;
CREATE TABLE user(
	id VARCHAR(225),
    user_name  VARCHAR(225) NOT NULL UNIQUE,
    email VARCHAR(225) NOT NULL UNIQUE,
    password VARCHAR(225),
    PRIMARY KEY (id,user_name,email)
);

INSERT INTO user values("2","user_2","email_2","pass");
UPDATE user SET user_name = "blaaaa" WHERE id="2";

