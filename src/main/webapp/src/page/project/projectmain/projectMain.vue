/**
*
*  @author ZTiger
*
*/


<template>
  <div class="g-main-wrap scroll">
    <router-view></router-view>
  </div>
</template>


<script>
export default {
  data() {
    return {};
  },
  methods: {},
};
</script>


<style scoped>
.g-main-wrap {
  padding: 30px;
  overflow: auto;
}
</style>
