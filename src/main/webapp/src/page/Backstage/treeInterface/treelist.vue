/**
*
*  @author ZTiger
*
*/


<template>
  <div id="know-tree-list">
    <div class="know-tree-list-header-title">类层次结构</div>
    <div class="know-tree-header-select df" v-if="itemExitFlag">
      <div
        class="know-tree-header-button df"
        @click="TLCallback(4)"
        :class="{'know-tree-header-button-no-selete':ctrlButtonFlag}"
      >
        <!-- <Icon type="md-add-circle" size="20" /> -->
        <div class="g-action-add g-action"></div>
        <div class="g-button-tip">添加</div>
      </div>

      <div
        class="know-tree-header-button df"
        :class="{'know-tree-header-button-no-selete':!SeleteNodeFlag}"
        @click="TLCallback(5)"
      >
        <!-- <Icon type="ios-close-circle" size="20" /> -->
        <div class="g-action-del g-action"></div>
        <div class="g-button-tip">删除</div>
      </div>

      <div
        class="know-tree-header-button df"
        :class="{'know-tree-header-button-no-selete':!SeleteNodeFlag||ctrlButtonFlag}"
        @click="TLCallback(6)"
      >
        <!-- <Icon type="ios-create" size="22" /> -->
        <div class="g-action-exit g-action"></div>
        <div class="g-button-tip">编辑</div>
      </div>
    </div>
    <div v-else class="know-tree-header-select"></div>
    <Content class="know-tree-main-content scroll" :style="{height:setTreeClientHeight}">
      <tree-z @selectNode="TLCallback" :treelistVal="treeZ" :itemId="itemId" ref="treeZ"></tree-z>
    </Content>

    <modal-a
      :AddModalFlag="buttonAddFlag"
      :treeNode="treeNode"
      :itemId="itemId"
      @addNameS="TLCallback"
    ></modal-a>
    <modal-d
      :DelModalFlag="buttonDelFlag"
      :selectNodeName="selectNodeName"
      :treeNodeId="treeNode.id"
      :selectNodes="selectNodes"
      :itemId="itemId"
      @addNameS="TLCallback"
    ></modal-d>
    <modal-e
      :ExitModalFalg="buttonExitFlag"
      :selectNodeName="selectNodeName"
      :treeNodeId="treeNode.id"
      @ExitNameS="TLCallback"
    ></modal-e>
  </div>
</template>

<script>
// 导入 删除 modal
import modalD from './modalADE/modalD';
// 导入 添加 modal
import modalA from './modalADE/modalA';
// 导入 编辑 modeal
import modalE from './modalADE/modalE';
// 导入 树
import treeZ from './tree/treeJquery';
export default {
  components: { modalD, modalA, modalE, treeZ },
  props: ['InnerHeight', 'itemId', 'itemExitFlag'],
  data() {
    return {
      // ctrl 点击标志 位
      ctrlButtonFlag: false,
      // 点击 添加标志 位
      buttonAddFlag: '',
      // 点击 删除标志 位
      buttonDelFlag: '',
      // 点击 编辑标志 位
      buttonExitFlag: '',
      // 删除 节点
      selectNodeName: '',
      // 设置top高度
      TopHeight: 100,
      //  树节点 点击标志位
      SeleteNodeFlag: false,
      // 树节点 值\
      treeNode: '',
      //传给treeZ组件的值
      treeZ: {
        ExitName: '',
        addName: '',
        delName: '',
        delNodes: '',
      },
      // 根节点数量
      rootNodeNum: '',
      // 选择多个节点 id + name
      selectNodes: [],
    };
  },
  methods: {
    //组件 传来的值标志位
    TLCallback(type, val) {
      const statusMap = {
        1: () => {
          //tree 点击
          this.SeleteNodeFlag = val;
        },
        //获取点击节点的名称
        2: () => {
          let node = {
            name: val.name,
            id: val.id,
          };
          this.selectNodeName = val.name;
          this.treeNode = node;
          this.$emit('MangageCallback', 1, node);
        },
        //modal修改 触发
        3: () => {
          this.treeZ.ExitName = val;
          this.selectNodeName = val;
          this.$emit('MangageCallback', 3, Math.random());
        },
        // 点击 添加按键
        4: () => {
          if (!this.ctrlButtonFlag) this.buttonAddFlag = Math.random();
        },
        // 点击 删除 按键
        5: () => {
          if (this.SeleteNodeFlag) {
            this.buttonDelFlag = Math.random();
          }
        },
        // 点击 编辑 按键
        6: () => {
          if (this.SeleteNodeFlag && !this.ctrlButtonFlag) {
            this.buttonExitFlag = Math.random();
          }
        },
        // modal 创建 触发
        7: () => {
          this.treeZ.addName = val;
          this.$emit('MangageCallback', 3, Math.random());
        },
        // modal 删除 触发
        8: () => {
          this.selectNodeName = '';
          this.treeNode = '';
          this.$emit('MangageCallback', 1, '');
          this.$emit('MangageCallback', 3, Math.random());
          this.treeZ.delName = val;
        },
        //根节点 数量 获取
        9: () => {
          this.rootNodeNum = val;
        },
        //ctrl 按下标志位置
        10: () => {
          this.ctrlButtonFlag = true;
          this.SeleteNodeFlag = true;
          this.selectNodes = val;
        },
        //ctrl 抬起标志位置
        11: () => {
          this.ctrlButtonFlag = false;
          this.selectNodes = val;
        },
        //删除 多节点 标志位
        12: () => {
          this.treeZ.delNodes = val;
        },
        //删除 多节点 包括选中 treeNode
        13: () => {
          this.selectNodeName = '';
          this.treeNode = '';
          this.$emit('MangageCallback', 1, '');
          this.$emit('MangageCallback', 3, Math.random());
        },
        // 名称 改变 details发送
        14: () => {
          this.$emit('MangageCallback', 4, val);
        },
        // 树数据请求
        15: () => {
          this.$refs.treeZ.getTreeData();
        },
      };
      statusMap[type]();
    },
  },
  computed: {
    //设置 树 可视区 高度
    setTreeClientHeight() {
      return this.InnerHeight - this.TopHeight + 'px';
    },
  },
};
</script>

<style  scoped>
.ivu-layout-header {
  line-height: 0;
}
#know-tree-list {
  min-width: 300px;
}
.know-tree-list-header-title {
  text-align: center;
  font-weight: bold;
  font-size: 18px;
  line-height: 25px;
  padding: 5px 10px 3px 10px;
  height: 40px;
  line-height: 40px;
}
.know-tree-header-button-title {
  display: inline-block;
  height: 40px;
  line-height: 40px;
  font-size: 16px;
  text-indent: 0.4em;
}
.know-tree-header-select {
  height: 50px;
  padding: 0;
}
.know-tree-header-button {
  height: 40px;
  width: 80px;
  margin: 0 5px;
  border-radius: 5px;
  color: #505050;
  cursor: pointer;
}
.know-tree-header-button:hover {
  background-color: #dcdee2;
  transition: all 0.5s;
}
.know-tree-main-content {
  overflow: auto;
  padding: 10px;
  border-radius: 10px;
  box-sizing: border-box;
  box-shadow: 0 1px 10px rgba(56, 37, 37, 0.08);
  background: #fff;
}
.know-tree-header-button-no-selete {
  color: #a0a0a0;
}
.know-tree-header-button-no-selete:hover {
  color: #a0a0a0;
}
.g-action {
  height: 15px;
  width: 15px;
}
.g-action-add {
  background: url('../../../assets/images/add.png') no-repeat 100%/100%;
}
.g-action-del {
  background: url('../../../assets/images/delete.png') no-repeat 100%/100%;
}
.g-action-exit {
  background: url('../../../assets/images/exit.png') no-repeat 100%/100%;
}
.g-button-tip {
  margin: 0 0 0 8px;
  font-size: 16px;
}
</style>