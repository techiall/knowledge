/**
*
*  @author ZTiger
*
*/


<template>
  <div class="g-searchheader-warp">
    <div class="g-left">
      <router-link class="g-left-title" to="/">Knowledge&nbsp;Graph</router-link>
      <AutoComplete
        v-model="InSearchMeg"
        :data="searchData"
        class="b-auto-complete"
        element-id="search-header-input"
        size="large"
        style="width:542px"
        icon="ios-search"
        @on-change="changeEvent"
        @on-select="SearchSelectShow"
      />
    </div>
    <div class="g-right">
      <router-link to="/project" class="g-link-keypad">
        <Icon type="ios-keypad" class="g-link-icon cup" />
      </router-link>
      <drop-down v-if="userName">
        <img v-if="images" :src="images" class="g-login-img cup" />
        <div v-else class="Noimg">{{nickName.charAt(0).toUpperCase()}}</div>
      </drop-down>
      <router-link to="/login" v-else>
        <span class="g-link-login cup">登录</span>
      </router-link>
    </div>
  </div>
</template>


<script>
import dropDown from '@/components/dropdown';
import { mapMutations, mapGetters } from 'vuex';

export default {
  components: { dropDown },
  props: ['routerKey'],
  data() {
    return {
      //联想 请求的数据
      searchData: [],
      // 联系请求异步
      searchDataAsyn: '',
      // 联系请求异步 标志位
      searchDataAsynFlag: false,
      //用户输入的数据
      InSearchMeg: '',
    };
  },
  computed: {
    ...mapGetters({
      images: 'getImageSrc',
      nickName: 'getnickName',
      userName: 'getuserName',
    }),
  },
  watch: {
    $route: {
      handler() {
        this.routerAssignment();
      },
      immediate: true,
    },
  },
  mounted() {
    this.MonitoringlAddInput();
  },
  methods: {
    ...mapMutations(['setUserData', 'setSearchHistory']),
    // 通过路由赋值
    routerAssignment() {
      const queryName = this.$route.query[this.routerKey];
      if (queryName) {
        this.InSearchMeg = queryName;
      }
    },
    // change 事件触发的函数
    changeEvent() {
      this.searchDataAsyn = this.InSearchMeg.replace(/\s+/, '');
      if (!this.searchDataAsyn) {
        this.searchData = '';
        return;
      }
      if (this.searchDataAsynFlag) {
        return;
      }
      const q = this.searchDataAsyn;
      const URL = '/search';
      const OBJ = {
        q,
        tips: true,
      };
      this.searchDataAsynFlag = true;
      this.get(URL, OBJ)
        .then((res) => {
          this.searchDataAsynFlag = false;
          this.SearchLists(res.data);
        })
        .catch(() => {
          this.searchDataAsynFlag = false;
        });
    },
    //下拉列表搜索显示的
    SearchLists(data) {
      this.searchData = [];
      data.forEach((item) => {
        this.searchData.push(item.name);
      });
    },
    // 绑定 enter 监听事件
    MonitoringlAddInput() {
      const autoComplete = document.querySelector('#search-header-input');
      autoComplete.addEventListener('keyup', this.SearchReqData);
    },
    // 回车请求数据
    SearchReqData(event) {
      if (event.keyCode !== 13) {
        return;
      }
      this.SearchSelectShow(this.InSearchMeg);
    },
    // 请求 数据 展示 向 main 传数据
    SearchSelectShow(value) {
      const queryValue = value.replace(/\s+/, '');
      if (!queryValue || this.$route.query.q === queryValue) {
        return;
      }
      this.setSearchHistory(queryValue);
      this.$router.push({
        path: '/search',
        query: {
          q: queryValue,
        },
      });
    },
  },
};
</script>


<style scoped>
.g-searchheader-warp {
  display: flex;
  align-items: center;
  justify-content: space-between;
  min-width: 900px;
  height: 60px;
  padding: 0 55px 0 0;
  box-shadow: 0 1px 10px rgba(0, 0, 0, 0.08);
}
.g-left {
  display: flex;
  align-items: center;
  margin: 0 0 0 10px;
}
.g-left > div {
  display: inline-block;
}
.g-right {
  display: flex;
  align-items: center;
}
.g-link-keypad {
  display: inline-block;
  height: 25px;
  width: 25px;
  margin: 0 15px 0 0;
}
.g-link-icon {
  font-size: 25px;
  margin-right: 15px;
  color: #808695;
  transition: color 0.5s;
}
.g-link-icon:hover {
  color: rgba(128, 134, 149, 0.8);
}
.g-link-login {
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
.g-link-login:hover {
  background: rgba(45, 140, 240, 0.8);
}
.g-loading {
  color: #2d8cf0;
  font-size: 20px;
  animation: ani-demo-load 2.5s linear infinite;
}
.g-login-img {
  display: inline-block;
  width: 25px;
  height: 25px;
  border-radius: 50%;
  text-align: center;
  font-family: Georgia;
  color: #fff;
  user-select: none;
}
.g-left-title {
  margin: 0 10px 0 0;
  font-family: Georgia;
  font-size: 20px;
  color: #000;
}
</style>
<style>
#search-header-input {
  padding-left: 32px;
  padding-right: 0;
}
</style>