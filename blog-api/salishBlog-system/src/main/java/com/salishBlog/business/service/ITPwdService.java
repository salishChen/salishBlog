package com.salishBlog.business.service;

import com.salishBlog.business.domain.TPwd;
import com.salishBlog.business.domain.vo.TPwdVo;
import com.salishBlog.business.domain.bo.TPwdQueryBo;
import com.salishBlog.business.domain.bo.TPwdAddBo;
import com.salishBlog.business.domain.bo.TPwdEditBo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Collection;
import java.util.List;

/**
 * 秘钥本Service接口
 *
 * @author salishChen
 * @date 2024-05-15
 */
public interface ITPwdService extends IService<TPwd> {
	/**
	 * 查询单个
	 * @return
	 */
	TPwdVo queryById(Long id);

	/**
	 * 查询列表
	 */
	List<TPwdVo> queryList(TPwdQueryBo bo);

	/**
	 * 根据新增业务对象插入秘钥本
	 * @param bo 秘钥本新增业务对象
	 * @return
	 */
	Boolean insertByAddBo(TPwdAddBo bo);

	/**
	 * 根据编辑业务对象修改秘钥本
	 * @param bo 秘钥本编辑业务对象
	 * @return
	 */
	Boolean updateByEditBo(TPwdEditBo bo);

	/**
	 * 校验并删除数据
	 * @param ids 主键集合
	 * @param isValid 是否校验,true-删除前校验,false-不校验
	 * @return
	 */
	Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
