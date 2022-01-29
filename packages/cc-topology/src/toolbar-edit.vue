<template>
    <div class="toolbar">
        <div class="left">
                    <cc-button size="mini" @click="$parent.goBack">返回</cc-button>
            <div class="graph-ops">
<!--                <i class="iconfont icon-return" size="mini" style="width: 60px;" @click="$parent.goBack"><span>&nbsp;&nbsp;返回</span></i>-->
<!--                <i v-if="$parent.$data.graphMode === 'edit'" class="iconfont icon-save" size="mini"-->
<!--                   @click="$parent.saveDataHandler" style="width: 60px;"><span>&nbsp;&nbsp;保存</span></i>-->
            </div>
        </div>
        <div class="center">
            <div class="graph-ops">
                <i v-if="$parent.$data.graphMode === 'edit'" class="iconfont icon-undo" title="撤销" style="width: 60px;"
                   :class="$parent.disableUndo ? 'disabled':''"
                   @click="$parent.undoHandler"><span>&nbsp;&nbsp;撤销</span></i>
                <i v-if="$parent.$data.graphMode === 'edit'" class="iconfont icon-redo" title="重做" style="width: 60px;"
                   :class="$parent.disableRedo ? 'disabled':''"
                   @click="$parent.redoHandler"><span>&nbsp;&nbsp;重做</span></i>
                <span v-if="$parent.$data.graphMode === 'edit'" class="separator"></span>
                <i v-if="$parent.$data.graphMode === 'edit'" class="iconfont icon-copy" title="复制" style="width: 60px;"
                   :class="$parent.disableCopy ? 'disabled':''"
                   @click="$parent.copyHandler"><span>&nbsp;&nbsp;复制</span></i>
                <i v-if="$parent.$data.graphMode === 'edit'" class="iconfont icon-paste" title="粘贴" style="width: 60px;"
                   :class="$parent.disablePaste ? 'disabled':''"
                   @click="$parent.pasteHandler"><span>&nbsp;&nbsp;粘贴</span></i>
                <i v-if="$parent.$data.graphMode === 'edit'" class="iconfont icon-clear" title="删除" style="width: 60px;"
                   :class="$parent.disableDelete ? 'disabled':''"
                   @click="$parent.deleteHandler"><span>&nbsp;&nbsp;删除</span></i>
                <span v-if="$parent.$data.graphMode === 'edit'" class="separator"></span>
                <i class="iconfont icon-zoom-in" id="zoom-in" style="width: 60px;" title="放大"
                   @click="$parent.zoomInHandler"><span>&nbsp;&nbsp;放大</span></i>
                <i class="iconfont icon-zoom-out" title="缩小" style="width: 60px;" @click="$parent.zoomOutHandler"><span>&nbsp;&nbsp;缩小</span></i>
<!--                <i class="iconfont icon-fit" title="适应画布" style="width: 80px;" @click="$parent.autoZoomHandler"><span>&nbsp;&nbsp;适应画布</span></i>-->
<!--                <i class="iconfont icon-actualsize" title="实际尺寸" style="width: 80px;" @click="$parent.resetZoomHandler"><span>&nbsp;&nbsp;实际尺寸</span></i>-->
                <span class="separator"></span>
<!--                <i class="iconfont icon-roi-select" id="multi-select" title="框选" style="width: 60px;"-->
<!--                   @click="$parent.multiSelectHandler"><span>&nbsp;&nbsp;框选</span></i>-->
                <i class="iconfont icon-auto-layout" title="自动布局" style="width: 80px;"
                   @click="$parent.forceLayoutHandler"><span>&nbsp;&nbsp;自动布局</span></i>
<!--                <i v-if="$parent.$data.graphMode === 'edit'" v-for="edgeItem in edgeShapeList " :class="edgeItem.class" v-on:title="edgeItem.label" style="width: 120px;"-->
<!--                    @click="$parent.changeEdgeShapeHandler(edgeItem.guid)"><span>&nbsp;&nbsp;{{edgeItem.label}}</span></i>-->
                <div v-if="$parent.$data.graphMode === 'edit'" class="iconfont zx"><cc-dropdown
                        class="edge-shape"
                        :dropdown-items="$parent.edgeShapeList"
                        @change="$parent.changeEdgeShapeHandler"
                >
                </cc-dropdown></div>

            </div>
        </div>
        <div class="right">
            <cc-button v-if="$parent.$data.graphMode === 'edit'"  size="mini" @click="$parent.saveDataHandler" >保存</cc-button>
            <cc-button v-if="$parent.$data.graphMode === 'edit'" size="mini" @click="$parent.submitDataHandler">提交</cc-button>
            <!--      <el-button size="mini" @click="$parent.circularLayoutHandler">环形布局</el-button>-->
            <!--      <el-button size="mini" @click="$parent.radialLayoutHandler">辐射</el-button>-->
            <!--      <el-button size="mini" @click="$parent.mdsLayoutHandler">MDS</el-button>-->
            <!--      <el-button size="mini" @click="$parent.dagreLayoutHandler">层次</el-button>-->
            <!--      <cc-button size="mini" @click="$parent.changeModeHandler('preview')"> 返回</cc-button>-->
            <div class="graph-ops">
            </div>

            <cc-button v-if="$parent.$data.url === '/rit'" size="mini" @click="$parent.reviewRejected">拒绝</cc-button>
            <cc-button v-if="$parent.$data.url === '/rit'" size="mini" @click="$parent.reviewPassed">通过</cc-button>

        </div>
    </div>
</template>

<script>
    import {Button, Dropdown} from '../../cc-elements'
    import $ from 'jquery'

    export default {
        name: 'ToolbarEdit',
        components: {
            'cc-button': Button,
            // eslint-disable-next-line vue/no-unused-components
            'cc-dropdown': Dropdown
        },
        data() {
            return {
                edgeShapeList: [
                    {guid: 'solidline', label: '数据控制流', class: 'iconfont icon-solidline'},
                    {guid: 'dottedline', label: '数据传输流', class: 'iconfont icon-dottedline'},
                    {guid: 'crudedottedline', label: '数据触发流', class: 'iconfont icon-crudedottedline'},
                ]
            }
        },
        methods:{
            changStart(){
            }
        }
    }
</script>

<style lang="scss" scoped>
    .zx{
        float: right;
    }
    .onclick:visited{
        background-color: red;
    }
    .iconfont {
        color: #666666;
        display: inline-block;

        span {
            font-size: 12px;
            line-height: 20px;
            height: 20px;
            padding-bottom: 2px;
            color: #666666;
        }
    }

    .toolbar {
        /*z-index: 3;*/
        /*width: 100%;*/
        /*height: 42px;*/
        color: #333;
        text-align: left;
        vertical-align: middle;
        line-height: 42px;
        background-color: #ffffff;
        border: 1px solid #e9e9e9;
        box-shadow: 0 8px 12px 0 rgba(0, 52, 107, 0.04);
        user-select: none;
        -webkit-user-select: none;
        -moz-user-select: none;
        -ms-user-select: none;

        .left {
            display: inline-block;
            width: 12%;
        }

        .center {
            display: inline-block;
            width: 76%;
        }

        .right {
            display: inline-block;
            width: 12%;
            text-align: right;
        }

        .edge-enabled {
            width: 40%;
            text-align: center;
        }
        // todo
        .edge-shape {
            width: 100%;
            /*margin-right: 20px;*/
            line-height: 25px;
            text-align: center;
            /*float: left;*/
            /*border-right: 1px solid #E6E9ED;*/
        }

        .graph-ops {
            display: inline-block;
            vertical-align: middle;
            margin-left: 20px;

            i {
                width: 20px;
                height: 20px;
                margin: 0 6px;
                line-height: 20px;
                color: #a8a8a8;
                text-align: center;
                border-radius: 2px;
                display: inline-block;
                border: 1px solid rgba(2, 2, 2, 0);
            }

            i:hover {
                cursor: pointer;
                border: 1px solid #e9e9e9;
            }

            .disabled {
                color: rgba(0, 0, 0, 0.25);
            }

            .disabled:hover {
                cursor: not-allowed;
                border: 1px solid rgba(2, 2, 2, 0);
            }

            .icon-select {
                background-color: #eeeeee;
            }

            .separator {
                margin: 4px;
                border-left: 1px solid #e9e9e9;
            }
        }

        .cc-button {
            margin: 0 5px;
        }
    }
</style>
