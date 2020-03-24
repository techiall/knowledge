/**
*
*  @author ZTiger
*
*/


<template>
  <div>
    <headerView
      :ShowSMeg="ShowSMeg"
      :totalElements="totalElements"
      :findTime="findTime"
      :pageNum="pageNum"
      :loadingSpin="loadingSpin"
    />
    <div class="know-s-c-m-c">
      <div v-for="(item) in reqShowData" :key="item.nodeId" class="know-s-c-m-c-f">
        <div class="s-c-m-b">
          <router-link
            :to="{name:'nodeshow', params: {id:item.nodeId},query:{type:'force'}}"
          >
            <div class="s-c-m-c-title">{{item.nodeName}}</div>
          </router-link>
          <div class="know-s-c-m-c-user">
            来源:
            <span class="know-s-c-m-c-username">{{item.Itemsource}}</span>
          </div>
          <div class="know-s-c-m-attr" v-if="item.labels">
            <div class="know-s-c-m-attr-tittle">属性</div>
            <folding-panel>
              <span v-for="value in item.labels" :key="value" class="know-s-c-m-attr-d-list">
                <span>{{value}}</span>
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
  </div>
</template>

<script>
import foldingPanel from '@/components/foldingPanel';
import headerView from './headerView.vue';
export default {
  components: { foldingPanel, headerView },
  props: [
    'ShowSMeg',
    'totalElements',
    'reqShowData',
    'findTime',
    'pageNum',
    'loadingSpin',
  ],
  data() {
    return {
      //显示加载信息
      loadMsg: '加载更多',
      //显示加载标志位
      loadFlag: true,
    };
  },
  methods: {},
  watch: {},
};
</script>

<style  scoped src="../../../assets/css/searchMain.css">
</style>
