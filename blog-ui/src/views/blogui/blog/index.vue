<template>
  <div class="app-container">
    <div class="blog-container"  v-for="item in blogList">
      <div class="blog-title">{{item.title}}</div>
      <el-container>
        <el-aside style="background-color: transparent;width: 180px">
          <img class="blog-cover" :src="item.cover">
        </el-aside>
        <el-main style="padding: 5px 5px 5px 5px">
          <div class="blog-summary">
            {{item.summary}}
          </div>
          <div v-for="tagid in item.tagIds">
            <BlogTag :tag-color="tags[tagid].tagColor" :tag-name="tags[tagid].tag"/>
          </div>
        </el-main>
      </el-container>
    </div>
    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />
  </div>
</template>

<script>
import { listBlog, getBlog, delBlog, addBlog, updateBlog, exportBlog } from "@/api/business/blog";
import Editor from '@/components/Editor';
import {listTag} from "@/api/business/tag";

export default {
  name: "Blog",
  components: {
    Editor,
  },
  data() {
    return {
      // 总条数
      total: 0,
      // 博客表格数据
      blogList: [],
      tags:{},
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
    this.getDicts("blog_type").then(response => {
      this.blogTypeOptions = response.data;
    });
    listTag().then(response => {
      let tagsOptions=[]
      tagsOptions = response.rows;
      tagsOptions.forEach(item=>{
        this.tags[item.id]=item;
      })
    });
    this.getList();
  },
  methods: {
    /** 查询博客列表 */
    getList() {
      this.loading = true;
      listBlog(this.queryParams).then(response => {
        this.blogList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    }
  }
};
</script>
<style>
.blog-container{
  background-color: #FFFFFF;
  margin: 10px;
  padding: 10px;
  height: 160px;
}
.blog-title{
  margin-top: 5px;
  margin-left: 10px;
  font-size: 20px;
  font-weight: 500;
}
.blog-cover{
  height: 90px;
  width: 120px;
}
.blog-summary{
  margin:5px;
  height: 60px;
  font-size: 13px;
  font-weight: 200;
  color: #666;
}
.pagination-container{
  background-color:transparent !important;
}
</style>
