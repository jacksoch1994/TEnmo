BEGIN TRANSACTION;

DROP TABLE IF EXISTS money_transaction;
DROP TABLE IF EXISTS user_wallet;
DROP TABLE IF EXISTS tenmo_user;
DROP SEQUENCE IF EXISTS seq_user_id;

CREATE SEQUENCE seq_user_id
  INCREMENT BY 1
  START WITH 1001
  NO MAXVALUE;

CREATE TABLE tenmo_user (
	user_id int NOT NULL DEFAULT nextval('seq_user_id'),
	username varchar(50) UNIQUE NOT NULL,
	password_hash varchar(200) NOT NULL,
	role varchar(20),
	CONSTRAINT PK_tenmo_user PRIMARY KEY (user_id),
	CONSTRAINT UQ_username UNIQUE (username)
);

CREATE TABLE user_wallet (
	wallet_id SERIAL NOT NULL PRIMARY KEY
	, user_id INT NOT NULL 
	, balance MONEY DEFAULT(1000.00)
	
	, CONSTRAINT fk_wallet_user FOREIGN KEY (user_id) REFERENCES tenmo_user(user_id)
	, CONSTRAINT ck_balance CHECK (balance > money(0.00))
);

CREATE TABLE money_transaction (
	transaction_id SERIAL NOT NULL PRIMARY KEY
	, sender_id INT NOT NULL 
	, receiver_id INT NOT NULL CHECK(receiver_id != sender_id)
	, status VARCHAR(8) NOT NULL DEFAULT('pending')
	, is_request BOOLEAN NOT NULL --not sure if we will keep this
	, amount MONEY NOT NULL
	, memo VARCHAR(250)
	, transaction_time TIMESTAMP DEFAULT(LOCALTIMESTAMP) NOT NULL
	
	, CONSTRAINT fk_sender_user FOREIGN KEY (sender_id) REFERENCES tenmo_user(user_id)
	, CONSTRAINT fk_receiver_user FOREIGN KEY (receiver_id) REFERENCES tenmo_user(user_id)
	, CONSTRAINT ck_amount CHECK (amount > money(0.00))
	, CONSTRAINT ck_status CHECK (status IN ('pending', 'accepted', 'rejected'))	
);

INSERT INTO tenmo_user (username,password_hash,role) VALUES ('user1','user1','ROLE_USER'); -- 1001
INSERT INTO tenmo_user (username,password_hash,role) VALUES ('user2','user2','ROLE_USER'); -- 1002
INSERT INTO tenmo_user (username,password_hash,role) VALUES ('user3','user3','ROLE_USER');

COMMIT TRANSACTION;
