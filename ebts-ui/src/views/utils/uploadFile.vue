<template>
  <div>
    <el-form ref="uploadFrom" :model="form" :rules="rules" label-width="80px">
      <div v-if="vPublic === null">
        <el-form-item label="是否公开">
          <el-radio-group v-model="form.isPublic" @change="isPublicChange">
            <el-radio label="1">公开</el-radio>
            <el-radio label="2">保护</el-radio>
          </el-radio-group>
        </el-form-item>
      </div>
      <div v-show="!isPublicShow">
        <el-form-item label="文件夹">
          <el-select v-model="form.pId" clearable placeholder="请选择">
            <el-option
              v-for="item in folderOptions"
              :key="item.fileId"
              :value="item.fileId"
              :label="item.fileName"/>
          </el-select>
        </el-form-item>
        <el-form-item label="角色" prop="roleIds">
          <el-select v-model="form.roleIds" clearable multiple placeholder="请选择">
            <el-option
              v-for="item in roleOptions"
              :key="item.roleId"
              :value="item.roleId"
              :label="item.roleName"/>
          </el-select>
        </el-form-item>
      </div>
      <el-form-item v-if="this.fileType === null" label="文件类型">
        <el-switch
          v-model="fileTypeShow"
          active-text="文件"
          inactive-color="#1890ff"
          inactive-text="图片" @change="fileTypeChange">
        </el-switch>
      </el-form-item>
      <el-form-item label="图片上传" v-show="!fileTypeShow">
        <ImageUpload v-model="staticUrl" v-on:change="changeAddress"/>
      </el-form-item>
      <el-form-item label="文件上传" v-show="fileTypeShow">
        <FileUpload v-model="staticUrl" v-on:change="changeAddress"/>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import {getRoleAll, getFolder} from "@/api/system/file";
import {uploadFile} from '@/utils/fileUtils'
import ImageUpload from '@/components/ImageUpload'
import FileUpload from '@/components/FileUpload'

export default {
  name: "uploadFile",
  components: {
    ImageUpload,
    FileUpload
  },
  props: {
    // value: {
    //   type: String,
    //   default: "",
    // },
    /**
     * 文件类型 图片:image 文件:file
     */
    fileType: {
      type: String,
      default: null
    },
    unionId: {
      type: Number,
      default: null
    },
    /**
     * 返回数据(true)或者已经上传到云端返回连接(false)
     */
    vData: {
      type: Boolean,
      default: true
    },
    /**
     * 是否公开 公开:true 保护:false 手动选择:父级组件不设置
     */
    vPublic: {
      type: Boolean,
      default: null
    },
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
      form: {
        pId: null,
        roleIds: [],
        isPublic: (this.vPublic === null) ? "1" : (this.vPublic === true) ? "1" : "2",
        fileAddr: "",
        unionId: this.unionId,
      },
      staticUrl: "",
      // 文件夹列表
      folderOptions: [],
      // 角色列表
      roleOptions: [],
      isPublicShow: (this.vPublic === null) ? true : this.vPublic,
      fileTypeShow: this.fileType === null ? false : this.fileType !== "image",
      rules: {
        roleIds: [
          {required: true, validator: checkRoleIds, trigger: 'change'},
        ]
      }
    }
  },
  created() {
    getRoleAll().then(res => {
      this.roleOptions = res.data
    })
    getFolder().then(res => {
      this.folderOptions = res.data
    })
  },
  methods: {
    /**
     *是否公开值改变
     */
    isPublicChange() {
      this.isPublicShow = (this.form.isPublic === "1")
    },
    /**
     * 文件类型改变
     */
    fileTypeChange() {
      this.form.fileAddr = ""
      this.staticUrl = ""
    },
    /**
     * 改变地址
     */
    changeAddress(addr) {
      this.form.fileAddr = addr
    },
    uploadFile() {
      let flag = false
      this.$refs["uploadFrom"].validate(valid => {
        if (valid) {
          let roleIds = ""
          if (this.form.isPublic === "2") {
            for (let i = 0; i < this.form.roleIds.length; i++) {
              roleIds += this.form.roleIds[i]
              if (i < this.form.roleIds.length - 1) {
                roleIds += ","
              }
            }
          }
          this.form.roleIds = roleIds
          if (this.vData) {
            this.$emit("input", this.form)
            flag = true
          } else {
            flag = uploadFile(this.form).then(res => {
              this.$emit("input", res)
            })
          }
        } else {
          flag = false
        }
      })
      return flag
    }
  }
}
</script>

<style scoped>

</style>
