/**
*
*  @author ZTiger
*
*/


<template>
  <div class="g-warp">
    <item-header :totalElements="totalElements"/>
    <loadAnimation v-if="!itemload" />
    <item-card
      :itemData="itemShareData"
      status="false"
      v-if="getShowType==='card'"
      @selectItem="selectSetting"
      @selectDelete="selectDelete"
    ></item-card>
    <item-list
      :itemData="itemShareData"
      status="false"
      v-if="getShowType==='list'"
      @on-setting="selectSetting"
      @on-delete="selectDelete"
    ></item-list>
    <item-setting ref="modalS" @updataItem="updataItem"></item-setting>
    <item-delete ref="modalD" @delItem="delItem"></item-delete>
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

export default {
  components: {
    itemHeader,
    itemList,
    itemCard,
    itemSetting,
    itemDelete,
    loadAnimation
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
    this.getShareItem();
  },
  methods: {
    // 获取服务器里面分享的项目
    getShareItem() {
      const url = '/item/share';
      const OBJ = {
        size: 50,
      };
      this.get(url, OBJ)
        .then((res) => {
          this.itemload = true;
          this.itemShareData = res.data.content;
          this.totalElements = res.data.totalElements;
        })
        .catch(() => {});
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
</style>
