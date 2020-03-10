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
        <span>昵称</span>
      </div>
      <div class="box-msg-header-title">对您的昵称所做的更改将反映在您的 “知识图谱构建平台” 帐号中</div>
    </div>
    <div class="box-msg-row">
      <div class="box-msg-row-i">{{nickName}}</div>
      <div class="box-msg-row-confirm curpoin" @click="Statustriger(6)">
        <Icon type="md-create" size="20" />
      </div>
    </div>

    <div class="Editmodal-warp editmodal" v-show="ExitModalFlag">
      <div class="Editmodal-box">
        <div class="Editmodal-box-header">
          <span class="Editmodal-title">更改昵称</span>
          <div @click="Statustriger(5)" class="curpoin">
            <Icon type="md-close" size="16" />
          </div>
        </div>
        <div class="Editmodal-box-title">昵称</div>
        <div class="Editmodal-box-main">
          <input
            type="text"
            v-model="nickNameM"
            class="Editmodal-input"
            ref="FullNameI"
            @focus="Statustriger(1)"
            @blur="Statustriger(3)"
            @keydown.enter="Statustriger(7)"
          />
          <div class="Editmodal-hr" ref="FullName"></div>
        </div>
        <div class="editmodal-f">
          <Button type="text" @click="Statustriger(5)">取消</Button>
          <Button type="primary" @click="Statustriger(7)">完成</Button>
        </div>
      </div>
    </div>
  </div>
</template>


<script>
import { mapGetters, mapMutations } from 'vuex';

export default {
  data() {
    return {
      // 编辑名称框展示 标志位
      ExitModalFlag: false,
      // 昵称 修改
      nickNameM: '',
    };
  },
  computed: {
    ...mapGetters({
      nickName: 'getnickName',
    }),
  },
  watch: {
    nickNameM(val) {
      if (val !== '') {
        this.$refs.FullName.classList.remove('Editmodal-error');
      }
    },
    ExitModalFlag(val) {
      if (val) {
        document.addEventListener('keyup', this.keyupEsc);
      } else {
        document.removeEventListener('keyup', this.keyupEsc);
      }
    },
  },
  methods: {
    ...mapMutations({
      storeModify: 'modify',
    }),
    // 发送修改名称到服务器
    modifyServer() {
      let nickName = this.nickNameM.replace(/^\s+|\s+$/g, '');
      if (nickName === this.nickName) {
        this.ExitModalFlag = false;
        return;
      } else if (nickName === '') {
        this.$refs.FullName.classList.add('Editmodal-error');
        return;
      }
      const url = '/user/me';
      const obj = {
        nickName,
      };
      this.patch_json(url, obj)
        .then((res) => {
          const data = res.data;
          this.storeModify(data);
          this.$Message.success('修改成功');
          this.ExitModalFlag = false;
        })
        .catch(() => {});
    },
    Statustriger(type) {
      const statusMap = {
        // 昵称input focus触发函数
        1: () => {
          this.$refs.FullName.classList.add('Editmodal-color');
        },
        // 昵称 input blur触发函数
        3: () => {
          this.$refs.FullName.classList.remove('Editmodal-color');
        },
        // 名称 关闭
        5: () => {
          this.ExitModalFlag = false;
        },
        // 展示
        6: () => {
          this.ExitModalFlag = true;
          this.nickNameM = this.nickName;
          this.$nextTick(() => {
            this.$refs.FullNameI.focus();
          });
        },
        // 向服务器发送数据
        7: () => {
          this.modifyServer();
        },
      };
      statusMap[type]();
    },
    // 选择回退
    selectBack() {
      this.$emit('userMainCallback', 1, 1);
    },
    // 注册按键事件
    keyupEsc(e) {
      if (e.keyCode === 27) {
        this.Statustriger(5);
      }
    },
  },
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
.box-msg-row {
  display: flex;
  align-items: center;
  padding: 0px 20px 20px;
}
.box-msg-row-i {
  flex-grow: 1;
  font-family: Georgia;
  height: 30px;
  line-height: 30px;
  font-size: 17px;
  color: rgba(0, 0, 0, 0.87);
}
.box-msg-row-confirm {
  padding: 0 0 0 10px;
}
.box-msg-row-confirm i {
  font-weight: bold;
}
.curpoin {
  cursor: pointer;
}
.Editmodal-warp {
  position: fixed;
  left: 0;
  top: 0;
  width: 100vw;
  height: 100vh;
}
.editmodal {
  background: rgba(0, 0, 0, 0.502);
}
.Editmodal-box {
  position: absolute;
  left: calc(50% - 175px);
  top: calc(50% - 115px);
  width: 350px;
  height: 230px;
  border-radius: 4px;
  background: #fff;
  box-shadow: 0 12px 15px 0 rgba(0, 0, 0, 0.24);
  animation: showModal 0.5s;
}
.Editmodal-box-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px 20px 20px;
}
.Editmodal-box-title {
  padding: 10px 20px 0px;
}
.Editmodal-box-main {
  margin: 0px 20px 10px;
}
.Editmodal-hr {
  width: 100%;
  height: 2px;
  background: rgba(0, 0, 0, 0.12);
}
.Editmodal-color {
  transition: all 1s;
  background: #4285f4;
}
.Editmodal-error {
  transition: all 1s;
  background: #ed4014;
}
.Editmodal-title {
  font-size: 18px;
  color: #3c4043;
}
.Editmodal-input {
  outline: none;
  border: none;
  width: 100%;
  height: 30px;
}
.editmodal-f {
  padding: 30px 20px 0;
  height: 80px;
  text-align: right;
}
.editmodal-f button {
  margin-left: 20px;
}
@keyframes showModal {
  0% {
    transform: scale(0);
  }
  100% {
    transform: scale(1);
  }
}
</style>