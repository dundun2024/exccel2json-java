package cn.dundun.tools;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

/**
 * @author xiaotian
 * @since 2025/2/22 14:49
 */
@Slf4j
@SpringBootApplication
public class ExcelToJsonApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExcelToJsonApplication.class, args);
        openBrowser();
    }

    private static void openBrowser() {

        try {
            Runtime.getRuntime().exec("cmd /c start http://localhost:9797");

        } catch (IOException e) {
            log.error(">>>>>>>>> 浏览器打开失败", e);
            throw new RuntimeException("浏览器打开失败");
        }
    }
}
