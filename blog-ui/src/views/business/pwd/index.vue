<template>
  <div class="app-container">
    <el-form class="app-search card" :model="queryParams" ref="queryFormRef" :inline="true" v-show="showSearch"
             label-width="68px">
      <el-form-item label="秘钥名" prop="keyName">
        <el-input
            v-model="queryParams.keyName"
            placeholder="请输入秘钥名"
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
        <right-toolbar :showSearch="showSearch" @update:showSearch="showSearch = $event"
                       @queryTable="getList"></right-toolbar>
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
          :page="queryParams.pageNum"
          :limit="queryParams.pageSize"
          @pagination="getList"
      />
    </div>
    <!-- 添加或修改秘钥本对话框 -->
    <el-dialog :title="title" v-model="open" width="1000px" append-to-body>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="80px">
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
import {listPwd, getPwd, delPwd, addPwd, updatePwd, exportPwd} from "@/api/business/pwd"

// 定义响应式数据
const loading = ref(true)
const ids = ref([])
const single = ref(true)
const multiple = ref(true)
const showSearch = ref(true)
const total = ref(0)
const pwdList = ref([])
const title = ref("")
const open = ref(false)
const queryFormRef = ref(null)
const formRef = ref(null)

// 查询参数
const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  keyName: undefined,
})

// key列表
const keyList = ref([{key: '', value: ''}])

// 表单参数
const form = reactive({
  id: undefined,
  keyName: undefined,
  keyValue: undefined,
  otherKeys: undefined,
})

// 表单校验规则
const rules = {}

// 查询秘钥本列表
const getList = () => {
  loading.value = true
  listPwd(queryParams).then(response => {
    pwdList.value = response.rows
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
    keyName: undefined,
    keyValue: undefined,
    otherKeys: undefined,
  })
  if (formRef.value) {
    formRef.value.resetFields()
  }
  keyList.value = [{key: '', value: ''}]
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
  title.value = "添加秘钥本"
}

// 修改按钮操作
const handleUpdate = (row) => {
  reset()
  const id = row.id || ids.value
  getPwd(id).then(response => {
    Object.assign(form, response.data)
    if (form.otherKeys != null && form.otherKeys != '') {
      keyList.value = JSON.parse(form.otherKeys)
    }
    open.value = true
    title.value = "修改秘钥本"
  })
}

// 提交按钮
const submitForm = () => {
  let otherKeys = []
  keyList.value.forEach((item) => {
    if (item.key != "" && item.key != undefined) {
      otherKeys.push("{\"key\":" + "\"" + item.key + "\"" + ",\"value\":" + "\"" + item.value + "\"" + "}")
    }
  })
  form.otherKeys = "[" + otherKeys.toString() + "]"

  if (formRef.value) {
    formRef.value.validate((valid) => {
      if (valid) {
        if (form.id != null) {
          updatePwd(form).then(response => {
            ElMessage.success("修改成功")
            open.value = false
            getList()
          })
        } else {
          addPwd(form).then(response => {
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
  ElMessageBox.confirm('是否确认删除秘钥本编号为"' + deleteIds + '"的数据项?', "警告", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning"
  }).then(() => {
    return delPwd(deleteIds)
  }).then(() => {
    getList()
    ElMessage.success("删除成功")
  }).catch(() => {
  })
}

// 导出按钮操作
const handleExport = () => {
  ElMessageBox.confirm('是否确认导出所有秘钥本数据项?', "警告", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning"
  }).then(() => {
    return exportPwd(queryParams)
  }).then(response => {
    // 假设有一个下载方法
    // download(response.msg)
  }).catch(() => {
  })
}

// 添加状态项
const addStatus = () => {
  keyList.value.push({key: '', value: ''})
}

// 减少状态项
const minusStatus = () => {
  if (keyList.value.length > 1) {
    keyList.value.pop()
  }
}

// 组件挂载后获取列表
getList()
</script>
