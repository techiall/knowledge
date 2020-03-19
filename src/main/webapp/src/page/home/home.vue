/**
*
*  @author ZTiger
*
*/


<template>
  <div class="g-home-wrap" v-title="知识图谱节点搜索">
    <div class="g-header-warp">
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
    <div class="g-search-wrap" ref="knowSearchBox">
      <div class="g-title">
        <div class="g-title-en">
          <span class="g-title-logo"></span>
          <span>Knowledge Graph</span>
        </div>
        <div class="g-title-cn">知识图谱节点搜索</div>
      </div>
      <AutoComplete
        class="b-auto-complete"
        v-model="InSearchMeg"
        element-id="home-header-input"
        size="large"
        style="width:542px"
        icon="ios-search"
        @on-change="changeEvent"
        @on-select="SearchSelectShow"
      >
        <div v-if="searchData.length">
          <Option v-for="(item,index) in searchData" :key="index" :value="item">{{item}}</Option>
        </div>
        <div v-else-if="searcHistory.length">
          <Option
            v-for="(item,index) in searcHistory"
            :key="index"
            class="g-auto-content cup"
            :value="item"
          >
            <div class="g-auto-left">
              <Icon type="md-time" class="g-auto-icon" />
              <div class="g-auto-title">{{item}}</div>
            </div>
            <div class="g-auto-right" @click.stop="delhistory(index)">删除</div>
          </Option>
        </div>
      </AutoComplete>
      <div class="g-footer">
        <div class="g-footer-button cup">Node 搜索</div>
        <div class="g-footer-button cup">{{time|LunarYYMMDD}}</div>
      </div>
    </div>
  </div>
</template>


<script>
import dropDown from '@/components/dropdown.vue';
import LunarCalendar from 'lunar-calendar';
import { mapMutations, mapGetters } from 'vuex';

export default {
  components: { dropDown },
  filters: {
    LunarYYMMDD(time) {
      const YY = time.getFullYear();
      const MM = time.getMonth() + 1;
      const DD = time.getDate();
      const lunar = LunarCalendar.solarToLunar(YY, MM, DD);
      return `农历${lunar.lunarMonthName}${lunar.lunarDayName}`;
    },
  },
  props: ['routerKey'],
  data() {
    return {
      // 联想 请求的数据
      searchData: [],
      // 联系请求异步
      searchDataAsyn: '',
      // 联系请求异步 标志位
      searchDataAsynFlag: false,
      //用户输入的数据
      InSearchMeg: '',
      // 时间
      time: new Date(),
    };
  },
  computed: {
    ...mapGetters({
      images: 'getImageSrc',
      nickName: 'getnickName',
      userName: 'getuserName',
      searcHistory: 'getSearcHistory',
    }),
  },
  created() {
    this.routerAssignment();
  },
  mounted() {
    this.MonitoringlAddInput();
  },
  methods: {
    ...mapMutations([
      'setUserData',
      'setSearchHistory',
      'delSearchHistory',
      'setToken',
    ]),
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
        this.searchData = [];
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
      const searchData = [];
      data.forEach((item) => {
        searchData.push(item.name);
      });
      this.searchData = searchData;
    },
    // 绑定 enter 监听事件
    MonitoringlAddInput() {
      const autoComplete = document.querySelector('#home-header-input');
      autoComplete.focus();
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
    // 清出历史纪录
    delhistory(index) {
      this.delSearchHistory(index);
    },
  },
};
</script>


<style scoped>
.g-home-wrap {
  position: relative;
  overflow-y: hidden;
  height: 100vh;
}
.g-header-warp {
  display: flex;
  align-items: center;
  justify-content: flex-end;
  min-width: 900px;
  height: 60px;
  padding: 0 55px 0 0;
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
  color: #fff;
  font-family: Georgia;
  display: inline-block;
  width: 25px;
  height: 25px;
  border-radius: 50%;
  text-align: center;
  user-select: none;
}
.g-search-wrap {
  position: absolute;
  top: 30%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 542px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}
.g-title-en span {
  display: inline-block;
}
.g-title-logo {
  height: 30px;
  width: 30px;
  margin: 0 10px 0 0;
  background: url('../../assets/images/logo.png') no-repeat;
  background-size: cover;
}
.g-title {
  margin: 20px 0 50px;
  font-family: Georgia;
  font-size: 33px;
  text-align: center;
  color: #000;
}
.g-title-cn {
  font-size: 15px;
  color: #00000073;
}
.g-autocomplete {
  width: 100%;
}
.g-auto-content {
  display: flex;
  justify-content: space-between;
  padding: 0 10px;
}
.g-auto-content:hover {
  background: #f3f3f3;
}
.g-auto-left,
.g-auto-right {
  display: flex;
  align-items: center;
}
.g-auto-right {
  font-size: 12px;
}
.g-auto-right:hover {
  color: #1a73e8;
  text-decoration: underline;
}
.g-auto-icon {
  font-size: 15px;
  margin: 0 14px 0 0;
}
.g-auto-title {
  display: inline-block;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  padding: 7px 0px;
  width: 460px;
  font-size: 14px;
  color: #52188c;
}
.g-footer {
  margin: 30px 0 0 0;
}
.g-footer-button {
  display: inline-block;
  height: 34px;
  padding: 0 16px;
  font-family: arial, sans-serif;
  line-height: 34px;
  color: #5f6368;
  background-color: #f2f2f2;
  background-image: linear-gradient(to top, #f5f5f5, #f1f1f1);
  border: 1px solid #f2f2f2;
  border-radius: 4px;
  user-select: none;
}
.g-footer-button:not(:last-of-type) {
  margin: 0 10px 0 0;
}
.g-footer-button:hover {
  color: #222;
  border: 1px solid #c6c6c6;
  background-color: #f8f8f8;
  background-image: linear-gradient(to top, #f8f8f8, #f1f1f1);
  box-shadow: 0 1px 1px rgba(0, 0, 0, 0.1);
}
.g-footer-button:active {
  border: 1px solid #4d90fe;
}
</style>
<style>
#home-header-input {
  padding-left: 32px;
  padding-right: 0;
}
</style>