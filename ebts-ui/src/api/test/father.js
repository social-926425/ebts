import request from '@/utils/request'


// 查询关联查询父列表
export function listFather(data) {
  return request({
    url: '/test/father/list',
    method: 'put',
    data: data
  })
}

// 导出关联查询父
export function exportFather(data) {
  return request({
    url: '/test/father/export',
    method: 'put',
    data: data
  })
}

// 查询关联查询父详细
export function getFather(id) {
  return request({
    url: '/test/father/' + id,
    method: 'get'
  })
}

// 新增关联查询父
export function addFather(data) {
  return request({
    url: '/test/father',
    method: 'post',
    data: data
  })
}

// 修改关联查询父
export function updateFather(data) {
  return request({
    url: '/test/father',
    method: 'put',
    data: data
  })
}

// 删除关联查询父
export function delFather(id) {
  return request({
    url: '/test/father/' + id,
    method: 'delete'
  })
}
