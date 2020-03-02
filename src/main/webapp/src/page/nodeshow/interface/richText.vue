/**
*
*  @author ZTiger
*
*/


<template>
  <div class="know-searchshow-text" ref="eixtText"></div>
</template>


<script>
export default {
  props: ['showSelectType', 'nodeId', 'spinShow'],
  data() {
    return {
      // 获取数据 标志位
      getDataFlag: false,
      // 数据请求过标志位
      requestDataFlag: false,
    };
  },
  methods: {
    // 获取 服务器 数据
    getforceData() {
      if (this.requestDataFlag) {
        this.$emit('update:spinShow', false);
      }
      if (this.getDataFlag) {
        return;
      }
      this.getDataFlag = true;
      let url = 'storage/text/' + this.nodeId;
      this.get(url)
        .then((res) => {
          if (this.showSelectType === 'text') {
            this.requestDataFlag = true;
            this.$emit('update:spinShow', false);
            this.handlerTextData(res.data);
          } else {
            this.getDataFlag = false;
          }
        })
        .catch(() => {});
    },
    //处理富文本数据
    handlerTextData(data) {
      if (data) {
        this.$refs.eixtText.innerHTML = data;
      } else {
        this.$refs.eixtText.innerHTML = '作者没有再此节点编辑任何信息。';
      }
    },
  },
  watch: {
    showSelectType: {
      handler(val) {
        if (val !== 'text') return;
        this.getforceData();
      },
      // 代表在wacth里声明了firstName这个方法之后立即先去执行handler方法
      immediate: true,
    },
    nodeId() {
      this.requestDataFlag = false;
      this.getDataFlag = false;
      this.$emit('update:spinShow', true);
      if (this.showSelectType === 'text') {
        this.getforceData();
      }
    },
  },
};
</script>


<style scoped>
</style>