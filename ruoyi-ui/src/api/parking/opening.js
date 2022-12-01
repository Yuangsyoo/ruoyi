import request from '@/utils/request'

// 查询停车场手动开杆管理列表
export function listOpening(query) {
  return request({
    url: '/parking/opening/list',
    method: 'get',
    params: query
  })
}

// 查询停车场手动开杆管理详细
export function getOpening(id) {
  return request({
    url: '/parking/opening/' + id,
    method: 'get'
  })
}

// 新增停车场手动开杆管理
export function addOpening(data) {
  return request({
    url: '/parking/opening',
    method: 'post',
    data: data
  })
}

// 修改停车场手动开杆管理
export function updateOpening(data) {
  return request({
    url: '/parking/opening',
    method: 'put',
    data: data
  })
}

// 删除停车场手动开杆管理
export function delOpening(id) {
  return request({
    url: '/parking/opening/' + id,
    method: 'delete'
  })
}
