package com.salishBlog.business.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.salishBlog.business.domain.TFillPit;
import com.salishBlog.business.domain.bo.TFillPitAddBo;
import com.salishBlog.business.domain.bo.TFillPitEditBo;
import com.salishBlog.business.domain.bo.TFillPitQueryBo;
import com.salishBlog.business.domain.vo.TFillPitVo;

import java.util.Collection;
import java.util.List;

/**
 * 填坑Service接口
 *
 * @author salishChen
 * @date 2023-06-26
 */
public interface ITFillPitService extends IService<TFillPit> {
	/**
	 * 查询单个
	 * @return
	 */
	TFillPitVo queryById(Long id);

	/**
	 * 查询列表
	 */
	List<TFillPitVo> queryList(TFillPitQueryBo bo);

	/**
	 * 根据新增业务对象插入填坑
	 * @param bo 填坑新增业务对象
	 * @return
	 */
	Boolean insertByAddBo(TFillPitAddBo bo);

	/**
	 * 根据编辑业务对象修改填坑
	 * @param bo 填坑编辑业务对象
	 * @return
	 */
	Boolean updateByEditBo(TFillPitEditBo bo);

	/**
	 * 校验并删除数据
	 * @param ids 主键集合
	 * @param isValid 是否校验,true-删除前校验,false-不校验
	 * @return
	 */
	Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

	List<String> getTags();
}
