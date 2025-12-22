<template>
  <div class="app-container">
    <div class="blog-content-container">
      <div class="blog-content-title">{{ blogContent.title }}</div>
      <div class="blog-content-summary">简介：{{ blogContent.summary }}</div>
      <div class="blog-content-tag">标签：
        <div v-if="blogContent.tagId!=='' && blogContent.tagId!==undefined" v-for="tagId in blogContent.tagIds">
          <BlogTag :tag-color="tags[tagId].tagColor" :tag-name="tags[tagId].tag" :tag-id="tagId"/>
        </div>
      </div>
      <div class="blog-content-content">
        <v-md-editor mode="preview" default-show-toc v-model="blogContent.content"></v-md-editor>
      </div>
    </div>

  </div>
</template>

<script setup>
import {getBlog, listTag} from "@/api/business/salish";
import BlogTag from "@/components/Tag";

import {ref} from "vue";
import {useRoute} from "vue-router";
const route = useRoute();

const tags = ref({});
const blogContent = ref({});

const blogId = ref(route.query.blogId);
listTag().then(response => {
  let tagsOptions=[]
  tagsOptions = response.rows;
  tagsOptions.forEach(item=>{
    tags.value[item.id]=item;
  })
  getBlog(blogId.value).then(res => {
    blogContent.value = res.data;
  })
});

</script>
<style scoped lang="scss">
.dark{
  .blog-content-container{
    background-color: rgba(51, 51, 51, 0.4);
  }
  .v-md-editor{
    background-color: rgba(51, 51, 51, 0.4);
  }
  .v-md-pre-wrapper {
    background-color: rgba(51, 51, 51, 0.4) !important;
  }
}
.blog-content-container {
  border-radius: 2px;
  background-color: #FFFFFF;
  margin: 10px;
  padding: 10px;
  /*min-height: 100%;*/
}

.blog-content-title {
  width: 100%;
  text-align: center;
  margin-top: 5px;
  margin-left: 10px;
  margin-bottom: 10px;
  font-size: 30px;
  font-weight: 600;
}

.blog-content-title2 {
  width: 100%;
  text-align: center;
  margin-top: 5px;
  margin-left: 10px;
  margin-bottom: 10px;
  font-size: 25px;
  font-weight: 400;
}

.blog-content-tag{
  margin-left: 20px;
  display: inline-flex;
}

.blog-content-content{

}

.blog-cover {
  height: 90px;
  width: 120px;
}

.blog-content-summary {
  margin: 5px;
  min-height: 30px;
  font-size: 13px;
  font-weight: 200;
  color: #666;
}

.dark .blog-content-summary{
  color: #ddd;
}

.pagination-container {
  background-color: transparent !important;
}
</style>
