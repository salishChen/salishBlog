<!--onlyoffice 编辑器-->
<template>
  <div id="editorDiv"></div>
</template>

<script>
export default {
  name: 'officeeditor',
  props: {
    option: {
      type: Object,
      default: () => {
        return {}
      }
    }
  },
  data() {
    return {
      doctype: '',
      docEditor:null,
    }
  },
  mounted() {
    if (this.option.url) {
      this.setEditor(this.option)
    }
  },
  methods: {
    setEditor(option) {
      this.doctype = this.handleDocType(option.fileType)
      let config = {
        document: {
          mode: option.mode,
          fileType: option.fileType,
          key: option.key,
          title: option.title,
          url: option.url,
          permissions: {
            comment: true,
            download: true,
            modifyContentControl: true,
            modifyFilter: true,
            edit: option.isEdit,
            fillForms: true,
            review: true,
          },
        },
        documentType: 'word',
        editorConfig: {
          user: {
            id: option.user.id+'',
            name: option.user.name,
          },
          mode: option.mode,
          // 隐藏插件菜单
          customization: {
            plugins: false,
            forcesave: true,
          },
          lang: 'zh',
          // callbackUrl: `${import.meta.env.VITE_APP_API_URL} +'/callback' `,
          callbackUrl: option.editUrl,
        },
        height: '100%',
        width: '100%',
      }

      console.log(config)
      this.docEditor = new DocsAPI.DocEditor('editorDiv', config)
    },
    handleDocType(fileType) {
      let docType = '';
      let fileTypesDoc = [
        'doc', 'docm', 'docx', 'dot', 'dotm', 'dotx', 'epub', 'fodt', 'htm', 'html', 'mht', 'odt', 'ott', 'pdf', 'rtf', 'txt', 'djvu', 'xps'
      ];
      let fileTypesCsv = [
        'csv', 'fods', 'ods', 'ots', 'xls', 'xlsm', 'xlsx', 'xlt', 'xltm', 'xltx'
      ];
      let fileTypesPPt = [
        'fodp', 'odp', 'otp', 'pot', 'potm', 'potx', 'pps', 'ppsm', 'ppsx', 'ppt', 'pptm', 'pptx'
      ];
      if (fileTypesDoc.includes(fileType)) {
        docType = 'text'
      }
      if (fileTypesCsv.includes(fileType)) {
        docType = 'spreadsheet'
      }
      if (fileTypesPPt.includes(fileType)) {
        docType = 'presentation'
      }
      return docType;
    }
  },
  watch: {
    option: {
      handler: function (n, o) {
        if (n.url) {
          console.log(this.docEditor)
          if(this.docEditor){
            this.docEditor.destroyEditor();
          }
          // let editdiv = document.getElementById('editorDiv');
          // while (editdiv.firstChild){
          //   editdiv.removeChild(editdiv.firstChild);
          // }

          this.setEditor(n)
        }else {
          if(this.docEditor){
            this.docEditor.destroyEditor();
          }
        }
        // this.doctype = this.handleDocType(n.fileType)
      },
      deep: true,
    }
  }
}
</script>

<style scoped>

</style>