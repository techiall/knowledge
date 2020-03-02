/**
*
*  @author ZTiger
*
*/


<template>
  <div>
    <div class="title-h1">项目创建</div>
    <div class="title-tips">欢迎使用 “知识图谱构建平台” ,尝试新建一个项目给自己</div>
    <div class="item-create">
      <div class="create-box box dis-fixc g-item-box" @click="statusSelect(1)">
        <Icon type="md-add" size="22" />
        <div class="create-box-title">创建项目</div>
      </div>
    </div>
    <load-animation v-if="itemLoadFlag" />
    <div v-if="itemData.length" class="title-h1">我参与的</div>
    <item-card
      v-if="itemData.length"
      :itemData="itemData"
      @selectItem="selectItem"
      @selectDelete="selectDelete"
      status="true"
    >
      <div class="g-item-box box dis-fixc" @click="statusSelect(2)">
        <Icon type="logo-buffer" size="22" />
        <div class="create-box-title">查看更多我参与的项目</div>
      </div>
    </item-card>
    <load-animation v-if="shareitemLoadFlag" />
    <div v-if="itemShareData.length" class="title-h1">分享项目</div>
    <item-card
      v-if="itemShareData.length"
      :itemData="itemShareData"
      @selectItem="selectItem"
      @selectDelete="selectDelete"
      status="false"
    >
      <div class="g-item-box box dis-fixc" @click="statusSelect(3)">
        <Icon type="md-share" size="22" />
        <div class="create-box-title">查看更多分享项目</div>
      </div>
    </item-card>
    <item-create ref="modalC" @addItem="addItem"></item-create>
    <item-setting ref="modalS" @updataItem="updataItem"></item-setting>
    <item-delete ref="modalD" @delItem="delItem"></item-delete>
  </div>
</template>


<script>
import itemCard from './operation/itemCard.vue';
import itemCreate from './operation/itemCreate.vue';
import itemSetting from './operation/itemSetting.vue';
import itemDelete from './operation/itemDelete.vue';
import loadAnimation from '@/components/loadAnimation.vue';

export default {
  components: { itemCard, itemCreate, itemSetting, itemDelete, loadAnimation },
  data() {
    return {
      // 项目数据
      itemData: [],
      // 分享项目数据
      itemShareData: [],
      // 选中的数据下标
      selectIndex: '',
      // 项目加载标志位
      itemLoadFlag: true,
      // 分享项目加载标志位
      shareitemLoadFlag: true,
    };
  },
  mounted() {
    this.getServeItem();
    this.getServeShareItem();
  },
  methods: {
    // 获取服务器里面的项目
    getServeItem() {
      const url = 'item';
      const OBJ = {
        size: 4,
      };
      this.get(url, OBJ)
        .then((res) => {
          this.itemLoadFlag = false;
          this.itemData = res.data.content;
        })
        .catch(() => {});
    },
    // 获取服务器里面分享的项目
    getServeShareItem() {
      const url = 'item/share';
      const OBJ = {
        size: 4,
      };
      this.get(url, OBJ)
        .then((res) => {
          this.shareitemLoadFlag = false;
          this.itemShareData = res.data.content;
        })
        .catch(() => {});
    },
    // 状态选择
    statusSelect(type) {
      const statusMap = {
        // 点击创建
        1: () => {
          this.$refs.modalC.setModalStatus();
        },
        // 点击查看更多项目
        2: () => {
          this.$router.push('/project/list');
        },
        // 点击查看分享项目
        3: () => {
          this.$router.push('/project/share');
        },
      };
      statusMap[type]();
    },
    // 选中 项目设置
    selectItem(index, status) {
      this.selectIndex = index;
      if (status === 'true') {
        this.$refs.modalS.showView(this.itemData[index], status);
      } else {
        this.$refs.modalS.showView(this.itemShareData[index], status);
      }
    },
    // 选中 项目删除
    selectDelete(index) {
      this.$refs.modalD.showView(this.itemData[index]);
    },
    // 创建成功 添加数据
    addItem(val) {
      this.itemData.push(val);
      this.getServeShareItem();
    },
    // 修改数据
    updataItem(val) {
      this.itemData.splice(this.selectIndex, 1, val);
      this.getServeShareItem();
    },
    // 删除项目
    delItem() {
      this.getServeShareItem();
      this.getServeItem();
    },
  },
};
</script>


<style scoped>
.title-h1 {
  font-size: 18px;
  color: #202124;
}
.title-tips {
  font-size: 13px;
}
.item-row,
.item-create {
  margin: 20px 0;
  display: flex;
  flex-wrap: wrap;
}
.box {
  width: 240px;
  height: 120px;
  border-radius: 8px;
  box-sizing: border-box;
  cursor: pointer;
}
.g-item-box {
  background: #f7f7f7;
}
.g-item-box:hover {
  background: #e8f0fe;
  color: #1b9aee;
}
.create-box:hover {
  border: 1px solid #87d2ff;
  box-shadow: 0 8px 16px rgba(27, 154, 238, 0.2);
}
.create-box-title {
  font-size: 15px;
  margin: 5px 0 0 0;
  letter-spacing: 0.1em;
}
</style>