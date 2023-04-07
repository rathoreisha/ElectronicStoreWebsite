package com.shruteekatech.ecommerce.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 *
 * @param <T>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PagableResponse<T> {

    private List<T> content;
    private Integer pageNumber;
    private Integer pageSize;
    private Long totalElement;

    private Integer totalPages;
    private boolean lastPage;

}
