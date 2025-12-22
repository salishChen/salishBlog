<template>
  <div class="app-container">
<!--    <div v-if="false" :style="{columnCount:columnsNum}" class="column" v-loading="loading">
      <div class="zzz-container" v-if="showList.length>0" v-for="item in showList" @click="openBlog(item.id)">
        <div style="display:block;">
          <img class="zzz-cover" :src="item.cover">
        </div>
        <div>
          <BlogTag v-for="tagId in item.tagIds" :tag-color="tags[tagId].tagColor" :tag-name="tags[tagId].tag"
                   :tag-id="tagId"/>
        </div>
        <div class="blog-title">{{ item.title }}</div>
      </div>
    </div>-->

    <div class="column">
      <div :style="{width:`calc(${100/columnsNum}% - 5px)`,display:'inline-block',marginLeft:`${index===1?0:20}px`}" v-for="index in columnsNum">
        <div class="zzz-container" v-for="item in columnsMap['column'+(index-1)]" @click="openBlog(item.id)">
          <div style="display:block;">
            <img class="zzz-cover" :src="item.cover">
          </div>
          <div>
            <BlogTag v-for="tagId in item.tagIds" :tag-color="tags[tagId].tagColor" :tag-name="tags[tagId].tag"
                     :tag-id="tagId"/>
          </div>
          <div class="blog-title">{{ item.title }}</div>
        </div>
      </div>

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
const showList = ref([])
const columnsMap = ref({})

const columnsNum = ref(5)

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
    getColumnsList(blogList.value, columnsNum.value)
    total.value = response.total;
    loading.value = false;
  });
}

function getLastPage() {
  if (isLoading) {
    isLoading.value = false
    queryParams.value.pageNum = queryParams.value.pageNum + 1
    listBlogInfo(queryParams.value).then(response => {
      getColumnsList(response.rows, columnsNum.value)
      total.value = response.total;
      isLoading.value = true;
    });
  }
}

function getColumnsList(list, columnsNum) {
  // const arrObject = {};
  // for (let i = 0; i < columnsNum; i++) {
  //   arrObject[i] = []; // 创建空的对象数组
  // }
  // list.forEach((element, index) => arrObject[index % columnsNum].push(element));
  // const cloGapList = [];
  // for (let key in arrObject) {
  //   cloGapList.push(...arrObject[key])
  // }
  //
  // return cloGapList;
  for (let i = 0; i < list.length; i++) {
    let number = i%columnsNum;
    columnsMap.value["column" + number].push(list[i])
  }
}

function openBlog(id) {
  router.push({path: "/salish/blogContent", query: {blogId: id}})
}

pageConfig()
onMounted(() => {
  for (let i = 0; i < columnsNum.value; i++) {
    columnsMap.value["column" + i] = []
  }
  nextTick(() => {
    let innerWidth = window.innerWidth;
    if (innerWidth < 992) {
      columnsNum.value = 2;
    } else if (innerWidth < 1200) {
      columnsNum.value = 3;
    } else {
      columnsNum.value = 5;
    }
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
    }
)

</script>
<style scoped>
.column {
  text-align: center;
  margin: 10px;
  display: flex;
}

.dark .zzz-container {
  background: rgba(51, 51, 51, 0.4);
}

.zzz-container {
  background-color: #FFFFFF;
  padding: 10px;
  min-height: 320px;
  margin-bottom: 15px;
  width: 100%;
  display: inline-block;
  vertical-align: top;
  text-align: initial;
  border-radius: 5px;
}

.blog-title {
  margin-top: 5px;
  margin-left: 10px;
  font-size: 18px;
  font-weight: 800;
}

.zzz-cover {
  border-radius: 5px;
  min-height: 200px;
  width: 90%;
  margin: 3% 5% 5% 5%
}

.blog-time {
  margin: 5px;
  min-height: 10px;
  font-size: 13px;
  font-weight: 200;
  color: #666;
}

.pagination-container {
  background-color: transparent !important;
}

.left-block {
  display: inline-flex;
  position: relative;
  float: left;
}

.right-block {
  display: inline-block;
  position: relative;
  float: right;
}

</style>
