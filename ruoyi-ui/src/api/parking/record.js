import request from '@/utils/request'

// 查询停车记录列表
export function listRecord(query) {
  return request({
    url: '/parking/record/list',
    method: 'get',
    params: query
  })
}

// 查询停车记录详细
export function getRecord(id) {
  return request({
    url: '/parking/record/' + id,
    method: 'get'
  })
}

// 新增停车记录
export function addRecord(data) {
  return request({
    url: '/parking/record',
    method: 'post',
    data: data
  })
}

// 修改停车记录
export function updateRecord(data) {
  return request({
    url: '/parking/record',
    method: 'put',
    data: data
  })
}

// 删除停车记录
export function delRecord(id) {
  return request({
    url: '/parking/record/' + id,
    method: 'delete'
  })
}
// 查询最近缴费记录详细
export function getPayRecord(id) {
  return request({
    url: '/parking/record/getPayRecord/' + id,
    method: 'get'
  })
}
