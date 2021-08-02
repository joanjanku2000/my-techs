package com.project.Restaurant.utils;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class SearchCriteria {
    private List<FieldValuePair> fieldValuePair;
    private String orderBy;
    private String SortDirection;
}

