/**
*
*  @author ZTiger
*
*/


<template>
  <div class="g-wrap">
    <item-header :totalElements="totalElements" />
    <loadAnimation v-if="!itemload" />
    <div class="g-body">
      <item-card
        :itemData="itemShareData"
        status="false"
        v-if="getShowType==='card'"
        @selectItem="selectSetting"
        @selectDelete="selectDelete"
      />
      <item-list
        :itemData="itemShareData"
        status="false"
        v-if="getShowType==='list'"
        @on-setting="selectSetting"
        @on-delete="selectDelete"
      />
    </div>

    <item-setting ref="modalS" @updataItem="updataItem" />
    <item-delete ref="modalD" @delItem="delItem" />
  </div>
</template>


<script>
import itemHeader from './operation/itemHeader.vue';
import itemCard from './operation/itemCard.vue';
import itemList from './operation/itemList.vue';
import itemSetting from './operation/itemSetting.vue';
import itemDelete from './operation/itemDelete.vue';
import loadAnimation from '@/components/loadAnimation.vue';
import { mapGetters } from 'vuex';
import { getShareItem } from '@/api/item';

export default {
  components: {
    itemHeader,
    itemList,
    itemCard,
    itemSetting,
    itemDelete,
    loadAnimation,
  },
  data() {
    return {
      // 分享项目
      itemShareData: [],
      // 项目个数
      totalElements: 0,
      // 项目加载标志位
      itemload: false,
    };
  },
  computed: {
    ...mapGetters(['getShowType']),
  },
  created() {
    this.shareItem();
  },
  methods: {
    // 获取服务器里面分享的项目
    async shareItem() {
      const data = await getShareItem({
        size: 50,
        sort: 'id,desc',
      });
      this.itemload = true;
      this.itemShareData = data.content;
      this.totalElements = data.totalElements;
    },
    // 选中设置
    selectSetting(index) {
      this.selectIndex = index;
      this.$refs.modalS.showView(this.itemShareData[index], 'false');
    },
    // 修改数据
    updataItem(val) {
      this.itemShareData.splice(this.selectIndex, 1, val);
    },
    // 选中 项目删除
    selectDelete(index) {
      this.$refs.modalD.showView(this.itemShareData[index]);
    },
    // 删除项目
    delItem() {
      this.getShareItem();
    },
  },
};
</script>


<style scoped>
.g-wrap {
  margin: 20px 0 0 0;
  height: calc(100% - 20px);
}
.g-body {
  padding: 0 20px 20px;
}
</style>
