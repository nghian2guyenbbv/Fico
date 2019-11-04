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

export function fnFixmanualy(appid, method) {
  return request({
    url: `/app/${appid}/PROCESSING_FIX?access_key=access_key_db`,
    method: method
  })
}