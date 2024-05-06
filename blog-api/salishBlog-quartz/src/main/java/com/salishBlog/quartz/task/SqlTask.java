package com.salishBlog.quartz.task;

import com.salishBlog.common.utils.sql.SqlBackupUtil;
import com.salishBlog.quartz.domain.vo.SqlBackupVo;
import com.salishBlog.quartz.service.ISqlBackupService;
import org.springframework.stereotype.Component;

@Component("sqlTask")
public class SqlTask {

    private final ISqlBackupService sqlBackupService;

    public SqlTask(ISqlBackupService sqlBackupService) {
        this.sqlBackupService = sqlBackupService;
    }

    public void mysqlBackup(String id){
        SqlBackupVo sqlBackupVo = sqlBackupService.queryById(Long.parseLong(id));
        SqlBackupUtil.dbBackUp(sqlBackupVo.getSqlIp(),sqlBackupVo.getSqlUsername(),sqlBackupVo.getSqlPassword(),sqlBackupVo.getDbName());
    }
}
