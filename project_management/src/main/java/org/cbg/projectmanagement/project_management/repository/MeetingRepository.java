package org.cbg.projectmanagement.project_management.repository;

import jakarta.ejb.Stateless;
import jakarta.persistence.criteria.*;
import org.cbg.projectmanagement.project_management.entity.*;

import java.time.LocalDateTime;
import java.util.List;

@Stateless
public class MeetingRepository extends BaseRepository<Meeting> {

    public MeetingRepository() {
        super(Meeting.class);
    }

    public List<Meeting> getUpcomingMeetings(String key) {
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
        return getEntityByCriteriaa(query).getResultList();
    }

    public List<Meeting> getCurrentUserMeetings(String username) {
        CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        CriteriaQuery<Meeting> query = getCriteriaQuery();
        Root<Meeting> meetingRoot = query.from(Meeting.class);
        Join<Meeting, User> meetingUserJoin = meetingRoot.join(Meeting_.users);
        Predicate equalUsername = criteriaBuilder.equal(meetingUserJoin.get(User_.username), username);
        Predicate notDeletedMeetings = criteriaBuilder.notEqual(meetingRoot.get(Meeting_.isDeleted), true);
        query.select(meetingRoot)
                .where(criteriaBuilder.and(equalUsername, notDeletedMeetings));
        return getEntityByCriteriaa(query).getResultList();
    }

    public boolean isUserInMeeting(Long meetingId, Long userId) {
        CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        CriteriaQuery<Meeting> query = getCriteriaQuery();
        Root<Meeting> meetingRoot = query.from(Meeting.class);
        Join<Meeting, User> meetingUserRoot = meetingRoot.join(Meeting_.users);
        Predicate equalUsername = criteriaBuilder.equal(meetingUserRoot.get(User_.id), userId);
        Predicate predicate = criteriaBuilder.notEqual(meetingUserRoot.get(User_.isDeleted), true);
        query.select(meetingRoot)
                .where(criteriaBuilder.and(equalUsername,predicate));
        return !getEntityByCriteriaa(query).getResultList().isEmpty();
    }
//
//    public List<Meeting> findMeetingsToUserProjectsThatAreNotSigned(String username, String projectKey) {
//        CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
//        CriteriaQuery<Meeting> query = getCriteriaQuery();
//        Root<Meeting> meetingRoot = query.from(Meeting.class);
//        Join<Meeting, User> meetingUserJoin = meetingRoot.join(Meeting_.users);
//    }

    public List<Meeting> getMeetingsRelatedToProject(String key) {
        CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        CriteriaQuery<Meeting> query = getCriteriaQuery();
        Root<Meeting> meetingRoot = query.from(Meeting.class);
        Join<Meeting, Project> meetingProjectJoin = meetingRoot.join(Meeting_.project);
        Predicate notDeletedMeetings = criteriaBuilder.notEqual(meetingRoot.get(Meeting_.isDeleted), true);
        Predicate relatedToProject = criteriaBuilder.equal(meetingProjectJoin.get(Project_.key), key);
        query.select(meetingRoot)
                .where(criteriaBuilder.and(notDeletedMeetings, relatedToProject));
        return getEntityByCriteriaa(query).getResultList();
    }

    public List<Meeting> getMeetingsRelatedToProjectAndUser(String username, String key) {
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
        return getEntityByCriteriaa(query).getResultList();
    }

    public List<Meeting> findAll() {
        CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        CriteriaQuery<Meeting> query = getCriteriaQuery();
        Root<Meeting> meetingRoot = query.from(Meeting.class);
        query.select(meetingRoot)
                .where(criteriaBuilder.notEqual(meetingRoot.get(Meeting_.isDeleted), true));
        return getEntityByCriteriaa(query).getResultList();
    }

    public void deleteByProjectId(Long projectId) {
        CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        CriteriaQuery<Meeting> query = getCriteriaQuery();
        Root<Meeting> meetingRoot = query.from(Meeting.class);
        Join<Meeting, Project> meetingProjectJoin = meetingRoot.join(Meeting_.project);
        Predicate notDeletedMeetings = criteriaBuilder.notEqual(meetingRoot.get(Meeting_.isDeleted), true);
        Predicate meetingsRelatedToProject = criteriaBuilder
                .equal(meetingProjectJoin.get(Project_.id), projectId);
        query.select(meetingRoot)
                .where(criteriaBuilder.and(meetingsRelatedToProject, notDeletedMeetings));
        List<Meeting> meetings = getEntityByCriteriaa(query).getResultList();
        meetings.forEach(meeting -> {
            meeting.setIsDeleted(Boolean.TRUE);
            update(meeting);
        });
    }
}
