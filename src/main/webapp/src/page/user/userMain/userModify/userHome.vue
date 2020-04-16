/**
*
*  @author ZTiger
*
*/


<template>
  <div>
    <user-msg class="b-user-border" />
    <logout-account class="b-user-border" />
  </div>
</template>


<script>
// 信息
import userMsg from './userMsg';
// 注销
import logoutAccount from './logoutAccount';

export default {
  components: { userMsg, logoutAccount },
  data() {
    return {};
  },
};
</script>


<style scoped>
</style>