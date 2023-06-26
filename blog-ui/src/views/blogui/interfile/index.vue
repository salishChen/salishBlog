<template>
  <div class="app-container interfile">
    <div v-for="(list,index) in blogObject" v-if="blogObject.length>0">
      <div style="color: #aaa;margin-top: 20px;width: calc(100% - 10px);padding-left: 10px">
        <el-divider content-position="left">
          {{ list[0].time }}
        </el-divider>
      </div>
      <div class="blog-container" v-for="item in list" @click="openBlog(item.id)">
        <div class="blog-title">{{ item.title }}</div>
        <el-container>
          <el-aside style="background-color: transparent;width: 180px">
            <img class="blog-cover" :src="item.cover">
          </el-aside>
          <el-main style="padding: 5px 5px 5px 5px">
            <div class="blog-summary">
              {{ item.summary }}
            </div>
            <template v-if="item.tagId!='' && item.tagId!=undefined">
              <div class="left-block">
                <div v-for="tagId in item.tagIds">
                  <BlogTag :tag-color="tags[tagId].tagColor" :tag-name="tags[tagId].tag" :tag-id="tagId"/>
                </div>
              </div>
              <div class="right-block">
                <div class="blog-time">
                  <i class="el-icon-date"/> {{ item.createTime }}
                </div>
              </div>
            </template>
          </el-main>
        </el-container>
      </div>
    </div>


<!--    <pagination
      style="right: 20px"
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />-->
  </div>
</template>

<script>
import {interfileBlog, listTag, getDictData} from "@/api/business/salish";
import Editor from '@/components/Editor';
import infiniteScroll from "@/views/dashboard/mixins/infiniteScroll";

export default {
  name: "Blog",
  components: {
    Editor,
  },
  mixins:[infiniteScroll],
  data() {
    return {
      // 总条数
      total: 0,
      // 博客表格数据
      blogList: [],
      blogObject: [],
      tags: {},
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
    getDictData("blog_type").then(response => {
      this.blogTypeOptions = response.data;
    });
    listTag().then(response => {
      let tagsOptions = []
      tagsOptions = response.rows;
      tagsOptions.forEach(item => {
        this.tags[item.id] = item;
      })
      this.getList();
    });
  },
  methods: {
    /** 查询博客列表 */
    getList() {
      this.loading = true;
      interfileBlog(this.queryParams).then(response => {
        this.blogObject = response.data.data;
        this.total = response.data.total;
        this.loading = false;
      });
    },
    getLasgPage(){
      this.queryParams.pageNum = this.queryParams.pageNum+1
      interfileBlog(this.queryParams).then(response => {
        let data = response.data.data;
        if (this.blogObject[0][0].time == data[0][0].time){
          this.blogObject[this.blogObject.length] = this.blogObject[this.blogObject.length].concat(data[0])
          data.slice(0,1);
          this.blogObject = this.blogObject.concat(data)
        }else {
          this.blogObject = this.blogObject.concat(data)
        }
        this.total = response.data.total;
      });
    },
    openBlog(id) {
      this.$router.push({path: "/salish/blogContent", query: {blogId: id}})
    }
  }
};
</script>
<style>
.interfile .el-divider__text{
  background-color: #f1f1f1;
}
</style>
<style src="@/style/blog.css" scoped>

</style>
