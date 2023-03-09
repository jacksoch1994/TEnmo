BEGIN TRANSACTION;

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
	, balance DECIMAL(1000,2) DEFAULT(1000.00)
	
	, CONSTRAINT fk_wallet_user FOREIGN KEY (user_id) REFERENCES tenmo_user(user_id)
	, CONSTRAINT ck_balance CHECK (balance >= 0)
);

CREATE TABLE money_transaction (
	transaction_id SERIAL NOT NULL PRIMARY KEY
	, sender_id INT NOT NULL 
	, receiver_id INT NOT NULL CHECK(receiver_id != sender_id)
	, status VARCHAR(8) NOT NULL DEFAULT('pending')
	, is_request BOOLEAN NOT NULL --not sure if we will keep this
	, amount DECIMAL(1000,2) NOT NULL
	, memo VARCHAR(250)
	, transaction_time TIMESTAMP DEFAULT(LOCALTIMESTAMP) NOT NULL
	
	, CONSTRAINT fk_sender_user FOREIGN KEY (sender_id) REFERENCES tenmo_user(user_id)
	, CONSTRAINT fk_receiver_user FOREIGN KEY (receiver_id) REFERENCES tenmo_user(user_id)
	, CONSTRAINT ck_amount CHECK (amount > 0)
	, CONSTRAINT ck_status CHECK (status IN ('pending', 'accepted', 'rejected'))	
);

COMMIT;
