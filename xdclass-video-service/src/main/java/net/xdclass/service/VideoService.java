package net.xdclass.service;

import net.xdclass.domain.Video;

/**
 * @author wuq
 * @Time 2022-4-30 11:31
 * @Description
 */
public interface VideoService {

    Video findById(int videoId);
}
