import request from '@/utils/request'

// 查询充值车管理列表
export function listRecharge(query) {
  return request({
    url: '/parking/recharge/list',
    method: 'get',
    params: query
  })
}

// 查询充值车管理详细
export function getRecharge(id) {
  return request({
    url: '/parking/recharge/' + id,
    method: 'get'
  })
}

// 新增充值车管理
export function addRecharge(data) {
  return request({
    url: '/parking/recharge',
    method: 'post',
    data: data
  })
}

// 修改充值车管理
export function updateRecharge(data) {
  return request({
    url: '/parking/recharge',
    method: 'put',
    data: data
  })
}
// 修改充值车管理
export function updateRecharge1(data) {
  return request({
    url: '/parking/recharge/update',
    method: 'put',
    data: data
  })
}
// 删除充值车管理
export function delRecharge(id) {
  return request({
    url: '/parking/recharge/' + id,
    method: 'delete'
  })
}
