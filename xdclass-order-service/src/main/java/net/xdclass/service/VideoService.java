package net.xdclass.service;

import net.xdclass.domain.Video;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author wuq
 * @Time 2022-4-30 11:31
 * @Description
 */
@FeignClient(name = "xdclass-video-service")    // 这里需要填写的就是相应的服务名
public interface VideoService {

    @GetMapping(value = "/api/v1/video/find_by_id")
    Video findById(@RequestParam("videoId") int videoId);


    @PostMapping(value = "/api/v1/video/saveByFeign")
    int saveByFeign(@RequestBody() Video video);
}
