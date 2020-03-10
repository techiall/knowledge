/**
*
*  @author ZTiger
*
*/


<template>
  <Modal v-model="modalFlag" width="500" :mask-closable="false">
    <p slot="header" class="know-modal-header">
      <Icon type="ios-information-circle"></Icon>
      <span>&nbsp;&nbsp;添&nbsp;加&nbsp;节&nbsp;点</span>
    </p>
    <div class="know-modal-text">
      <p>创建名称&nbsp;:</p>
      <p>
        <Input v-model="inputName" type="text" ref="modalAddInput" />
      </p>
      <p class="Tips">
        按&nbsp;
        <strong>ctrl&nbsp;+&nbsp;enter</strong>&nbsp;接受并关闭面板
      </p>
    </div>
    <div slot="footer">
      <Button type="text" @click="modalFlag=false">取&nbsp;消</Button>
      <Button type="primary" @click.stop="userAddfun">添&nbsp;加</Button>
    </div>
  </Modal>
</template>

<script>
export default {
  props: ['AddModalFlag', 'treeNode', 'itemId'],
  data() {
    return {
      // modal标志位
      modalFlag: false,
      // input 输入的名称
      inputName: '',
    };
  },
  methods: {
    // 点击 添加 节点
    userAddfun() {
      this.modalFlag = false;
      let name = this.inputName.replace(/^\s+|\s+$/g, '');
      if (name === '') {
        return;
      }
      let message = '添加节点，节点名称为 [' + name + ']';
      let obj = {
        name,
        parentId: this.treeNode === '' ? null : this.treeNode.id,
        itemId: this.itemId,
        record: {
          message: JSON.stringify({ message, name }),
          operator: 'ADD_NODE',
        },
      };
      this.post_json('/node', obj)
        .then((res) => {
          if (res.data.new) {
            this.$emit('addNameS', 7, res.data.node);
          } else {
            this.$Message.warning({
              content: '您添加的节点已经存在,请不要重复添加',
              duration: 5,
            });
          }
        })
        .catch(() => {});
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

<style  scoped>
.know-modal-header {
  text-align: center;
  color: #2d8cf0;
}
.Tips {
  color: #c5c8ce;
}
</style>