<template>
  <div>
    <div class="form-header">
      <el-row :gutter="15">
        <el-form ref="elForm" :model="info" :rules="rules" size="medium" label-width="100px">
          <el-col :span="18">
            <el-form-item label-width="1px" label="">
              <textarea ref="textarea" type="textarea" @change="sqlChang" placeholder="请输入sql"
                        :autosize="{minRows: 7, maxRows: 7}" :style="{width: '100%'}"></textarea>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label-width="55px" label="名称" prop="uqName">
              <el-input v-model="info.uqName" placeholder="请输入名称" clearable :style="{width: '100%'}">
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label-width="55px" label="描述" prop="uqDescribe">
              <el-input v-model="info.uqDescribe" placeholder="请输入描述" clearable :style="{width: '100%'}">
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-button type="text" icon="el-icon-upload" size="medium" @click="submitForm">提交</el-button>
            <el-button type="text" icon="el-icon-view" size="medium" @click="previewQuery">预览</el-button>
            <el-button type="text" icon="el-icon-plus" size="medium" @click="handleAdd" >新增</el-button>
            <el-button type="text" icon="el-icon-download" size="medium" @click="handleExport">导出</el-button>
            <el-button v-if="info.isRelease == 2" type="text" icon="el-icon-success" size="medium" @click="handRelease(1)">发布</el-button>
            <el-button v-if="info.isRelease == 1" type="text" icon="el-icon-error" size="medium" @click="handRelease(2)">撤销</el-button>
          </el-col>
        </el-form>
      </el-row>
      <el-row>
        <el-table ref="dragTable" :data="cloumns" row-key="columnId" :max-height="tableHeight">
          <el-table-column label="序号" type="index" min-width="5%" class-name="allowDrag"/>
          <el-table-column label="查询名称" min-width="10%">
            <template slot-scope="scope">
              <el-input v-model="scope.row.ucName"></el-input>
            </template>
          </el-table-column>
          <el-table-column label="条件描述" min-width="10%">
            <template slot-scope="scope">
              <el-input v-model="scope.row.ucDescribe"></el-input>
            </template>
          </el-table-column>
          <el-table-column label="key" min-width="10%">
            <template slot-scope="scope">
              <el-input v-model="scope.row.ucKey"></el-input>
            </template>
          </el-table-column>
          <el-table-column label="条件" min-width="10%">
            <template slot-scope="scope">
              <el-select v-model="scope.row.ucCon" @change="ucTypeChang(scope.$index,scope.row)">
                <el-option label="=" value="EQ"/>
                <el-option label="!=" value="NE"/>
                <el-option label=">" value="GT"/>
                <el-option label=">=" value="GTE"/>
                <el-option label="<" value="LT"/>
                <el-option label="<=" value="LTE"/>
                <el-option label="LIKE" value="LIKE"/>
                <el-option label="BETWEEN" value="BETWEEN"/>
              </el-select>
            </template>
          </el-table-column>
          <el-table-column label="显示类型" min-width="12%">
            <template slot-scope="scope">
              <el-select v-model="scope.row.ucType" @change="ucTypeChang(scope.$index,scope.row)">
                <el-option label="文本框" value="input"/>
                <el-option label="日期控件" value="datetime"/>
              </el-select>
            </template>
          </el-table-column>
          <el-table-column label="模拟数据" min-width="10%">
            <template slot-scope="scope">
              <el-input v-if="scope.row.type == 1" placeholder="请输入模拟数据" v-model="scope.row.ucMock"></el-input>
              <div v-else-if="scope.row.type == 2">
                <el-row :gutter="15">
                  <el-col :span="12">
                    <el-input placeholder="开始值" v-model="scope.row.ucMock.begin"></el-input>
                  </el-col>
                  <el-col :span="12">
                    <el-input placeholder="结束值" v-model="scope.row.ucMock.end"></el-input>
                  </el-col>
                </el-row>
              </div>
              <el-date-picker
                v-else-if="scope.row.type ==3"
                v-model="scope.row.ucMock"
                type="date"
                format="yyyy-MM-dd"
                value-format="yyyy-MM-dd"
                placeholder="选择日期时间">
              </el-date-picker>
              <el-date-picker v-else-if="scope.row.type ==4"
                              v-model="scope.row.ucMock"
                              size="small"
                              style="width: 240px"
                              value-format="yyyy-MM-dd"
                              type="daterange"
                              range-separator="-"
                              start-placeholder="开始日期"
                              end-placeholder="结束日期"
              ></el-date-picker>
            </template>
          </el-table-column>
          <el-table-column label="操作" align="center" min-width="5%">
            <template slot-scope="scope">
              <el-button
                size="mini"
                type="text"
                icon="el-icon-delete"
                @click="handleDelete(scope.$index,scope.row)"
              >删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-row>
    </div>
    <div class="el-table">
      <el-table v-show="previewDate.open" :data="previewDate.data">
        <el-table-column v-for="item in previewDate.header"
                         :label="item"
                         align="center"
                         :key="item"
                         :prop="item"/>
      </el-table>
      <pagination
        v-show="total>0"
        :total="total"
        :page.sync="queryParams.pageNum"
        :limit.sync="queryParams.pageSize"
        @pagination="previewQuery"
      />
    </div>
  </div>
</template>
<script>
// 引入全局实例
import CodeMirror from 'codemirror'
// 核心样式
import 'codemirror/lib/codemirror.css'
// 引入主题后还需要在 options 中指定主题才会生效
import 'codemirror/theme/idea.css'

// 需要引入具体的语法高亮库才会有对应的语法高亮效果
// codemirror 官方其实支持通过 /addon/mode/loadmode.js 和 /mode/meta.js 来实现动态加载对应语法高亮库
// 但 vue 貌似没有无法在实例初始化后再动态加载对应 JS ，所以此处才把对应的 JS 提前引入
import 'codemirror/mode/sql/sql.js'

//代码补全提示
import 'codemirror/addon/hint/show-hint.css';
import 'codemirror/addon/hint/show-hint.js';
import 'codemirror/addon/hint/sql-hint.js';
import {getQueryInfo, editQueryInfo, previewQueryData, Release, exportMock} from '@/api/tool/query'
import {Message} from "element-ui";


function JSONString(list) {
  for (let i = 0; i < list.length; i++) {

    if (list[i].type == 2) {
      list[i].ucMock = JSON.stringify(list[i].ucMock)
    } else if (list[i].type == 4) {
      let time = {
        startTime: list[i].ucMock[0],
        endTime: list[i].ucMock[1],
      }
      list[i].ucMock = JSON.stringify(time)
    }
  }
  return list;
}

function JSONparse(list) {
  for (let i = 0; i < list.length; i++) {
    if (list[i].type == 2) {
      list[i].ucMock = JSON.parse(list[i].ucMock)
    } else if (list[i].type == 4) {
      let time = JSON.parse(list[i].ucMock)
      list[i].ucMock = [time.startTime, time.endTime]
    }
  }
  return list;
}

export default {
  components: {},
  props: [],
  data() {
    return {
      tableHeight: document.documentElement.scrollHeight - 245 + "px",
      // 表列信息
      cloumns: [],
      sqlconfig: {
        // 默认的语法类型
        mode: 'sql',
        // 编辑器实例
        coder: null,
        // 默认配置
        options: {
          // 缩进格式
          tabSize: 4,
          // 主题，对应主题库 JS 需要提前引入
          theme: 'idea',
          spellcheck: true,
          cursorHeight: 0.85,
          // 显示行号
          lineNumbers: true,
          line: true,
          extraKeys: {"Tab": "autocomplete"},
          lineWrapping: true,//代码折叠
          matchBrackets: true,//括号匹配
        },
      },
      total: 0,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
      },
      queryId: null,
      info: {
        uqSql: '',
        uqName: null,
        uqDescribe: null,
      },

      previewDate: {
        open: false,
        data: [],
        header: [],
      },
      rules: {
        uqName: [{required: true, message: '请输入名称', trigger: 'blur'}],
        uqDescribe: [{required: true, message: '请输入描述', trigger: 'blur'}],
      },
    }
  },
  computed: {},
  watch: {},
  created() {
    this.queryId = this.$route.params && this.$route.params.queryId;
    getQueryInfo(this.queryId).then(res => {
      this.cloumns = []
      this.info = res.data.info
      this.cloumns = JSONparse(res.data.list)
      this.sqlconfig.coder.setValue(this.info.uqSql)
    })
    this.$nextTick(function () {
      this.initialize();
    });
  },
  mounted() {
  },
  methods: {
    changUniCon(list) {
      console.log(list[0])
      console.log(list.length)
      for (let i = 0; i < list.length; i++) {
        console.log(list[0].ucName)
        if (list[i].ucName == "") {
          Message({
            message: "序号" + i + "查询名称不能为空!",
            type: 'error'
          })
          return false;
        }
        if (list[i].ucDescribe == "") {
          Message({
            message: "序号" + i + "描述不能为空!",
            type: 'error'
          })
          return false;
        }
        if (list[i].ucKey == "") {
          Message({
            message: "序号" + i + "key不能为空!",
            type: 'error'
          })
          return false;
        }
      }
      return true
    },
    /** 发布与撤销 */
    handRelease(release){
      let data = {
        id: this.queryId,
        isRelease: release,
      }
      Release(data).then(res=>{
        console.log(res)
        this.info.isRelease = (this.info.isRelease==1)?2:1
        Message({
          message:res.msg,
          type:"success"
        })
      })
    },
    /** 预览 */
    previewQuery() {
      this.$refs['elForm'].validate(valid => {
        if (valid) {
          let list = JSONString(this.cloumns)
          let data = this.info
          if (list.length > 0) {
            if (this.changUniCon(list)) {
              data.uniCons = list
            } else {
              return
            }
          }
          data.pageNum = this.queryParams.pageNum
          data.pageSize = this.queryParams.pageSize
          previewQueryData(data).then(res => {
            this.previewDate.data = res.rows
            this.total = res.total
            this.previewDate.header = []
            for (var key in this.previewDate.data[0]) {
              this.previewDate.header.push(key)
            }
            this.previewDate.open = true
            this.cloumns = JSONparse(list)
          })
        }
      })
    },
    sqlChang() {
      console.log(this.sqlconfig.coder.getValue())
    },
    initialize() {
      // 初始化编辑器实例，传入需要被实例化的文本域对象和默认配置
      this.sqlconfig.coder = CodeMirror.fromTextArea(this.$refs.textarea, this.sqlconfig.options)
      // 编辑器赋值
      this.sqlconfig.coder.setValue(this.info.uqSql)
      // 支持双向绑定
      this.sqlconfig.coder.on('change', (coder) => {
        this.info.uqSql = coder.getValue()
        console.log(this.info.uqSql)
        if (this.$emit) {
          this.$emit('input', this.info.uqSql)
        }
      })

    },
    /** 删除按钮 */
    handleDelete(index) {
      this.cloumns.splice(index, 1);
    },
    ucTypeChang(index, row) {
      console.log(typeof this.cloumns[index].ucMock)
      if (row.ucType == 'input' && row.ucCon != 'BETWEEN') {
        this.cloumns[index].type = 1
        if (typeof (this.cloumns[index].ucMock) != 'string') {
          this.cloumns[index].ucMock = ''
        }
      } else if (row.ucType == 'input' && row.ucCon == 'BETWEEN') {
        this.cloumns[index].type = 2
        this.cloumns[index].ucMock = {begin: '', end: ''}
      } else if (row.ucType == 'datetime' && row.ucCon != 'BETWEEN') {
        this.cloumns[index].type = 3
        this.cloumns[index].ucMock = ''
      } else if (row.ucType == 'datetime' && row.ucCon == 'BETWEEN') {
        this.cloumns[index].type = 4
        this.cloumns[index].ucMock = []
      }
    },
    /** 添加 */
    handleAdd() {
      let row = {
        id: null,
        uqId: this.queryId,
        ucName: '',
        ucCon: 'EQ',
        ucDescribe: '',
        ucKey: '',
        ucMock: '',
        ucType: 'input',
        type: 1,
      }
      this.cloumns.push(row)
    },
    submitForm() {
      this.$refs['elForm'].validate(valid => {
        if (valid) {
          let list = JSONString(this.cloumns)
          let data = this.info
          if (list.length > 0) {
            data.uniCons = list
          }
          editQueryInfo(data).then(res => {
            console.log(res)
            this.cloumns = JSONparse(list)
            Message({
              message: res.msg,
              type: 'success'
            })
          })
        } else {
          return
        }
      })
    },

    /** 导出按钮操作 */
    handleExport() {
      var that = this
      let list = JSONString(that.cloumns)
      let data = that.info
      this.$confirm('是否确认导出查询数据项?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        if (list.length > 0) {
          if (that.changUniCon(list)) {
            data.uniCons = list
          } else {
            return
          }
        }
        return exportMock(data)
      }).then(response => {
        console.log(response)
        that.cloumns = JSONparse(list)
        this.download(response.msg);
      })
    }
  }
}

</script>
<style>

.CodeMirror {
  border: 1px solid #DCDFE6;
  height: 150px;
}

.CodeMirror-line {
  height: 20px;
  line-height: 20px !important;
}

.CodeMirror-linenumber {
  height: 20px;
  line-height: 20px !important;
}
</style>
<style scoped>
.form-header {
  margin: 18px 10px 0px 10px !important;
}

.el-table {
  margin-bottom: 10px;
}

.pagination-container {
  height: 50px !important;
}

</style>
