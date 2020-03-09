/**
*
*  @author ZTiger
*
*/


<template>
  <Modal v-model="modalFlag" width="400">
    <div slot="header" class="item-modal-header center">
      <span>项目创建</span>
    </div>
    <div class="item-modal-main">
      <div class="center">
        <img src="../../../../assets/images/item.png" class="main-img" />
      </div>
      <div class="item-modal-title">项目名称 (必填) :</div>
      <Input
        v-model="submitMsg.name"
        placeholder="项目名称"
        ref="NameI"
        @on-change="itemNameChange"
        maxlength="20"
      />
      <div class="item-modal-title">项目描述 (选填) :</div>
      <Input
        class="textarea-i"
        v-model="submitMsg.description"
        type="textarea"
        :autosize="{minRows: 2,maxRows: 6}"
        maxlength="200"
        show-word-limit
        placeholder="项目描述..."
      />
      <div class="item-modal-title">是否分享 :</div>
      <RadioGroup v-model="submitMsg.share">
        <Radio label="true">
          <span>是</span>
        </Radio>
        <Radio label="false">
          <span>否</span>
        </Radio>
      </RadioGroup>
    </div>
    <div slot="footer" class="item-modal-footer">
      <Button type="text" @click="closeModal">取消</Button>
      <Button type="primary" @click="pushServer" :disabled="allowFlag" :loading="serveLoadFlag">
        <span v-if="!serveLoadFlag">完成并创建</span>
        <span v-else>创建中</span>
      </Button>
    </div>
  </Modal>
</template>


<script>
export default {
  data() {
    return {
      // 显示modal 标志位
      modalFlag: false,
      // 向服务器上传信息
      submitMsg: {
        share: "false",
        name: "",
        description: ""
      },
      // 允许发送标志位
      allowFlag: true,
      // 向服务器发送 标志位
      serveLoadFlag: false
    };
  },
  methods: {
    // 设置状态
    setModalStatus() {
      this.modalFlag = true;
      this.submitMsg = {
        share: "false",
        name: "",
        description: ""
      };
      this.$nextTick(() => {
        this.$refs.NameI.focus();
      });
    },
    // 关闭按键
    closeModal() {
      this.modalFlag = false;
    },
    // 项目名称 change事件触发
    itemNameChange() {
      const name = this.submitMsg.name.replace(/^\s+|\s+$/g, "");
      if (name) {
        this.allowFlag = false;
      } else {
        this.allowFlag = true;
      }
    },
    // 向服务器发送数据
    pushServer() {
      const url = "/item";
      const obj = {
        share: this.submitMsg.share === "true" ? true : false,
        name: this.submitMsg.name,
        description: this.submitMsg.description
      };
      this.serveLoadFlag = true;
      this.post_json(url, obj)
        .then(res => {
          this.$emit("addItem", res.data);
          this.$Message.success("创建成功");
          this.modalFlag = false;
          this.serveLoadFlag = false;
        })
        .catch(() => {
          this.serveLoadFlag = false;
        });
    }
  }
};
</script>


<style scoped>
.item-modal-box {
  overflow: hidden;
}
.item-modal-header {
  font-size: 18px;
  font-weight: 600;
  letter-spacing: 0.1em;
}
.item-modal-title {
  margin: 20px 0 5px;
  font-weight: 600;
  letter-spacing: 0.1em;
}
.main-img {
  width: 100px;
  height: 100px;
  user-select: none;
}

.ivu-radio-default {
  margin-right: 20px;
}
.item-modal-footer button {
  margin-left: 20px;
}
</style>