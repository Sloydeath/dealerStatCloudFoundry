create  type authority as enum ('ANONYMOUS', 'ADMIN', 'TRADER');

CREATE TABLE IF NOT EXISTS users (
                                     id BIGSERIAL primary key,
                                     first_name VARCHAR(30),
                                     last_name VARCHAR(30),
                                     created_at timestampTZ NOT NULL,
                                     email VARCHAR(40) NOT NULL UNIQUE,
                                     password VARCHAR(100) NOT NULL,
                                     active boolean NOT NULL
);

CREATE TABLE IF NOT EXISTS roles (
                                     id BIGSERIAL PRIMARY KEY,
                                     role authority
);

CREATE TABLE IF NOT EXISTS user_role (
                                         user_id BIGINT,
                                         role_id BIGINT,
                                         FOREIGN KEY (user_id) REFERENCES users (id),
                                         FOREIGN KEY (role_id) REFERENCES roles (id),
                                         CONSTRAINT user_role_pkey PRIMARY KEY (user_id, role_id)
);

CREATE TABLE IF NOT EXISTS comments (
                                        id BIGSERIAL primary key,
                                        message TEXT,
                                        created_at timestampTZ NOT NULL,
                                        approved BOOLEAN,
                                        trader_id_fk BIGINT NOT NULL,
                                        FOREIGN KEY (trader_id_fk) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS game_objects (
                                            id BIGSERIAL primary key,
                                            title VARCHAR(100),
                                            text TEXT,
                                            created_at timestampTZ NOT NULL,
                                            updated_at timestampTZ,
                                            trader_id_fk BIGINT,
                                            FOREIGN KEY (trader_id_fk) REFERENCES users (id)
);

insert into users(first_name, last_name, created_at, email, password, active) values('Andrew', 'Panas', '2021-04-22 20:26:00', 'panas.andrew@mail.ru', '$2y$12$XhjAX4fRULP4ZBJpYR/gMeOLkxsljJCBe52FDtA1xEOows.X4/1pu', true);
insert into users(first_name, last_name, created_at, email, password, active) values('Andrew2', 'Panas', '2021-04-22 20:27:00', 'sloydeath@mail.ru', '$2y$12$aJ0vAemYkbDYLXq92alOouVBPNLWb1ZK6rSPWFgCBNk.eacKwctM6', true);
insert into users(first_name, last_name, created_at, email, password, active) values('Andrew3', 'Panas', '2021-04-22 20:28:00', 'ds124fas@mail.ru', '$2y$12$tmT6j5B6rZTtF9CXQtcXQutnihppb56TaJoohdlHo6Ch/11KF5J5i', true);
insert into users(first_name, last_name, created_at, email, password, active) values('Andrew4', 'Panas', '2021-04-22 20:28:00', 'ds124fas@mail3.ru', '$2y$12$tmT3j5B6rZTtF9CXQtcXQutnihppb56TaJoohdlHo6Ch/11KF5J5i', false);

insert into roles values(1, 'ADMIN');
insert into roles values(2, 'ANONYMOUS');
insert into roles values(3, 'TRADER');

insert into user_role values(1, 1);
insert into user_role values(2, 3);
insert into user_role values(3, 2);

insert into game_objects(title, text, created_at, updated_at, trader_id_fk) values('Some title', 'Some text', '2021-04-22 20:26:00', '2021-04-22 20:26:00', 1);
insert into game_objects(title, text, created_at, updated_at, trader_id_fk) values('Some title2', 'Some text2', '2021-04-22 20:25:00', '2021-04-22 20:26:00', 2);
insert into game_objects(title, text, created_at, updated_at, trader_id_fk) values('Some title3', 'Some text3', '2021-04-22 20:25:00', '2021-04-22 20:26:00', 2);

insert into comments(message, created_at, approved, trader_id_fk) values('comment to trader', '2021-04-22 20:25:01', true, 2);
insert into comments(message, created_at, approved, trader_id_fk) values('Super-comment', '2021-04-22 20:25:02', false, 2);
insert into comments(message, created_at, approved, trader_id_fk) values('the best trader', '2021-04-22 20:25:03', true, 3);
insert into comments(message, created_at, approved, trader_id_fk) values('he is so cute', '2021-04-22 20:25:04', true, 2);

select * from roles;
select * from users;
select * from user_role;
select * from game_objects;
select * from comments;

select * from game_objects where trader_id_fk = 2;

select * from users where id = 2 ;

select users.id, first_name, last_name, email, count(comments.id) from users inner join comments on users.id = comments.trader_id_fk group by users.id;