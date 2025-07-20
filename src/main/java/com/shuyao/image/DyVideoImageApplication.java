package com.shuyao.image;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;

@SpringBootApplication(exclude = {
        ErrorMvcAutoConfiguration.class
})
public class DyVideoImageApplication {

    public static void main(String[] args) {
        //FFmpegInitializer.init();  // 先初始化FFmpeg
        SpringApplication.run(DyVideoImageApplication.class);
    }
}
