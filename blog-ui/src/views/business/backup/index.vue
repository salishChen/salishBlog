<template>
  <div class="app-container">
    <!--    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
          <el-form-item>
            <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
            <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
          </el-form-item>
        </el-form>-->

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['business:backup:add']"
        >新增
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['business:backup:edit']"
        >修改
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['business:backup:remove']"
        >删除
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['business:backup:export']"
        >导出
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="backupList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="id" align="center" prop="id" v-if="false"/>
      <el-table-column label="数据库名称" align="center" prop="sqlName"/>
      <el-table-column label="数据库ip" align="center" prop="sqlIp"/>
<!--      <el-table-column label="用户名" align="center" prop="sqlUsername"/>-->
<!--      <el-table-column label="密码" align="center" prop="sqlPassword"/>-->
      <el-table-column label="定时规则" align="center" prop="sqlCron"/>
      <el-table-column label="备份数量限制" align="center" prop="fileLimit"/>
      <el-table-column label="数据库类型" align="center" prop="type" :formatter="typeFormat"/>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleRun(scope.row)"
            v-hasPermi="['business:backup:edit']"
          >手动备份
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['business:backup:edit']"
          >修改
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['business:backup:remove']"
          >删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :fileLimit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改数据库备份对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="400px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="数据库名称" prop="sqlName">
          <el-input v-model="form.sqlName" placeholder="请输入数据库名称"/>
        </el-form-item>
        <el-form-item label="数据库IP" prop="sqlIp">
          <el-input v-model="form.sqlIp" placeholder="请输入数据库IP"/>
        </el-form-item>
        <el-form-item label="用户名" prop="sqlUsername">
          <el-input v-model="form.sqlUsername" placeholder="请输入用户名"/>
        </el-form-item>
        <el-form-item label="密码" prop="sqlPassword">
          <el-input v-model="form.sqlPassword" placeholder="请输入密码"/>
        </el-form-item>
        <el-form-item label="定时规则" prop="sqlCron">
          <el-input v-model="form.sqlCron" placeholder="请输入定时规则"/>
        </el-form-item>
        <el-form-item label="备份数量限制" prop="fileLimit">
          <el-input v-model="form.fileLimit" placeholder="请输入备份数量限制"/>
        </el-form-item>
        <el-form-item label="数据库类型" prop="type">
          <el-select style="width: 100%" v-model="form.type" placeholder="请选择数据库类型">
            <el-option
              v-for="dict in typeOptions"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="dict.dictValue"
            ></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {listBackup, getBackup, delBackup, addBackup, updateBackup, exportBackup,runBackup} from "@/api/business/backup";

export default {
  name: "Backup",
  components: {},
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 数据库备份表格数据
      backupList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 数据库类型字典
      typeOptions: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {}
    };
  },
  created() {
    this.getList();
    this.getDicts("sql_type").then(response => {
      this.typeOptions = response.data;
    });
  },
  methods: {
    /** 查询数据库备份列表 */
    getList() {
      this.loading = true;
      listBackup(this.queryParams).then(response => {
        this.backupList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 数据库类型字典翻译
    typeFormat(row, column) {
      return this.selectDictLabel(this.typeOptions, row.type);
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: undefined,

        sqlIp: undefined,

        sqlUsername: undefined,

        sqlPassword: undefined,

        sqlCron: undefined,

        fileLimit: undefined,

        type: undefined,

      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加数据库备份";
    },
    /** 修改按钮操作 */
    handleRun(row) {
      this.reset();
      const id = row.id || this.ids
      runBackup({id:id}).then(response => {
        console.log(response)
      });
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getBackup(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改数据库备份";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateBackup(this.form).then(response => {
              this.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addBackup(this.form).then(response => {
              this.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$confirm('是否确认删除数据库备份编号为"' + ids + '"的数据项?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        return delBackup(ids);
      }).then(() => {
        this.getList();
        this.msgSuccess("删除成功");
      })
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出所有数据库备份数据项?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        return exportBackup(queryParams);
      }).then(response => {
        this.download(response.msg);
      })
    }
  }
};
</script>
