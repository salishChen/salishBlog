package com.salishBlog.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSONObject;
import com.github.pagehelper.PageInfo;
import com.salishBlog.business.service.ITTagService;
import com.salishBlog.common.constant.HttpStatus;
import com.salishBlog.common.core.domain.AjaxResult;
import com.salishBlog.common.core.page.TableDataInfo;
import org.springframework.beans.factory.annotation.Autowired;
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

import java.util.*;
import java.util.stream.Collectors;

/**
 * 博客Service业务层处理
 *
 * @author salish
 * @date 2022-07-21
 */
@Service
public class TBlogServiceImpl extends ServiceImpl<TBlogMapper, TBlog> implements ITBlogService {

    @Autowired
    ITTagService tagService;

    @Override
    public TBlogVo queryById(Long id){
        TBlog db = this.baseMapper.selectById(id);
        return BeanUtil.toBean(db, TBlogVo.class);
    }

    @Override
    public List<TBlogVo> queryList(TBlogQueryBo bo) {
        LambdaQueryWrapper<TBlog> lqw = Wrappers.lambdaQuery();
        lqw.like(StrUtil.isNotBlank(bo.getTitle()), TBlog::getTitle, bo.getTitle());
        lqw.eq(StrUtil.isNotBlank(bo.getCover()), TBlog::getCover, bo.getCover());
        lqw.eq(StrUtil.isNotBlank(bo.getSummary()), TBlog::getSummary, bo.getSummary());
        lqw.eq(bo.getTagId() != null, TBlog::getTagId, bo.getTagId());
        lqw.eq(StrUtil.isNotBlank(bo.getBlogType()), TBlog::getBlogType, bo.getBlogType());
        lqw.eq(StrUtil.isNotBlank(bo.getContentType()), TBlog::getContentType, bo.getContentType());
        lqw.eq(bo.getIsDelete() != null, TBlog::getIsDelete, bo.getIsDelete());
        return entity2Vo(this.list(lqw));
    }
    @Override
    public List<TBlog> queryInfo(TBlog tBlog) {
        return this.baseMapper.selectBlogByTag(tBlog);
    }

    @Override
    public List <TBlog> selectBlogByTag(TBlog tBlog){
        return this.baseMapper.selectBlogByTag(tBlog);
    }

    @Override
    public AjaxResult interfile(TBlog tBlog){
        List<TBlog> blogs = this.baseMapper.interfile(tBlog);
        Map<String, List<TBlog>> collect = blogs.stream().collect(Collectors.groupingBy(TBlog::getTime));
        List<List<TBlog>> collect1 = new ArrayList<>();


        ListIterator<Map.Entry<String, List<TBlog>>> li = new ArrayList<>(collect.entrySet()).listIterator(collect.size());

        while(li.hasPrevious()) {
            Map.Entry<String, List<TBlog>> entry = li.previous();
            collect1.add(entry.getValue());
        }

        JSONObject object1 = new JSONObject();
        object1.put("total",new PageInfo<>(blogs).getTotal());
        object1.put("data",collect1);

        return AjaxResult.success(object1);
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
        if (bo.getTagId()!=null&& !bo.getTagId().equals("")){
            String[] tagIds = bo.getTagId().split("'");
            for (String id:tagIds) {
                tagService.increaseTimes(Long.parseLong(id));
            }
        }
        TBlog add = BeanUtil.toBean(bo, TBlog.class);
        validEntityBeforeSave(add);
        return this.save(add);
    }

    @Override
    public TBlog insertByAddBoReturn(TBlogAddBo bo) {
        if (StrUtil.isNotBlank(bo.getTagId())){
            String[] tagIds = bo.getTagId().split(",");
            for (String id:tagIds) {
                tagService.increaseTimes(Long.parseLong(id));
            }
        }
        TBlog add = BeanUtil.toBean(bo, TBlog.class);
        validEntityBeforeSave(add);
        this.save(add);
        return add;
    }

    @Override
    public Boolean updateByEditBo(TBlogEditBo bo) {
        TBlogVo tBlogVo = queryById(bo.getId());
        if (tBlogVo.getTagId()!=null&& !tBlogVo.getTagId().equals("")){
            String[] tagIds = tBlogVo.getTagId().split(",");
            for (String id:tagIds) {
                tagService.decreaseTimes(Long.parseLong(id));
            }
        }
        if (bo.getTagId()!=null&& !bo.getTagId().equals("")){
            String[] tagIds = bo.getTagId().split(",");
            for (String id:tagIds) {
                tagService.increaseTimes(Long.parseLong(id));
            }
        }
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
        ids.forEach(blogId->{
            TBlogVo tBlogVo = queryById(blogId);
            if (tBlogVo.getTagId()!=null&& !tBlogVo.getTagId().equals("")){
                String[] tagIds = tBlogVo.getTagId().split("'");
                for (String id:tagIds) {
                    tagService.decreaseTimes(Long.parseLong(id));
                }
            }
        });
        return this.removeByIds(ids);
    }
}
