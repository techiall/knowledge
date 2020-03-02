/**
*
*  @author ZTiger
*
*/


<template>
  <div class="container">
    <div
      class="g-dis-fc g-list-row cup"
      v-for="(item,index) in itemData"
      :key="item.id"
      @click.stop="selectItem(index)"
    >
      <div class="g-list-left">
        <div class="g-row-img"></div>
      </div>
      <div class="g-list-right g-dis-fc">
        <div class="g-right-main">
          <div class="g-list-title">{{item.name}}</div>
          <div class="g-list-content">{{item.description}}</div>
        </div>
        <div class="g-right-select">
          <Tooltip content="打开项目设置" placement="top" class="g-tooltip">
            <Icon type="ios-settings" class="g-right-icon cup" @click.stop="selectAction(1,index)" />
          </Tooltip>
          <Tooltip
            content="项目删除"
            placement="top"
            class="g-tooltip"
            v-if="status !== 'false'||user.userName === item.author.userName"
          >
            <Icon type="ios-trash" class="g-right-icon cup" @click.stop="selectAction(2,index)" />
          </Tooltip>
        </div>
      </div>
    </div>
    <slot></slot>
  </div>
</template>


<script>
import { mapState } from 'vuex';

export default {
  props: ['itemData', 'status'],
  data() {
    return {};
  },
  computed: {
    ...mapState(['user']),
  },
  methods: {
    // 选中的项目
    selectItem(index) {
      this.$router.push({
        path: `/manage/${this.itemData[index]['author']['userName']}/${this.itemData[index].id}`,
      });
    },
    // 状态选中
    selectAction(val, index) {
      if (val === 1) {
        this.$emit('on-setting', index);
      } else if (val === 2) {
        this.$emit('on-delete', index);
      }
    },
  },
};
</script>


<style scoped>
.g-list-row:hover .g-list-title {
  color: #2b85e4;
}
.g-list-row:hover .g-tooltip {
  display: inline-block;
}
.g-dis-fc {
  display: flex;
  align-items: center;
}
.g-row-img {
  width: 40px;
  height: 40px;
  background: url('../../../../assets/images/item_bg.png') no-repeat;
  background-position: center center;
  background-size: cover;
  border-radius: 4px;
}
.g-list-left {
  margin: 0 30px 0 0;
  padding: 20px 0 20px;
}
.g-list-right {
  flex-grow: 1;
  padding: 20px 0 20px;
  border-bottom: 1px solid #f0f0f0;
}
.g-list-title {
  font-size: 15px;
  color: #000;
}
.g-list-content {
  font-size: 12px;
}
.g-right-main {
  flex-grow: 1;
}
.g-right-select {
  flex-shrink: 0;
  width: 50px;
  margin: 0 0 0 20px;
}
.g-right-icon {
  font-size: 18px;
  color: #808695;
}
.g-tooltip {
  display: none;
}
.g-tooltip:not(:first-of-type) {
  margin: 0 0 0 10px;
}
.g-right-icon:hover {
  color: #2db7f5;
}
</style>
