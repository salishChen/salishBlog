<template>
  <div class="app-container">
    <el-form class="app-search card" :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch"
             label-width="68px">
      <el-form-item label="标签名" prop="tag">
        <el-input
            v-model="queryParams.tag"
            placeholder="请输入标签"
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
              v-hasPermi="['tTag:tag:add']"
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
              v-hasPermi="['tTag:tag:edit']"
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
              v-hasPermi="['tTag:tag:remove']"
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
              v-hasPermi="['tTag:tag:export']"
          >导出
          </el-button>
        </el-col>
        <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
      </el-row>

      <el-table v-loading="loading" :data="tagList" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center"/>
        <el-table-column label="标签" align="center" prop="tag"/>
        <el-table-column label="标签色彩" align="center" prop="tagColor">
          <template #default="scope">
            <div :style="{backgroundColor:scope.row.tagColor,width:'25px',height:'25px',display:'inline-block'}"></div>
          </template>
        </el-table-column>
        <el-table-column label="使用次数" align="center" prop="times" width="180"/>
        <!--          <span>{{ parseTime(scope.row.times, '{y}-{m}-{d}') }}</span>-->
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template #default="scope">
            <el-button
                size="default"
                type="text"
                icon="Edit"
                @click="handleUpdate(scope.row)"
                v-hasPermi="['tTag:tag:edit']"
            >修改
            </el-button>
            <el-button
                size="default"
                type="text"
                icon="delete"
                @click="handleDelete(scope.row)"
                v-hasPermi="['tTag:tag:remove']"
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
    <!-- 添加或修改标签对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="标签" prop="tag">
          <el-input v-model="form.tag" placeholder="请输入标签"/>
        </el-form-item>
        <el-form-item label="标签色彩" prop="tagColor">
          <el-color-picker
              v-model="form.tagColor"
              show-alpha
              :predefine="predefineColors">
          </el-color-picker>
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
import {listTag, getTag, delTag, addTag, updateTag, exportTag} from "@/api/business/tag";

export default {
  name: "Tag",
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
      // 标签表格数据
      tagList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        tag: undefined,
        tagColor: undefined,
        times: undefined,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {},
      predefineColors: [
        '#ff4500',
        '#ff8c00',
        '#ffd700',
        '#90ee90',
        '#00ced1',
        '#1e90ff',
        '#c71585',
        'rgba(255, 69, 0, 0.68)',
        'rgb(255, 120, 0)',
        'hsv(51, 100, 98)',
        'hsva(120, 40, 94, 0.5)',
        'hsl(181, 100%, 37%)',
        'hsla(209, 100%, 56%, 0.73)',
        '#c7158577'
      ]
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询标签列表 */
    getList() {
      this.loading = true;
      listTag(this.queryParams).then(response => {
        this.tagList = response.rows;
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

        tag: undefined,

        tagColor: undefined,

        times: undefined,

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
      this.title = "添加标签";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getTag(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改标签";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateTag(this.form).then(response => {
              this.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addTag(this.form).then(response => {
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
      this.$confirm('是否确认删除标签编号为"' + ids + '"的数据项?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        return delTag(ids);
      }).then(() => {
        this.getList();
        this.msgSuccess("删除成功");
      })
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出所有标签数据项?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        return exportTag(queryParams);
      }).then(response => {
        this.download(response.msg);
      })
    }
  }
};
</script>
