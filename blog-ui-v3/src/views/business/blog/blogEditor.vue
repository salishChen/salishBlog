<template>
  <div style="margin: 30px">
    <el-form ref="elForm" :model="form" :rules="rules" size="default" label-width="100px">
      <el-row :gutter="15">
        <el-col :span="22">
          <el-form-item label="标题" prop="title">
            <el-input v-model="form.title" placeholder="请输入标题" clearable :style="{width: '100%'}">
            </el-input>
          </el-form-item>
        </el-col>
        <el-col :span="22">
          <el-form-item label="简介" prop="summary">
            <el-input v-model="form.summary" type="textarea" placeholder="请输入简介"
                      :autosize="{minRows: 4, maxRows: 4}" :style="{width: '100%'}"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="22">
          <el-form-item label="上传封面" prop="cover">
            <el-upload
                class="avatar-uploader"
                :action="uploadUrl"
                :headers="headers"
                :show-file-list="false"
                :on-success="handleAvatarSuccess"
                :before-upload="coverBeforeUpload">
              <img v-if="form.cover" :src="form.cover" class="avatar">
              <i v-else class="Plus avatar-uploader-icon"></i>
            </el-upload>
          </el-form-item>

          <el-form-item>
            <el-input v-model="imgUrl" style="width: 80%"></el-input>
            <el-button style="padding-left: 20px" type="primary" @click="addOutsideUrl">添加外链url</el-button>
          </el-form-item>

        </el-col>
        <el-col :span="7">
          <el-form-item label="标签" prop="tags">
            <el-select v-model="tags" placeholder="请选择标签" clearable multiple :style="{width: '100%'}">
              <el-option v-for="(item, index) in tagsOptions" :key="index" :label="item.tag"
                         :value="Number(item.id)" :disabled="item.disabled"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="博客类型" prop="blogType">
            <el-select v-model="form.blogType" placeholder="请选择博客类型" clearable :style="{width: '100%'}">
              <el-option v-for="(item, index) in blogTypeOptions" :key="index" :label="item.label"
                         :value="item.value" :disabled="item.disabled"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="7">
          <el-form-item label="内容类型" prop="contentType">
            <el-select v-model="form.contentType" placeholder="请选择内容类型" clearable
                       :style="{width: '100%'}">
              <el-option v-for="(item, index) in contentTypeOptions" :key="index" :label="item.label"
                         :value="item.value" :disabled="item.disabled"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="22">
          <el-form-item label="博文内容" prop="content">
            <Markdown v-if="form.contentType==1" :content.sync="form.content" :save="save" height="800px"></Markdown>
            <!--            <v-md-editor v-if="form.contentType==1"   @upload-image="handleUploadImage" ></v-md-editor>-->
            <Editor v-if="form.contentType==2||(form.contentType==''||form.contentType==undefined)"
                    v-model="form.content" :classs="'avatar-uploader'" :minHeight="150"/>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>

    <el-row>
      <el-col :span="24" :pull="2">
        <div style="float: right">
          <el-button type="primary" @click="submitForm">提交</el-button>
          <el-button @click="resetForm">重置</el-button>
        </div>
      </el-col>
    </el-row>
  </div>
</template>
<script setup>
import {listTag} from "@/api/business/tag";
import Editor from '@/components/Editor';
import {getToken} from "@/utils/auth";
import {addBlog, updateBlog, getBlog} from "@/api/business/blog";
import Markdown from "@/components/Markdowm";
import useTagsViewStore from '@/store/modules/tagsView'

const { proxy } = getCurrentInstance();
const route = useRoute()
const router = useRouter()

const blogId = ref('')
const imgUrl = ref('')
const uploadUrl = ref(import.meta.env.VUE_APP_BASE_API + "/common/upload")
const headers = ref({Authorization: "Bearer " + getToken()})
const tagsOptions = ref([])
const tags = ref([])
const blogTypeOptions = proxy.useDict("blog_type").blog_type;
const contentTypeOptions = proxy.useDict("content_type").content_type;


const data = reactive({
  form: {
    title: '',
    summary: undefined,
    cover: null,
    tags: [],
    tagId: undefined,
    blogType: undefined,
    contentType: '1',
    content: undefined,
  },
  rules: {
    title: [{required: true, message: '请输入标题', trigger: 'blur'}],
    blogType: [{required: true, message: '请选择博客类型', trigger: 'change'}],
    contentType: [{required: true, message: '请选择内容类型', trigger: 'change'}],
    content: [{required: true, message: '请输入博文内容', trigger: ['blur', 'change']}],
  },
})

const {form, rules} = toRefs(data)


function save() {
  proxy.$refs['elForm'].validate(valid => {
    if (!valid) return
    // TODO 提交表单
    form.value.tagId = tags.value.toString();
    if (blogId.value != undefined && blogId.value != "") {
      form.value.id = blogId.value;
      updateBlog(form.value).then(res => {
        proxy.$modal.msgSuccess("保存成功");
      });
    } else {
      addBlog(form.value).then(res => {
        if (res.code == 200) {
          blogId.value = res.data.id
        }
        proxy.$modal.msgSuccess("新增成功");
      });
    }
  })
}

function submitForm() {
  proxy.$refs['elForm'].validate(valid => {
    if (!valid) return
    // TODO 提交表单
    form.value.tagId = tags.value.toString();
    if (blogId.value != undefined && blogId.value != "") {
      form.value.id = blogId.value;
      updateBlog(form.value).then(response => {
        proxy.$modal.msgSuccess("修改成功");
        useTagsViewStore().delView(route)
        router.replace({path: "/blog"});
      });
    } else {
      addBlog(form.value).then(response => {
        proxy.$modal.msgSuccess("新增成功");
        useTagsViewStore().delView(route)
        router.replace({path: "/blog"});
      });
    }
  })
}

function resetForm() {
  proxy.resetForm("elForm")
}

function addOutsideUrl() {
  form.value.cover = imgUrl.value
  imgUrl.value = "";
}

function coverBeforeUpload(file) {
  let isRightSize = file.size / 1024 / 1024 < 4
  if (!isRightSize) {
    proxy.$modal.msgError('文件大小超过 4MB')
  }
  return isRightSize
}

function handleAvatarSuccess(res) {
  if (res.code == 200) {
    form.value.cover = res.url;
  }
}

listTag().then(response => {
  tagsOptions.value = response.rows;
});
blogId.value = route.query.id;
if (blogId.value != undefined && blogId.value != "") {
  getBlog(blogId.value).then(response => {
    form.value = response.data;
    tags.value = [];
    form.value.tagId.split(",").forEach(item => {
      tags.value.push(Number(item));
    })
  });
}


</script>
<style>
.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}

.avatar-uploader .el-upload:hover {
  border-color: #409EFF;
}

.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  line-height: 178px;
  text-align: center;
}

.avatar {
  width: 178px;
  height: 178px;
  display: block;
}
</style>
