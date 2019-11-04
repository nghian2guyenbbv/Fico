import Vue from 'vue'
import { apiLogin, apiLogout, apiGetInfo, apiUsers, createUser, updateUser } from '@/api/user'
import * as cookie from '@/utils/cookie'
import router, { resetRouter } from '@/router'

const state = {
  token: cookie.getToken(),
  avatar: '',
  active: false,
  exp: 0,
  user_name: '',
  roles: [],
  authorities: [],
  client_id: '',
  scope: [],
  departments: [],
  projects: []
}

const mutations = {
  // SET_TOKEN: (state, token) => {
  //   state.token = token
  // },
  SET_INFOR_USER: (state, value) => {
    Object.assign(state, value)
  },
  SET_ROLES_USER: (state, roles) => {
    Vue.set(state, 'roles', [...roles])
  }
}

const actions = {
  // user login
  login({ commit, dispatch }, userInfo) {
    const { username, password } = userInfo
    return new Promise((resolve, reject) => {
      // this.$store.dispatch('app/fnSocket', null)
      if (process.env.VUE_APP_ENV_API == 'off') {
        let response = {
          access_token: 'abc1234',
          expires_in: 1000000
        }
        cookie.setToken(response.access_token, response.expires_in)
        dispatch('getInfo').then((data) => {
            resolve(data)
        })
      } else {
        apiLogin({ username: username.trim(), password: password })
        .then(res => {
          localStorage.clear()
          let response = res.data
          cookie.setToken(response.access_token, response.expires_in)
          dispatch('getInfo').then((data) => {
            resolve(data)
          })
        }).catch(error => {
          reject(error)
        })
      }
      
    })
  },

  // get user info
  getInfo({ commit, state }) {
    return new Promise((resolve, reject) => {
      if (process.env.VUE_APP_ENV_API == 'off') {
        let response = {
          authorities: ['role_root'],
          avatar: '',
          active: true,
          optional: {
            roles: ['repayment_view']
          },
          user_name: 'root',
          client_id: '1234'
        }
        if (!response || response.error) {
            reject('Verification failed, please Login again.')
        }

        let roles = undefined
        if (response && response.authorities) {
            if (response.authorities.includes('role_root')) {
                roles = ['admin']
            } else {
              roles = response.optional.roles
            }
        }

        // commit('SET_INFOR_USER', response)
        cookie.setRoles(roles)
        cookie.setInforUser(response)
        resolve(response)
      } else {
        apiGetInfo()
        .then(res => {
          let response = res.data
          if (!response || response.error) {
            reject('Verification failed, please Login again.')
          }
          // commit('SET_INFOR_USER', response)
          cookie.setInforUser(response)
          let roles = undefined
          let projects = undefined
          if (response && response.authorities) {
            if (response.authorities.includes('role_root')) {
              roles = ['admin']
            } else {
              roles = response.optional.roles
            }
          }
          cookie.setRoles(roles)
          resolve(roles)
        }).catch(error => {
          reject(error)
        })
      }
      
    })
  },

  // user logout
  logout({ commit, state }) {
    return new Promise((resolve, reject) => {
      apiLogout(state.token).then(() => {
        cookie.clearCookie()
        localStorage.clear()
        resetRouter()
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },

  // // remove token
  resetToken({ commit }) {
    return new Promise(resolve => {
      cookie.clearCookie()
      localStorage.clear()
      resolve()
    })
  },

  getAllUser({ commit }) {
    return new Promise((resolve, reject) => {
      apiUsers().then(res => {
        let response = res.data
        resolve(response)
      }).catch(error => {
        reject(error)
      })
    })
  },

  createUser({ commit }, data) {
    return new Promise((resolve, reject) => {
      let user = {
        enabled: true
      }
      let userDetail = { ...user, ...data }
      createUser(userDetail).then(res => {
        let response = res.data
        resolve(response)
      }).catch(error => {
        reject(error)
      })
    })
  },

  updateUser({ commit }, {userId, data}) {
    return new Promise((resolve, reject) => {
      updateUser(userId, data).then(res => {
        let response = res.data
        resolve(response)
      }).catch(error => {
        reject(error)
      })
    })
  },

  // // dynamically modify permissions
  // changeRoles({ commit, dispatch }, role) {
  //   return new Promise(async resolve => {
  //     const token = role + '-token'

  //     commit('SET_TOKEN', token)
  //     cookie.setToken(token)

  //     const { roles } = await dispatch('getInfo')

  //     resetRouter()

  //     // generate accessible routes map based on roles
  //     // const accessRoutes = await dispatch('permission/generateRoutes', roles, { root: true })

  //     // dynamically add accessible routes
  //     router.addRoutes(accessRoutes)

  //     // reset visited views and cached views
  //     dispatch('tagsView/delAllViews', null, { root: true })

  //     resolve()
  //   })
  // }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
