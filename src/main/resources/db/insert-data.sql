insert into users values(1, 'Andrew', 'Panas', '2021-04-22 20:26:00', 'panas.andrew@mail.ru', '$2y$12$XhjAX4fRULP4ZBJpYR/gMeOLkxsljJCBe52FDtA1xEOows.X4/1pu', 1);
insert into users values(2, 'Andrew2', 'Panas', '2021-04-22 20:27:00', 'sloydeath@mail.ru', '$2y$12$aJ0vAemYkbDYLXq92alOouVBPNLWb1ZK6rSPWFgCBNk.eacKwctM6', 1);
insert into users values(3, 'Andrew3', 'Panas', '2021-04-22 20:28:00', 'ds124fas@mail.ru', '$2y$12$tmT6j5B6rZTtF9CXQtcXQutnihppb56TaJoohdlHo6Ch/11KF5J5i', 1);
insert into users values(4, 'Andrew4', 'Panas', '2021-04-22 20:28:00', 'ds124fas@mail3.ru', '$2y$12$tmT3j5B6rZTtF9CXQtcXQutnihppb56TaJoohdlHo6Ch/11KF5J5i', 1);

insert into roles values('1', 'ADMIN');
insert into roles values('2', 'ANONYMOUS');
insert into roles values('3', 'TRADER');

insert into user_role values(1, 1);
insert into user_role values(2, 3);
insert into user_role values(3, 2);

insert into game_objects values(1, 'Some title', 'Some text', '2021-04-22 20:26:00', '2021-04-22 20:26:00', 1);
insert into game_objects values(2, 'Some title2', 'Some text2', '2021-04-22 20:25:00', '2021-04-22 20:26:00', 2);
insert into game_objects values(3, 'Some title3', 'Some text3', '2021-04-22 20:25:00', '2021-04-22 20:26:00', 2);

insert into comments values(1, 'comment to trader', '2021-04-22 20:25:01', '1', 2);
insert into comments values(2, 'Super-comment', '2021-04-22 20:25:02', '0', 2);
insert into comments values(3, 'the best trader', '2021-04-22 20:25:03', '1', 3);
insert into comments values(4, 'he is so cute', '2021-04-22 20:25:04', '1', 2);