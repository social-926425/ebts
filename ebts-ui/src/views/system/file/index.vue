<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="文件名称" prop="fileName">
        <el-input
          v-model="queryParams.fileName"
          placeholder="请输入文件名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="文件路径" prop="fileAddr">
        <el-input
          v-model="queryParams.fileAddr"
          placeholder="请输入文件路径"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="文件类型" prop="fileType">
        <el-select v-model="queryParams.fileType" placeholder="请选择文件类型" clearable size="small">
          <el-option
            v-for="dict in fileTypeOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="是否公开" prop="isPublic">
        <el-select v-model="queryParams.isPublic" placeholder="请选择是否公开" clearable size="small">
          <el-option
            v-for="dict in isPublicOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
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
      <!--      <el-col :span="1.5">-->
      <!--        <el-button-->
      <!--          type="primary"-->
      <!--          plain-->
      <!--          icon="el-icon-plus"-->
      <!--          size="mini"-->
      <!--          @click="handleAdd"-->
      <!--          v-hasPermi="['system:file:add']"-->
      <!--        >新增</el-button>-->
      <!--      </el-col>-->
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          @click="handleFileUpdate"
          v-hasPermi="['system:file:edit']"
        >文件上传
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['system:file:edit']"
        >修改
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['system:file:remove']"
        >删除
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:file:export']"
        >导出
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="fileList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="文件名称" align="center" prop="fileName"/>
      <el-table-column label="文件路径" align="center" prop="fileAddr"/>
      <el-table-column label="文件类型" align="center" prop="fileType" :formatter="fileTypeFormat"/>
      <el-table-column label="文件大小(MB)" align="center" prop="fileSize"/>
      <el-table-column label="是否公开" align="center" prop="isPublic" :formatter="isPublicFormat"/>
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
          <el-button v-if="scope.row.isPublic === '2'"
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:file:edit']"
          >修改
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:file:remove']"
          >删除
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-download"
            @click="handleDownload(scope.row)"
          >下载
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.params.pageNum"
      :limit.sync="queryParams.params.pageSize"
      @pagination="getList"
    />

    <el-dialog :title="fileForm.title" :visible.sync="fileForm.open" width="500px" append-to-body>
      <el-form ref="uploadFrom" :model="form" :rules="rules" label-width="80px">
        <el-form-item v-show="isAdd" label="是否公开">
          <el-radio-group v-model="form.isPublic" @change="isPublicChange">
            <el-radio label="1">公开</el-radio>
            <el-radio label="2">保护</el-radio>
          </el-radio-group>
        </el-form-item>
        <div v-show="isPublicShow">
          <el-form-item v-show="isAdd" label="文件夹">
            <el-select v-model="form.pId" clearable placeholder="请选择">
              <el-option
                v-for="item in folderOptions"
                :key="item.fileId"
                :label="item.fileName"
                :value="item.fileId"
              ></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="角色" prop="roleIds">
            <el-select v-model="form.roleIds" clearable multiple placeholder="请选择">
              <el-option
                v-for="item in roleOptions"
                :key="item.roleId"
                :label="item.roleName"
                :value="item.roleId"
                :disabled="item.status == 1"
              ></el-option>
            </el-select>
          </el-form-item>
        </div>
        <div v-show="isAdd">
          <el-form-item label="文件类型">
            <el-switch
              v-model="fileForm.fileType"
              active-text="文件"
              inactive-text="图片" @change="fileTypeChange">
            </el-switch>
          </el-form-item>
          <el-form-item label="图片上传" v-show="!fileForm.fileType" >
            <ImageUpload v-model="fileForm.url" v-on:change="changeAddress"/>
          </el-form-item>
          <el-form-item label="文件上传" v-show="fileForm.fileType" >
            <FileUpload v-model="fileForm.url" v-on:change="changeAddress"/>
          </el-form-item>
        </div>
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {listFile, getFile, delFile, addFile, updateFile, exportFile, getRoleAll, getFolder} from "@/api/system/file";
import {downloadFile} from '@/utils/fileUtils'
import ImageUpload from '@/components/ImageUpload';
import FileUpload from '@/components/FileUpload';

export default {
  name: "File",
  components: {
    ImageUpload,
    FileUpload,
  },
  data() {
    const checkRoleIds = (rule, value, callback) => {
      if (this.form.isPublic === "2") {
        if (value.length === 0) {
          callback(new Error('角色不能为空'));
        } else {
          callback();
        }
      } else {
        callback()
      }
    };
    return {
      fileForm: {
        title: "",
        url: "",
        open: false,
        fileType: false,
      },
      // 角色选项
      roleOptions: [],
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
      isPublicShow: false,
      // 总条数
      total: 0,
      // 文件信息表格数据
      fileList: [],
      // 弹出层标题
      title: "",
      isAdd: true,
      folderOptions: [],
      // 文件类型(文件夹:folder,文件:file)字典
      fileTypeOptions: [],
      // 是否公开字典
      isPublicOptions: [],
      // 创建时间时间范围
      daterangeCreateTime: [],
      // 查询参数
      queryParams: {
        params: {
          pageNum: 1,
          pageSize: 10,
        },
        fileName: null,
        fileAddr: null,
        fileType: null,
        isPublic: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        roleIds: [
          {required: true, validator: checkRoleIds, trigger: 'change'},
        ]
      },
    };
  },
  created() {
    this.getList();
    this.getDicts("sys_fIle_type").then(response => {
      this.fileTypeOptions = response.data;
    });
    this.getDicts("sys_file_public").then(response => {
      this.isPublicOptions = response.data;
    });
    getRoleAll().then(res => {
      this.roleOptions = res.data
    })
    getFolder().then(res => {
      this.folderOptions = res.data
    })
  },
  methods: {
    /** 查询文件信息列表 */
    getList() {
      this.loading = true;
      if (null != this.daterangeCreateTime && '' != this.daterangeCreateTime) {
        this.queryParams.params["beginCreateTime"] = this.daterangeCreateTime[0];
        this.queryParams.params["endCreateTime"] = this.daterangeCreateTime[1];
      }
      listFile(this.queryParams).then(response => {
        this.fileList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    fileTypeChange() {
      this.form.fileAddr = ""
      this.fileForm.url = ""
    },
    /**
     *是否公开值改变
     */
    isPublicChange() {
      this.isPublicShow = (this.form.isPublic !== "1")
    },
    /**
     * 改变地址
     */
    changeAddress(addr) {
      this.form.fileAddr = addr
    },
    /** 文件上传 */
    handleFileUpdate() {
      this.reset()
      this.isAdd = true
      this.fileForm.title = "文件上传"
      this.fileForm.open = true
    },
    // 文件类型字典翻译
    fileTypeFormat(row, column) {
      return this.selectDictLabel(this.fileTypeOptions, row.fileType);
    },
    // 是否公开字典翻译
    isPublicFormat(row, column) {
      return this.selectDictLabel(this.isPublicOptions, row.isPublic);
    },
    // 取消按钮
    cancel() {
      this.fileForm.open = false;
      this.reset();
    },
    // 下载按钮
    handleDownload(row) {
      this.$confirm("是否确认下载" + row.fileName + "?", "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        return downloadFile(row.fileId)
      }).then(res => {
        this.download(res.data.msg);
      })
    },
    // 表单重置
    reset() {
      this.form = {
        fileId: null,
        pId: null,
        roleIds: [],
        fileName: null,
        mapping: null,
        fileAddr: null,
        fileType: null,
        isPublic: "1",
      };
      this.isPublicShow = false
      this.fileForm.url = null
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
    /**
     * 多选框选中数据
     */
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.fileId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加文件信息";
    },
    changeArray() {
      let roleIds = this.form.roleIds.split(",")
      this.form.roleIds = []
      for (let i = 0; i < roleIds.length; i++) {
        this.form.roleIds[i] = parseInt(roleIds[i])
      }
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      this.isAdd = false
      this.isPublicShow = true
      const fileId = row.fileId || this.ids
      getFile(fileId).then(response => {
        this.form = response.data;
        this.changeArray()
        this.fileForm.open = true;
        this.fileForm.title = "修改文件信息";
        console.log(this.form)
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["uploadFrom"].validate(valid => {
        if (valid) {
          let roleIds = ""
          if (this.form.isPublic ==="2"){
            for (let i = 0; i < this.form.roleIds.length; i++) {
              roleIds += this.form.roleIds[i]
              if (i < this.form.roleIds.length - 1) {
                roleIds += ","
              }
            }
          }
          this.form.roleIds = roleIds
          if (this.form.fileId != null) {
            updateFile(this.form).then(response => {
              this.msgSuccess("修改成功");
              this.fileForm.open = false;
              this.changeArray()
              this.getList();
            });
          } else {
            addFile(this.form).then(response => {
              this.msgSuccess("新增成功");
              this.fileForm.open = false;
              this.changeArray()
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const fileIds = row.fileId || this.ids;
      this.$confirm('是否确认删除文件信息编号为"' + fileIds + '"的数据项?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        return delFile(fileIds);
      }).then(() => {
        this.getList();
        this.msgSuccess("删除成功");
      })
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出所有文件信息数据项?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        return exportFile(queryParams);
      }).then(response => {
        this.download(response.msg);
      })
    }
  }
};
</script>
