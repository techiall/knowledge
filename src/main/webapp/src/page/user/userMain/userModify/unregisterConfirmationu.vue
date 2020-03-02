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
        <span>删除您的 “知识图谱构建平台” 帐号</span>
      </div>
      <div
        class="box-msg-header-tips box-msg-header-defaultC box-msg-header-default"
      >这部分内容非常重要，请仔细阅读。</div>
      <div
        class="box-msg-header-title box-msg-header-default"
      >您正在尝试删除自己的 “知识图谱构建平台” 帐号（您可以通过此帐号使用各种 “知识图谱构建平台” 服务）。删除后，您将无法再使用任何 “知识图谱构建平台” 服务，并且您的帐号和数据也将会丢失。</div>
      <div
        class="box-msg-header-title box-msg-header-default"
      >您无法再访问使用 “知识图谱构建平台” 。删除帐号后，您的力导图，树图，节点知识的数据也将一并删除。</div>
    </div>
    <Divider />
    <div class="box-msg-row">
      <div class="box-msg-header-tips box-msg-header-defaultC box-msg-header-default">勾选下方的注销按键</div>
    </div>
    <div class="box-msg-row">
      <Checkbox
        v-model="checkFlag"
        size="large"
        class="box-msg-row-check"
      >是的，我要永久删除此 “知识图谱构建平台” 帐号及其所有关联数据。</Checkbox>
    </div>
    <div class="box-msg-row">
      <Button type="primary" class="box-msg-row-button" @click="modifyServer">删除账号</Button>
      <Button type="text" class="box-msg-row-button" @click="selectBack">取消</Button>
    </div>
  </div>
</template>


<script>
import { mapMutations } from 'vuex';

export default {
  data() {
    return {
      // 判断用户选择
      checkFlag: false,
    };
  },
  methods: {
    ...mapMutations(['delToken']),
    // 发送修改名称到服务器
    modifyServer() {
      if (!this.checkFlag) {
        this.$Message.info('请勾选注销');
        return;
      }
      const url = 'user/me';
      this.delete_string(url)
        .then(() => {
          this.delToken();
          this.$router.push({ path: '/login' });
        })
        .catch(() => {});
    },
    // 选择回退
    selectBack() {
      this.$emit('userMainCallback', 1, 1);
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
}
.box-msg-header-default {
  letter-spacing: 0.1em;
  font-size: 14px;
}
.box-msg-header-defaultC {
  color: #202124;
}
.box-msg-header-tips {
  margin: 10px 0;
}
.box-msg-header-title {
  margin: 10px 0 0px 0;
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
  padding: 0px 20px 20px 20px;
}
.box-msg-row-button {
  margin: 0 20px 0 0;
  height: 36px;
}
.box-msg-row-check {
  margin: 0 0 0 0px;
}
.curpoin {
  cursor: pointer;
}
</style>
<style >
.box-msg-row-check .ivu-checkbox {
  margin: 0 10px 0 0;
}
</style>