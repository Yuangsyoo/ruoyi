import request from '@/utils/request'

// 查询停车记录列表
export function listRecord(query) {
  return request({
    url: '/parking/record/list',
    method: 'get',
    params: query
  })
}
// 查询停车记录列表
export function listRecord1(query) {
  return request({
    url: '/parking/record/list1',
    method: 'get',
    params: query
  })
}
// 查询停车记录详细
export function getRecord(id) {
  return request({
    url: '/parking/record' + id,
    method: 'get'
  })
}
// 查询停车记录详细
export function getRecord1(id) {
  return request({
    url: '/parking/record/moneyOne/' + id,
    method: 'get'
  })
}
// 查询停车记录详细
export function getRecord2(id) {
  return request({
    url: '/parking/record/moneyOne2/' + id,
    method: 'get'
  })
}
// 新增停车记录
export function addRecord(data) {
  return request({
    url: '/parking/record',
    method: 'post',
    data: data
  })
}

// 修改停车记录
export function updateRecord(data) {
  return request({
    url: '/parking/record',
    method: 'put',
    data: data
  })
}

// 删除停车记录
export function delRecord(id) {
  return request({
    url: '/parking/record/' + id,
    method: 'delete'
  })
}
// 查询最近缴费记录详细
export function getPayRecord(id) {
  return request({
    url: '/parking/record/getPayRecord/' + id,
    method: 'get'
  })
}
export function getPayRecord1(id,id1) {
  return request({
    url: '/parking/record/getPayRecord1?parkingLotInformationId='+id+"&parkingeqid="+id1,
    method: 'get',

  })
}
//修改停车记录（现金）
export function updateToRecord(id) {
  return request({
    url: '/parking/record/updateToRecord/'+id,
    method: 'get',
  })
}
//修改停车记录（现金）
export function updateToRecord1(id) {
  return request({
    url: '/parking/record/updateToRecordOne/'+id,
    method: 'get',
  })
}
//修改停车记录(优惠卷)
export function updateToRecordFromCoupon(id) {
  return request({
    url: '/parking/record/updateToRecordFromCoupon/'+id,
    method: 'get',
  })
}

//修改停车记录(优惠卷)
export function updateToRecordFromCoupon1(id) {
  return request({
    url: '/parking/record/updateToRecordFromCouponOne/'+id,
    method: 'get',
  })
}
//手动刷新逃费车辆
export function refreshstate(id) {
  return request({
    url: '/parking/record/refreshstate/' + id,
    method: 'get'
  })
}
