/**
 * @author: winyuan
 * @data: 2019/07/16
 * @repository: https://github.com/winyuan
 * @description: edit mode: 鼠标点击交互
 */

// 用来获取调用此js的vue组件实例（this）
let vm = null

const sendThis = (_this) => {
  vm = _this
}

export default {
  sendThis, // 暴露函数
  name: 'click-event-edit',
  options: {
    getEvents() {
      return {
        'node:click': 'onNodeClick',
        'node:contextmenu': 'onNodeRightClick',
        'edge:click': 'onEdgeClick',
        'edge:contextmenu': 'onEdgeRightClick',
        'canvas:click': 'onCanvasClick'
      }
    },
    onNodeClick(event) {
      // todo..."selected"是g6自带的状态         ，在"drag-add-edge"中的"node:mouseup"事件也会触发，故此处不需要设置"selected"状态
      //点击事件
      // let clickNode = event.item;
      // clickNode.setState('selected', !clickNode.hasState('selected'));
      vm.currentFocus = 'node'
      vm.rightMenuShow = false
      this.updateVmData(event)
    },
    onNodeRightClick(event) {
      console.log(event,'88888888888888888888888888888')
      let graph = vm.graph
      let clickNode = event.item
      let clickNodeModel = clickNode.getModel()
      let selectedNodes = graph.findAllByState('node', 'selected')
      let selectedNodeIds = selectedNodes.map(item => {return item.getModel().id})
      vm.selectedNode = clickNode
      // 如果当前点击节点是之前选中的某个节点，就进行下面的处理
      if (selectedNodes.length > 1 && selectedNodeIds.indexOf(clickNodeModel.id) > -1) {
        vm.rightMenuShow = true
        let rightMenu = vm.$refs.rightMenu
        rightMenu.style.left = event.clientX + 'px'
        rightMenu.style.top = event.clientY + 'px'
      } else {
        // 隐藏右键菜单
        vm.rightMenuShow = false
        // 先取消所有节点的选中状态
        selectedNodes.forEach(node => {
          node.setState('selected', false)
        })
        // 再添加该节点的选中状态
        clickNode.setState('selected', true)
        vm.currentFocus = 'node'
        this.updateVmData(event)
      }
      graph.paint()
    },
    onEdgeClick(event) {
      // todo
      let clickEdge = event.item
      // // todo 入口
      clickEdge.setState('selected', !clickEdge.hasState('selected'))


      this.onEdgeRightClick(event)


      vm.currentFocus = 'edge'
      vm.rightMenuShow = false
      this.updateVmData(event)
    },
    onEdgeRightClick(event) {
      let graph = vm.graph
      let clickEdge = event.item
      let clickEdgeModel = clickEdge.getModel()
      let selectedEdges = graph.findAllByState('edge', 'selected')
      // 如果当前点击节点不是之前选中的单个节点，才进行下面的处理
      if (!(selectedEdges.length === 1 && clickEdgeModel.id === selectedEdges[0].getModel().id)) {
        // 先取消所有节点的选中状态
        selectedEdges.forEach(edge => {
          edge.setState('selected', false)
        })
        // 再添加该节点的选中状态
        clickEdge.setState('selected', true)
        vm.currentFocus = 'edge'
        this.updateVmData(event)
      }
      // eslint-disable-next-line no-unused-vars
      let point = { x: event.x, y: event.y }
    },
    onCanvasClick() {
      vm.currentFocus = 'canvas'
      vm.rightMenuShow = false
    },
    updateVmData(event) {
      if (event.item._cfg.type === 'node') {
        // 更新vm的data: selectedNode 和 selectedNodeParams
        //TODO 修改右侧输入信息
        let clickNode = event.item
        if (clickNode.hasState('selected')) {
          console.log(clickNode.getModel())

          let clickNodeModel = clickNode.getModel()
          vm.selectedNode = clickNode
          //todo 修改右侧输入信息
          let nodeAppConfig = { ...vm.nodeAppConfig }


          // console.log(nodeAppConfig,'nodeAppConfig')



          Object.keys(nodeAppConfig).forEach(function(key) {
            nodeAppConfig[key] = ''
          })
          let uuids =[]
          for (let i = 0;i<clickNodeModel.uuids.length;i++){
            let uuidItem = {
              value:clickNodeModel.uuids[i],
              label:clickNodeModel.uuids[i]
            }
            uuids.push(uuidItem)
          }
          vm.selectedNodeParams = {
            label: clickNodeModel.label || '',
            uuids: uuids,
            number: clickNodeModel.number,
            appConfig: { ...nodeAppConfig, ...clickNodeModel.appConfig }
          }
        }
      } else if (event.item._cfg.type === 'edge') {
        // 更新vm的data: selectedEdge 和 selectedEdgeParams
        let clickEdge = event.item
        if (clickEdge.hasState('selected')) {
          let clickEdgeModel = clickEdge.getModel()
          vm.selectedEdge = clickEdge
          let edgeAppConfig = { ...vm.edgeAppConfig }
          Object.keys(edgeAppConfig).forEach(function(key) {
            edgeAppConfig[key] = ''
          })
          vm.selectedEdgeParams = {
            label: clickEdgeModel.label || '',
            type:clickEdgeModel.type,
            style: {
              stroke: clickEdgeModel.style.stroke || '',
            },
            appConfig: { ...edgeAppConfig, ...clickEdgeModel.appConfig }
          }
          console.log(vm.selectedEdgeParams)
        }
      }
    }
  }
}
