import request from '@/utils/request'

// 查询数据库备份列表
export function listBackup(query) {
  return request({
    url: '/business/backup/list',
    method: 'get',
    params: query
  })
}

// 查询数据库备份详细
export function getBackup(id) {
  return request({
    url: '/business/backup/' + id,
    method: 'get'
  })
}

// 新增数据库备份
export function addBackup(data) {
  return request({
    url: '/business/backup',
    method: 'post',
    data: data
  })
}

// 修改数据库备份
export function updateBackup(data) {
  return request({
    url: '/business/backup',
    method: 'put',
    data: data
  })
}

// 立即数据库备份
export function runBackup(data) {
  return request({
    url: '/business/backup/run',
    method: 'put',
    data: data
  })
}

// 删除数据库备份
export function delBackup(id) {
  return request({
    url: '/business/backup/' + id,
    method: 'delete'
  })
}

// 导出数据库备份
export function exportBackup(query) {
  return request({
    url: '/business/backup/export',
    method: 'get',
    params: query
  })
}
