package org.cbg.projectmanagement.project_management.repository;

import jakarta.ejb.Stateless;
import jakarta.persistence.criteria.*;
import org.cbg.projectmanagement.project_management.dto.Sort;
import org.cbg.projectmanagement.project_management.entity.UserTaskMeetingStatistics;
import org.cbg.projectmanagement.project_management.entity.UserTaskMeetingStatistics_;
import org.cbg.projectmanagement.project_management.enums.SortOrder;

import java.util.List;

@Stateless
public class UserTaskStatisticsRepository extends BaseRepository<UserTaskMeetingStatistics> {

    public UserTaskStatisticsRepository() {
        super(UserTaskMeetingStatistics.class);
    }

    public List<UserTaskMeetingStatistics> findAll(String username, int pageNumber, int offset, Sort sort) {
        CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        CriteriaQuery<UserTaskMeetingStatistics> criteriaQuery = getCriteriaQuery();
        Root<UserTaskMeetingStatistics> userTaskStatisticsRoot = criteriaQuery.from(UserTaskMeetingStatistics.class);
        Predicate user = criteriaBuilder.equal(userTaskStatisticsRoot.get(UserTaskMeetingStatistics_.username),username);
        criteriaQuery.select(userTaskStatisticsRoot)
                .where(criteriaBuilder.and(user));
        if (sort != null && !SortOrder.DEFAULT.equals(sort.getOrder())) {
            final Path<Object> sortColumn = userTaskStatisticsRoot.get(sort.getColumn());
            criteriaQuery.orderBy(SortOrder.ASCENDING.equals(sort.getOrder()) ?
                    criteriaBuilder.asc(sortColumn) :
                    criteriaBuilder.desc(sortColumn));
        }
        if (pageNumber == 0 && offset == 0) {
            return getEntityByCriteria(criteriaQuery).getResultList();
        }
        return getEntityByCriteria(criteriaQuery)
                .setFirstResult((pageNumber - 1) * offset)
                .setMaxResults(offset)
                .getResultList();
    }
}
