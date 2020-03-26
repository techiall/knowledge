/**
*
*  @author ZTiger
*
*/


<template>
  <div class="know-manage-split" :style="{height:ClientHeight}">
    <Spin size="large" fix v-if="loadingFlag" class="g-lodding"></Spin>
    <Split v-if="showErrorFlag" v-model="splitinit" :min="splitMin" :max="splitMax" >
      <div slot="left" class="know-manage-split-pane">
        <tree-list
          ref="treelist"
          :InnerHeight="InnerHeight - headerHeight"
          :itemId="itemId"
          :itemExitFlag="itemExitFlag"
          @MangageCallback="MangageCallback"
        ></tree-list>
      </div>
      <div slot="right" class="know-manage-split-pane know-manage-split-pane-right">
        <operate-main
          ref="operatemain"
          :InnerHeight="InnerHeight - headerHeight"
          :treeNode="treeNode"
          :itemId="itemId"
          :itemExitFlag="itemExitFlag"
          @MangageCallback="MangageCallback"
        ></operate-main>
      </div>
    </Split>
    <div v-else :style="{height:ClientHeight}">
      <div class="g-error-box">
        <div class="g-error-page"></div>
        <div class="g-error-title">抱歉，你访问的项目不存在。</div>
        <div>
          <Button type="primary">
            <router-link to="/project" class="g-error-a">返回项目管理页面</router-link>
          </Button>
        </div>
      </div>
    </div>
  </div>
</template>


<script>
//导入tree 模板
import treeList from './treeInterface/treelist';
// 导入 操作 模板
import operateMain from './operate/operateMain';
import { mapState } from 'vuex';

export default {
  components: { treeList, operateMain },
  data() {
    return {
      // 加载显示 标志位
      loadingFlag: true,
      // 显示 404 页面
      showErrorFlag: true,
      // 初始化 面板分割
      splitinit: 0.5,
      // 获取 innerHeight
      InnerHeight: '',
      // 获取 innerWidth
      InnerWidth: '',
      // Split 最小宽度
      splitMin: '300px',
      //Split 最大宽度
      splitMax: '500px',
      //树节点
      treeNode: '',
      // 项目id
      itemId: parseInt(this.$route.params.itemId),
      // 项目编辑 标志位
      itemExitFlag: false,
      // 获取项目 标志位
      getItemFlag: false,
    };
  },
  created() {},
  computed: {
    ...mapState(['headerHeight', 'user', 'nickName']),
    ClientHeight() {
      return this.InnerHeight - this.headerHeight + 'px';
    },
  },
  watch: {
    InnerWidth(val) {
      if (val < 800) {
        this.splitMax = '0px';
        this.splitinit = 1;
      } else {
        this.splitMax = '500px';
        this.splitinit = (parseInt(this.splitMin) + 50) / this.InnerWidth;
      }
    },
    'user.userName': {
      handler(val) {
        if (val) {
          this.getServerItem();
        }
      },
    },
  },
  mounted() {
    this.InnerHeight = window.innerHeight;
    this.InnerWidth = window.innerWidth;
    this.getServerItem();
    window.addEventListener('resize', this.getInner);
  },
  beforeDestroy() {
    window.removeEventListener('resize', this.getInner);
  },
  methods: {
    // 获取 浏览器 高度
    getInner() {
      this.InnerHeight = window.innerHeight;
      this.InnerWidth = window.innerWidth;
    },
    // 获取项目开始
    getServerItem() {
      if (!this.getItemFlag && this.user.userName) {
        this.getItemFlag = true;
        this.getItem();
      }
    },
    // 获取项目
    getItem() {
      const url = '/item/' + this.itemId;
      this.get(url)
        .then((res) => {
          this.loadingFlag = false;
          const data = res.data;
          const user = data.author.userName;
          const userWeb = this.$route.params.user;
          if (user === userWeb) {
            this.$emit('sendItem', data.name);
            this.setTitleLabel(data.name);
            if (this.user.userName === user) {
              this.itemExitFlag = true;
              this.$refs.treelist.TLCallback(15);
            } else if (data.share) {
              this.itemExitFlag = false;
              this.$refs.treelist.TLCallback(15);
            }
          } else {
            this.showErrorFlag = false;
          }
        })
        .catch(() => {
          this.showErrorFlag = false;
          this.loadingFlag = false;
        });
    },
    // 设置title标签
    setTitleLabel(title) {
      document.title = `${title} ● Knowledge Graph`;
    },
    // 子组件 调用函数
    MangageCallback(type, val) {
      const statusMap = {
        1: () => {
          this.treeNode = val;
        },
        //名称改变
        2: () => {
          this.$refs.treelist.TLCallback(3, val);
        },
        //  添加,删除,编辑 重新获取节点
        3: () => {
          this.$refs.operatemain.oMainCallback(3, val);
        },
        // 名称 改变 details 发送数据
        4: () => {
          this.$refs.operatemain.oMainCallback(4, val);
        },
      };
      statusMap[type]();
    },
  },
};
</script>


<style >
.know-manage-default {
  background-color: #fff;
}

.know-manage-split {
  background: #f0f2f5;
}
.know-manage-split-pane {
  padding: 0px;
  height: 100%;
}
.know-manage-split-pane-right {
  padding-left: 6px;
}
.g-error-box {
  margin: 0 auto;
  width: 400px;
  height: 600px;

  text-align: center;
}
.g-error-page {
  width: 400px;
  height: 400px;
  background: no-repeat url('../../assets/images/404.svg') center 100px;
}
.g-error-title {
  margin: 20px 0;
  color: rgba(0, 0, 0, 0.45);
}
.g-error-a,
.g-error-a:hover {
  color: #fff;
}
.g-lodding {
  z-index: 11;
}
@media screen and (max-width: 800px) {
  .know-manage-split-pane-right {
    display: none;
  }
  .right-pane.ivu-split-pane {
    display: none;
  }
  .ivu-split-trigger-con {
    display: none;
  }
}
</style>
