<template>
  <div class="blog-tag" :style="{backgroundColor:tagColor}" @click.stop="searchTag">{{tagName}}</div>
</template>

<script setup>
import router from "@/router";

const props = defineProps({
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
})


function searchTag(){
  if (props.tagId!==''&&props.tagId!==undefined){
    router.push({path: "/salish/category/"+props.tagId})
  }
}

</script>

<style>
.blog-tag{
  display: inline-block;
  margin: 0 0 5px 5px;
  width: auto;
  height: 25px;
  line-height: 14px !important;
  border-radius: 2px;
  color: #FFFFFF;
  text-align: center;
  font-size: 12px;
  padding: 5px;
  font-family: none;
}
</style>
