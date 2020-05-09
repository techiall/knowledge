/**
*
*  @author ZTiger
*
*/


<template>
  <Modal
    v-model="modalFlag"
    width="500"
    :mask-closable="false"
    :footer-hide="true"
    class="b-modal-wrap"
  >
    <div class="b-modal-header color-red">
      <Icon type="ios-information-circle" />
      <span>删除节点</span>
    </div>
    <div class="b-modal-input-wrap">
      <div>
        <span>你确定要</span>
        <strong class="b-modal-span color-red">删除</strong>
        <span>{{selectNodesFlag?'此':'这些'}}节点吗?</span>
      </div>
      <div>节点名称：</div>
      <div v-if="selectNodesFlag" class="g-text center">{{selectNodeName}}</div>
      <div v-else class="g-nodes-wrap">
        <div v-for="(item) in Nodes" :key="item" class="g-nodes-row">{{item}}</div>
      </div>
    </div>
    <div class="b-modal-footer">
      <Button type="text" @click="modalFlag=false">取 消</Button>
      <Button type="error" :loading="loadingFlag" @click.stop="userDelfun">删 除</Button>
    </div>
  </Modal>
</template>

<script>
import { delNode, delNodes } from '@/api/node';

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
      Nodes: [],
      // 多节点 id
      NodeId: [],
      // 设置按钮为加载中状态
      loadingFlag: false,
    };
  },
  watch: {
    //监听 treelist 传过来的标志符
    DelModalFlag() {
      this.modalFlag = true;
      this.loadingFlag = false;
      this.Nodes = [];
      this.NodeId = [];
      const NodesLen = this.selectNodes.length;
      if (NodesLen) {
        this.selectNodes.forEach((item) => {
          this.Nodes.push(item.name);
          this.NodeId.push(item.id);
        });
        this.Nodes.sort(this.sortId);
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
    async userDelfun() {
      this.loadingFlag = true;
      if (this.selectNodesFlag) {
        const data = await delNode(this.treeNodeId, { itemId: this.itemId });
        if (data) {
          this.$emit('addNameS', 8, Math.random());
          this.modalFlag = false;
        } else {
          this.$Message.error('删除失败');
          this.loadingFlag = false;
        }
      } else {
        const data = await delNodes(this.itemId, this.NodeId);
        if (data) {
          this.$emit('addNameS', 12, Math.random());
          this.modalFlag = false;
        } else {
          this.$Message.error('删除失败');
          this.loadingFlag = false;
        }
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
<style scoped>
.g-text {
  font-size: 14px;
}
.g-nodes-wrap {
  display: flex;
  flex-wrap: wrap;
  counter-reset: num;
}
.g-nodes-row {
  margin: 5px 5px 5px 0;
  color: #d63447;
}
.g-nodes-row:before {
  counter-increment: num;
  content: counter(num) '. ';
  color: #000;
}
</style>