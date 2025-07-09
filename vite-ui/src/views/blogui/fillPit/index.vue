<template style="height: 100%">
  <div class="app-container pit">
    <div>
      <el-collapse v-model="tagShow" accordion @change="changeTag">
        <el-collapse-item v-for="tag in pitTags" :title="tag" :name="tag">
          <div class="pit-container" v-if="tag!=undefined&&pitList[tag]!=undefined&&pitList[tag].length>0">
            <div :style="{borderBottom:pitList[tag].length-1===index?'':'1px #e6e6e6 solid'}"
                 v-for="(item,index) in pitList[tag]">
              <div class="pit-content" v-html="item.content"></div>
            </div>
          </div>
        </el-collapse-item>
      </el-collapse>
    </div>


  </div>
</template>

<script setup>
import {listPits, getPitTags} from "@/api/business/salish";

const pitList = ref({});
const pitTags = ref([]);
const tagShow = ref('');

const data = reactive({
  total: 0,
  queryParams: {
    tag: undefined,
  },
})
const {total, queryParams} = toRefs(data)

function getPitTagList() {
  getPitTags().then(res => {
    pitTags.value = res.data;
    if (pitTags.value.length > 0) {
      tagShow.value = pitTags.value[0]
      queryParams.value.tag = pitTags.value[0];
      getList();
    }
  })
}

function changeTag(tag) {
  if (tag != undefined && tag != '') {
    queryParams.tag = tag;
    if (pitList.value[tag] == undefined || pitList.value[tag] == []) {
      getList();
    }
  }
}

/** 查询博客列表 */
function getList() {
  listPits(queryParams.value).then(response => {
    pitList.value[queryParams.value.tag] = response.rows;
  });
}

getPitTagList()

</script>
<style>
.pit .el-collapse-item__content {
  padding-bottom: 0;
}

</style>
<style scoped>
.pit {
  min-height: calc(100vh - 100px);
  background-color: white;
  padding: 10px;
  margin: 10px
}

.pit-container {
  border-radius: 2px;
  background-color: #FFFFFF;
  margin: 10px;
  padding: 10px;
  min-height: 60px;
}

.pit_block {
  border-bottom: 1px #e6e6e6 solid;
}

.pit-content {
  padding: 0 10px;
}
</style>
