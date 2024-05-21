package org.cbg.projectmanagement.project_management.dto;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class PageFilterDTO {

    private int page;
    private int offset;
    private String sortColumn;
    private String sortOrder;
}
