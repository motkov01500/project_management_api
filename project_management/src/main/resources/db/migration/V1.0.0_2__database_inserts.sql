INSERT INTO role (name)
VALUES ('administrator');
INSERT INTO role (name)
VALUES ('user');

INSERT INTO userr (username, password, image_url, role_id)
VALUES ('admin', '$2a$12$D9x15f2upu7K4iimh1J5aOu7UhWp5zBmYdbMNZC/cVIMwTB9vPZxq', 'https://www.idahoagc.org/sites/default/files/default_images/default-medium.png', 1);
INSERT INTO userr (username, password,image_url, role_id)
VALUES ('liubaka', '$2a$12$QmfznLTYW0MNCZIcnh9db.taZpXwjvzEILb9eqSbMDOcX6dbnTzxu','https://www.idahoagc.org/sites/default/files/default_images/default-medium.png', 2);

INSERT INTO project (key, title)
VALUES ('edno', 'mercedes');
INSERT INTO project (key, title)
VALUES ('dve', 'porsche');

INSERT INTO meeting(date, title, duration, project_id)
VALUES ('2004-10-19 10:23:54', 'Daily meeting', 30, 1);
INSERT INTO meeting(date, title, duration, project_id)
VALUES ('2024-3-19 10:00:00', 'Docker Presentation', 20, 2);
INSERT INTO meeting(date, title, duration, project_id)
VALUES ('2024-5-15 10:00:00', 'Java Threads', 70, 2);

INSERT INTO task(progress, initial_estimation, hours_spent, project_id, task_status)
VALUES (0, 20, 0, 1, 'TODO');
INSERT INTO task(progress, initial_estimation, hours_spent, project_id, task_status)
VALUES (20, 20, 5, 2, 'TODO');

INSERT INTO user_task(userr_id, task_id)
VALUES (2, 1);
INSERT INTO user_task(userr_id, task_id)
VALUES (2, 2);

INSERT INTO user_project(userr_id, project_id)
VALUES (1, 1);
INSERT INTO user_project(userr_id, project_id)
VALUES (2, 1);
INSERT INTO user_project(userr_id, project_id)
VALUES (2, 2);

INSERT INTO user_meeting(userr_id, meeting_id)
VALUES (2, 1);
INSERT INTO user_meeting(userr_id, meeting_id)
VALUES (2, 2);