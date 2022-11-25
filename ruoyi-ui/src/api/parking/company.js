import request from '@/utils/request'

// 查询公司基本信息列表
export function listCompany(query) {
  return request({
    url: '/parking/company/list',
    method: 'get',
    params: query
  })
}

// 查询公司基本信息详细
export function getCompany(id) {
  return request({
    url: '/parking/company/' + id,
    method: 'get'
  })
}

// 新增公司基本信息
export function addCompany(data) {
  return request({
    url: '/parking/company',
    method: 'post',
    data: data
  })
}

// 修改公司基本信息
export function updateCompany(data) {
  return request({
    url: '/parking/company',
    method: 'put',
    data: data
  })
}

// 删除公司基本信息
export function delCompany(id) {
  return request({
    url: '/parking/company/' + id,
    method: 'delete'
  })
}
