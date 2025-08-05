<template>
    <div class="introduction_container">
      <div style="margin: 20px">
        <div class="title">咸鱼笔记</div>
        <div style="text-align: center">
          <img class="head_img" src="@/assets/logo/logo.png" alt="">
        </div>
        <div class="content">
          <div class="total">文章总数：{{total}}</div>
          <div class="year">年内文章：{{thisYear}}</div>
          <div class="nearest">近期方向：{{field}}</div>
        </div>
      </div>
    </div>
</template>

<script setup>
import {blogIntroduction} from "@/api/business/salish.js"

import {onMounted} from "vue";
const total = ref(0);
const thisYear = ref(0);
const field = ref("");


onMounted(() => {
  blogIntroduction().then(res => {
    total.value = res.total;
    thisYear.value = res.yearTotal;
    field.value = res.field;
  })
})
</script>
<style scoped lang="scss">
.dark .introduction_container{
  background-color: rgba(51, 51, 51, 0.4);;
}

.introduction_container {
  border-radius: 2px;
  width: 100%;
  height: 300px;
  float: right;
  background-color: #FFFFFF;
  .title{
    font-size: 20px;
    text-align: center;
  }
  .head_img{
    width: 80px;
    height: 80px;
    margin-top: 20px;
    border-radius: 100%;
    border: 1px solid #ddd;
  }
  .total{
    margin-top: 20px;
    margin-left: 5%;
    font-size: 13px;
    color: #45b787;
  }
  .year{
    margin-top: 5px;
    margin-left: 5%;
    font-size: 13px;
    color: #fa7e23;
  }
  .nearest{
    margin-top: 5px;
    margin-left: 5%;
    font-size: 13px;
    color: #00afff;
  }
}

</style>
