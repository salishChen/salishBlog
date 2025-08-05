<template>
  <el-tooltip content="工作台" @click="toWorkspace">
    <div style="padding-top: 2px;" class="right-menu-item hover-effect" @click="toWorkspace" >
      <svg-icon icon-class="ctrl" style="font-size: 22px"></svg-icon>
    </div>
  </el-tooltip>
</template>

<script setup>
import useTagsViewStore from "@/store/modules/tagsView.js";
import router from "@/router";
const route = useRoute()
import useSettingsStore from '@/store/modules/settings'
const settingsStore = useSettingsStore()

async function toWorkspace() {
  useTagsViewStore().delView(route)
  await router.replace({path: "/index"})
  let settings = JSON.parse(localStorage.getItem('layout-setting'));
  settings.defaultPage = 2;
  localStorage.setItem('layout-setting', JSON.stringify(settings));
  await settingsStore.changeSetting({key:'defaultPage',value: 2})
}
</script>

<style scoped lang="scss">
.right-menu-item {
  display: inline-block;
  padding: 0 20px;
  height: 100%;
  font-size: 18px;
  color: var(--el-menu-text-color);
  vertical-align: text-bottom;


  &.hover-effect {
    cursor: pointer;
    transition: background .3s;

    &:hover {
      background:var(--el-color-info-light-8)
    }
  }
}
</style>