import request from '@/utils/request'

// 查询接口信息列表
export function listIntertable(query) {
  return request({
    url: '/generator/intertable/list',
    method: 'get',
    params: query
  })
}

// 查询接口信息详细
export function getIntertable(id) {
  return request({
    url: '/generator/intertable/' + id,
    method: 'get'
  })
}

// 查询接口信息详细
export function getClassPreview(id) {
  return request({
    url: 'generator/intertable/classpreview/' + id,
    method: 'get'
  })
}

// 查询接口信息详细
export function getModulePreview(id) {
  return request({
    url: 'generator/intertable/modulepreview/' + id,
    method: 'get'
  })
}

// 新增接口信息
export function addIntertable(data) {
  return request({
    url: '/generator/intertable',
    method: 'post',
    data: data
  })
}

// 修改接口信息
export function updateIntertable(data) {
  return request({
    url: '/generator/intertable',
    method: 'put',
    data: data
  })
}

// 删除接口信息
export function delIntertable(id) {
  return request({
    url: '/generator/intertable/' + id,
    method: 'delete'
  })
}

// 导出接口信息
export function exportIntertable(query) {
  return request({
    url: '/generator/intertable/export',
    method: 'get',
    params: query
  })
}
