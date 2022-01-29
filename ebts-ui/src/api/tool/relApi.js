import request from "@/utils/request";

/**
 * 获取到gen_rel_table表的自增字段
 * @returns
 */
export function getRelId(){
  return request({
    url:'/seq/next/gen_rel_table',
    method:'get',
  })
}
/**
 * 获取关联表表的字段信息
 * @param tableName 关联表表名
 * @param relId 关联表id
 * @returns
 */
export function getRelColumns(tableName,relId){
  return request({
    url: '/rel/'+tableName+'/'+relId,
    method: 'get'
  })
}
/**
 * 获取表信息
 * @returns
 */
export function getTableInfos() {
  return request({
    url: '/rel/tableinfo',
    method: 'get'
  })
}

/**
 * 获取表字段信息
 * @returns
 */
export function getTableColumns(tableName) {
  return request({
    url: '/rel/'+tableName,
    method: 'get'
  })
}
/**
 * 获取子表所以信息(基本信息 字段信息)
 * @returns
 */
export function getRelColumnInfos(tableId) {
  return request({
    url: '/rel/colums/'+tableId,
    method: 'get'
  })
}
