package com.salishBlog.quartz.service;

import com.salishBlog.quartz.domain.SqlBackup;
import com.salishBlog.quartz.domain.SysJob;
import com.salishBlog.quartz.domain.vo.SqlBackupVo;
import com.salishBlog.quartz.domain.bo.SqlBackupQueryBo;
import com.salishBlog.quartz.domain.bo.SqlBackupAddBo;
import com.salishBlog.quartz.domain.bo.SqlBackupEditBo;
import com.baomidou.mybatisplus.extension.service.IService;
import org.quartz.SchedulerException;

import java.util.Collection;
import java.util.List;

/**
 * 数据库备份Service接口
 *
 * @author salishChen
 * @date 2024-04-23
 */
public interface ISqlBackupService extends IService<SqlBackup> {
	/**
	 * 查询单个
	 * @return
	 */
	SqlBackupVo queryById(Long id);

	/**
	 * 查询列表
	 */
	List<SqlBackupVo> queryList(SqlBackupQueryBo bo);

	/**
	 * 根据新增业务对象插入数据库备份
	 * @param bo 数据库备份新增业务对象
	 * @return
	 */
	Boolean insertByAddBo(SqlBackupAddBo bo);

	/**
	 * 根据编辑业务对象修改数据库备份
	 * @param bo 数据库备份编辑业务对象
	 * @return
	 */
	Boolean updateByEditBo(SqlBackupEditBo bo);

	/**
	 * 校验并删除数据
	 * @param ids 主键集合
	 * @param isValid 是否校验,true-删除前校验,false-不校验
	 * @return
	 */
	Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

	public void run(SqlBackup backup) throws SchedulerException;
}
