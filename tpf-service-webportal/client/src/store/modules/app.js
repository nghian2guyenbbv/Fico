import * as io from 'socket.io-client'
import * as cookie from '@/utils/cookie'
import Vue from 'vue'

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

  fnRootState: ({ rootState }, keyState) => {
    for (let key in rootState) {
        if (rootState[key][keyState] !== undefined) {
            return key
        }
    }
  },

  fnSocket ({ dispatch, rootState }) {
    state.socket = io.connect(`${process.env.VUE_APP_BASE_SOCKET_HOST}?token=${cookie.getToken()}`, { transports: ['websocket'] });

    state.socket.on('connect', () => { console.log("connect") });

    state.socket.on('disconnect', () => { console.log("disconnect") });

    state.socket.on('error', () => { console.log("error") });

    state.socket.on('message', ({ action, project, data, from, to }) => {
      const departments = {
        data_entry: { a: 'MomoDataentyAss', u: 'MomoDataentyUnAss' },
        document_check: { a: 'MomoDocumentCheckAss', u: 'MomoDocumentCheckUnAss' },
        loan_booking: { a: 'MomoLoanBookingAss', u: 'MomoLoanBookingUnAss' },
        "": { a: "quicklead", u: "quicklead" }
      }
      const model = {
        momo: { departments: departments },
        dataentry: { departments: departments }
      }
      
      switch (action) {
        case 'CREATE':
          dispatch('fnFilterCreate', { model: model[project].departments[to].u, data })
          break
        case 'UPDATE':
          if (data.assigned
            && (data.assigned === rootState.user.user_name
              || rootState.user.authorities[0] !== 'role_user')) 
          {
            dispatch('fnFilterCreate', { model: model[project].departments[to].a, data })
          } else {
            dispatch('fnFilterDelete', { model: model[project].departments[to].a, data })
          }

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
    let page = await dispatch('fnRootState', model)
    // console.log(page,'fnFilterCreate');
    console.log(model)
    dispatch('dataentry/pushNewQuicklead', data, { root: true })
    // const idx = rootState[page][model].list.findIndex(e => e.id === data.id)
    
    
    // if (idx !== -1) Vue.set(rootState[page][model].list, idx, { ...rootState[page][model].list[idx], ...data })
    // else {
    //   if (page == 'dataentry') {
    //     dispatch('dataentry/pushNewQuicklead', data, { root: true })
    //     // let array = rootState[page][model].list
    //     // console.log(array.length)
    //     // Vue.set(rootState[page][model].list, array.length, data)
    //     // console.log(rootState[page][model].list);
    //   } else {
    //     rootState[page][model].list.unshift(data)
    //   }
    //   rootState[page][model].total = (parseInt(rootState[page][model].total) + 1).toString()
    //   if (rootState[page][model].list.length > rootState[page][model].pagination.limit) rootState[page][model].list.pop()
    // }
  },

  // fnFilterUpdate: async ({ dispatch, rootState }, { model, data }) => {
  //   let page = await dispatch('fnRootState', model)

  //   const idx = rootState[page][model].list.findIndex(e => e.id === data.id)
  //   if (idx !== -1) Vue.set(rootState[page][model].list, idx, { ...rootState[page][model].list[idx], ...data })
  // },

  fnFilterDelete: async ({ dispatch, rootState }, { model, data }) => {
    let page = await dispatch('fnRootState', model)

    rootState[page][model].list = rootState[page][model].list.filter(e => e.id !== data.id)
    rootState[page][model].total += -1
    if (rootState[page][model].list.length === 0) dispatch('fnRead', model)
  },

  // fnLoadData: async ({ dispatch, rootState }, model) => {
  //   let page = await dispatch('fnRootState', model)
  //   switch (model) {
  //     case 'Client':
  //       if (rootState[page][model].isCreate) {
  //         rootState[page][model].obj = {
  //           ...rootState[page][model].obj, authorizedGrantTypes: 'client_credentials',
  //           accessTokenValidity: 2592000, refreshTokenValidity: 15552000
  //         }
  //       } else {
  //         rootState[page][model].obj = { ...rootState[page][model].obj, clientSecret: rootState[page][model].obj.secret }
  //       }
  //       break
  //   }
  // },
  
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
