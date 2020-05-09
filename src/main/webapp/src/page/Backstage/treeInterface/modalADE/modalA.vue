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
    <div class="b-modal-header color-blue">
      <Icon type="ios-information-circle" />
      <span>添加节点</span>
    </div>
    <div class="b-modal-input-wrap">
      <div class="b-modal-input-row">节点名称 :</div>
      <div class="b-modal-input-row">
        <Input v-model="inputName" type="text" ref="modalAddInput" />
      </div>
      <div class="b-modal-input-row b-modal-tips">
        <span>按</span>
        <strong class="b-modal-span">ctrl + enter</strong>
        <span>接受并关闭面板</span>
      </div>
    </div>
    <div class="b-modal-footer">
      <Button type="text" @click="modalFlag=false">取 消</Button>
      <Button type="primary" :loading="loadingFlag" @click.stop="userAddfun">添 加</Button>
    </div>
  </Modal>
</template>

<script>
import { createNode } from '@/api/node';

export default {
  props: ['AddModalFlag', 'treeNode', 'itemId'],
  data() {
    return {
      // modal标志位
      modalFlag: false,
      // input 输入的名称
      inputName: '',
      // 设置按钮为加载中状态
      loadingFlag: false,
    };
  },
  methods: {
    // 点击 添加 节点
    async userAddfun() {
      const name = this.inputName.replace(/\s+/g, '');
      if (!name) return;
      this.loadingFlag = true;
      const message = `添加节点，节点名称为 [${name}]`;
      const params = {
        name,
        parentId: this.treeNode ? this.treeNode.id : null,
        itemId: this.itemId,
        record: {
          message: JSON.stringify({ message, name }),
          operator: 'ADD_NODE',
        },
      };
      const data = await createNode(params);
      if (data.new) {
        this.$emit('addNameS', 7, data.node);
        this.modalFlag = false;
      } else {
        this.$Message.warning({
          content: '请勿重复添加相同名称节点',
          duration: 5,
        });
        this.loadingFlag = false;
      }
    },
    //监听 ctrl + ender 按键 执行函数
    upCtrlEnter(e) {
      if (e.ctrlKey && e.keyCode == 13) {
        this.userAddfun();
      }
    },
  },
  watch: {
    // 监听 treelist 事件
    AddModalFlag() {
      this.modalFlag = true;
      this.inputName = '';
      this.loadingFlag = false;
      this.$nextTick(() => {
        this.$refs.modalAddInput.focus();
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

