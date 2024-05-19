package org.cbg.projectmanagement.project_management.filter;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class BaseFilter {

    private List<Long> idList;
    private Long page;
    private Long size;
}
