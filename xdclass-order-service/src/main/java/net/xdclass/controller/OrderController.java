package net.xdclass.controller;

import net.xdclass.domain.Video;
import net.xdclass.domain.VideoOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.List;

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


    // http://localhost:8000/api/v1/video_order/save?videoId=30
    @RequestMapping("/save")
    public Object save(int videoId){

        // 不适用 discoveryClient 时，调用方式
        // Video video = restTemplate.getForObject("http://localhost:9000/api/v1/video/find_by_id?videoId="+videoId, Video.class);

        // 使用 负载之后，获取第一个
//        List<ServiceInstance> list = discoveryClient.getInstances("xdclass-video-service");
//        ServiceInstance serviceInstance = list.get(0);
//        String host = serviceInstance.getHost();
//        int port = serviceInstance.getPort();
//        Video video = restTemplate.getForObject("http://"+host+":"+port+"/api/v1/video/find_by_id?videoId="+videoId, Video.class);

        // 使用 负载均衡策略， 这里通过服务名去调用
        Video video =  restTemplate.getForObject("http://xdclass-video-service/api/v1/video/find_by_id?videoId="+videoId, Video.class);

        VideoOrder videoOrder = new VideoOrder();
        videoOrder.setVideoId(video.getId());
        videoOrder.setVideoTitle(video.getTitle());
        videoOrder.setCreateTime(new Date());
        videoOrder.setServerInfo(video.getServerInfo());

        return videoOrder;
    }
}
