package com.inspur.db.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * (Address)实体类
 *
 * @author makejava
 * @since 2020-04-03 14:23:58
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Address implements Serializable {
    private static final long serialVersionUID = 293811553412678498L;
    /**
    * id
    */
    private Integer id;
    /**
    * 省
    */
    private String province;
    /**
    * 市
    */
    private String city;
    /**
    * 区
    */
    private String area;
}