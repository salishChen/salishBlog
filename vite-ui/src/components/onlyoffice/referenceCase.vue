<template>
     <officeeditor :option="option"></officeeditor>
</template>

<script setup>
import officeeditor from './officeeditor'
import { getScenarioBase } from "@/api/textscenario/mapscenario"
const route = useRoute(); // 路由
const caseId = ref(); // 参考案例id

onMounted(() => {
  // 获取路由上面的参考案例id
  caseId.value = route.query.caseId;
  getScenarioidFn()
});

const  getScenarioidFn = () => {  
  // 获取参考案例详情
  getScenarioBase(caseId.value).then((response) => {
    if (response.code == 200) {
      fileObject.value = response.data;
    }
  });
}

</script>

<style scoped>

</style>