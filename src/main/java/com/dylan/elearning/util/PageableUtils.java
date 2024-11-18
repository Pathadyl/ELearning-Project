package com.dylan.elearning.util;

import org.springframework.data.domain.Sort;
import com.dylan.elearning.dto.SortOption;

import java.util.List;

public class PageableUtils {

    public static Sort determineSort(List<SortOption> sortOptions) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdDate");

        if (sortOptions != null && !sortOptions.isEmpty()) {
            sort = Sort.unsorted();
            for (SortOption sortOption : sortOptions) {
                if (SortOption.ASC.equalsIgnoreCase(sortOption.getOrder())) {
                    sort = sort.and(Sort.by(Sort.Direction.ASC, sortOption.getField()));
                } else if (SortOption.DESC.equalsIgnoreCase(sortOption.getOrder())) {
                    sort = sort.and(Sort.by(Sort.Direction.DESC, sortOption.getField()));
                }
            }
        }
        return sort;
    }

}

