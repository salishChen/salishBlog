package com.salishBlog.business.service;

import com.salishBlog.business.domain.TBlog;
import com.salishBlog.business.domain.vo.TBlogVo;
import com.salishBlog.business.domain.bo.TBlogQueryBo;
import com.salishBlog.business.domain.bo.TBlogAddBo;
import com.salishBlog.business.domain.bo.TBlogEditBo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Collection;
import java.util.List;

/**
 * 博客Service接口
 *
 * @author salish
 * @date 2022-07-21
 */
public interface ITBlogService extends IService<TBlog> {
	/**
	 * 查询单个
	 * @return
	 */
	TBlogVo queryById(Long id);

	/**
	 * 查询列表
	 */
	List<TBlogVo> queryList(TBlogQueryBo bo);

	/**
	 * 根据新增业务对象插入博客
	 * @param bo 博客新增业务对象
	 * @return
	 */
	Boolean insertByAddBo(TBlogAddBo bo);

	/**
	 * 根据编辑业务对象修改博客
	 * @param bo 博客编辑业务对象
	 * @return
	 */
	Boolean updateByEditBo(TBlogEditBo bo);

	/**
	 * 校验并删除数据
	 * @param ids 主键集合
	 * @param isValid 是否校验,true-删除前校验,false-不校验
	 * @return
	 */
	Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
