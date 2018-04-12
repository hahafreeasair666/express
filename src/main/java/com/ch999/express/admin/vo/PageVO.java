package com.ch999.express.admin.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


import java.util.List;

/**
 * @author hahalala
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
public class PageVO<T> {

    /**
     * 总页码
     */
    private Integer totalPage;

    /**
     * 当前页码
     */
    private Integer currentPage;

    /**
     * 评论内容
     */
    private List<T> list;

}
