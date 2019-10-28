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

export function apiGetDocsCheme() {
  let data = {
    "request_id": "d062b2f8-dc3c-4366-afbe-87aadae8818e",
    "date_time": "1565667082",
    "data": {
        "search_value": ""
    }
  }
  return request({
    url: 'dataentry/getproductbyname',
    method: 'post',
    data
  })
}

export function apiFirstCheck(customerInfor) {
  let data = {
    request_id: "d062b2f8-dc3c-4366-afbe-87aadae8818e",
    date_time: "1565667082",
    data: customerInfor
  }
  return request({
    url: 'dataentry/firstcheck',
    method: 'post',
    data
  })
}
