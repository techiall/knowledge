/**
*
*  @author ZTiger
*
*/


<template>
  <div class="know-s-c scroll" v-show="searchFocus">
    <div class="know-s-c-m">
      <content-mheader
        :reqSuccessFlag="reqSuccessFlag"
        :ShowSMeg="ShowSMeg"
        :totalElements="totalElements"
        :reqShowData="reqShowData"
        :totalPages="totalPages"
        :pageNum="pageNum"
        :transfer="true"
        @contentCallback="contentCallback"
      ></content-mheader>
    </div>
    <div class="know-s-c-a"></div>
  </div>
</template>


<script>
import contentMheader from './contentMheader';
export default {
  components: { contentMheader },
  data() {
    return {
      //用户输入的数据
      ShowSMeg: '',
      //纪录用户输入的数据防止重复提交
      InOldSearchMeg: '',
      // 分页 可以展示的总页数
      totalPages: '',
      // 当前请求的第几页
      pageNum: 1,
      // 一页可以展示的数据有多少
      pageSize: 8,
      // 总的数据有多少
      totalElements: '',
      // 请求的数据
      reqShowData: [],
      // 获取焦点标志位
      searchFocus: false,
      // 数据加载成功标志为
      reqSuccessFlag: '',
      //用户输入的数据
      InSearchMeg: '',
    };
  },
  methods: {
    // 请求 服务器数据
    getServerData() {
      this.InOldSearchMeg = this.InSearchMeg;
      let url = 'search';
      let obj = {
        size: this.pageSize,
        q: this.InSearchMeg,
        page: this.pageNum,
      };
      this.get(url, obj)
        .then((res) => {
          this.handleServerData(res.data);
          this.reqSuccessFlag = Math.random();
        })
        .catch(() => {});
    },
    // 处理请求的数据
    handleServerData(data) {
      this.searchFocus = true;
      this.totalElements = data.totalElements;
      this.ShowSMeg = this.InSearchMeg;
      this.totalPages = data.totalPages;
      let content = data.content;
      let reg = new RegExp(this.ShowSMeg, 'gi');
      if (this.pageNum === 0) {
        this.reqShowData = [];
      }
      content.forEach((item) => {
        this.reqShowData.push({
          info: item.info,
          nodeId: item.nodeId,
          labels: item.labels === null ? undefined : item.labels.labels,
          property:
            item.property === null
              ? {}
              : this.handleServerProperty(item.property.property),
          text: this.html2Escape(item.text).replace(
            reg,
            '<span class="s-c-m-c-t-u">' + this.ShowSMeg + '</span>',
          ),
          nodeName: item.nodeName,
          Itemsource: item.nodeItemName,
          nodeTitleName: this.html2Escape(item.nodeName).replace(
            reg,
            '<span class="s-c-m-c-t-u">' + this.ShowSMeg + '</span>',
          ),
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
    //HTML标签转义（< -> &lt;）
    html2Escape(sHtml) {
      return sHtml.replace(/[<>&"]/g, (c) => {
        return { '<': '&lt;', '>': '&gt;', '&': '&amp;', '"': '&quot;' }[c];
      });
    },
    //content 回调函数
    contentCallback(type, val) {
      const statusMap = {
        1: () => {
          this.pageNum++;
          this.getServerData();
        },
        2: () => {
          this.$refs.contentaside.CAsideCallback(1, val);
        },
      };
      statusMap[type]();
    },
    // 数据初始化
    InitData() {
      this.pageNum = 0;
      this.getServerData();
    },
  },
  watch: {
    $route: {
      handler(to) {
        if (!to.query.q) {
          this.$emit('SearchInCancel');
          this.$emit('setSearchMsg', '');
          this.searchFocus = false;
          return;
        }
        this.$emit('SearchInFocus');
        this.$emit('setSearchMsg', to.query.q);
        this.InSearchMeg = to.query.q;
        this.pageNum = 0;
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
</style>