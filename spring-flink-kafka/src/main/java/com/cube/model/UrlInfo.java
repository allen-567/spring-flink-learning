package com.cube.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Author: 滕飞
 * Created: 2019-10-11 14:10
 * Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class UrlInfo {
    private int id;

    private String url;

    private String hash;
}