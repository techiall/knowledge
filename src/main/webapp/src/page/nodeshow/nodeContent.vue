/**
*
*  @author ZTiger
*
*/


<template>
  <div class="know-selectid-content scroll" :style="{height:setClientHeight}">
    <Spin size="large" fix v-show="spinShow" />
    <force-diagram
      v-show="showSelectType === 'force'"
      :spinShow.sync="spinShow"
      :InnerHeight="InnerHeight"
      :showSelectType="showSelectType"
      :nodeId="nodeId"
    />
    <tree-diagram
      v-show="showSelectType==='tree'"
      :spinShow.sync="spinShow"
      :InnerHeight="InnerHeight"
      :showSelectType="showSelectType"
      :nodeId="nodeId"
    />
    <rich-text
      v-show="showSelectType==='text'"
      :spinShow.sync="spinShow"
      :InnerHeight="InnerHeight"
      :showSelectType="showSelectType"
      :nodeId="nodeId"
    />
  </div>
</template>


<script>
//导入力导图
import forceDiagram from './interface/forceDiagram';
//导入 树图
import treeDiagram from './interface/treeDiagram';
//导入 富文本
import richText from './interface/richText';

export default {
  components: { forceDiagram, treeDiagram, richText },
  props: ['InnerHeight', 'nodeId'],
  data() {
    return {
      // 设置top高
      TopHeight: 100,
      // 加载完成展示 标志位,
      spinShow: true,
      // 展示
      showSelectType: '',
    };
  },
  computed: {
    //设置  可视区 高度
    setClientHeight() {
      return this.InnerHeight - this.TopHeight + 'px';
    },
  },
  watch: {
    '$route.query': {
      handler(query) {
        this.spinShow = true;
        this.showSelectType = query.type;
      },
      immediate: true,
    },
  },
};
</script>


<style scoped>
.know-selectid-content {
  position: relative;
  padding: 10px;
  overflow: auto;
  min-width: 900px;
  background-color: #fff;
  border-radius: 10px;
}
</style>