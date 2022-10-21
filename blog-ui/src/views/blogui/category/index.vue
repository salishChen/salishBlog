<template>
  <div class="app-container">
    <el-tabs v-if="tagId==0" v-model="activeName" @tab-click="handleTabClick">
      <el-tab-pane label="全部" name=""/>
      <el-tab-pane v-for="item in blogTypeOptions" :label="item.dictLabel" :name="item.dictValue"/>
    </el-tabs>

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
                <BlogTag :tag-color="tags[tagId].tagColor" :tag-name="tags[tagId].tag" :tag-id="Number(tagId)"/>
              </div>
            </div>
            <div class="right-block">
              <div class="blog-time">
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
  name: "category",
  components: {
    Editor,
  },
  data() {
    return {
      // 总条数
      total: 0,
      activeName:'',
      // 博客表格数据
      blogList: [],
      blogTypeOptions: [],
      tags: {},
      tagId:0,
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
    this.tagId = this.$route.params && this.$route.params.tagId;
    if (this.tagId>0){
      this.queryParams.tagId = this.tagId;
    }
    getDictData("blog_type").then(response => {
      this.blogTypeOptions = response.data;
    });
    listTag().then(async response => {
      let tagsOptions = []
      tagsOptions = response.rows;
      tagsOptions.forEach(item => {
        this.tags[item.id] = item;
      })
      await this.getList();
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
    },
    handleTabClick(tab, event){
      this.queryParams.blogType = tab.name;
      this.getList();
    }
  }
};
</script>
<style src="@/style/blog.css" scoped></style>
