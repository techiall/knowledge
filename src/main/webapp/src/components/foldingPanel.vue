/**
*
*  @author ZTiger
*
*/


<template>
  <div class="know-fold">
    <div class="know-fold-center" ref="knowfoldcenter">
      <slot></slot>
    </div>
    <div class="know-folad-down" v-show="IconShowFlag">
      <Icon
        type="ios-arrow-down"
        class="know-folad-down-icon"
        @click="clickIcon"
        :class="{'know-folad-up-icon':!clickIconFlag}"
      />
    </div>
  </div>
</template>


<script>
export default {
  props: {
    showHeight:{
      default:100
    }
  },
  data() {
    return {
      // 点击图标标志位
      clickIconFlag: true,
      // 图标显示标志位
      IconShowFlag: true
    };
  },
  mounted () {
    this.InitSet();
  },
  methods: {
    // 点击展开图标
    clickIcon() {
      this.clickIconFlag = !this.clickIconFlag;

      if (this.clickIconFlag) {
        this.$refs.knowfoldcenter.style.height = this.showHeight + 'px';
      } else {
        this.$refs.knowfoldcenter.style.height =
          this.$refs.knowfoldcenter.scrollHeight + "px";
      }
    },
    //设置初始化高度等
    InitSet() {
      this.clickIconFlag = true;
      this.$refs.knowfoldcenter.style.height = "auto";
      this.$nextTick(() => {
				if(this.$refs.knowfoldcenter.scrollHeight>this.showHeight){
					this.$refs.knowfoldcenter.style.height = this.showHeight + 'px';
					this.IconShowFlag =true;
				}else{
					this.IconShowFlag =false;
					this.$refs.knowfoldcenter.style.height =this.$refs.knowfoldcenter.scrollHeight;
				}
      });
     
    }
  }
};
</script>


<style scoped>
.know-fold-center {
  height: 100px;
  overflow: hidden;
  transition: height 0.5s ease-in-out;
}

.know-folad-down {
  text-align: center;
  /* padding-top: 10px; */
}
.know-folad-up-icon {
  transform: rotate(-180deg);
}
.know-folad-down-icon {
  cursor: pointer;
  font-size: 16px;
  font-stretch: expanded;
}
.know-folad-down-icon:hover {
  color: #2db7f5;
}
</style>