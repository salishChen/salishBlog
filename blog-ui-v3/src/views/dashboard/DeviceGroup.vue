<template>
  <div class="device">


    <div  v-for="(item,index) in clients" class="card-panel-col">
      <div :class="cardClass(index)" >
        <div class="card-panel-description">
          <div class="card-panel-text">
            {{item.name}}
          </div>
          <!--  <count-to :end-val="params.num"  class="card-panel-num" />-->
          <span>ip:{{item.clientIp}}</span><br/>
          <span style="font-size: 14px">{{ping[index]?"已连接":"连接失败"}}</span>
        </div>
      </div>
    </div>

  </div>
</template>

<script>
import CountTo from 'vue-count-to'

export default {
  components: {
    CountTo
  },
  data() {
    return {
      params: {
        userCount: 0,
        appraisalCount: 0,
        num: 0
      },
      clients:[],
      ping:[],
      strURL:'',
      bolIsRunning:false,
      intSent: 0,
      intTimerID:null,
      bolIsTimeout:false,
      objIMG:{},
    }
  },
  created() {
    // this.getDicts("client_ips").then(response => {
    //   this.clients = response.data;
    // });



    this.getClientList();
    this.getClient();
  },
  methods: {
    cardClass(state){
      switch (state%5){
        case 0:
          return "details-box first-box";
        case 1:
          return "details-box second-box";
        case 2:
          return "details-box third-box";
        case 3:
          return "details-box fourth-box";
        case 4:
          return "details-box fifth-box";

      }

    }

  }
}
</script>



<style lang="scss" scoped>
.device {
  margin-top: 18px;
  margin-left: 15px;
  .card-panel-col {
    margin-bottom: 20px;
    margin-right: 20px;
    display: inline-block;
    width: calc(20% - 20px);
  }

  .details-box:before {
    content: "";
    position: absolute;
    right: -40px;
    top: 10px;
    width: 90px;
    height: 90px;
    border-radius: 90px;
    background-color: rgba(245,245,245,0.3);
  }
  .details-box:after {
    content: "";
    position: absolute;
    right: -20px;
    bottom: -10px;
    width: 100px;
    height: 100px;
    border-radius: 100px;
    background-color: rgba(245, 245, 245, 0.3);
  }
  .first-box{
    background: linear-gradient(45deg,rgba(79,81,127,1) 0,rgba(50,53,132,1) 44%,rgba(50,53,132,1) 100%);
  }
  .second-box{
    background: linear-gradient(45deg,rgba(192,133,248,1) 0,rgba(192,133,248,1) 44%,rgba(160,89,242,1) 100%)
  }
  .third-box{
    background: linear-gradient(45deg,rgba(65,133,248,1) 0,rgba(88,133,248,1) 44%,rgba(92,89,222,1) 100%)
  }
  .fourth-box{
    background: linear-gradient(45deg,rgba(253,181,131,1) 0,rgba(253,181,131,1) 26%,rgba(252,141,148,1) 100%)
  }
  .fifth-box{
    background: #fdb583;
  }

  .details-box {
    height: 150px;
    cursor: pointer;
    font-size: 15px;
    position: relative;
    overflow: hidden;
    color: white;

    border-radius: 10px;
   // background: #fff;
    box-shadow: 4px 4px 40px rgba(0, 0, 0, .05);
    border-color: rgba(0, 0, 0, .05);

    &:hover {
      .card-panel-icon-wrapper {
        color: #fff;
      }

      .icon-people {
        background: #40c9c6;
      }

      .icon-message {
        background: #36a3f7;
      }

      .icon-money {
        background: #f4516c;
      }

      .icon-shopping {
        background: #34bfa3
      }
    }

    .icon-people {
      color: #40c9c6;
    }

    .icon-message {
      color: #36a3f7;
    }

    .icon-money {
      color: #f4516c;
    }

    .icon-shopping {
      color: #34bfa3
    }

    .card-panel-icon-wrapper {
      float: left;
      margin: 14px 0 0 14px;
      padding: 16px;
      transition: all 0.38s ease-out;
      border-radius: 6px;
    }

    .card-panel-icon {
      float: left;
      font-size: 48px;
    }


    .card-panel-description {

      font-weight: bold;
      margin: 40px 0px 30px 30px;

      .card-panel-text {
        line-height: 18px;
        color: white;
        font-size: 18px;
        margin-bottom: 12px;
        font-weight: bolder;
      }

      .card-panel-num {
        font-size: 25px;
      }
    }
  }
}

@media (max-width:550px) {
  .card-panel-description {
    display: none;
  }

  .card-panel-icon-wrapper {
    float: none !important;
    width: 100%;
    height: 100%;
    margin: 0 !important;

    .svg-icon {
      display: block;
      margin: 18px auto !important;
      float: none !important;
    }
  }
}

</style>
