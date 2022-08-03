<template>
  <div class="app-container">
    <div class="blog-container" v-if="blogList.length>0" v-for="item in blogList" @click="openBlog(item.id)">
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
              <div class="blog-summary">
                <i class="el-icon-date"/>   {{item.createTime}}
              </div>
            </div>
          </template>
        </el-main>
      </el-container>
    </div>
    <pagination
      style="right: 20px"
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />
  </div>
</template>

<script>
import {listBlog, listBlogInfo, listTag, getDictData} from "@/api/business/salish";
import Editor from '@/components/Editor';

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
      listBlogInfo(this.queryParams).then(response => {
        this.blogList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    openBlog(id) {
      this.$router.push({path: "/salish/blogContent", query: {blogId: id}})
    }
  }
};
</script>
<style scoped>
.blog-container {
  border-radius: 2px;
  background-color: #FFFFFF;
  margin: 10px;
  padding: 10px;
  height: 160px;
}

.blog-title {
  margin-top: 5px;
  margin-left: 10px;
  font-size: 20px;
  font-weight: 500;
}

.blog-cover {
  height: 90px;
  width: 120px;
}

.blog-summary {
  margin: 5px;
  height: 60px;
  font-size: 13px;
  font-weight: 200;
  color: #666;
}

.pagination-container {
  background-color: transparent !important;
}
.left-block{
  display: inline-flex;
  position: relative;
  float: left;
}
.right-block{
  display: inline-block;
  position: relative;
  float: right;
}
</style>
