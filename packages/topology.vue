<template>
  <div class="demo-topology">
    <cc-topology
        ref="topology"
        :graph-data="graphData"
        :node-app-config="nodeAppConfig"
        :edge-app-config="edgeAppConfig"
        :graph-mode-config="graphModeConfig"
        :node-type-list="nodeTypeList"
        @doAutoRefresh="doAutoRefresh"
        @doManualRefresh="doManualRefresh"
        @doChangeMode="doChangeMode"
        @doSaveData="doSaveData"
        @submitDataHandler="submitDataHandler"
        @reviewPassed="reviewPassed"
        @reviewRejected="reviewRejected"
    >
    </cc-topology>
  </div>
</template>

<script>
/* 局部注册 */
import {dataSave, dataParsing, review} from '@/api/top/top'
import {CCTopology} from '../../../packages/index'
import {deepClone} from '../../utils/index'

export default {
  name: 'DemoTopology',
  components: {
    'cc-topology': CCTopology
  },
  data() {
    return {
      eid: '',
      graphData: {},
      nodeTypeList: [],
      // 节点配置
      nodeAppConfig: {
        videoAddress: '视频地址',
        uuid: 'uuid',
        ip: '设备ip',
        gatewayType: '网关类型',
        equipmentNum: '设备序号',
        tubeNum: '管道号'
      },
      edgeAppConfig: {
        threshold: '阈值'
      },
      // todo 配置预览
      graphModeConfig: 'edit',
      autoRefreshTimer: null
    }
  },
  mounted() {
    this.eid = this.$route.query.eid
    let graphData = deepClone(this.graphData)
    this.$refs.topology.initTopo(graphData)
  },
  methods: {
    //todo
    doAutoRefresh(interval) {
      if (interval === -1) {
        clearInterval(this.autoRefreshTimer)
      } else {
        clearInterval(this.autoRefreshTimer)
        this.autoRefreshTimer = setInterval(() => {
        }, interval)
        this.$once('hook:beforeDestroy', () => {
          clearInterval(this.autoRefreshTimer)
        })
      }
    },
    doChangeMode(graphMode) {
      clearInterval(this.autoRefreshTimer)
      let graphData = deepClone(this.graphData)
      this.$refs.topology.changeGraphMode(graphData, graphMode)
    },
    doManualRefresh() {
      // this.randomChange()
    },
    doSaveData(graphData) {
      this.$XModal.confirm('您确定要保存该数据?').then(type => {
        if (type === 'confirm') {
          var data = {
            id: this.eid,
            dsdata: JSON.stringify(graphData)
          }
          this.uploadData(data)
        }
      })
    },
    submitDataHandler(graphData) {
      this.$XModal.confirm('您确定要提交该数据?').then(type => {
        if (type === 'confirm') {
          var data = {
            id: this.eid,
            dsdata: JSON.stringify(graphData)
          }
          this.submitData(data)
        }
      })
    },
    submitData(data) {
      dataParsing(data).then((res) => {
        let data = res.data
        if (data.code == 1) {
          this.$XModal.message({message: data.msg, status: 'success'})
          this.$router.replace('/pl')
        } else {
          this.$XModal.message({message: data.msg})
        }
      })
    },
    reviewPassed(state) {
      this.$XModal.confirm('您确定要通过该项目?').then(type => {
        if (type === 'confirm') {
          var data = {
            id: this.eid,
            state: state
          }
          this.uploadReview(data)
        }
      })
    },
    reviewRejected(state) {
      this.$XModal.confirm('您确定要拒绝该项目?').then(type => {
        if (type === 'confirm') {
          var data = {
            id: this.eid,
            state: state
          }
          this.uploadReview(data)
        }
      })
    },
    uploadReview(data) {
      var vm = this
      review(data).then((res) => {
        let data = res.data
        if (data.code == 1) {
          this.$XModal.message({message: data.msg, status: 'success'})
          vm.$router.replace('/pl')
        } else {
          this.$XModal.message({message: data.msg})
        }
      })
    },
    uploadData(datas) {
      dataSave(datas).then((res) => {
        let data = res.data
        if (data.code == 1) {
          this.$XModal.message({message: data.msg, status: 'success'})
          // vm.$router.replace('/pl')
        } else {
          this.$XModal.message({message: data.msg})
        }
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.demo-topology {
  width: 100%;
  height: 100%;
}
</style>

<style lang="scss" scoped>
html {
  overflow-x: hidden;
  overflow-y: hidden;
}
</style>
