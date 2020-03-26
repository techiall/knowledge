/**
*
*  @author ZTiger
*
*/


<template>
  <div class="know-searchshow-select">
    <div
      class="know-searchshow-Button cup"
      :class="{'know-searchshow-Button-select':selectType === 'force'}"
      @click="clickButton('force',selectType)"
    >
      <i class="icon-lidaotu iconfont know-searchshow-Button-icon"></i>
      <div class="know-searchshow-Button-titlt">力导图</div>
    </div>
    <div
      class="know-searchshow-Button cup"
      :class="{'know-searchshow-Button-select':selectType === 'tree'}"
      @click="clickButton('tree',selectType)"
    >
      <i class="icon-shutu iconfont know-searchshow-Button-icon"></i>
      <div class="know-searchshow-Button-titlt">树图</div>
    </div>
    <div
      class="know-searchshow-Button cup"
      :class="{'know-searchshow-Button-select':selectType === 'text'}"
      @click="clickButton('text',selectType)"
    >
      <Icon type="ios-paper-outline " size="20" class="know-searchshow-Button-icon" />
      <div class="know-searchshow-Button-titlt">节点知识</div>
    </div>
  </div>
</template>


<script>
export default {
  data() {
    return {
      // 选择类型
      selectType: '',
    };
  },
  watch: {
    '$route.query': {
      handler(query) {
        const showType = ['text', 'force', 'tree'];
        const index = showType.indexOf(query.type);
        if (index !== -1) {
          this.selectType = query.type;
        } else {
          this.$router.push({
            query: {
              type: 'force',
            },
          });
        }
      },
      immediate: true,
    },
  },
  methods: {
    // 按钮点击事件
    clickButton(newval, oldval) {
      if (newval === oldval) return;
      this.selectType = newval;
      this.$router.push({
        query: {
          type: newval,
        },
      });
    },
  },
};
</script>


<style scoped>
.know-searchshow-select {
  height: 50px;
  display: flex;
  justify-content: flex-start;
  align-items: center;
}
.know-searchshow-Button {
  margin: 0 10px;
  padding: 0 10px;
  height: 40px;
  min-width: 80px;
  line-height: 40px;
  text-align: center;
  border-radius: 5px;
}
.know-searchshow-Button:hover {
  background: #dcdee2;
}
.know-searchshow-Button-titlt {
  display: inline;
  font-size: 16px;
  padding-left: 8px;
}
.know-searchshow-Button:hover .know-searchshow-Button-icon {
  color: #2d8cf0;
}
.know-searchshow-Button-select {
  color: #2d8cf0;
}
</style>

