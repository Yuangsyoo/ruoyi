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

// 删除停车场管理
export function delInformation(id) {
  return request({
    url: '/parking/information/' + id,
    method: 'delete'
  })
}