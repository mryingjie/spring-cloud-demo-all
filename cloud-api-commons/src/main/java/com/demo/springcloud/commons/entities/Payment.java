package com.demo.springcloud.commons.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * created by Yingjie Zheng at 2020-03-23 17:44
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Payment implements Serializable {

    private Long id;

    private String serial;

    private List<Long> idList;

    private List<String> serialList;

    private String name;

    private TestField testField;
}
