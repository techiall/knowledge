/**
*
*  @author ZTiger
*
*/

<template>
  <div>
    <div class="know-login-user-input">
      <div class="know-login-user">
        <Input
          v-model="formlogin.username"
          :class="{'know-login-error':showuserwarn}"
          prefix="ios-paw-outline"
          placeholder="用户名"
          size="large"
          @on-change="checkIswarn(1)"
          @on-blur="blurIswarn(1)"
          @on-enter="Submitlanding"
        />
        <transition name="knowerror">
          <div class="know-login-warn" v-show="showuserwarn">
            <Icon type="ios-information-circle" size="17" color="#f5222d" />请输入用户名!
          </div>
        </transition>
      </div>
      <div class="know-login-user">
        <Input
          v-model="formlogin.password"
          :class="{'know-login-error':showpasswordwarn}"
          prefix="ios-lock-outline"
          type="password"
          password
          placeholder="密码"
          size="large"
          @on-change="checkIswarn(2)"
          @on-blur="blurIswarn(2)"
          @on-enter="Submitlanding"
        />
        <transition name="knowerror">
          <div class="know-login-warn" v-show="showpasswordwarn">
            <Icon type="ios-information-circle" size="17" color="#f5222d" />请输入密码!
          </div>
        </transition>
      </div>
      <div class="know-login-user">
        <Checkbox v-model="formlogin.remeberMe" class="know-login-check-select-an">自动登录</Checkbox>
      </div>
    </div>
    <div class="know-login-user-land">
      <Button
        type="primary"
        class="know-login-user-land-button"
        size="large"
        :class="{'know-login-user-land-load':formloginFlag}"
        @click="Submitlanding"
      >
        <Icon
          type="ios-sync"
          :class="{'know-login-user-land-icon-load':formloginFlag,'know-login-user-land-icon-default':!formloginFlag}"
        />登 录
      </Button>
    </div>
  </div>
</template>

<script>
import { mapMutations } from 'vuex';

export default {
  props: ['showLoginRegister'],
  data() {
    return {
      // 用户名 警告 标志位
      showuserwarn: false,
      // 密码 警告标志位
      showpasswordwarn: false,
      // 登录标志位
      formloginFlag: false,
      //登陆 上传 的 信息
      formlogin: {
        username: '',
        password: '',
        remeberMe: false,
      },
      //感叹号
      information: 'ios-information-circle',
    };
  },
  methods: {
    ...mapMutations(['setUserData']),
    //登录
    Submitlanding() {
      if (this.formlogin.username === '') {
        this.showuserwarn = true;
      }
      if (this.formlogin.password === '') {
        this.showpasswordwarn = true;
      }
      if (this.formloginFlag || this.showuserwarn || this.showpasswordwarn) {
        // 同一时间只能提交一次
        return;
      }
      this.formloginFlag = true;
      let register = {
        username: this.formlogin.username,
        password: this.formlogin.password,
        'remember-me': this.formlogin.remeberMe ? 'on' : 'off',
      };
      let url = '/user/login';
      this.post_string(url, register)
        .then(() => {
          return Promise.resolve();
        })
        .then(() => {
          this.get('/user/me').then((res) => {
            let data = res.data;
            this.setUserData(data);
            this.$router.push(this.$route.query.redirect || '/project');
          });
        })
        .catch(() => {
          this.formloginFlag = false;
        });
    },
    //检测 信息 有误 change
    checkIswarn(val) {
      if (val === 1) {
        if (this.formlogin.username !== '') {
          this.showuserwarn = false;
        }
      } else if (val === 2) {
        if (this.formlogin.password !== '') {
          this.showpasswordwarn = false;
        }
      }
    },
    //失去 焦点
    blurIswarn(val) {
      if (val === 1) {
        if (this.formlogin.username === '') {
          this.showuserwarn = true;
        } else {
          this.showuserwarn = false;
        }
      } else if (val === 2) {
        if (this.formlogin.password === '') {
          this.showpasswordwarn = true;
        } else {
          this.showpasswordwarn = false;
        }
      }
    },
  },
  watch: {
    showLoginRegister() {
      this.showuserwarn = false;
      this.showpasswordwarn = false;
      this.formlogin = {
        username: '',
        password: '',
        remeberMe: false,
      };
    },
  },
};
</script>

<style  scoped>
#loginBox .know-login-user-land-button.know-login-user-land-load {
  background-color: rgba(24, 144, 255, 0.5);
  border-color: rgba(24, 144, 255, 0.5);
  cursor: default;
}
.know-login-user-land-icon-load {
  display: inline-block;
  font-size: 16px;
  margin-right: 5px;
  color: #fff;
  animation: ani-demo-load 1s linear infinite;
}
.know-login-user-land-icon-default {
  font-size: 16px;
  margin-right: 5px;
  color: #fff;
  display: none;
}
</style>