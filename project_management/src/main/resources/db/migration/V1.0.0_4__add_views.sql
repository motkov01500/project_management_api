CREATE VIEW project_user_task_meeting AS
SELECT
    ROW_NUMBER() OVER (
    ORDER BY
      P.id
    )  as id,
    U.username,
    P.id as project_id,
    P.key,
    P.title,
    (SELECT COUNT(T.id) FROM task T JOIN project PP ON T.project_id = PP.id JOIN user_task UT ON UT.task_id = T.id JOIN userr UU ON UU.id = UT.userr_id WHERE U.username = UU.username AND PP.key = P.key AND T.progress < 100) AS task_count,
    (SELECT M.date FROM meeting M JOIN project PP ON PP.id = M.project_id JOIN user_meeting UM ON UM.meeting_id = M.id WHERE UM.userr_id = U.id AND P.key = PP.key AND M.date > LOCALTIMESTAMP AT TIME ZONE 'UTC +3' GROUP BY M.date LIMIT 1) AS meeting_date,
    (SELECT M.title FROM meeting M JOIN project PP ON PP.id = M.project_id JOIN user_meeting UM ON UM.meeting_id = M.id WHERE  UM.userr_id = U.id AND PP.key = P.key AND M.date > LOCALTIMESTAMP AT TIME ZONE 'UTC +3' GROUP BY M.title LIMIT 1) AS meeting_title
    FROM project P
        FULL OUTER JOIN user_project UP ON UP.project_id = P.id
        FULL OUTER JOIN userr U ON UP.userr_id = U.id
WHERE P.is_deleted != true AND U.is_deleted !=true
GROUP BY P.id, U.id, U.username;