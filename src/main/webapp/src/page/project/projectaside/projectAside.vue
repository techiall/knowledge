/**
*
*  @author ZTiger
*
*/


<template>
  <ul class="project-aside-ul">
    <li class="project-aside-li" @click="SelectRow(0)" :class="{'select-li':selectLI === 0}">
      <Icon type="ios-home" />首页
    </li>
    <li class="project-aside-li" @click="SelectRow(1)" :class="{'select-li':selectLI === 1}">
      <Icon type="logo-buffer" />项目
    </li>
    <li class="project-aside-li" @click="SelectRow(2)" :class="{'select-li':selectLI=== 2}">
      <Icon type="md-share" />分享
    </li>
  </ul>
</template>


<script>
export default {
  data() {
    return {
      // 选中的 li
      selectLI: 0,
      // 路由
      router: ['', 'list', 'share'],
    };
  },
  watch: {
    $route: {
      handler() {
        this.InitAside();
      },
      immediate: true,
    },
  },
  mounted() {
    this.InitAside();
  },
  methods: {
    InitAside() {
      const path = this.$route.path.replace(/\/.+\//g, '');
      const index = this.router.indexOf(path);
      this.selectLI = index === -1 ? 0 : index;
    },
    // 设置选中行
    SelectRow(val) {
      const path = `/project/${this.router[val]}`;
      if (this.selectLI === val) {
        return;
      }
      this.selectLI = val;
      this.$router.push({
        path,
      });
    },
  },
};
</script>


<style scoped>
.project-aside-ul {
  list-style: none;
}
.project-aside-li {
  height: 44px;
  line-height: 44px;
  font-size: 14px;
  padding: 0 0 0 20px;
  border-radius: 4px;
  cursor: pointer;
}
.project-aside-li:hover {
  background: #f7f7f7;
}
.project-aside-li i {
  font-size: 25px;
  margin: 0 15px 0 0;
  color: #8c8c8c;
}
.project-aside-li.select-li {
  background: #e8f0fe;
  color: #1a73e8;
}
.select-li i {
  color: #1b9aee;
}
@media screen and (max-width: 1000px) {
  .project-aside-ul {
    display: flex;
  }
  .project-aside-li {
    padding: 0;
    flex-grow: 1;
    text-align: center;
  }
  .project-aside-li i {
    display: none;
  }
}
</style>