/**
*
*  @author ZTiger
*
*/


<template>
  <div class="upload-box upload-box-c" v-if="uploadShowFlag" @click.stop>
    <div class="upload-box-header dis-fix">
      <div class="upload-box-header-title">选择个人资料照片</div>
      <Tooltip content="关闭">
        <Icon type="md-close" size="16" class="cur-poin" @click.stop="clickStatus(2)" />
      </Tooltip>
    </div>
    <div class="upload-box-main" v-show="showImgFlag === 1">
      <div class="box-main-header">
        <div class="box-main-header-button cur-poin">上传照片</div>
      </div>
      <div class="box-main-main">
        <div class="flex-center cur-poin" v-show="uploadStatus === 1" @click.stop="clickStatus(1)">
          <Icon type="md-images" size="70" />
          <div class="box-main-main-title">选择您计算机的照片</div>
          <input type="file" class="box-main-main-button" ref="MMButton" @change="clickStatus(4)" />
        </div>
        <div class="flex-center main-precent" v-show="uploadStatus === 2">
          <Progress
            :percent="precent"
            :stroke-width="20"
            status="active"
            text-inside
            :stroke-color="['#108ee9', '#87d068']"
          />
        </div>
      </div>
    </div>
    <div class="upload-box-main" v-show="showImgFlag === 2">
      <div class="box-mainImg-header">
        <div class="button cur-poin" @click.stop="clickStatus(5)">上传照片</div>
        <Icon type="md-arrow-dropright" class="mainImg-icon" />
        <div>{{ImagesName}}</div>
      </div>
      <div class="flex-center">
        <img :src="srcImage" class="main-images" />
      </div>
    </div>
    <div class="box-floor">
      <Button type="primary" :disabled="disabledFlag" @click.stop="clickStatus(6)">设置为个人资料照片</Button>
      <Button type="text" @click.stop="clickStatus(2)">取消</Button>
    </div>
  </div>
</template>


<script>
import { mapMutations } from 'vuex';

export default {
  data() {
    return {
      // 上传层 展示 标志位
      uploadShowFlag: false,
      // 上传 进度条
      precent: 0,
      // 上传 状态
      uploadStatus: 1,
      // 显示 照片 标志位
      showImgFlag: 1,
      // 图片 连接地址
      srcImage: '',
      // 设置按钮可点击 标志位
      disabledFlag: true,
      // 照片名称
      ImagesName: '',
    };
  },
  mounted() {
    window.addEventListener('click', this.showModalView);
  },
  beforeDestroy() {
    window.removeEventListener('click', this.showModalView);
  },
  methods: {
    ...mapMutations({ modifyImg: 'modify' }),
    // 状态选择函数
    clickStatus(type) {
      const statusMap = {
        // 点击打开文件夹
        1: () => {
          this.$refs.MMButton.click();
        },
        // 关闭 上传视图
        2: () => {
          this.uploadShowFlag = false;
        },
        // 打开 展示 标志位
        3: () => {
          this.uploadShowFlag = true;
          this.precent = 0;
          this.uploadStatus = 1;
          this.showImgFlag = 1;
          this.disabledFlag = true;
        },
        // 上传 照片
        4: () => {
          const file = this.$refs.MMButton.files[0];
          const reg = /^image/;
          if (!file || !reg.test(file.type)) {
            return;
          }
          this.ImagesName = file.name;
          this.uploadStatus = 2;
          let data = new FormData();
          let url = 'storage';
          data.append('file', file);
          this.post_progress(url, data, (res) => {
            let loaded = res.loaded;
            let total = res.total;
            this.precent = (loaded / total) * 100;
          })
            .then((res) => {
              this.showImgFlag = 2;
              this.srcImage = res.link;
              this.disabledFlag = false;
            })
            .catch(() => {});
        },
        // 展示上传照片展示位
        5: () => {
          this.precent = 0;
          this.uploadStatus = 1;
          this.showImgFlag = 1;
          this.disabledFlag = true;
        },
        // 设置上传个人照片
        6: () => {
          const url = 'user/me';
          const obj = {
            image: this.srcImage,
          };
          this.patch_json(url, obj)
            .then((res) => {
              const data = res.data;
              this.modifyImg(data);
              this.$Message.success('修改成功');
              this.uploadShowFlag = false;
            })
            .catch(() => {});
        },
      };
      statusMap[type]();
    },
    // 注册全局点击事件
    showModalView() {
      if (this.uploadShowFlag) {
        this.uploadShowFlag = false;
      }
    },
  },
};
</script>


<style scoped>
.upload-box {
  position: fixed;
  left: 50%;
  top: 50%;
  transform: translate(-50%, -50%);
  width: 70vw;
  height: calc(70vw * 0.618);
  min-height: 350px;
  min-width: 350px;
  transition: all 1s;
}
.upload-box-c {
  border: 1px solid #acacac;
  background: #fff;
  box-shadow: rgba(0, 0, 0, 0.2) 0 4px 16px;
  user-select: none;
  z-index: 900;
}
.upload-box-header {
  padding: 10px 20px;
}
.upload-box-header-title {
  color: #000;
  font-size: 20px;
}
.cur-poin {
  cursor: pointer;
}

.upload-box-main {
  overflow: hidden;
  height: calc(100% - 110px);
}
.box-main-header {
  padding: 0 10px;
  box-shadow: 0 3px 5px 1px #e1e1e1;
  border-bottom: 1px solid #e1e1e1;
  box-sizing: border-box;
}
.box-main-header-button {
  display: inline-block;
  padding: 13px;
  border-bottom: 2px solid #4d90fe;
  font-weight: bold;
  color: #262626;
}
.box-main-main {
  height: calc(100% - 50px);
}
.flex-center {
  height: 100%;
  color: #aaa;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}
.main-precent {
  padding: 0 30px;
}
.main-images {
  width: 30%;
}
.box-main-main-title {
  margin: 10px 0 0 0;
  padding: 4px 10px;
  font-size: 14px;
  background-color: #f5f5f5;
  border: 1px solid rgba(0, 0, 0, 0.1);
  color: #444;
}
.box-floor {
  padding: 9px 30px;
}
.box-floor button {
  margin: 0 20px 0 0;
}
.box-main-main-button {
  display: none;
}
.box-mainImg-header {
  padding: 0 20px;
  height: 50px;
  display: flex;
  align-items: center;
}
.button {
  /* padding: 0 10px; */
  width: 70px;
  box-sizing: border-box;
  text-align: center;
}
.button:active {
  border: 2px solid rgba(45, 183, 245, 0.5);
  border-radius: 4px;
}
.mainImg-icon {
  margin: 0 10px;
}
</style>