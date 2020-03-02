/**
*
*  @author ZTiger
*
*/


<template>
  <div class="load-wrap">
    <div></div>
    <div></div>
    <div></div>
  </div>
</template>


<script>
export default {
  data() {
    return {};
  },
};
</script>


<style scoped>
.load-wrap {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 300px;
}
.load-wrap > div {
  width: 15px;
  height: 15px;
  background: #dcdee2;
  margin: 0 5 0 0px;
  border-radius: 50%;
  animation: loadAnimation .9s infinite;
}
.load-wrap > div:nth-of-type(2) {
  animation-delay: .2s;
}
.load-wrap > div:nth-of-type(3) {
  animation-delay: .5s;
}
@keyframes loadAnimation {
  0%,
  40%,
  100% {
    transform: scale(0.4);
  }
  20% {
    transform: scale(1);
  }
}
</style>
