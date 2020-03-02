/**
*
*  @author ZTiger
*
*/


<template>
  <div class="item-list">
    <div
      class="item-box box "
      v-for="(item,index) in itemData"
      :key="item.id"
      @click.stop="selectItem(index)"
    >
      <div class="item-box-bgc">
        <div class="dis-fix item-box-f">
          <Tooltip max-width="280" placement="top" :content="item.name">
            <div class="item-box-name">{{item.name}}</div>
          </Tooltip>
          <div class="dis-operation">
            <Tooltip content="打开项目设置" placement="top">
              <Icon
                type="ios-settings"
                class="item-box-icon"
                @click.stop="selectItemSetting(index)"
              />
            </Tooltip>
            <Tooltip
              content="项目删除"
              placement="top"
              v-if="status !== 'false'||user.userName === item.author.userName"
            >
              <Icon type="ios-trash" class="item-box-icon" @click.stop="selectItemDelete(index)" />
            </Tooltip>
          </div>
        </div>
        <Tooltip
          :content="item.description"
          placement="bottom"
          max-width="280"
          transfer
          v-if="item.description"
        >
          <div class="item-description">{{item.description}}</div>
        </Tooltip>
      </div>
    </div>
    <slot></slot>
  </div>
</template>


<script>
import { mapState } from 'vuex';

export default {
  components: {},
  props: ['itemData', 'status'],
  data() {
    return {
      modalFlag: false,
    };
  },
  computed: {
    ...mapState(['user']),
  },
  watch: {},
  methods: {
    // 选中 项目设置
    selectItemSetting(index) {
      this.$emit('selectItem', index, this.status);
    },
    // 选中 删除
    selectItemDelete(index) {
      this.$emit('selectDelete', index);
    },
    // 选中的项目
    selectItem(index) {
      this.$router.push({
        path: `/manage/${this.itemData[index]['author']['userName']}/${this.itemData[index].id}`,
      });
    },
  },
};
</script>


<style scoped>
.box {
  width: 240px;
  height: 120px;
  border-radius: 8px;
  box-sizing: border-box;
  cursor: pointer;
}
.item-box {
  background: url('../../../../assets/images/itembg.jpg');
  background-size: 100% 100%;
  margin: 0 20px 20px 0;
  transition: all 0.2s;
  color: #fff;
}
.item-list{
    margin: 20px 0;
  display: flex;
  flex-wrap: wrap;
}
.item-box-bgc {
  width: 100%;
  height: 100%;
  padding: 5px 10px;
  border-radius: 8px;
  background-image: linear-gradient(
    180deg,
    rgba(0, 0, 0, 0.35) 0%,
    transparent 80%
  );
}
.item-box:hover {
  box-shadow: 0 6px 12px rgba(38, 38, 38, 0.1);
  transform: translateY(-4px);
}
.item-box-f {
  align-items: center;
}
.item-box-bgc:hover .dis-operation{
  display: inline-block;
}
.dis-operation {
  display: none;
}
.item-box-name {
  font-size: 16px;
  font-weight: 600;
}
.item-description {
  white-space: nowrap;
  text-overflow: ellipsis;
  overflow: hidden;
  font-size: 12px;
  width: 215px;
}
.item-box-icon {
  font-size: 20px;
}
.item-box-title {
  font-size: 13px;
  overflow: hidden;
}
.ivu-tooltip {
  margin: 0 0 0 5px;
}
</style>