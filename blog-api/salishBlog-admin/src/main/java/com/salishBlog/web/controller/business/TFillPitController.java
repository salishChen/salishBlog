package com.salishBlog.web.controller.business;

import java.util.List;
import java.util.Arrays;

import com.salishBlog.business.service.ITFillPitService;
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
import com.salishBlog.business.domain.vo.TFillPitVo;
import com.salishBlog.business.domain.bo.TFillPitQueryBo;
import com.salishBlog.business.domain.bo.TFillPitAddBo;
import com.salishBlog.business.domain.bo.TFillPitEditBo;
import com.salishBlog.common.utils.poi.ExcelUtil;
import com.salishBlog.common.core.page.TableDataInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 填坑Controller
 *
 * @author salishChen
 * @date 2023-06-26
 */
@Api(value = "填坑控制器", tags = {"填坑管理"})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/business/pit")
public class TFillPitController extends BaseController {

    private final ITFillPitService iTFillPitService;
    @Autowired
    private TokenService tokenService;
    /**
     * 查询填坑列表
     */
    @ApiOperation("查询填坑列表")
    @PreAuthorize("@ss.hasPermi('business:pit:list')")
    @GetMapping("/list")
    public TableDataInfo<TFillPitVo> list(TFillPitQueryBo bo) {
        startPage();
        List<TFillPitVo> list = iTFillPitService.queryList(bo);
        return getDataTable(list);
    }

    /**
     * 导出填坑列表
     */
    @ApiOperation("导出填坑列表")
    @PreAuthorize("@ss.hasPermi('business:pit:export')")
    @Log(title = "填坑", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult<TFillPitVo> export(TFillPitQueryBo bo) {
        List<TFillPitVo> list = iTFillPitService.queryList(bo);
        ExcelUtil<TFillPitVo> util = new ExcelUtil<TFillPitVo>(TFillPitVo.class);
        return util.exportExcel(list, "pit" );
    }

    /**
     * 获取填坑详细信息
     */
    @ApiOperation("获取填坑详细信息")
    @PreAuthorize("@ss.hasPermi('business:pit:query')")
    @GetMapping("/{id}")
    public AjaxResult<TFillPitVo> getInfo(@PathVariable("id" ) Long id) {
        return AjaxResult.success(iTFillPitService.queryById(id));
    }

    /**
     * 新增填坑
     */
    @ApiOperation("新增填坑")
    @PreAuthorize("@ss.hasPermi('business:pit:add')")
    @Log(title = "填坑", businessType = BusinessType.INSERT)
    @PostMapping()
    public AjaxResult<Void> add(@RequestBody TFillPitAddBo bo) {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        SysUser user = loginUser.getUser();
        bo.setCreateBy(user.getNickName());
        bo.setCreateTime(DateUtils.getNowDate());
        return toAjax(iTFillPitService.insertByAddBo(bo) ? 1 : 0);
    }

    /**
     * 修改填坑
     */
    @ApiOperation("修改填坑")
    @PreAuthorize("@ss.hasPermi('business:pit:edit')")
    @Log(title = "填坑", businessType = BusinessType.UPDATE)
    @PutMapping()
    public AjaxResult<Void> edit(@RequestBody TFillPitEditBo bo) {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        SysUser user = loginUser.getUser();
        bo.setUpdateBy(user.getNickName());
        bo.setUpdateTime(DateUtils.getNowDate());
        return toAjax(iTFillPitService.updateByEditBo(bo) ? 1 : 0);
    }

    /**
     * 删除填坑
     */
    @ApiOperation("删除填坑")
    @PreAuthorize("@ss.hasPermi('business:pit:remove')")
    @Log(title = "填坑" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult<Void> remove(@PathVariable Long[] ids) {
        return toAjax(iTFillPitService.deleteWithValidByIds(Arrays.asList(ids), true) ? 1 : 0);
    }
}
