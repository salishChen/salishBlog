<template>
  <div class="blog-tag" :style="{backgroundColor:tagColor}">{{tagName}}</div>
</template>

<script>
import { isExternal } from '@/utils/validate'

export default {
  name: 'Tag',
  props: {
    tagName: {
      type: String,
      required: true
    },
    tagColor: {
      type: String,
      default: ''
    }
  },
  computed: {
  }
}
</script>

<style>
.blog-tag{
  margin-left: 5px;
  width: 36px;
  height: 25px;
  border-radius: 2px;
  color: #FFFFFF;
  font-weight: 200;
  text-align: center;
  font-size: 8px;
  padding-top: 5px;
}
</style>
