<template>
  <el-form ref="moduleInfoForm" :model="info" :rules="rules" label-width="150px">
    <el-row>
      <el-col :span="12">
        <el-form-item label="模块名称" prop="mDescribe">
          <el-input placeholder="请输入类名" v-model="info.mDescribe"/>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="模块描述" prop="mName">
          <el-input placeholder="请输入类名" v-model="info.mName"/>
        </el-form-item>
      </el-col>
      <el-col :span="24">
        <el-form-item label="备注">
          <el-input type="textarea" :rows="3" v-model="info.remark"></el-input>
        </el-form-item>
      </el-col>
    </el-row>
  </el-form>
</template>
<script>
  import {queryListModule} from "@/api/tool/module"
  export default {
    name: "ModuleInfoForm",
    props: {
      info: {
        type: Object,
        default: null
      }
    },
    data() {
      return {
        moduleList:[],
        rules: {
          mName: [
            { required: true, message: "模块名称不能为空}", trigger: "blur" },
          ],
          mDescribe: [
            { required: true, message: "模块描述不能为空}", trigger: "blur" },
          ],
        }
      };
    },
    created() {
      queryListModule().then(respone => {
        this.moduleList = respone.data
      })
    },
    methods:{
      transform() {
        var mid = this.info.mId
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
        this.info.packageName = 'com.hchyun.' + str
      },
    }
  };
</script>
