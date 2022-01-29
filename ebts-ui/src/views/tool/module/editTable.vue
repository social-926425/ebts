<template>
  <el-card>
    <el-tabs v-model="activeName">
      <el-tab-pane label="基本信息" name="basic">
        <module-info-form ref="moduleInfo" :info="info"/>
      </el-tab-pane>
      <el-tab-pane label="字段信息" name="cloum">
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
          <el-table-column label="接口类" min-width="12%">
            <template slot-scope="scope">
              <el-select v-model="scope.row.cId" placeholder="请选择类">
                <el-option
                  v-for="item in apiclassOptions"
                  :key="item.id"
                  :label="item.cName"
                  :value="item.id"/>
              </el-select>
            </template>
          </el-table-column>
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
  import {queryListApiclass} from "@/api/tool/apiclass";
  import ModuleInfoForm from "@/views/tool/module/moduleInfoForm";
  import {listIntertable,updateIntertable} from "@/api/tool/interTable";
  export default {
    name: "ModuleEdit",
    components: {ModuleInfoForm},
    data(){
      return{
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
        // 接口类信息
        apiclassOptions: [],
        // 菜单信息
        menus: [],
        regularOptions: [],
        // 表详细信息
        info: {}
      };
    },
    created() {
      const moduleId = this.$route.params && this.$route.params.moduleId;
      if (moduleId){
        let data = {
          mId : moduleId,
          type : 1,
        }
        listIntertable(data).then(response => {
          this.cloumns = response.data.rows
          this.info = response.data.info
        })
        queryListApiclass().then(res => {
          this.apiclassOptions = res.data
          console.log(res)
        })
      }
    },
    methods:{ /** 删除按钮 */
      handleDelete(index, row) {
        this.cloumns.splice(index, 1);
      },
      /** 提交按钮 */
      submitForm() {
        const moduleForm = this.$refs.moduleInfo.$refs.moduleInfoForm;
        Promise.all([moduleForm].map(this.getFormPromise)).then(res => {
          const validateResult = res.every(item => !!item);
          if (validateResult) {
            const interTableDto = {
              module : moduleForm.model,
              interTables : this.cloumns,
            };
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
      }, /** 新增按钮 */
      handleAdd() {
        let row = {
          id: null,
          cId: null,
          mId: this.info.id,
          isGenerate: '1',
          isPermission: '1',
          itDescribe: '',
          itName: '',
          method: 'Get',
          requrl: '',
          type: 1,
        }
        this.cloumns.push(row)
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
        this.$router.push({ path: "/tool/module", query: { t: Date.now()}})
      }
    },
  }
</script>

<style scoped>

</style>
