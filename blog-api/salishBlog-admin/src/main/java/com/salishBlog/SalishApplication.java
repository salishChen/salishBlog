package com.salishBlog;

import org.quartz.Scheduler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * 启动程序
 *
 * @author ruoyi
 */
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
public class SalishApplication
{

    public static void main(String[] args)
    {
        // System.setProperty("spring.devtools.restart.enabled", "false");
        SpringApplication.run(SalishApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ    启动成功     ლ(´ڡ`ლ)ﾞ  \n"  +
                "  ██████  ▄▄▄       ██▓     ██▓  ██████  ██░ ██ \n" +
                "▒██    ▒ ▒████▄    ▓██▒    ▓██▒▒██    ▒ ▓██░ ██▒\n" +
                "░ ▓██▄   ▒██  ▀█▄  ▒██░    ▒██▒░ ▓██▄   ▒██▀▀██░\n" +
                "  ▒   ██▒░██▄▄▄▄██ ▒██░    ░██░  ▒   ██▒░▓█ ░██ \n" +
                "▒██████▒▒ ▓█   ▓██▒░██████▒░██░▒██████▒▒░▓█▒░██▓\n" +
                "▒ ▒▓▒ ▒ ░ ▒▒   ▓▒█░░ ▒░▓  ░░▓  ▒ ▒▓▒ ▒ ░ ▒ ░░▒░▒\n" +
                "░ ░▒  ░ ░  ▒   ▒▒ ░░ ░ ▒  ░ ▒ ░░ ░▒  ░ ░ ▒ ░▒░ ░\n" +
                "░  ░  ░    ░   ▒     ░ ░    ▒ ░░  ░  ░   ░  ░░ ░\n" +
                "      ░        ░  ░    ░  ░ ░        ░   ░  ░  ░\n"
        );
    }
}
