package net.xdclass;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author wuq
 * @Time 2022-4-30 11:25
 * @Description
 */
@SpringBootApplication
@MapperScan("net.xdclass.dao")
public class VideoApplication {

    public static void main(String[] args) {
        SpringApplication.run(VideoApplication.class);
    }
}
