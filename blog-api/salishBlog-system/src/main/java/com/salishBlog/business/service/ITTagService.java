package com.salishBlog.business.service;

import com.salishBlog.business.domain.TTag;
import com.salishBlog.business.domain.vo.TTagVo;
import com.salishBlog.business.domain.bo.TTagQueryBo;
import com.salishBlog.business.domain.bo.TTagAddBo;
import com.salishBlog.business.domain.bo.TTagEditBo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Collection;
import java.util.List;

/**
 * 标签Service接口
 *
 * @author salishChen
 * @date 2022-07-21
 */
public interface ITTagService extends IService<TTag> {
	/**
	 * 查询单个
	 * @return
	 */
	TTagVo queryById(Long id);

	/**
	 * 查询列表
	 */
	List<TTagVo> queryList(TTagQueryBo bo);

	/**
	 * 根据新增业务对象插入标签
	 * @param bo 标签新增业务对象
	 * @return
	 */
	Boolean insertByAddBo(TTagAddBo bo);

	/**
	 * 根据编辑业务对象修改标签
	 * @param bo 标签编辑业务对象
	 * @return
	 */
	Boolean updateByEditBo(TTagEditBo bo);

	Boolean increaseTimes(Long id);
	Boolean decreaseTimes(Long id);

	/**
	 * 校验并删除数据
	 * @param ids 主键集合
	 * @param isValid 是否校验,true-删除前校验,false-不校验
	 * @return
	 */
	Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);


}
