<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="正则名称" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入正则名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="正则内容" prop="regular">
        <el-input
          v-model="queryParams.regular"
          placeholder="请输入正则内容"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="验证内容" prop="validation">
        <el-input
          v-model="queryParams.validation"
          placeholder="请输入验证内容"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="是否启用" prop="enable">
        <el-select v-model="queryParams.enable" placeholder="请选择是否启用" v-on:change="handleQuery" clearable size="small">
          <el-option
            v-for="dict in enableOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="cyan" icon="el-icon-search" size="mini" v-on:click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" v-on:click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          v-on:click="handleAdd"
          v-hasPermi="['system:regular:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          icon="el-icon-edit"
          plain
          size="mini"
          :disabled="single"
          v-on:click="handleUpdate"
          v-hasPermi="['system:regular:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          v-on:click="handleDelete"
          v-hasPermi="['system:regular:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          v-on:click="handleExport"
          v-hasPermi="['system:regular:export']"
        >导出</el-button>
      </el-col>
	  <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="regularList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
<!--      <el-table-column label="id" align="center" prop="id" />-->
      <el-table-column label="正则名称" align="center" prop="name" />
      <el-table-column label="正则内容" align="center" prop="regular" />
      <el-table-column label="验证内容" align="center" prop="validation" />
      <el-table-column label="是否启用" align="center" prop="enable" :formatter="enableFormat" />
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
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            v-on:click="handleUpdate(scope.row)"
            v-hasPermi="['system:regular:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            v-on:click="handleDelete(scope.row)"
            v-hasPermi="['system:regular:remove']"
          >删除</el-button>
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

    <!-- 添加或修改校验规则对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="正则名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入正则名称" />
        </el-form-item>
        <el-form-item label="正则内容" prop="regular">
          <el-input v-model="form.regular" placeholder="请输入正则内容" />
        </el-form-item>
        <el-form-item label="验证内容" prop="validation">
          <el-input v-model="form.validation" placeholder="请输入验证内容" />
        </el-form-item>
        <el-form-item label="是否启用">
          <el-radio-group v-model="form.enable">
            <el-radio
              v-for="dict in enableOptions"
              :key="dict.dictValue"
              :label="parseInt(dict.dictValue)"
            >{{dict.dictLabel}}</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" v-on:click="submitForm">确 定</el-button>
        <el-button v-on:click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listRegular, getRegular, delRegular, addRegular, updateRegular, exportRegular } from "@/api/tool/regular";

export default {
  name: "Regular",
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
      // 校验规则表格数据
      regularList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 是否启用 1:启动 2:关闭字典
      enableOptions: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: null,
        regular: null,
        validation: null,
        enable: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        name: [
          { required: true, message: "正则名称不能为空", trigger: "blur" }
        ],
        regular: [
          { required: true, message: "正则内容不能为空", trigger: "blur" }
        ],
        validation: [
          { required: true, message: "验证内容不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {
    this.getList();
    this.getDicts("sys_regular_enable").then(response => {
      this.enableOptions = response.data;
    });
  },
  methods: {
    /** 查询校验规则列表 */
    getList() {
      this.loading = true;
      listRegular(this.queryParams).then(response => {
        this.regularList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },

    // 是否启用 1:启动 2:关闭字典翻译
    enableFormat(row, column) {
      return this.selectDictLabel(this.enableOptions, row.enable);
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        name: null,
        regular: null,
        validation: null,
        enable: 0,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null
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
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加校验规则";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getRegular(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改校验规则";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateRegular(this.form).then(response => {
              this.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addRegular(this.form).then(response => {
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
      this.$confirm('是否确认删除校验规则编号为"' + ids + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delRegular(ids);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        })
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出所有校验规则数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return exportRegular(queryParams);
        }).then(response => {
          this.download(response.msg);
        })
    }
  }
};
</script>
