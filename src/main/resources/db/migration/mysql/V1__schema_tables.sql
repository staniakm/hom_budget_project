CREATE TABLE application_users (
  id int NOT NULL identity,
  email varchar(255) NOT NULL,
  password varchar(255) NOT NULL,
  enabled int not null default 0
  PRIMARY KEY (id)
);

CREATE TABLE verification_tokens (
  id int NOT NULL identity ,
  token VARCHAR(45) NULL,
  user_id INT NULL,
  expiry_date DATETIME NULL,
  PRIMARY KEY (id));

CREATE TABLE category (
  id int NOT NULL identity ,
  name varchar(255) NOT NULL,
  PRIMARY KEY (id));

CREATE TABLE expense (
  id int NOT NULL identity ,
  user_id int NULL,
  date DATETIME NULL DEFAULT GETDATE(),
  description VARCHAR(255) NULL,
  amount numeric(9,2) NULL DEFAULT 0,
  category_id int not null
  PRIMARY KEY (id),
  foreign key (category_id) references category(id));

CREATE TABLE income (
  id int NOT NULL identity,
  user_id int NULL,
  date DATETIME NULL DEFAULT GETDATE(),
  description VARCHAR(255) NULL,
  amount numeric(9,2) NULL DEFAULT 0,
  money_holder_id int not null,
  category_id int not null
  PRIMARY KEY (id),
    foreign key (category_id) references category(id));

  CREATE TABLE money_container (
  id int NOT NULL identity ,
  name varchar(45) ,
  amount numeric(9,2) DEFAULT '0.00',
  money_holder_id int not null,
  "type" varchar(45) NULL,
  user_id int NULL,
  PRIMARY KEY (id)
) ;

CREATE TABLE planned_operation (
  id int NOT NULL identity ,
  amount numeric(19,2) NOT NULL,
  days int NOT NULL,
  description varchar(255) null,
  due_date date NOT NULL,
  periodicity varchar(255) null,
  planed_type varchar(255) null,
  user_id int NOT NULL,
  money_holder_id int NOT NULL,
  active bit NOT NULL,
  finished bit NOT NULL,
  PRIMARY KEY (id),
  foreign key (user_id) references application_users (id)
);

CREATE TABLE planned_budget (
  id int NOT NULL identity ,
  planned numeric(19,2) DEFAULT NULL,
  spend numeric(19,2) DEFAULT NULL,
  category varchar(255) NULL,
  date date DEFAULT NULL,
  user_id int DEFAULT NULL,
  PRIMARY KEY (id),
  foreign key (user_id) references application_users (id)
);

CREATE TABLE investment (
  id int NOT NULL identity ,
  user_id int NOT NULL,
  start_date DATE NULL,
  amount numeric(9,2) NULL,
  percentage numeric(9,2) NULL,
  end_date DATE NULL,
  length_multiplier INT NULL,
  length VARCHAR(45) NULL,
  is_Active bit NOT NULL,
  PRIMARY KEY (id));

  CREATE TABLE currency (
  id int NOT NULL identity ,
  link VARCHAR(100) NOT NULL ,
  currency VARCHAR(45) NOT NULL,
  code VARCHAR(5) NOT NULL,
  date DATE NOT NULL,
  rate numeric(9,4) NOT NULL,
  last_update  DATETIME NULL DEFAULT GETDATE(),
  PRIMARY KEY (id));


CREATE TABLE user_currency (
  id int NOT NULL identity ,
  user_id int NOT NULL,
  currency_list varchar(1000) null,
  PRIMARY KEY (id));