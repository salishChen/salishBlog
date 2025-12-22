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
          <template v-if="item.tagId!=='' && item.tagId!==undefined">
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
</template>

<script setup>
import {onMounted} from "vue";
import {listBlogInfo, listTag} from "@/api/business/salish";
import useSettingsStore from '@/store/modules/settings'

const router = useRouter()

import {infiniteScroll} from "@/views/dashboard/mixins/infiniteScroll";
import {useRouter} from "vue-router";

const {isTail} = infiniteScroll()

const data = reactive({
  total: 0,
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    title: undefined,
    tagId: undefined,
  },
})

const {total, queryParams} = toRefs(data)

const loading = ref(false)
const isLoading = ref(true)
const tags = ref({})
const blogList = ref([])

async function pageConfig() {
  let settings = JSON.parse(localStorage.getItem('layout-setting'));
  if (settings.defaultPage !== 1) {
    settings.defaultPage = 1;
    localStorage.setItem('layout-setting', JSON.stringify(settings));
    await useSettingsStore().changeSetting({key: 'defaultPage', value: '1'})
  }

}

/** 查询博客列表 */
function getList() {
  loading.value = true;
  listBlogInfo(queryParams.value).then(response => {
    blogList.value = response.rows;
    total.value = response.total;
    loading.value = false;
  });
}

function getLastPage() {
  if (isLoading) {
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
pageConfig()
onMounted(() => {
  nextTick(() => {
    listTag().then(response => {
      let tagsOptions
      tagsOptions = response.rows;
      tagsOptions.forEach(item => {
        tags.value[item.id] = item;
      })
      getList();
    });
  })

})


watch(isTail, (newValue) => {
  if (newValue && (total.value > queryParams.value.pageSize * queryParams.value.pageNum)) {
    getLastPage();
  }
})

</script>
<style src="@/style/blog.scss" scoped></style>
