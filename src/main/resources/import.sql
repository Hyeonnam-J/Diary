insert into user (email, password, role, created_date) values ('hn', 'hn', 'USER', now());
insert into user (email, password, role, created_date) values ('hn2', 'hn2', 'ADMIN', now());
insert into user (email, password, role, created_date) values ('test@naver.com', '!@#QWEasdzxc', 'USER', now());
insert into post (title, content, view_count, is_delete, created_date, last_modified_date, user_id, origin, num, depth) values ('title1', 'content', 0, false, now(), now(), 1, 1, 0, 0);
insert into post (title, content, view_count, is_delete, created_date, last_modified_date, user_id, origin, num, depth) values ('title2', 'content', 0, false, now(), now(), 1, 2, 0, 0);
insert into comment (user_id, post_id, content, created_date, last_modified_date) values (1, 1, 'comment-content111', now(), now());
insert into comment (user_id, post_id, content, created_date, last_modified_date) values (2, 1, 'comment-content222', now(), now());