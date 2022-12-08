import request from '@/utils/request'

// 查询黑名单管理列表
export function listBlackList(query) {
  return request({
    url: '/parking/blackList/list',
    method: 'get',
    params: query
  })
}

// 查询黑名单管理详细
export function getBlackList(id) {
  return request({
    url: '/parking/blackList/' + id,
    method: 'get'
  })
}

// 新增黑名单管理
export function addBlackList(data) {
  return request({
    url: '/parking/blackList',
    method: 'post',
    data: data
  })
}

// 修改黑名单管理
export function updateBlackList(data) {
  return request({
    url: '/parking/blackList',
    method: 'put',
    data: data
  })
}

// 删除黑名单管理
export function delBlackList(id) {
  return request({
    url: '/parking/blackList/' + id,
    method: 'delete'
  })
}
