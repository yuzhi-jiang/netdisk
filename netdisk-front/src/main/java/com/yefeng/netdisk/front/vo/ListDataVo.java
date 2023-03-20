package com.yefeng.netdisk.front.vo;

import java.util.List;

/**
 * This class is for
 *
 * @author 夜枫
 * @version 2023-03-20 21:07
 */
public class ListDataVo<T> {
    List<T> list;
    Long total;

    public ListDataVo(List<T> list, Long total) {
        this.list = list;
        this.total = total;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }
}
