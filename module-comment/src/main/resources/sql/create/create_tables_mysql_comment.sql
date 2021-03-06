CREATE TABLE comments (
  id int(11) NOT NULL auto_increment,
  created_at datetime  not null,
  created_by varchar(30)  not null,
  modified_at datetime  not null,
  modified_by varchar(30)  not null,
  guest_name varchar(50) default NULL,
  guest_email varchar(50) default NULL,
  content text  not null,
  ip_address varchar(39)  not null,
  number_of_blames int(11) default 0,
  accepted bit(1)  not null,
  reviewed bit(1)  not null,
  automatic_blocked bit(1) not null,
  module_name varchar(20) not null,
  module_content_id varchar(20) not null,
  PRIMARY KEY  (id)
);

CREATE INDEX module_name_idx ON comments (module_name);
CREATE INDEX module_content_id_idx ON comments (module_content_id);
CREATE INDEX module_accepted_idx ON comments (accepted);
CREATE INDEX module_reviewed_idx ON comments (reviewed);
CREATE INDEX module_automatic_blocked_idx ON comments (automatic_blocked);
