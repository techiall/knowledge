/**
*
*  @author ZTiger
*
*/


<template>
  <div class="know-operate-header">
    <div
      class="know-operate-header-button"
      @click="clickButton(1,selectNum)"
      :class="{'know-operate-header-button-select':selectNum === 1}"
    >
      <div class="know-operate-header-button-icon">
        <Icon type="ios-list-box-outline" size="20" />
      </div>
      <div class="know-operate-header-button-title">详情</div>
    </div>

    <div
      class="know-operate-header-button"
      @click="clickButton(2,selectNum)"
      :class="{'know-operate-header-button-select':selectNum === 2}"
    >
      <div class="know-operate-header-button-icon">
        <i class="icon-lidaotu iconfont"></i>
      </div>
      <div class="know-operate-header-button-title">力导图</div>
    </div>
    <div
      class="know-operate-header-button"
      @click="clickButton(3,selectNum)"
      :class="{'know-operate-header-button-select':selectNum === 3}"
    >
      <div class="know-operate-header-button-icon">
        <i class="icon-shutu iconfont"></i>
      </div>
      <div class="know-operate-header-button-title">树图</div>
    </div>

    <div
      class="know-operate-header-button"
      v-if="itemExitFlag"
      @click="clickButton(4,selectNum)"
      :class="{'know-operate-header-button-select':selectNum === 4}"
    >
      <div class="know-operate-header-button-icon">
        <Icon type="ios-time-outline" size="20" />
      </div>
      <div class="know-operate-header-button-title">日志</div>
    </div>

    <div
      class="know-operate-header-button"
      @click="clickButton(5,selectNum)"
      :class="{'know-operate-header-button-select':selectNum === 5}"
    >
      <div class="know-operate-header-button-icon">
        <Icon type="ios-paper-outline" size="20" />
      </div>
      <div class="know-operate-header-button-title">节点知识</div>
    </div>
  </div>
</template>

<script>
export default {
  props: ["showClientFlag", "itemExitFlag"],
  data() {
    return {
      selectNum: 0
    };
  },
  methods: {
    // 按钮点击事件
    clickButton(newval, oldval) {
      if (newval === oldval || !this.showClientFlag) return;
      this.selectNum = newval;
      this.$emit("oMainCallback", 1, newval);
    },
    // 设置按钮 数值
    setSelectNum(val) {
      this.selectNum = val;
    }
  },
  watch: {
    showClientFlag(val) {
      if (!val) {
        this.selectNum = 0;
      }
    }
  }
};
</script>

<style  scoped>
.know-operate-header {
  display: flex;
  justify-content: flex-start;
  align-items: center;
  height: 50px;
}
.know-operate-header-button {
  box-sizing: border-box;
  min-width: 80px;
  height: 40px;
  padding: 0 10px;
  text-align: center;
  cursor: pointer;
  border-radius: 5px;
  margin: 0 5px;
}
.know-operate-header-button:hover {
  transition: background-color 0.5s;
  background: #dcdee2;
}
.know-operate-header-button:hover .ivu-icon,
.know-operate-header-button:hover .iconfont {
  color: #2d8cf0;
}
.know-operate-header-button-title {
  display: inline-block;
  height: 40px;
  line-height: 40px;
  font-size: 16px;
  text-indent: 0.3em;
}
.know-operate-header-button-icon {
  display: inline-block;
}
.know-operate-header-button-select {
  color: #2d8cf0;
}
</style>