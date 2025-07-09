<template>
  <div>
    <el-row style="display: flex;justify-content: center;font-weight: 400;font-size: 30px">
      {{title}}
    </el-row>
      <el-row :gutter="0">
        <el-col :span="24">
          <div style="width:100%;display: flex;justify-content: center;">
            <div ref="chartRef" style="width: 350px;height: 350px;"></div>
          </div>
        </el-col>
        <el-col :span="24">
          <el-row style="padding-top: 50px">
            <el-table :data="tableData" border :height="250" fit style="overflow:auto;">
              <el-table-column v-for="item in  tableColumnList" :prop="item.prop"  :label="item.label" :width="item.width">
                <template #default="scope" >
                  <div v-if="item.prop == 'position'" >
                    [{{scope.row.lon}},{{scope.row.lat}},{{scope.row.alt}}]
                  </div>
                </template>
              </el-table-column>>
            </el-table>
          </el-row>
        </el-col>
      </el-row>
  </div>
</template>

<script setup lang="ts" >
import {ref, watch} from "vue";
import * as echarts from "echarts";

const chartRef =ref(null)
let myChart = null
const xList = ref(['1','2','3'])
const yList =ref([1,2,3])
const infoList =ref()
const tableData = ref([])
const title=ref(null)
const tableColumnList =ref([])

const props = defineProps({
  option:{
    type:Object,
    required:true
  }
})

const loadInfo = () =>{
  title.value = props.option.name
  xList.value = props.option.nameList
  yList.value = props.option.valueList
  infoList.value = props.option.infoList
  tableColumnList.value = props.option.tableList
  tableData.value =[]
    echartsInit()
}

//界面初始化
const echartsInit =() =>{
  myChart = echarts.init(chartRef.value)

  const option ={
    title:{
    },
    textStyle:{
      color:'#ffffff'
    },
    tooltip:{
      trigger:'item'
    },
    legend:{
      orient:'vertical',
      left: 'left',
      textStyle:{
        color:'#ffffff'
      },
    },
    grid:{
      left:'30%'
    },
    xAxis:{},
    yAxis:{
      data:xList.value,
      axisLabel:{
        interval:0,
        rotate:0
      }
    },
    series:[
      {
        type:'bar',
        barWidth:'50%',
        data:yList.value
      }
    ],
  }

  myChart.setOption(option)
//监听柱状图点击事件
  myChart.on('click',function (params){
    console.log(params)
    tableData.value =[]
    infoList.value.forEach(item =>{
      if (item.type == params.name){
        tableData.value.push(item)
      }
    })
    console.log(tableData.value)
  })
}
//监听option，当值发生改变时，重新set option
watch(
    () => props.option,
    (newValue) => {
      if (myChart){
        loadInfo()
      }
    }
)


onMounted(() =>{
  loadInfo()
})




</script>


<style scoped lang="scss">
:deep(.el-table){
  background-color: #5f5f5f !important;
  color: #ffffff;
  --el-table-row-hover-bg-color:none;
}
:deep(.el-table th){
  background-color: #5f5f5f !important;
  color: #ffffff;
}
:deep(.el-table tr){
  background-color: #5f5f5f !important;
  color: #ffffff;
}
</style>