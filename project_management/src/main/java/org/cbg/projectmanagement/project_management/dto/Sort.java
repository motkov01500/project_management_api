package org.cbg.projectmanagement.project_management.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cbg.projectmanagement.project_management.enums.SortOrder;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Sort {

    private String column;
    private SortOrder order;
}
