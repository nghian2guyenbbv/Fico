import request from '@/utils/request'

export function apiLogin(data) {
  return request({
    url: 'login',
    method: 'post',
    data
  })
}

export function apiGetInfo() {
  return request({
    url: 'me',
    method: 'get'
  })
}

export function apiLogout() {
  return request({
    url: 'logout',
    method: 'get'
  })
}

export function apiUsers() {
  return request({
    url: 'account',
    method: 'get'
  })
}

export function createUser(data) {
  return request({
    url: 'account',
    method: 'post',
    data: data
  })
}

export function updateUser(userId, data) {
  return request({
    url: 'account/' + userId,
    method: 'put',
    data: data
  })
}
