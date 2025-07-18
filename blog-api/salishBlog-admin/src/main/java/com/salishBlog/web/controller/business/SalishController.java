package com.salishBlog.web.controller.business;

import com.alibaba.fastjson2.JSONObject;
import com.salishBlog.business.domain.TBlog;
import com.salishBlog.business.domain.bo.*;
import com.salishBlog.business.domain.vo.TBlogVo;
import com.salishBlog.business.domain.vo.TFillPitVo;
import com.salishBlog.business.domain.vo.TTagVo;
import com.salishBlog.business.service.ITBlogService;
import com.salishBlog.business.service.ITFillPitService;
import com.salishBlog.business.service.ITTagService;
import com.salishBlog.common.core.controller.BaseController;
import com.salishBlog.common.core.domain.AjaxResult;
import com.salishBlog.common.core.domain.entity.SysDictData;
import com.salishBlog.common.core.page.TableDataInfo;
import com.salishBlog.common.utils.StringUtils;
import com.salishBlog.system.service.ISysDictTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 博客Controller
 *
 * @author salish
 * @date 2022-07-21
 */
@Api(value = "博客控制器", tags = {"博客管理"})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/salish")
public class SalishController extends BaseController {
    @Autowired
    private final ITTagService tagService;
    @Autowired
    private final ITBlogService blogService;
    @Autowired
    private ISysDictTypeService dictTypeService;
    @Autowired
    private ITFillPitService fillPitService;

    /**
     * 查询博客列表
     */
    @GetMapping("/blog/list")
    public TableDataInfo<TBlog> list(TBlog bo) {
        startPage();
        List<TBlog> list = blogService.selectBlogByTag(bo);
        return getDataTable(list);
    }

    /**
     * 博客归档
     */
    @GetMapping("/blog/interfile")
    public AjaxResult interfile(TBlog bo) {
        startPage();
        return blogService.interfile(bo);
    }

    /**
     * 查询博客列表
     */
    @GetMapping("/blog/listInfo")
    public TableDataInfo<TBlog> listInfo(TBlog bo) {
        startPage();
        List<TBlog> list = blogService.queryInfo(bo);
        return getDataTable(list);
    }

    /**
     * 获取博客详细信息
     */
    @GetMapping("/blog/{id}")
    public AjaxResult<TBlogVo> getInfo(@PathVariable("id" ) Long id) {
        return AjaxResult.success(blogService.queryById(id));
    }

    /**
     * 查询标签列表
     */
    @GetMapping("/tag/list")
    public TableDataInfo<TTagVo> list(TTagQueryBo bo) {
        startPage();
        List<TTagVo> list = tagService.queryList(bo);
        return getDataTable(list);
    }

    /**
     * 根据字典类型查询字典数据信息
     */
    @GetMapping(value = "/dict/data/type/{dictType}")
    public AjaxResult dictType(@PathVariable String dictType)
    {
        List<SysDictData> data = dictTypeService.selectDictDataByType(dictType);
        if (StringUtils.isNull(data))
        {
            data = new ArrayList<SysDictData>();
        }
        return AjaxResult.success(data);
    }

    /**
     * 查询填坑标签列表
     */
    @GetMapping("/pit/tag")
    public AjaxResult getPitTags() {
        return AjaxResult.success(fillPitService.getTags());
    }

    /**
     * 查询填坑列表
     */
    @ApiOperation("查询填坑列表")
    @GetMapping("/pit/pits")
    public TableDataInfo<TFillPitVo> list(TFillPitQueryBo bo) {
        startPage();
        List<TFillPitVo> list = fillPitService.queryList(bo);
        return getDataTable(list);
    }

    /**
     * 统计博客
     */
    @ApiOperation("统计博客")
    @GetMapping("/blogIntroduction")
    public JSONObject blogIntroduction() {
        return blogService.blogIntroduction();
    }
}
