<template>
  <div class="app-container">
    <div class="app-content card">
      <el-row :gutter="10" class="mb8">
        <el-col :span="1.5">
          <el-button
              type="primary"
              plain
              icon="Plus"
              size="default"
              @click="handleAdd"
              v-hasPermi="['business:backup:add']"
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
              v-hasPermi="['business:backup:edit']"
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
              v-hasPermi="['business:backup:remove']"
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
              v-hasPermi="['business:backup:export']"
          >导出
          </el-button>
        </el-col>
        <right-toolbar :showSearch="showSearch" @update:showSearch="showSearch = $event"
                       @queryTable="getList"></right-toolbar>
      </el-row>

      <el-table v-loading="loading" :data="backupList" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center"/>
        <el-table-column label="id" align="center" prop="id" v-if="false"/>
        <el-table-column label="数据库名称" align="center" prop="sqlName"/>
        <el-table-column label="数据库ip" align="center" prop="sqlIp"/>
        <el-table-column label="定时规则" align="center" prop="sqlCron"/>
        <el-table-column label="备份数量限制" align="center" prop="fileLimit"/>
        <el-table-column label="数据库类型" align="center" prop="type" :formatter="typeFormat"/>
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template #default="scope">
            <el-button
                size="default"
                type="text"
                icon="Edit"
                @click="handleRun(scope.row)"
                v-hasPermi="['business:backup:edit']"
            >手动备份
            </el-button>
            <el-button
                size="default"
                type="text"
                icon="Edit"
                @click="handleUpdate(scope.row)"
                v-hasPermi="['business:backup:edit']"
            >修改
            </el-button>
            <el-button
                size="default"
                type="text"
                icon="delete"
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
          :page="queryParams.pageNum"
          :limit="queryParams.pageSize"
          @pagination="getList"
      />
    </div>

    <!-- 添加或修改数据库备份对话框 -->
    <el-dialog :title="title" v-model="open" width="400px" append-to-body>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
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
import {listBackup, getBackup, delBackup, addBackup, updateBackup, exportBackup, runBackup} from "@/api/business/backup"
import {getDicts} from "@/api/system/dict/data.js";

// 定义响应式数据
const loading = ref(true)
const ids = ref([])
const single = ref(true)
const multiple = ref(true)
const showSearch = ref(true)
const total = ref(0)
const backupList = ref([])
const title = ref("")
const open = ref(false)
const typeOptions = ref([])
const formRef = ref(null)

// 查询参数
const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
})

// 表单参数
const form = reactive({
  id: undefined,
  sqlName: undefined,
  sqlIp: undefined,
  sqlUsername: undefined,
  sqlPassword: undefined,
  sqlCron: undefined,
  fileLimit: undefined,
  type: undefined,
})

// 表单校验规则
const rules = {}

// 数据库类型字典翻译
const typeFormat = (row, column) => {
  return selectDictLabel(typeOptions.value, row.type)
}

// 获取字典标签
const selectDictLabel = (datas, value) => {
  if (value === undefined) {
    return ""
  }
  var actions = []
  Object.keys(datas).some((key) => {
    if (datas[key].dictValue == ('' + value)) {
      actions.push(datas[key].dictLabel)
      return true
    }
  })
  if (actions.length === 0) {
    actions.push(value)
  }
  return actions.join('')
}

// 查询数据库备份列表
const getList = () => {
  loading.value = true
  listBackup(queryParams).then(response => {
    backupList.value = response.rows
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
    sqlName: undefined,
    sqlIp: undefined,
    sqlUsername: undefined,
    sqlPassword: undefined,
    sqlCron: undefined,
    fileLimit: undefined,
    type: undefined,
  })
  if (formRef.value) {
    formRef.value.resetFields()
  }
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
  title.value = "添加数据库备份"
}

// 手动备份操作
const handleRun = (row) => {
  const id = row.id || ids.value
  runBackup({id: id}).then(response => {
    console.log(response)
  })
}

// 修改按钮操作
const handleUpdate = (row) => {
  reset()
  const id = row.id || ids.value
  getBackup(id).then(response => {
    Object.assign(form, response.data)
    open.value = true
    title.value = "修改数据库备份"
  })
}

// 提交按钮
const submitForm = () => {
  if (formRef.value) {
    formRef.value.validate((valid) => {
      if (valid) {
        if (form.id != null) {
          updateBackup(form).then(response => {
            ElMessage.success("修改成功")
            open.value = false
            getList()
          })
        } else {
          addBackup(form).then(response => {
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
  ElMessageBox.confirm('是否确认删除数据库备份编号为"' + deleteIds + '"的数据项?', "警告", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning"
  }).then(() => {
    return delBackup(deleteIds)
  }).then(() => {
    getList()
    ElMessage.success("删除成功")
  }).catch(() => {
  })
}

// 导出按钮操作
const handleExport = () => {
  ElMessageBox.confirm('是否确认导出所有数据库备份数据项?', "警告", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning"
  }).then(() => {
    return exportBackup(queryParams)
  }).then(response => {
    // 假设有一个下载方法
    // download(response.msg)
  }).catch(() => {
  })
}

// 获取字典数据
getDicts("sql_type").then(response => {
  typeOptions.value = response.data
})

// 组件挂载后获取列表
getList()
</script>
