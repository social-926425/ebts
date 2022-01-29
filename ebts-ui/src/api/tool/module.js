import request from '@/utils/request'



// 查询模块管理列表
export function listModule(query) {
  return request({
    url: '/tool/module/list',
    method: 'get',
    params: query
  })
}

// 获取模块管理选项列表
export function queryListModule() {
  return request({
    url: '/tool/module/querylist',
    method: 'get',
  })
}


// 查询模块管理详细
export function getModule(id) {
  return request({
    url: '/tool/module/' + id,
    method: 'get'
  })
}

// 新增模块管理
export function addModule(data) {
  return request({
    url: '/tool/module',
    method: 'post',
    data: data
  })
}

// 修改模块管理
export function updateModule(data) {
  return request({
    url: '/tool/module',
    method: 'put',
    data: data
  })
}

// 删除模块管理
export function delModule(id) {
  return request({
    url: '/tool/module/' + id,
    method: 'delete'
  })
}

// 导出模块管理
export function exportModule(query) {
  return request({
    url: '/tool/module/export',
    method: 'get',
    params: query
  })
}
