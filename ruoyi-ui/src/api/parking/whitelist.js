import request from '@/utils/request'

// 查询停车场白名单列表
export function listWhitelist(query) {
  return request({
    url: '/parking/whitelist/list',
    method: 'get',
    params: query
  })
}

// 查询停车场白名单详细
export function getWhitelist(id) {
  return request({
    url: '/parking/whitelist/' + id,
    method: 'get'
  })
}

// 新增停车场白名单
export function addWhitelist(data) {
  return request({
    url: '/parking/whitelist',
    method: 'post',
    data: data
  })
}

// 修改停车场白名单
export function updateWhitelist(data) {
  return request({
    url: '/parking/whitelist/renewal',
    method: 'put',
    data: data
  })
}

// 删除停车场白名单
export function delWhitelist(id) {
  return request({
    url: '/parking/whitelist/' + id,
    method: 'delete'
  })
}
