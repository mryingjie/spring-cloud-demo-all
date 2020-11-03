package com.demo.springcloud.commons.entities;

import lombok.Data;

import java.util.List;

/**
 * created by Yingjie Zheng at 2020/5/8 10:46
 */
@Data
public class TestField {

    private Long id;

    private String serial;

    private List<Long> idList;

    private List<String> serialList;

    private String name;

    private TestField testFieldmax;
}
