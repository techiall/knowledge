/**
*
*  @author ZTiger
*
*/


<template>
  <Modal v-model="modalFlag" width="500" :mask-closable="false">
    <p slot="header" class="know-modal-header">
      <Icon type="ios-information-circle"></Icon>
      <span>&nbsp;&nbsp;删&nbsp;除&nbsp;节&nbsp;点</span>
    </p>
    <div class="know-modal-text">
      <p v-show="selectNodesFlag">
        你确定要&nbsp;&nbsp;
        <strong>删除</strong>&nbsp;&nbsp;此节点吗?
      </p>
      <p v-show="!selectNodesFlag">
        你确定要&nbsp;&nbsp;
        <strong>删除</strong>&nbsp;&nbsp;这些节点吗?
      </p>
      <p class="know-modal-text-center" v-show="selectNodesFlag">{{selectNodeName}}</p>
      <p
        class="know-modal-text-nodes"
        v-show="!selectNodesFlag"
        v-for="(item,index) in Nodes"
        :key="index"
      >{{index + 1}}.&nbsp;{{item}}</p>
    </div>
    <div slot="footer">
      <Button type="text" @click="modalFlag=false">取&nbsp;消</Button>
      <Button type="error" @click.stop="userDelfun">删&nbsp;除</Button>
    </div>
  </Modal>
</template>

<script>
export default {
  props: [
    'DelModalFlag',
    'selectNodeName',
    'treeNodeId',
    'selectNodes',
    'itemId',
  ],
  data() {
    return {
      // modal标志位
      modalFlag: false,
      // 多节点按下 标志位
      selectNodesFlag: false,
      // 多节点
      Nodes: '',
      // 多节点 id
      NodeId: '',
    };
  },
  watch: {
    //监听 treelist 传过来的标志符
    DelModalFlag() {
      this.modalFlag = true;
      let NodesLen = this.selectNodes.length;
      // 判断 是ctrl 按下 还是 单个节点
      if (NodesLen) {
        // 多节点
        let arr = [];
        let arrId = [];
        this.selectNodes.forEach((item) => {
          arr.push(item.name);
          arrId.push(item.id);
        });
        this.NodeId = arrId;
        this.Nodes = arr.sort(this.sortId);
        this.selectNodesFlag = false;
      } else {
        this.selectNodesFlag = true;
      }
    },
    // 监听 modalflag = true
    modalFlag(val) {
      if (val) {
        document.addEventListener('keyup', this.upCtrlEnter);
      } else {
        document.removeEventListener('keyup', this.upCtrlEnter);
      }
    },
  },
  methods: {
    //点击 删除 按键
    userDelfun() {
      this.modalFlag = false;
      if (this.selectNodesFlag) {
        let url = '/node/' + this.treeNodeId;
        let obj = {
          itemId: this.itemId,
        };
        this.delete_string(url, obj)
          .then((res) => {
            if (res.data) {
              this.$emit('addNameS', 8, Math.random());
            } else {
              this.$Message.error('删除失败');
            }
          })
          .catch(() => {});
      } else {
        let url = '/node?itemId=' + this.itemId;
        let obj = this.NodeId;
        this.delete_json(url, obj)
          .then(() => {
            this.$emit('addNameS', 12, Math.random());
          })
          .catch(() => {});
      }
    },
    //监听 ctrl + ender 按键 执行函数
    upCtrlEnter(e) {
      if (e.ctrlKey && e.keyCode == 13) {
        this.userDelfun();
      }
    },
    // 排序
    sortId(ObjA, ObjB) {
      let valA = ObjA.length;
      let valB = ObjB.length;
      if (valA < valB) return -1;
      else if (valA > valB) return 1;
      else return 0;
    },
  },
};
</script>

<style  scoped>
.know-modal-header {
  color: #f60;
  text-align: center;
  user-select: none;
}
.know-modal-text-center {
  text-align: center;
  font-size: 16px;
  font-weight: bold;
}
.know-modal-text-nodes {
  display: inline-block;
  margin-right: 10px;
}
.Tips {
  color: #c5c8ce;
}
</style>