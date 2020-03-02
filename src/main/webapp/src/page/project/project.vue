/**
*
*  @author ZTiger
*
*/


<template>
  <div class="project" v-title="知识图谱构建平台">
    <public-header :RouterFlag="true"></public-header>
    <div class="project-content" :style="{height:ClientHeight}">
      <project-aside class="project-aside"></project-aside>
      <project-main class="project-main"></project-main>
    </div>
  </div>
</template>


<script>
//导入 header 模板
import publicHeader from '../../components/publicHeader';
import projectAside from './projectaside/projectAside';
import projectMain from './projectmain/projectMain';
import { mapState } from 'vuex';

export default {
  components: { publicHeader, projectAside, projectMain },
  data() {
    return {
      InnerHeight: '',
      InnerWidth: '',
    };
  },
  computed: {
    ...mapState(['headerHeight']),
    // 可视区高度
    ClientHeight() {
      return this.InnerHeight - this.headerHeight + 'px';
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
  },
};
</script>


<style scoped>
.project-content {
  display: flex;
  overflow: auto;
}
.project-aside {
  flex-shrink: 0;
  width: 280px; 
  padding: 30px 0 30px 30px;
  margin: 0 10px 0 0;
}
.project-main {
  flex-grow: 1;
}
@media screen and (max-width: 1000px) {
  .project-content {
    flex-direction: column;
  }
  .project-aside {
    width: 100%;
    margin: 0;
    padding:  30px 30px 0;
  }
}
</style>
