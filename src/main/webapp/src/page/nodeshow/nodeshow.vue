/**
*
*  @author ZTiger
*
*/

<template>
  <div class="g-node-show">
    <search-header />
    <div class="g-content" :style="{'height':clientHeight}">
      <div class="g-node-show-titlte">
        <span>{{nodeName}}</span>
      </div>
      <node-select />
      <node-content :InnerHeight="InnerHeight - topHeight" :nodeId="nodeId" />
    </div>
  </div>
</template>

<script>
import { mapGetters } from 'vuex';
// 导入头
import searchHeader from '@/components/searchHeader.vue';
import nodeSelect from './nodeshowButton.vue';
import nodeContent from './nodeContent.vue';
export default {
  components: { searchHeader, nodeSelect, nodeContent },
  data() {
    return {
      // 节点名称
      nodeName: '',
      // 选中的节点 id
      nodeId: '',
      // 获取 innerHeight
      InnerHeight: '',
      // 获取 innerWidth
      InnerWidth: '',
      // 路由跳转地址
      routerTO: '/project',
    };
  },
  computed: {
    ...mapGetters({
      topHeight: 'getTopHeigt',
    }),
    clientHeight() {
      return `${this.InnerHeight - this.topHeight}px`;
    },
  },
  watch: {
    '$route.params.id': {
      async handler(NodeID) {
        this.nodeId = '';
        document.title = `Knowledge Graph`;
        const nodeID = parseInt(NodeID, 10);
        const URL = `/node/${nodeID}`;
        try {
          const { data } = await this.get(URL);
          this.nodeName = data.name;
          this.nodeId = nodeID;
          this.setTitleLabel(data.name);
        } catch {
          this.$router.push('/404');
        }
      },
      immediate: true,
    },
  },
  mounted() {
    this.InnerHeight = window.innerHeight;
    this.InnerWidth = window.innerWidth;
    window.addEventListener('resize', this.getInner);
  },
  beforeDestroy() {
    window.removeEventListener('resize', this.getInner);
  },
  methods: {
    //获取 浏览器 高度
    getInner() {
      this.InnerHeight = window.innerHeight;
      this.InnerWidth = window.innerWidth;
    },
    // 设置title标签
    setTitleLabel(title) {
      document.title = `${title} ● Knowledge Graph`;
    },
  },
};
</script>

<style  scoped>
.g-content {
  background-color: #f0f2f5;
}
.g-node-show {
  overflow: hidden;
}
.g-node-show-titlte {
  overflow: hidden;
  padding: 0px 10px;
  margin: 0 10px;
  height: 40px;
  font-weight: bold;
  font-size: 18px;
  line-height: 40px;
  white-space: nowrap;
  text-overflow: ellipsis;
}
.g-tip-icon {
  margin: 0 10px;
}
.g-router-a {
  color: #515a6e;
}
</style>