import Vue from 'vue'
import * as momo from '@/api/momo'
import store from '@/store'

export const pagination = {
  _page: 1,
  rowsPerPage: 10,
  descending: true,
  sortBy: 'createdAt'
}

let opt = {
  total: 0,
  list: [],
  obj: {},
  _search: {},
  _sort: '',
  _select: '',
  selected: [],
  pagination,
  ...pagination,
  pages: [10, 20, 50, 100],
  isLoading: false
}

const state = {
  MomoDataentyAss: {
    ...opt
  },
  MomoDataentyUnAss: {
    ...opt
  },
  MomoDocumentCheckAss: {
    ...opt
  },
  MomoDocumentCheckUnAss: {
    ...opt
  },
  MomoLoanBookingUnAss: {
    ...opt
  },
  MomoLoanBookingAss: {
    ...opt
  },
  MomoStatus: {
    ...opt
  },
  ACCA: {
    ...opt,
    _id: ''
  }
}

const mutations = {
}

const actions = {
  fnCallListView({ commit, dispatch }, model) {
    return new Promise((resolve, reject) => {
      if (process.env.VUE_APP_ENV_API == 'off') {
        let response = require('@/store/data')
        resolve(response)
      } else {
        this.state.momo[model].isLoading = true
        const { rowsPerPage, _page, _sort, _search, _select } = this.state.momo[model]
        var params = {
          page: _page,
          limit: rowsPerPage
        }
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
            reject(error)
          })
      }
    })
  },

  fnUpdata({ commit, dispatch }, model) {
    return new Promise((resolve, reject) => {
      if (process.env.VUE_APP_ENV_API == 'off') {
        let response = require('@/store/data')
        resolve(response)
      } else {
        this.state.momo[model].isLoading = true
        momo.apiMomoAss(this.state.momo[model].obj.id, this.state.momo[model].obj, 'put')
          .then(success => {
            this.state.momo[model].isLoading = false
            resolve(success.data)
          }).catch(error => {
            reject(error)
          })
      }
    }
    );
  },

  fnACCA({ commit, dispatch }, model) {
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
              console.log(this.state.momo[model].obj);
            }
            this.state.momo[model].isLoading = false
            resolve(success.data)
          }).catch(error => {
            reject(error)
          })
      }
    })
  }

}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
