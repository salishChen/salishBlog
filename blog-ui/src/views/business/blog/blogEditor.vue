<template>
  <div style="margin: 30px">
    <el-row :gutter="15">
      <el-form ref="elForm" :model="form" :rules="rules" size="medium" label-width="100px">
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
              <i v-else class="el-icon-plus avatar-uploader-icon"></i>
            </el-upload>
          </el-form-item>
        </el-col>
        <el-col :span="7">
          <el-form-item label="标签" prop="tags">
            <el-select v-model="form.tags" placeholder="请选择标签" clearable :style="{width: '100%'}">
              <el-option v-for="(item, index) in tagsOptions" :key="index" :label="item.label"
                         :value="item.value" :disabled="item.disabled"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="博客类型" prop="blogType">
            <el-select v-model="form.blogType" placeholder="请选择博客类型" clearable :style="{width: '100%'}">
              <el-option v-for="(item, index) in blogTypeOptions" :key="index" :label="item.dictLabel"
                         :value="item.dictValue" :disabled="item.disabled"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="7">
          <el-form-item label="内容类型" prop="contentType">
            <el-select v-model="form.contentType" placeholder="请选择内容类型" clearable
                       :style="{width: '100%'}">
              <el-option v-for="(item, index) in contentTypeOptions" :key="index" :label="item.dictLabel"
                         :value="Number(item.dictValue)" :disabled="item.disabled"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="22">
          <el-form-item label="博文内容" prop="content">
            <Markdown v-if="form.contentType==1" :content.sync="form.content" min-height="400px"></Markdown>
<!--            <v-md-editor v-if="form.contentType==1"   @upload-image="handleUploadImage" ></v-md-editor>-->
            <Editor v-if="form.contentType==2||(form.contentType==''||form.contentType==undefined)"
                    v-model="form.content" :classs="'avatar-uploader'" :minHeight="150"/>
          </el-form-item>
        </el-col>
      </el-form>
    </el-row>

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
<script>
import {listTag} from "@/api/business/tag";
import Editor from '@/components/Editor';
import {getToken} from "@/utils/auth";
import {addBlog, updateBlog, getBlog} from "@/api/business/blog";
import Markdown from "@/components/Markdowm";
export default {
  components: {Markdown},
  props: [],
  data() {
    return {
      blogId: '',
      uploadUrl: process.env.VUE_APP_BASE_API + "/common/upload",
      headers: { Authorization: "Bearer " + getToken() },
      form: {
        title: '',
        summary: undefined,
        cover: null,
        tags: undefined,
        blogType: undefined,
        contentType: 2,
        content: undefined,
      },
      rules: {
        title: [{required: true, message: '请输入标题', trigger: 'blur'}],
        blogType: [{required: true, message: '请选择博客类型', trigger: 'change'}],
        contentType: [{required: true, message: '请选择内容类型', trigger: 'change'}],
        content: [{required: true, message: '请输入博文内容', trigger: ['blur','change']}],
      },
      tagsOptions: [],
      blogTypeOptions: [],
      contentTypeOptions: [],
    }
  },

  watch: {},
  created() {
    this.getDicts("content_type").then(response => {
      this.contentTypeOptions = response.data;
    });
    this.getDicts("blog_type").then(response => {
      this.blogTypeOptions = response.data;
    });
    listTag().then(response => {
      this.tagsOptions = response.rows;
    });
    this.blogId = this.$route.query.id;
    if (this.blogId !=undefined&&this.blogId!=""){
      getBlog(this.blogId).then(response => {
        this.form = response.data;
      });
    }
  },
  mounted() {
  },
  methods: {
    submitForm() {
      this.$refs['elForm'].validate(valid => {
        if (!valid) return
        // TODO 提交表单
        if (this.blogId != undefined && this.blogId != "") {
          this.form.id = this.blogId;
            updateBlog(this.form).then(response => {
              this.msgSuccess("修改成功");
              this.$router.push({ path: "/blog"});
            });
        } else {
          addBlog(this.form).then(response => {
            this.msgSuccess("新增成功");
            this.$router.push({ path: "/blog"});
          });
        }
      })
    },
    resetForm() {
      this.$refs['elForm'].resetFields()
    },
    coverBeforeUpload(file) {
      let isRightSize = file.size / 1024 / 1024 < 4
      if (!isRightSize) {
        this.$message.error('文件大小超过 4MB')
      }
      return isRightSize
    },
    handleAvatarSuccess(res){
      if (res.code == 200) {
        this.form.cover=res.url;
      }
    },
    handleUploadImage(event, insertImage, files) {
      // 拿到 files 之后上传到文件服务器，然后向编辑框中插入对应的内容
      console.log(files);
    },
  }
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
