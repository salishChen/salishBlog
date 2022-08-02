<template>
  <div :class="classObj" class="app-wrapper" :style="{'--current-color': theme}">
    <template v-if="defaultPage==1 || defaultPage==undefined">
      <div :class="{hasTagsView:needTagsView,sidebarHide:sidebar.hide}" class="" style="background-color: #f1f1f1">
        <div :class="{'fixed-header':fixedHeader}">
          <blog-navbar/>
        </div>
        <div :class="device==='mobile'?'blog-main-mobile-container':'blog-main-container'">
          <app-main/>
        </div>
        <tag-cloud class="tag-cloud"/>
        <right-panel>
          <settings/>
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
        <app-main/>
        <right-panel>
          <settings/>
        </right-panel>
      </div>
    </template>

  </div>
</template>

<script>
import RightPanel from '@/components/RightPanel'
import {AppMain, Navbar, BlogNavbar, Settings, Sidebar, TagsView} from './components'
import  './components/plugins/TagCloud'

import ResizeMixin from './mixin/ResizeHandler'
import {mapGetters, mapState} from 'vuex'
import variables from '@/assets/styles/variables.scss'
import TagCloud from "@/layout/components/plugins/TagCloud";

export default {
  name: 'Layout',
  components: {
    TagCloud,
    AppMain,
    Navbar,
    BlogNavbar,
    RightPanel,
    Settings,
    Sidebar,
    TagsView
  },
  mixins: [ResizeMixin],

  computed: {

    ...mapState({
      theme: state => state.settings.theme,
      sideTheme: state => state.settings.sideTheme,
      sidebar: state => state.app.sidebar,
      device: state => state.app.device,
      needTagsView: state => state.settings.tagsView,
      fixedHeader: state => state.settings.fixedHeader,
      defaultPage: state => state.settings.defaultPage,
    }),
    classObj() {
      return {
        hideSidebar: !this.sidebar.opened,
        openSidebar: this.sidebar.opened,
        withoutAnimation: this.sidebar.withoutAnimation,
        mobile: this.device === 'mobile'
      }
    },
    variables() {
      return variables;
    }
  },
  created() {

  },
  methods: {
    handleClickOutside() {
      this.$store.dispatch('app/closeSideBar', {withoutAnimation: false})
    }
  }
}
</script>

<style lang="scss" scoped>
@import "~@/assets/styles/mixin.scss";
@import "~@/assets/styles/variables.scss";

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
}
.blog-main-mobile-container {
  width: 100%;
}
.tag-cloud{
  margin: 10px 2% 0 0 ;
  width: 13%;
  float: right;
  display: inline-block;
  position: relative;
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
