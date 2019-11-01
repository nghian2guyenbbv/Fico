import request from '@/utils/request'
import moment from 'moment';
const uuidv1 = require('uuid/v1');
export function apiGetApp(params) {
  return request({
    url: 'app',
    method: 'get',
    params
  })
}

export function apiGetDocsCheme() {
  let data = {
    "request_id": uuidv1(),
    "date_time": moment().format(),
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
    request_id: uuidv1(),
    date_time: moment().format(),
    data: customerInfor
  }
  return request({
    url: 'dataentry/firstcheck',
    method: 'post',
    data
  })
}

export function uploadFiles(files) {
  return request({
    url: 'dataentry/uploadfile',
    method: 'post',
    data: files
  })
}

export function createQuicklead(quicklead) {
  let body = {
    request_id: uuidv1(),
    date_time: moment().format(),
    data: quicklead
  }
  return request({
    url: 'dataentry/quicklead',
    method: 'post',
    data: body
  })
}

export function getAriaCode() {
  let data = {
    request_id: uuidv1(),
    date_time: moment().format()
  }
  
  return request({
    url: 'dataentry/getaddress',
    method: 'post',
    data
  })
}

export function getBranch() {
  let data = {
    request_id: uuidv1(),
    date_time: moment().format()
  }
  
  return request({
    url: 'dataentry/getbranch',
    method: 'post',
    data
  })
}

export function retryQuickLead(quickleadId) {
  let data = {
    request_id: uuidv1(),
    date_time: moment().format(),
    data: {
      quickLeadId: quickleadId,
      retry: "YES"
    }
  }
  
  return request({
    url: 'dataentry/quicklead',
    method: 'post',
    data
  })
}

export function updateStatusManualy(appId) {
  let a = {
    request_id: uuidv1(),
    date_time: moment().format(),
    data: {
      applicationId: appId,
			status:"MANUALLY",
			description: null
    }
  }
  

  return request({
    url: 'dataentry/updatestatus',
    method: 'post',
    data: {
      request_id: "d062b2f8-dc3c-4366-afbe-87aadae8818e",
      date_time: "1565667082",
      data: {
        applicationId: appId,
        status:"MANUALLY",
        description: null
      }
    }
  })
}