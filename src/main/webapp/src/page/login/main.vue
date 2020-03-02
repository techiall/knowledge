/**
*
*  @author ZTiger
*
*/


<template>
  <div id="loginBox" v-title='知识图谱构建平台'>
    <Spin size="large" fix v-if="spinShow"></Spin>
    <div class="know-login-icon">
      <router-link to="/">
        <Icon type="ios-keypad" />
      </router-link>
    </div>
    <div class="know-login-box">
      <div class="know-login-title">
        <router-link to="/">
          <div class="know-login-title-en">
            <span class="kown-login-title-logo"></span>
            <span>Knowledge&nbsp;Graph</span>
          </div>
        </router-link>
        <div class="know-login-title-cn">
          <span>知识图谱构建平台</span>
        </div>
      </div>
      <div class="know-login-user-select">
        <span
          :class="{'know-login-user-select-span':true,'know-login-user-select-border':showLoginRegister}"
          @click="loginRegister(1)"
        >
          <span class="know-login-user-select-event">登&nbsp;录</span>
        </span>
        <span
          :class="{'know-login-user-select-span':true,'know-login-user-select-border':!showLoginRegister}"
          @click="loginRegister(2)"
        >
          <span class="know-login-user-select-event">注&nbsp;册</span>
        </span>
      </div>
      <div v-show="showLoginRegister">
        <login-content :showLoginRegister="showLoginRegister"></login-content>
      </div>
      <div v-show="!showLoginRegister">
        <register-content :showLoginRegister="showLoginRegister" @mainCallback="mainCallback"></register-content>
      </div>
    </div>
  </div>
</template>


<script>
import { mapMutations } from 'vuex';
import loginContent from './login/login';
import registerContent from './register/register';

export default {
  components: { loginContent, registerContent },
  data() {
    return {
      //设置免密登录加载
      spinShow: true,
      // 登陆 和 注册 之间 跳转
      showLoginRegister: true,
    };
  },
  methods: {
    ...mapMutations(['setUserData', 'setToken']),
    //登录 注册 切换 函数
    loginRegister(val) {
      switch (val) {
        case 1:
          this.showLoginRegister = true;
          break;
        case 2:
          this.showLoginRegister = false;
          break;
      }
    },
    // 回调函数
    mainCallback(type, val) {
      const statusMap = {
        1: () => {
          this.showLoginRegister = val;
        },
      };
      statusMap[type]();
    },
    // 获取  token
    getuserToken() {
      this.get('user/me')
        .then((res) => {
          this.spinShow = false;
          if (res.data.user.id) {
            let data = res.data;
            this.setUserData(data);
            this.$router.push({ path: '/project' });
          } else {
            this.setToken(res.data._csrf.token);
          }
        })
        .catch(() => {});
    },
  },
  mounted() {
    this.getuserToken();
  },
};
</script>


<style >
#loginBox {
  position: relative;
  height: 100vh;
  width: 100vw;
  background-image: url('../../assets/images/backgroudLogin.svg');
  background-color: #f0f2f5;
}
#loginBox .know-login-box {
  display: inline-block;
  width: 500px;
  position: absolute;
  left: 50%;
  top: 45%;
  transform: translate(-50%, -50%);
  height: 550px;
}
#loginBox .know-login-title-en {
  text-align: center;
  font-family: Georgia;
}
#loginBox .know-login-title-en span:nth-of-type(2) {
  /* font-weight: lighter; */
  user-select: none;
  height: 45px;
  line-height: 45px;
  font-size: 33px;
  color: #000;
  text-align: center;
}
#loginBox .know-login-title-cn {
  margin-top: 15px;
  height: 40px;
  line-height: 40px;
  font-size: 15px;
  color: rgba(0, 0, 0, 0.45);
  text-align: center;
}
#loginBox .kown-login-title-logo {
  background-image: url('../../assets/images/logo.png');
  background-repeat: no-repeat;
  background-position: 0px 12px;
  background-size: 70% 70%;
  display: inline-block;
  width: 40px;
  height: 40px;
  margin-right: 10px;
}
#loginBox .know-login-user-select {
  font-size: 16px;
  text-align: center;
  margin-top: 20px;
  height: 30px;
}
#loginBox .know-login-check-select-an {
  width: 300px;
  user-select: none;
}
#loginBox .know-login-user-select .know-login-user-select-span {
  display: inline-block;
  text-align: center;
  cursor: pointer;
  width: 100px;
  padding: 4px;
}
#loginBox .know-login-user-select .know-login-user-select-span:first-of-type {
  margin-right: 50px;
}
#loginBox .know-login-user-select-border {
  border-radius: 1px;
}
#loginBox .know-login-user-select-border .know-login-user-select-event {
  color: #1890ff;
}
#loginBox .know-login-user-select-event {
  width: 60px;
  display: inline-block;
  text-align: center;
  user-select: none;
}
#loginBox .know-login-user-select-event:hover {
  color: #2d8cf0;
}
#loginBox .know-login-warn {
  margin-top: 1px;
  font-size: 14px;
  color: #f5222d;
}
#loginBox .know-login-user {
  margin: 0 auto;
  margin-top: 30px;
  width: 400px;
}
#loginBox .know-login-user-land {
  text-align: center;
}
#loginBox .know-login-user-land-button {
  width: 400px;
  margin-top: 30px;
  background-color: #1890ff;
  border-color: #1890ff;
  /* text-shadow: 0 -1px 0 rgba(0,0,0,.12); */
}
#loginBox .know-login-error .ivu-input {
  border-color: #f5222d;
}
#loginBox .know-login-error .ivu-input:focus {
  border-color: #f5222d;
  box-shadow: 0 0 0 2px rgba(245, 34, 45, 0.2);
}
#loginBox .know-login-error .ivu-input:hover {
  border-color: #f5222d;
}
#loginBox .knowerror-enter-active {
  animation: namepasswordWarn 1s;
}
#loginBox .knowerror-leave-active {
  animation: namepasswordWarn 1s reverse;
}
#loginBox .knowTips-enter-active {
  animation: passwordTips 0.5s;
}
#loginBox .knowTips-leave-active {
  animation: passwordTips 0.5s reverse;
}
#loginBox .know-login-user-password-tips {
  color: #000;
}
#loginBox .know-login-user-password-tips-d {
  margin-top: 2px;
}
#loginBox .knowLoginShowTips-enter-active {
  animation: passwordshowWarn 0.5s ease-in;
}
#loginBox .knowLoginShowTips-leave-active {
  animation: passwordshowWarn 0.2s reverse;
}
#loginBox .know-login-show-tips {
  background-color: #f0f2f5;
}
#loginBox .know-login-icon {
  height: 60px;
  line-height: 60px;
  font-size: 24px;
  text-align: right;
  padding: 0 120px;
}
#loginBox .know-login-icon i {
  cursor: pointer;
  color: #808695;
  transition: color 0.5s;
}
#loginBox .know-login-icon i:hover {
  color: rgba(128, 134, 149, 0.8);
}
@keyframes namepasswordWarn {
  0% {
    transform: translateY(-5px);
    opacity: 0;
  }
  100% {
    transform: translateY(0);
    opacity: 1;
  }
}
@keyframes passwordTips {
  0% {
    height: 0;
  }
  100% {
    height: 66px;
  }
}
@keyframes passwordshowWarn {
  0% {
    transform: translateY(-10px);
    opacity: 0;
    height: 0;
  }
  100% {
    opacity: 1;
    transform: translateY(0);
    height: 22px;
  }
}

@media screen and (max-width: 500px) {
  #loginBox .know-login-box {
    width: 100%;
  }
  #loginBox .know-login-user {
    width: 100%;
  }
  #loginBox .know-login-user-land-button {
    width: 100%;
  }
}
@media screen and (max-width: 400px) {
  #loginBox .know-login-box {
    width: 400px;
  }
}
@media screen and (max-height: 650px) {
  #loginBox .know-login-box {
    top: 10%;
    transform: translate(-50%, 0%);
  }
}
</style>
