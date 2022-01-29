import request from '@/utils/request'

// 查询接口类名列表
export function listApiclass(query) {
  return request({
    url: '/tool/apiclass/list',
    method: 'get',
    params: query
  })
}

// 获取模块管理选项列表
export function queryListApiclass() {
  return request({
    url: '/tool/apiclass/querylist',
    method: 'get',
  })
}
// 查询接口类名详细
export function getApiclass(id) {
  return request({
    url: '/tool/apiclass/' + id,
    method: 'get'
  })
}

// 新增接口类名
export function addApiclass(data) {
  return request({
    url: '/tool/apiclass',
    method: 'post',
    data: data
  })
}

// 修改接口类名
export function updateApiclass(data) {
  return request({
    url: '/tool/apiclass',
    method: 'put',
    data: data
  })
}

// 删除接口类名
export function delApiclass(id) {
  return request({
    url: '/tool/apiclass/' + id,
    method: 'delete'
  })
}

// 导出接口类名
export function exportApiclass(query) {
  return request({
    url: '/tool/apiclass/export',
    method: 'get',
    params: query
  })
}
