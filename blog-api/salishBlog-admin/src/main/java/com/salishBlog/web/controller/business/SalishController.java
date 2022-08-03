package com.salishBlog.web.controller.business;

import com.salishBlog.business.domain.TBlog;
import com.salishBlog.business.domain.bo.TBlogAddBo;
import com.salishBlog.business.domain.bo.TBlogEditBo;
import com.salishBlog.business.domain.bo.TBlogQueryBo;
import com.salishBlog.business.domain.bo.TTagQueryBo;
import com.salishBlog.business.domain.vo.TBlogVo;
import com.salishBlog.business.domain.vo.TTagVo;
import com.salishBlog.business.service.ITBlogService;
import com.salishBlog.business.service.ITTagService;
import com.salishBlog.common.annotation.Log;
import com.salishBlog.common.core.controller.BaseController;
import com.salishBlog.common.core.domain.AjaxResult;
import com.salishBlog.common.core.domain.entity.SysDictData;
import com.salishBlog.common.core.domain.entity.SysUser;
import com.salishBlog.common.core.domain.model.LoginUser;
import com.salishBlog.common.core.page.TableDataInfo;
import com.salishBlog.common.enums.BusinessType;
import com.salishBlog.common.utils.DateUtils;
import com.salishBlog.common.utils.ServletUtils;
import com.salishBlog.common.utils.StringUtils;
import com.salishBlog.common.utils.poi.ExcelUtil;
import com.salishBlog.framework.web.service.TokenService;
import com.salishBlog.system.service.ISysDictDataService;
import com.salishBlog.system.service.ISysDictTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
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
    private final ITTagService iTTagService;
    @Autowired
    private final ITBlogService iTBlogService;
    @Autowired
    private ISysDictTypeService dictTypeService;

    /**
     * 查询博客列表
     */
    @GetMapping("/blog/list")
    public TableDataInfo<TBlog> list(TBlog bo) {
        startPage();
        List<TBlog> list = iTBlogService.selectBlogByTag(bo);
        return getDataTable(list);
    }

    /**
     * 查询博客列表
     */
    @GetMapping("/blog/listInfo")
    public TableDataInfo<TBlog> listInfo(TBlog bo) {
        startPage();
        List<TBlog> list = iTBlogService.queryInfo(bo);
        return getDataTable(list);
    }

    /**
     * 获取博客详细信息
     */
    @GetMapping("/blog/{id}")
    public AjaxResult<TBlogVo> getInfo(@PathVariable("id" ) Long id) {
        return AjaxResult.success(iTBlogService.queryById(id));
    }

    /**
     * 查询标签列表
     */
    @GetMapping("/tag/list")
    public TableDataInfo<TTagVo> list(TTagQueryBo bo) {
        startPage();
        List<TTagVo> list = iTTagService.queryList(bo);
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
}
