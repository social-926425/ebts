import request from '@/utils/request'

// 查询成绩列表
export function listResults(query) {
  return request({
    url: '/test/results/list',
    method: 'get',
    params: query
  })
}

// 查询成绩详细
export function getResults(id) {
  return request({
    url: '/test/results/' + id,
    method: 'get'
  })
}

// 新增成绩
export function addResults(data) {
  return request({
    url: '/test/results',
    method: 'post',
    data: data
  })
}

// 修改成绩
export function updateResults(data) {
  return request({
    url: '/test/results',
    method: 'put',
    data: data
  })
}

// 删除成绩
export function delResults(id) {
  return request({
    url: '/test/results/' + id,
    method: 'delete'
  })
}

// 导出成绩
export function exportResults(query) {
  return request({
    url: '/test/results/export',
    method: 'get',
    params: query
  })
}