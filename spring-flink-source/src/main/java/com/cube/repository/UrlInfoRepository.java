package com.cube.repository;

import com.cube.model.UrlInfo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Author: 滕飞
 * Created: 2019-10-11 14:11
 * Description:
 */
@Repository
public interface UrlInfoRepository {

    UrlInfo selectByPrimaryKey(Integer id);

    UrlInfo selectByUrl(String url);

    int insert(UrlInfo urlInfo);

    List<UrlInfo> queryAll();
}
