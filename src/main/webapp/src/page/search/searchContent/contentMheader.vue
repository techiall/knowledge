/**
*
*  @author ZTiger
*
*/


<template>
  <div class>
    <div class="s-c-m-b">
      <div class="know-s-c-m-h">
        <div class="know-s-c-m-ht">
          <div class="know-s-c-m-htt">{{ShowSMeg}}</div>
          <div class="know-s-c-m-htt-s">实体</div>
        </div>
        <div class="know-s-c-m-htn" title="总数据量">{{totalElements}}</div>
      </div>
    </div>

    <div class="know-s-c-m-c">
      <div v-for="(item) in reqShowData" :key="item.nodeId" class="know-s-c-m-c-f">
        <div class="s-c-m-b">
          <router-link
            :to="{name:'nodeshow', params: {id:item.nodeId,name:item.nodeName},query:{type:'force'}}"
          >
            <div class="s-c-m-c-title" v-html="item.nodeTitleName"></div>
          </router-link>
          <div class="know-s-c-m-c-user">
            来源:
            <span class="know-s-c-m-c-username">{{item.Itemsource}}</span>
          </div>
          <div class="know-s-c-m-attr" v-if="item.labels">
            <div class="know-s-c-m-attr-tittle">属性</div>
            <folding-panel>
              <span v-for="value in item.labels" :key="value" class="know-s-c-m-attr-d-list">
                “
                <span>{{value}}</span>”
              </span>
            </folding-panel>
          </div>
          <div class="know-s-c-m-attr" v-if="item.property.length >0">
            <div class="know-s-c-m-attr-tittle">关系</div>
            <folding-panel :showHeight="115">
              <div class="know-property-list">
                <div
                  v-for="(value, index) in item.property"
                  :key="index"
                  class="know-property-list-row dis-fix"
                >
                  <span>{{value.title}}</span>
                  <span>{{value.name}}</span>
                </div>
              </div>
            </folding-panel>
          </div>
          <div class="know-s-c-m-attr">
            <div class="know-s-c-m-attr-tittle">描述</div>
            <div class="s-c-m-c-text" v-html="item.text?item.text+'...':'没有查询到该节点描述信息。'"></div>
          </div>
        </div>
      </div>
    </div>
    <div
      class="know-s-c-m-footer"
      @click="getServerPage"
      v-show="loadFlag"
      :class="{'know-s-c-m-footer-load':loadMsg === '数据加载'}"
    >
      <div class="know-s-c-m-footer-title">{{loadMsg}}</div>
    </div>
  </div>
</template>

<script>
import foldingPanel from '../../../components/foldingPanel';
export default {
  components: { foldingPanel },
  props: [
    'ShowSMeg',
    'totalElements',
    'reqShowData',
    'pageNum',
    'totalPages',
    'reqSuccessFlag',
  ],
  data() {
    return {
      //显示加载信息
      loadMsg: '加载更多',
      //显示加载标志位
      loadFlag: true,
    };
  },
  methods: {
    // 点击加载数据请求分页
    getServerPage() {
      if (this.loadMsg !== '数据加载') {
        this.loadMsg = '数据加载';
        this.$emit('contentCallback', 1);
      }
    },
    // 点击获取节点信息
    getNodeInmes(index) {
      this.$emit('contentCallback', 2, index);
    },
  },
  watch: {
    reqSuccessFlag() {
      this.loadMsg = '加载更多';
      if (this.pageNum + 1 === this.totalPages || this.totalPages === 0) {
        this.loadFlag = false;
      } else {
        this.loadFlag = true;
      }
    },
  },
};
</script>

<style >
.s-c-m-c-t-u {
  color: #2d8cf0;
}
.know-s-c-m-c-user {
  display: inline;
  color: #808695;
  font-size: 12px;
  margin-left: 20px;
}
</style>
<style  scoped>
.s-c-m-b {
  border: 1px solid #dcdee2;
  border-radius: 5px;
  padding: 8px;
}
.know-s-c-m-h {
  width: 100%;
  display: flex;
  align-items: center;
}
.know-s-c-m-ht {
  padding: 0px 0 5px 10px;
  flex: 1;
}
.know-s-c-m-htt {
  font-size: 18px;
  color: #17233d;
}
.know-s-c-m-htt-s {
  margin-top: 3px;
  font-size: 12px;
  color: #808695;
}
.know-s-c-m-htn {
  border: 2px solid #19be6b;
  width: 30px;
  height: 30px;
  text-align: center;
  line-height: 25px;
  border-radius: 15px;
  cursor: Default;
  user-select: none;
}
.know-s-c-m-attr {
  border-top: 1px solid #dcdee2;
  padding: 5px 0 10px;
}
.know-s-c-m-attr-tittle {
  padding: 5px 0 5px 10px;
  font-size: 13px;
  font-weight: bold;
  color: #14181c;
}
.know-s-c-m-attr-d-list {
  display: inline-block;
  font-size: 14px;
  margin: 5px 5px 2px 3px;
  cursor: default;
  font-family: 'Helvetica Neue', Helvetica, 'PingFang SC', 'Hiragino Sans GB',
    'Microsoft YaHei', '微软雅黑', Arial, sans-serif;
}
.know-s-c-m-attr-d-list span {
  display: inline-block;
  border-bottom: 1.2px solid #28a745;
  padding: 0 5px 2px 5px;
}
.know-s-c-m-attr-footr {
  text-align: center;
}

.know-s-c-m-attr-footr-icon {
  font-size: 16px;
  color: #808695;
  cursor: pointer;
}
.s-c-m-c-title {
  display: inline-block;
  font-size: 18px;
  color: #14181c;
  cursor: pointer;
  padding: 0 10px 10px;
}

.s-c-m-c-text {
  margin-top: 8px;
  padding: 0 0 0 10px;
  word-wrap: break-word;
  word-break: normal;
}

.know-s-c-m-c-f {
  margin-top: 25px;
}

.know-s-c-m-footer {
  background-color: #e8eaec;
  border-radius: 4px;
  margin-bottom: 10px;
  cursor: pointer;
  padding: 3px;
}
.know-s-c-m-footer:active {
  background-color: #f8f8f9;
}
.know-s-c-m-footer-load {
  background-color: #f8f8f9;
}
.know-s-c-m-footer-load .know-s-c-m-footer-title {
  color: #e8eaec;
}
.know-s-c-m-footer-title {
  text-align: center;
  font-size: 15px;
}
.know-property-list {
  display: flex;
  flex-wrap: wrap;
}
.know-property-list-row {
  padding: 8px 10px;
  margin: 5px;
  width: calc(50% - 10px);
  background: #f7f8f9;
  border-radius: 8px;
}
.know-property-list-row span {
  display: inline-block;
  height: 30px;
  line-height: 30px;
}
.know-property-list-row span:last-of-type {
  background: rgba(40, 167, 69, 0.1);
  padding: 0 5px;
  border-radius: 4px;
  border-bottom: 2px solid #28a745;
}
.know-s-c-m-c-f:last-of-type {
  margin: 25px 0;
}
</style>
