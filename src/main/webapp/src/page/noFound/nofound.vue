/**
*
*  @author ZTiger
*
*/


<template>
  <div class="g-error" v-title='知识图谱构建平台'>
    <div class="g-error-box">
      <img src="@/assets/images/404.svg" alt />
      <div class="g-error-tips">抱歉，你访问的页面不存在。</div>
      <Button type="primary">
        <router-link to="/" class="g-error-a">返回首页</router-link>
      </Button>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {};
  },
};
</script>

<style  scoped>
.g-error {
  overflow: hidden;
  width: 100vw;
  height: 100vh;
}
.g-error-box {
  margin: 100px auto;
  width: 400px;
  height: 400px;
  text-align: center;
}
.g-error-tips {
  margin: 20px 0;
}
.g-error-a,
.g-error-a:hover {
  color: #fff;
}
</style>