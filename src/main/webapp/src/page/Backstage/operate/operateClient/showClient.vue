/**
*
*  @author ZTiger
*
*/


<template>
  <div class="know-Showclient">
    <Spin fix size="large" v-if="spinShow"></Spin>
    <details-show
      v-show="showSelectNum === 1"
      ref="detailsShow"
      :itemId="itemId"
      :treeNode="treeNode"
      :showSelectNum="showSelectNum"
      :spinShow.sync="spinShow"
      :itemExitFlag="itemExitFlag"
      @SClientCallback="SClientCallback"
      class="know-Showclient-default"
    />
    <force-diagram
      v-show="showSelectNum === 2"
      ref="forcediagram"
      :InnerHeight="InnerHeight"
      :treeNode="treeNode"
      :showSelectNum="showSelectNum"
      :spinShow.sync="spinShow"
      class="know-Showclient-default"
    />
    <tree-diagram
      v-show="showSelectNum === 3"
      ref="treediagram"
      :InnerHeight="InnerHeight"
      :treeNode="treeNode"
      :showSelectNum="showSelectNum"
      :spinShow.sync="spinShow"
      class="know-Showclient-default"
    />
    <journal-show
      v-show="showSelectNum === 4"
      v-if="itemExitFlag"
      ref="journalog"
      :treeNode="treeNode"
      :showSelectNum="showSelectNum"
      :InnerHeight="InnerHeight"
      :spinShow.sync="spinShow"
      class="know-Showclient-default"
    />
    <rich-text
      v-show="showSelectNum === 5"
      :InnerHeight="InnerHeight"
      :treeNode="treeNode"
      :showSelectNum="showSelectNum"
      :spinShow.sync="spinShow"
      :itemExitFlag="itemExitFlag"
    />
  </div>
</template>

<script>
//导入详情界面
import detailsShow from './interface/details/index.vue';
//导入 日志
import journalShow from './interface/journal/index.vue';
//导入 树图
import treeDiagram from './interface/treeDiagram/index.vue';
//导入力导图
import forceDiagram from './interface/forceDiagram/index.vue';
//导入富文本
import richText from './interface/richText/index.vue';

export default {
  props: ['treeNode', 'showSelectNum', 'InnerHeight', 'itemId', 'itemExitFlag'],
  components: { detailsShow, journalShow, treeDiagram, forceDiagram, richText },
  data() {
    return {
      // 加载 信息 动画
      spinShow: false,
    };
  },
  watch: {},
  methods: {
    SClientCallback(type, val) {
      const statusMap = {
        // 名称 改变
        1: () => {
          this.$emit('oMainCallback', 2, val);
          window.console.log(val);
          this.$refs.forcediagram.setForce();
          this.$refs.treediagram.setForce();
        },
        // 关系节点添加
        2: () => {
          this.$refs.forcediagram.setForce();
        },
        //  添加,删除,编辑 重新获取节点
        3: () => {
          this.$refs.forcediagram.setForce();
          this.$refs.treediagram.setForce();
        },
        // 添加属性 重新获取日志
        4: () => {
          this.$refs.journalog.setJournal();
        },
        // 名称 改变 details 发送数据
        5: () => {
          this.$refs.detailsShow.setNodeName(val);
        },
      };
      statusMap[type]();
    },
  },
};
</script>

<style  scoped>
.know-Showclient {
  height: 100%;
  position: relative;
}
.know-Showclient-default {
  padding: 10px;
}
</style>