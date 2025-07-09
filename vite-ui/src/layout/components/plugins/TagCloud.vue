<template>
  <div class="app-container">

    <div class="tag-cloud-container">
      <div style="margin: 20px">
        <h3>标签云</h3>
        <BlogTag v-for="tag in tagsOptions" :tag-color="tag.tagColor" :tag-name="tag.tag" :tag-id="tag.id"/>
      </div>
    </div>

  </div>
</template>

<script>
import {listBlog, listBlogInfo, listTag, getDictData} from "@/api/business/salish";
import Editor from '@/components/Editor';

export default {
  name: "TagCloud",
  components: {
    Editor,
  },
  data() {
    return {
      // 总条数
      total: 0,
      // 博客表格数据
      tagsOptions: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        title: undefined,
        tagId: undefined,
      },
    };
  },
  created() {
    listTag({timesAsc:true}).then(response => {
      this.tagsOptions = response.rows;
    });
  }
};
</script>
<style>
.tag-cloud-container {
  border-radius: 2px;
  width: 100%;
  height: 300px;
  float: right;
  background-color: #FFFFFF;
}

</style>
