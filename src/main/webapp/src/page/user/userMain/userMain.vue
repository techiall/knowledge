/**
*
*  @author ZTiger
*
*/


<template>
  <div class="user-main scroll">
    <div class="user-main-header user-bottom">
      <div class="user-main-header-name">个人信息</div>
      <div class="user-main-header-title">您在 “知识图谱构建平台” 中使用的基本信息，例如您的昵称和头像</div>
    </div>
    <div class="user-main-content">
      <user-msg
        class="user-main-content-msg"
        v-show="viewSeletctFlag === 1"
        @userMainCallback="userMainCallback"
      ></user-msg>
      <name-modify
        class="user-main-content-msg"
        v-show="viewSeletctFlag === 2"
        @userMainCallback="userMainCallback"
      ></name-modify>
      <password-modify
        class="user-main-content-msg"
        v-show="viewSeletctFlag === 3"
        @userMainCallback="userMainCallback"
        :viewSeletctFlag="viewSeletctFlag"
      ></password-modify>
      <logout-account
        class="user-main-content-msg"
        v-show="viewSeletctFlag === 1"
        @userMainCallback="userMainCallback"
      ></logout-account>
      <unregister-confirmationu
        class="user-main-content-msg"
        v-if="viewSeletctFlag === 4"
        @userMainCallback="userMainCallback"
      ></unregister-confirmationu>
    </div>
    <upload-images ref="uploadImages"></upload-images>
  </div>
</template>


<script>
// 修改名称
import nameModify from "./userModify/nameModify";
// 信息
import userMsg from "./userModify/userMsg";
// 密码修改
import passwordModify from "./userModify/passwordModify";
// 注销
import logoutAccount from "./userModify/logoutAccount";
// 注销确认
import unregisterConfirmationu from "./userModify/unregisterConfirmationu";
// 点击上传照片
import uploadImages from "../../../components/uploadImages";

export default {
  components: {
    nameModify,
    userMsg,
    passwordModify,
    logoutAccount,
    unregisterConfirmationu,
    uploadImages
  },
  data() {
    return {
      // 视图选择 标志位
      viewSeletctFlag: 1
    };
  },
  methods: {
    // 回调函数
    userMainCallback(type, val) {
      const stateMap = {
        // 视图选择
        1: () => {
          this.viewSeletctFlag = val;
        },
        // 上传 照片
        2: () => {
          this.$refs.uploadImages.clickStatus(3);
        }
      };
      stateMap[type]();
    }
  }
};
</script>


<style scoped>
.user-main {
  padding: 30px;
  overflow: auto;
  min-width: 460px;
}
.user-main-header {
  text-align: center;
  letter-spacing: 0.1em;
}
.user-bottom {
  margin: 0 0 30px 0;
}
.user-main-header-name {
  font-size: 23px;
  color: #202124;
}
.user-main-header-title {
  margin: 10px 0 0px 0;
  font-size: 12px;
  font-weight: 520;
  color: #5f6368;
}
.user-main-content-msg {
  overflow: hidden;
  margin: 0 auto 20px auto;
  width: 800px;
  border: 1px solid #dadce0;
  border-radius: 8px;
}
@media screen and (max-width: 860px) {
  .user-main-content-msg {
    width: 100%;
  }
}
</style>