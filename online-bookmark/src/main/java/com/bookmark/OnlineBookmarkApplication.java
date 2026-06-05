package com.bookmark;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 在线书签收藏夹系统启动类
 * @author 开发者
 * @date 2024-01-01
 */
@SpringBootApplication
@MapperScan("com.bookmark.mapper")  // MyBatis-Plus Mapper 扫描
public class OnlineBookmarkApplication {

    public static void main(String[] args) {
        SpringApplication.run(OnlineBookmarkApplication.class, args);
        System.out.println();
        System.out.println();
        System.out.println("启动成功！");
        System.out.println();
        System.out.println("网站：http://localhost:8080/");
        System.out.println();
        System.out.println();
    }
}