SET autocommit=0;
SET unique_checks=0;
SET foreign_key_checks=0;

insert into tblTeam (id, number) values (1, 101);
insert into tblTeam (id, number) values (2, 101);
insert into tblTeam (id, number) values (3, 101);

insert into tblUser (id, number, firstname, lastname, username, team_id, password) values (1, 101, 'Kelly', 'Lewis', 'user1', 1, '1000:6a141086635a31296a28452b2b3fa407032c20e5886e14d7:21678a520d8f2e2abe044973568afd11118d073c1b9ca911');
insert into tblUser (id, number, firstname, lastname, username, team_id, password) values (2, 102, 'William', 'Edwards', 'user2', 1, '1000:6a141086635a31296a28452b2b3fa407032c20e5886e14d7:21678a520d8f2e2abe044973568afd11118d073c1b9ca911');
insert into tblUser (id, number, firstname, lastname, username, team_id, password) values (3, 103, 'Sharon', 'Howell', 'user3', 1, '1000:6a141086635a31296a28452b2b3fa407032c20e5886e14d7:21678a520d8f2e2abe044973568afd11118d073c1b9ca911');

insert into tblUser (id, number, firstname, lastname, username, team_id, password) values (4, 104, 'Roy', 'Rogers', 'user4', 2, '1000:6a141086635a31296a28452b2b3fa407032c20e5886e14d7:21678a520d8f2e2abe044973568afd11118d073c1b9ca911');
insert into tblUser (id, number, firstname, lastname, username, team_id, password) values (5, 105, 'Julia', 'Gonzales', 'user5', 2, '1000:6a141086635a31296a28452b2b3fa407032c20e5886e14d7:21678a520d8f2e2abe044973568afd11118d073c1b9ca911');
insert into tblUser (id, number, firstname, lastname, username, team_id, password) values (6, 106, 'Bert', 'Grunt', 'user6', 2, '1000:6a141086635a31296a28452b2b3fa407032c20e5886e14d7:21678a520d8f2e2abe044973568afd11118d073c1b9ca911');

insert into tblUser (id, number, firstname, lastname, username, team_id, password) values (7, 107, 'Fred', 'Tom', 'user7', 3, '1000:6a141086635a31296a28452b2b3fa407032c20e5886e14d7:21678a520d8f2e2abe044973568afd11118d073c1b9ca911');
insert into tblUser (id, number, firstname, lastname, username, team_id, password) values (8, 108, 'Golf', 'Boll', 'user8', 3, '1000:6a141086635a31296a28452b2b3fa407032c20e5886e14d7:21678a520d8f2e2abe044973568afd11118d073c1b9ca911');
insert into tblUser (id, number, firstname, lastname, username, team_id, password) values (9, 109, 'Grodan', 'G', 'user9', 3, '1000:6a141086635a31296a28452b2b3fa407032c20e5886e14d7:21678a520d8f2e2abe044973568afd11118d073c1b9ca911');


insert into tblWorkItem (id, number, description, status, issue_id) values (1, 101, 'Water the flowers', 'DONE', null);
insert into tblWorkItem (id, number, description, status, issue_id) values (2, 102, 'Dance with the elephants', 'IN_PROGRESS', null);
insert into tblWorkItem (id, number, description, status, issue_id) values (3, 103, 'Grow a beard', 'ON_BACKLOG', null);
insert into tblWorkItem (id, number, description, status, issue_id) values (4, 104, 'Chase rats', 'ON_BACKLOG', null);
insert into tblWorkItem (id, number, description, status, issue_id) values (5, 105, 'Finish ITS-Frontend', 'IN_PROGRESS', null);
insert into tblWorkItem (id, number, description, status, issue_id) values (6, 106, 'Plan vacation on bahamas', 'DONE', null);

insert into tblWorkItem (id, number, description, status, issue_id) values (7, 107, 'Travel to India', 'IN_PROGRESS', null);
insert into tblWorkItem (id, number, description, status, issue_id) values (8, 108, 'Crack coconut', 'DONE', null);
insert into tblWorkItem (id, number, description, status, issue_id) values (9, 109, 'Paint Picasso', 'ON_BACKLOG', null);
insert into tblWorkItem (id, number, description, status, issue_id) values (10, 110, 'Buy a lamp', 'IN_PROGRESS', null);
insert into tblWorkItem (id, number, description, status, issue_id) values (11, 111, 'Clean house', 'DONE', null);
insert into tblWorkItem (id, number, description, status, issue_id) values (12, 112, 'Plant Trees', 'ON_BACKLOG', null);

insert into tblWorkItem (id, number, description, status, issue_id) values (13, 113, 'Finish YHC3L', 'DONE', null);
insert into tblWorkItem (id, number, description, status, issue_id) values (14, 114, 'Write ITS-Backend', 'DONE', null);
insert into tblWorkItem (id, number, description, status, issue_id) values (15, 115, 'Rock Internship', 'IN_PROGRESS', null);
insert into tblWorkItem (id, number, description, status, issue_id) values (16, 116, 'Master Android', 'IN_PROGRESS', null);
insert into tblWorkItem (id, number, description, status, issue_id) values (17, 117, 'Get a job', 'ON_BACKLOG', null);
insert into tblWorkItem (id, number, description, status, issue_id) values (18, 118, 'Buy a house', 'ON_BACKLOG', null);

insert into tblUser_tblWorkItem (user_id, workItem_id) values (1, 1);
insert into tblUser_tblWorkItem (user_id, workItem_id) values (1, 2);
insert into tblUser_tblWorkItem (user_id, workItem_id) values (2, 3);
insert into tblUser_tblWorkItem (user_id, workItem_id) values (2, 4);
insert into tblUser_tblWorkItem (user_id, workItem_id) values (3, 5);
insert into tblUser_tblWorkItem (user_id, workItem_id) values (3, 6);
insert into tblUser_tblWorkItem (user_id, workItem_id) values (3, 1);
insert into tblUser_tblWorkItem (user_id, workItem_id) values (2, 2);

insert into tblUser_tblWorkItem (user_id, workItem_id) values (4, 7);
insert into tblUser_tblWorkItem (user_id, workItem_id) values (4, 8);
insert into tblUser_tblWorkItem (user_id, workItem_id) values (5, 9);
insert into tblUser_tblWorkItem (user_id, workItem_id) values (5, 10);
insert into tblUser_tblWorkItem (user_id, workItem_id) values (6, 11);
insert into tblUser_tblWorkItem (user_id, workItem_id) values (6, 12);
insert into tblUser_tblWorkItem (user_id, workItem_id) values (6, 7);
insert into tblUser_tblWorkItem (user_id, workItem_id) values (5, 8);

insert into tblUser_tblWorkItem (user_id, workItem_id) values (7, 13);
insert into tblUser_tblWorkItem (user_id, workItem_id) values (7, 14);
insert into tblUser_tblWorkItem (user_id, workItem_id) values (8, 15);
insert into tblUser_tblWorkItem (user_id, workItem_id) values (8, 16);
insert into tblUser_tblWorkItem (user_id, workItem_id) values (9, 17);
insert into tblUser_tblWorkItem (user_id, workItem_id) values (9, 18);
insert into tblUser_tblWorkItem (user_id, workItem_id) values (9, 13);
insert into tblUser_tblWorkItem (user_id, workItem_id) values (8, 14);

COMMIT;
SET autocommit=1;
SET unique_checks=1;
SET foreign_key_checks=1;