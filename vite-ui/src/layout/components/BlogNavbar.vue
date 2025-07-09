<template>
  <div class="navbar">
    <div :class="appStore.device==='mobile'?'left-menu-mobile':'left-menu'">
      <el-menu
          :default-active="activeIndex"
          class="el-menu-demo"
          mode="horizontal"
          background-color="#fff"
          text-color="#3c3f41"
          :router="true"
          active-text-color="#2b2b2b">
        <el-menu-item index="/salish/blog">记事</el-menu-item>
        <el-menu-item index="/salish/category/0">分类</el-menu-item>
        <el-menu-item index="/salish/interfile">归档</el-menu-item>
        <el-menu-item index="/salish/fillPit">填坑</el-menu-item>
        <!--        <el-menu-item index="9">关于我</el-menu-item>-->
      </el-menu>
    </div>

    <div class="right-menu" v-if="appStore.device!=='mobile'">
<!--      <el-tooltip content="全屏">-->
<!--      <screenfull id="screenfull" class="right-menu-item hover-effect"/>-->
<!--      </el-tooltip>-->
      <el-tooltip content="工作台" @click="toWorkspace">
        <div style="padding-top: 2px;" class="right-menu-item hover-effect" @click="toWorkspace" >
          <svg-icon :icon-class="'ctrl'" style="font-size: 22px"></svg-icon>
        </div>
      </el-tooltip>
    </div>
  </div>
</template>

<script setup>
import Screenfull from '@/components/Screenfull'
import useAppStore from '@/store/modules/app'
import useUserStore from '@/store/modules/user'
import useSettingsStore from '@/store/modules/settings'
import router from "@/router";

const appStore = useAppStore()
const userStore = useUserStore()
const settingsStore = useSettingsStore()

const activeIndex = ref('1')

function toggleSideBar() {
  appStore.toggleSideBar()
}

async function toWorkspace() {
  await router.replace({path: "/index"})
  let settings = JSON.parse(localStorage.getItem('layout-setting'));
  settings.defaultPage = 2;
  localStorage.setItem('layout-setting', JSON.stringify(settings));
  await settingsStore.changeSetting({key:'defaultPage',value: 2})
}

</script>

<style lang="scss" scoped>
.navbar {
  height: 50px;
  overflow: hidden;
  position: relative;
  background: #fff;
  box-shadow: 0 1px 4px rgba(0, 21, 41, .08);

  .hamburger-container {
    line-height: 46px;
    height: 100%;
    float: left;
    cursor: pointer;
    transition: background .3s;
    -webkit-tap-highlight-color: transparent;

    &:hover {
      background: rgba(0, 0, 0, .025)
    }
  }

  .breadcrumb-container {
    float: left;
  }

  .topmenu-container {
    position: absolute;
    left: 50px;
  }

  .errLog-container {
    display: inline-block;
    vertical-align: top;
  }

  .left-menu {
    padding-left: 50px;
    float: left;
    height: 100%;
    line-height: 50px;
    width: 80%;
  }

  .left-menu-mobile {
    float: left;
    height: 100%;
    line-height: 50px;
    width: 80%;
  }

  .right-menu {
    float: right;
    height: 100%;
    line-height: 50px;
    display: inline-flex;

    &:focus {
      outline: none;
    }

    .right-menu-item {
      display: inline-block;
      padding: 0 20px;
      height: 100%;
      font-size: 18px;
      color: #5a5e66;
      vertical-align: text-bottom;


      &.hover-effect {
        cursor: pointer;
        transition: background .3s;

        &:hover {
          background: rgba(0, 0, 0, .025)
        }
      }
    }

    .avatar-container {
      margin-right: 30px;

      .avatar-wrapper {
        margin-top: 5px;
        position: relative;

        .user-avatar {
          cursor: pointer;
          width: 40px;
          height: 40px;
          border-radius: 10px;
        }

        .el-icon-caret-bottom {
          cursor: pointer;
          position: absolute;
          right: -20px;
          top: 25px;
          font-size: 12px;
        }
      }
    }
  }
}
</style>
