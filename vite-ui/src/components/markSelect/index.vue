<template>
  <div class="icon-body">
    <el-input
      v-model="markName"
      class="icon-search"
      clearable
      placeholder="请输入图标名称"
      @clear="filterIcons"
      @input="filterIcons"
    >
      <template #suffix><i class="el-icon-search el-input__icon" /></template>
    </el-input>
    <div class="icon-list">
      <div class="list-container">
        <div
          v-for="(item, index) in markList"
          class="icon-item-wrapper"
          :key="index"
          @click="selectedIcon(item)"
        >
          <div :class="['icon-item', { active: activeIcon === item.markName }]">
            <img :src="`/assets/mark/${ item.markFile }`" alt="" srcset="" style="width:80px;height:80px"/>
            <span>{{ item.markFile }}</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>


const markListAllArr = inject("markListAllArr"); //mark数组

const props = defineProps({
  activeIcon: {
    type: String,
  },
});

const markName = ref("");
const markList = ref(markListAllArr.value);
const emit = defineEmits(["selected"]);

function filterIcons() {
  markList.value = markListAllArr.value;
  console.log(markListAllArr.value);
  if (markName.value) {
    markList.value = markListAllArr.value.filter(
      (item) => {
        return item.markName.indexOf(markName.value) !== -1
      }
    );
  }
}

function selectedIcon(item) {
  emit("selected", item);
  document.body.click();
}

function reset() {
  markName.value = "";
  markList.value = markListAllArr.value;
}

defineExpose({
  reset,
});
</script>

<style lang='scss' scoped>
.icon-body {
  width: 100%;
  padding: 10px;
  .icon-search {
    position: relative;
    margin-bottom: 5px;
  }
  .icon-list {
    height: 200px;
    overflow: auto;
    .list-container {
      display: flex;
      flex-wrap: wrap;
      .icon-item-wrapper {
        width: calc(100% / 5);
        height: 120px;
        // line-height: 70px;
        cursor: pointer;
        display: flex;
        .icon-item {
          display: flex;
          flex-direction: column;
          justify-content: center;
          align-items: center;
          max-width: 100%;
          height: 100%;
          padding: 0 5px;
          &:hover {
            background: #ececec;
            border-radius: 5px;
          }
          .icon {
            flex-shrink: 0;
          }
          span {
            display: inline-block;
            vertical-align: -0.15em;
            fill: currentColor;
            padding-left: 2px;
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
          }
        }
        .icon-item.active {
          background: #ececec;
          border-radius: 5px;
        }
      }
    }
  }
}
</style>