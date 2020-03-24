/**
*
*  @author ZTiger
*
*/


<template>
  <div class="g-headerView-wrap">
    <div v-if="totalElements">
      <div class="g-select-drop cup">
        <Dropdown trigger="click" style="margin-left: 20px" @on-click="handlerList">
          <div class="g-select-content">
            <span>{{selectItem[selectIndex].text}}</span>
            <Icon type="ios-arrow-down" />
          </div>
          <DropdownMenu slot="list">
            <DropdownItem v-for="(item,index) in selectItem" :key="index" :name="index">
              <Icon v-show="index === selectIndex" type="md-checkmark" class="g-list-icon" />
              <span>{{item.text}}</span>
            </DropdownItem>
          </DropdownMenu>
        </Dropdown>
        <div class="g-result">
          <span>找到 {{totalElements}} 条结果</span>
          <span>（用时约 {{findTime|handleTime}} 秒）</span>
        </div>
      </div>
      <div class="g-exceeded">{{ShowSMeg|handleExceeded}}</div>
    </div>
    <no-findnode v-else-if="!loadingSpin" :ShowSMeg="ShowSMeg" />
  </div>
</template>


<script>
import noFindnode from './noFindnode.vue';

export default {
  components: { noFindnode },
  filters: {
    handleTime(time) {
      return (time / 1000).toFixed(2);
    },
    // 处理搜索字符串
    handleExceeded(content) {
      if (content.length >= 32) {
        return `  "${content.substr(
          32,
          3,
        )}" (及之后的字词)均被忽略，因为 知识图谱节点 的查询限制在 32 个词以内。`;
      }
      return '';
    },
  },
  props: {
    ShowSMeg: String,
    totalElements: Number,
    findTime: Number,
    pageNum: Number,
    loadingSpin: Boolean,
  },
  data() {
    return {
      selectItem: [
        {
          text: '创建时间',
          name: 'createTime,desc',
        },
        {
          text: '修改时间',
          name: 'updateTime,desc',
        },
        {
          text: '默认排序',
          name: 'default',
        },
      ],
      selectIndex: 2,
    };
  },
  watch: {
    $route: {
      handler(to) {
        if (to.query.sort) {
          this.selectIndex = to.query.sort === 'createTime,desc' ? 0 : 1;
        } else {
          this.selectIndex = 2;
        }
      },
      immediate: true,
    },
  },
  methods: {
    // 点击菜单项时触发
    handlerList(index) {
      this.selectIndex = parseInt(index, 10);
      const query = {
        q: this.ShowSMeg,
        page: this.pageNum,
      };
      if (index !== 2) query.sort = this.selectItem[index].name;
      this.$router.push({
        path: '/search',
        query,
      });
    },
  },
};
</script>


<style scoped>
.g-select-drop {
  margin: 0 0 10px;
}
.g-select-content span {
  margin: 0 3px 0 0;
}
.g-select-content span:hover {
  color: #1a73e8;
}
.g-result {
  display: inline-block;
  margin: 0 0 0 20px;
}
.ivu-dropdown-item {
  text-align: right;
  padding: 7px 16px 7px 0;
}
.g-list-icon {
  margin: 0 5px;
}
.g-exceeded {
  margin: 0 0 0 20px;
}
</style>
