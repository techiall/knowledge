/**
*
*  @author ZTiger
*
*/


<template>
  <div class="know-s-c ">
    <Spin size="large" fix v-show="loadingSpin" />
    <div class="know-s-c-m">
      <content-mheader
        :ShowSMeg="InSearchMeg"
        :totalElements="totalElements"
        :reqShowData="reqShowData"
        :findTime="findTime"
        :pageNum="pageNum"
        :loadingSpin="loadingSpin"
      />
      <div class="g-footer" v-show="this.totalElements">
        <Page
          :total="totalElements"
          :current="pageNum"
          :page-size="pageSize"
          @on-change="pageChange"
        />
      </div>
    </div>
  </div>
</template>


<script>
import contentMheader from './contentMheader';
export default {
  components: { contentMheader },
  data() {
    return {
      // 数据加载
      loadingSpin: false,
      // 当前请求的第几页
      pageNum: 1,
      // 一页可以展示的数据有多少
      pageSize: 8,
      // 总的数据有多少
      totalElements: '',
      // 请求的数据
      reqShowData: [],
      // 数据加载成功标志为
      reqSuccessFlag: '',
      //用户输入的数据
      InSearchMeg: '',
      // 查找时间
      findTime: '',
      // 查找排序
      sort: '',
    };
  },
  methods: {
    // 请求 服务器数据
    getServerData() {
      const url = '/search';
      const obj = {
        size: this.pageSize,
        q: this.InSearchMeg.substr(0, 32),
        page: this.pageNum - 1,
      };
      const startTime = new Date().getTime();
      if (this.sort) obj.sort = this.sort;
      this.loadingSpin = true;
      this.get(url, obj)
        .then((res) => {
          this.loadingSpin = false;
          this.findTime = new Date().getTime() - startTime;
          this.handleServerData(res.data);
          this.reqSuccessFlag = Math.random();
        })
        .catch(() => {});
    },
    // 处理请求的数据
    handleServerData(data) {
      this.totalElements = data.totalElements;
      const { content } = data;
      this.reqShowData = [];
      content.forEach((item) => {
        this.reqShowData.push({
          info: item.info,
          nodeId: item.nodeId,
          labels: item.labels === null ? undefined : item.labels.labels,
          property:
            item.property === null
              ? {}
              : this.handleServerProperty(item.property.property),
          text: item.text,
          nodeName: item.nodeName,
          Itemsource: item.nodeItemName,
        });
      });
    },
    // 处理属性 数据
    handleServerProperty(data) {
      let Arr = [];
      for (let key in data) {
        data[key].forEach((item) => {
          Arr.push({
            id: item.id,
            name: key,
            title: item.name,
          });
        });
      }
      return Arr;
    },
    // 页码改变的回调，返回改变后的页码
    pageChange(page) {
      const query = {
        q: this.InSearchMeg,
        page: page,
      };
      if (this.sort) query.sort = this.sort;
      this.$router.push({
        path: '/search',
        query,
      });
    },
  },
  watch: {
    $route: {
      handler(to) {
        this.InSearchMeg = to.query.q;
        this.pageNum = parseInt(to.query.page, 10) || 1;
        this.sort = to.query.sort || '';
        this.getServerData();
      },
      immediate: true,
    },
  },
};
</script>


<style scoped>
.know-s-c {
  height: 100%;
  padding: 10px 10px 10px 50px;
  display: flex;
  overflow: auto;
}
.know-s-c-m {
  min-width: 670px;
  width: 670px;
}
.know-s-c-a {
  flex: 1;
  padding: 10px;
}
.g-footer {
  margin: 10px 0 40px;
  text-align: center;
}
</style>