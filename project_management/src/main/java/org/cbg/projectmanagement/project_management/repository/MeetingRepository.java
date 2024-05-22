package org.cbg.projectmanagement.project_management.repository;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.cbg.projectmanagement.project_management.dto.Sort;
import org.cbg.projectmanagement.project_management.entity.*;
import org.cbg.projectmanagement.project_management.enums.SortOrder;
import org.cbg.projectmanagement.project_management.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

@Stateless
public class MeetingRepository extends BaseRepository<Meeting> {
    
    public MeetingRepository() {
        super(Meeting.class);
    }

    public List<Meeting> getCurrentUserMeetings(String username, int pageNumber, int offset, Sort sort) {
        CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        CriteriaQuery<Meeting> query = getCriteriaQuery();
        Root<Meeting> meetingRoot = query.from(Meeting.class);
        Join<Meeting, User> meetingUserJoin = meetingRoot.join(Meeting_.users);
        Predicate equalUsername = criteriaBuilder.equal(meetingUserJoin.get(User_.username), username);
        Predicate notDeletedMeetings = criteriaBuilder.notEqual(meetingRoot.get(Meeting_.isDeleted), true);
        query.select(meetingRoot)
                .where(criteriaBuilder.and(equalUsername, notDeletedMeetings));
        if (sort != null && !SortOrder.DEFAULT.equals(sort.getOrder())) {
            final Path<Object> sortColumn = meetingRoot.get(sort.getColumn());
            query.orderBy(SortOrder.ASCENDING.equals(sort.getOrder()) ?
                    criteriaBuilder.asc(sortColumn) :
                    criteriaBuilder.desc(sortColumn));
        }
        if (pageNumber == 0 && offset == 0) {
            return getEntityByCriteria(query).getResultList();
        }
        return getEntityByCriteria(query)
                .setFirstResult((pageNumber - 1) * offset)
                .setMaxResults(offset)
                .getResultList();
    }

    public boolean isUserInMeeting(Long meetingId, Long userId) {
        TypedQuery<Meeting> meetingTypedQuery = getEntityManager()
                .createQuery("FROM Meeting M JOIN M.users U " +
                        "WHERE U.id=:userId and M.id=:meetingId and M.isDeleted!=true", Meeting.class);
        meetingTypedQuery.setParameter("userId", userId);
        meetingTypedQuery.setParameter("meetingId", meetingId);
        return !meetingTypedQuery.getResultList().isEmpty();
    }

    public List<Meeting> getMeetingsRelatedToProjectWithPaging(String key, int pageNumber, int offset, Sort sort) {
        CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        CriteriaQuery<Meeting> query = getCriteriaQuery();
        Root<Meeting> meetingRoot = query.from(Meeting.class);
        Join<Meeting, Project> meetingProjectJoin = meetingRoot.join(Meeting_.project);
        Predicate notDeletedMeetings = criteriaBuilder.notEqual(meetingRoot.get(Meeting_.isDeleted), true);
        Predicate relatedToProject = criteriaBuilder.equal(meetingProjectJoin.get(Project_.key), key);
        query.select(meetingRoot)
                .where(criteriaBuilder.and(notDeletedMeetings, relatedToProject));
        if (sort != null && !SortOrder.DEFAULT.equals(sort.getOrder())) {
            final Path<Object> sortColumn = meetingRoot.get(sort.getColumn());
            query.orderBy(SortOrder.ASCENDING.equals(sort.getOrder()) ?
                    criteriaBuilder.asc(sortColumn) :
                    criteriaBuilder.desc(sortColumn));
        }
        if (pageNumber == 0 && offset == 0) {
            return getEntityByCriteria(query).getResultList();
        }
        return getEntityByCriteria(query)
                .setFirstResult((pageNumber - 1) * offset)
                .setMaxResults(offset)
                .getResultList();
    }

    public List<Meeting> getMeetingsRelatedToProjectAndUser(String username, String key, int pageNumber, int offset, Sort sort) {
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
        if (sort != null && !SortOrder.DEFAULT.equals(sort.getOrder())) {
            final Path<Object> sortColumn = meetingRoot.get(sort.getColumn());
            query.orderBy(SortOrder.ASCENDING.equals(sort.getOrder()) ?
                    criteriaBuilder.asc(sortColumn) :
                    criteriaBuilder.desc(sortColumn));
        }
        if (pageNumber == 0 && offset == 0) {
            return getEntityByCriteria(query).getResultList();
        }
        return getEntityByCriteria(query)
                .setFirstResult((pageNumber - 1) * offset)
                .setMaxResults(offset)
                .getResultList();
    }

    public List<Meeting> findRecentMeetingsToUser(String username, String projectKey) {
        CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        CriteriaQuery<Meeting> query = getCriteriaQuery();
        Root<Meeting> meetingRoot = query.from(Meeting.class);
        Join<Meeting, Project> meetingProjectJoin = meetingRoot.join(Meeting_.project);
        Join<Meeting, User> meetingUserJoin = meetingRoot.join(Meeting_.users);
        Predicate equalProject = criteriaBuilder.equal(meetingProjectJoin.get(Project_.key), projectKey);
        Predicate equalUsername = criteriaBuilder.equal(meetingUserJoin.get(User_.username), username);
        Predicate greaterOrEqualToLocalDate = criteriaBuilder.greaterThanOrEqualTo(meetingRoot.get(Meeting_.date), LocalDateTime.now());
        Predicate notDeletedMeetings = criteriaBuilder.notEqual(meetingRoot.get(Meeting_.isDeleted), true);
        query.select(meetingRoot)
                .where(criteriaBuilder.and(equalUsername, equalProject, notDeletedMeetings, greaterOrEqualToLocalDate))
                .orderBy(criteriaBuilder.asc(meetingRoot.get(Meeting_.date)));
        return getEntityByCriteria(query)
                .getResultList();

    }

    public List<Meeting> findAllByUsername(String username) {
        CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        CriteriaQuery<Meeting> query = getCriteriaQuery();
        Root<Meeting> meetingRoot = query.from(Meeting.class);
        Join<Meeting, User> meetingUserJoin = meetingRoot.join(Meeting_.users);
        query.select(meetingRoot)
                .where(criteriaBuilder.equal(meetingUserJoin.get(User_.username), username));
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
