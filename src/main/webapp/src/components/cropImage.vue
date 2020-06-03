/**
*
*  @author ZTiger
*
*/


<template>
  <div ref="cropWrap" class="crop-image-wrap">
    <canvas ref="canvasFirst" class="g-canvas-first" />
    <canvas ref="canvasSecond" class="g-canvas-second" @mousedown.stop="startEvent($event)" />
    <div ref="selectResize" class="g-se-resize" @mousedown.stop="handleStart($event)" />
  </div>
</template>


<script>
import { toDecimal } from '@/utils/method';

export default {
  props: {
    imgSrc: {
      type: String,
    },
    cropHeight: {
      type: Number,
    },
    cropWidth: {
      type: Number,
    },
  },
  data() {
    return {
      // 黑色边宽度
      leftSide: '',
      // 裁剪框开始位置
      startX: '',
      startY: '',
      sourceX: '',
      sourceY: '',
      // 图像
      imageSecond: '',
      // 背景图片宽度
      canvasHeight: '',
      canvasWidth: '',
      // 裁剪大小
      dropSizeWidth: '',
      dropSizeHeight: '',
    };
  },
  watch: {
    imgSrc(val) {
      const canvasFirst = this.$refs.canvasFirst;
      const ctxFirst = canvasFirst.getContext('2d');
      const img = new Image();
      this.clearCanvas(this.$refs.canvasFirst);
      this.clearCanvas(this.$refs.canvasSecond);
      this.dropSizeWidth = this.cropWidth;
      this.dropSizeHeight = this.cropHeight;
      this.imageSecond = new Image();
      img.src = val;
      img.onload = () => {
        const { width, height } = img;
        const { clientWidth, clientHeight } = this.$refs.cropWrap;
        this.canvasHeight = clientHeight;
        this.canvasWidth = Math.floor((clientHeight * width) / height);
        const left = (clientWidth - this.canvasWidth) / 2;
        this.setConvasSize(canvasFirst, this.canvasWidth, this.canvasHeight);
        canvasFirst.style.left = `${left}px`;
        ctxFirst.fillStyle = '#000';
        ctxFirst.fillRect(0, 0, this.canvasHeight, this.canvasHeight);
        ctxFirst.drawImage(img, 0, 0, this.canvasWidth, this.canvasHeight);
        let sx = this.canvasWidth / 2 - this.cropWidth / 2;
        let sy = this.canvasHeight / 2 - this.cropHeight / 2;
        sx = sx > 0 ? sx : 0;
        sy = sy > 0 ? sy : 0;
        this.imageSecond.src = canvasFirst.toDataURL('image/png');
        if (this.dropSizeWidth > this.canvasWidth)
          this.dropSizeWidth = this.canvasWidth - 10;
        if (this.dropSizeHeight > this.canvasHeight)
          this.dropSizeHeight = this.canvasHeight - 10;
        this.imageSecond.onload = () => {
          this.leftSide = left;
          this.setDropConvasSize();
          this.setPosition(this.$refs.canvasSecond, { x: left + sx, y: sy });
          this.setPosition(this.$refs.selectResize, {
            x: left + sx + this.dropSizeWidth - 5,
            y: sy + this.dropSizeHeight - 5,
          });
          this.setConvas(sx, sy);
        };
      };
    },
  },

  methods: {
    // 按下
    startEvent(Event) {
      this.startX = Event.pageX;
      this.startY = Event.pageY;
      const pos = this.getPosition(this.$refs.canvasSecond);
      this.sourceX = pos.x;
      this.sourceY = pos.y;
      document.addEventListener('mousemove', this.moveEvent, false);
      document.addEventListener('mouseup', this.endEvent, false);
    },
    // 移动
    moveEvent(Event) {
      const currentX = Event.pageX;
      const currentY = Event.pageY;
      const distanceX = currentX - this.startX;
      const distanceY = currentY - this.startY;
      let x = toDecimal(this.sourceX + distanceX);
      let y = toDecimal(this.sourceY + distanceY);
      if (x < this.leftSide) x = this.leftSide;
      else if (x > this.leftSide + this.canvasWidth - this.dropSizeWidth)
        x = this.leftSide + this.canvasWidth - this.dropSizeWidth;
      if (y < 0) y = 0;
      else if (y > this.canvasHeight - this.dropSizeHeight)
        y = this.canvasHeight - this.dropSizeHeight;
      this.setPosition(this.$refs.canvasSecond, { x, y });
      this.setPosition(this.$refs.selectResize, {
        x: x + this.dropSizeWidth - 5,
        y: y + this.dropSizeHeight - 5,
      });
      this.setConvas(x - this.leftSide, y);
    },
    // 松开
    endEvent() {
      document.removeEventListener('mousemove', this.moveEvent);
      document.removeEventListener('mouseup', this.endEvent);
    },
    // 按下
    handleStart(Event) {
      this.startX = Event.pageX;
      this.startY = Event.pageY;
      const pos = this.getPosition(this.$refs.selectResize);
      this.sourceX = pos.x;
      this.sourceY = pos.y;
      document.addEventListener('mousemove', this.handleMove, false);
      document.addEventListener('mouseup', this.handleEnd, false);
    },
    // 移动
    handleMove(Event) {
      const currentX = Event.pageX;
      const currentY = Event.pageY;
      const distanceX = currentX - this.startX;
      const distanceY = currentY - this.startY;
      const pos = this.getPosition(this.$refs.canvasSecond);
      let x = toDecimal(this.sourceX + distanceX);
      let y = toDecimal(this.sourceY + distanceY);
      if (x - pos.x <= 0) x = pos.x + 5;
      else if (x > this.leftSide + this.canvasWidth - 5)
        x = this.leftSide + this.canvasWidth - 5;
      if (y - pos.y <= 0) y = pos.y + 5;
      else if (y > this.canvasHeight - 5) y = this.canvasHeight - 5;
      this.dropSizeWidth = x - pos.x + 5;
      this.dropSizeHeight = y - pos.y + 5;
      this.setDropConvasSize();
      this.setConvas(pos.x - this.leftSide, pos.y);
      this.setPosition(this.$refs.selectResize, { x, y });
    },
    // 松开
    handleEnd() {
      document.removeEventListener('mousemove', this.handleMove, false);
      document.removeEventListener('mouseup', this.handleEnd, false);
    },
    // 设置位置
    setPosition(dom, pos) {
      dom.style['left'] = pos.x + 'px';
      dom.style['top'] = pos.y + 'px';
    },
    // 设置 canvas 大小
    setConvasSize(dom, width, height) {
      dom.width = width;
      dom.height = height;
    },
    // 设置裁剪画布大小
    setDropConvasSize() {
      this.setConvasSize(
        this.$refs.canvasSecond,
        this.dropSizeWidth,
        this.dropSizeHeight,
      );
    },
    // 获取位置
    getPosition(dom) {
      const x = this.getStyle(dom, 'left');
      const y = this.getStyle(dom, 'top');
      const pos = {
        x: parseInt(x ? x : 0),
        y: parseInt(y ? y : 0),
      };
      return pos;
    },
    // 获取位置
    getStyle(dom, property) {
      return window.getComputedStyle
        ? window.getComputedStyle(dom, null)[property]
        : dom.currentStyle[property];
    },
    // 设置照片位置
    setConvas(sx, sy) {
      const canvasSecond = this.$refs.canvasSecond;
      const ctxSecond = canvasSecond.getContext('2d');
      const dropSize = [this.dropSizeWidth, this.dropSizeHeight];
      this.$emit('on-callback', this.imageSecond, sx, sy, ...dropSize);
      ctxSecond.drawImage(
        this.imageSecond,
        sx,
        sy,
        ...dropSize,
        0,
        0,
        ...dropSize,
      );
      canvasSecond.style.cursor = 'move';
      canvasSecond.style.outline = '1px solid #dcdee2';
    },
    // 清空画布
    clearCanvas(dom) {
      const ctx = dom.getContext('2d');
      ctx.clearRect(0, 0, dom.width, dom.height);
    },
  },
};
</script>


<style scoped>
.crop-image-wrap {
  position: relative;
  height: 100%;
  width: 100%;
  user-select: none;
  overflow: hidden;
}
.g-canvas-first {
  position: absolute;
  top: 0;
  opacity: 0.5;
}
.g-canvas-second {
  position: absolute;
}
.g-se-resize {
  position: absolute;
  width: 10px;
  height: 10px;
  border: 1px solid #dcdee2;
  background: #00000055;
  cursor: se-resize;
}
</style>