<template>
  <el-row style="width:100%;height: 100%">
    <el-col :span="leftspan" v-show="letfshow">
      <el-row :gutter="10" class="mb8">
        <el-col :span="12">
          <el-button
              type="primary"
              plain
              icon="Plus"
              @click="handleAdd"
          >新增</el-button>
        </el-col>
      </el-row>
      <el-table :data="fileTableList" :show-header="true">
        <el-table-column label="文件名称" align="center" prop="fileName" />
        <el-table-column label="文件状态" width="80" align="center" prop="submitStatus">
          <template #default="scope">
<!--            <dict-tag :options="gd_submit_status" :value="scope.row.submitStatus" />-->
            <el-tag type="primary" v-if="scope.row.submitStatus == 2">已提交</el-tag>
            <el-tag type="primary" v-else>未提交</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作者" width="100" align="center" prop="nickName" />
        <el-table-column label="操作" width="240" align="center" class-name="small-padding fixed-width">
          <template #default="scope">
            <el-button link type="primary" icon="Edit" @click="editfile(scope.row)" v-if="scope.row.submitStatus==0 ||scope.row.submitStatus==1 " >重命名</el-button>
            <el-button link type="primary" icon="View" @click="showfile(scope.row)" v-if="scope.row.submitStatus==2" >查看</el-button>
            <el-button link type="primary" icon="Edit" @click="showfile(scope.row)" v-if="scope.row.submitStatus==0 ||scope.row.submitStatus==1" >编辑</el-button>
            <el-button link type="primary" icon="Delete" @click="deletefile(scope.row)" v-if="scope.row.submitStatus==0 ||scope.row.submitStatus==1" >删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-col>
    <el-col :span="rightspan" style="display: flex;flex-direction: column;">
      <el-row style="height: 40px;">
        <el-button type="primary" @click="showhideleft">{{bttext}}</el-button>
        <span>可用模板：</span>
        <el-select v-model="worktemplate" filterable placeholder="请选择" >
          <el-option
              v-for="dict in worktemplateOptions"
              :key="dict.id"
              :label="dict.templateName"
              :value="dict.id"
          ></el-option>
        </el-select>
        <el-button type="primary" @click="loadtemplate">导入模板</el-button>
        <!-- <el-button type="primary" @click="loadScenario">展示态势</el-button> -->
        <el-button type="primary" @click="showScenarioFile">查看任务</el-button>
        <el-button type="primary" @click="submitwork">提交作业</el-button>
      </el-row>
      <el-row style="flex: 1">
        <officeeditor :option="option"></officeeditor>
      </el-row>

    </el-col>
    <!-- 添加或修改想定配置对话框 -->
    <el-dialog :title="addFileTitle" v-model="addFileOpen" width="500px" append-to-body class="app-dialog" draggable
               :close-on-click-modal="false">
      <el-form ref="addFileRef" :model="form" :rules="rules" label-width="120px">
        <el-row>
          <el-col :span="24">
            <el-form-item label="文件名称" prop="fileName">
              <el-input v-model="form.fileName" placeholder=""/>
            </el-form-item>
          </el-col>
          <el-col :span="24" v-if="isadd">
            <el-form-item label="作业文件" prop="file">
              <el-upload
                  ref="uploadRef"
                  :http-request="handleupload"
                  :on-success="handleuploadsuccess"
                  :on-error="handleuploaderror"
                  :before-upload="handlebeforeupload"
                  :limit="1"
                  :on-exceed="handleExceed"
                  :file-list="fileList"
                  :auto-upload="false">
                <template #trigger>
                  <el-button type="primary">选择文件</el-button>
                </template>
                <el-button type="success" @click="submitUpload" style="margin-left: 10px;margin-top: -1px;">上传文件</el-button>
                <template #tip>
                  <div class="el-upload__tip text-red">
                    可上传dox/docx格式的文件,或由系统生成空白文件
                  </div>
                </template>
              </el-upload>
            </el-form-item>
          </el-col>

        </el-row>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancelEdit">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </el-row>
</template>

<script setup>
import officeeditor from './officeeditor'
import {
  addTaskFile,
  editTaskFile,
  deleteTaskFile,
  findAllTaskList,
  findStudentTaskList
} from "@/api/missiondesign/workfile.js"
import {delScenario, getScenarioBaseApi} from "@/api/textscenario/textscenario.js"
import {loadWordTemplate, startTrain, submitJob} from "@/api/missiondesign/trainmission.js"
import {listWorkTemplateOption} from "@/api/missiondesign/worktemplate.js"
import { ref,onMounted } from 'vue'
import {getUserProfile} from "@/api/system/user.js";
import {genFileId} from "element-plus"
import {uploadFile} from "@/api/file/file.js";
import axios from "axios";
const { proxy } = getCurrentInstance();
const route = useRoute();
const router = useRouter()
const { gd_submit_status } = proxy.useDict("gd_submit_status");

const rightspan = ref(16);
const leftspan = ref(8);
const letfshow = ref(true);
const bttext = ref("隐藏左侧");

const fileTableList = ref([]);

const worktemplate = ref(null);
const worktemplateOptions = ref([]);

const taskid = ref(null);
const scenarioid = ref(null);

const user = ref({});

const rules= ref({
  file: [{required: false}],
  fileName: [{required: true, message: "文件名称不能为空", trigger: "blur"}],
});
const form = ref();
const addFileOpen = ref(false);
const isadd = ref(true);
const addFileTitle = ref("");

const currentRow = ref({});

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
  key: undefined,
})
onMounted(()=>{
  taskid.value = route.query.taskid;
  scenarioid.value = route.query.scenarioid;
  getFile();
  getworktemplateOptions();
})

//region 上传相关变量函数
const uploadRef = ref();
const fileList = ref([]);
const handleExceed = (files) => {
  if(uploadRef.value!=null){
    uploadRef.value.clearFiles();
    const file = files[0];
    file.uid = genFileId()
    uploadRef.value.handleStart(file)
  }
}
const submitUpload = () => {
  uploadRef.value.submit();
}
const handleupload = (options)=>{
  const {file} = options;
  console.log(file);
  const formData = new FormData();
  formData.append('file',file);

  uploadFile(formData).then(response => {
    if(response.code==200){
      form.value.file = response.data
      proxy.$modal.msgSuccess("上传成功")
    }else {
      proxy.$modal.msgError("上传失败")
    }
    console.log(response);
  });
}
const handleuploadsuccess=(response,file,fileList)=>{
  // proxy.$modal.msgSuccess("上传成功2")
  console.log("success",response)
}
const handleuploaderror = (error,file,fileList)=>{
  // proxy.$modal.msgError("上传失败")
  console.log("error",error)
}
const handlebeforeupload = (rawFile) => {
  console.log(rawFile);
  if (!rawFile.name.endsWith('.doc') && !rawFile.name.endsWith('.docx')) {
    proxy.$modal.msgError("请上传word类型的文档")
    return false
  } else if (rawFile.size / 1024 / 1024 > 50) {
    proxy.$modal.msgError("文件大小不能超过50M")
    return false
  }
  if(rawFile.name.split(".").length>1){
    form.value.fileName = rawFile.name.split(".")[0];
  }
  return true
}
//endregion

function getTableList(){

  let info ={
    taskId:taskid.value
  }
  findStudentTaskList(info).then(response=>{
    if(response.code == 200){
      fileTableList.value = response.data;
    }
  })
}

//重置表单
function reset() {
  form.value = {
    taskId:taskid.value,
    id: undefined,
    file: undefined,
    fileName: undefined,
  }
  fileList.value = [];
  proxy.resetForm("addFileRef")
}
//新增按钮
function handleAdd(){
  reset();
  isadd.value = true;
  addFileTitle.value = "新增文件"
  addFileOpen.value = true;
}
//删除文件
function deletefile(row){
  currentRow.value = JSON.parse(JSON.stringify(row))
  proxy.$modal.confirm('是否确认删除该文件?').then(function () {
    deleteTaskFile(row.id).then(response=>{
      if(response.code == 200){
        currentRow.value = {};
        //把文件关了
        if(option.value.url == row.file){
          option.value.key = generaterKey();
          option.value.url = undefined;
          option.value.editUrl = undefined;
        }
        getTableList()
        proxy.$modal.msgSuccess("删除成功");
      }else {
        proxy.$modal.msgError(response.msg);
      }
    })
  })
}
//编辑文件
function editfile(row){
  currentRow.value = JSON.parse(JSON.stringify(row))
  reset();
  form.value = {
    taskId:taskid.value,
    id: row.id,
    file: row.file,
    fileName: row.fileName,
  }
  isadd.value = false;
  addFileTitle.value = "重命名"
  addFileOpen.value = true;
}
//表单提交
function submitForm(){
  proxy.$refs["addFileRef"].validate(valid => {
    if (valid) {
      if(isadd.value){
        addTaskFile(form.value).then(response => {
          if(response.code == 200){
            proxy.$modal.msgSuccess("新增成功");
            addFileOpen.value = false
            getTableList();
          }
        });
      }else {
        editTaskFile(form.value).then(response => {
          if(response.code == 200){
            proxy.$modal.msgSuccess("操作成功");
            addFileOpen.value = false
            getTableList();
          }
        });
      }

    }
  });
}
//表单取消
function cancelEdit(){
  addFileOpen.value = false;
}

//获取文件列表
function getFile(){
  getUserProfile().then(response => {
    user.value = response.data;
    startTrain(taskid.value).then(response=>{
      if(response.code == 200){
        getTableList();
      }
    })
  });

}
//获取可用模板
function getworktemplateOptions() {
  listWorkTemplateOption().then(response=>{
    if(response.code == 200){
      worktemplateOptions.value = response.data;
    }
  })
}
//生成key
function generaterKey() {
  let gen = String( Math.floor(Math.random() * 10000));
  if(option.value.key && gen == option.value.key){
    gen = String( Math.floor(Math.random() * 10000));
  }
  return gen;
}
//加载模板
function loadtemplate() {
  if(worktemplate.value){
    if(currentRow.value && currentRow.value.id && currentRow.value.submitStatus !=2){
      proxy.$modal.confirm('加载模板将使用模板覆盖当前文件，是否继续？').then(function() {
        loadWordTemplate(currentRow.value.id,worktemplate.value).then(response=>{
          if(response.code == 200){
            option.value.key = generaterKey();
            option.value.url = response.msg;
            option.value.mode='edit';
            option.value.isEdit=true;
            axios.get("/config.json").then(response => {
              option.value.editUrl = response.data.ONLYOFFICE_CALLBACKURL+'?originurl='+response.msg;
            })
          }
        })
      })
    }else {
      proxy.$modal.msgWarning("请打开一个可编辑的文件")
    }
  }else {
    proxy.$modal.msgWarning("请先选择模板")
  }
}
//显示文件
function showfile(row) {
  currentRow.value = JSON.parse(JSON.stringify(row))
  option.value.key = generaterKey();
  option.value.user.id = user.value.userId;
  option.value.user.name = user.value.userName;
  option.value.url = row.file;
  option.value.title = row.fileName;
  option.value.fileType='docx';
  if(row.submitStatus==2){
    option.value.mode='view';
    option.value.isEdit=false;
  }else {
    option.value.mode='edit';
    option.value.isEdit=true;
  }
  axios.get("/config.json").then(response => {
    option.value.editUrl = response.data.ONLYOFFICE_CALLBACKURL+'?originurl='+row.file;
  })
}

//左侧显隐
const showhideleft = ()=>{
  letfshow.value=!letfshow.value;
  if(letfshow.value){
    rightspan.value=16;
    bttext.value="隐藏左侧";
  }else {
    rightspan.value=24;
    bttext.value="显示左侧";
  }
}
//查看想定文件
const showScenarioFile = ()=>{
  getScenarioBaseApi(scenarioid.value).then((response)=>{
    if(response.code == 200){
      if(response.data && response.data.textFile && response.data.textFileName){
        const rt = router.resolve({
          path:'/fileview',
          query:{
            // userid:user.value.userId,
            // username:user.value.userName,
            fileurl:response.data.textFile,
            title:response.data.textFileName,
            // mode:'view',
            // isEdit:false,
          }
        })
        console.log(rt);
        window.open(rt.href,'_blank')
      }else {
        proxy.$modal.msgError("未查询到文件信息")
      }
    }
  })
}

//查看态势
const loadScenario = ()=>{
  const rt = router.resolve({
    path:'/tasksituation',
    query:{

    }
  })
  window.open(rt.href,'_blank')
  // const rt = router.resolve({
  //   path:'/mapscenarioedit',
  //   query:{
  //     scenarioId: scenarioid.value,
  //   }
  // })
  // console.log(rt);
  // window.open(rt.href,'_blank')
}
//提交作业
const submitwork = ()=>{
  submitJob(taskid.value).then(response=>{
    proxy.$modal.msgSuccess("提交成功")
    if(response.code == 200){
      setTimeout(()=>{
        window.close();
        window.opener.postMessage({
          type:'edit'
        })
      },2000)
    }
  })
}
</script>

<style scoped>

</style>