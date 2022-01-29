import request from '@/utils/request'

// 查询树测试列表
export function listTree(query) {
  return request({
    url: '/test/tree/list',
    method: 'get',
    params: query
  })
}

// 查询树测试详细
export function getTree(id) {
  return request({
    url: '/test/tree/' + id,
    method: 'get'
  })
}

// 新增树测试
export function addTree(data) {
  return request({
    url: '/test/tree',
    method: 'post',
    data: data
  })
}

// 修改树测试
export function updateTree(data) {
  return request({
    url: '/test/tree',
    method: 'put',
    data: data
  })
}

// 删除树测试
export function delTree(id) {
  return request({
    url: '/test/tree/' + id,
    method: 'delete'
  })
}

// 导出树测试
export function exportTree(query) {
  return request({
    url: '/test/tree/export',
    method: 'get',
    params: query
  })
}