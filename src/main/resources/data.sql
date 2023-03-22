-- ddl-auto: create-drop
-- ㄴSpring Boot 서버가 시작될 때마다 테이블들을 새로 만들기 때문에,
-- ㄴ편의를 위해 서버가 시작될 때마다 Data를 자동으로 DB에 넣어주는 기능 활용


-- 부트 서버가 시작될 때마다 실행할 쿼리문(자동실행): 비밀번호는 둘 다 admin
insert into user (username, password, nickname, activated) values ('admin', '$2a$08$lDnHPz7eUkSi6ao14Twuau08mzhWrL4kyZGGU5xfiGALO/Vxd5DOi', 'admin', 1);
insert into user (username, password, nickname, activated) values ('user', '$2a$08$lDnHPz7eUkSi6ao14Twuau08mzhWrL4kyZGGU5xfiGALO/Vxd5DOi', 'user', 1);

insert into authority (authority_name) values ('ROLE_USER');
insert into authority (authority_name) values ('ROLE_ADMIN');

insert into user_authority (user_id, authority_name) values (1, 'ROLE_USER');
insert into user_authority (user_id, authority_name) values (1, 'ROLE_ADMIN');
insert into user_authority (user_id, authority_name) values (2, 'ROLE_USER');