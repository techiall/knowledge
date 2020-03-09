/**
*
*  @author ZTiger
*
*/


<template>
  <ul class="know-journal-ul">
    <Scroll :on-reach-bottom="getPageLogData" :height="SetHeight" :loading-text="loadingText">
      <li v-for="(item ,index) in JournalData" v-bind:key="item.randomId" class="know-journal-li">
        <div
          class="know-journal-title"
          v-if="index===0||item.dateLog!==JournalData[index-1].dateLog"
        >
          <Icon type="ios-ionic" color="#19be6b" />
          <span class="know-journal-title-span">时间：{{item.dateLog}}</span>
        </div>
        <div class="know-journal-content">
          <div class="know-journal-content-title">
            <span class="know-journal-strong">{{item.nickName}}</span>
            在 {{item.dateValue}} 前，操作了
            <span
              class="know-journal-strong"
            >{{item.nodeName}}</span>
            节点，「{{item.operatorCrux}}」
          </div>
          <div class="know-journal-content-user">
            <span class="know-journal-strong">{{item.nodeName}}</span>
            节点，{{item.operator}}
          </div>
        </div>
      </li>
    </Scroll>
  </ul>
</template>


<script>
export default {
  props: ['treeNode', 'showSelectNum', 'InnerHeight', 'spinShow'],
  data() {
    return {
      // 请求数据标志位
      getDataFlag: false,
      // 请求的日志数据
      JournalData: [],
      // 请求数据数量
      dataSize: 10,
      // 分页起始位置
      dataPage: 0,
      // 分页终点位置
      dataEndPage: 1,
      // 请求标志位
      requestFlag: false,
      // topHeight
      TopHeight: 120,
      // 加载中的文案
      loadingText: '加载中',
    };
  },
  methods: {
    // 获取 服务器 数据
    getJournalData() {
      if (this.getDataFlag) return;
      this.$emit('update:spinShow', true);
      this.getDataFlag = true;
      this.dataPage = 1;
      this.getServerData(0);
    },
    // 分页点击加载 请求服务器数据 数据
    getPageLogData() {
      if (this.dataPage === this.dataEndPage) {
        this.loadingText = '没有更多的日志了'
        return;
      }
      this.getServerData(this.dataPage++);
    },
    // 请求 服务器 数据
    getServerData(Pageval) {
      if (this.requestFlag) return;
      this.requestFlag = true;
      let url = '/record/node/' + this.treeNode.id;
      let obj = {
        size: this.dataSize,
        page: Pageval,
      };
      this.get(url, obj)
        .then((res) => {
          this.$emit('update:spinShow', false);
          if (this.showSelectNum === 4) {
            this.requestFlag = false;
            this.dataEndPage = res.data.totalPages;
            this.handleJournalData(res.data.content);
          }
        })
        .catch(() => {
          this.requestFlag = false;
        });
    },
    // 设置重新获取数据
    setJournal() {
      this.getDataFlag = false;
      this.JournalData = [];
    },
    // 处理并且渲染 数据
    handleJournalData(data) {
      data.forEach((item) => {
        let Dateobj = this.handleTime(item.createTime);
        let DateFlite = (date) => {
          return date < 10 ? '0' + date : date;
        };
        let Megobj = this.handleMessage(item.operator, item.message);
        this.JournalData.push({
          randomId: Math.random(),
          dateLog:
            Dateobj.YY +
            ' - ' +
            DateFlite(Dateobj.MM) +
            ' - ' +
            DateFlite(Dateobj.DD),
          dateValue: Dateobj.dateValue,
          nodeName: Megobj.nodeName,
          Time:
            DateFlite(Dateobj.dateStart.getHours()) +
            ':' +
            DateFlite(Dateobj.dateStart.getMinutes()) +
            ':' +
            DateFlite(Dateobj.dateStart.getSeconds()),
          operator: Megobj.operator,
          nickName: item.nickName,
          operatorCrux: Megobj.operatorCrux,
        });
      });
    },
    // 处理响应 时间
    handleTime(dateVal) {
      let dateStart = new Date(dateVal);
      let dateEnd = new Date();
      let YY = dateStart.getFullYear();
      let MM = dateStart.getMonth() + 1;
      let DD = dateStart.getDate();
      let dateValue = dateEnd - dateStart;
      let DateValue = [1000, 60, 60, 24, 30, 12, Infinity];
      for (var i = 0; i < DateValue.length - 1; i++) {
        dateValue /= DateValue[i];
        if (dateValue < DateValue[i + 1]) {
          switch (i) {
            case 0:
              dateValue = parseInt(dateValue) + ' 秒';
              break;
            case 1:
              dateValue = parseInt(dateValue) + ' 分钟';
              break;
            case 2:
              dateValue = parseInt(dateValue) + ' 小时';
              break;
            case 3:
              dateValue = parseInt(dateValue) + ' 天';
              break;
            case 4:
              dateValue = parseInt(dateValue) + ' 个月';
              break;
            case 5:
              dateValue = parseInt(dateValue) + ' 年';
              break;
          }
          break;
        }
      }
      return {
        YY,
        MM,
        DD,
        dateStart,
        dateValue,
      };
    },
    // 处理message 信息
    handleMessage(type, val) {
      let value = JSON.parse(val);
      let operator,
        operatorCrux,
        nodeName = value.name;
      const statusMap = {
        UPDATE_NODE_NAME: () => {
          operator = value.message;
          operatorCrux = '修改节点名称';

          return {
            operator,
            nodeName,
            operatorCrux,
          };
        },
        ADD_NODE: () => {
          operator = value.message;
          operatorCrux = '添加节点';
          return {
            operator,
            nodeName,
            operatorCrux,
          };
        },
        ADD_NODE_PROPER: () => {
          operator = value.message;
          operatorCrux = '添加属性';

          return {
            operator,
            nodeName,
            operatorCrux,
          };
        },
        UPDATE_NODE_PROPER: () => {
          operator = value.message;
          operatorCrux = '修改属性';
          return {
            operator,
            nodeName,
            operatorCrux,
          };
        },
        DELETE_NODE_PROPER: () => {
          operator = value.message;
          operatorCrux = '删除属性';

          return {
            operator,
            nodeName,
            operatorCrux,
          };
        },
        ADD_NODE_PROPERTY: () => {
          operator = value.message;
          operatorCrux = '添加节点关系';

          return {
            operator,
            nodeName,
            operatorCrux,
          };
        },
        UPDATE_NODE_PROPERTY: () => {
          operator = value.message;
          operatorCrux = '修改节点关系';
          return {
            operator,
            nodeName,
            operatorCrux,
          };
        },
        UPDATE_NODE_PROPERTYNAME: () => {
          operator = value.message;
          operatorCrux = '变更节点关系';
          return {
            operator,
            nodeName,
            operatorCrux,
          };
        },
        DELETE_NODE_PROPERTY: () => {
          operator = value.message;
          operatorCrux = '删除节点关系';
          return {
            operator,
            nodeName,
            operatorCrux,
          };
        },
      };
      return statusMap[type]();
    },
  },
  watch: {
    treeNode: {
      handler(newval, oldval) {
        if (newval === '' || newval.id === oldval.id) return;
        this.getDataFlag = false;
        this.JournalData = [];
        if (this.showSelectNum === 4) {
          this.getJournalData();
        }
      },
    },
    showSelectNum(val) {
      if (val === 4) this.getJournalData();
    },
  },
  computed: {
    //设置  可视区 高度
    SetHeight() {
      return this.InnerHeight - this.TopHeight;
    },
  },
};
</script>


<style scoped>
.know-journal-ul {
  list-style: none;
}
.know-journal-title {
  color: #9a9a9a;
}
.know-journal-title-span {
  display: inline-block;
  text-indent: 1em;
  font-size: 16px;
}
.know-journal-content-title {
  margin-bottom: 5px;
  letter-spacing: 0.1em;
}
.know-journal-strong {
  color: #2395f1;
  cursor: pointer;
}
.know-journal-content {
  padding: 25px 5px 5px 40px;
  margin-left: 5px;
  border-left: 1px solid #f8f8f9;
  height: 100px;
}
.know-journal-content:hover {
  background-color: #f8f8f9;
  border-radius: 10px;
  box-shadow: 0 0 5px #f8f8f9;
  transition: all 0.5s;
}
.know-journal-content-user {
  color: #9a9a9a;
  margin-bottom: 5px;
  letter-spacing: 0.1em;
}
.know-journal-content-green {
  border-top: 1px solid #f0f0f0;
  text-indent: 1em;
  letter-spacing: 0.1em;
}
</style>
<style >
.ivu-scroll-container::-webkit-scrollbar {
  width: 8px;
  height: 8px;
}
.ivu-scroll-container::-webkit-scrollbar-thumb {
  background-color: #c5c8ce;
  border-radius: 5px;
}
</style>