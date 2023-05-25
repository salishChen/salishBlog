<template>
  <v-md-editor v-model="blogContent" @change="changeContent" @save="mdSave()" @upload-image="handleUploadImage"
               :style="{minHeight:minHeight}"></v-md-editor>
</template>

<script>

export default {
  name: "Markdown",
  props: {
    content: {
      type: String,
      default: ""
    },
    /* 高度 */
    height: {
      type: String,
      default: null
    },
    /* 最小高度 */
    minHeight: {
      type: String,
      default: null
    },
    save: {
      type: Function,
      default: null
    }
  },
  data() {
    return {
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
        this.$emit('update:content', val)
      }
    },
  },
  watch: {},
  mounted() {
  },
  methods: {
    mdSave() {
      console.log(this.save)
      if (this.save) {
        this.save();
      }
    },
    handleUploadImage() {

    },
    changeContent() {
      // console.log(this.blogContent)
      // console.log(this.content)
      // this.$emit('content', this.blogContent)
    }
  }

}
</script>

<style scoped>

</style>
