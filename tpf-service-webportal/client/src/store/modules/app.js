import * as io from 'socket.io-client'
import * as cookie from '@/utils/cookie'

const state = {
  socket: null,
  sidebar: {
    opened: true,
    withoutAnimation: false
  },
  device: 'desktop',
  size: 'medium',
  app: {},
  assigned: {},
  unassigned: {}
}

const mutations = {
  TOGGLE_SIDEBAR: state => {
    state.sidebar.opened = !state.sidebar.opened
    state.sidebar.withoutAnimation = false
    // if (state.sidebar.opened) {
    //   Cookies.set('sidebarStatus', 1)
    // } else {
    //   Cookies.set('sidebarStatus', 0)
    // }
  },
  CLOSE_SIDEBAR: (state, withoutAnimation) => {
    // Cookies.set('sidebarStatus', 0)
    state.sidebar.opened = false
    state.sidebar.withoutAnimation = withoutAnimation
  },
  TOGGLE_DEVICE: (state, device) => {
    state.device = device
  },
  SET_SIZE: (state, size) => {
    state.size = size
    // Cookies.set('size', size)
  }
}

const actions = {
  toggleSideBar({ commit }) {
    commit('TOGGLE_SIDEBAR')
  },
  closeSideBar({ commit }, { withoutAnimation }) {
    commit('CLOSE_SIDEBAR', withoutAnimation)
  },
  toggleDevice({ commit }, device) {
    commit('TOGGLE_DEVICE', device)
  },
  setSize({ commit }, size) {
    commit('SET_SIZE', size)
  },

  fnSocket: ({ dispatch, rootState }) => {
    state.socket = io.connect(`${process.env.VUE_APP_BASE_SOCKET_HOST}?token=${cookie.getToken()}`, { transports: ['websocket'] });

    state.socket.on('connect', () => { console.log("connect") });

    state.socket.on('disconnect', () => { console.log("disconnect") });

    state.socket.on('error', () => { console.log("error") });
    console.log(rootState)
    state.socket.on('message', ({ action, project, data, from, to }) => {
      console.log({ action, project, data, from, to })
      // action: "UPDATE"
      // data: {id: "5db93d1a6184040001884f5c", project: "dataentry", uuid: "dataentry_5db93d179c41850001fbf952",…}
      // from: ""
      // project: "dataentry"
      // to: ""
      const departments = {
        data_entry: { a: 'MomoDataentyAss', u: 'MomoDataentyUnAss' },
        document_check: { a: 'MomoDocumentCheckAss', u: 'MomoDocumentCheckUnAss' },
        loan_booking: { a: 'MomoLoanBookingAss', u: 'MomoLoanBookingUnAss' }
      }

      const model = {
        momo: { departments: departments }
      }
      
      switch (action) {
        case 'CREATE':
          dispatch('fnFilterCreate', { model: model[project].departments[to].u, data })
          break
        case 'UPDATE':
          if (data.assigned
            && (data.assigned === rootState.user_state.userSession.user_name || rootState.user_state.userSession.authorities[0] !== 'role_user')) {
            dispatch('fnFilterCreate', { model: model[project].departments[to].a, data })
          } else dispatch('fnFilterDelete', { model: model[project].departments[to].a, data })

          if (!data.assigned) dispatch('fnFilterCreate', { model: model[project].departments[to].u, data })
          else dispatch('fnFilterDelete', { model: model[project].departments[to].u, data })
          break
        case 'DELETE':


          dispatch('fnFilterDelete', { model: model[project].departments[from].a, data })
          dispatch('fnFilterDelete', { model: model[project].departments[from].u, data })
          break
      }
    })
  },

  fnFilterCreate: async ({ dispatch, rootState }, { model, data }) => {
    let page = await dispatch('fnRootState', model, { root: true })
    console.log(page,'fnFilterCreate');
    
    const idx = rootState[page][model].list.findIndex(e => e.id === data.id)
    console.log(rootState[page][model].list);
    
    if (idx !== -1) Vue.set(rootState[page][model].list, idx, { ...rootState[page][model].list[idx], ...data })
    else {
      rootState[page][model].list.unshift(data)
      rootState[page][model].total += 1
      if (rootState[page][model].list.length > rootState[page][model].rowsPerPage) rootState[page][model].list.pop()
    }
  },

  fnFilterUpdate: async ({ dispatch, rootState }, { model, data }) => {
    let page = await dispatch('fnRootState', model, { root: true })

    const idx = rootState[page][model].list.findIndex(e => e.id === data.id)
    if (idx !== -1) Vue.set(rootState[page][model].list, idx, { ...rootState[page][model].list[idx], ...data })
  },

  fnFilterDelete: async ({ dispatch, rootState }, { model, data }) => {
    let page = await dispatch('fnRootState', model, { root: true })

    rootState[page][model].list = rootState[page][model].list.filter(e => e.id !== data.id)
    rootState[page][model].total += -1
    if (rootState[page][model].list.length === 0) dispatch('fnRead', model)
  },

  fnLoadData: async ({ dispatch, rootState }, model) => {
    let page = await dispatch('fnRootState', model, { root: true })
    switch (model) {
      case 'Client':
        if (rootState[page][model].isCreate) {
          rootState[page][model].obj = {
            ...rootState[page][model].obj, authorizedGrantTypes: 'client_credentials',
            accessTokenValidity: 2592000, refreshTokenValidity: 15552000
          }
        } else {
          rootState[page][model].obj = { ...rootState[page][model].obj, clientSecret: rootState[page][model].obj.secret }
        }
        break
    }
  },
  
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
