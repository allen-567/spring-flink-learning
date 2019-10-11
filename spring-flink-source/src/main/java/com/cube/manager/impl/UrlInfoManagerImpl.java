package com.cube.manager.impl;

import com.cube.manager.UrlInfoManager;
import com.cube.model.UrlInfo;
import com.cube.repository.UrlInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.List;

/**
 * Author: 滕飞
 * Created: 2019-10-11 14:14
 * Description:
 */
@Transactional
@Component("urlInfoManager")
public class UrlInfoManagerImpl implements UrlInfoManager {

    @Autowired
    private UrlInfoRepository urlInfoRepository;

    @Override
    public int insert(UrlInfo urlInfo) {

        urlInfo.setHash(DigestUtils.md5DigestAsHex(urlInfo.getUrl().getBytes()));

        UrlInfo info = urlInfoRepository.selectByUrl(urlInfo.getUrl());
        if(null != info)
        {
            return 0;
        }

        return urlInfoRepository.insert(urlInfo);
    }

    @Override
    public List<UrlInfo> queryAll() {
        return urlInfoRepository.queryAll();
    }
}

