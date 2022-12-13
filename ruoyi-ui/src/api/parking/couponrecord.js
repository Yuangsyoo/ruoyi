import request from '@/utils/request'

// 查询停车场优惠卷记录列表
export function listCouponrecord(query) {
  return request({
    url: '/parking/couponrecord/list',
    method: 'get',
    params: query
  })
}

// 查询停车场优惠卷记录详细
export function getCouponrecord(id) {
  return request({
    url: '/parking/couponrecord/' + id,
    method: 'get'
  })
}

// 新增停车场优惠卷记录
export function addCouponrecord(data) {
  return request({
    url: '/parking/couponrecord',
    method: 'post',
    data: data
  })
}

// 修改停车场优惠卷记录
export function updateCouponrecord(data) {
  return request({
    url: '/parking/couponrecord',
    method: 'put',
    data: data
  })
}

// 删除停车场优惠卷记录
export function delCouponrecord(id) {
  return request({
    url: '/parking/couponrecord/' + id,
    method: 'delete'
  })
}
