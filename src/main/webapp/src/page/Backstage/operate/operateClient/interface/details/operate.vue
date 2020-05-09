/**
*
*  @author ZTiger
*
*/


<template>
  <div class="b-detail-wrap">
    <div class="g-content-row">
      <div class="g-row-tip">属性</div>
      <Input
        v-model="NodeName"
        element-id="g-property-name"
        :disabled="disabled"
        @on-blur="updateNodeName"
      >
        <Icon v-if="disabled" type="md-refresh" slot="prefix" class="g-loading" />
        <Icon v-else type="md-list" color="#19be6b" slot="prefix" />
      </Input>
      <labels-view ref="labelsRef" :treeNode="treeNode" :itemId="itemId" @journal="updateJournal" />
    </div>
    <div class="g-content-row">
      <div class="g-row-tip">关系</div>
      <property-view
        ref="propertyRef"
        :treeNode="treeNode"
        :itemId="itemId"
        @journal="updateJournal"
        @force="updateforce"
      />
    </div>
  </div>
</template>


<script>
import { updateNode, queryNode } from '@/api/node';
import labelsView from './labels.vue';
import propertyView from './property.vue';

export default {
  props: ['treeNode', 'itemId'],
  components: { labelsView, propertyView },
  data() {
    return {
      // 节点名称
      NodeName: '',
      // 设置输入框为禁用状态
      disabled: false,
    };
  },

  methods: {
    setdetails(data) {
      this.NodeName = data.name;
      this.disabled = false;
      this.$refs.labelsRef.initLabels(JSON.stringify(data.labels || []));
      this.$refs.propertyRef.initProperty(JSON.stringify(data.property || {}));
    },
    // 设置节点名称
    setNodeName(name) {
      this.NodeName = name;
    },
    // 日志
    updateJournal() {
      this.$emit('SClientCallback', 4);
    },
    // 力导图
    updateforce() {
      this.$emit('SClientCallback', 2);
    },
    // 修改节点名称
    async updateNodeName() {
      const name = this.NodeName;
      if (this.treeNode.name === name || !name) return;
      this.disabled = true;
      const itemId = this.itemId;
      const queryData = await queryNode({ query: name, itemId });
      if (queryData.length > 0 && queryData[0].name === name) {
        this.disabled = false;
        this.$Message.warning({
          content: `${name}节点已存在,请重新修改`,
          duration: 5,
        });
        this.$nextTick(() => {
          document.querySelector('#g-property-name').focus();
        });
        return;
      }
      const message = `[${this.treeNode.name}] 节点名称修改为 [${name}]`;
      const params = {
        name,
        itemId,
        record: {
          message: JSON.stringify({ message, name }),
          operator: 'UPDATE_NODE_NAME',
        },
      };
      await updateNode(this.treeNode.id, params);
      this.disabled = false;
      this.$emit('SClientCallback', 4);
      // 告诉其他 名称修改成功
      this.$emit('SClientCallback', 1, name);
    },
  },
};
</script>


<style scoped>
.g-content-row {
  margin-bottom: 20px;
  padding: 0 10px;
}
.g-row-tip {
  font-size: 16px;
  font-weight: 700;
  margin-bottom: 5px;
}
.g-row-tip::after {
  content: ':';
  margin: 0 0 0 5px;
}
.g-loading {
  animation: ani-demo-load 2.5s linear infinite;
  font-size: 20px;
  color: #19be6b;
}
</style>
