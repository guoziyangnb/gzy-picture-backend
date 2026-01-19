package com.yupi.yupicturebackend;

import org.apache.shardingsphere.spring.boot.ShardingSphereAutoConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.session.SessionAutoConfiguration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication(exclude = {
//        RedisAutoConfiguration.class,  // 先暂时不触发redis连接
        SessionAutoConfiguration.class, // spring-session依赖触发Redis连接,
        ShardingSphereAutoConfiguration.class
})
@EnableAsync
@MapperScan("com.yupi.yupicturebackend.mapper")
@EnableAspectJAutoProxy(exposeProxy = true) //开启代理
public class YuPictureBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(YuPictureBackendApplication.class, args);
    }

}
