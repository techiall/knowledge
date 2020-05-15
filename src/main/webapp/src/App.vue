<template>
  <div id="app">
    <router-view></router-view>
  </div>
</template>

<script>
import { getUserMe } from '@/api/user';
import { mapMutations } from 'vuex';

export default {
  name: 'app',
  created() {
    this.getServerUser();
  },
  methods: {
    ...mapMutations(['setUserData', 'setToken', 'delToken']),
    async getServerUser() {
      const data = await getUserMe(); 
      if (data.user.id) {
        this.setUserData(data);
      } else {
        this.delToken();
        this.setToken(data._csrf.token);
      }
    },
  },
};
</script>

<style>
#app {
  font-family: -apple-system, BlinkMacSystemFont, Segoe UI, PingFang SC,
    Hiragino Sans GB, Microsoft YaHei, Helvetica Neue, Helvetica, Arial,
    sans-serif, Apple Color Emoji, Segoe UI Emoji, Segoe UI Symbol;
}
</style>

