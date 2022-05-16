package net.xdclass.controller;

import net.xdclass.domain.Video;
import net.xdclass.domain.VideoOrder;
import net.xdclass.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wuq
 * @Time 2022-4-30 13:46
 * @Description
 */
@RestController
@RequestMapping("api/v1/video_order")
public class OrderController {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private DiscoveryClient discoveryClient;
    @Autowired
    private VideoService videoService;

    /**
     * 这块是使用 ribbon 的方式调用，这里不做屏蔽，方便于学习对比
     *
     * @param videoId videoId
     * @return VideoOrder
     */
    // http://localhost:8000/api/v1/video_order/save?videoId=30
    @RequestMapping("/save")
    public Object save(int videoId) {

        // 不适用 discoveryClient 时，调用方式
        // Video video = restTemplate.getForObject("http://localhost:9000/api/v1/video/find_by_id?videoId="+videoId, Video.class);

        // 使用 负载之后，获取第一个
//        List<ServiceInstance> list = discoveryClient.getInstances("xdclass-video-service");
//        ServiceInstance serviceInstance = list.get(0);
//        String host = serviceInstance.getHost();
//        int port = serviceInstance.getPort();
//        Video video = restTemplate.getForObject("http://"+host+":"+port+"/api/v1/video/find_by_id?videoId="+videoId, Video.class);

        // 使用 负载均衡策略， 这里通过服务名去调用
        Video video = restTemplate.getForObject("http://xdclass-video-service/api/v1/video/find_by_id?videoId=" + videoId, Video.class);

        VideoOrder videoOrder = new VideoOrder();
        videoOrder.setVideoId(video.getId());
        videoOrder.setVideoTitle(video.getTitle());
        videoOrder.setCreateTime(new Date());
        videoOrder.setServerInfo(video.getServerInfo());

        return videoOrder;
    }

    /**
     * 这里是使用 feign 接口
     *
     * @param videoId videoId
     * @return VideoOrder
     */
    // http://localhost:8000/api/v1/video_order/findById?videoId=30
    @RequestMapping("/findById")
    public Object findById(int videoId) {

        Video video = videoService.findById(videoId);

        VideoOrder videoOrder = new VideoOrder();
        videoOrder.setVideoId(video.getId());
        videoOrder.setVideoTitle(video.getTitle());
        videoOrder.setCreateTime(new Date());
        videoOrder.setServerInfo(video.getServerInfo());
        return videoOrder;
    }

    @PostMapping("saveByFeign")
    public int saveByFeign(@RequestBody Video video) {
        return videoService.saveByFeign(video);
    }


    int temp = 0;

    /**
     * 用于测试熔断、降级
     *
     * @return map
     */
    @RequestMapping("list")
    private Map list() {

        temp++;

        if (temp % 3 == 0) {
            throw new RuntimeException("服务异常");
        }

        Map<String, String> map = new HashMap<>();
        map.put("title", "测试返回数据");
        map.put("name", "返回名称");
        return map;
    }

}
