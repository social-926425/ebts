import request from "@/utils/request";

// 获取查询基本信息
export function RealInfo(id){
  return request({
    url: "/data/real/"+id,
    method: 'get',
  })
}
// 查询数据
export function RealData(data){
  return request({
    url: "/data/real",
    method: 'put',
    data:data
  })
}
// 查询数据
export function exportReal(data){
  return request({
    url: "/data/real/export",
    method: 'put',
    data:data
  })
}
