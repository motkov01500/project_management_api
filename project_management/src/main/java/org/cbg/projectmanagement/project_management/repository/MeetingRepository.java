package org.cbg.projectmanagement.project_management.repository;

import jakarta.ejb.Stateless;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.cbg.projectmanagement.project_management.entity.*;

import java.time.LocalDateTime;
import java.util.List;

@Stateless
public class MeetingRepository extends BaseRepository<Meeting> {

    public MeetingRepository() {
        super(Meeting.class);
    }

    public List<Meeting> getUpcomingMeetings(String key, int pageNumber, int offset) {
        CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        CriteriaQuery<Meeting> query = getCriteriaQuery();
        Root<Meeting> meetingRoot = query.from(Meeting.class);
        Join<Meeting, Project> meetingProjectJoin = meetingRoot.join(Meeting_.project);
        Predicate equalKey = criteriaBuilder.equal(meetingProjectJoin.get(Project_.key), key);
        Predicate checkDateIsGreaterThanMeetingStarting = criteriaBuilder.greaterThanOrEqualTo(
                meetingRoot.get(Meeting_.date), LocalDateTime.now());
        Predicate notDeletedMeetings = criteriaBuilder.notEqual(meetingRoot.get(Meeting_.isDeleted), true);
        query.select(meetingRoot)
                .where(criteriaBuilder.and(equalKey, checkDateIsGreaterThanMeetingStarting, notDeletedMeetings));
        if(pageNumber == 0 && offset == 0) {
            return getEntityByCriteria(query)
                    .getResultList();
        }
        return getEntityByCriteria(query)
                .setFirstResult((pageNumber-1) * 10)
                .setMaxResults(10)
                .getResultList();
    }

    public List<Meeting> getCurrentUserMeetings(String username, int pageNumber, int offset) {
        CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        CriteriaQuery<Meeting> query = getCriteriaQuery();
        Root<Meeting> meetingRoot = query.from(Meeting.class);
        Join<Meeting, User> meetingUserJoin = meetingRoot.join(Meeting_.users);
        Predicate equalUsername = criteriaBuilder.equal(meetingUserJoin.get(User_.username), username);
        Predicate notDeletedMeetings = criteriaBuilder.notEqual(meetingRoot.get(Meeting_.isDeleted), true);
        query.select(meetingRoot)
                .where(criteriaBuilder.and(equalUsername, notDeletedMeetings));
        if(pageNumber == 0 && offset == 0) {
            return getEntityByCriteria(query).getResultList();
        }
        return getEntityByCriteria(query)
                .setFirstResult((pageNumber-1) * offset)
                .setMaxResults(offset)
                .getResultList();
    }

    public boolean isUserInMeeting(Long meetingId, Long userId) {
        TypedQuery<Meeting> meetingTypedQuery = getEntityManager()
                .createQuery("FROM Meeting M JOIN M.users U " +
                        "WHERE U.id=:userId and M.id=:meetingId and M.isDeleted!=true", Meeting.class);
        meetingTypedQuery.setParameter("userId",userId);
        meetingTypedQuery.setParameter("meetingId",meetingId);
        return !meetingTypedQuery.getResultList().isEmpty();
    }

    public List<Meeting> getMeetingsRelatedToProjectWithPaging(String key, int pageNumber, int offset) {
        CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        CriteriaQuery<Meeting> query = getCriteriaQuery();
        Root<Meeting> meetingRoot = query.from(Meeting.class);
        Join<Meeting, Project> meetingProjectJoin = meetingRoot.join(Meeting_.project);
        Predicate notDeletedMeetings = criteriaBuilder.notEqual(meetingRoot.get(Meeting_.isDeleted), true);
        Predicate relatedToProject = criteriaBuilder.equal(meetingProjectJoin.get(Project_.key), key);
        query.select(meetingRoot)
                .where(criteriaBuilder.and(notDeletedMeetings, relatedToProject));
        if(pageNumber == 0 && offset == 0) {
            return getEntityByCriteria(query).getResultList();
        }
        return getEntityByCriteria(query)
                .setFirstResult((pageNumber-1) * offset)
                .setMaxResults(offset)
                .getResultList();
    }

    public List<Meeting> getMeetingsRelatedToProjectAndUser(String username, String key, int pageNumber, int offset) {
        CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        CriteriaQuery<Meeting> query = getCriteriaQuery();
        Root<Meeting> meetingRoot = query.from(Meeting.class);
        Join<Meeting, Project> meetingProjectJoin = meetingRoot.join(Meeting_.project);
        Join<Meeting, User> meetingUserJoin = meetingRoot.join(Meeting_.users);
        Predicate notDeletedMeetings = criteriaBuilder.notEqual(meetingRoot.get(Meeting_.isDeleted), true);
        Predicate getMeetingsToCurrentUser = criteriaBuilder.equal(meetingUserJoin.get(User_.username), username);
        Predicate getMeetingsToCurrentProject = criteriaBuilder.equal(meetingProjectJoin.get(Project_.key), key);
        query.select(meetingRoot)
                .where(criteriaBuilder.and(notDeletedMeetings, getMeetingsToCurrentProject
                        , getMeetingsToCurrentUser));
        if(pageNumber == 0 && offset == 0) {
            return getEntityByCriteria(query).getResultList();
        }
        return getEntityByCriteria(query)
                .setFirstResult((pageNumber-1) * offset)
                .setMaxResults(offset)
                .getResultList();
    }

    public List<Meeting> findAll(int pageNumber, int offset) {
        TypedQuery<Meeting> meetingTypedQuery = getEntityManager()
                .createQuery("FROM Meeting M WHERE M.isDeleted != true", Meeting.class);
        if(pageNumber == 0 && offset == 0) {
            return meetingTypedQuery.getResultList();
        }
        return meetingTypedQuery
                .setFirstResult((pageNumber - 1) * offset)
                .setMaxResults(offset)
                .getResultList();
    }

    public List<Meeting> findAllByUsername(String username) {
        CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        CriteriaQuery<Meeting> query = getCriteriaQuery();
        Root<Meeting> meetingRoot = query.from(Meeting.class);
        Join<Meeting, User> meetingUserJoin = meetingRoot.join(Meeting_.users);
        query.select(meetingRoot)
                .where(criteriaBuilder.equal(meetingUserJoin.get(User_.username),username));
        return getEntityByCriteria(query).getResultList();
    }

    public void deleteByProjectId(Long projectId) {
        TypedQuery<Meeting> meetingTypedQuery = getEntityManager().createQuery("FROM Meeting M JOIN M.project P WHERE M.isDeleted != true and P.id=:projectId", Meeting.class);
        meetingTypedQuery.setParameter("projectId", projectId);
        List<Meeting> meetings = meetingTypedQuery.getResultList();
        meetings.forEach(meeting -> {
            meeting.setIsDeleted(Boolean.TRUE);
            update(meeting);
        });
    }
}
