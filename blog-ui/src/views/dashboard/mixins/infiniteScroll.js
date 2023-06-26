
export default {
  methods: {
    //获取当前可视范围的高度
    getClientHeight() {
      var clientHeight = 0;
      if (document.body.clientHeight && document.documentElement.clientHeight) {
        clientHeight = Math.min(document.body.clientHeight, document.documentElement.clientHeight)
      } else {
        clientHeight = Math.max(document.body.clientHeight, document.documentElement.clientHeight)
      }
      return clientHeight
    },

//获取文档完整的高度
    getScrollHeight() {
      return Math.max(document.body.scrollHeight, document.documentElement.scrollHeight)
    },

//获取当前滚动条的位置
    getScrollTop() {
      var scrollTop = 0;
      //window.pageYOffset = document.documentElement.scrollTop
      if (document.documentElement && document.documentElement.scrollTop) {
        scrollTop = document.documentElement.scrollTop
      } else if (document.body) {
        scrollTop = document.body.scrollTop
      }
      return scrollTop
    },
    //回调函数
    windowScroll() {
      //获取三个值
      var scrollTop = this.getScrollTop()
      var clientHeight = this.getClientHeight()
      var scrollHeight = this.getScrollHeight()
      //如果满足公式则，确实到底了
      if (scrollTop + clientHeight === scrollHeight) {
        //发送异步请求请求数据，同时携带offset并自增offset
        //noMore是自定义变量，如果是最后一批数据则以后都不加载
        if (this.total>this.queryParams.pageSize*this.queryParams.pageNum) {
            this.getLasgPage();
            //记得对offset进行自增
            // this.offset += 10
        }
      }
    }
  },
  mounted() {
    window.addEventListener('scroll', this.windowScroll, true) //监听页面滚动
  }
  ,
  destroyed() {
    window.removeEventListener("scroll", this.windowScroll);//销毁滚动事件
  },
//删除滚动监听器，建议使用beforeRouteLeave，因为destroyed()钩子在路由跳转时不会触发
/*  beforeRouteLeave() {
    window.removeEventListener("scroll", this.windowScroll);//销毁滚动事件
  }*/

}
