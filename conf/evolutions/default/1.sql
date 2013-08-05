# --- !Ups

CREATE TABLE entity (
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

CREATE INDEX entity_id ON entity(id);

# --- !Downs

DROP TABLE entity;
