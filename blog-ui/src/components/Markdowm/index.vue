<template>
  <v-md-editor v-model="blogContent" @change="changeContent" @save="mdSave()" :disabled-menus="[]"
               @upload-image="handleUploadImage"
               :style="{minHeight:minHeight}"></v-md-editor>
</template>

<script>

import axios from "axios";
import {getToken} from "@/utils/auth";

export default {
  name: "Markdown",
  props: {
    content: {
      type: String,
      default: ""
    },
    /* 高度 */
    height: {
      type: String,
      default: null
    },
    /* 最小高度 */
    minHeight: {
      type: String,
      default: null
    },
    save: {
      type: Function,
      default: null
    }
  },
  data() {
    return {
      // blogContent:"",
      url: process.env.VUE_APP_BASE_API + "/common/upload"
    }
  },
  created() {
  },
  computed: {
    blogContent: {
      get() {
        return this.content
      },
      set(val) {
        this.$emit('update:content', val)
      }
    },
  },
  watch: {},
  mounted() {
  },
  methods: {
    mdSave() {
      console.log(this.save)
      if (this.save) {
        this.save();
      }
    },
    handleUploadImage(event, insertImage, files) {
      // console.log(event)
      // 拿到 files 之后上传到文件服务器，然后向编辑框中插入对应的内容
      // console.log(files);

      const file = files[0]
      const formData = new FormData()
      formData.append('file', file)

      axios.post(this.url, formData, {headers: {Authorization: "Bearer " + getToken()}}).then(res => {
        // 处理上传成功后的逻辑
        if (res.data.code == 200) {
          insertImage({
            url: res.data.url,
            desc: file.name,
          });
        }else {
         this.error("上传错误");
        }
      })
    },
    changeContent() {
      // console.log(this.blogContent)
      // console.log(this.content)
      // this.$emit('content', this.blogContent)
    }
  }

}
</script>

<style scoped>

</style>
