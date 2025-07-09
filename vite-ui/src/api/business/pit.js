import request from '@/utils/request'

// 查询填坑列表
export function listPit(query) {
  return request({
    url: '/business/pit/list',
    method: 'get',
    params: query
  })
}

// 查询填坑详细
export function getPit(id) {
  return request({
    url: '/business/pit/' + id,
    method: 'get'
  })
}

// 新增填坑
export function addPit(data) {
  return request({
    url: '/business/pit',
    method: 'post',
    data: data
  })
}

// 修改填坑
export function updatePit(data) {
  return request({
    url: '/business/pit',
    method: 'put',
    data: data
  })
}

// 删除填坑
export function delPit(id) {
  return request({
    url: '/business/pit/' + id,
    method: 'delete'
  })
}

// 导出填坑
export function exportPit(query) {
  return request({
    url: '/business/pit/export',
    method: 'get',
    params: query
  })
}