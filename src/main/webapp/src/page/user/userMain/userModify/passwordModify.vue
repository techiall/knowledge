/**
*
*  @author ZTiger
*
*/


<template>
  <div class="msg-news-box">
    <div class="msg-news-box-msg-title">
      <div class="box-msg-header-name">
        <Icon type="md-arrow-round-back" class="box-msg-header-s curpoin" @click="selectBack" />
        <span>密码</span>
      </div>
      <div class="box-msg-header-title">请选择安全系数高的密码，并且不要将其用于其他帐号。</div>
    </div>
    <div class="box-msg-row">
      <Input
        v-model="fromCPass.oldPass"
        class="box-msg-row-i"
        placeholder="输入您的旧密码"
        type="password"
        maxlength="32"
        password
        @on-change="changeInput(1)"
      />
      <div class="box-msg-row-tip" ref="oldInTip"></div>
    </div>
    <div class="box-msg-row">
      <div class="box-msg-row-di">
        <Input
          v-model="fromCPass.newPass"
          class="box-msg-row-i"
          placeholder="新密码"
          type="password"
          maxlength="32"
          @on-change="changeInput(2)"
          @on-blur="blurInput(2)"
        />
      </div>
      <div class="box-msg-row-tip" ref="newInTip"></div>
      <div class="box-msg-row-tips">
        <div class="box-msg-row-tips-name">密码强度：</div>
        <div class="box-msg-row-tips-title" ref="newInTipTitle"></div>
      </div>
      <div class="box-msg-row-tip-title">请使用 6 - 32 个字符，字符类型包含 字母、数字、符号其中的 2 种类型 ，请不要使用 空格 。</div>
    </div>
    <div class="box-msg-row">
      <Input
        v-model="fromCPass.RepeatPass"
        class="box-msg-row-i"
        placeholder="确认新密码"
        type="password"
        maxlength="32"
        @on-change="changeInput(3)"
      />
      <div class="box-msg-row-tip" ref="RepeatInTip"></div>
    </div>
    <div class="box-msg-row">
      <Button type="primary" @click="modifyServer" class="box-msg-row-button">更改密码</Button>
      <Button type="text" @click="selectBack">取消</Button>
    </div>
  </div>
</template>


<script>
export default {
  props: ["viewSeletctFlag"],
  data() {
    return {
      //修改密碼 上传 的 信息
      fromCPass: {
        oldPass: "",
        newPass: "",
        RepeatPass: ""
      }
    };
  },
  watch: {
    viewSeletctFlag(newVal) {
      if (newVal === 3) {
        this.fromCPass = {
          oldPass: "",
          newPass: "",
          RepeatPass: ""
        };
        this.$refs.oldInTip.innerHTML = "";
        this.$refs.newInTip.innerHTML = "";
        this.$refs.RepeatInTip.innerHTML = "";
      }
    }
  },
  methods: {
    // 发送密码名称到服务器
    modifyServer() {
      let flag = true;
      let numStatus = this.PasswordDetection();
      if (this.fromCPass.oldPass === "") {
        this.$refs.oldInTip.innerHTML = "请输入旧密码!";
        flag = false;
      }
      if (this.fromCPass.newPass === "") {
        this.$refs.newInTip.innerHTML = "请输入新密码!";
        flag = false;
      } else if (numStatus !== 0) {
        this.blurInput();
        flag = false;
      } else if (this.fromCPass.oldPass === this.fromCPass.RepeatPass) {
        this.$refs.newInTip.innerHTML = "旧密码和新密码不能一样!";
        flag = false;
      }
      if (this.fromCPass.RepeatPass === "") {
        this.$refs.RepeatInTip.innerHTML = "请确认新密码!";
        flag = false;
      } else if (this.fromCPass.RepeatPass !== this.fromCPass.newPass) {
        this.$refs.RepeatInTip.innerHTML = "密码不一致!";
        flag = false;
      }
      if (!flag) {
        return;
      }
      let url = "user/me/password";
      let obj = {
        srcPassword: this.fromCPass.oldPass,
        password: this.fromCPass.newPass
      };
      this.patch_string(url, obj)
        .then(() => {
          this.$Message.success("密码修改成功");
          this.$emit("userMainCallback", 1, 1);
        })
        .catch(() => {});
    },
    // 选择回退
    selectBack() {
      this.$emit("userMainCallback", 1, 1);
    },
    // input change 事件
    changeInput(val) {
      //空格正则
      let regSpace = /(^\s+)|(\s+$)|\s+/g;
      //密码强度正则  包含字母、数字、符号中至少2种
      let regDifferent = /(?=.*[a-zA-Z])(?=.*\d)|(?=.*[a-zA-Z])(?=.*[-+=|,!@#$%^&*?_`.~/(){}[\]<>])|(?=.*\d)(?=.*[-+=|,!@#$%^&*?_`.~/(){}[\]<>])/;
      let numStatus = 0;
      let oldPass = this.fromCPass.oldPass;
      let password = this.fromCPass.newPass;
      let RepeatPass = this.fromCPass.RepeatPass;
      switch (val) {
        case 1:
          if (oldPass !== "") {
            this.$refs.oldInTip.innerHTML = "";
          }
          break;
        case 2:
          this.$refs.newInTip.innerHTML = "";
          if (password === "") {
            this.$refs.newInTipTitle.innerHTML = "";
          } else {
            // 匹配是否有空格
            if (!regSpace.test(password)) {
              numStatus += 1;
            }
            //判断输入的密码长度不小于6位
            if (password.length >= 6) {
              numStatus += 3;
            }
            //判断包含字母、数字、符号中至少2种
            if (regDifferent.test(password)) {
              numStatus += 5;
            }
            if (
              numStatus === 1 ||
              numStatus === 0 ||
              numStatus === 5 ||
              numStatus === 6
            ) {
              this.$refs.newInTipTitle.innerHTML = "太短";
            } else if (numStatus === 3 || numStatus === 4) {
              this.$refs.newInTipTitle.innerHTML = "良好";
            } else if (numStatus === 8 || numStatus === 9) {
              this.$refs.newInTipTitle.innerHTML = "极佳";
            }
          }
          break;
        case 3:
          if (RepeatPass !== "") {
            this.$refs.RepeatInTip.innerHTML = "";
          }
          break;
      }
    },
    //失去焦点
    blurInput(val) {
      if (val === 2) {
        if (this.fromCPass.newPass !== "") {
          let numStatus = this.PasswordDetection();
          if (numStatus === 1) {
            this.$refs.newInTip.innerHTML = "不能包括空格!";
          } else if (numStatus === 2) {
            this.$refs.newInTip.innerHTML = "长度为6-32个字符!";
          } else if (numStatus === 3) {
            this.$refs.newInTip.innerHTML =
              "必须包含字母、数字、符号中至少2种!";
          } else {
            this.$refs.newInTip.innerHTML = "";
          }
        }
      }
    },
    // 密码检测
    PasswordDetection() {
      //空格正则
      let regSpace = /(^\s+)|(\s+$)|\s+/g;
      //密码强度正则  包含字母、数字、符号中至少2种
      let regDifferent = /(?=.*[a-zA-Z])(?=.*\d)|(?=.*[a-zA-Z])(?=.*[-+=|,!@#$%^&*?_`.~/(){}[\]<>])|(?=.*\d)(?=.*[-+=|,!@#$%^&*?_`.~/(){}[\]<>])/;
      let password = this.fromCPass.newPass;
      let numStatus = 0;
      if (regSpace.test(password)) {
        numStatus = 1;
      } else if (password.length < 6) {
        numStatus = 2;
      } else if (!regDifferent.test(password)) {
        numStatus = 3;
      }
      return numStatus;
    }
  }
};
</script>


<style scoped>
.msg-news-box-msg-title {
  padding: 20px;
}
.box-msg-header-name {
  display: flex;
  align-items: center;
  height: 34px;
  font-size: 23px;
  color: #202124;
}
.box-msg-header-title {
  margin: 10px 0 0px 0;
  font-size: 12px;
  font-weight: 520;
  color: #5f6368;
}
.box-msg-row {
  width: 350px;
  padding: 0px 20px;
}
.box-msg-row:last-of-type {
  margin: 0 0 20px 0;
}
.box-msg-row-confirm {
  padding: 0 0 0 10px;
}
.box-msg-header-s {
  margin: 0 10px 0 0;
  height: 34px;
  width: 34px;
  line-height: 34px;
}
.box-msg-header-s:active {
  border-radius: 50%;
  background: #dcdee2;
}
.box-msg-row-tip {
  user-select: none;
  margin: 5px 0;
  height: 25px;
  line-height: 25px;
  color: #ed4014;
}
.box-msg-row-tip-title {
  margin: 0 0 5px 0;
}
.box-msg-row-tips {
  height: 25px;
  line-height: 25px;
}
.box-msg-row-tips-name {
  display: inline;
  color: rgba(0, 0, 0, 0.87);
}
.box-msg-row-tips-title {
  display: inline;
  font-size: 12px;
}
.box-msg-row-button {
  margin: 0 20px 0 0;
  height: 36px;
}
.curpoin {
  cursor: pointer;
}
</style>