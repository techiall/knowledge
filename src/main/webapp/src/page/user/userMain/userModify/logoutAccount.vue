/**
*
*  @author ZTiger
*
*/


<template>
  <div class="msg-news-box">
    <div class="msg-news-box-msg-title">
      <div class="box-msg-header-name">
        <span>帐号注销</span>
      </div>
      <div class="box-msg-header-title">您可以随时删除自己的 “知识图谱构建平台” 帐号。如果事后改变主意了，您可能无法恢复帐号。</div>
      <div class="box-msg-header-title">您将失去该帐号中的所有数据和内容</div>
      <div class="box-msg-header-title">您将无法使用需要通过该帐号登录的 "知识图谱构建平台" 服务</div>
    </div>
    <div class="box-msg-row">
      <div class="box-msg-row-title curpoin" @click="selectRow">删除您的帐号</div>
    </div>
  </div>
</template>


<script>
export default {
  data() {
    return {
    };
  },
  methods: {
    // 确认选择
    selectRow() {
      this.$emit('userMainCallback',1,4);
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
  display: flex;
  align-items: center;
  padding: 0px 20px 20px 20px;
}
.box-msg-row-title{
  color: #1a73e8;
}
.curpoin {
  cursor: pointer;
}
</style>