/**
*
*  @author ZTiger
*
*/


<template>
  <force-chart :style="{height:SetHeight}" ref="forcechart" />
</template>

<script>
// 力导向组件
import forceChart from '@/components/forceChart';
export default {
  components: { forceChart },
  props: ['treeNode', 'showSelectNum', 'InnerHeight', 'spinShow'],
  data() {
    return {
      // 获取数据 标志位
      getDataFlag: false,
      //设置top高
      TopHeight: 100,
    };
  },
  methods: {
    // 获取 服务器 数据
    getforceData() {
      if (this.getDataFlag) return;
      this.$emit('update:spinShow', true);
      this.getDataFlag = true;
      let url = '/node/' + this.treeNode.id + '/graph';
      this.get(url)
        .then((res) => {
          this.$emit('update:spinShow', false);
          if (this.showSelectNum === 2) {
            this.$refs.forcechart.handlecomponentsforceData(res.data);
          }
        })
        .catch(() => {});
    },
    //从新获取数据
    setForce() {
      this.getDataFlag = false;
      if (this.showSelectNum !== 2) return;
      this.getforceData();
    },
  },
  watch: {
    treeNode: {
      handler(newval, oldval) {
        if (newval === '' || newval.id === oldval.id) return;
        this.getDataFlag = false;
        if (this.showSelectNum === 2) {
          this.getforceData();
        }
      },
      deep: true,
    },
    showSelectNum(val) {
      if (val !== 2) return;
      this.getforceData();
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

<style  scoped>
.know-force {
  height: 100%;
}
</style>