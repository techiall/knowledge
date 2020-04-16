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
      <router-view />
    </div>
  </div>
</template>


<script>
export default {
  data() {
    return {};
  },
  methods: {},
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
</style>