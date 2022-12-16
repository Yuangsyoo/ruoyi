import request from '@/utils/request'

// 查询停车场固定车位列表
export function listFixedparkingspace(query) {
  return request({
    url: '/parking/fixedparkingspace/list',
    method: 'get',
    params: query
  })
}

// 查询停车场固定车位详细
export function getFixedparkingspace(id) {
  return request({
    url: '/parking/fixedparkingspace/' + id,
    method: 'get'
  })
}

// 新增停车场固定车位
export function addFixedparkingspace(data) {
  return request({
    url: '/parking/fixedparkingspace',
    method: 'post',
    data: data
  })
}

// 修改停车场固定车位
export function updateFixedparkingspace(data) {
  return request({
    url: '/parking/fixedparkingspace',
    method: 'put',
    data: data
  })
}

// 删除停车场固定车位
export function delFixedparkingspace(id) {
  return request({
    url: '/parking/fixedparkingspace/' + id,
    method: 'delete'
  })
}
