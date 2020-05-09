/**
*
*  @author ZTiger
*
*/


<template>
  <div>
    <div class="g-labels-content" ref="labelsRef">
      <Input
        v-for="(item) in labels"
        :key="item"
        :value="item"
        @on-blur="labelEvnet('UPDATE_NODE_PROPER','blur',item)"
        @on-focus="labelEvnet('FOCUS_NODE_PROPER','focus',item)"
      >
        <Icon type="md-list" color="#19be6b" slot="prefix" />
        <Icon
          type="ios-close"
          class="g-del-data"
          slot="suffix"
          size="20"
          @click.once="labelEvnet('DELETE_NODE_PROPER','click',item)"
        />
      </Input>
    </div>
    <Input
      v-model="labelInput"
      placeholder="请输入属性"
      element-id="g-label-input"
      :disabled="disabledLabel"
      @on-blur="labelEvnet('ADD_NODE_PROPER','blur')"
      @on-enter="labelEvnet('ADD_NODE_PROPER','enter')"
    >
      <Icon v-if="disabledLabel" type="md-refresh" slot="prefix" class="g-loading" />
      <Icon v-else type="md-list-box" color="#808695" slot="prefix" />
    </Input>
  </div>
</template>


<script>
import { updateNode } from '@/api/node';

export default {
  props: ['treeNode', 'itemId'],
  data() {
    return {
      // 标签
      labels: [],
      labelInput: '',
      // 设置输入框为禁用状态
      disabledLabel: false,
    };
  },
  methods: {
    // 初始化 labels
    initLabels(labels) {
      this.labels = JSON.parse(labels);
    },
    // 标签处理函数
    labelEvnet(operator, type, content) {
      const NodeName = this.treeNode.name;
      const labels = JSON.stringify(this.labels);
      const params = {
        labels: JSON.parse(labels),
        itemId: this.itemId,
        record: {
          operator,
        },
      };
      let index, childDom, inputDom;
      if (content) {
        index = this.labels.indexOf(content);
        childDom = this.$refs.labelsRef.children[index];
        inputDom = childDom.querySelector('input');
      }
      const stateMap = {
        ADD_NODE_PROPER: async () => {
          const labelInput = this.labelInput.replace(/\s+/, '');
          if (!labelInput || this.disabledLabel) return;
          if (this.labels.includes(labelInput)) {
            this.$Message.warning(`${labelInput} 属性已经存在`);
            return;
          }
          const message = `添加新的节点属性，属性名称为 [${labelInput}]`;
          params.labels.push(labelInput);
          params.record.message = JSON.stringify({ message, NodeName });
          this.disabledLabel = true;
          await updateNode(this.treeNode.id, params);
          this.$emit('journal');
          this.disabledLabel = false;
          this.labels.push(labelInput);
          this.labelInput = '';
          if (type === 'enter') {
            this.$nextTick(() => {
              document.querySelector('#g-label-input').focus();
            });
          }
        },
        UPDATE_NODE_PROPER: async () => {
          const oldValue = this.labels[index];
          const newValue = inputDom.value.replace(/\s+/, '');
          const message = `[${oldValue}]属性名称修改为[${newValue}]`;
          if (oldValue === newValue) return;
          if (!newValue) {
            this.labelEvnet('DELETE_NODE_PROPER', 'click', oldValue);
            return;
          } else if (this.labels.includes(newValue)) {
            this.$Message.warning(`${newValue} 属性已经存在`);
            inputDom.style.borderColor = '#ff9900';
            return;
          }
          params.labels[index] = newValue;
          params.record.message = JSON.stringify({ message, NodeName });
          this.sendLoading(childDom, 'start');
          await updateNode(this.treeNode.id, params);
          this.$emit('journal');
          this.sendLoading(childDom, 'end');
          this.$set(this.labels, index, newValue);
        },
        DELETE_NODE_PROPER: async () => {
          const value = this.labels[index];
          const message = `[${value}] 属性被删除`;
          params.labels.splice(index, 1);
          params.record.message = JSON.stringify({ message, NodeName });
          this.sendLoading(childDom, 'start');
          await updateNode(this.treeNode.id, params);
          this.$emit('journal');
          this.labels.splice(index, 1);
        },
        FOCUS_NODE_PROPER: () => {
          inputDom.style.borderColor = '';
        },
      };
      stateMap[operator]();
    },
    // 发送图标
    sendLoading(childDom, state) {
      const suffixDom = childDom.querySelector('.ivu-input-suffix i');
      const prefixDom = childDom.querySelector('.ivu-input-prefix i');
      const inputDom = childDom.querySelector('input');
      switch (state) {
        case 'start':
          prefixDom.className = 'g-loading ivu-icon ivu-icon-md-refresh';
          inputDom.disabled = 'disabled';
          suffixDom.style.cursor = 'default';
          suffixDom.classList.remove('g-del-data');
          suffixDom.style.color = '#ccc';
          break;
        case 'end':
          prefixDom.className = 'ivu-icon ivu-icon-md-list';
          inputDom.disabled = '';
          suffixDom.style.cursor = '';
          suffixDom.classList.add('g-del-data');
          suffixDom.style.color = '';
          break;
      }
    },
  },
};
</script>


<style scoped>
.g-del-data {
  cursor: pointer;
}
.g-del-data:hover {
  color: #ed4014;
  font-weight: bold;
}
.g-loading {
  animation: ani-demo-load 2.5s linear infinite;
  font-size: 20px;
}
</style>