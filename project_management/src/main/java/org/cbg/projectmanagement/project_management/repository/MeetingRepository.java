package org.cbg.projectmanagement.project_management.repository;

import jakarta.inject.Named;
import org.cbg.projectmanagement.project_management.entity.Meeting;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Named("MeetingRepository")
public class MeetingRepository extends BaseRepository<Meeting> {

    @Override
    public String getEntityName() {
        return Meeting.class.getSimpleName();
    }

    public List<Meeting> getUnfinishedMeetings(String key) {
        String query = "FROM Meeting M JOIN M.project PR WHERE PR.key = :key AND M.status != 'END'";
        Map<String, Object> criteria = new HashMap<>();
        criteria.put("key", key);
        return getEntityByCriteria(query, criteria);
    }
}
