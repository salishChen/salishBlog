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
              <div class="blog-time">
                <i class="el-icon-date"/>   {{item.createTime}}
              </div>
            </div>
          </template>
        </el-main>
      </el-container>
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
import {listBlog, listBlogInfo, listTag, getDictData} from "@/api/business/salish";
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
      tags: {},
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        title: undefined,
        tagId: undefined,
      },
      isLoading:true
    };
  },
  created() {
    this.pageConfig()
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
    async pageConfig(){
      let settings = JSON.parse(sessionStorage.getItem('layout-setting'));
      if (settings.defaultPage!=1){
        settings.defaultPage=1;
        console.log(111)
        sessionStorage.setItem('layout-setting', JSON.stringify(settings));
        await this.$store.dispatch('settings/changeSetting', {
          key: 'defaultPage',
          value: 1
        })
      }

    },
    /** 查询博客列表 */
    getList() {
      this.loading = true;
      listBlogInfo(this.queryParams).then(response => {
        this.blogList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    getLastPage() {
      if (this.isLoading){
        this.isLoading = false
        this.queryParams.pageNum = this.queryParams.pageNum + 1
        listBlogInfo(this.queryParams).then(response => {
          this.blogList = this.blogList.concat(response.rows)
          this.total = response.total;
          this.isLoading = true;
        });
      }

    },
    openBlog(id) {
      this.$router.push({path: "/salish/blogContent", query: {blogId: id}})
    }
  }
};
</script>
<style src="@/style/blog.css" scoped></style>
