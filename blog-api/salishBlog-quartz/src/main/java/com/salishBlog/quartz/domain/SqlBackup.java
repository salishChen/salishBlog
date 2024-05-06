package com.salishBlog.quartz.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
/**
 * 数据库备份对象 sql_backup
 *
 * @author salishChen
 * @date 2024-04-23
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("sql_backup")
public class SqlBackup implements Serializable {

private static final long serialVersionUID=1L;


    /** id */
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;

    /** 名称 */
    private String sqlName;

    /** 数据库名称 */
    private String dbName;

    /** 数据库ip */
    private String sqlIp;

    /** 用户名 */
    private String sqlUsername;

    /** 密码 */
    private String sqlPassword;

    /** 定时规则 */
    private String sqlCron;

    /** 备份数量限制 */
    private Long fileLimit;

    /** 数据库类型 */
    private String type;

    /** 备份状态 0=正常,1=暂停 */
    private String status;

}
