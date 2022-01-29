<template>
  <el-card>
    <el-tabs v-model="activeName">
      <el-tab-pane label="基本信息" name="basic">
        <apiclass-info-form ref="apiclassInfo" :info="info"/>
      </el-tab-pane>
      <el-tab-pane label="接口信息" name="cloum">
        <el-row class="mb8">
          <el-col :span="1.5">
            <el-button
              type="primary"
              plain
              icon="el-icon-plus"
              size="mini"
              @click="handleAdd"
              v-hasPermi="['tool:module:export']"
            >新增
            </el-button>
          </el-col>
        </el-row>
        <el-table ref="dragTable" :data="cloumns" row-key="columnId" :max-height="tableHeight">
          <el-table-column label="序号" type="index" min-width="5%" class-name="allowDrag"/>
          <el-table-column label="接口名称" min-width="10%">
            <template slot-scope="scope" >
              <el-input v-model="scope.row.itName"></el-input>
            </template>
          </el-table-column>
          <el-table-column label="接口描述" min-width="10%">
            <template slot-scope="scope">
              <el-input v-model="scope.row.itDescribe"></el-input>
            </template>
          </el-table-column>
          <el-table-column label="请求路径" min-width="10%">
            <template slot-scope="scope">
              <el-input v-model="scope.row.requrl"></el-input>
            </template>
          </el-table-column>
          <el-table-column label="许可" min-width="5%">
            <template slot-scope="scope">
              <el-checkbox true-label="1" false-label="0" v-model="scope.row.isPermission"></el-checkbox>
            </template>
          </el-table-column>
          <el-table-column label="生成" min-width="5%">
            <template slot-scope="scope">
              <el-checkbox true-label="1" false-label="0" v-model="scope.row.isGenerate"></el-checkbox>
            </template>
          </el-table-column>
          <el-table-column label="请求类型" min-width="12%">
            <template slot-scope="scope">
              <el-select v-model="scope.row.method">
                <el-option label="Get" value="Get"/>
                <el-option label="Post" value="Post"/>
                <el-option label="Put" value="Put"/>
                <el-option label="Delete" value="Delete"/>
              </el-select>
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
      </el-tab-pane>
      <!--      <el-tab-pane label="生成信息" name="genInfo">-->
      <!--        <gen-info-form ref="genInfo" :info="info" :tables="tables" :menus="menus"/>-->
      <!--      </el-tab-pane>-->
    </el-tabs>
    <el-form label-width="100px">
      <el-form-item style="text-align: center;margin-left:-100px;margin-top:10px;">
        <el-button type="primary" @click="submitForm()">提交</el-button>
        <el-button @click="close()">返回</el-button>
      </el-form-item>
    </el-form>
  </el-card>
</template>

<script>
import ApiclassInfoForm from "./apiclassInfoForm";
import {listIntertable,updateIntertable} from "@/api/tool/interTable";
import {Message} from "element-ui";

export default {
  name: "ApiclassEdit",
  components: {ApiclassInfoForm},
  data() {
    return {
      // 选中选项卡的 name
      activeName: "cloum",
      // 表格的高度
      tableHeight: document.documentElement.scrollHeight - 245 + "px",
      // 表信息
      tables: [],
      // 表列信息
      cloumns: [],
      // 字典信息
      dictOptions: [],
      // 菜单信息
      menus: [],
      regularOptions: [],
      // 表详细信息
      info: {}
    };
  },
  created() {
    const apiclassId = this.$route.params && this.$route.params.apiclassId;
    if (apiclassId) {
      let data = {
        cId: apiclassId,
        type: 2,
      }
      listIntertable(data).then(response => {
        this.cloumns = response.data.rows
        this.info = response.data.info
      })
    }
    console.log(apiclassId)
  },
  methods: {
    /** 删除按钮 */
    handleDelete(index, row) {
      this.cloumns.splice(index, 1);
    },
    /** 新增按钮 */
    handleAdd() {
      let row = {
        id: null,
        cId: this.info.id,
        mId: this.info.mId,
        isGenerate: '1',
        isPermission: '1',
        itDescribe: '',
        itName: '',
        method: 'Get',
        requrl: '',
        type: 2,
      }
      this.cloumns.push(row)
    },
    /** 提交按钮 */
    submitForm() {
      const apiclassForm = this.$refs.apiclassInfo.$refs.apiclassInfoForm;
      Promise.all([apiclassForm].map(this.getFormPromise)).then(res => {
        const validateResult = res.every(item => !!item);
        for (let i=0;i<this.cloumns.length;i++){
          if (this.cloumns[i].itDescribe == ''){
            Message({
              message: "序号"+(i+1)+"接口描述不能为空!",
              type: 'error'
            })
            return
          }else if (this.cloumns[i].itName == ''){
            Message({
              message: "序号"+(i+1)+"接口名称不能为空!",
              type: 'error'
            })
            return
          }else if (this.cloumns[i].requrl == ''){
            Message({
              message: "序号"+(i+1)+"请求路径不能为空!",
              type: 'error'
            })
            return
          }
        }
        if (validateResult) {
          console.log(apiclassForm.model)
          const interTableDto = {
            apiclass: apiclassForm.model,
            interTables: this.cloumns,
          };
          console.log(interTableDto)
          updateIntertable(interTableDto).then(res => {
            this.msgSuccess(res.msg);
            if (res.code === 200) {
              this.close();
            }
          });
        } else {
          this.msgError("表单校验未通过，请重新检查提交内容");
        }
      });
    },
    getFormPromise(form) {
      return new Promise(resolve => {
        form.validate(res => {
          resolve(res);
        });
      });
    },
    /** 关闭按钮 */
    close() {
      this.$store.dispatch("tagsView/delView", this.$route);
      this.$router.push({path: "/tool/apiclass", query: {t: Date.now()}})
    }
  },
}
</script>

<style scoped>

</style>
