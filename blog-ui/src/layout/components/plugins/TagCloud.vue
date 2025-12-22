<template>
  <div class="tag-cloud-container">
    <div style="margin: 20px">
      <h3>标签云</h3>
      <BlogTag v-for="tag in tagsOptions" :tag-color="tag.tagColor" :tag-name="tag.tag" :tag-id="tag.id"/>
    </div>
  </div>
</template>

<script setup>
import {listTag} from "@/api/business/salish";
import {onMounted} from "vue";

const tagsOptions = ref([])

onMounted(() => {
  listTag({timesAsc: true}).then(response => {
    tagsOptions.value = response.rows;
  });
})
</script>
<style>
.tag-cloud-container {
  border-radius: 2px;
  width: 100%;
  height: 300px;
  float: right;
  background-color: #FFFFFF;
}
.dark .tag-cloud-container{
  background-color: rgba(51, 51, 51, 0.4);;
}

</style>
