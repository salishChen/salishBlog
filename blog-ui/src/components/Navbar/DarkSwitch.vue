<template>
  <el-switch
      class="dark-switch"
      style="margin-top: 10px"
      inline-prompt
      v-model="isDark"
      size="default"
      :width="45"
      @click="change"
      active-color="rgb(44, 44, 44)"
      inactive-color="rgb(242, 242, 242)"
      border-color="rgb(220, 223, 230)"
      :active-action-icon="MoonIcon"
      :inactive-action-icon="SunIcon"
  />
</template>

<script setup>
import {useDark, useToggle} from "@vueuse/core";
import MoonIcon from '@/assets/icons/vueSvg/MoonIcon.vue'
import SunIcon from '@/assets/icons/vueSvg/SunIcon.vue'

const isDark = useDark()
const darkStatus = ref(isDark.value)
function change() {
  darkStatus.value = isDark.value
  useToggle(isDark)
}
function toggle() {
  isDark.value=!isDark.value
  useToggle(isDark)
}
defineExpose({toggle, isDark, darkStatus})
onMounted(()=>{
  if (!darkStatus.value){
    isDark.value = true
    change()
  }
})
</script>
<style scoped lang="scss">
.dark-switch{
  :deep(.el-switch__action){
    background-color: transparent;
    color: #333;
  }
}

.dark .dark-switch{
  :deep(.el-switch__action){
    background-color: transparent;
    color: #fff;
  }
}
</style>