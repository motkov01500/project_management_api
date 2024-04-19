CREATE TABLE role(
                     id SERIAL NOT NULL PRIMARY KEY,
                     name VARCHAR(30)
);

CREATE TABLE userr(
                      id SERIAL NOT NULL PRIMARY KEY,
                      username VARCHAR(200) NOT NULL,
                      password VARCHAR(500) NOT NULL,
                      full_name VARCHAR(120),
                      image_url VARCHAR,
                      role_id SERIAL
);

CREATE TABLE project(
                        id SERIAL NOT NULL PRIMARY KEY,
                        key VARCHAR(10) NOT NULL,
                        title VARCHAR(50)
);

CREATE TABLE task(
                     id SERIAL NOT NULL PRIMARY KEY,
                     progress INTEGER,
                     status VARCHAR,
                     initial_estimation INTEGER,
                     hours_spent INTEGER,
                     project_id SERIAL
);

CREATE TABLE meeting(
                        id SERIAL NOT NULL PRIMARY KEY,
                        date TIMESTAMP NOT NULL,
                        status VARCHAR,
                        project_id SERIAL
);

CREATE TABLE user_project(
                             userr_id SERIAL NOT NULL ,
                             project_id SERIAL NOT NULL
);

CREATE TABLE user_meeting(
    userr_id SERIAL NOT NULL,
    meeting_id SERIAL NOT NULL
);

ALTER TABLE userr
    ADD CONSTRAINT fk_userr_role FOREIGN KEY(role_id) REFERENCES role (id);

ALTER TABLE userr
    ADD CONSTRAINT uniq_userr_username UNIQUE(username);

ALTER TABLE project
    ADD CONSTRAINT uniq_project_key UNIQUE(key);

ALTER TABLE task
    ADD CONSTRAINT fk_task_project  FOREIGN KEY(project_id) REFERENCES project (id);

ALTER TABLE meeting
    ADD CONSTRAINT fk_meeting_project FOREIGN KEY(project_id) REFERENCES project(id);

ALTER TABLE user_project
    ADD CONSTRAINT fk_user_project_userr FOREIGN KEY(userr_id) REFERENCES userr(id);

ALTER TABLE user_project
    ADD CONSTRAINT fk_user_project_project FOREIGN KEY(project_id) REFERENCES project(id);

ALTER TABLE user_meeting
    ADD CONSTRAINT fk_user_meeting_user FOREIGN KEY (userr_id) REFERENCES userr(id);

ALTER TABLE user_meeting
    ADD CONSTRAINT fk_user_meeting_meeting FOREIGN KEY (meeting_id) REFERENCES meeting(id);