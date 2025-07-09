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
                <i class="el-icon-date"/> {{ item.createTime }}
              </div>
            </div>

          </template>

        </el-main>
      </el-container>
    </div>
  </div>
</template>

<script setup>
import {listBlogInfo, listTag, getDictData} from "@/api/business/salish";
import {infiniteScroll} from "@/views/dashboard/mixins/infiniteScroll";
const {isTail} = infiniteScroll()
const route = useRoute();
const router = useRouter();

const activeName = ref('');
const blogList = ref([]);
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
  listBlogInfo(queryParams.value).then(response => {
    blogList.value = response.rows;
    total.value = response.total;
    loading.value = false;
  });
}

function getLastPage() {
  if (isLoading.value) {
    isLoading.value = false
    queryParams.value.pageNum = queryParams.value.pageNum + 1
    listBlogInfo(queryParams.value).then(response => {
      blogList.value = blogList.value.concat(response.rows)
      total.value = response.total;
      isLoading.value = true;
    });
  }

}

function openBlog(id) {
  router.push({path: "/salish/blogContent", query: {blogId: id}})
}

function handleTabClick(tab, event) {
  queryParams.value.blogType = tab.props.name;
  getList();
}

tagId.value = route.params && route.params.tagId;
if (tagId.value > 0) {
  queryParams.value.tagId = tagId.value;
}
getDictData("blog_type").then(response => {
  blogTypeOptions.value = response.data;
});
listTag().then(async response => {
  let tagsOptions = []
  tagsOptions = response.rows;
  tagsOptions.forEach(item => {
    tags.value[item.id] = item;
  })
  await getList();
});

watch(isTail, (newValue) => {
  if (newValue && (total.value > queryParams.value.pageSize * queryParams.value.pageNum)) {
    getLastPage();
  }
})

</script>
<style src="@/style/blog.css" scoped></style>
