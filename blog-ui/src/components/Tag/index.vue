<template>
  <div class="blog-tag" :style="{backgroundColor:tagColor}" @click.stop="searchTag">{{tagName}}</div>
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
    },
    tagId:{
      type: [Number,String],
      default: undefined
    }
  },
  computed: {
  },
  methods:{
    searchTag(){
      if (this.tagId!==''&&this.tagId!==undefined){
          this.$router.push({path: "/salish/category/"+this.tagId})
      }
    }
  }
}
</script>

<style>
.blog-tag{
  display: inline-block;
  margin: 0 0 5px 5px;
  width: 36px;
  height: 25px;
  border-radius: 2px;
  color: #FFFFFF;
  font-weight: 200;
  text-align: center;
  font-size: 10px;
  padding-top: 5px;
  font-family: none;
}
</style>
