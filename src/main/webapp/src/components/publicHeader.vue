/**
*
*  @author ZTiger
*
*/


<template>
  <header class="know-public-header" ref="knowSearchHeader">
    <div class="know-public-header-left df">
      <router-link to="/" class="df" v-if="TitleShow !== false">
        <span class="know-public-logo"></span>
        <span class="know-public-title">Knowledge Graph</span>
      </router-link>
      <div class="df" v-else>
        <span class="know-public-logo"></span>
        <router-link to="/">
          <span class="know-public-title know-item-home cup">
            <span v-if="title">{{title}}</span>
            <span v-else>Knowledge Graph</span>
          </span>
        </router-link>

        <Icon type="ios-arrow-forward " class="know-item-icon" />
        <span class="know-item cup">{{itemName}}</span>
      </div>
    </div>
    <div class="know-public-header-right df">
      <router-link :to="routerTO?routerTO:'/project'" class="know-public-header-icon cup ivu-icon ivu-icon-ios-keypad">
      </router-link>
      <drop-down v-if="userName">
        <img :src="images" class="know-public-header-user-logo cup" v-if="images" />
        <div v-else class="Noimg">{{nickName.charAt(0).toUpperCase()}}</div>
      </drop-down>
      <router-link to="/login" v-else>
        <span class="know-public-header-login cup">登录</span>
      </router-link>
    </div>
  </header>
</template>


<script>
import dropDown from './dropdown';
import { mapGetters, mapMutations } from 'vuex';

export default {
  components: { dropDown },
  props: ['routerTO', 'RouterFlag', 'itemName', 'TitleShow', 'title'],
  data() {
    return {
    };
  },
  computed: {
    ...mapGetters({
      images: 'getImageSrc',
      nickName: 'getnickName',
      userName: 'getuserName',
    }),
  },
  methods: {
    ...mapMutations(['setUserData','delToken']),
  },
};
</script>


<style scoped>
.know-public-header {
  height: 60px;
  padding: 0 55px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  box-shadow: 0 1px 10px rgba(0, 0, 0, 0.08);
}
.know-public-logo {
  display: inline-block;
  background-image: url('../assets/images/logo.png');
  background-size: 100% 100%;
  background-repeat: no-repeat;
  height: 20px;
  width: 20px;
}
.know-public-title {
  display: inline-block;
  font-family: Georgia;
  font-size: 20px;
  margin: 0 0 0 10px;
  color: #17233d;
}
.know-public-header-icon {
  font-size: 25px;
  margin-right: 15px;
  color: #808695;
  transition: color 0.5s;
}
.know-public-header-icon:hover {
  color: rgba(128, 134, 149, 0.8);
}
.know-public-header-login {
  display: inline-block;
  height: 30px;
  padding: 0 12px;
  line-height: 28px;
  text-align: center;
  border-radius: 2px;
  font-weight: 500;
  letter-spacing: 0.1em;
  font-size: 12px;
  color: #fff;
  background-color: #2d8cf0;
  transition: background-color 0.8s;
  font-weight: bold;
}
.know-public-header-login:hover {
  background-color: RGBA(45, 140, 240, 0.8);
}
.know-public-header-user-logo {
  display: inline-block;
  width: 25px;
  height: 25px;
  border-radius: 50%;
  text-align: center;
  line-height: 25px;
  font-size: 15px;
  user-select: none;
}
.know-header-user-load {
  color: #2d8cf0;
  font-size: 20px;
  animation: ani-demo-load 2.5s linear infinite;
}
.know-item {
  font-size: 18px;
  color: #17233d;
}
.know-item-home {
  margin-left: 10px;
  font-size: 18px;
}
.know-item-icon {
  margin: 0 5px;
  font-weight: 600;
}
</style>