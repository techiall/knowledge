/**
*
*  @author ZTiger
*
*/


<template>
  <div class="container">
    <public-header
      class="know-manage-header"
      :RouterFlag="true"
      :itemName="itemName"
      :TitleShow="false"
    ></public-header>
    <manage-content @sendItem="getItemName"></manage-content>
  </div>
</template>


<script>
//导入 header 模板
import publicHeader from '@/components/publicHeader.vue';

import manageContent from './manageContent.vue';

export default {
  components: { publicHeader, manageContent },
  data() {
    return {
      // 项目名称
      itemName: '',
    };
  },
  methods: {
    // 获取项目名称
    getItemName(val) {
      this.itemName = val;
    },
  },
};
</script>


<style scoped>
.know-manage-header {
  min-width: 300px;
  height: 60px;
  padding: 0 55px;
}
</style>