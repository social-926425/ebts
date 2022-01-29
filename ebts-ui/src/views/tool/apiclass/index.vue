<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="模块" prop="mId">
        <el-select v-model="queryParams.mId" placeholder="请选择模块" v-on:change="handleQuery" clearable size="small">
          <el-option
            v-for="item in moduleList"
            :key="item.id"
            :label="item.mName"
            :value="item.id">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="类名" prop="cName">
        <el-input
          v-model="queryParams.cName"
          placeholder="请输入类名"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="类描述" prop="cDescribe">
        <el-input
          v-model="queryParams.cDescribe"
          placeholder="请输入类描述"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="创建时间">
        <el-date-picker
          v-model="daterangeCreateTime"
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
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['tool:apiclass:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['tool:apiclass:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['tool:apiclass:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['tool:apiclass:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="apiclassList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="45" align="center"/>
      <el-table-column label="包名" width="180" align="center" prop="packageName"/>
      <el-table-column label="模块" align="center" prop="module.mName"/>
      <el-table-column label="模块名称" align="center" prop="module.mDescribe"/>
      <el-table-column label="类名" align="center" prop="cName"/>
      <el-table-column label="类描述" align="center" prop="cDescribe"/>
      <el-table-column label="作者" align="center" prop="author"/>
      <el-table-column label="电子邮件" width="180" align="center" prop="email"/>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{m}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="更新时间" align="center" prop="updateTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.updateTime, '{y}-{m}-{d} {h}:{m}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column width="280" label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            type="text"
            size="small"
            icon="el-icon-view"
            @click="handlePreview(scope.row.id)"
            v-hasPermi="['tool:apiclass:preview']"
          >预览</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['tool:apiclass:edit']"
          >修改</el-button>
          <el-button
            type="text"
            size="small"
            icon="el-icon-edit-outline"
            @click="handleEditTable(scope.row)"
            v-hasPermi="['tool:apiclass:update']"
          >接口</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['tool:apiclass:remove']"
          >删除</el-button>
          <el-button
            type="text"
            size="small"
            icon="el-icon-download"
            @click="handleGenTable(scope.row)"
            v-hasPermi="['tool:apiclass:code']"
          >生成代码</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改接口类名对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="包名" prop="packageName">
          <el-input v-model="form.packageName" :disabled="true"/>
        </el-form-item>
        <el-form-item label="前缀" prop="prefix">
          <el-input v-model="form.prefix" :disabled="true"/>
        </el-form-item>
        <el-form-item label="模块" prop="mId">
          <el-select v-model="form.mId" placeholder="请选择模块" @change="transform" clearable>
            <el-option
              v-for="item in moduleList"
              :key="item.id"
              :label="item.mName"
              :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="类名" prop="cName">
          <el-input v-model="form.cName" @input="transform" placeholder="请输入类名"/>
        </el-form-item>
        <el-form-item label="类描述" prop="cDescribe">
          <el-input v-model="form.cDescribe" placeholder="请输入类描述"/>
        </el-form-item>
        <el-form-item label="作者" prop="author">
          <el-input v-model="form.author" placeholder="请输入作者"/>
        </el-form-item>
        <el-form-item label="电子邮件" prop="email">
          <el-input v-model="form.email" placeholder="请输入电子邮件"/>
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="form.remark" type="textarea" :rows="3" placeholder="请输入备注"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
    <!-- 预览界面 -->
    <el-dialog :title="preview.title" :visible.sync="preview.open" width="80%" top="5vh" append-to-body>
      <el-tabs v-model="preview.activeName">
        <el-tab-pane
          v-for="(value, key) in preview.data"
          :label="key"
          :name="key"
          :key="key"
        >
          <pre><code class="hljs" v-html="highlightedCode(value, key)"></code></pre>
        </el-tab-pane>
      </el-tabs>
    </el-dialog>
  </div>
</template>

<script>
  import { listApiclass,  getApiclass,  delApiclass,  addApiclass,  updateApiclass,  exportApiclass } from "@/api/tool/apiclass";
  import {queryListModule} from "@/api/tool/module"
  import {getClassPreview} from "@/api/tool/interTable"
  import hljs from "highlight.js/lib/highlight";
  import "highlight.js/styles/github-gist.css";
  import {genCode} from "@/api/tool/gen";
  import {downLoadZip} from "@/utils/zipdownload";
  hljs.registerLanguage("java", require("highlight.js/lib/languages/java"));
  hljs.registerLanguage("sql", require("highlight.js/lib/languages/sql"));
  export default {
    name: "Apiclass",
    components: {},
    data() {
      return {
        // 遮罩层
        loading: true,
        // 选中数组
        ids: [],
        // 非单个禁用
        single: true,
        // 非多个禁用
        multiple: true,
        // 显示搜索条件
        showSearch: true,
        // 总条数
        total: 0,
        // 接口类名表格数据
        apiclassList: [],
        // 模块数据列表
        moduleList: [],
        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
        // 创建时间时间范围
        daterangeCreateTime: [],
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          mId: null,
          cName: null,
          cDescribe: null,
        },
        // 预览参数
        preview: {
          open: false,
          title: "代码预览",
          data: {},
          activeName: ""
        },
        // 表单参数
        form: {},
        // 表单校验
        rules: {
          packageName: [
            {required: true, message: "包名不能为空", trigger: "blur"},
          ],
          prefix: [
            {required: true, message: "包名不能为空", trigger: "blur"},
          ],
          mId: [
            {required: true, message: "模块不能为空", trigger: "blur"},
          ],
          cName: [
            {required: true, message: "类名不能为空", trigger: "blur"},
          ],
          cDescribe: [
            {required: true, message: "类描述不能为空", trigger: "blur"},
          ],
          author: [
            {required: true, message: "作者不能为空", trigger: "blur"},
          ],
          email: [
            {required: true, message: "电子邮件不能为空", trigger: "blur"},
            {
              pattern: /^[A-Za-z0-9\u4e00-\u9fa5]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/,
              message: '电子邮件格式有误',
              trigger: "blur"
            },
          ],
        }
      };
    },
    created() {
      this.getList();
      queryListModule().then(respone => {
        this.moduleList = respone.data
      })
    },
    methods: {
      /** 预览代码 */
      handlePreview(id){
        console.log(id)
        getClassPreview(id).then(res =>{
          this.preview.data = res.data;
          this.preview.open = true;
          var keys = Object.keys(res.data)
          this.preview.activeName = keys[0]
        })
      },
      /** 生成代码操作 */
      handleGenTable(row) {
          downLoadZip("/generator/intertable/classgen/" + row.id, "hchyun");
      },
      /** 高亮显示 */
      highlightedCode(code, key) {
        const vmName = key;
        var language = vmName.substring(vmName.indexOf(".") + 1, vmName.length);
        console.log(language)
        const result = hljs.highlight(language, code || "", true);
        return result.value || '&nbsp;';
      },
      /** 查询接口类名列表 */
      getList() {
        this.loading = true;
        listApiclass(this.addCreateDateRange(this.queryParams, this.daterangeCreateTime)).then(response => {
          this.apiclassList = response.rows;
          this.total = response.total;
          this.loading = false;
        });
      },
      // 取消按钮
      cancel() {
        this.open = false;
        this.reset();
      },
      handleEditTable(row){
        const apiclassId = row.id || this.ids[0];
        this.$router.push("/apiclass/edit/" + apiclassId);
      },
      transform() {
        var mid = this.form.mId
        var str = ''
        var start = true
        for (let i = 0; i < this.moduleList.length; i++) {
          if (mid == this.moduleList[i].id) {
            str = this.moduleList[i].mName
            start = false
          }
        }
        if (start) {
          str = ''
        }
        this.form.packageName = 'com.hchyun.' + str
        this.form.prefix = str+":"+this.form.cName.toLowerCase()
      },
      // 表单重置
      reset() {
        this.form = {
          id: null,
          mId: null,
          cName: "",
          cDescribe: null,
          packageName: "com.hchyun.",
          author: "hchyun",
          email: "clay@huchyun.com",
          remark: null,
          prefix: ""
        };
        this.resetForm("form");
      },
      /** 搜索按钮操作 */
      handleQuery() {
        this.queryParams.pageNum = 1;
        this.getList();
      },
      /** 重置按钮操作 */
      resetQuery() {
        this.daterangeCreateTime = [];
        this.resetForm("queryForm");
        this.handleQuery();
      },
      // 多选框选中数据
      handleSelectionChange(selection) {
        this.ids = selection.map(item => item.id)
        this.single = selection.length !== 1
        this.multiple = !selection.length
      },
      /** 新增按钮操作 */
      handleAdd() {
        this.reset();
        this.open = true;
        this.title = "添加接口类名";
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset();
        const id = row.id || this.ids
        getApiclass(id).then(response => {
          this.form = response.data;
          this.open = true;
          this.title = "修改接口类名";
        });
      },
      /** 提交按钮 */
      submitForm() {
        this.$refs["form"].validate(valid => {
          if (valid) {
            if (this.form.id != null) {
              updateApiclass(this.form).then(response => {
                this.msgSuccess("修改成功");
                this.open = false;
                this.getList();
              });
            } else {
              addApiclass(this.form).then(response => {
                this.msgSuccess("新增成功");
                this.open = false;
                this.getList();
              });
            }
          }
        });
      },
      /** 删除按钮操作 */
      handleDelete(row) {
        const ids = row.id || this.ids;
        this.$confirm('是否确认删除接口类名编号为"' + ids + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function () {
          return delApiclass(ids);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        })
      },
      /** 导出按钮操作 */
      handleExport() {
        const queryParams = this.queryParams;
        this.$confirm('是否确认导出所有接口类名数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function () {
          return exportApiclass(queryParams);
        }).then(response => {
          this.download(response.msg);
        })
      }
    }
  };
</script>
