/**
*
*  @author ZTiger
*
*/


<template>
  <div>
    <operate-view v-if="itemExitFlag" ref="operateView" :treeNode="treeNode" :itemId="itemId" />
    <inoperable-view v-else ref="inoperableView" />
  </div>
</template>


<script>
import { getNode } from '@/api/node';
import operateView from './operate.vue';
import inoperableView from './inoperable.vue';

export default {
  props: ['treeNode', 'showSelectNum', 'itemId', 'spinShow', 'itemExitFlag'],
  components: { operateView, inoperableView },
  data() {
    return {
      //请求数据标志位
      getDataFlag: false,
    };
  },
  watch: {
    treeNode: {
      handler(newval, oldval) {
        if (newval === '' || newval.id === oldval.id) return;
        this.getDataFlag = false;
        if (this.showSelectNum === 1) this.getServerData();
      },
      deep: true,
    },
    showSelectNum(val) {
      if (val !== 1) return;
      this.getServerData();
    },
  },
  methods: {
    // 获取服务器数据
    async getServerData() {
      if (this.getDataFlag) return;
      this.$emit('update:spinShow', true);
      this.getDataFlag = true;
      const data = await getNode(this.treeNode.id);
      if (this.itemExitFlag) {
        this.$refs.operateView.setdetails(data);
      } else {
        this.$refs.inoperableView.setdetails(data);
      }
      this.$emit('update:spinShow', false);
    },
  },
};
</script>


<style scoped>
</style>
