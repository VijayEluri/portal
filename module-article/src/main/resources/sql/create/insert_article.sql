INSERT INTO core_right (right_id,description,created_at,created_by,modified_at,modified_by) VALUES ('page.ArticleEditPage','Article Author: Edit articles',{ts '2009-01-10 21:48:33.000'},'admin',{ts '2009-01-10 21:48:33.000'},'admin');
INSERT INTO core_right (right_id,description,created_at,created_by,modified_at,modified_by) VALUES ('article.read','Article: Read as admin',{ts '2008-12-30 23:36:13.000'},'admin',{ts '2008-12-30 23:36:13.000'},'admin');
INSERT INTO core_right (right_id,description,created_at,created_by,modified_at,modified_by) VALUES ('article.read.guest','Article: Read as guest',{ts '2008-12-30 23:36:31.000'},'admin',{ts '2008-12-30 23:36:31.000'},'admin');
INSERT INTO core_right (right_id,description,created_at,created_by,modified_at,modified_by) VALUES ('article.read.preview','Article: Preview read',{ts '2008-12-30 23:51:18.000'},'admin',{ts '2008-12-30 23:51:18.000'},'admin');
INSERT INTO core_right (right_id,description,created_at,created_by,modified_at,modified_by) VALUES ('article.read.registered','Article: Read as registered user',{ts '2008-12-30 23:36:57.000'},'admin',{ts '2008-12-30 23:36:57.000'},'admin');
INSERT INTO core_right (right_id,description,created_at,created_by,modified_at,modified_by) VALUES ('article.view','Article: View as admin',{ts '2008-12-30 23:34:35.000'},'admin',{ts '2008-12-30 23:34:35.000'},'admin');
INSERT INTO core_right (right_id,description,created_at,created_by,modified_at,modified_by) VALUES ('article.view.guest','Article: View as guest',{ts '2008-12-30 23:35:00.000'},'admin',{ts '2008-12-30 23:35:00.000'},'admin');
INSERT INTO core_right (right_id,description,created_at,created_by,modified_at,modified_by) VALUES ('article.view.preview','Article: Preview teaser',{ts '2008-12-30 23:50:53.000'},'admin',{ts '2008-12-30 23:51:34.000'},'admin');
INSERT INTO core_right (right_id,description,created_at,created_by,modified_at,modified_by) VALUES ('article.view.registered','Article: View as registered user',{ts '2008-12-30 23:35:19.000'},'admin',{ts '2008-12-30 23:35:19.000'},'admin');
INSERT INTO core_right (right_id,description,created_at,created_by,modified_at,modified_by) VALUES ('general.ArticleBoxPanel','See Box: Latest articles box',{ts '2009-01-05 14:19:24.000'},'admin',{ts '2009-01-05 14:20:10.000'},'admin');
INSERT INTO core_right (right_id,description,created_at,created_by,modified_at,modified_by) VALUES ('page.ArticlePage','Article: See the articles',{ts '2009-01-05 23:33:57.000'},'admin',{ts '2009-01-10 21:26:54.000'},'admin');

INSERT INTO core_role_right_xref (role_id,right_id) VALUES (1,'article.read.preview');
INSERT INTO core_role_right_xref (role_id,right_id) VALUES (1,'article.view.preview');
INSERT INTO core_role_right_xref (role_id,right_id) VALUES (1,'article.read');
INSERT INTO core_role_right_xref (role_id,right_id) VALUES (1,'article.read.guest');
INSERT INTO core_role_right_xref (role_id,right_id) VALUES (1,'article.read.registered');
INSERT INTO core_role_right_xref (role_id,right_id) VALUES (1,'article.view');
INSERT INTO core_role_right_xref (role_id,right_id) VALUES (1,'article.view.guest');
INSERT INTO core_role_right_xref (role_id,right_id) VALUES (1,'article.view.registered');
INSERT INTO core_role_right_xref (role_id,right_id) VALUES (1,'general.ArticleBoxPanel');
INSERT INTO core_role_right_xref (role_id,right_id) VALUES (1,'page.ArticlePage');
INSERT INTO core_role_right_xref (role_id,right_id) VALUES (1,'page.ArticleEditPage');
INSERT INTO core_role_right_xref (role_id,right_id) VALUES (2,'article.read.guest');
INSERT INTO core_role_right_xref (role_id,right_id) VALUES (2,'article.view.guest');
INSERT INTO core_role_right_xref (role_id,right_id) VALUES (2,'general.ArticleBoxPanel');
INSERT INTO core_role_right_xref (role_id,right_id) VALUES (2,'page.ArticlePage');
INSERT INTO core_role_right_xref (role_id,right_id) VALUES (3,'article.view.guest');
INSERT INTO core_role_right_xref (role_id,right_id) VALUES (3,'article.view.registered');
INSERT INTO core_role_right_xref (role_id,right_id) VALUES (3,'article.read.guest');
INSERT INTO core_role_right_xref (role_id,right_id) VALUES (3,'article.read.registered');
INSERT INTO core_role_right_xref (role_id,right_id) VALUES (3,'general.ArticleBoxPanel');
INSERT INTO core_role_right_xref (role_id,right_id) VALUES (3,'page.ArticlePage');
INSERT INTO core_configuration (conf_key,conf_description,conf_group,conf_type,conf_value) VALUES ('articles_per_page','Articles per page','Articles','java.lang.Integer','5');
INSERT INTO core_configuration (conf_key,conf_description,conf_group,conf_type,conf_value) VALUES ('box_num_latest_articles','Number of latest articles','Articles','java.lang.Integer','3');
INSERT INTO core_box (id,created_at,created_by,modified_at,modified_by,box_type,content,sort,title,hide_title) VALUES (9,{ts '2009-01-05 12:17:49.000'},'admin',{ts '2009-01-05 12:18:07.000'},'admin','ArticleBoxPanel',null,7,'Latest Articles Box', 0);

INSERT INTO article (id,created_at,created_by,modified_at,modified_by,content_id,teaser,title) VALUES (1,{ts '2009-01-06 19:28:56.000'},'admin',{ts '2009-01-06 19:28:56.000'},'admin','Sample_article','<p>This is a sample article and this is the teaser</p>','Sample article');
INSERT INTO article_page (content_id,page,content,article_id) VALUES ('Sample_article',1,'<p>Some sample content on page 1.</p>',1);
INSERT INTO article_page (content_id,page,content,article_id) VALUES ('Sample_article',2,'<p>Some sample content on page 2.</p>',1);
INSERT INTO article_page (content_id,page,content,article_id) VALUES ('Sample_article',3,'<p>Some sample content on page 3.</p>',1);


INSERT INTO article_right_xref (article_id,right_id) VALUES (1,'article.view.guest');
INSERT INTO article_right_xref (article_id,right_id) VALUES (1,'article.view.registered');
INSERT INTO article_right_xref (article_id,right_id) VALUES (1,'article.view.preview');
INSERT INTO article_right_xref (article_id,right_id) VALUES (1,'article.read.preview');
INSERT INTO article_right_xref (article_id,right_id) VALUES (1,'article.read.guest');
INSERT INTO article_right_xref (article_id,right_id) VALUES (1,'article.read.registered');
INSERT INTO article_tag (tagname,created_at,created_by,modified_at,modified_by) VALUES ('sample',{ts '2009-01-06 19:28:56.000'},'admin',{ts '2009-01-06 19:28:56.000'},'admin');
INSERT INTO article_tag_xref (article_id,tagname) VALUES (1,'sample');

-- since 1.0-rc3
INSERT INTO core_configuration (conf_key,conf_description,conf_group,conf_type,conf_value) VALUES ('article_entries_in_feed','Article entries in feed','Articles','java.lang.Integer','10');
INSERT INTO core_configuration (conf_key,conf_description,conf_group,conf_type,conf_value) VALUES ('article_feed_title','Article feed title','Articles','java.lang.String','Articles');

