/**
*
*  @author ZTiger
*
*/


<template>
  <div class="b-detail-wrap">
    <div class="g-content-row">
      <div class="g-content-tip">属性</div>
      <div class="g-row-wrap g-hover">
        <Icon type="md-list" class="g-row-icon" />
        <span>{{nodeName}}</span>
      </div>
      <div class="g-row-wrap g-hover" v-for="item in labels" :key="item.keyId">
        <Icon type="md-list" class="g-row-icon" />
        <span>{{item}}</span>
      </div>
    </div>
    <div class="g-content-row">
      <div class="g-content-tip">关系</div>
      <div class="g-row-wrap" v-for="item in property" :key="item.keyId">
        <div class="g-row-relation g-hover">
          <Icon type="md-funnel" class="g-row-icon" />
          <span>{{item.relation}}</span>
        </div>
        <div class="g-row-node g-hover">
          <Icon type="md-list" class="g-row-icon" />
          <span>{{item.name}}</span>
        </div>
      </div>
    </div>
  </div>
</template>


<script>
export default {
  data() {
    return {
      nodeName: '',
      labels: [],
      property: [],
    };
  },
  methods: {
    // 设置数据
    setdetails(data) {
      this.nodeName = data.name;
      this.labels = data.labels || [];
      this.property = [];
      const propertyData = data.property || {};
      for (let key in propertyData) {
        propertyData[key].forEach((item) => {
          this.property.push({
            relation: key,
            name: item.name,
            keyId: `${data.name}-${key}-${item.name}`,
          });
        });
      }
    },
  },
};
</script>


<style scoped>
.g-content-row {
  margin-bottom: 20px;
  padding: 0 10px;
}
.g-content-tip {
  font-size: 16px;
  font-weight: 700;
  margin-bottom: 5px;
}
.g-content-tip::after {
  content: ':';
  margin: 0 0 0 5px;
}
.g-row-icon {
  width: 32px;
  height: 32px;
  text-align: center;
  line-height: 32px;
  color: #19be6b;
  font-size: 16px;
}
.g-row-wrap {
  display: flex;
  align-items: center;
  cursor: pointer;
}
.g-hover {
  border-bottom: 1px solid #e8eaec;
}
.g-hover:hover {
  border-color: #57a3f3;
}
.g-row-relation {
  width: 30%;
}
.g-row-node {
  flex: 1;
}
</style>
