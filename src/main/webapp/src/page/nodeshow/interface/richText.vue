/**
*
*  @author ZTiger
*
*/


<template>
  <div class="know-searchshow-text" v-html="Nodecontent" />
</template>


<script>
import Prism from 'prismjs';

export default {
  props: ['showSelectType', 'nodeId', 'spinShow'],
  data() {
    return {
      // 获取数据 标志位
      getDataFlag: false,
      // 数据请求过标志位
      requestDataFlag: false,
      // 节点知识
      Nodecontent: '',
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
      let url = '/storage/text/' + this.nodeId;
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
        .catch((err) => {
          window.console.log(err);
        });
    },
    //处理富文本数据
    handlerTextData(data) {
      if (data) {
        this.Nodecontent = data;
        this.$nextTick(() => {
          Prism.highlightAll();
        });
      } else {
        this.Nodecontent = '作者没有再此节点编辑任何信息。';
      }
    },
  },
  watch: {
    showSelectType: {
      handler(val) {
        if (val !== 'text' || !this.nodeId) return;
        this.getforceData();
      },
    },
    nodeId(val) {
      if (!val) return;
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

<style scoped >
</style>