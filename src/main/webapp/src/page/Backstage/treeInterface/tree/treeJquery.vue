/**
*
*  @author ZTiger
*
*/


<template>
  <div>
    <ul id="ztreeDemo" class="ztree"></ul>
  </div>
</template>

<script>
import $ from '../../../../assets/jquery-vendor';
import 'ztree';
import 'ztree/css/metroStyle/metroStyle.css';
export default {
  props: ['treelistVal', 'itemId'],
  data() {
    return {
      //设置 ztree 树
      selectTreeSetting: {
        view: {
          howLine: false,
          showIcon: false,
          selectedMulti: true,
          dblClickExpand: true,
          addDiyDom: this.addDiyDom,
        },
        data: {
          simpleData: {
            enable: true,
          },
        },
        edit: {
          drag: {
            isMove: true,
            inner: true,
          },
          enable: false,
          showRemoveBtn: false,
          showRenameBtn: false,
        },
        callback: {
          beforeDblClick: this.showChildClik,
          beforeExpand: this.showChildClik,
          onClick: this.zTreeOnClick,
          beforeDrop: this.zTreeBeforeDrop,
          beforeDragOpen: this.zTreeBeforeDragOpen,
        },
      },
      //点击后ztree节点 id
      StreeId: '',
      // 点击后ztree 节点
      StreeNode: '',
      // 树
      zTree: '',
    };
  },
  mounted() {},
  methods: {
    //获取服务器数据
    getTreeData() {
      const url = 'node';
      const obj = {
        itemId: this.itemId,
      };
      this.get(url, obj)
        .then((res) => {
          this.HandleData(res.data);
        })
        .catch(() => {});
    },
    // 请求数据处理
    HandleData(data) {
      let Arr = [];
      this.$emit('selectNode', 9, data.length);
      data.forEach((item) => {
        let obj = {
          id: item.id,
          name: item.name,
          isParent: item.child,
        };
        Arr.push(obj);
      });
      this.createTree(Arr);
    },
    //添加节点 修改样式
    addDiyDom(treeId, treeNode) {
      let spaceWidth = 15;
      let switchObj = $('#' + treeNode.tId + '_switch');
      let icoObj = $('#' + treeNode.tId + '_ico');
      switchObj.remove();
      icoObj.before(switchObj);
      if (treeNode.level > 0) {
        let spaceStr = `<span  id='${
          treeNode.tId
        }_space' style='display: inline-block;width:${spaceWidth *
          treeNode.level}px'></span>`;
        switchObj.before(spaceStr);
      }
    },
    //创建 树图 修改样式
    createTree(nodes) {
      $.fn.zTree.init($('#ztreeDemo'), this.selectTreeSetting, nodes);
      this.zTree = $.fn.zTree.getZTreeObj('ztreeDemo');
    },
    //点击 节点 前 响应函数
    beforeMouseUp(treeId, treeNode) {
      if (!treeNode) return;
      this.StreeId = treeNode.tId;
      this.StreeNode = treeNode;
      this.$emit('selectNode', 1, true);
      this.$emit('selectNode', 2, treeNode);
    },
    // 双击 节点 展开 节点 函数
    showChildClik(treeId, treeNode, callback, parameter) {
      if (!treeNode) return;
      if (!treeNode.isParent || treeNode.asyncParent) return 1;
      //异步加载 防止重复加载
      treeNode.asyncParent = true;
      let url = 'node/' + treeNode.id + '/child';
      this.get(url)
        .then((res) => {
          let data = res.data;
          let Arr = [];
          data.forEach((item) => {
            Arr.push({
              id: item.id,
              name: item.name,
              isParent: item.child,
            });
          });
          this.zTree.addNodes(treeNode, Arr, false);
          if (callback) {
            callback(...parameter);
          }
        })
        .catch(() => {});
    },
    // 点击 节点 后
    zTreeOnClick(event, treeId, treeNode) {
      if (!treeNode) return;
      else if (event.ctrlKey) {
        this.$emit('selectNode', 10, this.zTree.getSelectedNodes());
      } else {
        this.StreeId = treeNode.tId;
        this.StreeNode = treeNode;
        this.$emit('selectNode', 1, true);
        this.$emit('selectNode', 2, treeNode);
        this.$emit('selectNode', 11, []);
      }
    },
    // 用于捕获拖拽节点移动到折叠状态的父节点后
    zTreeBeforeDragOpen() {
      return false;
    },
    // 用于捕获节点拖拽操作结束之前的事件回调函数，并且根据返回值确定是否允许此拖拽操作
    zTreeBeforeDrop(treeId, treeNodes, targetNode, moveType) {
      if (!(targetNode === null || moveType !== 'inner')) {
        let treeNode = treeNodes[0];
        if (
          this.showChildClik('', targetNode, this.pushServeDrop, {
            treeNode,
            targetNode,
          }) === 1
        ) {
          // 加载过数据
          this.pushServeDrop(treeNode, targetNode);
        }
      }
      return false;
    },
    // 拖拽结束,并向服务器发送请求成功
    DropSuccess(treeNodes) {
      let spaceWidth = 15;
      let treeNodeBFS = (Node) => {
        let spaceNum =
          parseInt($(`#${Node.parentTId}_space`).css('width') || 0) /
            spaceWidth +
          1;
        $(`#${Node.tId}_space`).css('width', spaceWidth * spaceNum + 'px');
        if (Node.isParent && Node.asyncParent) {
          let childNode = Node.children;
          childNode.forEach((item) => {
            treeNodeBFS(item);
          });
        }
      };
      treeNodes.forEach((item) => {
        treeNodeBFS(item);
      });
    },
    // 向服务器发送拖拽的
    pushServeDrop(treeNode, targetNode) {
      const URL = `node/${treeNode.id}/movement?target=${targetNode.id}`;
      this.put_json(URL)
        .then(() => {
          this.zTree.moveNode(targetNode, treeNode, true);
          this.DropSuccess([treeNode]);
        })
        .catch(() => {});
    },
  },
  watch: {
    'treelistVal.ExitName': {
      handler: function(val) {
        $('#' + this.StreeId + '_a').attr('title', val);
        $('#' + this.StreeId + '_span').text(val);
        this.StreeNode['name'] = val;
        this.$emit('selectNode', 2, this.StreeNode);
      },
    },
    'treelistVal.addName': {
      handler: function(val) {
        if (this.StreeNode) {
          //判断 是否双击 点击过
          if (this.showChildClik('', this.StreeNode) === 1) {
            this.StreeNode.asyncParent = true;
            this.zTree.addNodes(this.StreeNode, {
              id: val.id,
              name: val.name,
              isParent: false,
            });
          }
        } else {
          this.zTree.addNodes(null, {
            id: val.id,
            name: val.name,
            isParent: false,
          });
        }
      },
    },
    'treelistVal.delName': {
      handler: function() {
        this.zTree.removeNode(this.StreeNode, false);
        this.StreeNode = '';
        this.$emit('selectNode', 1, false);
      },
    },
    'treelistVal.delNodes': {
      handler: function() {
        let Nodes = this.zTree.getSelectedNodes();
        Nodes.forEach((item) => {
          if (this.StreeNode.id === item.id) {
            this.StreeNode = '';
            this.$emit('selectNode', 1, false);
            this.$emit('selectNode', 13);
          }
          this.zTree.removeNode(item, false);
        });
        if (this.StreeNode) {
          this.zTree.selectNode(this.StreeNode);
          this.$emit('selectNode', 1, true);
        }
        this.$emit('selectNode', 11, []);
      },
    },
  },
};
</script>

<style>
.ztree * {
  font-size: 13px;
}
.ztree li {
  line-height: 30px;
}
.ztree li ul {
  margin: 0;
  padding: 0;
}

.ztree li a {
  text-overflow: ellipsis;
  overflow: hidden;
  width: 100%;
  height: 30px;
  padding-top: 0px;
  color: rgba(0, 0, 0, 0.65);
}

.ztree li a:hover {
  text-decoration: none;
  background-color: #eaf4fe;
}
.ztree.showIcon li a span.button.switch {
  visibility: visible;
}

.ztree li a.curSelectedNode {
  background-color: #d5e8fc;
  border: 0;
  height: 30px;
  color: #000;
}

.ztree li span {
  line-height: 30px;
}
.ztree li span.button.switch {
  width: 16px;
  height: 16px;
}

.ztree li span.button.switch.level0 {
  width: 20px;
  height: 20px;
}

.ztree li span.button.switch.level1 {
  width: 20px;
  height: 20px;
}
.ztree li span.button,
.ztree li ul.line {
  background-image: none;
}
.ztree li span.button {
  display: inline-block;
  font-family: Ionicons;
  speak: none;
  font-style: normal;
  font-weight: 400;
  font-variant: normal;
  text-transform: none;
  text-rendering: optimizeLegibility;
  line-height: 1;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  vertical-align: -0.125em;
  text-align: center;
}
.ztree li span.button.root_close::before,
.ztree li span.button.roots_close::before,
.ztree li span.button.bottom_close::before,
.ztree li span.button.center_close::before {
  content: '\f11f';
  font-size: 15px;
}
.ztree li span.button.root_open::before,
.ztree li span.button.roots_open::before,
.ztree li span.button.bottom_open::before,
.ztree li span.button.center_open::before {
  content: '\F116';
  font-size: 15px;
}
.ztree li a.tmpTargetNode_inner {
  height: 30px;
  background: #2d8cf0;
  border: none;
}
ul.ztree.zTreeDragUL {
  z-index: 12;
  border: none;
  padding: 0 20px 0 0;
  background: #f8f8f9;
  border-radius: 8px;
}
span.tmpzTreeMove_arrow {
  background-image: none;
}
</style>