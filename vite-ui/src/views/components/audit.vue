<template>
  <div class="app-container">
      <el-timeline>
        <el-timeline-item v-for="(item,index) in auditList" :timestamp="item.auditTime" :key="index" placement="top">
          <el-card>
            <h4>{{item.auditUser}}{{getStatusStr(item.status)}}</h4>
            <p>{{item.auditOpinion}}</p>
          </el-card>
        </el-timeline-item>
      </el-timeline>
  </div>
</template>

<script>
  import request from '@/utils/request'
  export default {
    props: {
      query:{
        type:Object,
        default:()=>{
          return {}
        }
      }
    },
    data() {
      return {
        // 选中数组
        ids: [],
        // 非单个禁用
        single: true,
        // 非多个禁用
        multiple: true,
        // 总条数
        total: 0,
        // 项目表格数据
        auditList: [],
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 100,
        },
        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
      };
    },
    created() {
      this.getAuditList();
    },
    methods: {
      getAuditList() {
        listAudit(this.query).then(response => {
          this.auditList = response.rows;
          this.getAuditCount(response.total)
        });
      },
      getStatusStr(status) {
        if (status == 0) {
          return "拒绝，当前状态：【已退回】"
        } else if (status == 2) {
          return "已提交，当前状态：【待县级管理员审核】"
        } else if (status == 3) {
          return "同意，当前状态：【待市级级管理员审核】"
        } else if (status == 4) {
          return "同意，当前状态：【待省级管理员审核】"
        } else if (status == 5) {
          return "同意，当前状态：【审核通过】"
        }
      },
      getAuditCount(total){
        this.$emit("getAuditCount",total)
      }
    }
  };
</script>
<style>
  .project .el-select {
    width: 100%;
  }
</style>
