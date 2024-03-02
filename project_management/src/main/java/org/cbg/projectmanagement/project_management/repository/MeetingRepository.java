package org.cbg.projectmanagement.project_management.repository;

import jakarta.inject.Named;
import org.cbg.projectmanagement.project_management.entity.Meeting;

@Named("MeetingRepository")
public class MeetingRepository extends BaseRepository<Meeting>{

    @Override
    public String getEntityName() {
        return Meeting.class.getSimpleName();
    }
}
