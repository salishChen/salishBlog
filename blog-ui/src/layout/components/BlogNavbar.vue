<template>
  <div class="navbar">
    <div :class="appStore.device==='mobile'?'left-menu-mobile':'left-menu'">
      <el-menu
          :default-active="activeIndex"
          mode="horizontal"
          :router="true">
        <el-menu-item @click="changeMenu(1)" index="/salish/blog">记事</el-menu-item>
        <el-menu-item @click="changeMenu(2)" index="/salish/category/0">分类</el-menu-item>
        <el-menu-item @click="changeMenu(3)" index="/salish/interfile">归档</el-menu-item>
        <el-menu-item @click="changeMenu(4)" index="/salish/fillPit">tips</el-menu-item>
        <el-menu-item @click="changeMenu(101)" index="/salish/zzz">绳网</el-menu-item>
        <!--        <el-menu-item index="9">关于我</el-menu-item>-->
      </el-menu>
    </div>

    <div class="right-menu">
      <dark-switch ref="darkRef"/>
      <worktop/>
    </div>
  </div>
</template>

<script setup>
import worktop from '@/components/Navbar/worktop.vue'
import DarkSwitch from '@/components/Navbar/DarkSwitch.vue'
import useAppStore from '@/store/modules/app'

const appStore = useAppStore()

const activeIndex = ref('1')

const darkStatus = ref();
const darkRef = ref();

function changeMenu(index) {
  if (index > 100) {
    if (!darkStatus.value && !darkRef.value?.isDark) {
      darkRef.value?.toggle()
    }
  } else {
    if (!darkStatus.value && darkRef.value?.isDark) {
      darkRef.value?.toggle()
    }
  }
}

onMounted(
    () => {
      darkStatus.value = darkRef.value?.darkStatus
    }
)

watch(
    () => {
      darkStatus.value = darkRef.value?.darkStatus
    }
)


</script>

<style lang="scss" scoped>
.navbar {
  height: 50px;
  overflow: hidden;
  position: fixed;
  z-index: 1000;
  width: 100%;
  background: #fff;
  box-shadow: 0 1px 4px rgba(0, 21, 41, .08);

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
    width: 70%;
  }

  .right-menu {
    float: right;
    height: 100%;
    line-height: 50px;
    display: inline-flex;

    &:focus {
      outline: none;
    }
  }
}

.dark {
  .navbar {
    background-color: rgba(0, 0, 0, 0.8);
    color: #eee;
  }

  .el-menu-item:not(.is-disabled):hover {
    background-color: var(--el-border-color)
  }

  .el-menu-item:not(.is-disabled):focus {
    background-color: var(--el-border-color)
  }
}
</style>
