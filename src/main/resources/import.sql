insert into user (email, password, role, created_at) values ('hn', 'hn', 'USER', now());
insert into user (email, password, role, created_at) values ('hn2', 'hn2', 'ADMIN', now());
insert into user (email, password, role, created_at) values ('test@naver.com', '!@#QWEasdzxc', 'USER', now());
insert into board (title, content, view_count, is_delete, created_date, last_modified_date, user_id, origin, num, depth) values ('title', 'content', 0, false, now(), now(), 1, 1, 0, 0);
insert into comment (board_id, content, created_date, last_modified_date) values (1, 'comment-content', now(), now());