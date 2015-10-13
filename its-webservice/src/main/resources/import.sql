SET autocommit=0;
SET unique_checks=0;
SET foreign_key_checks=0;

insert into tblTeam (id, number) values (1, 101);

insert into tblUser (id, number, firstname, lastname, username, team_id, password) values (1, 101, 'Kelly', 'Lewis', 'klewis0', 1, '1000:6a141086635a31296a28452b2b3fa407032c20e5886e14d7:21678a520d8f2e2abe044973568afd11118d073c1b9ca911');
insert into tblUser (id, number, firstname, lastname, username, team_id, password) values (2, 102, 'William', 'Edwards', 'wedwards1', 1, '1000:6a141086635a31296a28452b2b3fa407032c20e5886e14d7:21678a520d8f2e2abe044973568afd11118d073c1b9ca911');
insert into tblUser (id, number, firstname, lastname, username, team_id, password) values (3, 103, 'Sharon', 'Howell', 'showell2', 1, '1000:6a141086635a31296a28452b2b3fa407032c20e5886e14d7:21678a520d8f2e2abe044973568afd11118d073c1b9ca911');
insert into tblUser (id, number, firstname, lastname, username, team_id, password) values (4, 104, 'Roy', 'Rogers', 'rrogers3', 1, '1000:6a141086635a31296a28452b2b3fa407032c20e5886e14d7:21678a520d8f2e2abe044973568afd11118d073c1b9ca911');
insert into tblUser (id, number, firstname, lastname, username, team_id, password) values (5, 105, 'Julia', 'Gonzales', 'jgonzales4', 1, '1000:6a141086635a31296a28452b2b3fa407032c20e5886e14d7:21678a520d8f2e2abe044973568afd11118d073c1b9ca911');


insert into tblWorkItem (id, number, description, status, issue_id) values (1, 101, 'Water the flowers', 'DONE', null);
insert into tblWorkItem (id, number, description, status, issue_id) values (2, 102, 'Dance with the elephants', 'IN_PROGRESS', null);
insert into tblWorkItem (id, number, description, status, issue_id) values (3, 103, 'Grow a beard', 'IN_PROGRESS', null);
insert into tblWorkItem (id, number, description, status, issue_id) values (4, 104, 'Chase rats', 'ON_BACKLOG', null);
insert into tblWorkItem (id, number, description, status, issue_id) values (5, 105, 'Finish ITS-Frontend', 'IN_PROGRESS', null);
insert into tblWorkItem (id, number, description, status, issue_id) values (6, 106, 'Plan vacation on bahamas', 'DONE', null);

insert into tblUser_tblWorkItem (user_id, workItem_id) values (1, 1);
insert into tblUser_tblWorkItem (user_id, workItem_id) values (2, 1);
insert into tblUser_tblWorkItem (user_id, workItem_id) values (3, 2);
insert into tblUser_tblWorkItem (user_id, workItem_id) values (2, 2);
insert into tblUser_tblWorkItem (user_id, workItem_id) values (5, 2);
insert into tblUser_tblWorkItem (user_id, workItem_id) values (5, 5);
insert into tblUser_tblWorkItem (user_id, workItem_id) values (5, 5);
insert into tblUser_tblWorkItem (user_id, workItem_id) values (4, 6);

COMMIT;
SET autocommit=1;
SET unique_checks=1;
SET foreign_key_checks=1;