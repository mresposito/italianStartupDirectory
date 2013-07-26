# --- !Ups

CREATE TABLE login(
  id BIGINT(20) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  
  login_type VARCHAR(10),

  deleted int DEFAULT 0
);

CREATE TABLE entity(
  id BIGINT(20) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(500) NOT NULL,
  surname VARCHAR(50),
  email VARCHAR(100),
  category VARCHAR(50),
  page_url VARCHAR(100),
  
  web_page VARCHAR(100),
  github_url VARCHAR(100),
  fb_url VARCHAR(100),
  g_plus_url VARCHAR(100),
  description VARCHAR(1000),

  created_by BIGINT(20) NOT NULL,
  contact_me int DEFAULT 1,
  email_me int DEFAULT 1,
  deleted int DEFAULT 0
);
-- 
-- CREATE TABLE person(
--   id BIGINT(20) NOT NULL AUTO_INCREMENT PRIMARY KEY,
--   entity_id BIGINT(20) NOT NULL,
--   deleted int DEFAULT 0
-- );

-- CREATE INDEX person_id ON person(id);
CREATE INDEX entity_id ON entity(id);
CREATE INDEX login_id ON login(id);

# --- !Downs

-- DROP TABLE person;
DROP TABLE entity;
DROP TABLE login;
