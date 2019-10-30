import Vue from 'vue'
import * as dataentry from '@/api/dataentry'

const state = {
  newCustomer: {
    requestID: '123456',
    customerName: '',
    customerId: '',
    dsaCode: '',
    bankCardNumber: '',
    currentAddress: '',
    areaId: ''
  },
  quickLeadData: {
    quickLeadId: '',
    documents: [],
    productTypeCode: 'Personal Finance',
    customerType: 'Individual',
    productCode: 'UNSECURED CASHLOAN',
    loanAmountRequested: '10000000',
    firstName: '',
    lastName: '',
    city: '',
    sourcingChannel: 'DIRECT',
    dateOfBirth: '01/01/1990',
    sourcingBranch: '',
    natureOfOccupation: 'Others',
    schemeCode: '',
    comment: 'dummy',
    preferredModeOfCommunication: 'Web-Portal Comments',
    leadStatus: 'Converted',
    communicationTranscript: 'dummy',
    identificationNumber: ''
  }
}

const mutations = {
}

const actions = {
  clearDataState({commit, dispatch}) {
    state.newCustomer = {
      requestID: '123456',
      customerName: '',
      customerId: '',
      dsaCode: '',
      bankCardNumber: '',
      currentAddress: '',
      areaId: ''
    }
    state.quickLeadData = {
      quickLeadId: '',
      documents: [],
      productTypeCode: 'Personal Finance',
      customerType: 'Individual',
      productCode: 'UNSECURED CASHLOAN',
      loanAmountRequested: '10000000',
      firstName: '',
      lastName: '',
      city: '',
      sourcingChannel: 'DIRECT',
      dateOfBirth: '01/01/1990',
      sourcingBranch: '',
      natureOfOccupation: 'Others',
      schemeCode: '',
      comment: 'dummy',
      preferredModeOfCommunication: 'Web-Portal Comments',
      leadStatus: 'Converted',
      communicationTranscript: 'dummy',
      identificationNumber: ''
    }
  },

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

  uploadFiles({ commit, dispatch }, data) {
    return new Promise((resolve, reject) => {
      dataentry.uploadFiles(data)
        .then(response => {
          resolve(response)
        }).catch(error => {
          reject(error)
        })
    })
  },

  createQuicklead({ commit, dispatch }) {
    return new Promise((resolve, reject) => {
      dataentry.createQuicklead(state.quickLeadData)
        .then(response => {
          resolve(response)
        }).catch(error => {
          reject(error)
        })
    })
  },

  getAriaCode({ commit, dispatch }) {
    return new Promise((resolve, reject) => {
      dataentry.getAriaCode()
        .then(response => {
          resolve(response)
        }).catch(error => {
          reject(error)
        })
    })
  },

  getBranch({ commit, dispatch }) {
    return new Promise((resolve, reject) => {
      dataentry.getBranch()
        .then(response => {
          resolve(response)
        }).catch(error => {
          reject(error)
        })
    })
  },

  retryQuickLead({ commit, dispatch }, data) {
    return new Promise((resolve, reject) => {
      dataentry.retryQuickLead(data)
        .then(response => {
          resolve(response)
        }).catch(error => {
          reject(error)
        })
    })
  },

  updateStatusManualy({ commit, dispatch }, data) {
    return new Promise((resolve, reject) => {
      dataentry.updateStatusManualy(data)
        .then(response => {
          resolve(response)
        }).catch(error => {
          reject(error)
        })
    })
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
