/**
*
*  @author ZTiger
*
*/


<template>
  <Modal
    class="b-modal-wrap"
    v-model="modalFlag"
    width="592"
    :styles="{top: '50px'}"
    :footer-hide="true"
    :mask-closable="false"
  >
    <div class="modal-title">
      <span>编辑项目封面</span>
      <Icon class="g-close" type="md-close" @click="modalFlag=false" />
    </div>
    <div class="modal-body">
      <crop-image :imgSrc="imgSrc" :cropHeight="150" :cropWidth="300" @on-callback="getCanvasPos" />
    </div>
    <div class="g-text-row">
      <div class="g-select-img cup">
        <Icon type="ios-image-outline" class="g-select-icon" />
        <span>选择图片</span>
      </div>
      <div class="g-tip">（最佳图片比例2:1, 可以上传高质量图片进行裁剪）</div>
    </div>
    <div class="g-preview">
      <div class="g-preview-text">图片预览</div>
      <div class="g-preview-canvas">
        <div>
          <canvas ref="canvasFirst" width="300" height="150" />
          <div class="g-tip">300×150</div>
        </div>
        <div class="g-canvas-mini">
          <canvas ref="canvasSecond" width="50" height="50" />
          <div class="g-tip">50×50</div>
        </div>
      </div>
    </div>
    <div class="g-footer">
      <Button type="text" @click="modalFlag=false">取 消</Button>
      <Button type="primary" @click="saveEvent">保 存</Button>
    </div>
  </Modal>
</template>


<script>
import { uploadFile } from '@/api/storage';
import cropImage from '@/components/cropImage.vue';

export default {
  components: { cropImage },
  model: {
    prop: 'parentFlag',
    event: 'parent-event',
  },
  props: ['parentFlag', 'imgSrc'],
  data() {
    return {
      // 对话框标志位
      modalFlag: false,
      // 照片
      imageClass: new Image(),
    };
  },
  watch: {
    modalFlag(val) {
      if (!val) {
        this.$emit('parent-event', val);
      }
    },
    parentFlag(val) {
      if (val) {
        this.modalFlag = val;
      }
    },
  },
  mounted() {
    this.imageClass.src = this.imgSrc;
  },
  methods: {
    getCanvasPos(imgSrc, sx, sy, sw, sh) {
      const ctxFirst = this.$refs.canvasFirst.getContext('2d');
      const ctxSecond = this.$refs.canvasSecond.getContext('2d');
      ctxFirst.drawImage(imgSrc, sx, sy, sw, sh, 0, 0, 300, 150);
      ctxSecond.drawImage(imgSrc, sx, sy, sw, sh, 0, 0, 50, 50);
    },
    async saveEvent() {
      this.modalFlag = false;
      const base64String = this.$refs.canvasFirst.toDataURL('image/jpeg');
      let bytes = window.atob(base64String.split(',')[1]);
      let ab = new ArrayBuffer(bytes.length);
      let ia = new Uint8Array(ab);
      for (let i = 0; i < bytes.length; i++) {
        ia[i] = bytes.charCodeAt(i);
      }
      let blob = new Blob([ab], { type: 'image/jpeg' });
      let fd = new FormData();
      fd.append('file', blob, Date.now() + '.jpg');
      const link = await uploadFile(fd);
      this.$emit('on-upload', link);
      this.$Notice.success({ title: '项目封面选取成功' });
    },
  },
};
</script>


<style scoped>
.modal-title {
  margin: 0 0 20px 0;
  color: #262626;
  font-size: 16px;
  font-weight: bold;
}
.g-close {
  float: right;
  font-size: 18px;
  font-weight: bold;
  line-height: 24px;
  color: #808695;
  cursor: pointer;
}
.g-close:hover {
  color: #5cadff;
}
.modal-body {
  height: 280px;
  width: 560px;
  background: #000;
}
.g-text-row {
  display: flex;
  align-items: center;
  padding: 15px 0;
  border-bottom: 1px solid #e8eaec;
}
.g-select-img {
  position: relative;
  color: #1b9aee;
}
.g-select-icon {
  margin: 0 4px 0 0;
  font-size: 16px;
  color: #8c8c8c;
}
.g-tip {
  color: #8c8c8c;
}
.g-preview {
  color: #000;
  padding: 15px 0;
  user-select: none;
}
.g-preview-text {
  font-size: 18px;
  margin: 0 0 15px 0;
}
canvas {
  border-radius: 3px;
  box-shadow: 0 0 2px silver;
  background: #f0f0f0;
}
.g-preview-canvas {
  display: flex;
}
.g-canvas-mini {
  margin: 0 0 0 80px;
}
.g-footer {
  text-align: right;
}
.g-footer button:first-of-type {
  margin: 0 10px 0 0;
}
@media screen and (max-width: 560px) {
  .modal-body {
    width: 100%;
  }
}
</style>