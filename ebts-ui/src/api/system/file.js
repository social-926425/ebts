import request from '@/utils/request'


// 查询文件信息列表
export function listFile(data) {
  return request({
    url: '/system/file/list',
    method: 'put',
    data: data
  })
}

// 导出文件信息
export function exportFile(data) {
  return request({
    url: '/system/file/export',
    method: 'put',
    data: data
  })
}

// 查询文件信息详细
export function getFile(fileId) {
  return request({
    url: '/system/file/' + fileId,
    method: 'get'
  })
}

// 新增文件信息
export function addFile(data) {
  return request({
    url: '/system/file',
    method: 'post',
    data: data
  })
}

// 修改文件信息
export function updateFile(data) {
  return request({
    url: '/system/file',
    method: 'put',
    data: data
  })
}

// 删除文件信息
export function delFile(fileId) {
  return request({
    url: '/system/file/' + fileId,
    method: 'delete'
  })
}

// 获取角色选择框列表
export function getRoleAll(){
  return request({
    url:'/system/role/optionselect',
    method:'get',
    params:{
      type:2
    }
  })
}

// 获取文件夹列表
export function getFolder(){
  return request({
    url:'/system/file/folder',
    method:'get',
  })
}

