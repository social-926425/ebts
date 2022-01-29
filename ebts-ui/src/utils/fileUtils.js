import axios from "axios";
import {getToken} from "@/utils/auth";
import {Message} from "element-ui";

export function downloadFile(fileId) {
  return axios({
    method: 'get',
    url: process.env.VUE_APP_BASE_API + '/system/file/download/' + fileId,
    headers: {
      token: 'Bearer ' + getToken() // 让每个请求携带自定义token 请根据实际情况自行修改
    }
  })
}

export function uploadFile(data) {
  return axios({
    url: process.env.VUE_APP_BASE_API + '/system/file',
    method: 'post',
    data: data,
    headers: {
      Authorization: 'Bearer ' + getToken() // 让每个请求携带自定义token 请根据实际情况自行修改
    }
  }).then(res => {
    if (res.data.code === 200) {
      return res.data.data
    } else {
      Message({
        message: res.data.msg,
        type: 'error'
      })
      return false
    }
  })
}
