<template>
  <div id="app">
    <router-view></router-view>
  </div>
</template>

<script>
import { mapMutations } from 'vuex';

export default {
  name: 'app',
  created() {
    this.getServerUser();
  },
  methods: {
    ...mapMutations(['setUserData', 'setToken','delToken']),
    getServerUser() {
      const url = '/user/me';
      this.get(url)
        .then((res) => {
          if (res.data.user.id) {
            let { data } = res;
            this.setUserData(data);
          } else {
            this.delToken();
            this.setToken(res.data._csrf.token);
          }
        })
        .catch(() => {});
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

