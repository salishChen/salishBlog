import {ref, onMounted, onUnmounted} from "vue";

export function infiniteScroll() {

    const isTail = ref(false)

    //获取当前可视范围的高度
    function getClientHeight() {
        var clientHeight = 0;
        if (document.body.clientHeight && document.documentElement.clientHeight) {
            clientHeight = Math.min(document.body.clientHeight, document.documentElement.clientHeight)
        } else {
            clientHeight = Math.max(document.body.clientHeight, document.documentElement.clientHeight)
        }
        return clientHeight
    }

//获取文档完整的高度
    function getScrollHeight() {
        return Math.max(document.body.scrollHeight, document.documentElement.scrollHeight)
    }

//获取当前滚动条的位置
    function getScrollTop() {
        return window.pageYOffset || document.documentElement.scrollTop || document.body.scrollTop;
    }

    //回调函数
    function windowScroll() {
        //获取三个值
        const scrollTop = getScrollTop();
        const clientHeight = getClientHeight();
        const scrollHeight = getScrollHeight();
        //如果满足公式则，确实到底了
        if (scrollTop + clientHeight >= scrollHeight - 280) {
            //发送异步请求请求数据，同时携带offset并自增offset
            //noMore是自定义变量，如果是最后一批数据则以后都不加载
            isTail.value = true;
        } else {
            isTail.value = false;
        }
    }

    onMounted(() => window.addEventListener('scroll', windowScroll, true))//监听页面滚动
    onUnmounted(() => window.removeEventListener('scroll', windowScroll)) //销毁滚动事件

    return {isTail}

}
