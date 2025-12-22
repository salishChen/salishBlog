<template>
  <div class="app-container">
    <el-form class="app-search card" :model="queryParams" ref="queryFormRef" :inline="true" v-show="showSearch"
             label-width="68px">
      <el-form-item label="标题" prop="title">
        <el-input
            v-model="queryParams.title"
            placeholder="请输入标题"
            clearable
            size="default"
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="博客类型" prop="blogType">
        <el-select v-model="queryParams.blogType" size="default" placeholder="请选择博客类型" clearable
                   :style="{width: '100%'}">
          <el-option v-for="(item, index) in blogTypeOptions" :key="index" :label="item.dictLabel"
                     :value="item.dictValue" :disabled="item.disabled"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="标签" prop="tagId">
        <el-select v-model="queryParams.tagId" size="default" placeholder="请选择标签" clearable
                   :style="{width: '100%'}">
          <el-option v-for="(item, index) in tagsOptions" :key="index" :label="item.tag"
                     :value="item.id" :disabled="item.disabled"></el-option>
        </el-select>
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
              v-hasPermi="['tBlog:blog:add']"
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
              v-hasPermi="['tBlog:blog:edit']"
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
              v-hasPermi="['tBlog:blog:remove']"
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
              v-hasPermi="['tBlog:blog:export']"
          >导出
          </el-button>
        </el-col>
        <right-toolbar :showSearch="showSearch" @update:showSearch="showSearch = $event"
                       @queryTable="getList"></right-toolbar>
      </el-row>

      <el-table v-loading="loading" :data="blogList" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center"/>
        <el-table-column label="标题" align="center" prop="title"/>
        <el-table-column label="简介" align="center" prop="summary"/>
        <el-table-column label="标签" align="center" prop="tagId">
          <template #default="scope">
            <template v-if="scope.row.tagId!='' && scope.row.tagId!=undefined">
              <BlogTag v-for="tagid in scope.row.tagId.split(',')" :key="tagid" :tag-color="tags[tagid]?.tagColor"
                       :tag-name="tags[tagid]?.tag"/>
            </template>
          </template>
        </el-table-column>
        <el-table-column label="类型" align="center" prop="blogType" :formatter="blogTypeFormat"/>
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template #default="scope">
            <el-button
                size="default"
                type="text"
                icon="Edit"
                @click="handleUpdate(scope.row)"
                v-hasPermi="['tBlog:blog:edit']"
            >修改
            </el-button>
            <el-button
                size="default"
                type="text"
                icon="delete"
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
          v-model:page="queryParams.pageNum"
          v-model:limit="queryParams.pageSize"
          @pagination="getList"
      />
    </div>
  </div>
</template>

<script setup>
import {ref, reactive} from 'vue'
import {useRouter} from 'vue-router'
import {ElMessage, ElMessageBox} from 'element-plus'
import {listBlog, delBlog, exportBlog} from "@/api/business/blog"
import {listTag} from "@/api/business/tag"
import {getDicts} from "@/api/system/dict/data"
import BlogTag from '@/components/Tag/index.vue'

// 定义响应式数据
const loading = ref(true)
const ids = ref([])
const single = ref(true)
const multiple = ref(true)
const showSearch = ref(true)
const total = ref(0)
const blogList = ref([])
const tagsOptions = ref([])
const blogTypeOptions = ref([])
const tags = ref({})
const queryFormRef = ref(null)

// 查询参数
const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  title: undefined,
  tagId: undefined,
  blogType: undefined
})

// 路由实例
const router = useRouter()

// 格式化博客类型显示
const blogTypeFormat = (row, column) => {
  return selectDictLabel(blogTypeOptions.value, row.blogType)
}

// 获取字典标签
const selectDictLabel = (datas, value) => {
  if (value === undefined) {
    return ""
  }
  var actions = []
  Object.keys(datas).some((key) => {
    if (datas[key].dictValue === ('' + value)) {
      actions.push(datas[key].dictLabel)
      return true
    }
  })
  if (actions.length === 0) {
    actions.push(value)
  }
  return actions.join('')
}

// 获取博客列表
const getList = () => {
  loading.value = true
  listBlog(queryParams).then(response => {
    blogList.value = response.rows
    total.value = response.total
    loading.value = false
  })
}

// 搜索按钮操作
const handleQuery = () => {
  queryParams.pageNum = 1
  getList()
}

// 重置按钮操作
const resetQuery = () => {
  if (queryFormRef.value) {
    queryFormRef.value.resetFields()
  }
  handleQuery()
}

// 多选框选中数据
const handleSelectionChange = (selection) => {
  ids.value = selection.map(item => item.id)
  single.value = selection.length !== 1
  multiple.value = !selection.length
}

// 新增按钮操作
const handleAdd = () => {
  router.push({path: "/blogs/editBlog"})
}

// 修改按钮操作
const handleUpdate = (row) => {
  const id = row.id || ids.value
  router.push({path: "/blogs/editBlog", query: {id: id}})
}

// 删除按钮操作
const handleDelete = (row) => {
  const deleteIds = row.id || ids.value
  ElMessageBox.confirm('是否确认删除博客编号为"' + deleteIds + '"的数据项?', "警告", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning"
  }).then(() => {
    return delBlog(deleteIds)
  }).then(() => {
    getList()
    ElMessage.success("删除成功")
  }).catch(() => {
  })
}

// 导出按钮操作
const handleExport = () => {
  ElMessageBox.confirm('是否确认导出所有博客数据项?', "警告", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning"
  }).then(() => {
    return exportBlog(queryParams)
  }).then(response => {
    // 这里假设有一个下载方法，根据实际项目情况调整
    // download(response.msg)
  }).catch(() => {
  })
}

// 组件创建时获取字典和标签数据
getDicts("blog_type").then(response => {
  blogTypeOptions.value = response.data
})

listTag().then(response => {
  tagsOptions.value = response.rows
  tagsOptions.value.forEach(item => {
    tags.value[item.id] = item
  })
})

// 组件激活时获取列表
getList()
</script>
