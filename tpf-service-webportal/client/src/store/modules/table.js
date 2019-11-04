import * as table from '@/api/table'

const state = {
  url: 'app',
  params: {},
  _list: [],
  _total: 0,
  _detail: {},
  _tempt: {},
  _loading: false,
  _search: {},
  _page: 1,
  _rowsPerPage: 10,
  _sortBy: 'createdAt,asc',
  _project: 'dataentry',
  _pageSize: [10, 15, 20, 100],
  _isCreate: false,
  _isUpdate: false,
  _isDelete: false
}

const mutations = {
}

const actions = {
  getDataTable({ commit, dispatch }, value) {
    return new Promise((resolve, reject) => {
      state._page = value && value._page ? value._page : state._page
      state._rowsPerPage = value && value._rowsPerPage ? value._rowsPerPage : state._rowsPerPage
      state._sortBy = value && value._sortBy ? value._sortBy : state._sortBy
      state._project = value && value._project ? value._project : state._project
      let params = {
        page: state._page,
        limit: state._rowsPerPage,
        sort: state._sortBy,
        project: state._project
      }
      state._loading = true
      table.apiList(params).then(response => {
        state._loading = false
        state._list = response.data
        state._total = response.total
        resolve(response)
      })
      .catch(error => {
        state._loading = false
        reject(error)
      })
    })
  },
  
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
