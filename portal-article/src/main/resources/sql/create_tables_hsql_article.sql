CREATE TABLE article
(
   id int,
   created_at timestamp,
   created_by varchar(30),
   modified_at timestamp,
   modified_by varchar(30),
   content_id varchar(255),
   teaser varchar(10000),
   title varchar(255),
   PRIMARY KEY(id)
)
;
CREATE TABLE article_page
(
   content_id varchar(255) NOT NULL,
   page int NOT NULL,
   content varchar(10000),
   article_id int NOT NULL,
   PRIMARY KEY (content_id,page)
)
;
CREATE TABLE article_right_xref
(
   article_id int NOT NULL,
   right_id varchar(50) NOT NULL
)
;
CREATE TABLE article_tag
(
   tagname varchar(255),
   created_at timestamp,
   created_by varchar(30),
   modified_at timestamp,
   modified_by varchar(30),
   PRIMARY KEY(tagname)
)
;
CREATE TABLE article_tag_xref
(
   article_id int NOT NULL,
   tagname varchar(255) NOT NULL
)
;