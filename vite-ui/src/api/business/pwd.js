import request from '@/utils/request'

// 查询秘钥本列表
export function listPwd(query) {
  return request({
    url: '/business/pwd/list',
    method: 'get',
    params: query
  })
}

// 查询秘钥本详细
export function getPwd(id) {
  return request({
    url: '/business/pwd/' + id,
    method: 'get'
  })
}

// 新增秘钥本
export function addPwd(data) {
  return request({
    url: '/business/pwd',
    method: 'post',
    data: data
  })
}

// 修改秘钥本
export function updatePwd(data) {
  return request({
    url: '/business/pwd',
    method: 'put',
    data: data
  })
}

// 删除秘钥本
export function delPwd(id) {
  return request({
    url: '/business/pwd/' + id,
    method: 'delete'
  })
}

// 导出秘钥本
export function exportPwd(query) {
  return request({
    url: '/business/pwd/export',
    method: 'get',
    params: query
  })
}