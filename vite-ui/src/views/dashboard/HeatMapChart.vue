<template>
  <div ref="heatChart" :class="className" :style="{height:height,width:width}"/>
</template>

<script>
import * as echarts from 'echarts'

import resize from './mixins/resize'

export default {
  mixins: [resize],
  props: {
    className: {
      type: String,
      default: 'chart'
    },
    width: {
      type: String,
      default: '100%'
    },
    height: {
      type: String,
      default: '300px'
    },
    chartData: {
      type: Array,
      default: []
    }
  },
  data() {
    return {
      chart: null,
    }
  },
  mounted() {

  },
  beforeDestroy() {
    if (!this.chart) {
      return
    }
    this.chart.dispose()
    this.chart = null
  }
  ,
  methods: {
    initChart() {

      let todayTime = echarts.number.parseDate(new Date());
      let dayTime = 3600 * 24 * 1000;
      let thatdayTime = todayTime - dayTime * 365;

      let today = echarts.format.formatTime('yyyy-MM-dd', todayTime);
      let thatday = echarts.format.formatTime('yyyy-MM-dd', thatdayTime);
      this.chart = echarts.init(this.$el, 'macarons')
      this.chart.setOption({
        visualMap: {
          min: 0,
          max: 5,
          inRange: {
            color: ['#ebedf0', '#c6e48b', '#7bc96f', '#239a3b', '#196027']
          },
          show: false
        },
        backgroundColor: '#fff',
        calendar: {
          cellSize: [14, 14],
          range: [thatday, today],
          itemStyle: {
            borderColor: '#fff',
            borderWidth: 4
          },
          splitLine: {
            show: false
          },
          dayLabel: {
            firstDay: 1, // 从周一开始
            nameMap:['Sun.','Mon.','Tue.','Wed.','Thu.','Fri.','Sat.']
          },
          monthLabel:{
            nameMap:'cn'
          },
          yearLabel: {show: false}
        },
        series: {
          type: 'heatmap',
          coordinateSystem: 'calendar',
          data: this.chartData
        }
      })

    }
  }
}
</script>
