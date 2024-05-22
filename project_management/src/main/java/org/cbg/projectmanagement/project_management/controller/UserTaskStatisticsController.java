package org.cbg.projectmanagement.project_management.controller;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.cbg.projectmanagement.project_management.dto.PageFilterDTO;
import org.cbg.projectmanagement.project_management.dto.Sort;
import org.cbg.projectmanagement.project_management.enums.SortOrder;
import org.cbg.projectmanagement.project_management.service.UserTaskStatisticsService;

@Path("/v1/user-task-statistics")
public class UserTaskStatisticsController {

    @Inject
    private UserTaskStatisticsService userTaskStatisticsService;

    @POST
    @Path("get-all")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"user","administrator"})
    public Response getAll(PageFilterDTO pageFilterDTO) {
        SortOrder sortOrder = pageFilterDTO.getSortOrder().isEmpty() ?
                SortOrder.DEFAULT:
                SortOrder.valueOf(pageFilterDTO.getSortOrder().toUpperCase());
        return Response
                .status(Response.Status.OK)
                .entity(userTaskStatisticsService.findAll(pageFilterDTO.getPage(), pageFilterDTO.getOffset(),
                        new Sort(pageFilterDTO.getSortColumn(), sortOrder)))
                .build();
    }
}
