package com.salishBlog.quartz.config;

import com.salishBlog.quartz.service.impl.SqlBackupServiceImpl;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class QuartzAppRunner implements ApplicationRunner {

    private final SqlBackupServiceImpl sqlBackupService;

    public QuartzAppRunner(SqlBackupServiceImpl sqlBackupService) {
        this.sqlBackupService = sqlBackupService;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        sqlBackupService.init();
    }
}
