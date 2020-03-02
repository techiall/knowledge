/**
*
*  @author ZTiger
*
*/


<template>
  <div class="g-search-warp" v-title="知识图谱节点搜索">
    <search-header routerKey="q" />
    <search-content class="g-content scroll" />
  </div>
</template>


<script>
import searchHeader from '@/components/searchHeader.vue';
import searchContent from './searchContent/searchContent';

export default {
  components: { searchHeader, searchContent },
  data() {
    return {};
  },
};
</script>


<style scoped>
.g-search-warp {
  height: 100vh;
  overflow-y: hidden;
}
.g-content {
  height: calc(100% - 60px);
  overflow: auto;
}
</style>
