<template>
  <el-row style="width:100%;height: 100%">
    <el-col :span="leftspan">
      <el-form class="app-search card" :model="queryParams" ref="queryRef" :inline="true" style="border: none"
               label-width="68px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="文件名称" prop="textFileName" style="width: 90%;margin: 0px;">
              <el-input
                  v-model="queryParams.textFileName"
                  placeholder="请输入文件名称"
                  clearable
                  style="width: 240px"
                  @keyup.enter="handleQuery"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="创建时间" style="width: 90%;margin: 0px;">
              <el-date-picker
                  v-model="dateRange"
                  value-format="YYYY-MM-DD"
                  type="daterange"
                  range-separator="-"
                  start-placeholder="开始日期"
                  end-placeholder="结束日期"
              ></el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row style="margin-top: 15px">
          <!--          <el-col :span="15">-->
          <!--            <el-form-item label="创建时间" style="width: 90%;margin: 0px;">-->
          <!--              <el-date-picker-->
          <!--                  v-model="dateRange"-->
          <!--                  value-format="YYYY-MM-DD"-->
          <!--                  type="daterange"-->
          <!--                  range-separator="-"-->
          <!--                  start-placeholder="开始日期"-->
          <!--                  end-placeholder="结束日期"-->
          <!--              ></el-date-picker>-->
          <!--            </el-form-item>-->
          <!--          </el-col>-->
          <el-col :span="9">
            <el-form-item style="width: 100%;margin: 0px;">
              <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
              <el-button icon="Refresh" @click="resetQuery">重置</el-button>
            </el-form-item>
          </el-col>
        </el-row>




      </el-form>
      <!-- 表格数据 -->
      <el-table v-loading="loading" :data="scenarioList">
        <el-table-column label="想定文件" align="center" prop="fileName">
          <template #default="scope">
            <a style="color: #409EFF;text-decoration: underline;" href="javascript:void(0);" @click="handleview(scope.row)">{{ scope.row.fileName }}</a>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="180px">
          <template #default="scope">
            <el-button link type="primary" icon="Edit" @click="handleSelect(scope.row)">选择</el-button>
            <el-button link type="primary" icon="Operation" @click="handleScenario(scope.row)">态势编辑</el-button>
          </template>
        </el-table-column>
      </el-table>

      <pagination
          v-show="total > 0"
          :total="total"
          v-model:page="queryParams.pageNum"
          v-model:limit="queryParams.pageSize"
          @pagination="getList"
      />
    </el-col>
    <el-col :span="rightspan">
      <officeeditor :option="option"></officeeditor>
    </el-col>
  </el-row>

</template>

<script setup>
import officeeditor from './officeeditor'
import { ref,onMounted } from 'vue'
import {delScenario, getScenarioTextlist, listScenario, saveScenario} from "@/api/textscenario/textscenario.js"
import {getUserProfile} from "@/api/system/user";
import {getTaskDetail} from "@/api/missiondesign/trainmissiontemplate.js";
import axios from "axios";


const { proxy } = getCurrentInstance();
const route = useRoute();
const router = useRouter();
// const { gd_submit_status } = proxy.useDict("gd_submit_status");
const fileList = ref([]);

const rightspan = ref(16);
const leftspan = ref(8);

const scenarioList = ref([]);
const loading = ref(false);

const data = reactive({
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    scenarioName: undefined,
    textFileName: undefined,
    beginTime:undefined,
    endTime:undefined
  },
});
const {queryParams} = toRefs(data);
const userinfo = ref({});
const dateRange = ref([]);
const total = ref(0);
const option= ref({
  url: null,
  isEdit: true,
  fileType: '',
  title: '',
  user: {
    id: '',
    name: ''
  },
  mode: 'edit',
  editUrl: '',
  key: String( Math.floor(Math.random() * 10000)),
})
onMounted(()=>{
  getUserProfile().then(response => {
    userinfo.value = response.data;
    getList();
  });
  receivemsg();
})

function receivemsg(){
  window.addEventListener('message',(event)=>{
    if(event.data.type == "scenarioselect"){
      if(event.data.data == "success"){
        proxy.$modal.msgSuccess("保存成功");
      }
      console.log(event.data.data)
    }
  })
}
function handleSelect(row){
  proxy.$modal.confirm('是否确认选择该想定？').then(function() {
    if(window.opener){
      window.opener.postMessage({
        type:'scenarioselect',
        data:row.id
      })
    }else {
      proxy.$modal.msgError("请从演训任务界面打开本页面");
    }
  })
}

function handleview(row) {
  if(row.file!=null && row.fileName!=""){
    option.value.key = String( Math.floor(Math.random() * 10000));
    option.value.user.id = userinfo.value.userId;
    option.value.user.name = userinfo.value.userName;
    option.value.url = row.file;
    option.value.title = row.fileName;
    option.value.fileType='docx';
    option.value.mode = 'view';
    option.value.isEdit = false;
    axios.get("/config.json").then(response => {
      option.value.editUrl = response.data.ONLYOFFICE_CALLBACKURL;
    })
  }else {
    proxy.$modal.msgError("未找到想定文件");
  }

}

/** 查询想定列表 */
function getList() {
  loading.value = true;
  let param = queryParams.value;
  if(dateRange.value == null || dateRange.value.length!=2){
    param.beginTime = null;
    param.endTime = null;
  }else {
    param.beginTime = dateRange.value[0]+" 00:00:00";
    param.endTime = dateRange.value[1]+" 23:59:59";
  }
  getScenarioTextlist(param).then(response => {
    scenarioList.value = response.data.content;
    total.value = response.data.totalElements;
    loading.value = false;
  });
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1;
  getList();
}

/** 重置按钮操作 */
function resetQuery() {
  dateRange.value = [];
  proxy.resetForm("queryRef");
  handleQuery();
}

function handleScenario(row) {
  const rt = router.resolve({
    path:'/tasksituation',
    query:{

    }
  })
  window.open(rt.href,'_blank')
}

</script>

<style scoped>

</style>