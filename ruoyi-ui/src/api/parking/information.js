import request from '@/utils/request'

// 查询停车场管理列表
export function listInformation(query) {
  return request({
    url: '/parking/information/list',
    method: 'get',
    params: query
  })
}

// 查询停车场管理详细
export function getInformation(id) {
  return request({
    url: '/parking/information/' + id,
    method: 'get'
  })
}

// 新增停车场管理
export function addInformation(data) {
  return request({
    url: '/parking/information',
    method: 'post',
    data: data
  })
}

// 修改停车场管理
export function updateInformation(data) {
  return request({
    url: '/parking/information',
    method: 'put',
    data: data
  })
}
// 修改停车场管理
export function updateInformation1(data) {
  return request({
    url: '/parking/information/edit1',
    method: 'put',
    data: data
  })
}
// 删除停车场管理
export function delInformation(id) {
  return request({
    url: '/parking/information/' + id,
    method: 'delete'
  })
}
// 查询停车场管理详细
export function getParkingLots(id) {
  return request({
    url: '/parking/information/getParkingLots/' + id,
    method: 'get'
  })
}
// 查询停车场管理详细
export function getMoney(id) {
  return request({
    url: '/parking/record/getMoney/' + id,
    method: 'get'
  })
}

export function getDailyInformation(id) {
  return request({
    url: '/parking/record/getDailyInformation/' + id,
    method: 'get'
  })
}
// 查询白名单、过期白名单、黑名单、优惠卷领取总数量、优惠卷过期数量总汇
export function getSumAllListAndCoupon(id) {
  return request({
    url: '/parking/whitelist/getSumAllListAndCoupon/' + id,
    method: 'get'
  })
}
// 获取各个停车场被占用停车位总数
export function getOccupiedParkingSpace(id) {
  return request({
    url: '/parking/information/getOccupiedParkingSpace/' + id,
    method: 'get'
  })
}
