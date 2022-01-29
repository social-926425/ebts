import request from '@/utils/request'

// 查询学生列表
export function listStu(query) {
  return request({
    url: '/test/stu/list',
    method: 'get',
    params: query
  })
}
export function mapTest(){
  return request({
    url: '/test/map',
    method: 'get',
  })
}
// 查询学生详细
export function getStu(id) {
  return request({
    url: '/test/stu/' + id,
    method: 'get'
  })
}

// 新增学生
export function addStu(data) {
  return request({
    url: '/test/stu',
    method: 'post',
    data: data
  })
}

// 修改学生
export function updateStu(data) {
  return request({
    url: '/test/stu',
    method: 'put',
    data: data
  })
}

// 删除学生
export function delStu(id) {
  return request({
    url: '/test/stu/' + id,
    method: 'delete'
  })
}

// 导出学生
export function exportStu(query) {
  return request({
    url: '/test/stu/export',
    method: 'get',
    params: query
  })
}
