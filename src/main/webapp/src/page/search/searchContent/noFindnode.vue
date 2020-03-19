/**
*
*  @author ZTiger
*
*/


<template>
  <div class="g-nofind-wrap">
    <span>找不到和您查询的</span>
    <span class="g-nofind">{{ShowSMeg}}</span>
    <span>相符的内容</span>
    <div class="g-nofind-tip">建议：</div>
    <ul class="g-nofind-ul">
      <li>请检查输入字词有无错误。</li>
      <li>请尝试其他查询词。</li>
      <li>请改用较常见的字词。</li>
    </ul>
  </div>
</template>


<script>
export default {
  props: {
    ShowSMeg: String,
  },
  data() {
    return {};
  },
};
</script>


<style scoped>
.g-nofind-wrap {
  margin: 40px 30px;
  font-size: 16px;
  color: #000;
}
.g-nofind {
  color: #dd4b39;
  font-weight: bold;
}
.g-nofind::before {
  content: '“';
  margin: 0 2px;
}
.g-nofind::after {
  content: '”';
  margin: 0 2px;
}
.g-nofind-tip {
  margin: 10px 0;
}
.g-nofind-ul {
  margin: 0 0 0 20px;
}
</style>
