

const state = {
  table: {
    url: 'app',
    params: {},
    _list: [],
    _total: 0,
    _detail: {},
    _tempt: {},
    _page: 1,
    _rowsPerPage: 10,
    _loading: false,
    _search: {},
    _sortBy: 'createdAt',
    _isCreate: false,
    _isUpdate: false,
    _isDelete: false
  }
}

const mutations = {
}

const actions = {
  getDataPage({ }, params) {
    
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
