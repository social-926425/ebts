import request from '@/utils/request'


// 查询测试a列表
export function listA(data) {
  return request({
    url: '/test/a/list',
    method: 'put',
    data: data
  })
}

// 导出测试a
export function exportA(data) {
  return request({
    url: '/test/a/export',
    method: 'put',
    data: data
  })
}

// 查询测试a详细
export function getA(aid) {
  return request({
    url: '/test/a/' + aid,
    method: 'get'
  })
}

// 新增测试a
export function addA(data) {
  return request({
    url: '/test/a',
    method: 'post',
    data: data
  })
}

// 修改测试a
export function updateA(data) {
  return request({
    url: '/test/a',
    method: 'put',
    data: data
  })
}

// 删除测试a
export function delA(aid) {
  return request({
    url: '/test/a/' + aid,
    method: 'delete'
  })
}