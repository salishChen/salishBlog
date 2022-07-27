<template>
  <div class="navbar">
    <div class="left-menu">
      <el-menu
        :default-active="activeIndex"
        class="el-menu-demo"
        mode="horizontal"
        @select="handleSelect"
        background-color="#fff"
        text-color="#3c3f41"
        active-text-color="#2b2b2b">
        <el-menu-item index="1">博客</el-menu-item>
        <el-menu-item index="2">分类</el-menu-item>
        <el-menu-item index="3">归档</el-menu-item>
        <el-menu-item index="9">关于我</el-menu-item>
      </el-menu>
    </div>
    <div class="right-menu">
      <template v-if="device!=='mobile'">
        <screenfull id="screenfull" class="right-menu-item hover-effect"/>
        <el-tooltip content="工作台" class="right-menu-item hover-effect" @click.native="toWorkspace">
          <div style="padding-top: 2px;">
            <svg-icon icon-class="druid" style="font-size: 25px"></svg-icon>
          </div>
        </el-tooltip>
      </template>
    </div>
  </div>
</template>

<script>
import {mapGetters} from 'vuex'
import Breadcrumb from '@/components/Breadcrumb'
import TopNav from '@/components/TopNav'
import Hamburger from '@/components/Hamburger'
import Screenfull from '@/components/Screenfull'
import SizeSelect from '@/components/SizeSelect'
import Search from '@/components/HeaderSearch'
import RuoYiGit from '@/components/RuoYi/Git'
import RuoYiDoc from '@/components/RuoYi/Doc'

export default {
  components: {
    Breadcrumb,
    TopNav,
    Hamburger,
    Screenfull,
    SizeSelect,
    Search,
    RuoYiGit,
    RuoYiDoc
  },
  computed: {
    ...mapGetters([
      'sidebar',
      'avatar',
      'device'
    ]),
    setting: {
      get() {
        return this.$store.state.settings.showSettings
      },
      set(val) {
        this.$store.dispatch('settings/changeSetting', {
          key: 'showSettings',
          value: val
        })
      }
    },
    topNav: {
      get() {
        return this.$store.state.settings.topNav
      }
    }
  },
  data(){
    return{
      activeIndex:'1'
    }
  },
  methods: {
    handleSelect(){

    },
    toggleSideBar() {
      this.$store.dispatch('app/toggleSideBar')
    },
    async toWorkspace() {
      location.href = '/login'
      let settings = JSON.parse(sessionStorage.getItem('layout-setting'));
      settings.defaultPage=2;
      sessionStorage.setItem('layout-setting', JSON.stringify(settings));
      await this.$store.dispatch('settings/changeSetting', {
        key: 'defaultPage',
        value: 2
      })
    }
  }
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

  .left-menu{
    padding-left: 50px;
    float: left;
    height: 100%;
    line-height: 50px;
    width: 80%;
  }

  .right-menu {
    float: right;
    height: 100%;
    line-height: 50px;

    &:focus {
      outline: none;
    }

    .right-menu-item {
      display: inline-block;
      padding: 0 8px;
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
