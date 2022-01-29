import request from '@/utils/request'

// 查询万能查询列表
export function listQuery(query) {
  return request({
    url: '/tool/query/list',
    method: 'get',
    params: query
  })
}

// 查询万能查询详细
export function getQuery(id) {
  return request({
    url: '/tool/query/' + id,
    method: 'get'
  })
}

// 新增万能查询
export function addQuery(data) {
  return request({
    url: '/tool/query',
    method: 'post',
    data: data
  })
}

// 修改万能查询
export function updateQuery(data) {
  return request({
    url: '/tool/query',
    method: 'put',
    data: data
  })
}

// 删除万能查询
export function delQuery(id) {
  return request({
    url: '/tool/query/' + id,
    method: 'delete'
  })
}

// 导出万能查询
export function exportQuery(query) {
  return request({
    url: '/tool/query/export',
    method: 'get',
    params: query
  })
}

// 获取条件
export function getQueryInfo(id) {
  return request({
    url: '/query/'+id,
    method: 'get',
  })
}
// 修改条件
export function editQueryInfo(data) {
  return request({
    url: '/query',
    method: 'put',
    data: data
  })
}

// 预览
export function previewQueryData(data) {
  return request({
    url: '/query/preview',
    method: 'put',
    data: data
  })
}

export function exportMock(data){
  return request({
    url: '/query/export',
    method: 'put',
    data: data
  })
}

//发布撤销
export function Release(data){
  return request({
    url: "/query/release",
    method: 'put',
    data:data
  })
}
