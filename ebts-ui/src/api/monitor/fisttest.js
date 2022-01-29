import request from '@/utils/request'

// 查询测试列表
export function listFisttest(query) {
  return request({
    url: '/monitor/fisttest/list',
    method: 'get',
    params: query
  })
}

// 查询测试详细
export function getFisttest(id) {
  return request({
    url: '/monitor/fisttest/' + id,
    method: 'get'
  })
}

// 新增测试
export function addFisttest(data) {
  return request({
    url: '/monitor/fisttest',
    method: 'post',
    data: data
  })
}

// 修改测试
export function updateFisttest(data) {
  return request({
    url: '/monitor/fisttest',
    method: 'put',
    data: data
  })
}

// 删除测试
export function delFisttest(id) {
  return request({
    url: '/monitor/fisttest/' + id,
    method: 'delete'
  })
}

// 导出测试
export function exportFisttest(query) {
  return request({
    url: '/monitor/fisttest/export',
    method: 'get',
    params: query
  })
}