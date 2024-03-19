INSERT INTO role (name)
VALUES('administrator');
INSERT INTO role (name)
VALUES('user');

INSERT INTO userr (username,password,role_id)
VALUES('admin','$2a$12$D9x15f2upu7K4iimh1J5aOu7UhWp5zBmYdbMNZC/cVIMwTB9vPZxq',1);
INSERT INTO userr (username,password,role_id)
VALUES('liubaka','$2a$12$CL2uV0doRkH36KDpnlrBxuf0GDZNL/vmDVECMqb5il3PzhRlGkHyG',2);

INSERT INTO project (key,title)
VALUES('edno', 'mercedes');
INSERT INTO project (key,title)
VALUES('dve', 'porsche');

INSERT INTO meeting(date, status, project_id)
VALUES ('2004-10-19 10:23:54', 'END', 1);
INSERT INTO meeting(date, status, project_id)
VALUES ('2024-3-19 10:23:54', 'UPCOMING', 2);

INSERT INTO task(progress,status,initial_estimation,hours_spent,project_id,meeting_id)
VALUES(0,'opened', 20, 0, 1, 1);
INSERT INTO task(progress,status,initial_estimation,hours_spent,project_id,meeting_id)
VALUES(20, 'in-progress', 20, 5, 2, 2);

INSERT INTO user_project(userr_id, project_id)
VALUES(1,1);
INSERT INTO user_project(userr_id, project_id)
VALUES(2,1);
INSERT INTO user_project(userr_id, project_id)
VALUES(2,2);