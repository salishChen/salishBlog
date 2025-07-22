<template>
  <div :class="classObj" class="app-wrapper" :style="{'--current-color': theme}">
    <template v-if="defaultPage==1 || defaultPage==undefined">
      <div :class="{hasTagsView:needTagsView,sidebarHide:sidebar.hide}" style="background-color: #F2F3F5">
        <div :class="{'fixed-header':fixedHeader}">
          <blog-navbar/>
        </div>
        <div style="padding-top: 60px">
          <introduce v-if="device!=='mobile'" class="introduction"/>
          <div :class="device==='mobile'?'blog-main-mobile-container':'blog-main-container'">
            <app-main/>
          </div>
          <tag-cloud v-if="device!=='mobile'" class="tag-cloud"/>
        </div>

        <right-panel>
          <settings ref="settingRef"/>
        </right-panel>
      </div>
    </template>
    <template v-if="defaultPage==2">
      <div v-if="device==='mobile'&&sidebar.opened" class="drawer-bg" @click="handleClickOutside"/>
      <sidebar v-if="!sidebar.hide" class="sidebar-container"/>
      <div :class="{hasTagsView:needTagsView,sidebarHide:sidebar.hide}" class="main-container">
        <div :class="{'fixed-header':fixedHeader}">
          <navbar/>
          <tags-view v-if="needTagsView"/>
        </div>
        <app-main style="padding: 20px"/>
        <right-panel>
          <settings ref="settingRef"/>
        </right-panel>
      </div>
    </template>

  </div>
</template>

<script setup>
import {useWindowSize} from '@vueuse/core'
import Sidebar from './components/Sidebar/index.vue'
import {AppMain, Navbar, BlogNavbar, Settings, TagsView} from './components'
import defaultSettings from '@/settings'
import TagCloud from "@/layout/components/plugins/TagCloud";
import Introduce from "@/layout/components/plugins/introduce.vue";
import RightPanel from '@/components/RightPanel'

import useAppStore from '@/store/modules/app'
import useSettingsStore from '@/store/modules/settings'

const settingsStore = useSettingsStore()
const theme = computed(() => settingsStore.theme);
const sideTheme = computed(() => settingsStore.sideTheme);
const sidebar = computed(() => useAppStore().sidebar);
const device = computed(() => useAppStore().device);
const needTagsView = computed(() => settingsStore.tagsView);
const fixedHeader = computed(() => settingsStore.fixedHeader);
const defaultPage = computed(() => settingsStore.defaultPage);

const classObj = computed(() => ({
  hideSidebar: !sidebar.value.opened,
  openSidebar: sidebar.value.opened,
  withoutAnimation: sidebar.value.withoutAnimation,
  mobile: device.value === 'mobile'
}))

const {width, height} = useWindowSize();
const WIDTH = 992; // refer to Bootstrap's responsive design

watchEffect(() => {
  if (device.value === 'mobile' && sidebar.value.opened) {
    useAppStore().closeSideBar({withoutAnimation: false})
  }
  if (width.value - 1 < WIDTH) {
    useAppStore().toggleDevice('mobile')
    useAppStore().closeSideBar({withoutAnimation: true})
  } else {
    useAppStore().toggleDevice('desktop')
  }
})

function handleClickOutside() {
  useAppStore().closeSideBar({withoutAnimation: false})
}

const settingRef = ref(null);

function setLayout() {
  settingRef.value.openSetting();
}
</script>

<style lang="scss" scoped>
@import "@/assets/styles/mixin.scss";
@import "@/assets/styles/variables.module.scss";

.app-wrapper {
  @include clearfix;
  position: relative;
  height: 100%;
  width: 100%;

  &.mobile.openSidebar {
    position: fixed;
    top: 0;
  }
}

.blog-main-container {
  border-radius: 2px;
  width: 70%;
  margin: 0 0 0 15%;
  display: inline-block;
  position: relative;
  padding-bottom: 30px;
}

.blog-main-mobile-container {
  width: 100%;
}

.introduction {
  margin: 20px 0 0 2%;
  width: 13%;
  float: left;
  display: inline-block;
  position: fixed;
}

.tag-cloud {
  margin: 20px 2% 0 0;
  width: 13%;
  float: right;
  display: inline-block;
  position: fixed;
}

.drawer-bg {
  background: #000;
  opacity: 0.3;
  width: 100%;
  top: 0;
  height: 100%;
  position: absolute;
  z-index: 999;
}

.fixed-header {
  position: fixed;
  top: 0;
  right: 0;
  z-index: 9;
  width: calc(100% - #{$base-sidebar-width});
  transition: width 0.28s;
}

.hideSidebar .fixed-header {
  width: calc(100% - 54px);
}

.sidebarHide .fixed-header {
  width: 100%;
}

.mobile .fixed-header {
  width: 100%;
}
</style>
