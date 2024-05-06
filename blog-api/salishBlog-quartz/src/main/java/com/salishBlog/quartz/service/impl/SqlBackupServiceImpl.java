package com.salishBlog.quartz.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.salishBlog.common.constant.ScheduleConstants;
import com.salishBlog.common.exception.job.TaskException;
import com.salishBlog.common.utils.StringUtils;
import com.salishBlog.quartz.domain.SysJob;
import com.salishBlog.quartz.util.CronUtils;
import com.salishBlog.quartz.util.ScheduleUtils;
import org.quartz.JobDataMap;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.Page;
import com.salishBlog.quartz.domain.bo.SqlBackupAddBo;
import com.salishBlog.quartz.domain.bo.SqlBackupQueryBo;
import com.salishBlog.quartz.domain.bo.SqlBackupEditBo;
import com.salishBlog.quartz.domain.SqlBackup;
import com.salishBlog.quartz.mapper.SqlBackupMapper;
import com.salishBlog.quartz.domain.vo.SqlBackupVo;
import com.salishBlog.quartz.service.ISqlBackupService;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 数据库备份Service业务层处理
 *
 * @author salishChen
 * @date 2024-04-23
 */
@Service
public class SqlBackupServiceImpl extends ServiceImpl<SqlBackupMapper, SqlBackup> implements ISqlBackupService {

    private static final Logger log = LoggerFactory.getLogger(SqlBackupServiceImpl.class);
    private final Scheduler scheduler;

    public SqlBackupServiceImpl(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

//    @PostConstruct
    public void init() throws SchedulerException, TaskException
    {
//        scheduler.clear();
        List<SqlBackup> jobList = this.baseMapper.selectList(new QueryWrapper<>());
        for (SqlBackup job : jobList)
        {
            ScheduleUtils.createScheduleJoGeneral(scheduler, toJob(job), "sqlTask");
        }
    }

    public void updateSchedulerJob(SysJob job, String jobGroup ,String key) throws SchedulerException, TaskException
    {
        String jobId = key + job.getJobId();
        // 判断是否存在
        JobKey jobKey = ScheduleUtils.getJobKey(jobId, jobGroup);
        if (scheduler.checkExists(jobKey))
        {
            // 防止创建时存在数据问题 先移除，然后在执行创建操作
            scheduler.deleteJob(jobKey);
        }
        ScheduleUtils.createScheduleJoGeneral(scheduler, job, "sqlTask");
    }

    /**
     * 立即运行任务
     *
     * @param backup 调度信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void run(SqlBackup backup) throws SchedulerException
    {
        String jobId = "sqlTask" + backup.getId();
        SqlBackup sqlBackup = this.getById(backup.getId());
        // 参数
        JobDataMap dataMap = new JobDataMap();
        dataMap.put(ScheduleConstants.TASK_PROPERTIES, toJob(sqlBackup));
        scheduler.triggerJob(ScheduleUtils.getJobKey(jobId, "SQL"), dataMap);
    }

    private SysJob toJob(SqlBackup job){
        SysJob sysJob = new SysJob();
        sysJob.setJobId(job.getId());
        sysJob.setJobGroup("SQL");
        sysJob.setCronExpression(job.getSqlCron());
        sysJob.setJobName(job.getSqlName());
        sysJob.setStatus(job.getStatus());
        sysJob.setInvokeTarget(StringUtils.format("sqlTask."+job.getType()+"Backup('{}')",job.getId()));
        sysJob.setConcurrent("0");
        return sysJob;
    }

    @Override
    public SqlBackupVo queryById(Long id){
        SqlBackup db = this.baseMapper.selectById(id);
        return BeanUtil.toBean(db, SqlBackupVo.class);
    }

    @Override
    public List<SqlBackupVo> queryList(SqlBackupQueryBo bo) {
        LambdaQueryWrapper<SqlBackup> lqw = Wrappers.lambdaQuery();
        return domain2Vo(this.list(lqw));
    }

    /**
    * 实体类转化成视图对象
    *
    * @param collection 实体类集合
    * @return
    */
    private List<SqlBackupVo> domain2Vo(Collection<SqlBackup> collection) {
        List<SqlBackupVo> voList = collection.stream()
                .map(any -> BeanUtil.toBean(any, SqlBackupVo.class))
                .collect(Collectors.toList());
        if (collection instanceof Page) {
            Page<SqlBackup> page = (Page<SqlBackup>)collection;
            Page<SqlBackupVo> pageVo = new Page<>();
            BeanUtil.copyProperties(page,pageVo);
            pageVo.addAll(voList);
            voList = pageVo;
        }
        return voList;
    }

    @Override
    public Boolean insertByAddBo(SqlBackupAddBo bo) {
        SqlBackup add = BeanUtil.toBean(bo, SqlBackup.class);
        validdomainBeforeSave(add);
        boolean save = this.save(add);
        if (save){
            try {
                ScheduleUtils.createScheduleJoGeneral(scheduler, toJob(add), "sqlTask");
            }catch (Exception e){
                log.error(StringUtils.format("SqlBackupError:insert,message:{}",e.getMessage()));
            }
        }
        return save;
    }

    @Override
    public Boolean updateByEditBo(SqlBackupEditBo bo) {
        SqlBackup update = BeanUtil.toBean(bo, SqlBackup.class);
        validdomainBeforeSave(update);
        SqlBackup sqlBackup = this.baseMapper.selectById(bo.getId());
        if (!sqlBackup.getSqlCron().equals(bo.getSqlCron())){
            try {
                updateSchedulerJob(toJob(BeanUtil.toBean(bo,SqlBackup.class)),"SQL","sqlTask");
            }catch (Exception e){
                log.error(StringUtils.format("SqlBackupError:update,message:{}",e.getMessage()));
            }
        }
        return this.updateById(update);
    }

    /**
     * 保存前的数据校验
     *
     * @param domain 实体类数据
     */
    private void validdomainBeforeSave(SqlBackup domain){
        //TODO 做一些数据校验,如唯一约束
        if (StringUtils.isNotEmpty(domain.getSqlCron())){
            boolean valid = CronUtils.isValid(domain.getSqlCron());
            if (!valid){
                throw new RuntimeException("定时规则错误！");
            }
        }

    }

    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return this.removeByIds(ids);
    }
}
