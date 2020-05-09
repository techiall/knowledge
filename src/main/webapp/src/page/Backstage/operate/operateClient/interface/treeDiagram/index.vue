/**
*
*  @author ZTiger
*
*/


<template>
  <tree-chart :style="{height:SetHeight}" ref="treechart" />
</template>

<script>
import { getTree } from '@/api/node';
import treeChart from '@/components/treeChart';

export default {
  components: { treeChart },
  props: ['treeNode', 'showSelectNum', 'InnerHeight', 'spinShow'],
  data() {
    return {
      getDataFlag: false,
      //设置top高
      TopHeight: 100,
    };
  },
  methods: {
    // 获取服务器数据
    async getTreeData() {
      if (this.getDataFlag) return;
      this.$emit('update:spinShow', true);
      this.getDataFlag = true;
      const data = await getTree(this.treeNode.id);
      this.$emit('update:spinShow', false);
      this.$refs.treechart.handletreeData(data);
    },

    //设置 数据重新获取
    //从新获取数据
    setForce() {
      this.getDataFlag = false;
      if (this.showSelectNum !== 3) return;
      this.getTreeData();
    },
  },
  watch: {
    treeNode: {
      handler(newval, oldval) {
        if (newval === '' || newval.id === oldval.id) return;
        this.getDataFlag = false;
        if (this.showSelectNum === 3) {
          this.getTreeData();
        }
      },
      deep: true,
    },
    showSelectNum(val) {
      if (val !== 3) return;
      this.getTreeData();
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
</style>