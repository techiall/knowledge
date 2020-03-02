/**
*
*  @author ZTiger
*
*/


<template>
  <div class="know-force-wrap">
    <div class="know-force" ref="knowForce"></div>
    <!-- 后期加入点击 -->
    <!-- <div class="g-button-wrap">
      <div>1</div>
      <div>2</div>
      <div>3</div>
      <div>4</div>
      <div>5</div>
    </div>-->
  </div>
</template>

<script>
//导入 d3 数据 包
import * as d3 from 'd3';
export default {
  data() {
    return {
      fillColor: [
        '#f9320c',
        '#00b9f1',
        '#f9c00c',
        '#c886e5',
        '#bd83ce',
        '#EC6A5C',
        '#2c7873',
        '#ffd800',
        '#fa697c',
        '#75D701',
        '#ef9e9f',
        '#a8dba8',
        '#df42d1',
        '#e26241',
        '#30A9DE',
        '#eea5f6',
        '#56A902',
        '#fab1ce',
        '#A593E0',
        '#4FB0C6',
      ],
    };
  },
  methods: {
    //处理 组件传来的 数据
    handlecomponentsforceData(data) {
      let hashMap = {}; //映射
      let hashnumI = 0; // 映射计数器
      let hashLink = {}; // 相同数据何合并
      let Nodes = [];
      let Links = [];
      data.nodes.forEach((item, i) => {
        if (!hashMap[item.id]) {
          Nodes.push({
            name: item.name,
            id: i,
            Tid: item.id,
          });
          hashMap[item.id] = hashnumI++;
        }
      });
      data.links.forEach(function(item) {
        let index =
          Nodes[hashMap[item.source]]['Tid'] +
          '' +
          Nodes[hashMap[item.target]]['Tid'];
        if (!hashLink[index]) {
          hashLink[index] = {
            num: 0,
            pos: 0,
          };
        }
        let relation;
        let parent = false;
        if (item.relation === 'parent' || item.relation === 'child') {
          relation = '';
        } else {
          relation = item.relation;
        }
        if (item.relation === 'parent') {
          parent = true;
        }
        Links.push({
          relation: relation,
          hashPos: hashLink[index]['num']++,
          source: hashMap[item.source],
          target: hashMap[item.target],
        });
        Nodes[hashMap[item.target]]['parent'] = parent;
        Nodes[hashMap[item.target]]['Pid'] = item.source;
        Nodes[hashMap[item.target]]['PNid'] = hashMap[item.source];
        Nodes[hashMap[item.target]]['ColorId'] =
          Nodes[hashMap[item.source]]['ColorId'] + 1 || 0;
      });
      this.handleforceData(Nodes, Links, hashLink);
    },
    //处理 渲染
    handleforceData(Nodes, Links, hashLink) {
      if (this.$refs.knowForce.firstChild)
        this.$refs.knowForce.removeChild(this.$refs.knowForce.firstChild);
      this.CreateDjsPower(Nodes, Links, hashLink);
    },
    //创建 力导向图
    CreateDjsPower(nodes, edges, hashLink) {
      const width = this.$refs.knowForce.offsetWidth;
      const height = this.$refs.knowForce.offsetHeight;
      const nodeConf = {
        alpha: '4c',
        strokeWidth: 10,
        textFillColor: '#000',
        textWeight: 900,
        radius: {
          child: 20,
          root: 26,
        },
        TextFontSize: {
          child: '13px',
          root: '16px',
        },
      };
      const LineConf = {
        TextFontSize: 12,
      };
      let svg = d3
        .select(this.$refs.knowForce)
        .append('svg')
        .attr('width', width)
        .attr('height', height)
        .call(
          d3.zoom().on('zoom', function() {
            g.attr('transform', d3.event.transform);
          }),
        );
      let g = svg.append('g');
      //新建一个力导向图
      let forceSimulation = d3
        .forceSimulation()
        .force('link', d3.forceLink())
        .force('charge', d3.forceManyBody().strength(-200))
        .force('center', d3.forceCenter());
      //生成节点数据
      forceSimulation.nodes(nodes).on('tick', ticked); //这个函数很重要，后面给出具体实现和说明
      //生成边数据
      forceSimulation
        .force('link')
        .links(edges)
        .distance(150);
      //设置图形的中心位置
      forceSimulation
        .force('center')
        .x(width / 2)
        .y(height / 2);
      //有了节点和边的数据后，我们开始绘制
      svg
        .append('marker')
        .attr('id', 'resolved')
        .attr('markerUnits', 'userSpaceOnUse')
        .attr('viewBox', '0 -5 10 10') //坐标系的区域
        .attr('refX', nodeConf['radius']['root'] + 10) //箭头坐标
        .attr('refY', -0)
        .attr('markerWidth', 10) //标识的大小
        .attr('markerHeight', 8)
        .attr('orient', 'auto') //绘制方向，可设定为：auto（自动确认方向）和 角度值
        .attr('stroke-width', 10) //箭头宽度
        .append('path')
        .attr('d', 'M0,-5L10,0L0,5') //箭头的路径
        .attr('fill', '#8B8989'); //箭头颜色
      //绘制边
      let links = g
        .append('g')
        .selectAll('path')
        .data(edges)
        .enter()
        .append('path')
        .attr('id', function(d, i) {
          return 'edgepath' + i;
        })
        .attr('fill', 'transparent')
        .attr('stroke', '#EFEFEF')
        .style('pointer-events', 'none')
        .attr('stroke-width', 2);
      //绘制边上的文字
      let linksText = g
        .append('g')
        .selectAll('text')
        .data(edges)
        .enter()
        .append('g')
        .style('fill', '#EFEFEF')
        .append('text')
        .style('pointer-events', 'none');
      linksText
        .append('textPath')
        .attr('class', 'linksText')
        .attr('xlink:href', (d, i) => {
          return '#edgepath' + i;
        })
        .style('pointer-events', 'none')
        .style('font-weight', 100)
        .style('font-size', '15px')
        .attr('fill', '#000')
        .text(function(d) {
          return d.relation;
        })
        .attr('stroke', '#696969');
      //绘制节点
      //老规矩，先为节点和节点上的文字分组
      let gs = g
        .selectAll('.circleText')
        .data(nodes)
        .enter()
        .append('g')
        .attr('transform', function(d) {
          let cirX = d.x;
          let cirY = d.y;
          return 'translate(' + cirX + ',' + cirY + ')';
        })
        .call(
          d3
            .drag()
            .on('start', started)
            .on('drag', dragged)
            .on('end', ended),
        )
        .on('click', (d) => {
          this.$emit('callback', d);
        });
      //绘制节点
      gs.append('circle')
        .attr('r', (d, i) => {
          return nodeConf['radius'][i === 0 ? 'root' : 'child'];
        })
        .attr('fill', (d) => {
          let color = d.parent
            ? 0
            : (d.ColorId + 1 || 0) % this.fillColor.length;
          return this.fillColor[color];
        })
        .style('stroke-width', nodeConf['strokeWidth'])
        .style('cursor', 'pointer')
        .attr('stroke', (d) => {
          const color = d.parent
            ? 0
            : (d.ColorId + 1 || 0) % this.fillColor.length;
          return this.fillColor[color] + nodeConf['alpha'];
        });
      //文字
      gs.append('text')
        .attr('dy', function(d, i) {
          return i === 0 ? 5 : 4;
        })
        .attr('dx', (d, i) => {
          const fontSize = parseInt(
            i === 0
              ? nodeConf['TextFontSize']['root']
              : nodeConf['TextFontSize']['child'],
          );
          return (-1 * (fontSize * d.name.length)) / 2;
        })
        .style('fill', nodeConf['textFillColor'])
        .style('font-size', (d, i) => {
          return i === 0
            ? nodeConf['TextFontSize']['root']
            : nodeConf['TextFontSize']['child'];
        })
        .style('font-weight', 600)
        .style('cursor', 'pointer')
        .text((d) => {
          return d.name;
        });
      function ticked() {
        links.attr('d', function(d) {
          let sx = d.source.x;
          let sy = d.source.y;
          let tx = d.target.x;
          let ty = d.target.y;
          let r = Math.hypot(tx - sx, ty - sy);
          let id = d.source.Tid + '' + d.target.Tid;

          if (hashLink[id]['num'] === 1) {
            return `M${sx},${sy}A${0},${0} 0 0 1 ,${tx},${ty}`;
          } else if (hashLink[id]['num'] % 2 === 0) {
            let k = d.hashPos % 2;
            let value = (d.hashPos % 2 === 0 ? d.hashPos : d.hashPos - 1) * 30;
            return `M${sx},${sy}A${r - value},${r -
              value} 0 0 ${k},${tx},${ty}`;
          } else {
            let k = d.hashPos % 2;
            let value =
              (d.hashPos % 2 === 0 ? d.hashPos - 1 : d.hashPos - 0) * 30;
            if (d.hashPos === 0) {
              value = 0;
              r = 0;
            }
            return `M${sx},${sy}A${r - value},${r -
              value} 0 0 ${k},${tx},${ty}`;
          }
        });
        linksText
          .attr('transform', function(d) {
            if (d.target.x < d.source.x) {
              let bbox = this.getBBox();
              let rx = bbox.x + bbox.width / 2;
              let ry = bbox.y + bbox.height / 2;
              linksText.attr('dy', 8);
              return 'rotate(180 ' + rx + ' ' + ry + ')';
            } else {
              linksText.attr('dy', 5);
              return 'rotate(0)';
            }
          })
          .attr('dx', (d) => {
            const sr = nodeConf['radius'][d.source.PNid ? 'child' : 'root'];
            // const tr = nodeConf["radius"][d.target.PNid ? "child" : "root"];
            const sx = d.source.x;
            const sy = d.source.y;
            const tx = d.target.x;
            const ty = d.target.y;
            const distance = Math.hypot(tx - sx, ty - sy);
            const textLength = d.relation.length;
            const deviation = 16; //调整误差
            const dx =
              (distance - sr - textLength * LineConf['TextFontSize']) / 2 +
              deviation;
            return dx;
          });
        gs.attr('transform', function(d) {
          return 'translate(' + d.x + ',' + d.y + ')';
        });
      }
      function started(d) {
        if (!d3.event.active) {
          forceSimulation.alphaTarget(0.8).restart();
        }
        d.fx = d.x;
        d.fy = d.y;
      }
      function dragged(d) {
        d.fx = d3.event.x;
        d.fy = d3.event.y;
      }
      function ended(d) {
        if (!d3.event.active) {
          forceSimulation.alphaTarget(0);
        }
        d.fx = null;
        d.fy = null;
      }
    },
  },
};
</script>

<style  scoped>
.know-force,
.know-force-wrap {
  height: 100%;
  width: 100%;
}
.g-button-wrap {
  position: absolute;
  display: flex;
  justify-content: center;
  left: 0;
  top: 0;
  width: 100%;
  user-select: none;
}
.g-button-wrap div {
  width: 20px;
  height: 20px;
  margin: 0 20px;
  text-align: center;
}
</style>