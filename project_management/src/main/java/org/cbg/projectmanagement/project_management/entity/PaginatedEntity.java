package org.cbg.projectmanagement.project_management.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaginatedEntity<T> implements Serializable {

    private List<T> data;
    private long totalRecords;
}
