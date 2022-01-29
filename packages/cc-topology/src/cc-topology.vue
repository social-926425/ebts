<template>
    <div class="topology">
        <!-- toolbar -->
        <div v-if="toolbarShow">
            <toolbar-edit></toolbar-edit>
        </div>

        <!-- container -->
        <div class="container" :class="graphBg" @contextmenu.prevent>
            <!-- item-pannel -->
            <div v-if="graphMode === 'edit'" class="left">
                <div v-for="nodeType in nodeTypeList" class="top-nav-confg">
                    <div class="top-nav" v-on:click="showtopnav($event)">{{ nodeType.name }}</div>
                    <div class="top-img">
                        <div width="10px" height="10px"
                             class="node-type"
                             v-for="node in nodeType.nodeList"
                             :key="node.guid">
                            <img
                                    :src="node.imgSrc"
                                    :alt="node.label"
                                    :title="node.label"
                                    draggable="true"
                                    @dragstart="dragstartHandler($event, node)"
                                    @dragend="dragendHandler"
                            />
                            <p style="
                           text-align: center;
                           color: #272727;
                           display: block;
                           overflow: hidden;
                           white-space: nowrap;
                           text-overflow: ellipsis;
                           width: 60px;">{{node.label}}</p>
                        </div>
                    </div>
                </div>

            </div>
            <!--             graph-container-->
            <div
                    class="center graph-container"
                    :style="{'width': graphMode === 'edit' ? '76%': '100%'}"
                    ref="graphContainer">
                <div
                        id="mount-topology"
                        @dragenter="dragenterHandler"
                        @dragover="dragoverHandler"
                        @drop="dropHandler"
                >
                </div>
            </div>
            <!-- graph-pannel -->
            <div v-if="graphMode === 'edit'" class="right">
                <div class="detail-pannel">
                    <div v-if="currentFocus === 'node'">
                        <div class="pannel-title">节点</div>
                        <div class="block-container">
                            <span>名称</span>
                            <!-- TODO 右侧状态栏-->
                            <el-input class="block-container-padding" v-model="selectedNodeParams.label"
                                      size="mini"></el-input>
                            <span v-if="selectedNodeParams.number==1">
                            <span>视频地址</span>
                            <el-input class="block-container-padding"
                                      v-model="selectedNodeParams.appConfig.videoAddress" size="mini"></el-input>
                            <span>设备序号</span>
                            <el-input class="block-container-padding"
                                      v-model="selectedNodeParams.appConfig.equipmentNum" size="mini"></el-input>
                            <span>管道号</span>
                            <el-input class="block-container-padding"
                                      v-model="selectedNodeParams.appConfig.tubeNum" size="mini"></el-input>
                            </span>
                            <span>
                                <span>uuid</span>
                            <el-select class="block-container-padding" v-model="selectedNodeParams.appConfig.uuid"
                                       size="mini"
                                       placeholder="选择uuid">
                                <el-option
                                        v-for="item in selectedNodeParams.uuids"
                                        :key="item.value"
                                        :label="item.label"
                                        :value="item.value">
                                </el-option>
                            </el-select>
                            </span>
                            <span v-if="selectedNodeParams.number==3||selectedNodeParams.number==5">
                                <span>设备ip</span>
                            <el-input class="block-container-padding" v-model="selectedNodeParams.appConfig.ip"
                                      size="mini"></el-input>
                            </span>
                            <span v-if="selectedNodeParams.number==3">
                                <span>网关类型</span><el-select class="block-container-padding"
                                       v-model="selectedNodeParams.appConfig.gatewayType" size="mini"
                                       placeholder="选择网关类型">
                                <el-option style="padding-left: 20px"
                                           v-for="item in gatewayTypeList"
                                           :key="item.value"
                                           :label="item.label"
                                           :value="item.value">
                                </el-option>
                            </el-select>
                            </span>
                        </div>
                    </div>
                    <div v-else-if="currentFocus === 'edge'">
                        <div class="pannel-title">连线</div>
                        <div class="block-container">
                            <span>连线标签</span>
                            <el-input class="block-container-padding" size="mini" type="text"
                                      v-model="selectedEdgeParams.label"/>
                            <!--                            <span>连线颜色</span>-->
                            <!--                            &lt;!&ndash;TODO 右侧状态栏&ndash;&gt;-->
                            <!--                            <br>-->
                            <!--                            <input class="params-radio" style="background-color:red;" name="changecolor" type="radio"-->
                            <!--                                   v-model="selectedEdgeParams.style.stroke" value="red"/>-->
                            <!--                            <input class="params-radio" style="background-color:blue;" name="changecolor" type="radio"-->
                            <!--                                   v-model="selectedEdgeParams.style.stroke" value="blue"/>-->
                            <!--                            <br>-->
                            <span v-if="selectedEdgeParams.type=='crudedottedline'">
                                <span>阀值</span>
                                <el-input class="block-container-padding"
                                          v-model="selectedEdgeParams.appConfig.threshold" size="mini"></el-input>
                            </span>
                        </div>
                    </div>
                    <div v-else>
                        <div class="pannel-title">画布</div>
                        <div class="block-container">
                            <!--todo 项目信息-->
                            <cc-checkbox @change="enableGridHandler">网格对齐</cc-checkbox>
                        </div>
                    </div>
                </div>
                <!-- navigator-pannel -->
                <div class="navigator-pannel">
                    <div class="pannel-title">导航器</div>
                    <div class="navigator" ref="navigator">
                        <div id="g6-minimap"></div>
                    </div>
                </div>
            </div>
        </div>
        <cc-loading v-if="loading" loading-text="自动布局中..."></cc-loading>
        <div class="right-menu" ref="rightMenu" v-show="rightMenuShow" @contextmenu.prevent>
            <ul v-if="currentFocus === 'node' && selectedNodes.length > 1">
                <li @click="alignHandler('horizontal')">水平对齐</li>
                <li @click="alignHandler('vertical')">垂直对齐</li>
                <li @click="addGroup">创建分组</li>
            </ul>
            <ul v-else-if="currentFocus === 'group'">
                <li @click="removeGroup">移除分组</li>
            </ul>
        </div>
    </div>
</template>

<script>
    import G6 from '@antv/g6'
    import $ from 'jquery'

    import {Loading, Checkbox, Dropdown} from '../../cc-elements'
    import ToolbarEdit from './toolbar-edit'
    import registerEdge from './edge'
    import ccNode from './node'
    import ccBehavior from './behavior'
    import config from './config'
    import theme from './theme'
    import initGraph from './graph'
    import utils from './utils'
    import {getData, getNodeData} from '@/api/top/top'

    registerEdge(G6)
    ccNode.register(G6)
    ccBehavior.register(G6)

    export default {
        name: 'CCTopology',
        components: {
            'cc-checkbox': Checkbox,
            'cc-loading': Loading,
            'toolbar-edit': ToolbarEdit,
        },
        props: {
            graphData: {
                type: Object,
                default: () => {
                    return {nodes: [], edges: []}
                }
            },
            nodeAppConfig: {
                type: Object,
                default: () => {
                    return {}
                }
            },
            edgeAppConfig: {
                type: Object,
                default: () => {
                    return {}
                }
            },
            graphModeConfig: {
                type: String,
                default: () => {
                    return {}
                }
            }
        },
        data() {
            return {
                eid: '',
                url: '',
                gatewayTypeList: [
                    {
                        value: '01',
                        label: 'zigbee'
                    },
                    {
                        value: '02',
                        label: 'can'
                    },
                    {
                        value: '03',
                        label: 'lora'
                    },
                    {
                        value: '04',
                        label: 'bluetooth'
                    },
                ],
                graphBg: 'default-style',
                toolbarShow: true,
                rightMenuShow: false,
                nodeTypeList: [],
                edgeShapeList: [
                    {guid: 'solidline', label: '数据控制流', class: 'iconfonts icon-solidline'},
                    {guid: 'dottedline', label: '数据传输流', class: 'iconfonts icon-dottedline'},
                    {guid: 'crudedottedline', label: '数据触发流', class: 'iconfonts icon-crudedottedline'},
                ],
                loading: false,
                clientWidth: window.innerWidth || document.documentElement.clientWidth || document.body.clientWidth,
                clientHeight: window.innerHeight || document.documentElement.clientHeight || document.body.clientHeight,
                graph: null,
                grid: null,
                minimap: null,
                graphMode: 'edit',
                currentEdgeShape: {
                    guid: 'solidline',
                    label: '数据控制流'
                },
                currentFocus: 'canvas',
                selectedNode: null,
                selectedNodeParams: {
                    label: '',
                    style: {
                        stroke: ''
                    },
                    appConfig: this.nodeAppConfig
                },
                selectedNodeParamsTimeout: null,
                selectedEdge: null,
                selectedEdgeParams: {
                    label: '',
                    style: {
                        stroke: ''
                    },
                    appConfig: this.edgeAppConfig
                },
                selectedEdgeParamsTimeout: null,
                selectedGroupId: null,
                zoomValue: 1,
                nodesInClipboard: [],
                historyIndex: 0,
                undoCount: 0,
                onresizeTimeout: null,
                pasteCount: 0
            }
        },
        computed: {
            disableUndo: function () {
                return this.historyIndex === 0 || this.historyIndex - (this.undoCount + 1) < 0
            },
            disableRedo: function () {
                return this.historyIndex === 0 || this.historyIndex === 10 || this.undoCount < 1
            },
            disableCopy: function () {
                return this.selectedNodes.length === 0
            },
            disablePaste: function () {
                return this.nodesInClipboard.length === 0
            },
            disableDelete: function () {
                return this.selectedNodes.length === 0 && this.selectedEdges.length === 0
            },
            selectedNodes: function () {
                let self = this
                let graph = self.graph
                if (graph && !graph.destroyed) {
                    return graph.findAllByState('node', 'selected')
                } else {
                    return []
                }
            },
            selectedEdges: function () {
                let graph = this.graph
                if (graph && !graph.destroyed) {
                    return graph.findAllByState('edge', 'selected')
                } else {
                    return []
                }
            }
        },
        watch: {
            layoutType() {
                this.initTopo(this.graphData)
            },
            //TODO 右侧输入
            selectedNodeParams: {
                deep: true,
                handler: function (newVal, oldVal) {
                    let selectedNodeModel = this.selectedNode.getModel()
                    if (utils.isObjectValueEqual(selectedNodeModel.appConfig, newVal.appConfig) && selectedNodeModel.label === newVal.label && selectedNodeModel.id === newVal.id) {
                        return
                    }
                    // 实时监听input值的变化，停止输入300ms后执行update，而不是时时update
                    clearTimeout(this.selectedNodeParamsTimeout)
                    this.selectedNodeParamsTimeout = setTimeout(() => {
                        selectedNodeModel.label = newVal.label
                        selectedNodeModel.appConfig = newVal.appConfig
                        this.selectedNode.update(selectedNodeModel)
                    }, 300)
                }
            },
            selectedEdgeParams: {
                deep: true,
                // eslint-disable-next-line no-unused-vars
                handler: function (newVal, oldVal) {
                    let selectedEdgeModel = this.selectedEdge.getModel()
                    if (utils.isObjectValueEqual(selectedEdgeModel.appConfig, newVal.appConfig) && selectedEdgeModel.label === newVal.label && selectedEdgeModel.style.stroke === newVal.style.stroke) {
                        return
                    }

                    selectedEdgeModel.style.stroke = newVal.style.stroke
                    this.selectedEdge.update(selectedEdgeModel)
                    // 实时监听input值的变化，停止输入300ms后执行update，而不是时时update
                    clearTimeout(this.selectedEdgeParamsTimeout)
                    this.selectedEdgeParamsTimeout = setTimeout(() => {
                        let selectedEdgeModel = this.selectedEdge.getModel()
                        selectedEdgeModel.label = newVal.label
                        selectedEdgeModel.appConfig = newVal.appConfig
                        this.selectedEdge.update(selectedEdgeModel)
                    }, 300)
                }
            }
        },
        created() {
        },
        mounted() {
            //todo
            this.eid = this.$route.query.eid
            this.url = this.$route.path
            this.init()
            this.getInitData()
            ccNode.obj.ccImage.sendThis(this)
            ccBehavior.obj.clickEventEdit.sendThis(this)
            ccBehavior.obj.dragAddEdge.sendThis(this)
            ccBehavior.obj.dragEventEdit.sendThis(this)
            ccBehavior.obj.keyupEventEdit.sendThis(this)
            // ccBehavior.obj.colorEdgeEdit.sendThis(this)
            this.clearHistoryData()
            // this.initTopo(this.graphData)
            // this.autoZoomHandler()
            window.onresize = () => {
                return (() => {
                    this.onresizeHandler()
                })()
            }
        },
        beforeRouteUpdate(to, from, next) {
            this.clearHistoryData()
            next()
        },
        beforeRouteLeave(to, from, next) {
            this.clearHistoryData()
            next()
        },
        beforeDestroy() {
            this.clearHistoryData()
        },
        methods: {
            getInitData() {
                var resdata = null
                var vm = this
                if (this.url == '/it') {
                    resdata = {
                        uid: sessionStorage.getItem('uid'),
                        id: this.eid
                    }
                } else if (this.url == '/vit' || this.url == '/rit') {
                    resdata = {
                        id: this.eid,
                        uid: 0
                    }
                }
                // todo 获取设备
                this.nodeTypeList = []
                getNodeData(this.eid).then((res) => {
                    let data = res.data
                    if (data.code == 1) {
                        let list = data.data.list
                        for (let i = 0; i < list.length; i++) {
                            if (list[i].list.length != 0) {
                                let nodeTypeItem = {
                                    name: list[i].name,
                                    nodeList: []
                                }
                                let number = list[i].number
                                for (let j = 0; j < list[i].list.length; j++) {
                                    let nodeItem = {
                                        guid: list[i].list[j].id,
                                        label: list[i].list[j].name,
                                        imgSrc: list[i].list[j].img,
                                        uuids: list[i].list[j].uuids,
                                        number: number
                                    }
                                    nodeTypeItem.nodeList.push(nodeItem)
                                }
                                this.nodeTypeList.push(nodeTypeItem)
                            }
                        }
                    }
                })
                getData(resdata).then((res) => {
                    let data = res.data
                    if (data.code == 1) {
                        let datas = JSON.parse(data.data.pro_josn)
                        if ((this.url == '/it') && (data.data.state == 2 || sessionStorage.getItem('utype') == 1)) {
                            vm.$router.replace('/pl')
                        } else {
                            if (datas != null) {
                                this.graphData = {
                                    nodes: datas.nodes,
                                    edges: datas.edges
                                }
                            }
                        }
                        this.initTopo(this.graphData)
                        this.resetZoomHandler()
                    } else {
                        this.$XModal.message({message: data.msg})
                    }
                })
            },
            init() {
                let url = this.$route.path
                // alert(type)
                if ((url == '/rit') && (sessionStorage.getItem('utype') == 2)) {
                    this.$router.replace('/pl')
                } else if (url == '/vit' || this.url == '/rit') {
                    this.graphMode = '1'
                } else if (url == '/it') {
                    this.graphMode = 'edit'
                } else {
                    return
                }
            },


            // 修改线条颜色
            edgecolorChange(e) {
                ccBehavior.obj.colorEdgeEdit.sendThis(this)
            },
            openFullScreenLoading() {
                this.loading = true
            },
            closeFullScreenLoading() {
                let self = this
                self.$nextTick(() => {
                    self.loading = false
                })
            },
            dragstartHandler(event, nodeType) {
                event.dataTransfer.setData('text', JSON.stringify(nodeType))
            },
            dragenterHandler(event) {
                event.preventDefault()
            },
            dragoverHandler(event) {
                event.preventDefault()
            },
            dropHandler(event) {
                let nodeTypeStr = event.dataTransfer.getData('text')
                let nodeType = JSON.parse(nodeTypeStr)
                let clientX = event.clientX
                let clientY = event.clientY
                this.addNode(clientX, clientY, nodeType)
            },
            dragendHandler() {
            },
            initTopo(graphData) {
                let self = this
                if (self.graph) {
                    self.graph.destroy()
                }
                // 【预览模式】 - 封装的节点展开/收缩交互
                G6.registerBehavior('my-collapse-expand', {
                    getEvents() {
                        return {
                            'node:click': 'onNodeClick'
                        }
                    },
                    onNodeClick(event) {
                        let clickNode = event.item
                        this.sourceNodeIds = [clickNode._cfg.id]
                        if (clickNode.hasState('collapse')) {
                            // 节点已收缩, 需要展开
                            let visible = true
                            this.collapseOrExpand(clickNode, visible)
                            clickNode.clearStates('collapse')
                        } else {
                            // 节点未收缩, 需要收缩
                            let visible = false
                            this.collapseOrExpand(clickNode, visible)
                            clickNode.setState('collapse', true)
                        }
                        self.graph.paint()
                    },
                    collapseOrExpand(sourceNode, visible) {
                        let outEdges = sourceNode.getOutEdges()
                        for (let i = 0; i < outEdges.length; i++) {
                            let targetNode = outEdges[i].getTarget()
                            let targetNodeId = targetNode._cfg.id
                            if (!this.sourceNodeIds.includes(targetNodeId)) {
                                targetNode.changeVisibility(visible)
                                // 如果一个节点隐藏/显示了，那么它关联的所有边都隐藏
                                let relationEdges = targetNode.getEdges()
                                for (let i = 0; i < relationEdges.length; i++) {
                                    relationEdges[i].changeVisibility(visible)
                                }
                                this.sourceNodeIds.push(targetNodeId)
                                // 递归, 该节点的下属节点继续隐藏
                                if (targetNode.getOutEdges().length > 0) {
                                    this.collapseOrExpand(targetNode, visible)
                                }
                            }
                        }
                    }
                })

                // 图画布的定义
                let graphContainer = self.$refs.graphContainer
                let graphWidth = graphContainer.clientWidth
                let graphHeight = graphContainer.clientHeight
                // Plugins
                let plugins = []
                let modes = {
                    default: [
                        'drag-canvas',
                        'drag-node',
                        {
                            type: 'click-select',
                            trigger: 'ctrl',
                            multiple: true
                        }
                    ],
                    edit: [
                        'drag-node',
                        'drag-canvas',
                        {
                            type: 'click-select',
                            // trigger: 'ctrl', // TODO... 疑似官方bug，ctrl无效
                            multiple: true
                        },
                        {
                            type: 'brush-select',
                            trigger: 'ctrl',
                            includeEdges: false
                        },
                        'right-click-node',
                        'right-click-edge',
                        // Group Behavior
                        // 'drag-group',
                        // 'collapse-expand-group',
                        // 'drag-node-with-group',
                        // 自定义Behavior
                        'hover-event-edit',
                        'click-event-edit',
                        'keyup-event',
                        'drag-event-edit',
                        'keyup-event-edit',
                        'drag-add-edge'
                    ],
                    addEdge: [
                        'drag-canvas',
                        // 自定义Behavior
                        'click-add-edge'
                    ],
                    multiSelect: [
                        {
                            type: 'brush-select',
                            trigger: 'drag',
                            onSelect() {
                                this.graph.setMode('edit')
                                window.document.getElementById('multi-select').style.backgroundColor = 'transparent'
                            }
                        }
                    ]
                }
                if (self.graphMode === 'edit') {
                    let navigator = self.$refs.navigator
                    let minimapWidth = navigator ? navigator.clientWidth : 160
                    let minimapHeight = navigator ? navigator.clientHeight : 120
                    minimapWidth = minimapWidth > 160 ? minimapWidth : 120
                    minimapHeight = minimapHeight > 160 ? minimapHeight : 120
                    let minimap = new G6.Minimap({
                        size: [minimapWidth, minimapHeight],
                        container: 'g6-minimap',
                        type: 'default'
                    })
                    plugins.push(minimap)
                }
                /* 生成图 */
                if (self.layoutType === 'force') {
                    /* 力导布局: force-layout */
                    self.graph = initGraph.forceLayoutGraph(G6, {
                        plugins: plugins,
                        container: 'mount-topology',
                        width: graphWidth,
                        height: graphHeight,
                        modes: modes,
                        graphData: graphData
                    })
                } else {
                    /* 默认布局: 自由布局 */
                    self.graph = initGraph.commonGraph(G6, {
                        plugins: plugins,
                        container: 'mount-topology',
                        width: graphWidth,
                        height: graphHeight,
                        modes: modes,
                        graphData: graphData
                    })
                }
                self.graph.$C = config
                self.graph.setMode(self.graphMode)
                self.graph.refresh()
                self.autoZoomHandler()
                if (this.graphMode === 'edit') {
                    // 由于 G6 目前不支持监听 Group 的点击事件，故自行实现
                    self.graph.on('click', evt => {
                        if (evt.target.cfg.groupId) {
                            self.currentFocus = 'group'
                            self.selectedGroupId = evt.target.cfg.groupId
                        }
                    })
                    self.graph.on('contextmenu', evt => {
                        if (evt.target.cfg.groupId) {
                            self.currentFocus = 'group'
                            self.selectedGroupId = evt.target.cfg.groupId
                            self.rightMenuShow = true
                        }
                    })
                }
            },
            /* 自动布局 */
            forceLayoutHandler() {
                let graph = this.graph
                if (graph && !graph.destroyed) {
                    this.openFullScreenLoading()
                    graph.updateLayout({
                        type: 'force',
                        center: [200, 200],
                        preventOverlap: true,
                        linkDistance: 150,
                        nodeStrength: -200,
                        onLayoutEnd: () => {
                            this.closeFullScreenLoading()
                        }
                    })
                }
            },
            changeEdgeShapeHandler(edgeShape) {
                this.graph.$C.edge.type = edgeShape
            },
            undoHandler() {
                if (this.historyIndex > 0 && this.historyIndex - (this.undoCount + 1) >= 0) {
                    this.undoCount += 1
                    let key = `graph_history_${this.historyIndex - this.undoCount}`
                    let historyData = this.getHistoryData(key)
                    this.changeGraphData(JSON.parse(historyData))
                    this.refreshGraph()
                }
            },
            redoHandler() {
                if (this.undoCount > 0) {
                    let key = `graph_history_${this.historyIndex - this.undoCount + 1}`
                    let historyData = this.getHistoryData(key)
                    this.changeGraphData(JSON.parse(historyData))
                    this.undoCount -= 1
                    this.refreshGraph()
                }
            },
            copyHandler() {
                this.nodesInClipboard = this.selectedNodes
                this.pasteCount = 0
            },
            pasteHandler() {
                this.pasteCount += 1 // 连续paste次数统计
                let graph = this.graph
                let nodesInClipboard = this.nodesInClipboard
                if (graph && !graph.destroyed && nodesInClipboard.length > 0) {

                    // ************** 记录【粘贴】前的数据状态 start **************
                    let historyData = JSON.stringify(graph.save())
                    let key = `graph_history_${this.historyIndex}`
                    this.addHistoryData(key, historyData)
                    // ************** 记录【粘贴】前的数据状态 end **************

                    // 开始粘贴
                    for (let i = 0; i < nodesInClipboard.length; i++) {
                        let node = nodesInClipboard[i]
                        let model = node.getModel()
                        let newModel = {
                            ...model,
                            id: utils.generateUUID(),
                            x: model.x + 10 * this.pasteCount,
                            y: model.y + 10 * this.pasteCount
                        }
                        graph.addItem('node', newModel)
                    }

                    // ************** 记录【粘贴】后的数据状态 start **************
                    // 如果当前点过【撤销】了，【粘贴】后将取消【重做】功能
                    // 重置undoCount，【粘贴】后的数据状态给(当前所在historyIndex + 1)，且清空这个时间点之后的记录
                    if (this.undoCount > 0) {
                        this.historyIndex = this.historyIndex - this.undoCount // 此时的historyIndex应当更新为【撤销】后所在的索引位置
                        for (let i = 1; i <= this.undoCount; i++) {
                            let key = `graph_history_${this.historyIndex + i}`
                            this.removeHistoryData(key)
                        }
                        this.undoCount = 0
                    }
                    // 存储【粘贴】后的数据状态
                    this.historyIndex += 1
                    key = `graph_history_${this.historyIndex}`
                    let currentData = JSON.stringify(graph.save())
                    this.addHistoryData(key, currentData)
                    // ************** 记录【粘贴】后的数据状态 end **************
                }
            },
            deleteHandler() {
                if (this.graphMode === 'edit') {
                    let graph = this.graph
                    let selectedNodes = graph.findAllByState('node', 'selected')
                    let selectedEdges = graph.findAllByState('edge', 'selected')
                    if (this.selectedNodes.length > 0 || this.selectedEdges.length > 0) {

                        // ************** 记录【删除】前的数据状态 start **************
                        let historyData = JSON.stringify(graph.save())
                        let key = `graph_history_${this.historyIndex}`
                        this.addHistoryData(key, historyData)
                        // ************** 记录【删除】前的数据状态 end **************

                        // 开始删除
                        for (let i = 0; i < selectedNodes.length; i++) {
                            graph.removeItem(selectedNodes[i])
                        }
                        for (let i = 0; i < selectedEdges.length; i++) {
                            graph.removeItem(selectedEdges[i])
                        }
                        if (this.undoCount > 0) {
                            this.historyIndex = this.historyIndex - this.undoCount
                            for (let i = 1; i <= this.undoCount; i++) {
                                let key = `graph_history_${this.historyIndex + i}`
                                this.removeHistoryData(key)
                            }
                            this.undoCount = 0
                        }
                        // 记录【删除】后的数据状态
                        this.historyIndex += 1
                        key = `graph_history_${this.historyIndex}`
                        let currentData = JSON.stringify(graph.save())
                        this.addHistoryData(key, currentData)
                    }

                }
            },
            zoomInHandler() {
                let graph = this.graph
                if (graph && !graph.destroyed) {
                    graph.zoom(1.2)
                }
            },
            zoomOutHandler() {
                let graph = this.graph
                if (graph && !graph.destroyed) {
                    graph.zoom(0.8)
                }
            },
            autoZoomHandler() {
                let graph = this.graph
                if (graph && !graph.destroyed && graph.save().nodes.length > 0) {
                    graph.fitView(10)
                    // this.zoomValue = graph.getZoom();  // TODO...怎么处理changeZoomHandler的二次触发问题
                }
            },
            resetZoomHandler() {
                let graph = this.graph
                if (graph && !graph.destroyed) {
                    graph.zoomTo(1)
                }
            },
            multiSelectHandler(event) {
                event.target.style.backgroundColor = '#EEEEEE'
                this.graph.setMode('multiSelect')
            },
            enableGridHandler(enableGrid) {
                if (enableGrid) {
                    this.grid = new G6.Grid()
                    this.graph.addPlugin(this.grid)
                } else {
                    this.graph.removePlugin(this.grid)
                }
            },
            enableMinimapHandler(enableMinimap) {
                if (enableMinimap) {
                    this.minimap = new G6.Minimap({
                        size: [200, 120],
                        type: 'default',
                        className: 'g6-minimap-preview'
                    })
                    this.graph.addPlugin(this.minimap)
                } else {
                    this.graph.removePlugin(this.minimap)
                }
            },
            // 右键菜单
            alignHandler(coordinate) {
                let graph = this.graph
                if (this.selectedNodes.length > 1 && this.selectedNode) {

                    // ************** 记录【节点对齐】前的数据状态 start **************
                    let historyData = JSON.stringify(graph.save())
                    let key = `graph_history_${this.historyIndex}`
                    this.addHistoryData(key, historyData)
                    // ************** 记录【节点对齐】前的数据状态 end **************

                    // 开始节点对齐
                    let cfg = coordinate === 'horizontal' ? {y: this.selectedNode.getModel().y} : {x: this.selectedNode.getModel().x}
                    for (let i = 0, len = this.selectedNodes.length; i < len; i++) {
                        this.selectedNodes[i].updatePosition(cfg)
                    }
                    graph.refresh()

                    // ************** 记录【节点对齐】后的数据状态 start **************
                    // 如果当前点过【撤销】了，节点对齐后将取消【重做】功能
                    // 重置undoCount，【节点对齐】后的数据状态给(当前所在historyIndex + 1)，且清空这个时间点之后的记录
                    if (this.undoCount > 0) {
                        this.historyIndex = this.historyIndex - this.undoCount // 此时的historyIndex应当更新为【撤销】后所在的索引位置
                        for (let i = 1; i <= this.undoCount; i++) {
                            let key = `graph_history_${this.historyIndex + i}`
                            this.removeHistoryData(key)
                        }
                        this.undoCount = 0
                    }
                    // 记录【节点对齐】后的数据状态
                    this.historyIndex += 1
                    key = `graph_history_${this.historyIndex}`
                    let currentData = JSON.stringify(graph.save())
                    this.addHistoryData(key, currentData)
                    // ************** 记录【节点对齐】后的数据状态 end **************
                }
                this.rightMenuShow = false
            },
            addGroup() {
                let graph = this.graph
                if (graph && !graph.destroyed && this.selectedNodes.length > 1 && this.selectedNode) {

                    // ************** 记录【添加分组】前的数据状态 start **************
                    let historyData = JSON.stringify(graph.save())
                    let key = `graph_history_${this.historyIndex}`
                    this.addHistoryData(key, historyData)
                    // ************** 记录【添加分组】前的数据状态 end **************

                    let nodeIds = this.selectedNodes.map(item => {
                        return item._cfg.id
                    })
                    // 创建分组
                    let group = graph.addItem('group', {
                        groupId: utils.generateUUID(),
                        nodes: nodeIds,
                        type: 'circle ',
                        zIndex: 2,
                        title: {
                            text: '分组',
                            stroke: '#87e8de',
                            fill: '#87e8de',
                            offsetX: 2,
                            offsetY: 2
                        }
                    })

                    // ************** 记录【添加分组】后的数据状态 start **************
                    if (group) {
                        // 如果当前点过【撤销】了，【添加分组】后将取消【重做】功能
                        // 重置undoCount，【添加分组】后的数据状态给(当前所在historyIndex + 1)，且清空这个时间点之后的记录
                        if (this.undoCount > 0) {
                            this.historyIndex = this.historyIndex - this.undoCount // 此时的historyIndex应当更新为【撤销】后所在的索引位置
                            for (let i = 1; i <= this.undoCount; i++) {
                                let key = `graph_history_${this.historyIndex + i}`
                                this.removeHistoryData(key)
                            }
                            this.undoCount = 0
                        }
                        // 记录【添加分组】后的数据状态
                        this.historyIndex += 1
                        let key = `graph_history_${this.historyIndex}`
                        let currentData = JSON.stringify(graph.save())
                        this.addHistoryData(key, currentData)
                    }
                    // ************** 记录【添加分组】后的数据状态 end **************
                    this.rightMenuShow = false
                }
            },
            removeGroup() {
                let graph = this.graph
                if (this.selectedGroupId) {

                    // ************** 记录【删除】前的数据状态 start **************
                    let historyData = JSON.stringify(graph.save())
                    let key = `graph_history_${this.historyIndex}`
                    this.addHistoryData(key, historyData)
                    // ************** 记录【删除】前的数据状态 end **************

                    // 开始删除
                    graph.removeItem(this.selectedGroupId)

                    // ************** 记录【删除】后的数据状态 start **************
                    // 如果当前点过【撤销】了，删除分组后将取消【重做】功能
                    // 重置undoCount，【删除】后的数据状态给(当前所在historyIndex + 1)，且清空这个时间点之后的记录
                    if (this.undoCount > 0) {
                        this.historyIndex = this.historyIndex - this.undoCount // 此时的historyIndex应当更新为【撤销】后所在的索引位置
                        for (let i = 1; i <= this.undoCount; i++) {
                            let key = `graph_history_${this.historyIndex + i}`
                            this.removeHistoryData(key)
                        }
                        this.undoCount = 0
                    }
                    // 记录【删除】后的数据状态
                    this.historyIndex += 1
                    key = `graph_history_${this.historyIndex}`
                    let currentData = JSON.stringify(graph.save())
                    this.addHistoryData(key, currentData)
                    // ************** 记录【删除】后的数据状态 end **************
                }
            },
            addNode(clientX, clientY, nodeType) {
                let graph = this.graph
                if (graph && !graph.destroyed) {

                    // ************** 记录【添加节点】前的数据状态 start **************
                    let historyData = JSON.stringify(graph.save())
                    let key = `graph_history_${this.historyIndex}`
                    this.addHistoryData(key, historyData)
                    // ************** 记录【添加节点】前的数据状态 end **************

                    // todo 开始添加节点内容
                    let droppoint = graph.getPointByClient(clientX, clientY)
                    let node = graph.addItem('node', {
                        id: utils.generateUUID(),
                        x: droppoint.x,
                        y: droppoint.y,
                        guid: nodeType.guid,
                        label: nodeType.label,
                        uuids: nodeType.uuids,
                        number: nodeType.number,
                        labelCfg: {
                            position: 'bottom'
                        },
                        type: 'cc-image',
                        img: nodeType.imgSrc,
                        size: [55, 55],
                        width: 48,
                        height: 48,
                        anchorPoints: [
                            [0.5, 0], // top
                            [1, 0.5], // right
                            [0.5, 1], // bottom
                            [0, 0.5] // left
                        ],
                        // 自定义属性
                        appState: {
                            alert: false
                        }
                    })

                    // ************** 记录【添加节点】后的数据状态 start **************
                    if (node) {
                        // 如果当前点过【撤销】了，【添加节点】后将取消【重做】功能
                        // 重置undoCount，【添加节点】后的数据状态给(当前所在historyIndex + 1)，且清空这个时间点之后的记录
                        if (this.undoCount > 0) {
                            this.historyIndex = this.historyIndex - this.undoCount // 此时的historyIndex应当更新为【撤销】后所在的索引位置
                            for (let i = 1; i <= this.undoCount; i++) {
                                let key = `graph_history_${this.historyIndex + i}`
                                this.removeHistoryData(key)
                            }
                            this.undoCount = 0
                        }
                        // 记录【添加节点】后的数据状态
                        this.historyIndex += 1
                        let key = `graph_history_${this.historyIndex}`
                        let currentData = JSON.stringify(graph.save())
                        this.addHistoryData(key, currentData)
                    }
                    // ************** 记录【添加节点】后的数据状态 end **************
                }
            },
            unselectAllNodes() {
            },
            addHistoryData(key, value) {
                try {
                    sessionStorage.setItem(key, value)
                } catch (oException) {
                    if (oException.name === 'QuotaExceededError') {
                        console.warn('已经超出本地存储限定大小，清空历史记录！')
                        // 可进行超出限定大小之后的操作，如下面可以先清除记录，再次保存
                        // sessionStorage.clear()
                        this.clearHistoryData()
                        this.historyIndex = 0
                        this.undoCount = 0
                        sessionStorage.setItem(key, value)
                    }
                }
            },
            getHistoryData(key) {
                return sessionStorage.getItem(key)
            },
            removeHistoryData(key) {
                sessionStorage.removeItem(key)
            },
            clearHistoryData() {
                let keys = Object.keys(sessionStorage)
                keys.forEach(key => {
                    if (key.startsWith('graph_history')) {
                        sessionStorage.removeItem(key)
                    }
                })
            },
            onresizeHandler() {
                // 实时监听窗口大小变化
                let self = this
                clearTimeout(this.onresizeTimeout)
                this.onresizeTimeout = setTimeout(() => {
                    let graph = self.graph
                    if (graph && !graph.destroyed) {
                        let graphContainer = self.$refs.graphContainer
                        let graphWidth = graphContainer.clientWidth
                        let graphHeight = graphContainer.clientHeight
                        graph.changeSize(graphWidth, graphHeight)
                    }
                }, 1000)
            },
            /* 暴露给外部的接口 */
            refreshGraph() {
                let graph = this.graph
                if (graph && !graph.destroyed) {
                    graph.refresh()
                }
            },
            getGraphData() {
                let graph = this.graph
                if (graph && !graph.destroyed) {
                    return graph.save()
                } else {
                    return {nodes: [], edges: []}
                }
            },
            changeGraphData(data) {
                let graph = this.graph
                if (graph && !graph.destroyed) {
                    graph.changeData(data)
                }
            },
            changeGraphSize(graphWidth = 0, graphHeight = 0) {
                let graph = this.graph
                if (graph && !graph.destroyed) {
                    let graphContainer = this.$refs.graphContainer
                    graphWidth = graphWidth || graphContainer.clientWidth
                    graphHeight = graphHeight || graphContainer.clientHeight
                    graph.changeSize(graphWidth, graphHeight)
                }
            },
            changeGraphMode(graphData, graphMode) {
                this.graphMode = graphMode
                this.$nextTick(() => {
                    this.initTopo(graphData)
                    this.autoZoomHandler()
                })
            },
            changeGraphTheme(themeValue) {
                let graph = this.graph
                if (graph && !graph.destroyed) {
                    let nodes = graph.getNodes()
                    let edges = graph.getEdges()
                    switch (themeValue) {
                        case 'darkStyle':
                            this.graphBg = 'dark-style'
                            graph.$T = theme.darkStyle
                            for (let i = 0, len = edges.length; i < len; i++) {
                                let edge = edges[i]
                                let edgeModel = edge.getModel()
                                edgeModel.style = graph.$T.edgeStyle.default
                                edge.update(edgeModel)
                            }
                            for (let i = 0, len = nodes.length; i < len; i++) {
                                let node = nodes[i]
                                let nodeModel = node.getModel()
                                nodeModel.labelCfg = graph.$T.nodeLabelCfg
                                node.update(nodeModel)
                            }
                            graph.paint()
                            break
                        case 'officeStyle':
                            this.graphBg = 'office-style'
                            graph.$T = theme.officeStyle
                            for (let i = 0, len = edges.length; i < len; i++) {
                                let edge = edges[i]
                                let edgeModel = edge.getModel()
                                edgeModel.style = graph.$T.edgeStyle.default
                                edge.update(edgeModel)
                            }
                            for (let i = 0, len = nodes.length; i < len; i++) {
                                let node = nodes[i]
                                let nodeModel = node.getModel()
                                nodeModel.labelCfg = graph.$T.nodeLabelCfg
                                node.update(nodeModel)
                            }
                            graph.paint()
                            break
                        default:
                            this.graphBg = 'default-style'
                            graph.$T = theme.defaultStyle
                            for (let i = 0, len = edges.length; i < len; i++) {
                                let edge = edges[i]
                                let edgeModel = edge.getModel()
                                edgeModel.style = graph.$T.edgeStyle.default
                                edge.update(edgeModel)
                            }
                            for (let i = 0, len = nodes.length; i < len; i++) {
                                let node = nodes[i]
                                let nodeModel = node.getModel()
                                nodeModel.labelCfg = graph.$T.nodeLabelCfg
                                node.update(nodeModel)
                            }
                            graph.paint()
                    }
                }
            },
            /* 子组件向父组件传值 */
            autoRefreshHandler(interval) {
                this.$emit('doAutoRefresh', interval)
            },
            manualRefreshHandler() {
                this.$emit('doManualRefresh')
            },
            changeModeHandler(graphMode) {
                this.changeGraphTheme('defaultStyle')
                this.$emit('doChangeMode', graphMode)
            },
            goBack() {
                this.$router.replace('/pl')
            },
            //todo 保存接口
            saveDataHandler() {
                let graphData = this.getGraphData()
                this.$emit('doSaveData', graphData)
            },
            // todo 预留提交接口
            submitDataHandler() {
                let grahData = this.getGraphData()
                this.$emit('submitDataHandler', grahData)
            },
            //通过
            reviewPassed() {
                this.$emit('reviewPassed', 4)
            },
            //拒绝
            reviewRejected() {
                this.$emit('reviewRejected', 2)
            },
            showtopnav(e) {
                var topnavList = $('.top-nav')
                var topimgList = $('.top-img')
                const oldh = e.path[1].children[1].style.display
                for (let i = 0; i < topnavList.length; i++) {
                    topimgList[i].style.display = 'none'
                }
                if (oldh === '' || oldh === 'none') {
                    e.path[1].children[1].style.display = 'block'
                } else {
                    e.path[1].children[1].style.display = 'none'
                }
            }
        }
    }
</script>
<style lang="scss" scoped>
    @import '../../assets/iconfont/iconfont.css';

    .device-nav {
        margin-top: 20px;
    }

    * {
        padding: 0;
        margin: 0;
    }

    .top-nav-confg {
        border-bottom: 1px solid gainsboro;
    }

    .top-nav-confg:last-child {
        border: 0;
    }

    .top-nav {
        margin-left: 20px;
        margin-top: 10px;
        margin-bottom: 10px;
        font-size: 16px;
        font-weight: bold;
        cursor: pointer;
    }

    .top-nav:first-child {
        margin-top: 20px;
    }

    .top-img {
        overflow: hidden;
        /*height: 0px;*/
        display: none;
        transition: all 0.8s;
    }


    *[draggable = true] {
        -khtml-user-drag: element;
    }

    .topology {
        height: 100%;
        margin: 0;
        padding: 0;
    }

    .edge-shape {
        width: 100%;
        /*margin-right: 20px;*/
        line-height: 25px;
        /*display: block;*/
        text-align: center;
        /*border-right: 1px solid #E6E9ED;*/
    }

    .container {
        /* todo */
        height: calc(100% - 10%);
        max-height: 100% -20%;
        height: 95%;
        width: calc(100% - 5px);

        .left {
            float: left;
            display: inline-block;
            width: 12%;
            height: 100%;
            min-height: calc(600px - 55px);
            padding-top: 0;
            color: #333;
            font-size: 12px;
            /*text-align: center;*/
            background-color: #F7F9FB;
            /*border-right: 1px solid #E6E9ED;*/

            .node-type {
                display: inline-block;
                width: 60px;
                height: 60px;
                margin-left: 5%;
                margin-right: 5%;
                margin-top: 10px;
                /*text-align: center;*/
                /*vertical-align: middle;*/
                border: 1px solid transparent;
                /*margin: 3px;*/

                img {
                    height: 55px;
                    width: 55px;
                    padding-top: 5px;
                }

                span {
                    font-size: 14px;
                    display: block;
                    line-height: 3px;
                }
            }

            .node-type:hover {
                cursor: move;
                border: 1px solid #ccc;
            }
        }


        .center {
            display: inline-block;
            height: calc(100% - 55px);
        }

        .right {
            float: right;
            display: inline-block;
            width: 12%;
            height: 100%;
            min-height: calc(600px - 55px);
            padding-top: 0;
            color: #333;
            font-size: 12px;
            text-align: left;
            background-color: #F7F9FB;
            /*border-left: 1px solid #E6E9ED;*/
            user-select: none;
            -webkit-user-select: none;
            -moz-user-select: none;
            -ms-user-select: none;

            .detail-pannel {
                height: 60%;
                min-height: 360px;

                .block-container {
                    padding: 16px 20px;

                    .block-container-padding {
                        margin-top: 6px;
                        margin-bottom: 6px;
                    }

                    .params-radio {
                        -webkit-appearance: none;
                        border-radius: 4px;
                        height: 25px;
                        width: 25px;
                    }
                }
            }

            .navigator-pannel {
                height: 40%;
                min-height: 135px;

                .navigator {
                    padding: 1px;
                    height: 55%;
                    min-height: 135px;
                }
            }

            .pannel-title {
                padding-left: 12px;
                height: 32px;
                color: #000;
                line-height: 32px;
                background-color: #ebeef2;
                border-top: 1px solid #dce3e8;
                border-bottom: 1px solid #dce3e8;
            }
        }

        .graph-container {
            height: 100%;
            /* todo */
            /*min-height: calc(600px - 55px);*/
            max-height: 95%;

            #mount-topology {
                width: 100%;
                height: 100%;
            }
        }
    }

    .right-menu {
        width: 100px;
        position: absolute;
        padding: 10px 5px;
        list-style: none;
        background-color: #fff;
        opacity: 1;
        border: 1px solid #dcdfe6;
        border-radius: 10px;

        li {
            padding: 5px;
            list-style-type: none;
            cursor: pointer;
        }

        li:hover {
            color: #409eff;
        }
    }

    // 背景主题
    .default-style {
        background: #fff;
    }

    .dark-style {
        background: linear-gradient(to bottom, rgba(255, 255, 255, 0.15) 0%, rgba(0, 0, 0, 0.15) 100%), radial-gradient(at top center, rgba(255, 255, 255, 0.40) 0%, rgba(0, 0, 0, 0.40) 120%) #989898;
        background-blend-mode: multiply, multiply;
    }

    .office-style {
        background-image: linear-gradient(to right, #fef6de 0%, #f3ebd3 100%);
    }
</style>

<style lang="scss">
    .g6-tooltip {
        padding: 10px 6px;
        color: #444;
        background-color: rgba(255, 255, 255, 0.9);
        border: 1px solid #e2e2e2;
        border-radius: 4px;
    }

    // 预览模式自动生成的节点
    .graph-container {
        #mount-topology .g6-minimap-preview {
            /*width: 100%;*/
            /*height: 100%;*/
            position: absolute;

            right: 10px;
            bottom: 60px;
            border: 1px solid #e2e2e2;
        }
    }
</style>
