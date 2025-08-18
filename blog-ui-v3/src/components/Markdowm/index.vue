<template>
  <v-md-editor
      v-model="blogContent"
      @change="changeContent"
      @save="mdSave()"
      :disabled-menus="[]"
      @upload-image="handleUploadImage"
      :height="height">
  </v-md-editor>
</template>

<script setup>

import axios from "axios";
import {getToken} from "@/utils/auth";

// 定义 props
const props = defineProps({
  content: {
    type: String,
    default: ""
  },
  /* 高度 */
  height: {
    type: String,
    default: "800px"
  },
  /* 最小高度 */
  minHeight: {
    type: String,
    default: ""
  },
  save: {
    type: Function,
    default: null
  }
})

// 定义 emits
const emit = defineEmits(['update:content'])

// 数据定义
const url = import.meta.env.VUE_APP_BASE_API + "/common/upload"

// 计算属性
const blogContent = computed({
  get() {
    return props.content
  },
  set(val) {
    emit('update:content', val)
  }
})

// 方法定义
function mdSave() {
  console.log(props.save)
  if (props.save) {
    props.save()
  }
}

function handleUploadImage(event, insertImage, files) {
  const file = files[0]
  const formData = new FormData()
  formData.append('file', file)

  axios.post(url, formData, {headers: {Authorization: "Bearer " + getToken()}}).then(res => {
    // 处理上传成功后的逻辑
    if (res.data.code == 200) {
      insertImage({
        url: res.data.url,
        desc: file.name,
      })
    } else {
      // 注意：这里的 error 方法需要根据实际情况替换
      console.error("上传错误")
    }
  })
}

function changeContent() {
  // 可根据需要启用
  // console.log(blogContent.value)
  // console.log(props.content)
  // emit('content', blogContent.value)
}
</script>

<style scoped>


</style>