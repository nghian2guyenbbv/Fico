import axios from 'axios'
const axios = axios.create({
  baseURL: process.env.VUE_APP_BASE_API_REPAYMENT,
  timeout: 5000
})
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
