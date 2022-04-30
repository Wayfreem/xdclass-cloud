package net.xdclass.service.impl;

import net.xdclass.dao.VideoMapper;
import net.xdclass.domain.Video;
import net.xdclass.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author wuq
 * @Time 2022-4-30 11:32
 * @Description
 */
@Service
public class VideoServiceImpl implements VideoService {

    @Autowired
    private VideoMapper videoMapper;

    @Override
    public Video findById(int videoId) {
        return videoMapper.findById(videoId);
    }
}
