package net.xdclass.service;

import net.xdclass.domain.Video;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * @author wuq
 * @Time 2022-4-30 11:31
 * @Description
 */
@FeignClient(name = "xdclass-video-service")    // 这里需要填写的就是相应的服务名
public interface VideoService {

    @GetMapping(value = "/api/v1/video/find_by_id")
    Video findById(@RequestParam("videoId") int videoId);


    // 这里使用 @RequestMapping 与 @PostMapping 注解都是可以的
    @RequestMapping(value = "/api/v1/video/saveByFeign")
    int saveByFeign(@RequestBody() Video video);
}
