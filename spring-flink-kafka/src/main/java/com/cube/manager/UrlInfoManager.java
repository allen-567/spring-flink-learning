package com.cube.manager;


import com.cube.model.UrlInfo;

import java.util.List;

/**
 * Author: 滕飞
 * Created: 2019-10-11 14:11
 * Description:
 */
public interface UrlInfoManager {

    int insert(UrlInfo urlInfo);

    List<UrlInfo> queryAll();
}
