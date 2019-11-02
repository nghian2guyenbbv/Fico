import request from '@/utils/request'

export function apiMomo(params, method) {
  return request({
    url: '/app',
    method: method,
    params
  })
}

export function apiMomoAss(id, data, method) {
  return request({
    url: `/app/${id}`,
    method: method,
    data
  })
}

export function apiMomoACCA(id, method) {
  return request({
    url: `/app/${id}`,
    method: method
  })
}
