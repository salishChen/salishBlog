import {createApp} from 'vue'

import Cookies from 'js-cookie'

import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import locale from 'element-plus/es/locale/lang/zh-cn'
import 'element-plus/theme-chalk/dark/css-vars.css'

import '@/assets/styles/index.scss' // global css

import App from './App'
import store from './store'
import router from './router'
import directive from './directive' // directive

// 注册指令
import plugins from './plugins' // plugins
import {download} from '@/utils/request'

// svg图标
import 'virtual:svg-icons-register'
import SvgIcon from '@/components/SvgIcon'
import elementIcons from '@/components/SvgIcon/svgicon'

import './permission' // permission control
import { getDicts } from "@/api/system/dict/data";
import { getConfigKey } from "@/api/system/config";

import {useDict} from '@/utils/dict'
import {
  parseTime,
  resetForm,
  addDateRange,
  handleTree,
  selectDictLabel,
  selectDictLabels,
  selectListLabel,
  selectListItem,
  downloadResources
} from '@/utils/ruoyi'

// 分页组件
import Pagination from '@/components/Pagination'
// 自定义表格工具组件
import RightToolbar from '@/components/RightToolbar'
// 富文本组件
import Editor from "@/components/Editor"
// 文件上传组件
import FileUpload from "@/components/FileUpload"
// 图片上传组件
import ImageUpload from "@/components/ImageUpload"
// 图片预览组件
import ImagePreview from "@/components/ImagePreview"
// 自定义树选择组件
import TreeSelect from '@/components/TreeSelect'
// 字典标签组件
import DictTag from '@/components/DictTag'

import BlogTag from "@/components/Tag"

import * as echarts from 'echarts'


import VMdEditor from '@kangc/v-md-editor/lib/codemirror-editor';
import VMdPreview from '@kangc/v-md-editor/lib/preview';
import '@kangc/v-md-editor/lib/style/codemirror-editor.css';
import githubTheme from '@kangc/v-md-editor/lib/theme/github.js';
import '@kangc/v-md-editor/lib/theme/style/github.css';

// highlightjs
import hljs from 'highlight.js';

// codemirror 编辑器的相关资源
import Codemirror from 'codemirror';
// mode
import 'codemirror/mode/markdown/markdown';
import 'codemirror/mode/javascript/javascript';
import 'codemirror/mode/css/css';
import 'codemirror/mode/htmlmixed/htmlmixed';
import 'codemirror/mode/vue/vue';
// edit
import 'codemirror/addon/edit/closebrackets';
import 'codemirror/addon/edit/closetag';
import 'codemirror/addon/edit/matchbrackets';
// placeholder
import 'codemirror/addon/display/placeholder';
// active-line
import 'codemirror/addon/selection/active-line';
// scrollbar
import 'codemirror/addon/scroll/simplescrollbars';
import 'codemirror/addon/scroll/simplescrollbars.css';
// style
import 'codemirror/lib/codemirror.css';

import defaultSettings from '@/settings'
if (localStorage.getItem('layout-setting')===null) {
  localStorage.setItem('layout-setting',JSON.stringify(defaultSettings));
}
if (sessionStorage.getItem('layout-setting')===null) {
  sessionStorage.setItem('layout-setting',JSON.stringify(defaultSettings))
}

const Vue = createApp(App)

// 全局方法挂载
Vue.config.globalProperties.useDict = useDict
Vue.config.globalProperties.download = download
Vue.config.globalProperties.parseTime = parseTime
Vue.config.globalProperties.resetForm = resetForm
Vue.config.globalProperties.handleTree = handleTree
Vue.config.globalProperties.addDateRange = addDateRange
Vue.config.globalProperties.selectDictLabel = selectDictLabel
Vue.config.globalProperties.selectDictLabels = selectDictLabels
Vue.config.globalProperties.selectListLabel = selectListLabel
Vue.config.globalProperties.selectListItem = selectListItem
Vue.config.globalProperties.downloadResources = downloadResources
Vue.config.globalProperties.$echarts = echarts
Vue.config.globalProperties.getDicts = getDicts
Vue.config.globalProperties.getConfigKey = getConfigKey

// 全局组件挂载
Vue.component('DictTag', DictTag)
Vue.component('Pagination', Pagination)
Vue.component('TreeSelect', TreeSelect)
Vue.component('FileUpload', FileUpload)
Vue.component('ImageUpload', ImageUpload)
Vue.component('ImagePreview', ImagePreview)
Vue.component('RightToolbar', RightToolbar)
Vue.component('Editor', Editor)
Vue.component('BlogTag', BlogTag)

Vue.use(router)
Vue.use(store)
Vue.use(plugins)
Vue.use(elementIcons)
Vue.component('svg-icon', SvgIcon)

VMdEditor.Codemirror = Codemirror;
VMdEditor.use(githubTheme, {
  Hljs: hljs,
});

Vue.use(VMdEditor)
Vue.use(VMdPreview)
directive(Vue)

// 使用element-plus 并且设置全局的大小
Vue.use(ElementPlus, {
  locale: locale,
  // 支持 large、default、small
  size: Cookies.get('size') || 'default'
})

Vue.mount('#app')
