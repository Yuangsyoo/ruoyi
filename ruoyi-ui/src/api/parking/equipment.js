import request from '@/utils/request'

// 查询停车场设备管理列表
export function listEquipment(query) {
  return request({
    url: '/parking/equipment/list',
    method: 'get',
    params: query
  })
}

// 查询停车场设备管理详细
export function getEquipment(id) {
  return request({
    url: '/parking/equipment/' + id,
    method: 'get'
  })
}

// 新增停车场设备管理
export function addEquipment(data) {
  return request({
    url: '/parking/equipment',
    method: 'post',
    data: data
  })
}

// 修改停车场设备管理
export function updateEquipment(data) {
  return request({
    url: '/parking/equipment',
    method: 'put',
    data: data
  })
}

// 删除停车场设备管理
export function delEquipment(id) {
  return request({
    url: '/parking/equipment/' + id,
    method: 'delete'
  })
}
