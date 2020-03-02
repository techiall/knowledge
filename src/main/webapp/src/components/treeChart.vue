/**
*
*  @author ZTiger
*
*/


<template>
  <div class="know-treeChart" ref="knowtree"></div>
</template>


<script>
//导入 d3 数据 包
import * as echarts from 'echarts';
export default {
  data() {
    return {};
  },
  methods: {
    //处理传来的数据
    handletreeData(data) {
      let child = this.ChildNodeProcessing([data.child]);
      let treeData = this.parentNodeProcessing(child, data.parent);
      this.CreateD3jsTree(JSON.stringify(treeData));
    },
    //子节点处理
    ChildNodeProcessing(child) {
      //遍历数据
      if (!child[0].child) {
        return [];
      }
      let getRandomColor = () => {
        let color = '#';
        for (let i = 0; i < 6; i++) {
          color += '0123456789abcdef'[Math.floor(Math.random() * 16)];
        }
        return color;
      };
      let nodeBFS = (data, val, color) => {
        let Arr = [];
        data.forEach((item) => {
          let randomColor = getRandomColor();
          let obj = {
            lineStyle: { color: color || randomColor },
            name: item.name,
          };
          if (item.child) {
            if (val)
              obj.children = nodeBFS(item.child, val, color || randomColor);
            else obj.children = nodeBFS(item.child, ++val);
          }
          Arr.push(obj);
        });
        return Arr;
      };

      return nodeBFS(child, 0);
    },
    //父亲节点处理
    parentNodeProcessing(childdata, parent) {
      let treeData = childdata.length ? childdata[0].children : [];
      parent = parent.reverse();

      parent.forEach((item) => {
        treeData = {
          name: item.name,
          children: treeData.constructor === Array ? treeData : [treeData],
        };
      });
      return treeData;
    },
    //树图渲染数据
    CreateD3jsTree(dataset) {
      let option = {
        tooltip: {
          trigger: 'item',
          triggerOn: 'mousemove',
        },
        series: [
          {
            type: 'tree',
            data: [JSON.parse(dataset)],
            top: '1%',
            left: '8%',
            bottom: '1%',
            right: '8%',
            symbolSize: 10,
            roam: true,
            label: {
              normal: {
                position: 'left',
                verticalAlign: 'middle',
                align: 'right',
                fontSize: 14,
              },
            },
            leaves: {
              label: {
                normal: {
                  position: 'right',
                  verticalAlign: 'middle',
                  align: 'left',
                },
              },
            },
            itemStyle: {
              borderColor: '#9D85FB',
            },
            lineStyle: { color: '#fb767b' },
            expandAndCollapse: true,
            animationDuration: 550,
            animationDurationUpdate: 750,
          },
        ],
      };

      let myChart = echarts.init(this.$refs.knowtree);
      myChart.clear();
      myChart.setOption(option);
    },
  },
};
</script>


<style scoped>
.know-treeChart {
  height: 100%;
  width: 100%;
}
</style>