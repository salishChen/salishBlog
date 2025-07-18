package com.salishBlog.web.controller.business;

import java.util.List;
import java.util.Arrays;

import com.alibaba.fastjson2.JSONObject;
import com.salishBlog.business.domain.TBlog;
import com.salishBlog.business.service.ITBlogService;
import com.salishBlog.common.core.domain.entity.SysUser;
import com.salishBlog.common.core.domain.model.LoginUser;
import com.salishBlog.common.utils.DateUtils;
import com.salishBlog.common.utils.ServletUtils;
import com.salishBlog.framework.web.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.salishBlog.common.annotation.Log;
import com.salishBlog.common.core.controller.BaseController;
import com.salishBlog.common.core.domain.AjaxResult;
import com.salishBlog.common.enums.BusinessType;
import com.salishBlog.business.domain.vo.TBlogVo;
import com.salishBlog.business.domain.bo.TBlogQueryBo;
import com.salishBlog.business.domain.bo.TBlogAddBo;
import com.salishBlog.business.domain.bo.TBlogEditBo;
import com.salishBlog.common.utils.poi.ExcelUtil;
import com.salishBlog.common.core.page.TableDataInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 博客Controller
 *
 * @author salish
 * @date 2022-07-21
 */
@Api(value = "博客控制器", tags = {"博客管理"})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/tBlog/blog")
public class TBlogController extends BaseController {

    private final ITBlogService blogService;
    @Autowired
    private TokenService tokenService;

    /**
     * 查询博客列表
     */
    @ApiOperation("查询博客列表")
    @PreAuthorize("@ss.hasPermi('tBlog:blog:list')")
    @GetMapping("/list")
    public TableDataInfo<TBlog> list(TBlog bo) {
        startPage();
        List<TBlog> list = blogService.selectBlogByTag(bo);
        return getDataTable(list);
    }

    /**
     * 查询博客列表
     */
    @ApiOperation("查询博客列表")
    @PreAuthorize("@ss.hasPermi('tBlog:blog:list')")
    @GetMapping("/listInfo")
    public TableDataInfo<TBlog> listInfo(TBlog bo) {
        startPage();
        List<TBlog> list = blogService.selectBlogByTag(bo);
        return getDataTable(list);
    }

    /**
     * 导出博客列表
     */
    @ApiOperation("导出博客列表")
    @PreAuthorize("@ss.hasPermi('tBlog:blog:export')")
    @Log(title = "博客", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult<TBlogVo> export(TBlogQueryBo bo) {
        List<TBlogVo> list = blogService.queryList(bo);
        ExcelUtil<TBlogVo> util = new ExcelUtil<TBlogVo>(TBlogVo.class);
        return util.exportExcel(list, "blog");
    }

    /**
     * 获取博客详细信息
     */
    @ApiOperation("获取博客详细信息")
    @PreAuthorize("@ss.hasPermi('tBlog:blog:query')")
    @GetMapping("/{id}")
    public AjaxResult<TBlogVo> getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(blogService.queryById(id));
    }

    /**
     * 新增博客
     */
    @ApiOperation("新增博客")
    @PreAuthorize("@ss.hasPermi('tBlog:blog:add')")
    @Log(title = "博客", businessType = BusinessType.INSERT)
    @PostMapping()
    public AjaxResult<TBlog> add(@RequestBody TBlogAddBo bo) {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        SysUser user = loginUser.getUser();
        bo.setCreateBy(user.getNickName());
        bo.setCreateTime(DateUtils.getNowDate());
        return AjaxResult.success(blogService.insertByAddBoReturn(bo));
    }

    /**
     * 修改博客
     */
    @ApiOperation("修改博客")
    @PreAuthorize("@ss.hasPermi('tBlog:blog:edit')")
    @Log(title = "博客", businessType = BusinessType.UPDATE)
    @PutMapping()
    public AjaxResult edit(@RequestBody TBlogEditBo bo) {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        SysUser user = loginUser.getUser();
        bo.setUpdateBy(user.getNickName());
        bo.setUpdateTime(DateUtils.getNowDate());
        return toAjax(blogService.updateByEditBo(bo) ? 1 : 0);
    }

    /**
     * 删除博客
     */
    @ApiOperation("删除博客")
    @PreAuthorize("@ss.hasPermi('tBlog:blog:remove')")
    @Log(title = "博客", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult<Void> remove(@PathVariable Long[] ids) {
        return toAjax(blogService.deleteWithValidByIds(Arrays.asList(ids), true) ? 1 : 0);
    }


    /**
     * 统计博客
     */
    @ApiOperation("统计博客")
    @PreAuthorize("@ss.hasPermi('tBlog:blog:list')")
    @GetMapping("/blogStatistic")
    public JSONObject blogStatistic() {
        return blogService.blogStatistic();
    }

    /**
     * 统计博客
     */
    @ApiOperation("统计博客")
    @PreAuthorize("@ss.hasPermi('tBlog:blog:list')")
    @GetMapping("/blogIntroduction")
    public JSONObject blogIntroduction() {
        return blogService.blogIntroduction();
    }
}
