package com.salishBlog.quartz.controller;

import java.util.List;
import java.util.Arrays;

import com.salishBlog.quartz.domain.SqlBackup;
import com.salishBlog.quartz.domain.SysJob;
import lombok.RequiredArgsConstructor;
import org.quartz.SchedulerException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
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
import com.salishBlog.quartz.domain.vo.SqlBackupVo;
import com.salishBlog.quartz.domain.bo.SqlBackupQueryBo;
import com.salishBlog.quartz.domain.bo.SqlBackupAddBo;
import com.salishBlog.quartz.domain.bo.SqlBackupEditBo;
import com.salishBlog.quartz.service.ISqlBackupService;
import com.salishBlog.common.utils.poi.ExcelUtil;
import com.salishBlog.common.core.page.TableDataInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 数据库备份Controller
 *
 * @author salishChen
 * @date 2024-04-23
 */
@Api(value = "数据库备份控制器", tags = {"数据库备份管理"})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/business/backup")
public class SqlBackupController extends BaseController {

    private final ISqlBackupService iSqlBackupService;
    /**
     * 查询数据库备份列表
     */
    @ApiOperation("查询数据库备份列表")
    @PreAuthorize("@ss.hasPermi('business:backup:list')")
    @GetMapping("/list")
    public TableDataInfo<SqlBackupVo> list(SqlBackupQueryBo bo) {
        startPage();
        List<SqlBackupVo> list = iSqlBackupService.queryList(bo);
        return getDataTable(list);
    }

    /**
     * 导出数据库备份列表
     */
    @ApiOperation("导出数据库备份列表")
    @PreAuthorize("@ss.hasPermi('business:backup:export')")
    @Log(title = "数据库备份", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult<SqlBackupVo> export(SqlBackupQueryBo bo) {
        List<SqlBackupVo> list = iSqlBackupService.queryList(bo);
        ExcelUtil<SqlBackupVo> util = new ExcelUtil<SqlBackupVo>(SqlBackupVo.class);
        return util.exportExcel(list, "backup" );
    }

    /**
     * 获取数据库备份详细信息
     */
    @ApiOperation("获取数据库备份详细信息")
    @PreAuthorize("@ss.hasPermi('business:backup:query')")
    @GetMapping("/{id}")
    public AjaxResult<SqlBackupVo> getInfo(@PathVariable("id" ) Long id) {
        return AjaxResult.success(iSqlBackupService.queryById(id));
    }

    /**
     * 新增数据库备份
     */
    @ApiOperation("新增数据库备份")
    @PreAuthorize("@ss.hasPermi('business:backup:add')")
    @Log(title = "数据库备份", businessType = BusinessType.INSERT)
    @PostMapping()
    public AjaxResult<Void> add(@RequestBody @Validated SqlBackupAddBo bo) {

        return toAjax(iSqlBackupService.insertByAddBo(bo) ? 1 : 0);
    }

    /**
     * 修改数据库备份
     */
    @ApiOperation("修改数据库备份")
    @PreAuthorize("@ss.hasPermi('business:backup:edit')")
    @Log(title = "数据库备份", businessType = BusinessType.UPDATE)
    @PutMapping()
    public AjaxResult<Void> edit(@RequestBody @Validated SqlBackupEditBo bo) {

        return toAjax(iSqlBackupService.updateByEditBo(bo) ? 1 : 0);
    }

    /**
     * 删除数据库备份
     */
    @ApiOperation("删除数据库备份")
    @PreAuthorize("@ss.hasPermi('business:backup:remove')")
    @Log(title = "数据库备份" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult<Void> remove(@PathVariable Long[] ids) {
        return toAjax(iSqlBackupService.deleteWithValidByIds(Arrays.asList(ids), true) ? 1 : 0);
    }

    /**
     * 定时任务立即执行一次
     */
    @PreAuthorize("@ss.hasPermi('business:backup:edit')")
    @Log(title = "定时任务", businessType = BusinessType.UPDATE)
    @PutMapping("/run")
    public AjaxResult run(@RequestBody SqlBackup backup) throws SchedulerException
    {
        iSqlBackupService.run(backup);
        return AjaxResult.success();
    }
}
