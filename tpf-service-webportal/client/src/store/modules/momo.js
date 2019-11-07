import Vue from 'vue'
import * as momo from '@/api/momo'
import store from '@/store'


let opt = {
  total: 0,
  list: [],
  obj: {},
  _search: {},
  _sort: '',
  _select: '',
  selected: [],
  _page: 1,
  rowsPerPage: 10,
  isLoading: false
}

const state = {
  MomoDataentyAss: {
    ...opt,
    load: false
  },
  MomoDataentyUnAss: {
    ...opt,
    load: false
  },
  MomoDocumentCheckAss: {
    ...opt,
    load: false
  },
  MomoDocumentCheckUnAss: {
    ...opt,
    load: false
  },
  MomoLoanBookingUnAss: {
    ...opt,
    load: false
  },
  MomoLoanBookingAss: {
    ...opt,
    load: false
  },
  MomoStatus: {
    ...opt
  },
  ACCA: {
    ...opt,
    _id: ''
  },
  Documents: {
    show: false,
    items: [],
    disabledDown: false
  }
}

const mutations = {
}

const actions = {
  fnCallListView({ dispatch }, model) {
    return new Promise((resolve, reject) => {
      if (process.env.VUE_APP_ENV_API == 'off') {
        let response = require('@/store/data')
        resolve(response)
      } else {
        this.state.momo[model].isLoading = true
        const { rowsPerPage, _page, _sort, _search, _select } = this.state.momo[model]
        var params = {}
        if (_page) params = { ...params, page: _page }
        if (rowsPerPage) params = { ...params, limit: rowsPerPage }
        if (_select) params = { ...params, select: _select }
        if (_sort) params = { ...params, sort: _select }
        for (const i in _search) params = { ...params, [i]: _search[i] }
        momo.apiMomo(params, 'get')
          .then(success => {
            if (Array.isArray(success.data)) {
              this.state.momo[model].list = success.data
              this.state.momo[model].total = success.total || 0
            } else {
              this.state.momo[model].obj = success.data
            }
            this.state.momo[model].isLoading = false
            resolve(success.data)
          }).catch(error => {
            this.state.momo[model].isLoading = false
            reject(error)
          })
      }
    })
  },

  fnUpdata({ dispatch }, model) {
    return new Promise((resolve, reject) => {
      if (process.env.VUE_APP_ENV_API == 'off') {
        let response = require('@/store/data')
        resolve(response)
      } else {
        momo.apiMomoAss(this.state.momo[model].obj.id, this.state.momo[model].obj, 'put')
          .then(success => {
            resolve(success.data)
          }).catch(error => {
            this.state.momo[model].isLoading = false
            reject(error)
          })
      }
    }
    );
  },

  fnFixmanualy({ dispatch }, appid) {
    return new Promise((resolve, reject) => {
      if (process.env.VUE_APP_ENV_API == 'off') {
        let response = require('@/store/data')
        resolve(response)
      } else {
        momo.fnFixmanualy(appid, 'post')
          .then(success => {
            resolve(success.data)
          }).catch(error => {
            reject(error)
          })
      }
    }
    );
  },

  fnRetry({ dispatch }, appid) {
    return new Promise((resolve, reject) => {
      if (process.env.VUE_APP_ENV_API == 'off') {
        let response = require('@/store/data')
        resolve(response)
      } else {
        // this.state.momo[model].isLoading = true
        momo.fnFixmanualy(appid, 'put')
          .then(success => {
            // this.state.momo[model].isLoading = false
            console.log(success.data);
            
            resolve(success.data)
          }).catch(error => {
            reject(error)
          })
      }
    }
    );
  },

  fnACCA({  dispatch }, model) {
    return new Promise((resolve, reject) => {
      if (process.env.VUE_APP_ENV_API == 'off') {
        let response = require('@/store/data')
        resolve(response)
      } else {
        this.state.momo[model].isLoading = true
        momo.apiMomoACCA(this.state.momo[model]._id, 'get')
          .then(success => {
            if (Array.isArray(success.data)) {
              this.state.momo[model].list = success.data
              this.state.momo[model].total = success.total || 0
            } else {
              this.state.momo[model].obj = success.data
            }
            this.state.momo[model].isLoading = false
            resolve(success.data)
          }).catch(error => {
            this.state.momo[model].isLoading = false
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
