package com.salishBlog.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.Page;
import com.salishBlog.business.domain.bo.TBlogAddBo;
import com.salishBlog.business.domain.bo.TBlogQueryBo;
import com.salishBlog.business.domain.bo.TBlogEditBo;
import com.salishBlog.business.domain.TBlog;
import com.salishBlog.business.mapper.TBlogMapper;
import com.salishBlog.business.domain.vo.TBlogVo;
import com.salishBlog.business.service.ITBlogService;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 博客Service业务层处理
 *
 * @author salish
 * @date 2022-07-21
 */
@Service
public class TBlogServiceImpl extends ServiceImpl<TBlogMapper, TBlog> implements ITBlogService {

    @Override
    public TBlogVo queryById(Long id){
        TBlog db = this.baseMapper.selectById(id);
        return BeanUtil.toBean(db, TBlogVo.class);
    }

    @Override
    public List<TBlogVo> queryList(TBlogQueryBo bo) {
        LambdaQueryWrapper<TBlog> lqw = Wrappers.lambdaQuery();
        lqw.eq(StrUtil.isNotBlank(bo.gettitle()), TBlog::gettitle, bo.gettitle());
        lqw.eq(StrUtil.isNotBlank(bo.getCover()), TBlog::getCover, bo.getCover());
        lqw.eq(StrUtil.isNotBlank(bo.getSummary()), TBlog::getSummary, bo.getSummary());
        lqw.eq(StrUtil.isNotBlank(bo.getContent()), TBlog::getContent, bo.getContent());
        lqw.eq(bo.getTagId() != null, TBlog::getTagId, bo.getTagId());
        lqw.eq(StrUtil.isNotBlank(bo.getBlogType()), TBlog::getBlogType, bo.getBlogType());
        lqw.eq(StrUtil.isNotBlank(bo.getContentType()), TBlog::getContentType, bo.getContentType());
        lqw.eq(bo.getIsDelete() != null, TBlog::getIsDelete, bo.getIsDelete());
        return entity2Vo(this.list(lqw));
    }

    /**
    * 实体类转化成视图对象
    *
    * @param collection 实体类集合
    * @return
    */
    private List<TBlogVo> entity2Vo(Collection<TBlog> collection) {
        List<TBlogVo> voList = collection.stream()
                .map(any -> BeanUtil.toBean(any, TBlogVo.class))
                .collect(Collectors.toList());
        if (collection instanceof Page) {
            Page<TBlog> page = (Page<TBlog>)collection;
            Page<TBlogVo> pageVo = new Page<>();
            BeanUtil.copyProperties(page,pageVo);
            pageVo.addAll(voList);
            voList = pageVo;
        }
        return voList;
    }

    @Override
    public Boolean insertByAddBo(TBlogAddBo bo) {
        TBlog add = BeanUtil.toBean(bo, TBlog.class);
        validEntityBeforeSave(add);
        return this.save(add);
    }

    @Override
    public Boolean updateByEditBo(TBlogEditBo bo) {
        TBlog update = BeanUtil.toBean(bo, TBlog.class);
        validEntityBeforeSave(update);
        return this.updateById(update);
    }

    /**
     * 保存前的数据校验
     *
     * @param entity 实体类数据
     */
    private void validEntityBeforeSave(TBlog entity){
        //TODO 做一些数据校验,如唯一约束
    }

    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return this.removeByIds(ids);
    }
}
