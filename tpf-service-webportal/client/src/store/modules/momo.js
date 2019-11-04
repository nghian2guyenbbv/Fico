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
        const { _rowsPerPage, _page, _sort, _search, _select } = this.state.momo[model]
        var params = {}
        if (_page) params = { ...params, page: _page }
        if (_rowsPerPage) params = { ...params, limit: _rowsPerPage }
        if (_select) params = { ...params, select: _select }
        if (_sort) params = { ...params, sort: _select }
        for (const i in _search) params = { ...params, [i]: _search[i] }
        momo.apiMomo(params, 'get')
          .then(success => {
            this.state.momo[model].total = 0
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

  fnUpdata({ dispatch }, model) {
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

  fnFixmanualy({ dispatch }, model) {
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
            reject(error)
          })
      }
    })
  },

  fnFilterCreate: async ({ dispatch }, { model, data }) => {
    const idx = this.state.momo[model].list.findIndex(e => e.id === data.id)
    if (idx !== -1) Vue.set(this.state.momo[model].list, idx, { ...this.state.momo[model].list[idx], ...data })// update
    else {
      this.state.momo[model].list.unshift(data)
      this.state.momo[model].total += 1
      if (this.state.momo[model].list.length > this.state.momo[model].rowsPerPage) this.state.momo[model].list.pop()
    }
  },

  fnFilterUpdate: async ({ dispatch }, { model, data }) => {
    const idx = this.state.momo[model].list.findIndex(e => e.id === data.id)
    if (idx !== -1) Vue.set(this.state.momo[model].list, idx, { ...this.state.momo[model].list[idx], ...data })
  },

  fnFilterDelete: async ({ dispatch }, { model, data }) => {
    this.state.momo[model].list = this.state.momo[model].list.filter(e => e.id !== data.id)
    this.state.momo[model].total += -1
    if (this.state.momo[model].list.length === 0) dispatch('fnCallListView', model)
  },

}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
