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
  </div>
</template>

<script setup>
import {interfileBlog, listTag, getDictData} from "@/api/business/salish";
import Editor from '@/components/Editor';
import {infiniteScroll} from "@/views/dashboard/mixins/infiniteScroll";
const {isTail} = infiniteScroll()

const blogList = ref([]);
const blogObject = ref([]);
const blogTypeOptions = ref([]);
const tags = ref({});
const tagId = ref(0);

const data = reactive({
  total: 0,
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    title: undefined,
    tagId: undefined,
  },
  loading: true,
  isLoading: true
})
const {total, queryParams,loading, isLoading} = toRefs(data)

function getList() {
  loading.value = true;
  interfileBlog(queryParams.value).then(response => {
    blogObject.value = response.data.data;
    total.value = response.data.total;
    loading.value = false;
  });
}
function getLastPage() {
  if (isLoading.value) {
    isLoading.value = false
    queryParams.value.pageNum = queryParams.value.pageNum + 1
    interfileBlog(queryParams.value).then(response => {
      let data = response.data.data;
      console.log(blogObject.value)
      console.log(data[0][0].time)
      if (blogObject.value[blogObject.value.length-1][0].time == data[0][0].time) {
        blogObject.value[blogObject.value.length-1] = blogObject.value[blogObject.value.length-1].concat(data[0])
        data = data.slice(1, data.length-1);
        blogObject.value = blogObject.value.concat(data)
      } else {

        blogObject.value = blogObject.value.concat(data)
      }
      total.value = response.data.total;
      isLoading.value = true;
    });
  }
}
function openBlog(id) {
  router.push({path: "/salish/blogContent", query: {blogId: id}})
}

getDictData("blog_type").then(response => {
  blogTypeOptions.value = response.data;
});
listTag().then(response => {
  let tagsOptions = []
  tagsOptions = response.rows;
  tagsOptions.forEach(item => {
    tags.value[item.id] = item;
  })
  getList();
});

watch(isTail, (newValue) => {
  if (newValue && (total.value > queryParams.value.pageSize * queryParams.value.pageNum)) {
    getLastPage();
  }
})
</script>
<style>
.interfile .el-divider__text {
  background-color: #f1f1f1;
}
.dark .interfile .el-divider__text {
  background-color: #333;
  opacity:0.8;
}
</style>
<style src="@/style/blog.scss" scoped>

</style>
