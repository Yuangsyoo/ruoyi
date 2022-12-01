import request from '@/utils/request'

// 查询白名单操作记录列表
export function listParkingWhiteListOperationRecord(query) {
  return request({
    url: '/parking/parkingWhiteListOperationRecord/list',
    method: 'get',
    params: query
  })
}

// 查询白名单操作记录详细
export function getParkingWhiteListOperationRecord(id) {
  return request({
    url: '/parking/parkingWhiteListOperationRecord/' + id,
    method: 'get'
  })
}

// 新增白名单操作记录
export function addParkingWhiteListOperationRecord(data) {
  return request({
    url: '/parking/parkingWhiteListOperationRecord',
    method: 'post',
    data: data
  })
}

// 修改白名单操作记录
export function updateParkingWhiteListOperationRecord(data) {
  return request({
    url: '/parking/parkingWhiteListOperationRecord',
    method: 'put',
    data: data
  })
}

// 删除白名单操作记录
export function delParkingWhiteListOperationRecord(id) {
  return request({
    url: '/parking/parkingWhiteListOperationRecord/' + id,
    method: 'delete'
  })
}
