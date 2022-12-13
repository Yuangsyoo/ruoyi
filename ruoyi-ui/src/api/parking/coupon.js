import request from '@/utils/request'

// 查询优惠卷列表
export function listCoupon(query) {
  return request({
    url: '/parking/coupon/list',
    method: 'get',
    params: query
  })
}

// 查询优惠卷详细
export function getCoupon(id) {
  return request({
    url: '/parking/coupon/' + id,
    method: 'get'
  })
}

// 新增优惠卷
export function addCoupon(data) {
  return request({
    url: '/parking/coupon',
    method: 'post',
    data: data
  })
}

// 修改优惠卷
export function updateCoupon(data) {
  return request({
    url: '/parking/coupon',
    method: 'put',
    data: data
  })
}

// 删除优惠卷
export function delCoupon(id) {
  return request({
    url: '/parking/coupon/' + id,
    method: 'delete'
  })
}
