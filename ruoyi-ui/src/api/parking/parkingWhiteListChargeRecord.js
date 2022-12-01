import request from '@/utils/request'

// 查询白名单收费记录列表
export function listParkingWhiteListChargeRecord(query) {
  return request({
    url: '/parking/parkingWhiteListChargeRecord/list',
    method: 'get',
    params: query
  })
}

// 查询白名单收费记录详细
export function getParkingWhiteListChargeRecord(id) {
  return request({
    url: '/parking/parkingWhiteListChargeRecord/' + id,
    method: 'get'
  })
}

// 新增白名单收费记录
export function addParkingWhiteListChargeRecord(data) {
  return request({
    url: '/parking/parkingWhiteListChargeRecord',
    method: 'post',
    data: data
  })
}

// 修改白名单收费记录
export function updateParkingWhiteListChargeRecord(data) {
  return request({
    url: '/parking/parkingWhiteListChargeRecord',
    method: 'put',
    data: data
  })
}

// 删除白名单收费记录
export function delParkingWhiteListChargeRecord(id) {
  return request({
    url: '/parking/parkingWhiteListChargeRecord/' + id,
    method: 'delete'
  })
}
