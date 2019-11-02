import * as io from 'socket.io-client'
import * as cookie from '@/utils/cookie'
import Vue from 'vue'
import { pushNotify } from '@/utils/notifications'

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
    
    let userInfor = cookie.getInforUser()

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
      pushNotify('TPF_HOME NOTIFICATION', project + ' - ' + action)
      switch (action) {
        case 'CREATE':
          if (project == 'dataentry') {
            dispatch('dataentry/pushNewQuicklead', data)
          } else {
            dispatch('fnFilterCreate', { model: model[project].departments[to].u, data })
          }
          break
        case 'UPDATE':
          if (data.assigned && (data.assigned === userInfor.user_name || userInfor.authorities[0] !== 'role_user')) 
          {
            if (project == 'dataentry') {
              dispatch('dataentry/pushNewQuicklead', data)
            } else {
              dispatch('fnFilterCreate', { model: model[project].departments[to].a, data })
              dispatch('fnFilterDelete', { model: model[project].departments[to].u, data })
            }
          } else {
            if (project == 'dataentry') {
              dispatch('dataentry/deleteQuicklead', data)
            } else {
              dispatch('fnFilterDelete', { model: model[project].departments[to].a, data })
              dispatch('fnFilterCreate', { model: model[project].departments[to].u, data })
            }
          }
          if (project != 'dataentry') {
            if (!data.assigned) dispatch('fnFilterCreate', { model: model[project].departments[to].u, data })
            else dispatch('fnFilterDelete', { model: model[project].departments[to].u, data })
          }
          break
        case 'DELETE':
          if (project == 'dataentry') {
            dispatch('dataentry/deleteQuicklead', data)
          } else {
            dispatch('fnFilterDelete', { model: model[project].departments[from].a, data })
            dispatch('fnFilterDelete', { model: model[project].departments[from].u, data })
          }
          
          break
      }
    })
  },

  fnFilterCreate: async ({ dispatch, rootState }, { model, data }) => {
    let page = await dispatch('fnRootState', model)
    
    const idx = rootState[page][model].list.findIndex(e => e.id === data.id)
			
    if (idx !== -1) Vue.set(rootState[page][model].list, idx, { ...rootState[page][model].list[idx], ...data })
    else {
      rootState[page][model].list.unshift(data)
      rootState[page][model].total += 1
      if (rootState[page][model].list.length > rootState[page][model].rowsPerPage) rootState[page][model].list.pop()
    }
  },

  fnFilterDelete: async ({ dispatch, rootState }, { model, data }) => {
    let page = await dispatch('fnRootState', model)

    rootState[page][model].list = rootState[page][model].list.filter(e => e.id !== data.id)
    rootState[page][model].total += -1
    if (rootState[page][model].list.length === 0) {
      
    }
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
