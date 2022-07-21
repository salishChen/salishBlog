package com.salishBlog.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.Page;
import com.salishBlog.business.domain.bo.TTagAddBo;
import com.salishBlog.business.domain.bo.TTagQueryBo;
import com.salishBlog.business.domain.bo.TTagEditBo;
import com.salishBlog.business.domain.TTag;
import com.salishBlog.business.mapper.TTagMapper;
import com.salishBlog.business.domain.vo.TTagVo;
import com.salishBlog.business.service.ITTagService;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 标签Service业务层处理
 *
 * @author salishChen
 * @date 2022-07-21
 */
@Service
public class TTagServiceImpl extends ServiceImpl<TTagMapper, TTag> implements ITTagService {

    @Override
    public TTagVo queryById(Long id){
        TTag db = this.baseMapper.selectById(id);
        return BeanUtil.toBean(db, TTagVo.class);
    }

    @Override
    public List<TTagVo> queryList(TTagQueryBo bo) {
        LambdaQueryWrapper<TTag> lqw = Wrappers.lambdaQuery();
        lqw.eq(StrUtil.isNotBlank(bo.getTag()), TTag::getTag, bo.getTag());
        lqw.eq(StrUtil.isNotBlank(bo.getTagColor()), TTag::getTagColor, bo.getTagColor());
        lqw.eq(bo.getTimes() != null, TTag::getTimes, bo.getTimes());
        return entity2Vo(this.list(lqw));
    }

    /**
    * 实体类转化成视图对象
    *
    * @param collection 实体类集合
    * @return
    */
    private List<TTagVo> entity2Vo(Collection<TTag> collection) {
        List<TTagVo> voList = collection.stream()
                .map(any -> BeanUtil.toBean(any, TTagVo.class))
                .collect(Collectors.toList());
        if (collection instanceof Page) {
            Page<TTag> page = (Page<TTag>)collection;
            Page<TTagVo> pageVo = new Page<>();
            BeanUtil.copyProperties(page,pageVo);
            pageVo.addAll(voList);
            voList = pageVo;
        }
        return voList;
    }

    @Override
    public Boolean insertByAddBo(TTagAddBo bo) {
        TTag add = BeanUtil.toBean(bo, TTag.class);
        validEntityBeforeSave(add);
        return this.save(add);
    }

    @Override
    public Boolean updateByEditBo(TTagEditBo bo) {
        TTag update = BeanUtil.toBean(bo, TTag.class);
        validEntityBeforeSave(update);
        return this.updateById(update);
    }

    /**
     * 保存前的数据校验
     *
     * @param entity 实体类数据
     */
    private void validEntityBeforeSave(TTag entity){
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
