import request from '@/utils/request'

// 查询校验规则列表
export function listRegular(query) {
  return request({
    url: '/tool/regular/list',
    method: 'get',
    params: query
  })
}

// 查询校验规则详细
export function getRegular(id) {
  return request({
    url: '/tool/regular/' + id,
    method: 'get'
  })
}

// 新增校验规则
export function addRegular(data) {
  return request({
    url: '/tool/regular',
    method: 'post',
    data: data
  })
}

// 修改校验规则
export function updateRegular(data) {
  return request({
    url: '/tool/regular',
    method: 'put',
    data: data
  })
}

// 删除校验规则
export function delRegular(id) {
  return request({
    url: '/tool/regular/' + id,
    method: 'delete'
  })
}

// 导出校验规则
export function exportRegular(query) {
  return request({
    url: '/system/regular/export',
    method: 'get',
    params: query
  })
}
