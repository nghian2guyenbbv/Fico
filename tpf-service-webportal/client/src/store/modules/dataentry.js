import Vue from 'vue'
import * as dataentry from '@/api/dataentry'

const state = {
  list: [],
  newCustomer: {
    requestID: '123456',
    customerName: '',
    customerId: '',
    dsaCode: '',
    bankCardNumber: '',
    currentAddress: '',
    areaId: '100001'
  }
}

const mutations = {
}

const actions = {
  getQuickList({ commit, dispatch }, params) {
    return new Promise((resolve, reject) => {
      if (process.env.VUE_APP_ENV_API == 'off') {
        let response = require('@/store/data')
        resolve(response)
      } else {
        dataentry.apiGetApp(params)
        .then(response => {
          resolve(response)
        }).catch(error => {
          reject(error)
        })
      }
      
    })
  },

  getDocsCheme({ commit, dispatch }, params) {
    return new Promise((resolve, reject) => {
      if (process.env.VUE_APP_ENV_API == 'off') {
        let response = []
        resolve(response)
      } else {
        dataentry.apiGetDocsCheme()
        .then(response => {
          resolve(response)
        }).catch(error => {
          reject(error)
        })
      }
      
    })
  },

  postFirstCheck({ commit, dispatch }, data) {
    return new Promise((resolve, reject) => {
      if (process.env.VUE_APP_ENV_API == 'off') {
        let response = []
        resolve(response)
      } else {
        dataentry.apiFirstCheck(data)
        .then(response => {
          resolve(response)
        }).catch(error => {
          reject(error)
        })
      }
      
    })
  },
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
