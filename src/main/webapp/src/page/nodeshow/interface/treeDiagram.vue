/**
*
*  @author ZTiger
*
*/


<template>
  <tree-chart
    :TopHeight="TopHeight"
    :RightWeight="RightWeight"
    :style="{height:SetHeight}"
    ref="treechart"
  ></tree-chart>
</template>


<script>
import treeChart from '../../../components/treeChart';
export default {
  components: { treeChart },
  props: ['showSelectType', 'InnerHeight', 'nodeId', 'spinShow'],
  data() {
    return {
      // 获取数据 标志位
      getDataFlag: false,
      //设置top高
      TopHeight: 120,
      //设置right宽度
      RightWeight: 0,
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
      let url = `/node/${this.nodeId}/link`;
      this.get(url)
        .then((res) => {
          if (this.showSelectType === 'tree') {
            this.requestDataFlag = true;
            this.$emit('update:spinShow', false);
            this.$refs.treechart.handletreeData(res.data);
          } else {
            this.getDataFlag = false;
          }
        })
        .catch(() => {});
    },
  },
  watch: {
    showSelectType: {
      handler(val) {
        if (val !== 'tree' || !this.nodeId) return;
        this.getforceData();
      },
    },
    nodeId(val) {
      if (!val) return;
      this.requestDataFlag = false;
      this.getDataFlag = false;
      this.$emit('update:spinShow', true);
      if (this.showSelectType === 'tree') {
        this.getforceData();
      }
    },
  },
  computed: {
    //设置  可视区 高度
    SetHeight() {
      return this.InnerHeight - this.TopHeight + 'px';
    },
  },
};
</script>


<style scoped>
</style>