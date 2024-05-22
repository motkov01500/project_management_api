package org.cbg.projectmanagement.project_management.service;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.SecurityContext;
import org.cbg.projectmanagement.project_management.dto.Sort;
import org.cbg.projectmanagement.project_management.entity.UserTaskMeetingStatistics;
import org.cbg.projectmanagement.project_management.repository.UserTaskStatisticsRepository;

import java.util.List;

@Stateless
public class UserTaskStatisticsService {

    @Inject
    private UserTaskStatisticsRepository userTaskStatisticsRepository;

    @Context
    private SecurityContext context;

    public List<UserTaskMeetingStatistics> findAll(int page, int offset, Sort sort) {
        return userTaskStatisticsRepository
                .findAll(context.getUserPrincipal().getName(), page, offset, sort);
    }
}
