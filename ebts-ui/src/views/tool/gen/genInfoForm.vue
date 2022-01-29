<template>
  <el-form ref="genInfoForm" :model="info" :rules="rules" label-width="150px">
    <el-row>
      <el-col :span="12">
        <el-form-item prop="tplCategory">
          <span slot="label">生成模板</span>
          <el-select v-model="info.tplCategory" @change="tplSelectChange">
            <el-option label="单表（增删改查）" value="crud"/>
            <el-option label="树表（增删改查）" value="tree"/>
            <el-option label="多表关联（增删改查）" value="rel"/>
            <el-option label="主子表（增删改查）" value="sub"/>
          </el-select>
        </el-form-item>
      </el-col>

      <el-col :span="12">
        <el-form-item prop="packageName">
          <span slot="label">
            生成包路径
            <el-tooltip content="生成在哪个java包下，例如 com.hchyun.system" placement="top">
              <i class="el-icon-question"></i>
            </el-tooltip>
          </span>
          <el-input v-model="info.packageName"/>
        </el-form-item>
      </el-col>

      <el-col :span="12">
        <el-form-item prop="moduleName">
          <span slot="label">
            生成模块名
            <el-tooltip content="可理解为子系统名，例如 system" placement="top">
              <i class="el-icon-question"></i>
            </el-tooltip>
          </span>
          <el-input v-model="info.moduleName"/>
        </el-form-item>
      </el-col>

      <el-col :span="12">
        <el-form-item prop="businessName">
          <span slot="label">
            生成业务名
            <el-tooltip content="可理解为功能英文名，例如 user" placement="top">
              <i class="el-icon-question"></i>
            </el-tooltip>
          </span>
          <el-input v-model="info.businessName"/>
        </el-form-item>
      </el-col>

      <el-col :span="12">
        <el-form-item prop="functionName">
          <span slot="label">
            生成功能名
            <el-tooltip content="用作类描述，例如 用户" placement="top">
              <i class="el-icon-question"></i>
            </el-tooltip>
          </span>
          <el-input v-model="info.functionName"/>
        </el-form-item>
      </el-col>

      <el-col :span="12">
        <el-form-item>
          <span slot="label">
            上级菜单
            <el-tooltip content="分配到指定菜单下，例如 系统管理" placement="top">
              <i class="el-icon-question"></i>
            </el-tooltip>
          </span>
          <treeselect
            :append-to-body="true"
            v-model="info.parentMenuId"
            :options="menus"
            :normalizer="normalizer"
            :show-count="true"
            placeholder="请选择系统菜单"
          />
        </el-form-item>
      </el-col>

      <el-col :span="12">
        <el-form-item prop="genType">
          <span slot="label">
            生成代码方式
            <el-tooltip content="默认为zip压缩包下载，也可以自定义生成路径" placement="top">
              <i class="el-icon-question"></i>
            </el-tooltip>
          </span>
          <el-radio v-model="info.genType" label="0">zip压缩包</el-radio>
          <el-radio v-model="info.genType" label="1">自定义路径</el-radio>
        </el-form-item>
      </el-col>

      <el-col :span="24" v-if="info.genType == '1'">
        <el-form-item prop="genPath">
          <span slot="label">
            自定义路径
            <el-tooltip content="填写磁盘绝对路径，若不填写，则生成到当前Web项目下" placement="top">
              <i class="el-icon-question"></i>
            </el-tooltip>
          </span>
          <el-input v-model="info.genPath">
            <el-dropdown slot="append">
              <el-button type="primary">
                最近路径快速选择
                <i class="el-icon-arrow-down el-icon--right"></i>
              </el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item @click.native="info.genPath = '/'">恢复默认的生成基础路径</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </el-input>
        </el-form-item>
      </el-col>
    </el-row>
    <!--树表配置-->
    <el-row v-show="info.tplCategory == 'tree'">
      <h4 class="form-header">其他信息</h4>
      <el-col :span="12">
        <el-form-item>
          <span slot="label">
            树编码字段
            <el-tooltip content="树显示的编码字段名， 如：dept_id" placement="top">
              <i class="el-icon-question"></i>
            </el-tooltip>
          </span>
          <el-select v-model="info.treeCode" placeholder="请选择">
            <el-option
              v-for="(column, index) in info.columns"
              :key="index"
              :label="column.columnName + '：' + column.columnComment"
              :value="column.columnName"
            ></el-option>
          </el-select>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item>
          <span slot="label">
            树父编码字段
            <el-tooltip content="树显示的父编码字段名， 如：parent_Id" placement="top">
              <i class="el-icon-question"></i>
            </el-tooltip>
          </span>
          <el-select v-model="info.treeParentCode" placeholder="请选择">
            <el-option
              v-for="(column, index) in info.columns"
              :key="index"
              :label="column.columnName + '：' + column.columnComment"
              :value="column.columnName"
            ></el-option>
          </el-select>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item>
          <span slot="label">
            树名称字段
            <el-tooltip content="树节点的显示名称字段名， 如：dept_name" placement="top">
              <i class="el-icon-question"></i>
            </el-tooltip>
          </span>
          <el-select v-model="info.treeName" placeholder="请选择">
            <el-option
              v-for="(column, index) in info.columns"
              :key="index"
              :label="column.columnName + '：' + column.columnComment"
              :value="column.columnName"
            ></el-option>
          </el-select>
        </el-form-item>
      </el-col>
    </el-row>
    <!--主子表配置-->
    <el-row v-show="info.tplCategory == 'sub'">
      <h4 class="form-header">关联信息</h4>
      <el-col :span="12">
        <el-form-item>
          <span slot="label">
            关联子表的表名
            <el-tooltip content="关联子表的表名， 如：sys_user" placement="top">
              <i class="el-icon-question"></i>
            </el-tooltip>
          </span>
          <el-select v-model="info.subTableName" placeholder="请选择" @change="subSelectChange">
            <el-option
              v-for="(table, index) in tables"
              :key="index"
              :label="table.tableName + '：' + table.tableComment"
              :value="table.tableName"
            ></el-option>
          </el-select>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item>
          <span slot="label">
            子表关联的外键名
            <el-tooltip content="子表关联的外键名， 如：user_id" placement="top">
              <i class="el-icon-question"></i>
            </el-tooltip>
          </span>
          <el-select v-model="info.subTableFkName" placeholder="请选择">
            <el-option
              v-for="(column, index) in subColumns"
              :key="index"
              :label="column.columnName + '：' + column.columnComment"
              :value="column.columnName"
            ></el-option>
          </el-select>
        </el-form-item>
      </el-col>
    </el-row>
    <!--多表关联配置-->
    <div v-if="info.tplCategory == 'rel'">
      <el-row class="form-header">
        <el-col :span="4">
          <div style="font-size: 15px;color:#6379bb;font-weight: bold;">多表关联关系</div>
        </el-col>
        <el-col :offset="18" :span="2">
          <el-button
            clearable
            @click="addRelTable"
          >添加表
          </el-button>
        </el-col>
      </el-row>
      <div v-for="(table,index) in relTables">
        <el-row>
          <el-col :span="4">
            <el-form-item label-width="95px">
              <span slot="label">
                关联父表
                <el-tooltip content="父表名称， 如：sys_table" placement="top">
                  <i class="el-icon-question"></i>
                </el-tooltip>
              </span>
              <el-select v-model="table.tableName" @change="tableChange(table.tableName,index)"
                         placeholder="请选择关联主表">
                <el-option
                  v-for="(item , index) in tableList"
                  :key="index"
                  :label="item.tableName"
                  :value="item.tableName"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="4">
            <el-form-item label-width="80px">
              <span slot="label">
                父表id
                <el-tooltip content="父表id， 如：table_id" placement="top">
                  <i class="el-icon-question"></i>
                </el-tooltip>
              </span>
              <el-select v-model="table.tableColumn" placeholder="请选择字段">
                <el-option
                  v-for="(column , index) in table.tableColumns"
                  :key="index"
                  :label="column.column_name+':'+column.column_comment"
                  :value="column.column_name"/>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="4">
            <el-form-item label-width="110px">
              <span slot="label">
                关联表表名
                <el-tooltip content="关联子表的表名， 如：sys_user" placement="top">
                  <i class="el-icon-question"></i>
                </el-tooltip>
              </span>
              <el-select v-model="table.relName" @change="relNameChang(index,table.relName)" placeholder="请选择表名">
                <el-option
                  v-for="(table , index) in tableInfo"
                  :key="index"
                  :label="table.table_name + ':'+table.table_comment"
                  :value="table.table_name"/>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="4">
            <el-form-item label-width="110px">
              <span slot="label">
                关联表字段
                <el-tooltip content="关联子表的关联字段， 如：user_id" placement="top">
                  <i class="el-icon-question"></i>
                </el-tooltip>
              </span>
              <el-select v-model="table.relColumn" placeholder="请选择表名">
                <el-option
                  v-for="(cloumn , index) in table.relColumns"
                  :key="index"
                  :label="cloumn.columnName + ':'+cloumn.columnComment"
                  :value="cloumn.columnName"/>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="4">
            <el-form-item label-width="125px">
              <span slot="label">
                关联查询方式
                <el-tooltip content="关联子表的查询方式， 如：inner join" placement="top">
                  <i class="el-icon-question"></i>
                </el-tooltip>
              </span>
              <el-select v-model="table.queryType" placeholder="请选择查询方式">
                <el-option label="inner join" value="inner"/>
                <el-option label="left join" value="left"/>
                <el-option label="right join" value="right"/>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="3">
            <el-form-item label-width="20px">
              <div style="margin-right: 20px">
                <el-select v-model="table.sort" placeholder="请选择关联查询级别">
                  <el-option label="一级关联" :value="1"/>
                  <el-option label="二级关联" :value="2"/>
                  <el-option label="三级关联" :value="3"/>
                  <el-option label="四级关联" :value="4"/>
                </el-select>
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="1">
            <el-button @click="removeRelTable(index)">删除</el-button>
          </el-col>
        </el-row>
        <el-table :ref="'relColumns'+index" :data="table.relColumns" row-key="columnId" :max-height="tableHeight">
          <el-table-column label="序号" type="index" min-width="5%" class-name="allowDrag"/>
          <el-table-column
            label="字段列名"
            prop="columnName"
            min-width="10%"
            :show-overflow-tooltip="true"
          />
          <el-table-column label="字段描述" min-width="10%">
            <template slot-scope="scope">
              <el-input v-model="scope.row.columnComment"></el-input>
            </template>
          </el-table-column>
          <el-table-column
            label="物理类型"
            prop="columnType"
            min-width="10%"
            :show-overflow-tooltip="true"
          />
          <el-table-column label="Java类型" min-width="11%">
            <template slot-scope="scope">
              <el-select v-model="scope.row.javaType">
                <el-option label="Long" value="Long"/>
                <el-option label="String" value="String"/>
                <el-option label="Integer" value="Integer"/>
                <el-option label="Double" value="Double"/>
                <el-option label="BigDecimal" value="BigDecimal"/>
                <el-option label="Date" value="Date"/>
              </el-select>
            </template>
          </el-table-column>
          <el-table-column label="java属性" min-width="10%">
            <template slot-scope="scope">
              <el-input v-model="scope.row.javaField"></el-input>
            </template>
          </el-table-column>
          <el-table-column label="列表" min-width="5%">
            <template slot-scope="scope">
              <el-checkbox true-label="1" v-model="scope.row.isList"></el-checkbox>
            </template>
          </el-table-column>
          <el-table-column label="查询" min-width="5%">
            <template slot-scope="scope">
              <el-checkbox true-label="1" v-model="scope.row.isQuery"></el-checkbox>
            </template>
          </el-table-column>
          <el-table-column label="查询方式" min-width="10%">
            <template slot-scope="scope">
              <el-select v-model="scope.row.queryType">
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
              <el-select v-model="scope.row.htmlType">
                <el-option label="文本框" value="input"/>
                <el-option label="下拉框" value="select"/>
                <el-option label="单选框" value="radio"/>
                <el-option label="复选框" value="checkbox"/>
                <el-option label="日期控件" value="datetime"/>
              </el-select>
            </template>
          </el-table-column>
          <el-table-column label="字典类型" min-width="12%">
            <template slot-scope="scope">
              <el-select v-model="scope.row.dictType" clearable filterable placeholder="请选择">
                <el-option
                  v-for="dict in dictOptions"
                  :key="dict.dictType"
                  :label="dict.dictName"
                  :value="dict.dictType">
                  <span style="float: left">{{ dict.dictName }}</span>
                  <span style="float: right; color: #8492a6; font-size: 13px">{{ dict.dictType }}</span>
                </el-option>
              </el-select>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </div>
  </el-form>
</template>
<script>
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import {getRelId, getRelColumns, getTableInfos, getTableColumns, getRelColumnInfos} from '@/api/tool/relApi'

export default {
  name: "BasicInfoForm",
  components: {Treeselect},
  props: {
    info: {
      type: Object,
      default: null
    },
    tables: {
      type: Array,
      default: null
    },
    menus: {
      type: Array,
      default: []
    },
    dictOptions: {
      type: Array,
      default: []
    }
  },
  data() {
    return {
      subColumns: [],
      // 表格的高度
      tableHeight: document.documentElement.scrollHeight - 245 + "px",
      tableId: null,
      relTables: [],
      tableInfo: [],
      tableList: [],
      rules: {
        tplCategory: [
          {required: true, message: "请选择生成模板", trigger: "blur"}
        ],
        packageName: [
          {required: true, message: "请输入生成包路径", trigger: "blur"}
        ],
        moduleName: [
          {required: true, message: "请输入生成模块名", trigger: "blur"}
        ],
        businessName: [
          {required: true, message: "请输入生成业务名", trigger: "blur"}
        ],
        functionName: [
          {required: false, message: "请输入生成功能名", trigger: "blur"}
        ],
      }
    };
  },
  created() {
    this.relTables = []
    this.tableId = this.$route.params && this.$route.params.tableId;
    getTableInfos().then(res => {
      this.tableInfo = res.data
    })
  },
  watch: {
    'info.subTableName': function (val) {
      this.setSubTableColumns(val);
    },
    'info': function (val) {
      if (val.tplCategory === 'rel') {
        let table = {
          tableName: val.tableName
        }
        this.tableList.unshift(table)
        getRelColumnInfos(this.tableId).then(res => {
          if (res.data.length === 0) {
            getRelId().then(res => {
              let relTable = {
                id: res.data,
                tableId: this.tableId,
                tableName: null,
                relName: null,
                relComment: null,
                relColumn: null,
                tableColumn: null,
                queryType: 'left',
                sort: 1,
                tableColumns: [],
                relColumns: [],
              }
              this.relTables.push(relTable);
            })
          } else {
            this.relTables = []
            for (let i = 0; i < res.data.length; i++) {
              let table = {
                tableName: res.data[i].relName,
              }
              let tableData = res.data[i]
              getTableColumns(tableData.tableName).then(res => {
                tableData.tableColumns = res.data
              })
              this.relTables.push(tableData)
              this.tableList.push(table)
            }
          }
        })
      }
    }
  },
  methods: {
    /** 关联父表改变 */
    tableChange(tableName, index) {
      this.relTables[index].tableColumns = []
      getTableColumns(tableName).then(res => {
        this.relTables[index].tableColumns = res.data
        this.$forceUpdate()
      })
    },
    /** 删除关子联表 */
    removeRelTable(index) {
      let tableDate = this.relTables[index]
      if (tableDate.relName !== null || tableDate.relName !== '') {
        for (let i=0;i<this.tableList.length;i++){
          if (tableDate.relName === this.tableList[i].tableName){
            this.tableList.splice(i,1);
          }
        }
      }
      this.relTables.splice(index, 1)
    },
    /** 关联子表改变 */
    relNameChang(index, relName) {
      let start = true;
      for (let i = 0; i < this.tableList.length; i++) {
        if (relName === this.tableList[i].tableName) {
          start = false
          break
        }
      }
      if (start) {
        let table = {
          tableName: relName,
        }
        this.tableList.push(table)
      }
      getRelColumns(relName, this.relTables[index].id).then(res => {
        for (let i = 0; i < this.tableInfo.length; i++) {
          if (this.tableInfo[i].table_name === relName) {
            this.relTables[index].relComment = this.tableInfo[i].table_comment
            break
          }
        }
        this.relTables[index].relColumns = res.data
      })
    },
    /** 添加关联表 */
    addRelTable() {
      getRelId().then(res => {
        let relTable = {
          id: res.data,
          tableId: this.tableId,
          tableName: null,
          relName: null,
          relComment: null,
          relColumn: null,
          tableColumn: null,
          queryType: 'left',
          sort: 1,
          tableColumns: [],
          relColumns: [],
        }
        this.relTables.push(relTable)
      })
    },
    /** 转换菜单数据结构 */
    normalizer(node) {
      if (node.children && !node.children.length) {
        delete node.children;
      }
      return {
        id: node.menuId,
        label: node.menuName,
        children: node.children
      };
    },
    /** 选择子表名触发 */
    subSelectChange() {
      this.info.subTableFkName = '';
    },
    /** 选择生成模板触发 */
    tplSelectChange(value) {
      if (value === 'rel') {
        this.tableList = []
        let table = {
          tableName: this.info.tableName,
        }
        this.tableList.push(table)
        getRelId().then(res => {
          let relTable = {
            id: res.data,
            tableId: this.tableId,
            tableName: null,
            relName: null,
            relComment: null,
            relColumn: null,
            tableColumn: null,
            queryType: 'left',
            sort: 1,
            tableColumns: [],
            relColumns: [],
          }
          this.relTables.push(relTable);
        })
      } else if (value !== 'sub') {
        this.info.subTableName = '';
        this.info.subTableFkName = '';
        this.relTables = []
      } else {
        this.relTables = []
      }
    },
    /** 设置关联外键 */
    setSubTableColumns(value) {
      for (var item in this.tables) {
        const name = this.tables[item].tableName;
        if (value === name) {
          this.subColumns = this.tables[item].columns;
          break;
        }
      }
    }
  }
};
</script>
<style scoped>
.el-table {
  margin-bottom: 22px;
}
</style>
