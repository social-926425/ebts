<template>
  <div class="demo-topology">
    <topology
      ref="topology"
      :graph-data="graphData"
      :node-app-config="nodeAppConfig"
      @doAutoRefresh="doAutoRefresh"
      @doManualRefresh="doManualRefresh"
      @doChangeMode="doChangeMode"
      @doSaveData="doSaveData"
    >
    </topology>
  </div>
</template>

<script>
/* 局部注册 */
import Topology from "./packages/topology/src/topology";
import { deepClone } from "./utils/index";

export default {
  name: "DemoTopology",
  components: {
    "topology": Topology
  },
  data() {
    return {
      graphData: {
        nodes: [
          {
            "id": "info",
            "label": "Employee",
            "attrs": [{
              "key": "id",
              "type": "number(6)"
            },
              {
                "key": "key",
                "type": "varchar(255)"
              },
              {
                "key": "gender",
                "type": "enum(M, F)"
              },
              {
                "key": "birthday",
                "type": "date"
              },
              {
                "key": "hometown",
                "type": "varchar(255)"
              },
              {
                "key": "country",
                "type": "varchar(255)"
              },
              {
                "key": "nation",
                "type": "varchar(255)"
              },
              {
                "key": "jobId",
                "type": "number(3)",
                "relation": [{
                  "key": "id",
                  "nodeId": "job"
                }]
              },
              {
                "key": "phone",
                "type": "varchar(255)"
              },
              {
                "key": "deptId",
                "type": "number(6)",
                "relation": [{
                  "key": "id",
                  "nodeId": "dept"
                }]
              },
              {
                "key": "startTime",
                "type": "date"
              },
              {
                "key": "leaveTime",
                "type": "date"
              }
            ]
          },
          {
            "id": "job",
            "label": "Job",
            "attrs": [{
              "key": "id",
              "type": "number(3)"
            },
              {
                "key": "title",
                "type": "varchar(255)"
              },
              {
                "key": "level",
                "type": "number(3)"
              }
            ]
          },
          {
            "id": "dept",
            "label": "Department",
            "attrs": [{
              "key": "id",
              "type": "number(6)"
            },
              {
                "key": "title",
                "type": "varchar(255)"
              },
              {
                "key": "desc",
                "type": "text"
              },
              {
                "key": "parent",
                "type": "number(6)",
                "relation": [{
                  "key": "id",
                  "nodeId": "dept"
                }]
              },
              {
                "key": "manager",
                "type": "number(6)"
              }
            ]
          }
        ],
        edges: [
          {
            "source": "info",
            "target": "job",
            "sourceKey": "jobId",
            "targetKey": "id"
          },
          {
            "source": "info",
            "target": "dept",
            "sourceKey": "deptId",
            "targetKey": "id"
          },
          {
            "source": "dept",
            "target": "dept",
            "sourceKey": "parent",
            "targetKey": "id"
          }
        ]
      },
      nodeTypeList: [
        // { guid: "blue", label: "蓝色", imgSrc: require("../../assets/images/blue.svg") },
        // { guid: "green", label: "绿色", imgSrc: require("@/assets/images/green.svg") },
        // { guid: "purple", label: "紫色", imgSrc: require("@/assets/images/purple.svg") }
      ],
      // 节点配置
      nodeAppConfig: {
        ip: "节点IP",
        port: "节点端口",
        sysName: "设备名称"
      },
      autoRefreshTimer: null
    };
  },
  mounted() {
    let graphData = deepClone(this.graphData);
    this.$refs.topology.initTopo(graphData);
    this.randomChange();
  },
  methods: {
    doAutoRefresh(interval) {
      if (interval === -1) {
        clearInterval(this.autoRefreshTimer);
      } else {
        clearInterval(this.autoRefreshTimer);
        this.autoRefreshTimer = setInterval(() => {
          this.randomChange();
        }, interval);
        this.$once("hook:beforeDestroy", () => {
          clearInterval(this.autoRefreshTimer);
        });
      }
    },
    doChangeMode(graphMode) {
      clearInterval(this.autoRefreshTimer);
      let graphData = deepClone(this.graphData);
      this.$refs.topology.changeGraphMode(graphData, graphMode);
    },
    doManualRefresh() {
      this.randomChange();
    },
    doSaveData(graphData) {
      console.log(JSON.stringify(graphData));
    },
    randomChange() {
      let graphData = deepClone(this.$refs.topology.getGraphData());
      let { nodes } = graphData;
      this.$refs.topology.changeGraphData(graphData);
    }
  }
};
</script>

<style lang="scss" scoped>
.demo-topology {
  width: 100%;
  height: 100%;
}
</style>

<style lang="scss">
html {
  overflow-x: hidden;
  overflow-y: hidden;
}
</style>
