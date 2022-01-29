<template>
  <div class="app-container">
    <el-form ref="ucon" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item v-for="item in uconList"
                    :key="item.id"
                    :label="item.ucName">

        <el-input v-if="item.type == 1" v-model="item.ucReal"
                  clearable
                  :placeholder="outPlaceholder(item)"
                  size="small"
                  @keyup.enter.native="handleQuery"
        />
        <div style="width: 200px" v-else-if="item.type == 2">
          <el-row :gutter="15">
            <el-col :span="12">
              <el-input size="small" placeholder="开始值" v-model="item.ucReal.begin"></el-input>
            </el-col>
            <el-col :span="12">
              <el-input size="small" placeholder="结束值" v-model="item.ucReal.end"></el-input>
            </el-col>
          </el-row>
        </div>
        <el-date-picker
          v-else-if="item.type ==3"
          v-model="item.ucReal"
          type="date"
          format="yyyy-MM-dd"
          value-format="yyyy-MM-dd"
          placeholder="选择日期时间">
        </el-date-picker>
        <el-date-picker v-else-if="item.type ==4"
                        v-model="item.ucReal"
                        size="small"
                        style="width: 240px"
                        value-format="yyyy-MM-dd"
                        type="daterange"
                        range-separator="-"
                        start-placeholder="开始日期"
                        end-placeholder="结束日期"
        ></el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['tool:query:export']"
        >导出
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="handleQuery"></right-toolbar>
    </el-row>
    <el-table :data="realDate.data">
      <el-table-column v-for="item in realDate.header"
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
      @pagination="handleQuery"
    />
  </div>
</template>

<script>
import {RealInfo, RealData,exportReal} from "@/api/system/data"

function listInit(list) {
  for (let i = 0; i < list.length; i++) {
    if (list[i].type == 2) {
      list[i].ucReal = {begin: null, end: null}
    } else if (list[i].type == 4) {
      list[i].ucReal = []
    }
  }
  return list;
}

export default {
  name: "queryDate",
  data() {
    return {
      dataId: null,
      uconList: [],
      showSearch: true,
      realDate: {
        data: [],
        header: [],
      },
      total: 0,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
      },
    }
  },
  created() {
    this.dataId = this.$route.fullPath.split("/")[3]
    RealInfo(this.dataId).then(res => {
      this.uconList = listInit(res.data)
    })
    this.handleQuery()
  },
  methods: {
    outPlaceholder(item) {
      return "请输入" + item.ucName
    },
    /**
     * 重置
     */
    resetQuery() {
      for (let i = 0; this.uconList.length; i++) {
        this.uconList[i].ucReal = null
      }
    },
    /**
     * 搜索
     */
    handleQuery() {
      let data = {
        id: this.dataId,
        pageNum: this.queryParams.pageNum,
        pageSize: this.queryParams.pageSize,
      }
      data.uniCons = this.uconList
      RealData(data).then(res => {
        this.realDate.data = res.rows
        this.total = res.total
        this.realDate.header = []
        for (var key in this.realDate.data[0]) {
          this.realDate.header.push(key)
        }
      })
    },

    /** 导出按钮操作 */
    handleExport() {
      var that = this
      this.$confirm('是否确认导出查询数据项?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        let data = {
          id:that.dataId,
          uniCons:that.uconList,
        }
        return exportReal(data)
      }).then(response => {
        this.download(response.msg);
      })
    }
  }
}
</script>

<style scoped>

</style>
