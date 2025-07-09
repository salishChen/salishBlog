<template>
  <officeeditor :option="option"></officeeditor>
</template>

<script setup>
import officeeditor from './officeeditor'
import { ref,onMounted } from 'vue'
import {getUserProfile} from "@/api/system/user.js";
import axios from "axios";
const route = useRoute();

const data = reactive({
    id: route.params.id,
    option: {
      url: '',
      isEdit: false,
      fileType: '',
      title: '',
      user: {
        id: '',
        name: ''
      },
      mode: 'view',
      editUrl: '',
      key: String( Math.floor(Math.random() * 10000)),
    },
  })
const { option, user } = toRefs(data);
onMounted(()=>{
  getFile()
})


  function getFile() {
    getUserProfile().then(response => {
      if(response.code == 200){
        option.value.user.id = response.data.userId;
        option.value.user.name = response.data.userName;
        option.value.url = route.query.fileurl;
        option.value.title = route.query.title;
        // option.value.mode = route.query.mode;
        // option.value.isEdit = route.query.isEdit == "true"?true:false;
        option.value.fileType='docx';
        axios.get("/config.json").then(response => {
          option.value.editUrl = response.data.ONLYOFFICE_CALLBACKURL + '?originurl='+route.query.fileurl;
        })
      }
    });


  }
</script>

<style scoped>

</style>