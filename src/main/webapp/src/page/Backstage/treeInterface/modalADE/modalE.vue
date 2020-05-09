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
    <div class="b-modal-header">
      <Icon type="ios-information-circle" />
      <span>编辑节点</span>
    </div>
    <div class="b-modal-input-wrap">
      <div class="b-modal-input-row">
        <span>编辑</span>
        <strong class="b-modal-span">{{selectNodeName|strsubNodeName}}</strong>
        <span>节点</span>
      </div>
      <div class="b-modal-input-row">
        <Input v-model="changeName" type="text" element-id="modalExitInput" />
      </div>
      <div class="b-modal-input-row b-modal-tips">
        <span>按</span>
        <strong class="b-modal-span">ctrl + enter</strong>
        <span>接受并关闭面板</span>
      </div>
    </div>
    <div class="b-modal-footer">
      <Button type="text" @click="modalFlag=false">取 消</Button>
      <Button type="primary" :loading="loadingFlag" @click.stop="userExitfun">编 辑</Button>
    </div>
  </Modal>
</template>

<script>
import { queryNode, updateNode } from '@/api/node';

export default {
  props: ['ExitModalFalg', 'selectNodeName', 'treeNodeId', 'itemId'],
  filters: {
    strsubNodeName(name) {
      if (name.length > 20) {
        return `${name.substr(0, 20)}...`;
      }
      return name;
    },
  },
  data() {
    return {
      // modal标志位
      modalFlag: false,
      //改变的名称
      changeName: '',
      // 设置按钮为加载中状态
      loadingFlag: false,
    };
  },
  methods: {
    // 点击 编辑  上传 节点
    async userExitfun() {
      const name = this.changeName.replace(/^\s+|\s+$/g, '');
      if (this.selectNodeName === name || !name) return;
      this.loadingFlag = true;
      const itemId = this.itemId;
      const queryData = await queryNode({ query: name, itemId });
      if (queryData.length > 0 && queryData[0].name === name) {
        this.$Message.warning({
          content: `${name}节点已存在,请重新修改`,
          duration: 5,
        });
        this.loadingFlag = false;
        return;
      }
      const message = `[${this.selectNodeName}] 节点名称修改为 [${name}]`;
      const params = {
        name,
        itemId: this.itemId,
        record: {
          message: JSON.stringify({ message, name }),
          operator: 'UPDATE_NODE_NAME',
        },
      };
      await updateNode(this.treeNodeId, params);
      this.modalFlag = false;
      this.$emit('ExitNameS', 14, name);
    },
    //监听 ctrl + ender 按键 执行函数
    upCtrlEnter(e) {
      if (e.ctrlKey && e.keyCode == 13) {
        this.userExitfun();
      }
    },
  },
  watch: {
    // 监听 treelist 事件
    ExitModalFalg() {
      this.modalFlag = true;
      this.changeName = this.selectNodeName;
      this.loadingFlag = false;
      this.$nextTick(() => {
        let IputDom = document.querySelector('#modalExitInput');
        IputDom.focus();
        IputDom.setSelectionRange(0, this.selectNodeName.length);
      });
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
};
</script>

<style  scoped>
.know-modal-header {
  text-align: center;
}
.Tips {
  color: #c5c8ce;
}
</style>