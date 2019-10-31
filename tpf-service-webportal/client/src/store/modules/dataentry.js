import Vue from 'vue'
import * as dataentry from '@/api/dataentry'

const state = {
  list: [],
  total: 0,
  obj: {},
  _search: {},
  _sort: '',
  _select: '',
  selected: [],
  isLoading: false,
  pagination: {
    page: 1,
    limit: 10,
    sort: 'createdAt,asc',
    project: 'dataentry'
  },
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
  PUSH_NEW_QUICKLEAD: (state, data) => {
    let array = state.list
    Vue.set(state.list, array.length, data)
    state.total = parseInt(state.total) + 1
    if (state.list.length > state.pagination.limit) state.list.pop()
  },
  UPDATE_QUICKLEAD: (state, data) => {
    let array = state.list
    Vue.set(state.list, data.idx, { ...state.list[data.idx], ...data.data })
  },
  DELETE_QUICKLEAD: (state, data) => {
    state.list = state.list.filter(e => e.id !== data.id)
    state.total = parseInt(state.total) - 1
  }
}

const actions = {
  pushNewQuicklead({commit, dispatch}, quicklead) {
    const idx = state.list.findIndex(e => e.id === quicklead.id)
    if (idx !== -1) {
      commit('UPDATE_QUICKLEAD', { idx: idx, data: quicklead })
    } else {
      commit('PUSH_NEW_QUICKLEAD', quicklead)
    }
  },

  deleteQuicklead({commit, dispatch}, quicklead) {
    commit('DELETE_QUICKLEAD', quicklead)
    if (state.list.length === 0) dispatch('getQuickList')
  },

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

  getQuickList({ commit, dispatch }) {
    return new Promise((resolve, reject) => {
      state.isLoading = true
      if (process.env.VUE_APP_ENV_API == 'off') {
        let response = require('@/store/data')
        state.isLoading = false
        resolve(response)
      } else {
        dataentry.apiGetApp(state.pagination)
          .then(response => {
            state.isLoading = false
            state.list = response.data
            state.total = response.total
            resolve(response)
          }).catch(error => {
            state.isLoading = false
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
