/**
*
*  @author ZTiger
*
*/


<template>
  <div class="drop-warp">
    <div class="drop-warp-s" ref="dropWarpS">
      <slot></slot>
    </div>
    <transition name="boxShow">
      <div class="drop-box drop-box-c" ref="dropBox" @click.stop v-show="showViewFlag">
        <div class="drop-box-header">
          <div class="box-header-img">
            <img :src="images" class="box-header-image" v-if="images" />
            <div class="box-header-nimg" v-else>{{nickName.charAt(0).toUpperCase()}}</div>
            <div class="box-header-img-icon img-icon-default disF">
              <div class="box-header-img-i disF curP" @click="ClickStatus(4)">
                <Icon type="ios-camera" size="18" />
              </div>
            </div>
          </div>
          <div class="box-header-nickname">{{nickName}}</div>
          <div class="box-header-username">{{username}}</div>
          <router-link to="/user" class="box-header-m curP box-button">管理您的 Knowledge Graph 账号</router-link>
        </div>
        <Divider />
        <div class="drop-box-main">
          <div
            class="drop-box-main-button curP box-button"
            @click="ClickStatus(3)"
            :class="{'drop-box-main-button-loading':logoutLoad}"
          >
            <Icon type="md-refresh" class="drop-box-main-button-icon" v-show="logoutLoad" />退出
          </div>
        </div>
      </div>
    </transition>
    <upload-images ref="uploadImages"></upload-images>
  </div>
</template>


<script>
import { mapGetters, mapMutations } from 'vuex';
// 点击上传照片
import uploadImages from './uploadImages';

export default {
  components: { uploadImages },
  data() {
    return {
      // 管理页面展示标志位
      showViewFlag: false,
      // 字母表
      wordList: 'abcdefghigklmnopqrstuvwxyz1234567890',
      // 随机
      randomPass: '',
      // 退出标志位
      logoutLoad: false,
    };
  },
  computed: {
    ...mapGetters({
      username: 'getuserName',
      images: 'getImageSrc',
      nickName: 'getnickName',
    }),
  },
  created() {
    for (let i = 0; i < 8; i++) {
      this.randomPass += this.wordList[Math.floor(Math.random() * 36)];
    }
  },
  mounted() {
    this.$refs.dropWarpS.dataset.k = this.randomPass;
    window.addEventListener('click', this.showUserView);
  },
  beforeDestroy() {
    window.removeEventListener('click', this.showUserView);
  },
  methods: {
    ...mapMutations(['delToken']),
    // 点击事件
    ClickStatus(type) {
      const statusMap = {
        // 展示 隐藏
        1: () => {
          // if(!this.showViewFlag)
          // event.stopPropagation();
          this.showViewFlag = !this.showViewFlag;
        },
        // 点击跳转 user页面
        2: () => {
          this.$router.push({ path: '/user' });
        },
        // 点击 退出
        3: () => {
          if (this.logoutLoad) {
            return;
          }
          const url = '/user/logout';
          this.logoutLoad = true;
          this.post_json(url)
            .then(() => {
              this.delToken();
              this.$router.push({ path: '/login' });
            })
            .catch(() => {
              this.logoutLoad = false;
            });
        },
        // 点击 展开上传文件
        4: () => {
          this.showViewFlag = false;
          this.$refs.uploadImages.clickStatus(3);
        },
      };
      statusMap[type]();
    },
    // 注册全局点击事件
    showUserView() {
      const target = event.target;
      const Ptarget = target.parentNode;
      if (
        Ptarget.dataset &&
        (Ptarget.dataset.k === this.randomPass ||
          target.dataset.k === this.randomPass)
      ) {
        this.showViewFlag = !this.showViewFlag;
      } else {
        this.showViewFlag = false;
      }
    },
  },
};
</script>


<style scoped>
.drop-warp {
  position: relative;
}
.drop-warp-s {
  font-size: 0;
  cursor: pointer;
}
/* .drop-warp-border {
  border: 3px solid rgba(0, 0, 0, 0.4);
} */
.drop-box {
  position: absolute;
  left: -280px;
  top: 35px;
  width: 350px;
  z-index: 800;
}
.drop-box-c {
  background: #fff;
  border: 1px solid #ccc;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.2);
}
.drop-box-header {
  padding: 30px 30px 10px;
  text-align: center;
  font-size: 15px;
}
.box-header-img {
  position: relative;
  display: inline-block;
  font-size: 0;
}
.box-header-image,
.box-header-nimg {
  width: 80px;
  height: 80px;
  border-radius: 50%;
}
.box-header-nimg {
  line-height: 80px;
  font-size: 22px;
  font-family: Georgia;
  color: #dff6f0;
  background: #4d80e4;
}
.box-header-img-icon {
  position: absolute;
  bottom: 0;
  right: 0;
}
.box-header-img-i {
  box-shadow: 0 1px 1px 0 rgba(65, 69, 73, 0.3),
    0 1px 3px 1px rgba(65, 69, 73, 0.15);
  border-radius: 50%;
  height: 24px;
  width: 24px;
}
.box-header-img-i:hover {
  background: #f8faff;
  color: #1a73e8;
}
.img-icon-default {
  height: 32px;
  width: 32px;
  border-radius: 50%;
  background: #fff;
}
.disF {
  display: flex;
  justify-content: center;
  align-items: center;
}
.box-header-nickname {
  margin: 10px 0 5px 0;
  font-size: 16px;
  font-family: Georgia;
  color: #202124;
  letter-spacing: 0.1em;
}
.box-header-username {
  margin: 0px 0 5px 0;
  color: #5f6368;
}
.box-header-m {
  display: inline-block;
  margin: 15px 0 0px 0;
  padding: 5px 20px;
  border: 1px solid #dadce0;
  border-radius: 100px;
  color: #3c4043;
}
.box-button:hover {
  background: #f7f8f8;
}
.drop-box-main {
  padding: 10px 30px 30px;
  text-align: center;
}
.drop-box-main-button {
  display: inline-block;
  border: 1px solid #dadce0;
  border-radius: 4px;
  padding: 8px 25px;
}
.curP {
  cursor: pointer;
}
.boxShow-enter-active,
.boxShow-leave-active {
  transition: all 0.5s;
}
.boxShow-enter,
.boxShow-leave-to {
  opacity: 0;
}
.drop-box-main-button-icon {
  animation: ani-demo-load 2.5s linear infinite;
  margin: 0 5px 0 0;
  font-size: 16px;
}
.drop-box-main-button.drop-box-main-button-loading {
  background: rgba(0, 0, 0, 0.3);
  color: #fff;
  cursor: default;
}
</style>