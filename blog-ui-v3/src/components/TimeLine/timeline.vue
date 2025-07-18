<template>
  <canvas
    @touchmove="touchmove"
    @touchend="touchend"
    @touchstart="touchstart"
    onselectstart="return false;"
    :style="`background-color: ${this.colors.background}; cursor: ${isMobile ? 'default' : 'pointer'}`"
    :height="height"
    ref="canvas"
  ></canvas>
</template>

<script>
import moment from "moment";

/**
 *  timeline-canvas  canvas的绘制的时间轴组件
 * @description 可用于视频、录像播放或实时数据展示等业务
 * @tutorial https://gitee.com/my87/timeline-canvas
 * @property {Number String} width 时间轴宽度，支持固定(不需要带单位)和百分比，默认自适应父容 （默认100%）
 * @property {String Number} height 时间轴高度，非必要可以忽 （默认60）
 * @property {String} startMeddleTime启动时间，未传参时会根据 timeRange 计算；timeRange 参数也未传采用当前时间
 * @property {Array String } timeRange 时间轴绘制的时间范 （默认当天）
 * @property {Array} markTime  区域进行标记，可以自定义背景颜色和文
 * @property {Boolean} isAutoPlay 开启后时间轴将以 1s 的速度前进进（默认false）
 * @property {Object} colors	自定义颜色
 * @Methods {Function} play(date) 当未开启自动播放，时可以手动播放	date：播放的起始时间
 * @Methods {Function} stop 手动暂停播放
 * @event {Function} click 当时间和播放状态发生变化时触发，只有当开启 isAutoPlay=true 才有状态变化
 * @event {Function} change 图片上传成功时触发
 * @example <TimeLineCanvas ref="time_line" @click="clickCanvas" @change="changeDate" :mark-time="markTime" :time-range="time_range" :isAutoPlay="isAutoPlay" :startMeddleTime="startMeddleTime"/>
 */
export default {
  props: {
    width: {
      type: [Number, String],
      default: "100%",
    },
    // 
    height: {
      type:[Number, String],
      default: 60,
    },
    // 中间的时间,
    startMeddleTime: String,
    // 时间范围
    timeRange: {
      type: [Array, String],
      default() {
        return "";
      },
    },
    // 需要标记的时间
    markTime: {
      type: Array,
      default() {
        return [];
      },
    },

    //是否自动播放
    isAutoPlay: {
      type: Boolean,
      default: false,
    },
    colors: {
      type: Object,
      default() {
        return {
          //背景
          background: "#2b2f33",
          //中间线
          meddleLine: "#33CC33",
          //中间时间
          meddleDate: "#33CC33", // "rgb(64, 196, 255)",
          //移动线
          moveLine: "#808080",
          //移动时间
          moveDate: "#009966",
          //刻度线
          scaleLine: "#808080",
          //刻度条
          scaleBar: "#45484c",
        };
      },
    },
    //最小像素秒(每1px对应的秒数，保证时间轴的宽度缩小情况下时间刻度不会压缩在一起)
    minPxSecond: {
      type: Number,
      default: 65,
    },
  },
  data() {
    return {
      //像素比
      dpr: 1,
      // realTimeRange: [],
      // 整个canvas显示多少个小时
      whole_hour: 24,
      // canvas的画布宽度
      canvasWidth: 1000,
      //中间时间
      meddleTime: "",
      // 移动时鼠标有没有按下
      mouseDown: false,
      // 鼠标按下时的位置
      mouseDownPosition: "",
      // 鼠标按下时中间的时间,
      mouseDownMeddleTime: "",
      // 鼠标按下时有没有移动
      isMove: false,
      //是否手机端
      isMobile: false,
      //两端之间距离
      distance: 0,
      //是否在播放中
      isPlay: false,
    };
  },
  mounted() {
   this.$nextTick(() => {
     this.canvas = this.$refs.canvas;
    //是否为手机端
    this.isMobile = /Mobi/i.test(navigator.userAgent); //navigator.userAgent.match(/Mobi/i);
    //移动端如不禁止，在滑动时会触发鼠标事件与滑动事件冲突
    if (!this.isMobile) {
      this.canvas.addEventListener("mousewheel", this.mousewheel);
      this.canvas.addEventListener("mousemove", this.mousemove);
      this.canvas.addEventListener("mousedown", this.mousedown);
      this.canvas.addEventListener("mouseup", this.mouseup);
      this.canvas.addEventListener("mouseleave", this.mouseleave);
    }
    // 屏幕大小监听
    window.addEventListener("onorientationchange" in window ? "orientationchange" : "resize", this.resize, false);
    // 计算默认中间时间
    this.setStartMeddleTime();
    this.ctx = this.canvas.getContext("2d");
    //初始化
    this.init();
    // 自动播放
    if (this.isAutoPlay) {
      this.play();
    }
   })
  },
  methods: {
    init() {
      this.canvas = this.$refs.canvas;

      let width = this.width;
      //自适应父容器宽度 this.width参数支持百分比设置
      let parentWidth = this.canvas.parentElement.clientWidth;
      if (/^(\d|[1-9]\d|100)%$/.test(this.width)) {
        width = Math.floor((this.width.replace("%", "") / 100) * parentWidth);
      }

      //移动端像素模糊问题处理
      //是由于dpr像素比造成的，扩大canvas画布的像素，使1个canvas像素和1个物理像素相等
      this.dpr = window.devicePixelRatio; // 假设dpr为2
      // //获取css的宽高
      // const { width: cssWidth, height: cssHeight } = this.canvas.getBoundingClientRect();
      // // 设置图像大小
      this.canvas.style.width = `${width}px`;
      this.canvas.style.height = `${this.height}px`;
      // 设置画布大小
      this.canvas.width = Math.round(width * this.dpr);
      this.canvas.height = Math.round(this.height * this.dpr);
      // 由于画布扩大，canvas的坐标系也跟着扩大，
      // 按照原先的坐标系绘图内容会缩小, 所以需要将绘制比例放大
      this.ctx.scale(this.dpr, this.dpr);
      this.canvasWidth = this.canvas.width / this.dpr;
      this.drow();
    },
    // 监听窗口大小
    resize() {
      if (window.orientation === 180 || window.orientation === 0) {
        // "竖屏";
      }
      if (window.orientation === 90 || window.orientation === -90) {
        //"横屏";
      }
      //有时屏幕尺寸变化了，而容器的尺寸还未改变的情况下的处理
      if (this.canvas.style.width === this.canvas.parentElement.clientWidth + "px") {
        setTimeout(() => {
          this.resize();
        }, 10);
      } else {
        //重新初始化
        this.init();
      }
    },
    //进度条停止播放
    stop() {
      if (this.isPlay) {
        this.isPlay = false;
        this.$emit("change", this.meddleTime, "stop");
      }
      if (this.interval_play) clearInterval(this.interval_play);
    },
    /**
     * 进度条播放(每次走一秒)
     * @param {String|Date} date 启动时间,不传启动时间为this.meddleTime
     */
    play(date) {
      this.isPlay = true;
      clearInterval(this.interval_play);
      if (date) {
        this.meddleTime = date;
      }
      this.interval_play = setInterval(() => {
        //中间时间增加1s
        this.meddleTime = moment(this.meddleTime).add(1, "s").format("YYYY-MM-DD HH:mm:ss");
        let status =
          this.realTimeRange[1] && new Date(this.meddleTime).getTime() >= new Date(this.realTimeRange[1]).getTime()
            ? "end"
            : "play";
        this.$emit("change", this.meddleTime, status);
        // 开发过程中热更新时，会在每更新一次就开启一个setInterval，前面又不释放
        // 没有释放定时任务会报错，所有在异常就视为前个任务并清理掉
        try {
          this.drow();
        } catch (ee) {
          console.log(ee);
          clearInterval(this.interval_play);
        }
        if (!this.isPlay || status == "end") {
          clearInterval(this.interval_play);
        }
      }, 1000);
    },

    //移动端滑动
    touchmove(e) {
      let touches = e.touches;
      e.offsetX = touches[0].pageX;
      e.offsetY = touches[0].pageY;
      //双指缩放 (在本组件上因区域的限制不适合用双指缩放手势)
      if (touches.length >= 2) {
        e.preventDefault();
        let now = Date.now();
        if (!this._moveTime) {
          this._moveTime = now;
        }
        //抖动处理
        else if (now - this._moveTime > 100) {
          let _hypot = this.getDistance({ x: e.offsetX, y: e.offsetY }, { x: touches[1].pageX, y: touches[1].pageY });
          if (_hypot > this.distance) {
            //放大
            e.wheelDelta = 1;
          } else {
            //缩小
            e.wheelDelta = -1;
          }
          this.distance = _hypot;
          this._moveTime = null;
          this.mousewheel(e);
        }
      } else {
        this.mousemove(e);
      }
    },
    //滑动结束
    touchend(e) {
      let touches = e.changedTouches;
      e.offsetX = touches[0].pageX;
      e.offsetY = touches[0].pageY; //pc 与m的值是否相同
      this.mouseup(e);
    },
    //滑动开始
    touchstart(e) {
      let touches = e.touches;
      e.offsetX = touches[0].pageX;
      e.offsetY = touches[0].pageY;
      //双指事件(在本组件上因区域的限制不适合用双指缩放手势)
      if (touches.length >= 2) {
        e.preventDefault();
        this.distance = this.getDistance({ x: e.offsetX, y: e.offsetY }, { x: touches[1].pageX, y: touches[1].pageX });
      }
      this.mousedown(e);
    },
    //鼠标离开
    mouseleave(e) {
      this.drow();
      //鼠标离开无法在触发mouseup，所以当拖动时将离开视释放
      if (this.mouseDown) {
        this.mouseup(e);
      } else {
        this.mouseDown = false;
      }
    },
    // 鼠标移动
    mousemove(e) {
      this.drow();
      //PC滑动显示时间
      if (!this.isMobile) {
        this.drowMoveLine(e);
      }

      if (this.mouseDown) {
        this.mouseDownMove(e);
        this.isMove = true;
      }
    }, //126 10：00
    // 滚动鼠标滚轮
    mousewheel(e) {
      e.preventDefault();
      if (e.wheelDelta > 0) {
        // 时间变短
        this.whole_hour -= 4;
        this.whole_hour < 1 && (this.whole_hour = 1);
      } else {
        // 时间变长
        if (this.whole_hour < 4) {
          this.whole_hour = 4;
        } else {
          this.whole_hour += 4;
          this.whole_hour > 24 && (this.whole_hour = 24);
        }
      }
      this.drow();
    },
    // 按下鼠标
    mousedown(e) {
      this.mouseDown = true;
      this.mouseDownPosition = e.offsetX;
      this.mouseDownMeddleTime = this.meddleTime;
      this.isMove = false;
    },
    // 抬起鼠标
    mouseup(e) {
      this.mouseDown = false;
      // 没有滑动或鼠标移动(视为点击操作)，就渲染中间时间（另外的是在移动事件里渲染）。
      if (!this.isMove) {
        let date = e.offsetX * this.px_second * 1000 + this.firstTime;
        date = this.boundary_time(date);
        let _date = moment(date).format("YYYY-MM-DD HH:mm:ss");
        this.meddleTime = _date;
        this.drow();
        //PC滑动显示时间
        if (!this.isMobile) {
          this.drowMoveLine(e);
                  this.$emit("click", this.meddleTime);
        }

      }
      // 释放时确定时间选择
      // change事件会在自动播放返回播放中的实时时间
      this.$emit("change", this.meddleTime, "start");
      //click事件只有在释放时才返回时间
      // this.$emit("click", this.meddleTime);

      // 自动播放
      if (this.isAutoPlay) {
        this.play();
      }
    },
    // 鼠标按下移动(组合事件)
    mouseDownMove(e) {
      this.stop();
      // 记录点击位置与滑动后的坐标距离
      let offset = this.mouseDownPosition - e.offsetX;
      // 点击时的中间时间 + 坐标距离差转换后的时间 = 移动后的中间时间
      let date = new Date(this.mouseDownMeddleTime).getTime() + offset * this.px_second * 1000;
      date = this.boundary_time(date);
      this.meddleTime = moment(date).format("YYYY-MM-DD HH:mm:ss");
    },
    drow() {
      //重置高宽清空画布
      this.canvas.width = this.canvas.width;
      this.canvas.height = this.canvas.height;
      this.ctx.scale(this.dpr, this.dpr);
      this.drowMark();
      this.drowScaleLine();
      // this.drowMeddleLine(this.meddleTime);
    },
    // 画鼠标移上去的线
    drowMoveLine(e) {
      let date = e.offsetX * this.px_second * 1000 + this.firstTime;
      // 超出有效时间范围就不做渲染
      if (
        (this.realTimeRange[0] && date < new Date(this.realTimeRange[0]).getTime()) ||
        (this.realTimeRange[1] && date > new Date(this.realTimeRange[1]).getTime())
      ) {
        return;
      }
      this.ctx.beginPath();
      this.ctx.moveTo(e.offsetX - 1, 0);
      this.ctx.lineTo(e.offsetX - 1, 45);
      this.ctx.strokeStyle = this.colors.moveLine;
      this.ctx.lineWidth = 1;
      this.ctx.stroke();
      this.ctx.fillStyle = this.colors.moveDate;

      let text = this.getMark(date)?.text;
      text = text ? ` (${text})` : "";
      this.ctx.font = `${12}px serif`;
      this.ctx.fillText(moment(date).format("YYYY-MM-DD HH:mm:ss") + text, e.offsetX - 50, 55);
    },
    // 画中间时间的线
    drowMeddleLine(time) {
      this.ctx.beginPath();
      this.ctx.moveTo(this.canvasWidth / 2, 0);
      this.ctx.lineTo(this.canvasWidth / 2, 30);
      this.ctx.strokeStyle = this.colors.meddleLine;
      this.ctx.lineWidth = 1;
      this.ctx.stroke();
      this.ctx.fillStyle = this.colors.meddleLine;
      this.ctx.font = `12px serif`;
      this.ctx.fillText(time, this.canvasWidth / 2 - 50, 40);
    },
    // 画刻度线
    drowScaleLine() {
      // // 画canvas上部分的颜色
      // this.ctx.fillStyle = "rgba(69, 72, 76, 0.5)";
      // this.ctx.fillRect(0, 0, this.canvasWidth, 20);

      // 画第一个刻度线
      let time = new Date(this.getFirstLineTime()).getTime() - this.firstTime;
      //几个像素点后画第一个刻度
      let p = time / 1000 / this.px_second;
      // 每条线之间的间隔 scaleLine_minute来确定每个格代表多长时间
      let line_px = (this.scaleLine_minute * 60) / this.px_second;
      for (let i = p; i <= this.canvasWidth; i += line_px) {
        let date = this.firstTime + i * this.px_second * 1000;
        if (
          (this.realTimeRange[0] && date < new Date(this.realTimeRange[0]).getTime()) ||
          (this.realTimeRange[1] && date > new Date(this.realTimeRange[1]).getTime())
        ) {
          continue;
        }

        let time = moment(date).format("HH:mm");
        this.ctx.beginPath();
        this.ctx.moveTo(i, 0);
        this.ctx.lineTo(i, this.showTime(time) ? 20 : 10);
        this.ctx.strokeStyle = this.colors.scaleLine;
        this.ctx.lineWidth = 1;
        this.ctx.stroke();
        this.ctx.fillStyle = this.colors.scaleLine;
        this.ctx.font = `12px serif`;
        if (time == "00:00") {
          let show_time = moment(date).format("YYYY-MM-DD");
          this.ctx.fillText(show_time, i - 28, 30);
        } else if (this.showTime(time)) {
          this.ctx.fillText(time, i - 10, 30);
        }
      }
    },

    // 计算默认中间时间
    setStartMeddleTime() {
      //根据可活动时间范围计算中间值
      let time;
      if (this.realTimeRange[0] && this.realTimeRange[1]) {
        time = moment(
          (new Date(this.realTimeRange[0]).getTime() + new Date(this.realTimeRange[1]).getTime()) / 2
        ).format("YYYY-MM-DD HH:mm:ss");
      }
      //设置默认中间时间（优先级：startMeddleTime参数指定>按有可活动时间范围计算>当前时间）
      this.meddleTime = this.startMeddleTime || time || moment(new Date()).format("YYYY-MM-DD HH:mm:ss");
    },
    //获取刻度上的第一个时间
    getFirstLineTime() {
      const start = moment(this.firstTime);
      //第一个时间不一定刚好落在刻度上，计算少了多少分才能到第一个刻度上（开始位置留白）
      const remainder = this.scaleLine_minute - (start.minute() % this.scaleLine_minute);
      return start.add(remainder, "minutes").format("YYYY-MM-DD HH:mm");
    },
    // 刻度时间显示(按级别显示)
    showTime(time) {
      // 每2时级
      if (this.whole_hour >= 16) {
        return [
          "00:00",
          "02:00",
          "04:00",
          "06:00",
          "08:00",
          "10:00",
          "12:00",
          "14:00",
          "16:00",
          "18:00",
          "20:00",
          "22:00",
        ].includes(time);
      }
      // 每1时级
      if (this.whole_hour >= 8) {
        return [
          "00:00",
          "02:00",
          "03:00",
          "04:00",
          "05:00",
          "06:00",
          "07:00",
          "08:00",
          "09:00",
          "10:00",
          "11:00",
          "12:00",
          "13:00",
          "14:00",
          "15:00",
          "16:00",
          "17:00",
          "18:00",
          "19:00",
          "20:00",
          "21:00",
          "22:00",
        ].includes(time);
      }
      // 每20分
      if (this.whole_hour >= 4) {
        return ["00", "20", "40"].find((item) => time.endsWith(item));
      }
      // 每10分
      return ["00", "10", "20", "30", "40", "50"].find((item) => time.endsWith(item));
    },
    // 画标记的时间
    drowMark() {
      // 画canvas上部分的颜色
      this.ctx.fillStyle = this.colors.scaleBar;
      this.ctx.fillRect(0, 0, this.canvasWidth, 20);

      this.markTime.forEach((item) => {
        //标签所有范围超出时间区域（realTimeRange）就不渲染
        if (
          (this.realTimeRange[0] && new Date(item.endTime).getTime() < new Date(this.realTimeRange[0]).getTime()) ||
          (this.realTimeRange[1] && new Date(item.beginTime).getTime() > new Date(this.realTimeRange[1]).getTime())
        ) {
          return;
        }
        this.ctx.fillStyle = item.bgColor;
        // 标签起点超时间效区域realTimeRange[0]就用realTimeRange[0]做为起点时间
        let beginTime =
          this.realTimeRange[0] && new Date(item.beginTime).getTime() < new Date(this.realTimeRange[0]).getTime()
            ? this.realTimeRange[0]
            : item.beginTime;
        // 标签终点超时间效区域realTimeRange[1]就用realTimeRange[1]做为终点时间
        let endTime =
          this.realTimeRange[1] && new Date(item.endTime).getTime() > new Date(this.realTimeRange[1]).getTime()
            ? this.realTimeRange[1]
            : item.endTime;
        let sx = (new Date(beginTime).getTime() - this.firstTime) / 1000 / this.px_second;
        let ex = (new Date(endTime).getTime() - this.firstTime) / 1000 / this.px_second;
        this.ctx.fillRect(sx, 0, ex - sx, 20);
      });
    },
    //获取点坐标的距离（用于移动端双指放大缩小手势识别）
    getDistance(a, b) {
      const x = a.x - b.x;
      const y = a.y - b.y;
      return Math.hypot(x, y); // Math.sqrt(x * x + y * y);
    },
    //超出有效时间范围，返回边界值
    boundary_time(date) {
      //超出起点有效区域
      if (this.realTimeRange[0] && date < new Date(this.realTimeRange[0]).getTime()) {
        return new Date(this.realTimeRange[0]).getTime();
      }
      // 超出端点有效区域
      else if (this.realTimeRange[1] && date > new Date(this.realTimeRange[1]).getTime()) {
        return new Date(this.realTimeRange[1]).getTime();
      } else {
        return date;
      }
    },
    //获取标记对象
    getMark(date) {
      return this.markTime.find(
        (item) =>
          new Date(item.beginTime).getTime() < new Date(date).getTime() &&
          new Date(date).getTime() < new Date(item.endTime).getTime()
      );
    },
    /**
     *设置时间
     * @param {String} date 中间时间
     * @param @param {String| Array} timeRange 可切换到新时间范围(如下个录像时段)，可选参数
     */
    // setDate(date, timeRange) {
    //   if (timeRange) {
    //     this.setTimeRange(timeRange);
    //   }
    //   this.meddleTime = date;
    //   this.drow();
    // },
    /**
     *设置时间轴可活动的时间区域
     * @param {String| Array} timeRange 时间范围，不传默认组件timeRange参数原始值
     */
    // setTimeRange(timeRange) {
    //   this.realTimeRange = timeRange || this.timeRange;
    //   if (typeof this.realTimeRange == "string") {
    //     let date = this.realTimeRange ? moment(this.realTimeRange).format("YYYY-MM-DD") : moment().format("YYYY-MM-DD");
    //     this.realTimeRange = [date + " 00:00:00", date + " 23:59:59:59"];
    //   }
    // },
  },
  computed: {
    // canvas最左边的时间的时间戳（滑动计算时间）
    firstTime() {
      return new Date(this.meddleTime).getTime() - ((this.px_second * this.canvasWidth) / 2) * 1000;
    },
    // 每个像素点对应多少秒（像素转时间和刻度）
    px_second() {
      let second = (this.whole_hour * 60 * 60) / this.canvasWidth;
      // 保证不管缩小到什么宽度，刻度线都不会挤压在一起
      if (second > this.minPxSecond) second = this.minPxSecond;
      return second;
    },
    // 计算timeRange 兼容string |Array转换
    realTimeRange() {
      if (typeof this.timeRange == "string") {
        let date = this.timeRange ? moment(this.timeRange).format("YYYY-MM-DD") : moment().format("YYYY-MM-DD");
        return [date + " 00:00:00", date + " 23:59:59:59"];
      } else {
        return this.timeRange;
      }
    },
    // 每个刻度线之间的多分钟（画刻度）
    scaleLine_minute() {
      if (this.whole_hour >= 20) {
        //30分钟半小时
        return 30;
      }
      if (this.whole_hour >= 16) {
        return 20;
      }
      if (this.whole_hour >= 12) {
        return 15;
      }
      if (this.whole_hour >= 8) {
        return 10;
      }
      if (this.whole_hour >= 4) {
        return 5;
      }
      return 2;
    },

    //计算需要重新绘制的参数，方便统一监听
    changeProps() {
      let { startMeddleTime, markTime, timeRange } = this;
      this.setStartMeddleTime();
      return { startMeddleTime, markTime, timeRange };
    },
  },
  watch: {
    //监听需要重新绘制的参数
    changeProps: function (newV) {
      this.drow();
    },
  },
};
// import Hammer from "./assets/hammerjs";
</script>