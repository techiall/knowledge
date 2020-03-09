/**
*
*  @author ZTiger
*
*/


<template>
  <div id="know-register">
    <div class="know-login-user-input">
      <div class="know-login-user" :class="registeruserClass">
        <Input
          v-model="formRegister.username"
          placeholder="用户名"
          prefix="ios-paw-outline"
          :icon="registerUserIcon"
          size="large"
          :class="{'know-login-error':registerusernameFlag}"
          @on-change="checkIswarn(3)"
          @on-blur="blurIswarn(3)"
          @on-enter="SubmitRegister"
        />
        <transition name="knowerror">
          <div class="know-login-warn" v-show="registerusernameFlag">
            <Icon type="ios-information-circle" size="17" color="#f5222d" />请输入用户名！
          </div>
        </transition>
      </div>
      <div class="know-login-user">
        <Input
          type="password"
          autocomplete="on"
          aria-autocomplete="inline"
          v-model="formRegister.password"
          prefix="ios-lock-outline"
          placeholder="密码"
          size="large"
          :class="{'know-login-error':registerPassword}"
          maxlength="32"
          @on-change="checkIswarn(4)"
          @on-focus="registerPasswordShowTips = true,registerPassword = false"
          @on-blur="blurIswarn(4)"
          @on-enter="SubmitRegister"
        />
        <transition name="knowTips">
          <div class="know-login-user-password-tips" v-show="registerPasswordShowTips">
            <div :class="{'know-login-user-password-tips-d':true}">
              <Icon :type="regexPassword.space" size="17" :color="regexPassword.Scolor" />&nbsp;&nbsp;&nbsp; 不能包括空格
            </div>
            <div :class="{'know-login-user-password-tips-d':true}">
              <Icon :type="regexPassword.number" size="17" :color="regexPassword.Ncolor" />&nbsp;&nbsp;&nbsp;长度为 6-32 个字符
            </div>
            <div :class="{'know-login-user-password-tips-d':true}">
              <Icon :type="regexPassword.different" size="17" :color="regexPassword.Dcolor" />&nbsp;&nbsp;&nbsp;必须包含字母、数字、符号中至少 2 种
            </div>
          </div>
        </transition>
        <transition name="knowLoginShowTips">
          <div class="know-login-warn know-login-show-tips" v-show="registerPassword">
            <Icon type="ios-information-circle" size="17" color="#f5222d" />
            &nbsp;&nbsp;&nbsp;{{registerShowPassword}}
          </div>
        </transition>
      </div>
      <div class="know-login-user">
        <Input
          autocomplete="off"
          type="password"
          v-model="formRegister.RepeatPassword"
          prefix="ios-lock-outline"
          placeholder="确认密码"
          size="large"
          :class="{'know-login-error':registerRepeatPassword}"
          maxlength="32"
          @on-change="checkIswarn(5)"
          @on-blur="blurIswarn(5)"
          @on-enter="SubmitRegister"
        />
        <transition name="knowerror">
          <div class="know-login-warn" v-show="registerRepeatPassword">
            <Icon type="ios-information-circle" size="17" color="#f5222d" />
            &nbsp;&nbsp;&nbsp;
            {{registerRepeatPasswordFalg?'两次输入的密码不匹配！':'请确认密码!'}}
          </div>
        </transition>
      </div>
    </div>
    <div class="know-login-user-land">
      <Button
        type="primary"
        class="know-login-user-land-button"
        :class="{'know-register-user-land-load':submitFlag}"
        size="large"
        @click="SubmitRegister"
      >
        <Icon
          type="ios-sync"
          :class="{'know-register-icon-load':submitFlag,'know-register-icon-load-default':!submitFlag}"
        />注 册
      </Button>
    </div>
  </div>
</template>

<script>
export default {
  props: ['showLoginRegister'],
  data() {
    return {
      // 注册 用户名 警告 标志位
      registerusernameFlag: false,
      // 注册 密码 警告 标志位
      registerPassword: false,
      // 注册 密码 显示 语句
      registerShowPassword: '请输入密码！',
      // 注册 密码 显示 提示
      registerPasswordShowTips: false,
      // 注册 密码 正则 标志位
      regexPassword: {
        // 空格
        space: 'md-checkmark-circle',
        Scolor: '#19be6b',
        Sflag: true,
        // 位数
        number: 'ios-information-circle',
        Ncolor: '#87CEFA',
        Nflag: false,
        // 不同
        different: 'ios-information-circle',
        Dcolor: '#87CEFA',
        Dflag: false,
      },
      // 注册 信息
      formRegister: {
        username: '',
        password: '',
        RepeatPassword: '',
      },
      // 注册 重复 密码  警告 标志位
      registerRepeatPassword: false,
      // 注册 重复 密码  警告 信息 标志位
      registerRepeatPasswordFalg: false,
      // 防止 多次注册提交
      submitFlag: false,
      // 绿色
      successColor: '#19be6b',
      // 蓝色
      primaryColor: '#87CEFA',
      // 红色
      errorColor: '#f5222d',
      // 感叹号
      information: 'ios-information-circle',
      // 对号
      checkmark: 'md-checkmark-circle',
      // 判断用户名是否存在icon
      registerUserIcon: '',
      // 判断用户存在 class
      registeruserClass: '',
      // 判断用户存在 请求 延迟 标志 符号
      registerdelayuserFlag: false,
      //判断用户存在 请求 延迟 纪录
      registerdelayusername: '',
    };
  },
  methods: {
    // 注册
    SubmitRegister() {
      if (this.formRegister.username === '') {
        this.registerusernameFlag = true;
      }
      if (this.formRegister.password === '') {
        this.registerPassword = true;
      }
      if (this.formRegister.RepeatPassword === '') {
        this.registerRepeatPassword = true;
        this.registerRepeatPasswordFalg = false;
      }
      if (
        this.registerusernameFlag ||
        this.registerPassword ||
        this.registerRepeatPassword ||
        this.registeruserClass !== 'register-success'
      )
        return;
      if (this.submitFlag) return;
      this.submitFlag = true;

      this.post_json('/register', {
        userName: this.formRegister.username,
        password: this.formRegister.password,
      })
        .then((res) => {
          this.submitFlag = false;
          if (res.code === 0 && res.msg === 'Success') {
            this.$emit('mainCallback', 1, true);
          }
        })
        .catch(() => {
          this.submitFlag = false;
        });
    },
    //判断用户名是否存在
    IsregisterUser(userName) {
      if (this.registeruserFlag) {
        this.registerdelayusername = userName;
        return;
      }
      this.registeruserFlag = true;
      this.registerUserIcon = 'md-refresh';
      this.registeruserClass = 'register-wait';
      let url = '/register/query';
      let obj = {
        name: userName,
      };
      this.get(url, obj)
        .then((res) => {
          this.registeruserFlag = false;
          if (this.registerdelayusername !== '') {
            let registerName = this.registerdelayusername;
            this.registerdelayusername = '';
            this.IsregisterUser(registerName);
          } else if (this.formRegister.username === '') {
            this.registerUserIcon = '';
            this.registeruserClass = '';
          } else {
            if (res.data) {
              this.registerUserIcon = 'md-alert';
              this.registeruserClass = 'register-warning';
            } else {
              this.registerUserIcon = 'md-checkmark-circle';
              this.registeruserClass = 'register-success';
            }
          }
        })
        .catch(() => {});
    },
    //检测 信息 有误 change
    checkIswarn(val) {
      //空格正则
      let regSpace = /(^\s+)|(\s+$)|\s+/g;
      //密码强度正则  包含字母、数字、符号中至少2种
      let regDifferent = /(?=.*[a-zA-Z])(?=.*\d)|(?=.*[a-zA-Z])(?=.*[-+=|,!@#$%^&*?_`.~/(){}[\]<>])|(?=.*\d)(?=.*[-+=|,!@#$%^&*?_`.~/(){}[\]<>])/;

      if (val === 3) {
        if (this.formRegister.username !== '') {
          this.registerusernameFlag = false;
          if (regSpace.test(this.formRegister.username)) {
            this.registerUserIcon = 'md-alert';
            this.registeruserClass = 'register-warning';
          } else {
            this.IsregisterUser(this.formRegister.username);
          }
        } else {
          this.registerUserIcon = '';
          this.registeruserClass = '';
        }
      } else if (val === 4) {
        let password = this.formRegister.password;
        if (password !== '') {
          this.registerPassword = false;
          if (this.formRegister.RepeatPassword !== '') {
            if (this.formRegister.RepeatPassword === password) {
              this.registerRepeatPassword = false;
            } else {
              this.registerRepeatPassword = true;
            }
          }
        }

        // 匹配是否有空格
        if (regSpace.test(password)) {
          this.regexPassword.space = this.information;
          this.regexPassword.Scolor = this.errorColor;
          this.regexPassword.Sflag = false;
        } else {
          this.regexPassword.space = this.checkmark;
          this.regexPassword.Scolor = this.successColor;
          this.regexPassword.Sflag = true;
        }
        //判断输入的密码长度不小于6位
        if (password.length >= 6) {
          this.regexPassword.number = this.checkmark;
          this.regexPassword.Ncolor = this.successColor;
          this.regexPassword.Nflag = true;
        } else {
          this.regexPassword.number = this.information;
          this.regexPassword.Ncolor = this.primaryColor;
          this.regexPassword.Nflag = false;
        }
        //判断包含字母、数字、符号中至少2种
        if (regDifferent.test(password)) {
          this.regexPassword.different = this.checkmark;
          this.regexPassword.Dcolor = this.successColor;
          this.regexPassword.Dflag = true;
        } else {
          this.regexPassword.different = this.information;
          this.regexPassword.Dcolor = this.primaryColor;
          this.regexPassword.Dflag = false;
        }
      } else if (val === 5) {
        if (this.formRegister.RepeatPassword !== '') {
          this.registerRepeatPassword = false;
          let strRPass = this.formRegister.RepeatPassword;
          let strPass = this.formRegister.password.substr(0, strRPass.length);
          if (strPass !== strRPass) {
            this.registerRepeatPassword = true;
            this.registerRepeatPasswordFalg = true;
          }
        }
      }
    },
    //失去 焦点
    blurIswarn(val) {
      if (val === 3) {
        if (this.formRegister.username === '') {
          this.registerusernameFlag = true;
          this.registerUserIcon = '';
          this.registeruserClass = '';
        } else {
          this.registerusernameFlag = false;
        }
      } else if (val === 4) {
        this.registerPasswordShowTips = false;
        if (this.formRegister.password === '') {
          this.registerPassword = true;
          this.registerShowPassword = '请输入密码！';
        } else {
          if (!this.regexPassword.Sflag) {
            this.registerPassword = true;
            this.registerShowPassword = '不能包括空格！';
          } else if (!this.regexPassword.Nflag) {
            this.registerPassword = true;
            this.registerShowPassword = '长度为6-32个字符';
          } else if (!this.regexPassword.Dflag) {
            this.registerPassword = true;
            this.registerShowPassword = '必须包含字母、数字、符号中至少2种';
          } else this.registerPassword = false;
        }
      } else if (val === 5) {
        if (this.formRegister.RepeatPassword === '') {
          this.registerRepeatPassword = true;
          this.registerRepeatPasswordFalg = false;
        } else {
          this.registerRepeatPassword = false;
          if (this.formRegister.RepeatPassword !== this.formRegister.password) {
            this.registerRepeatPassword = true;
            this.registerRepeatPasswordFalg = true;
          }
        }
      }
    },
    // 初始化 函数
    Initvariable() {
      this.registerusernameFlag = false;
      this.registerPassword = false;
      this.registerRepeatPassword = false;
      this.registerdelayuserFlag = false;
      this.registerdelayusername = '';
      this.formRegister = {
        username: '',
        password: '',
        RepeatPassword: '',
      };
      this.registerUserIcon = '';
      this.registeruserClass = '';
      this.regexPassword = {
        space: 'md-checkmark-circle',
        Scolor: '#19be6b',
        Sflag: true,
        number: 'ios-information-circle',
        Ncolor: '#87CEFA',
        Nflag: false,
        different: 'ios-information-circle',
        Dcolor: '#87CEFA',
        Dflag: false,
      };
      this.registerShowPassword = '请输入密码!';
    },
  },
  watch: {
    showLoginRegister() {
      this.Initvariable();
    },
  },
};
</script>

<style>
#know-register
  .register-wait
  .ivu-icon.ivu-icon-md-refresh.ivu-input-icon.ivu-input-icon-normal {
  animation: ani-demo-spin 1s linear infinite;
  font-size: 20px;
  color: #2db7f5;
}
#know-register
  .register-warning
  .ivu-icon.ivu-input-icon.ivu-input-icon-normal {
  color: #f5222d;

  font-size: 20px;
}
#know-register
  .register-success
  .ivu-icon.ivu-input-icon.ivu-input-icon-normal {
  color: #19be6b;
  font-size: 20px;
}
#know-register .know-register-icon-load {
  display: inline-block;
  font-size: 16px;
  margin-right: 5px;
  color: #fff;
  animation: ani-demo-load 1s linear infinite;
}
#know-register .know-register-icon-load-default {
  font-size: 16px;
  margin-right: 5px;
  color: #fff;
  display: none;
}
#know-register .know-login-user-land-button.know-register-user-land-load {
  background-color: rgba(24, 144, 255, 0.5);
  border-color: rgba(24, 144, 255, 0.5);
  cursor: default;
}
</style>