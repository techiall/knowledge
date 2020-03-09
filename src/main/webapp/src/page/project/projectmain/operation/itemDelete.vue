/**
*
*  @author ZTiger
*
*/


<template>
  <Modal v-model="delmodal" width="400" :closable="false" footer-hide>
    <div class="g-del-title">
      <span>删除 {{ItemName}} 项目</span>
    </div>
    <div style="text-align:center;">
      <p>您确定要删除此项目吗?</p>
    </div>
    <div class="g-footer">
      <Button type="text" @click="delmodal = false">取消</Button>
      <Button type="error" @click="delItem" :loading="serveLoadFlag">
        <span v-if="!serveLoadFlag">删除</span>
        <span v-else>删除中</span>
      </Button>
    </div>
  </Modal>
</template>


<script>
export default {
  data() {
    return {
      // 删除模态框标志位
      delmodal: false,
      // 项目名称
      ItemName: '',
      // 项目 id
      ItemId: '',
      // 向服务器发送 标志位
      serveLoadFlag: false,
    };
  },
  methods: {
    showView(val) {
      this.delmodal = true;
      this.ItemName = val.name;
      this.ItemId = val.id;
    },
    // 删除项目
    delItem() {
      const url = '/item/' + this.ItemId;
      this.serveLoadFlag = true;
      this.delete_string(url)
        .then(() => {
          this.$Message.success('删除成功');
          this.delmodal = false;
          this.serveLoadFlag = false;
          this.$emit('delItem');
        })
        .catch(() => {
          this.serveLoadFlag = false;
        });
    },
  },
};
</script>


<style scoped>
.g-del-title{
  padding: 4px 0px 20px;
  font-size: 16px;
  color: #000;
}
.g-footer{
  padding: 20px 0px 4px;
  text-align: right;
}
</style>