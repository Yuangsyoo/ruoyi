import request from '@/utils/request'

// 查询充值车操作记录列表
export function listRechargerecord(query) {
  return request({
    url: '/parking/rechargerecord/list',
    method: 'get',
    params: query
  })
}

// 查询充值车操作记录详细
export function getRechargerecord(id) {
  return request({
    url: '/parking/rechargerecord/' + id,
    method: 'get'
  })
}

// 新增充值车操作记录
export function addRechargerecord(data) {
  return request({
    url: '/parking/rechargerecord',
    method: 'post',
    data: data
  })
}

// 修改充值车操作记录
export function updateRechargerecord(data) {
  return request({
    url: '/parking/rechargerecord',
    method: 'put',
    data: data
  })
}

// 删除充值车操作记录
export function delRechargerecord(id) {
  return request({
    url: '/parking/rechargerecord/' + id,
    method: 'delete'
  })
}
