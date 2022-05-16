## 来自小滴课堂 Spring Cloud Alibaba 学习

### 使用的技术有 

#### nacos

**linux 中安装 nacos** 

- 下载官方的包，并且解压
- 进入 nacos 的 bin 路径下，执行 nacos 的启动命令：`sh startup.sh -m standalone`
- 需要添加开放端口 8848，以及重新加载防火墙
- 访问地址为：`localhost:8848/nacos`， 用户密码为：nacos/nacos

另外在启动完成之后，可以进入 nacos 路径下的 logs 目录，查看启日志 `tail start.out`

**nacos 代码修改** 
 
在 video-service、order-service、user-service 的 pom 文件中添加依赖

```xml
<!--添加nacos客户端-->
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
</dependency>
```

配置 Nacos 的地址

```yml
server:
  port:9000

spring:
  application:
    name: xsclass-video-service
  cloud:
    nacos:
      discovery:
        server-addr:127.0.0.1:8848
```

启动类增加注解  `@EnableDiscoveryClient`

### 集成 sentinel

#### sentinel 控制台

sentinel 有一个单独的控制台，可以去官网上面去下载，然后通过命令行启动 [链接地址](https://sentinelguard.io/zh-cn/docs/dashboard.html)

**注意：如果是通过下面的方式启动，需要开放防火墙端口**

默认的账号、密码都是 sentinel 
```shell
java -Dserver.port=8858 -Dcsp.sentinel.dashboard.server=192.168.152.129:8858 -Dproject.name=sentinel-dashboard -jar sentinel-dashboard-1.8.0.jar

nohup java -Dserver.port=8858 -Dcsp.sentinel.dashboard.server=192.168.152.129:8858 -Dproject.name=sentinel-dashboard -jar sentinel-dashboard-1.8.0.jar &
```