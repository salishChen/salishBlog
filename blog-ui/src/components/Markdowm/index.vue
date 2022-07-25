<template>
  <v-md-editor v-model="blogContent" @change="changeContent"  @upload-image="handleUploadImage" :style="{minHeight:minHeight}"></v-md-editor>
</template>

<script>
export default {
  name: "Markdown",
  props:{
    content:{
      type:String,
      default:""
    },
    /* 高度 */
    height: {
      type: String,
      default: null,
    },
    /* 最小高度 */
    minHeight: {
      type: String,
      default: null,
    },
  },
  data(){
    return{
      // blogContent:"",
    }
  },
  created() {
  },
  computed: {
    blogContent: {
      get() {
        return this.content
      },
      set(val) {
        console.log(val)
        this.$emit('update:content', val)
      }
    },
  },
  watch:{

  },
  mounted() {
  },
  methods:{
    handleUploadImage(){

    },
    changeContent(){
      // console.log(this.blogContent)
      // console.log(this.content)
      // this.$emit('content', this.blogContent)
    }
  }

}
</script>

<style scoped>

</style>
