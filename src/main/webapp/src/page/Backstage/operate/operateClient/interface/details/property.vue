/**
*
*  @author ZTiger
*
*/


<template>
  <div class="g-property-wrap">
    <div ref="propertyRef">
      <div class="g-input-wrap" v-for="(item) in property" :key="item.keyId">
        <Input
          class="g-input-relation"
          :value="item.relation"
          @on-focus="focusEvent($event,item.keyId,'relation')"
          @on-blur="relationEvent('UPDATE_NODE_PROPERTY','relationBlur',item.keyId)"
        >
          <Icon type="md-funnel" color="#19be6b" slot="prefix" />
        </Input>
        <Input
          class="g-input-node"
          :value="item.name"
          @on-focus="focusEvent($event,item.keyId,'node')"
          @on-keydown="keyboardEvent('update',$event)"
          @on-change="updateNodeEvent(item.keyId)"
          @on-blur="relationEvent('UPDATE_NODE_PROPERTYNAME','nodeBlur',item.keyId)"
        >
          <Icon type="md-list" color="#19be6b" slot="prefix" />
          <Icon
            type="ios-close"
            class="g-del-data"
            slot="suffix"
            size="20"
            @click.once="relationEvent('DELETE_NODE_PROPERTY','delete',item.keyId)"
          />
        </Input>
      </div>
    </div>
    <div class="g-input-wrap">
      <Input
        v-model="relationInput"
        placeholder="请输入关系名称"
        class="g-input-relation"
        element-id="id-property-relation"
        :disabled="disabledFlag"
        @on-blur="relationEvent('ADD_NODE_PROPERTY','relationBlur')"
        @on-enter="relationEvent('ADD_NODE_PROPERTY','relationEnter')"
      >
        <Icon v-if="disabledFlag" type="md-refresh" slot="prefix" class="g-loading" />
        <Icon v-else type="md-list-box" color="#808695" slot="prefix" />
      </Input>
      <Input
        v-model="nodeInput"
        placeholder="请输入节点名称"
        class="g-input-node"
        element-id="id-property-node"
        :disabled="disabledFlag"
        @on-focus="focusEvent($event,'input')"
        @on-blur="relationEvent('ADD_NODE_PROPERTY','nodeBlur')"
        @on-enter="relationEvent('ADD_NODE_PROPERTY','nodeEnter')"
        @on-keydown="keyboardEvent('input',$event)"
        @on-change="inputNodeEvent"
      >
        <Icon v-if="disabledFlag" type="md-refresh" slot="prefix" class="g-loading" />
        <Icon v-else type="md-git-commit" color="#808695" slot="prefix" />
      </Input>
    </div>
    <div
      v-show="(queryLoading||this.queryData.length)&&dropdownFlag"
      class="g-select-dropdown"
      :style="{top:positionY}"
    >
      <div v-show="queryLoading" class="g-select-load">{{queryLoading}}</div>
      <div v-show="this.queryData.length">
        <ul class="g-ul" v-for="(item,index) in queryData" :key="item.id">
          <li
            class="g-li"
            :class="{'g-li-focus':directionIndex===index}"
            @click="dropdownEvent(index)"
            @mousedown.prevent
          >{{item.name}}</li>
        </ul>
      </div>
    </div>
  </div>
</template>


<script>
import { queryNode, updateNode } from '@/api/node';

export default {
  props: ['treeNode', 'itemId'],
  data() {
    return {
      // 关系
      property: [],
      propertyServer: {},
      // 方向键下标
      directionIndex: -1,
      dropdownFlag: false,
      // 位置
      positionY: '32px',
      positionIndex: 0,
      // 用户输入
      nodeInput: '',
      relationInput: '',
      // 查询标志位,内容
      queryData: [],
      queryFlag: false,
      queryContent: '',
      queryLoading: '',
      // 设置输入框为禁用状态
      disabledFlag: false,
    };
  },
  methods: {
    // 初始化 labels
    initProperty(property) {
      this.property = [];
      this.queryFlag = false;
      this.propertyServer = JSON.parse(property);
      const data = JSON.parse(property);
      for (let key in data) {
        data[key].forEach((item) => {
          this.property.push({
            relation: key,
            name: item.name,
            oldName: item.name,
            keyId: `${this.treeNode.name}-${key}-${item.name}`,
            id: item.id,
          });
        });
      }
      this.positionY = `${(this.property.length + 1) * 32}px`;
    },
    // 远程搜索的方法
    async remoteMethod(query) {
      if (this.queryFlag) {
        this.queryContent = query;
        return;
      }
      this.queryFlag = true;
      this.queryLoading = '加载中';
      const itemId = this.itemId;
      const data = await queryNode({ query, itemId });
      this.queryFlag = false;
      this.queryLoading = '';
      if (this.queryContent) {
        const Tquery = this.queryContent;
        this.queryContent = false;
        this.remoteMethod(Tquery);
      } else {
        this.queryData = data.slice(0, 5);
        if (data.length <= 0) {
          this.queryLoading = '无匹配数据';
        }
      }
    },
    // 键盘事件
    async keyboardEvent(type, event) {
      const { keyCode } = event;
      const index = this.directionIndex;
      const length = this.queryData.length;
      const stateMap = {
        38: () => {
          event.preventDefault();
          this.directionIndex = index <= 0 ? length - 1 : index - 1;
        },
        40: () => {
          event.preventDefault();
          this.directionIndex = (index + 1) % length;
        },
        input: () => {
          this.nodeInput = this.queryData[this.directionIndex].name;
        },
        update: () => {
          const data = { ...this.property[this.positionIndex] };
          data.name = this.queryData[this.directionIndex].name;
          this.$set(this.property, this.positionIndex, data);
        },
        enter: () => {
          const childDom = this.$refs.propertyRef.children[this.positionIndex];
          const nodeInputDom = childDom.querySelector('.g-input-node input');
          nodeInputDom.blur();
        },
        default: () => {},
      };
      if (length <= 0) return;
      (stateMap[keyCode] || stateMap.default)();
      if (keyCode === 38 || keyCode === 40) {
        stateMap[type]();
      } else if (keyCode === 13 && type === 'update') {
        stateMap.enter();
      }
    },
    // 获取焦点
    focusEvent(event, keyId, type) {
      let index;
      if (keyId === 'input') {
        index = this.property.length;
      } else {
        index = this.property.map((item) => item.keyId).indexOf(keyId);
        const childDom = this.$refs.propertyRef.children[index];
        const nodeInputDom = childDom.querySelector(`.g-input-${type} input`);
        nodeInputDom.style.borderColor = '';
      }
      this.dropdownFlag = true;
      this.queryData = [];
      this.queryLoading = '';
      this.directionIndex = -1;
      this.positionY = `${(index + 1) * 32}px`;
      this.positionIndex = index;
    },
    // 输入节点change事件
    inputNodeEvent() {
      const queryValue = this.nodeInput.replace(/\s+/g, '');
      if (!queryValue) return;
      this.remoteMethod(queryValue);
    },
    // 点击下拉菜单
    dropdownEvent(index) {
      if (this.positionIndex === this.property.length)
        this.nodeInput = this.queryData[index].name;
      else {
        const data = { ...this.property[this.positionIndex] };
        data.name = this.queryData[index].name;
        this.$set(this.property, this.positionIndex, data);
      }
    },
    relationEvent(operator, type, propertyKeyId) {
      const NodeName = this.treeNode.name;
      const itemId = this.itemId;
      const params = {
        itemId,
        record: {
          operator,
        },
      };
      const stateMap = {
        ADD_NODE_PROPERTY: async () => {
          if (this.disabledFlag) return;
          this.disabledFlag = true;
          if (type === 'nodeBlur' || type === 'nodeEnter')
            this.dropdownFlag = false;
          const relationValue = this.relationInput.replace(/\s+/g, '');
          const nodeValue = this.nodeInput.replace(/\s+/g, '');
          const nodeDom = document.querySelector('#id-property-node');
          const relationDom = document.querySelector('#id-property-relation');
          if (!nodeValue || !relationValue) {
            if (type === 'relationEnter' && !nodeValue && relationValue) {
              nodeDom.focus();
            } else if (type === 'nodeEnter' && !relationValue && nodeValue) {
              relationDom.focus();
            }
            this.disabledFlag = false;
            return;
          }
          if (nodeValue === NodeName) {
            this.$Message.warning({
              content: `节点名称与关系节点名称相同`,
              duration: 5,
            });
            nodeDom.focus();
            this.disabledFlag = false;
            return;
          }
          const queryData = await queryNode({ query: nodeValue, itemId });
          if (!IsnodeExists(queryData, nodeValue)) {
            this.disabledFlag = false;
            this.$nextTick(() => {
              nodeDom.focus();
            });
            return;
          }
          const status = this.indexOfValue(relationValue, {
            id: queryData[0].id,
            name: nodeValue,
          });
          if (!status) {
            this.$Message.warning({
              content: `${NodeName} ← ${relationValue} → ${nodeValue} 关系已经存在`,
              duration: 5,
            });
            this.disabledFlag = false;
            this.$nextTick(() => {
              relationDom.focus();
            });
            return;
          }
          const message = `[${NodeName}] 与 [${nodeValue}] 添加新的关系,关系名称为 [${relationValue}]`;
          params.record.message = JSON.stringify({ message, name: NodeName });
          params.property = this.propertyServer;
          await updateNode(this.treeNode.id, params);
          this.$emit('journal');
          this.$emit('force');
          this.property.push({
            relation: relationValue,
            name: nodeValue,
            keyId: `${NodeName}-${relationValue}-${nodeValue}`,
            id: queryData[0].id,
          });
          this.nodeInput = '';
          this.relationInput = '';
          this.disabledFlag = false;
          if (type === 'relationEnter' || type === 'nodeEnter') {
            this.$nextTick(() => {
              if (type === 'relationEnter') relationDom.focus();
              else nodeDom.focus();
            });
          }
        },
        UPDATE_NODE_PROPERTYNAME: async () => {
          this.dropdownFlag = false;
          const childDom = this.$refs.propertyRef.children[propertyIndex];
          const relationDom = childDom.querySelector('.g-input-relation');
          const nodeDom = childDom.querySelector('.g-input-node');
          const nodeInputDom = nodeDom.querySelector('input');
          const nodeValue = nodeInputDom.value.replace(/\s+/g, '');
          const relationValue = this.property[propertyIndex].relation;
          const oldNodeName = this.property[propertyIndex].oldName;
          if (nodeValue === oldNodeName) return;
          if (nodeValue === NodeName) {
            this.$Message.warning({
              content: `节点名称与关系节点名称相同`,
              duration: 5,
            });
            handlerErr(relationDom, nodeDom, nodeInputDom);
            return;
          }
          this.sendLoading(nodeDom, 'start');
          this.sendLoading(relationDom, 'open');
          const queryData = await queryNode({ query: nodeValue, itemId });
          if (!IsnodeExists(queryData, nodeValue)) {
            handlerErr(relationDom, nodeDom, nodeInputDom);
            return;
          }
          const state = this.indexOfValue(
            relationValue,
            {
              name: nodeValue,
              id: queryData[0].id,
            },
            oldNodeName,
          );
          if (!state) {
            handlerErr(relationDom, nodeDom, nodeInputDom);
            this.$Message.warning({
              content: `${NodeName} ←  ${relationValue} → ${nodeValue} 关系已经存在`,
              duration: 5,
            });
            return;
          }
          const message = `[${oldNodeName}] 关系节点修改为 [${nodeValue}]`;
          params.record.message = JSON.stringify({ message, name: NodeName });
          params.property = this.propertyServer;
          await updateNode(this.treeNode.id, params);
          this.$emit('journal');
          this.$emit('force');
          const data = {
            relation: relationValue,
            name: nodeValue,
            oldName: nodeValue,
            keyId: `${NodeName}-${relationValue}-${nodeValue}`,
            id: queryData[0].id,
          };
          this.$set(this.property, propertyIndex, data);
        },
        UPDATE_NODE_PROPERTY: async () => {
          const childDom = this.$refs.propertyRef.children[propertyIndex];
          const relationDom = childDom.querySelector('.g-input-relation');
          const nodeDom = childDom.querySelector('.g-input-node');
          const relationInputDom = relationDom.querySelector('input');
          const relationViewValue = relationInputDom.value.replace(/\s+/g, '');
          const propertyValue = this.property[propertyIndex];
          if (relationViewValue === propertyValue.relation) return;
          this.sendLoading(relationDom, 'start');
          this.sendLoading(nodeDom, 'open');
          const state = this.indexOfValue(relationViewValue, {
            id: propertyValue.id,
            name: propertyValue.name,
          });
          if (!state) {
            handlerErr(nodeDom, relationDom, relationInputDom);
            this.$Message.warning({
              content: `${NodeName} ←  ${relationViewValue} → ${propertyValue.name} 关系已经存在`,
              duration: 5,
            });
            return;
          }
          const ROldIndex = this.propertyServer[propertyValue.relation]
            .map((item) => item.name)
            .indexOf(propertyValue.name);
          this.propertyServer[propertyValue.relation].splice(ROldIndex, 1);
          const message = `[${NodeName}] 与 [${propertyValue.name}] 之间的关系由 [${propertyValue.relation}] 修改为 [${relationViewValue}]`;
          params.record.message = JSON.stringify({ message, name: NodeName });
          params.property = this.propertyServer;
          await updateNode(this.treeNode.id, params);
          this.$emit('journal');
          this.$emit('force');
          const data = { ...this.property[propertyIndex] };
          data.relation = relationViewValue;
          data.keyId = `${NodeName}-${relationViewValue}-${propertyValue.name}`;
          this.$set(this.property, propertyIndex, data);
        },
        DELETE_NODE_PROPERTY: async () => {
          const propertyValue = this.property[propertyIndex];
          const relationValue = propertyValue.relation;
          const nodeValue = propertyValue.name;
          const message = `[${NodeName}] 与 [${nodeValue}] 不在拥有 [${relationValue}] 关系`;
          const childDom = this.$refs.propertyRef.children[propertyIndex];
          const relationDom = childDom.querySelector('.g-input-relation');
          const nodeDom = childDom.querySelector('.g-input-node');
          const ROldIndex = this.propertyServer[relationValue]
            .map((item) => item.name)
            .indexOf(nodeValue);
          this.sendLoading(nodeDom, 'start');
          this.sendLoading(relationDom, 'start');
          this.propertyServer[relationValue].splice(ROldIndex, 1);
          params.record.message = JSON.stringify({ message, name: NodeName });
          params.property = this.propertyServer;
          await updateNode(this.treeNode.id, params);
          this.$emit('journal');
          this.$emit('force');
          this.property.splice(propertyIndex, 1);
        },
      };
      const IsnodeExists = (queryData, nodeName) => {
        if (!queryData.length || queryData[0].name !== nodeName) {
          this.$Message.warning({
            content: '无法匹配到您输入的节点名称',
            duration: 5,
          });
          return false;
        }
        return true;
      };
      const handlerErr = (keyDom, valueDom, inputDom) => {
        this.sendLoading(valueDom, 'end');
        this.sendLoading(keyDom, 'close');
        inputDom.style.borderColor = '#ff9900';
      };
      let propertyIndex;
      if (propertyKeyId) {
        propertyIndex = this.property
          .map((item) => item.keyId)
          .indexOf(propertyKeyId);
      }
      stateMap[operator]();
    },
    // 查找值是否存在
    indexOfValue(key, value, repValue) {
      if (!this.propertyServer.hasOwnProperty(key)) {
        this.propertyServer[key] = [value];
        return true;
      }
      if (!this.propertyServer[key].some((item) => item.name === value.name)) {
        if (repValue) {
          const index = this.propertyServer[key]
            .map((item) => item.name)
            .indexOf(repValue);
          this.propertyServer[key].splice(index, 1, value);
        } else {
          this.propertyServer[key].push(value);
        }
        return true;
      }
      return false;
    },
    // 更新节点名称
    updateNodeEvent(keyId) {
      const index = this.property.map((item) => item.keyId).indexOf(keyId);
      const childDom = this.$refs.propertyRef.children[index];
      const nodeInput = childDom.querySelector('.g-input-node input');
      const queryValue = nodeInput.value.replace(/\s+/g, '');
      if (!queryValue) return;
      this.remoteMethod(queryValue);
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
          break;
        case 'end':
          prefixDom.className = 'ivu-icon ivu-icon-md-list';
          inputDom.disabled = '';
          break;
        case 'open':
          inputDom.disabled = 'disabled';
          break;
        case 'close':
          inputDom.disabled = '';
          break;
      }
      switch (state) {
        case 'start':
        case 'open':
          if (suffixDom) {
            suffixDom.style.cursor = 'default';
            suffixDom.classList.remove('g-del-data');
            suffixDom.style.color = '#ccc';
          }
          break;
        case 'end':
        case 'close':
          if (suffixDom) {
            suffixDom.style.cursor = '';
            suffixDom.classList.add('g-del-data');
            suffixDom.style.color = '';
          }
          break;
      }
    },
  },
};
</script>


<style scoped>
.g-input-wrap {
  display: flex;
}
.g-input-relation {
  width: 30%;
}
.g-input-node {
  flex: 1;
}
.g-property-wrap {
  position: relative;
}
.g-select-dropdown {
  position: absolute;
  left: 30%;
  width: 70%;
  margin: 5px 0;
  padding: 5px 0;
  box-shadow: 0 1px 6px rgba(0, 0, 0, 0.2);
  border-radius: 4px;
  background-color: #fff;
  z-index: 1998;
  overflow: hidden;
}
.g-ul {
  list-style: none;
}
.g-li {
  cursor: pointer;
  padding: 7px 16px;
}
.g-li:hover,
.g-li-focus {
  background-color: #f3f3f3;
}
.g-select-load {
  text-align: center;
  color: #c5c8ce;
  height: 35px;
  line-height: 35px;
  letter-spacing: 0.1em;
}
.g-loading {
  animation: ani-demo-load 2.5s linear infinite;
  font-size: 20px;
}
.g-del-data {
  cursor: pointer;
}
.g-del-data:hover {
  color: #ed4014;
  font-weight: bold;
}
</style>