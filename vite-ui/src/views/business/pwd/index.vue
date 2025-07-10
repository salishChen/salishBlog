<template>
  <div class="app-container">
    <el-form class="app-search card" :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch"
             label-width="68px">
      <el-form-item label="秘钥名" prop="keyName">
        <el-input
            v-model="queryParams.keyName"
            placeholder="请输入秘钥名"
            clearable
            size="default"
            @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="default" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="default" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>
    <div class="app-content card">
      <el-row :gutter="10" class="mb8">
        <el-col :span="1.5">
          <el-button
              type="primary"
              plain
              icon="Plus"
              size="default"
              @click="handleAdd"
              v-hasPermi="['business:pwd:add']"
          >新增
          </el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button
              type="success"
              plain
              icon="Edit"
              size="default"
              :disabled="single"
              @click="handleUpdate"
              v-hasPermi="['business:pwd:edit']"
          >修改
          </el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button
              type="danger"
              plain
              icon="delete"
              size="default"
              :disabled="multiple"
              @click="handleDelete"
              v-hasPermi="['business:pwd:remove']"
          >删除
          </el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button
              type="warning"
              plain
              icon="Download"
              size="default"
              @click="handleExport"
              v-hasPermi="['business:pwd:export']"
          >导出
          </el-button>
        </el-col>
        <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
      </el-row>

      <el-table v-loading="loading" :data="pwdList" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center"/>
        <el-table-column label="id" align="center" prop="id" v-if="false"/>
        <el-table-column label="秘钥名" align="center" prop="keyName"/>
        <el-table-column label="秘钥" align="center" prop="keyValue"/>
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template #default="scope">
            <el-button
                size="default"
                type="text"
                icon="Edit"
                @click="handleUpdate(scope.row)"
                v-hasPermi="['business:pwd:edit']"
            >修改
            </el-button>
            <el-button
                size="default"
                type="text"
                icon="delete"
                @click="handleDelete(scope.row)"
                v-hasPermi="['business:pwd:remove']"
            >删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <pagination
          v-show="total>0"
          :total="total"
          :page.sync="queryParams.pageNum"
          :limit.sync="queryParams.pageSize"
          @pagination="getList"
      />
    </div>
    <!-- 添加或修改秘钥本对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="1000px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="秘钥名" prop="keyName">
          <el-input v-model="form.keyName" placeholder="请输入秘钥名"/>
        </el-form-item>
        <el-form-item label="秘钥" prop="keyValue">
          <el-input v-model="form.keyValue" placeholder="请输入秘钥"/>
        </el-form-item>
        <el-form-item label="其他秘钥">
          <el-table :data="keyList">
            <el-table-column label="key" align="center">
              <template #default="scope">
                <el-input name="score" v-model="scope.row.key"></el-input>
              </template>
            </el-table-column>
            <el-table-column label="value" align="center">
              <template #default="scope">
                <el-input name="score" v-model="scope.row.value"></el-input>
              </template>
            </el-table-column>
          </el-table>
        </el-form-item>
        <el-row style="text-align: center">
          <el-col :span="2" :push="20">
            <el-button @click="addStatus">+</el-button>
          </el-col>
          <el-col :span="2" :push="20">
            <el-button @click="minusStatus">-</el-button>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {listPwd, getPwd, delPwd, addPwd, updatePwd, exportPwd} from "@/api/business/pwd";

export default {
  name: "Pwd",
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
      // 秘钥本表格数据
      pwdList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        keyName: undefined,
      },
      //key列表
      keyList: [{key: '', value: ''}],
      // 表单参数
      form: {
        stateList: '',
      },
      // 表单校验
      rules: {}
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询秘钥本列表 */
    getList() {
      this.loading = true;
      listPwd(this.queryParams).then(response => {
        this.pwdList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
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
        keyName: undefined,
        keyValue: undefined,
        otherKeys: undefined,
      };
      this.resetForm("form");
      this.keyList = [{key: '', value: ''}];
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
      this.title = "添加秘钥本";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getPwd(id).then(response => {
        this.form = response.data;
        if (this.form.otherKeys != null && this.form.otherKeys != '') {
          this.keyList = JSON.parse(this.form.otherKeys)
        }
        this.open = true;
        this.title = "修改秘钥本";
      });
    },
    /** 提交按钮 */
    submitForm() {
      let otherKeys = [];
      this.keyList.forEach((item, index) => {
        if (item.key != "" && item.key != undefined) {
          otherKeys.push("{\"key\":" + "\"" + item.key + "\"" + ",\"value\":" + "\"" + item.value + "\"" + "}")
        }
      })
      this.form.otherKeys = "[" + otherKeys.toString() + "]";

      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updatePwd(this.form).then(response => {
              this.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addPwd(this.form).then(response => {
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
      this.$confirm('是否确认删除秘钥本编号为"' + ids + '"的数据项?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        return delPwd(ids);
      }).then(() => {
        this.getList();
        this.msgSuccess("删除成功");
      })
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出所有秘钥本数据项?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        return exportPwd(queryParams);
      }).then(response => {
        this.download(response.msg);
      })
    },
    addStatus() {
      this.keyList.push({key: '', value: ''})
    },
    minusStatus() {
      this.keyList.pop()
    },
  }
};
</script>
