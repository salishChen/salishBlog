package com.salishBlog.common.utils.sql;


import com.salishBlog.common.config.SalishConfig;
import com.salishBlog.common.utils.file.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SqlBackupUtil {

    private static final Logger log = LoggerFactory.getLogger(SqlBackupUtil.class);

    /**
     * 备份数据库db
     */
    public static void dbBackUp(String ip, String uname, String pwd, String dbName) {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd-HH-mm-sss");
        String backName = dateFormat.format(date) + ".sql";
        String pathSql = "";
        //输出流文件
        File fileSql;
        boolean inDocker = false;
        try {
            //判断系统
            String osName = System.getProperties().getProperty("os.name");
            if (osName.equals("Linux")) {
                pathSql = SalishConfig.getBackupPath() + "/" + dbName + "/" + backName;
                Path path = Paths.get("/proc/1/cgroup");
                if (Files.exists(path)) {
                    String content = new String(Files.readAllBytes(path));
                    inDocker = content.contains("docker");
                }

            } else {
                pathSql = SalishConfig.getWinProfile() + "\\" + dbName + "\\" + backName;

            }

            //输出流文件
            fileSql = new File(pathSql);
            //创建备份sql文件
            if (!fileSql.exists()) {
                if (!fileSql.getParentFile().exists()) {
                    fileSql.getParentFile().mkdirs();
                }
                fileSql.createNewFile();
            }

            //mysqldump -hlocalhost -uroot -p123456 db > /home/back.sql  整个库进行备份sql文件
            //mysqldump -hlocalhost -uroot -p123456 数据库名  数据表名 > /home/back.sql  单张表备份sql文件
            StringBuffer sb = new StringBuffer();
            if (inDocker) {
                sb.append("docker exec mysql sh -c ");
                sb.append("\'mysqldump");
                sb.append(" -h" + ip);
                sb.append(" " + dbName);
                sb.append(" -u" + uname);
                sb.append(" -p" + pwd + " 2>/dev/null\'");
                sb.append(" -r ");//java中必须使用"-r"替代">"
                sb.append(pathSql);
            } else {
                sb.append("mysqldump");
                sb.append(" -h " + ip);
                sb.append(" -u " + uname);
                sb.append(" -p " + pwd);
                sb.append(" " + dbName + " ");
                sb.append(" -r ");//java中必须使用"-r"替代">"
                sb.append(pathSql);
            }
            log.error(sb.toString());


            Process process = null;
            //判断系统
            if (osName.equals("Linux")) {
                BufferedReader bufferedReader = null;
                PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(fileSql), "utf8"));

                if (inDocker) {
                    process = new ProcessBuilder("/bin/sh", "-c", sb.toString()).start();
                } else {
                    process = Runtime.getRuntime().exec(sb.toString());
                }
                InputStreamReader inputStreamReader = new InputStreamReader(process.getInputStream(), "utf8");
                bufferedReader = new BufferedReader(inputStreamReader);
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    printWriter.println(line);
                }
                printWriter.flush();
                log.info("-==[ 备份结束！ ]==-");
            } else {
                System.out.println("cmd命令为：" + "cmd /c " + sb.toString());
                process = Runtime.getRuntime().exec("cmd /c " + sb.toString());//执行语句
            }
            process.waitFor();//等待上述命令执行完毕后打印下面log

            //输出错误信息
            InputStream is = process.getErrorStream();
            InputStreamReader isr = new InputStreamReader(is, "gbk");//读取
            System.out.println(isr.getEncoding());
            BufferedReader bufr = new BufferedReader(isr);//缓冲
            String line = null;
            while ((line = bufr.readLine()) != null) {
                System.out.println("error:" + line);
            }
            isr.close();
            log.info("数据库备份结束，备份结果：{}", process.exitValue() == 0 ? "success" : "fail");
        } catch (Exception e) {
            log.error("数据库备份失败：{}", e.getMessage());
        }
    }

}
