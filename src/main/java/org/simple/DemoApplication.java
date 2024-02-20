package org.simple;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author yxl17
 * @Package : org.simple
 * @Create on : 2024/2/18 21:46
 **/

@SpringBootApplication
@MapperScan("org/simple/mapper")
public class DemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}
