<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="标题" prop="title">
        <el-input
          v-model="queryParams.title"
          placeholder="请输入标题"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="标签" prop="tagId">
        <el-select v-model="queryParams.tagId" placeholder="请选择标签" clearable :style="{width: '100%'}">
          <el-option v-for="(item, index) in tagsOptions" :key="index" :label="item.tag"
                     :value="item.id" :disabled="item.disabled"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['tBlog:blog:add']"
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
          v-hasPermi="['tBlog:blog:edit']"
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
          v-hasPermi="['tBlog:blog:remove']"
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
          v-hasPermi="['tBlog:blog:export']"
        >导出
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="blogList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="标题" align="center" prop="title"/>
      <el-table-column label="简介" align="center" prop="summary"/>
      <el-table-column label="标签" align="center" prop="tagId">
        <template slot-scope="scope">
          <template v-if="scope.row.tagId!='' && scope.row.tagId!=undefined">
            <BlogTag v-for="tagid in scope.row.tagId.split(',')" :tag-color="tags[tagid].tagColor"
                     :tag-name="tags[tagid].tag"/>
          </template>
        </template>
      </el-table-column>
      <el-table-column label="类型" align="center" prop="blogType" :formatter="blogTypeFormat"/>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['tBlog:blog:edit']"
          >修改
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['tBlog:blog:remove']"
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
</template>

<script>
import {listBlog, getBlog, delBlog, addBlog, updateBlog, exportBlog} from "@/api/business/blog";
import Editor from '@/components/Editor';
import {listTag} from "@/api/business/tag";

export default {
  name: "Blog",
  components: {
    Editor,
  },
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
      // 博客表格数据
      blogList: [],
      tagsOptions: [],
      blogTypeOptions: [],
      tags: {},
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        title: undefined,
        tagId: undefined,
      },
    };
  },
  created() {
    this.getDicts("blog_type").then(response => {
      this.blogTypeOptions = response.data;
    });
    listTag().then(response => {
      this.tagsOptions = response.rows;
      this.tagsOptions.forEach(item => {
        this.tags[item.id] = item;
      })
    });
    this.getList();
  },
  methods: {
    blogTypeFormat(row, column) {
      return this.selectDictLabel(this.blogTypeOptions, row.blogType);
    },
    /** 查询博客列表 */
    getList() {
      this.loading = true;
      listBlog(this.queryParams).then(response => {
        this.blogList = response.rows;
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
        title: undefined,
        cover: undefined,
        summary: undefined,
        content: undefined,
        tagId: undefined,
        blogType: undefined,
        contentType: undefined,
        createTime: undefined,
        createBy: undefined,
        updateTime: undefined,
        updateBy: undefined,
        isDelete: undefined,
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
      this.$router.push({path: "/blogs/editBlog"});
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      this.$router.push({path: "/blogs/editBlog", query: {id: id}});
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$confirm('是否确认删除博客编号为"' + ids + '"的数据项?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        return delBlog(ids);
      }).then(() => {
        this.getList();
        this.msgSuccess("删除成功");
      })
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出所有博客数据项?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        return exportBlog(queryParams);
      }).then(response => {
        this.download(response.msg);
      })
    }
  }
};
</script>
