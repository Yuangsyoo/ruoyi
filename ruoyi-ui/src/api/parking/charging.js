import request from '@/utils/request'

// 查询计费规则列表
export function listCharging(query) {
  return request({
    url: '/parking/charging/list',
    method: 'get',
    params: query
  })
}

// 查询计费规则详细
export function getCharging(id) {
  return request({
    url: '/parking/charging/' + id,
    method: 'get'
  })
}

// 新增计费规则
export function addCharging(data) {
  return request({
    url: '/parking/charging',
    method: 'post',
    data: data
  })
}

// 修改计费规则
export function updateCharging(data) {
  return request({
    url: '/parking/charging',
    method: 'put',
    data: data
  })
}

// 删除计费规则
export function delCharging(id) {
  return request({
    url: '/parking/charging/' + id,
    method: 'delete'
  })
}
