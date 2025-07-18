import request from '@/utils/request'

// 查询博客列表
export function listBlog(query) {
  return request({
    url: '/salish/blog/list',
    method: 'get',
    params: query
  })
}

// 查询博客列表
export function listBlogInfo(query) {
  return request({
    url: '/salish/blog/listInfo',
    method: 'get',
    params: query
  })
}

// 归档
export function interfileBlog(query) {
  return request({
    url: '/salish/blog/interfile',
    method: 'get',
    params: query
  })
}

// 查询博客详细
export function getBlog(id) {
  return request({
    url: '/salish/blog/' + id,
    method: 'get'
  })
}

// 查询标签列表
export function listTag(query) {
  return request({
    url: '/salish/tag/list',
    method: 'get',
    params: query
  })
}

// 查询标签详细
export function getTag(id) {
  return request({
    url: '/salish/tag/' + id,
    method: 'get'
  })
}

// 坑标签列表
export function getPitTags() {
  return request({
    url: '/salish/pit/tag',
    method: 'get'
  })
}

// 查询标签详细
export function listPits(query) {
  return request({
    url: '/salish/pit/pits' ,
    method: 'get',
    params: query
  })
}

// 根据字典类型查询字典数据信息
export function getDictData(dictType) {
  return request({
    url: '/salish/dict/data/type/' + dictType,
    method: 'get'
  })
}

// 根据字典类型查询字典数据信息
export function blogIntroduction() {
  return request({
    url: '/salish/blogIntroduction',
    method: 'get'
  })
}
