## 订单服务

### 集成 Ribbon 的修改

由于调用是需要使用到 `RestTemplate` 类，这里就需要增加一个 负载均衡的注解
```java
@Bean
@LoadBalanced   // 增加的注解
public RestTemplate getRestTemplate(){
    return new RestTemplate();
}
```