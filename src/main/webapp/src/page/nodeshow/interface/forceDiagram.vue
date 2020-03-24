/**
*
*  @author ZTiger
*
*/


<template>
  <force-chart
    :style="{height:SetHeight}"
    :RightWeight="RightWeight"
    ref="forcechart"
    @callback="forceCallback"
  ></force-chart>
</template>


<script>
import forceChart from '../../../components/forceChart';
export default {
  components: { forceChart },
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
      let url = '/node/' + this.nodeId + '/graph';
      this.get(url)
        .then((res) => {
          if (this.showSelectType === 'force') {
            this.requestDataFlag = true;
            this.$emit('update:spinShow', false);
            this.$refs.forcechart.handlecomponentsforceData(res.data);
          } else {
            this.getDataFlag = false;
          }
        })
        .catch(() => {});
    },
    forceCallback(d) {
      if (d.Tid === this.nodeId) return;
      this.$router.push({
        path: `/node/${d.Tid}`,
        query: {
          type: 'text',
        },
      });
    },
  },
  watch: {
    showSelectType: {
      handler(val) {
        if (val !== 'force' || !this.nodeId) return;
        this.getforceData();
      },
    },
    nodeId(val) {
      if (!val) return;
      this.getDataFlag = false;
      this.requestDataFlag = false;
      this.$emit('update:spinShow', true);
      if (this.showSelectType === 'force') {
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