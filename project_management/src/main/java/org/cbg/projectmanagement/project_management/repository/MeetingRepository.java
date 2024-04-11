package org.cbg.projectmanagement.project_management.repository;

import jakarta.ejb.Stateless;
import jakarta.persistence.criteria.*;
import org.cbg.projectmanagement.project_management.entity.*;

import java.util.List;

@Stateless
public class MeetingRepository extends BaseRepository<Meeting> {

    public MeetingRepository() {
        super(Meeting.class);
    }

    public List<Meeting> getUnfinishedMeetings(String key) {
        CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        CriteriaQuery<Meeting> query = getCriteriaQuery();
        Root<Meeting> meetingRoot = query.from(Meeting.class);
        Join<Meeting, Project> meetingProjectJoin = meetingRoot.join(Meeting_.project);
        Predicate equalKey = criteriaBuilder.equal(meetingProjectJoin.get(Project_.key),key);
        Predicate notEqualStatus = criteriaBuilder.notEqual(meetingRoot.get(Meeting_.status),"END");
        query.select(meetingRoot)
                .where(criteriaBuilder.and(equalKey,notEqualStatus));
        return getEntityByCriteriaa(query).getResultList();
    }

    public List<Meeting> getCurrentUserMeetings(String username) {
        CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        CriteriaQuery<Meeting> query = getCriteriaQuery();
        Root<Meeting> meetingRoot = query.from(Meeting.class);
        Join<Meeting, User> meetingUserJoin = meetingRoot.join(Meeting_.users);
        query.select(meetingRoot)
                .where(criteriaBuilder.equal(meetingUserJoin.get(User_.username),username));
        return getEntityByCriteriaa(query).getResultList();
    }

    public List<Meeting> findAll() {
        CriteriaQuery<Meeting> query = getCriteriaQuery();
        Root<Meeting> meetingRoot = query.from(Meeting.class);
        query.select(meetingRoot);
        return getEntityByCriteriaa(query).getResultList();
    }
}
