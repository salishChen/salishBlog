<template>
  <div class="app-container">
    <el-form class="app-search card" :model="queryParams" ref="queryFormRef" :inline="true" v-show="showSearch"
             label-width="68px">
      <el-form-item label="标签" prop="tag">
        <el-input
            v-model="queryParams.tag"
            placeholder="请输入标签"
            clearable
            size="default"
            @keyup.enter="handleQuery"
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
              v-hasPermi="['business:pit:add']"
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
              v-hasPermi="['business:pit:edit']"
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
              v-hasPermi="['business:pit:remove']"
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
              v-hasPermi="['business:pit:export']"
          >导出
          </el-button>
        </el-col>
        <right-toolbar :showSearch="showSearch" @update:showSearch="showSearch = $event"
                       @queryTable="getList"></right-toolbar>
      </el-row>

      <el-table v-loading="loading" :data="pitList" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center"/>
        <el-table-column label="id" align="center" prop="id" v-if="false"/>
        <el-table-column label="内容" align="center" prop="content"/>
        <el-table-column label="标签" align="center" prop="tag"/>
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template #default="scope">
            <el-button
                size="default"
                type="text"
                icon="Edit"
                @click="handleUpdate(scope.row)"
                v-hasPermi="['business:pit:edit']"
            >修改
            </el-button>
            <el-button
                size="default"
                type="text"
                icon="delete"
                @click="handleDelete(scope.row)"
                v-hasPermi="['business:pit:remove']"
            >删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <pagination
          v-show="total>0"
          :total="total"
          :page="queryParams.pageNum"
          :limit="queryParams.pageSize"
          @pagination="getList"
      />
    </div>

    <!-- 添加或修改填坑对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="内容">
          <editor v-model="form.content" :min-height="192"/>
        </el-form-item>
        <el-form-item label="标签" prop="tag">
          <el-input v-model="form.tag" placeholder="请输入标签"/>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import {ref, reactive} from 'vue'
import {ElMessage, ElMessageBox} from 'element-plus'
import {listPit, getPit, delPit, addPit, updatePit, exportPit} from "@/api/business/pit"
import Editor from '@/components/Editor'

// 定义响应式数据
const loading = ref(true)
const ids = ref([])
const single = ref(true)
const multiple = ref(true)
const showSearch = ref(true)
const total = ref(0)
const pitList = ref([])
const title = ref("")
const open = ref(false)
const queryFormRef = ref(null)
const formRef = ref(null)

// 查询参数
const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  content: undefined,
  tag: undefined,
})

// 表单参数
const form = reactive({
  id: undefined,
  content: undefined,
  tag: undefined,
  createTime: undefined,
  createBy: undefined,
  updateTime: undefined,
  updateBy: undefined,
})

// 表单校验规则
const rules = {}

// 查询填坑列表
const getList = () => {
  loading.value = true
  listPit(queryParams).then(response => {
    pitList.value = response.rows
    total.value = response.total
    loading.value = false
  })
}

// 取消按钮
const cancel = () => {
  open.value = false
  reset()
}

// 表单重置
const reset = () => {
  Object.assign(form, {
    id: undefined,
    content: undefined,
    tag: undefined,
    createTime: undefined,
    createBy: undefined,
    updateTime: undefined,
    updateBy: undefined,
  })
  if (formRef.value) {
    formRef.value.resetFields()
  }
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
  reset()
  open.value = true
  title.value = "添加填坑"
}

// 修改按钮操作
const handleUpdate = (row) => {
  reset()
  const id = row.id || ids.value
  getPit(id).then(response => {
    Object.assign(form, response.data)
    open.value = true
    title.value = "修改填坑"
  })
}

// 提交按钮
const submitForm = () => {
  if (formRef.value) {
    formRef.value.validate((valid) => {
      if (valid) {
        if (form.id != null) {
          updatePit(form).then(response => {
            ElMessage.success("修改成功")
            open.value = false
            getList()
          })
        } else {
          addPit(form).then(response => {
            ElMessage.success("新增成功")
            open.value = false
            getList()
          })
        }
      }
    })
  }
}

// 删除按钮操作
const handleDelete = (row) => {
  const deleteIds = row.id || ids.value
  ElMessageBox.confirm('是否确认删除填坑编号为"' + deleteIds + '"的数据项?', "警告", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning"
  }).then(() => {
    return delPit(deleteIds)
  }).then(() => {
    getList()
    ElMessage.success("删除成功")
  }).catch(() => {
  })
}

// 导出按钮操作
const handleExport = () => {
  ElMessageBox.confirm('是否确认导出所有填坑数据项?', "警告", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning"
  }).then(() => {
    return exportPit(queryParams)
  }).then(response => {
    // 假设有一个下载方法
    // download(response.msg)
  }).catch(() => {
  })
}

// 组件挂载后获取列表
getList()
</script>
