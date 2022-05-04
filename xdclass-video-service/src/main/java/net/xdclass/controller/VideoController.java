package net.xdclass.controller;

import net.xdclass.domain.Video;
import net.xdclass.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author wuq
 * @Time 2022-4-30 11:30
 * @Description
 */
@RestController
@RequestMapping("api/v1/video")
public class VideoController {

    @Autowired
    private VideoService videoService;

    // http://localhost:9000/api/v1/video/find_by_id?videoId=30
    @RequestMapping("find_by_id")
    public Object findById(int videoId, HttpServletRequest httpRequest){
        Video video = videoService.findById(videoId);
        String serverInfo = httpRequest.getServerName() + ":"+ httpRequest.getServerPort();
        video.setServerInfo(serverInfo);
        return video;
    }
}
