/**
*
*  @author ZTiger
*
*/


<template>
  <div class="g-warp">
    <item-header :totalElements="totalElements" />
    <div class="g-body scroll" ref="gBody">
      <load-animation v-if="!itemload" />
      <item-card
        :itemData="itemData"
        status="true"
        v-if="getShowType==='card'"
        @selectItem="selectSetting"
        @selectDelete="selectDelete"
      >
        <div class="g-card-create cup dis-fixc" @click="createItem">
          <Icon type="md-add" class="g-card-i" />
          <div class="g-card-Ctitle">创建项目</div>
        </div>
      </item-card>
      <item-list
        :itemData="itemData"
        status="true"
        v-if="getShowType==='list'"
        @on-setting="selectSetting"
        @on-delete="selectDelete"
      >
        <div class="g-list-create cup" @click="createItem">
          <Icon type="md-add" class="g-list-i" />
          <div class="g-list-Ctitle">创建项目</div>
        </div>
      </item-list>
    </div>
    <item-setting ref="modalS" @updataItem="updataItem" />
    <item-delete ref="modalD" @delItem="delItem" />
    <item-create ref="modalC" @addItem="addItem" />
  </div>
</template>


<script>
import itemCard from './operation/itemCard.vue';
import itemList from './operation/itemList.vue';
import itemHeader from './operation/itemHeader.vue';
import itemSetting from './operation/itemSetting.vue';
import itemDelete from './operation/itemDelete.vue';
import itemCreate from './operation/itemCreate.vue';
import loadAnimation from '@/components/loadAnimation.vue';
import { mapGetters } from 'vuex';
import { getItem } from '@/api/item';

export default {
  components: {
    itemCard,
    itemList,
    itemHeader,
    itemSetting,
    itemDelete,
    itemCreate,
    loadAnimation,
  },
  data() {
    return {
      // 项目列表
      itemData: [],
      // 项目个数
      totalElements: 0,
      // 选中项目下标
      selectIndex: '',
      // 项目加载标志位
      itemload: false,
    };
  },
  computed: {
    ...mapGetters(['getShowType']),
  },
  created() {
    this.getItemList();
  },
  methods: {
    // 获取项目列表
    async getItemList() {
      const data = await getItem({
        size: 50,
        sort: 'id,desc',
      });
      this.itemload = true;
      this.itemData = data.content;
      this.totalElements = data.totalElements;
    },
    // 创建项目
    createItem() {
      this.$refs.modalC.setModalStatus();
    },
    // 创建成功 添加数据
    addItem(val) {
      this.itemData.unshift(val);
      this.totalElements += 1;
    },
    // 选中设置
    selectSetting(index) {
      this.selectIndex = index;
      this.$refs.modalS.showView(this.itemData[index], 'true');
    },
    // 修改数据
    updataItem(val) {
      this.itemData.splice(this.selectIndex, 1, val);
    },
    // 选中 项目删除
    selectDelete(index) {
      this.$refs.modalD.showView(this.itemData[index]);
    },
    // 删除项目
    delItem() {
      this.getItemList();
    },
  },
};
</script>


<style scoped>
.g-warp {
  margin: 20px 0 0 0;
  height: calc(100% - 20px);
}
.g-body {
  height: calc(100% - 80px);
  padding: 0px 20px 20px;
  overflow: auto;
}
.g-list-create {
  display: flex;
  align-items: center;
}
.g-list-create:hover .g-list-i,
.g-card-create:hover {
  background: #e8f0fe;
  color: #1b9aee;
}
.g-list-create:hover .g-list-Ctitle {
  color: #1b9aee;
}
.g-list-i {
  height: 40px;
  width: 40px;
  margin: 20px 30px 20px 0;
  text-align: center;
  line-height: 40px;
  background: #f7f7f7;
  border-radius: 4px;
  color: #808695;
}
.g-list-Ctitle {
  color: #808695;
}
.g-card-create {
  width: 240px;
  height: 120px;
  background: #f7f7f7;
  border-radius: 8px;
}
.g-card-Ctitle {
  font-size: 15px;
  margin: 5px 0 0 0;
  letter-spacing: 0.1em;
}
.g-card-i {
  font-size: 22px;
}
</style>

