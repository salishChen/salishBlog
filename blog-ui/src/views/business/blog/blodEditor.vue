<template>
  <div>
    <el-row :gutter="15">
      <el-form ref="elForm" :model="formData" :rules="rules" size="medium" label-width="100px">
        <el-col :span="22">
          <el-form-item label="标题" prop="title">
            <el-input v-model="formData.title" placeholder="请输入标题" clearable :style="{width: '100%'}">
            </el-input>
          </el-form-item>
        </el-col>
        <el-col :span="22">
          <el-form-item label="简介" prop="summary">
            <el-input v-model="formData.summary" type="textarea" placeholder="请输入简介"
              :autosize="{minRows: 4, maxRows: 4}" :style="{width: '100%'}"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="22">
          <el-form-item label="上传封面" prop="cover">
            <el-upload ref="cover" :file-list="coverfileList" :action="coverAction"
              :before-upload="coverBeforeUpload">
              <el-button size="small" type="primary" icon="el-icon-upload">点击上传</el-button>
              <div slot="tip" class="el-upload__tip">只能上传不超过 2MB 的文件</div>
            </el-upload>
          </el-form-item>
        </el-col>
        <el-col :span="11">
          <el-form-item label="标签" prop="tags">
            <el-select v-model="formData.tags" placeholder="请选择标签" clearable :style="{width: '100%'}">
              <el-option v-for="(item, index) in tagsOptions" :key="index" :label="item.label"
                :value="item.value" :disabled="item.disabled"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="11">
          <el-form-item label="博客类型" prop="blogType">
            <el-select v-model="formData.blogType" placeholder="请选择博客类型" clearable :style="{width: '100%'}">
              <el-option v-for="(item, index) in blogTypeOptions" :key="index" :label="item.label"
                :value="item.value" :disabled="item.disabled"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="11">
          <el-form-item label="内容类型" prop="contentType">
            <el-select v-model="formData.contentType" placeholder="请选择内容类型" clearable
              :style="{width: '100%'}">
              <el-option v-for="(item, index) in contentTypeOptions" :key="index" :label="item.label"
                :value="item.value" :disabled="item.disabled"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="22">
          <el-form-item label="多行文本" prop="field110">
            <el-input v-model="formData.field110" type="textarea" placeholder="请输入多行文本"
              :autosize="{minRows: 4, maxRows: 4}" :style="{width: '100%'}"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item size="large">
            <el-button type="primary" @click="submitForm">提交</el-button>
            <el-button @click="resetForm">重置</el-button>
          </el-form-item>
        </el-col>
      </el-form>
    </el-row>
  </div>
</template>
<script>
export default {
  components: {},
  props: [],
  data() {
    return {
      formData: {
        title: '',
        summary: undefined,
        cover: null,
        tags: undefined,
        blogType: undefined,
        contentType: undefined,
        field110: undefined,
      },
      rules: {
        title: [{
          required: true,
          message: '请输入标题',
          trigger: 'blur'
        }],
        summary: [],
        tags: [{
          required: true,
          message: '请选择标签',
          trigger: 'change'
        }],
        blogType: [{
          required: true,
          message: '请选择博客类型',
          trigger: 'change'
        }],
        contentType: [{
          required: true,
          message: '请选择内容类型',
          trigger: 'change'
        }],
        field110: [{
          required: true,
          message: '请输入多行文本',
          trigger: 'blur'
        }],
      },
      coverAction: 'https://jsonplaceholder.typicode.com/posts/',
      coverfileList: [],
      tagsOptions: [{
        "label": "选项一",
        "value": 1
      }, {
        "label": "选项二",
        "value": 2
      }],
      blogTypeOptions: [{
        "label": "选项一",
        "value": 1
      }, {
        "label": "选项二",
        "value": 2
      }],
      contentTypeOptions: [{
        "label": "markdown",
        "value": 1
      }, {
        "label": "富文本",
        "value": 2
      }],
    }
  },
  computed: {},
  watch: {},
  created() {},
  mounted() {},
  methods: {
    submitForm() {
      this.$refs['elForm'].validate(valid => {
        if (!valid) return
        // TODO 提交表单
      })
    },
    resetForm() {
      this.$refs['elForm'].resetFields()
    },
    coverBeforeUpload(file) {
      let isRightSize = file.size / 1024 / 1024 < 2
      if (!isRightSize) {
        this.$message.error('文件大小超过 2MB')
      }
      return isRightSize
    },
  }
}

</script>
<style>
.el-upload__tip {
  line-height: 1.2;
}

</style>
