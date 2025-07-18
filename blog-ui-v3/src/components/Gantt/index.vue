<template>
  <div class="gantt-container">
    <div ref="ganttRef" class="gantt-content"></div>
    <div style="margin-top: 10px">
      <div style="display: inline-block;width: 100%">
        <div style="display: inline-flex">
          <span style="color: #333;margin-top: 4px">缩放</span>
          <el-slider class="gantt_slider" v-model="zoom" :max="6" @change="changeZoom"/>
        </div>
        <span style="font-size: 12px;float: right">双击甘特图内的任务可进行编辑</span>
      </div>
    </div>
  </div>
</template>

<script setup>
import {onMounted} from "vue";
import gantt from 'dhtmlx-gantt';
import 'dhtmlx-gantt/codebase/dhtmlxgantt.css';
import {parseTime} from "@/utils/ruoyi.js";

const openTaskEditor = ref(false);
const props = defineProps({
  //任务列表
  tasks: {
    type: Array,
    default: []
  },
  //默认显示时间类型
  timeType: {
    type: String,
    default: 'hour'
  },
  //列最小宽度
  minColumnWidth: {
    type: Number,
    default: 30
  },
  //时间轴高度
  scaleHeight: {
    type: Number,
    default: 80
  },
  //提示工具
  toolTip: {
    type: Function,
    default: (start, end, task) => `
        <div class="gantt-tooltop">
          <div>任务名称：${task.name}</div>
          <div>开始时间：${parseTime(task.start_date, '{y}-{m}-{d} {h}')}</div>
          <div>结束时间：${parseTime(task.end_date, '{y}-{m}-{d} {h}')}</div>
        </div>`
  },
  //双击编辑事件
  editFunction: {
    type: Function,
    default: () => {
    }
  },
  //拖拽任务事件
  updateTimeFunction: {
    type: Function,
    default: () => {
    }
  },
//冲突检测
  conflictDetection: {
    type: Function,
    default: () => {
    }
  }
});


const ganttRef = ref()

// 初始化甘特图
function initGantt() {
  //插件
  gantt.plugins({
    click_drag: true,//点击拖拽
    drag_timeline: true,//时间轴拖拽
    tooltip: true//提示
  });

  //配置
  gantt.config.fit_tasks = true;
  gantt.config.drag_project = true;
  gantt.config.drag_resize = true;
  gantt.config.drag_timeline = true;
  gantt.config.scale_height = props.scaleHeight;
  gantt.config.row_height = 25;
  gantt.config.bar_height = 18;
  gantt.config.autosize = true;
  gantt.config.autoscroll = true;
  gantt.config.drag_move = true;
  gantt.config.drag_resize = true;
  gantt.config.fit_tasks = true;
  gantt.config.min_grid_column_width = props.minColumnWidth;

  gantt.config.initial_scroll = false;
  gantt.config.show_links = false;
  gantt.config.show_progress = false;
  gantt.config.show_grid = true;
  gantt.config.scales = props.timeType === 'hour' ? [
    {unit: 'month', step: 1, date: '%Y, %F'},
    {unit: 'day', step: 1, date: '%j日'},
    {unit: 'hour', step: 1, date: '%H'}
  ] : [
    {unit: 'month', step: 1, date: '%Y, %F'},
    {unit: 'day', step: 1, date: '%j日'}
  ];
  gantt.config.columns = [
    {name: 'name', label: '任务名称', width: '120', align: 'center'}
  ];
  gantt.templates.task_text = function (start, end, task) {
    return task.name;
  };


  gantt.i18n.setLocale('cn');
  //日期格式转换
  gantt.templates.parse_date = function (date) {
    return new Date(date);
  };
  //日期格式转换
  gantt.templates.format_date = function (date) {
    return date.toISOString();
  };
  //任务右边的文本
  gantt.templates.rightside_text = function (start, end, task) {
    return task.groupName;
  };
  //提示
  gantt.templates.tooltip_text = props.toolTip

  //双击编辑,阻碍原生编辑，启动自定义弹框
  gantt.attachEvent("onBeforeLightbox", function (id) {
    gantt.ext.tooltips.tooltip.hide()
    props.editFunction(id)
  });

  //拖拽任务事件
  gantt.attachEvent("onAfterTaskUpdate", function (id, task) {
    //这里写任何自定义逻辑
    let taskInfo = {
      id: task.id,
      startDate: parseTime(task.start_date),
      endDate: parseTime(task.end_date)
    }
    props.updateTimeFunction(taskInfo)
  });

  gantt.init(ganttRef.value);
  gantt.ext.zoom.init(zoomConfig.value)
  gantt.parse(props.tasks);
}

var hourToStr = gantt.date.date_to_str("%H");

//小时范围格式
function hourRangeFormat(step) {
  return function (date) {
    // var intervalEnd = new Date(gantt.date.add(date, step, "hour") - 1)
    return hourToStr(date);
  }
}

//缩放配置
const zoomConfig = ref({
  levels: [
    {
      name: '0',
      min_column_width: 40,
      scale_height: 40,
      scales: [
        {unit: 'month', step: 1, date: '%Y, %F'}
      ]
    },
    {
      name: '1',
      min_column_width: 40,
      scale_height: 80,
      scales: [
        {unit: 'month', step: 1, date: '%Y, %F'},
        {unit: 'day', step: 7, date: '%j日'}
      ]
    },
    {
      name: '2',
      min_column_width: 120,
      scale_height: 80,
      scales: [
        {unit: 'month', step: 1, date: '%Y, %F'},
        {unit: 'day', step: 1, date: '%j日'}
      ]
    },
    {
      name: '3',
      min_column_width: 30,
      scale_height: 120,
      scales: [
        {unit: 'month', step: 1, date: '%Y, %F'},
        {unit: 'day', step: 1, date: '%j日'},
        {unit: 'hour', format: hourRangeFormat(12), step: 12, date: '%H'}
      ]
    },
    {
      name: '4',
      min_column_width: 20,
      scale_height: 120,
      scales: [
        {unit: 'month', step: 1, date: '%Y, %F'},
        {unit: 'day', step: 1, date: '%j日'},
        {unit: 'hour', format: hourRangeFormat(6), step: 6, date: '%H'}
      ]
    },
    {
      name: '5',
      min_column_width: 30,
      scale_height: 120,
      scales: [
        {unit: 'month', step: 1, date: '%Y, %F'},
        {unit: 'day', step: 1, date: '%j日'},
        {unit: 'hour', format: hourRangeFormat(3), step: 3, date: '%H'}
      ]
    },
    {
      name: '6',
      min_column_width: 20,
      scale_height: 120,
      scales: [
        {unit: 'month', step: 1, date: '%Y, %F'},
        {unit: 'day', step: 1, date: '%j日'},
        {unit: 'hour', step: 1, date: '%H'}
      ]
    },
    {
      name: '7',
      min_column_width: 40,
      scale_height: 120,
      scales: [
        {unit: 'month', step: 1, date: '%Y, %F'},
        {unit: 'day', step: 1, date: '%j日'},
        {unit: 'hour', step: 1, date: '%H'}
      ]
    }
  ],
})
const zoom = ref(2);

//改变缩放
function changeZoom() {
  gantt.ext.zoom.setLevel(zoom.value);
}

//刷新数据
function refreshData() {
  gantt.clearAll();
  gantt.parse(props.tasks);
}

function parseData() {
  gantt.parse(props.tasks);
}

onMounted(() => {
  initGantt();
  changeZoom()
})
defineExpose({refreshData,parseData})
</script>

<style lang="scss">
.gantt_task {
  background: #79bbff;
}

.gantt_task_progress {
  background: #53a8ff;
}

.gantt_grid_data .gantt_row.gantt_selected {
  background: #f0f3f9;
}

.gantt_slider {
  width: 200px;
  margin-left: 20px;
}

</style>
<style>
.gantt_tooltip {
  z-index: 100000000000;
}

.gantt_cal_light {
  z-index: 100000
}
</style>
