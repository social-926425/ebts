<template>
  <el-form ref="apiclassInfoForm" :model="info" :rules="rules" label-width="150px">
    <el-row>
      <el-col :span="12">
        <el-form-item label="类名" prop="cName">
          <el-input placeholder="请输入类名" v-model="info.packageName" :disabled="true"/>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="前缀" prop="prefix">
          <el-input v-model="info.prefix" :disabled="true"/>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="模块名" prop="cName">
          <el-select v-model="info.mId" placeholder="请选择模块" @change="transform" clearable>
            <el-option
              v-for="item in moduleList"
              :key="item.id"
              :label="item.mName"
              :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="类名" prop="cName">
          <el-input placeholder="请输入类名" @input="transform" v-model="info.cName"/>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="作者" prop="author">
          <el-input placeholder="请输入" v-model="info.author"/>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="类描述" prop="cDescribe">
          <el-input placeholder="请输入" v-model="info.cDescribe"/>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="电子邮件" prop="email">
          <el-input placeholder="请输入" v-model="info.email"/>
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
  name: "ApiclassInfoForm",
  props: {
    info: {
      type: Object,
      default: null
    }
  },
  data() {
    return {
      moduleList: [],
      rules: {
        packageName: [
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
    queryListModule().then(respone => {
      this.moduleList = respone.data
    })
  },
  methods: {
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
      this.info.prefix = str + ":" + this.info.cName.toLowerCase()
    },
  }
};
</script>
