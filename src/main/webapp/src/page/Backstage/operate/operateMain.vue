/**
*
*  @author ZTiger
*
*/


<template>
  <div>
    <div class="know-operate-mian-header-title">名称&nbsp;:&nbsp;{{treeNodename}}</div>
    <div class="know-operate-mian-header-select">
      <selete-button
        ref="OperateSelectBu"
        :showClientFlag="showClientFlag"
        :itemExitFlag ='itemExitFlag'
        @oMainCallback="oMainCallback"
      ></selete-button>
    </div>
    <div class="know-operate-mian-content scroll" :style="{height:setTreeClientHeight}">
      <show-client
        ref="showclient"
        :InnerHeight="InnerHeight"
        :treeNode="treeNode"
        :showSelectNum="showSelectNum"
        :itemId="itemId"
        :itemExitFlag="itemExitFlag"
        @oMainCallback="oMainCallback"
        v-show="showClientFlag"
      ></show-client>
    </div>
  </div>
</template>

<script>
// 导入operaSelete 组件
import SeleteButton from "./operateSelete/SeleteButton";
// 导入 展示操作 界面
import showClient from "./operateClient/showClient";
export default {
  components: { SeleteButton, showClient },
  props: ["InnerHeight", "treeNode", "itemId", "itemExitFlag"],
  data() {
    return {
      //节点名称
      treeNodename: "",
      // 设置top高度
      TopHeight: 100,
      //展示 操作界面标志位
      showClientFlag: false,
      // 选择 视图
      showSelectNum: 0
    };
  },
  methods: {
    oMainCallback(type, val) {
      const statusMap = {
        // 选择 视图
        1: () => {
          this.showSelectNum = val;
        },
        // 名称 改变
        2: () => {
          this.$emit("MangageCallback", 2, val);
        },
        //  添加,删除,编辑 重新获取节点
        3: () => {
          this.$refs.showclient.SClientCallback(3, val);
        },
        // 名称 改变 details 发送数据
        4: () => {
          this.$refs.showclient.SClientCallback(5, val);
        }
      };
      statusMap[type]();
    }
  },
  watch: {
    treeNode: {
      handler: function(val) {
        if (val === "") {
          this.treeNodename = "";
          this.showSelectNum = 0;
          this.showClientFlag = false;
          return;
        }
        if (this.showSelectNum === 0) {
          this.$refs.OperateSelectBu.setSelectNum(1);
          this.showSelectNum = 1;
        }
        this.showClientFlag = true;
        this.treeNodename = val.name;
      },
      deep: true
    }
  },
  computed: {
    //设置 树 可视区 高度
    setTreeClientHeight() {
      return this.InnerHeight - this.TopHeight + "px";
    }
  }
};
</script>

<style  scoped>
.know-operate-mian-header-title {
  white-space: nowrap;
  text-overflow: ellipsis;
  overflow: hidden;
  font-weight: bold;
  font-size: 18px;
  line-height: 25px;
  padding: 5px 10px 3px 10px;
  height: 40px;
  line-height: 40px;
}
.know-operate-mian-header-select {
  height: 50px;
  padding: 0;
}
.know-operate-mian-content {
  background-color: #fff;
  box-shadow: 0 1px 10px rgba(56, 37, 37, 0.08);
  border-radius: 10px;
  padding: 0px;
  overflow: auto;
}
</style>