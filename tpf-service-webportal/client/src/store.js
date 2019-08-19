import Vue from 'vue'
import Vuex from 'vuex'
import axios from 'axios'
import router from './router'
import { fnCopy } from './utils'
import * as io from 'socket.io-client'

Vue.use(Vuex)

const pagination = { _page: 1, rowsPerPage: 10, descending: true, sortBy: 'createdAt' }

const opt = {
	total: 0, list: [], obj: {}, _search: {}, _sort: '', _select: '',
	selected: [], pagination, ...pagination, pages: [10, 20, 50, 100],
	isLoading: false, isCreate: false, isUpdate: false, isDelete: false, isForm: false
}

const host = window.location.hostname === 'localhost' ? 'http://localhost:4000' : ''
const hostSocket = window.location.hostname === 'localhost' ? 'http://localhost:3000' : ''

export default new Vuex.Store({
	state: {
		s: {
			socket: null,
			userSession: null,
			http: axios.create({ baseURL: host }),
			snackbar: { color: '', message: '', timeout: 3000, show: false },
			rules: {
				required: v => !!v || 'Required',
				number: v => Number(v) >= 0 || 'Required',
				minPass: v => !!v && v.length >= 5 || 'Must be minimum 5 characters',
				array: v => !!v.length || 'Required',
				email: v => /.+@.+/.test(v) || 'Invalid E-mail'
			},
			Me: { ...fnCopy(opt), url: '/v1/me' },
			Logout: { ...fnCopy(opt), url: '/v1/logout' },
			Login: { ...fnCopy(opt), url: '/v1/login' },
			ChangePassword: { ...fnCopy(opt), url: '/v1/change-password' },
			App: { ...fnCopy(opt), url: '/v1/app' },
			Assigned: {
				...fnCopy(opt), url: '/v1/app', headers: [
					{ text: 'App Id', value: 'appId', sortable: false },
					{ text: 'Partner Id', value: 'partnerId', sortable: false },
					{ text: 'Full Name', value: 'fullName', sortable: false },
					{ text: 'Product Code', value: 'productCode', sortable: false },
					{ text: 'Scheme Code', value: 'schemeCode', sortable: false },
					{ text: 'Status', value: 'status', sortable: false },
					{ text: 'Project', value: 'project', sortable: false },
					{ text: 'Photos', value: 'photos', sortable: false },
					{ text: 'Assigned', value: 'assigned', sortable: false },
					{ text: 'Created At', value: 'createdAt', sortable: false }
				]
			},
			Unassigned: {
				...fnCopy(opt), url: '/v1/app', headers: [
					{ text: 'App Id', value: 'appId', sortable: false },
					{ text: 'Partner Id', value: 'partnerId', sortable: false },
					{ text: 'Full Name', value: 'fullName', sortable: false },
					{ text: 'Product Code', value: 'productCode', sortable: false },
					{ text: 'Scheme Code', value: 'schemeCode', sortable: false },
					{ text: 'Status', value: 'status', sortable: false },
					{ text: 'Project', value: 'project', sortable: false },
					{ text: 'Photos', value: 'photos', sortable: false },
					{ text: 'Assigned', value: 'assigned', sortable: false },
					{ text: 'Created At', value: 'createdAt', sortable: false }
				]
			},
			Account: {
				...fnCopy(opt), url: '/v1/account',
				roles: [
					{ value: 'role_admin', text: 'Admin' },
					{ value: 'role_manager', text: 'Manager' },
					{ value: 'role_viewer', text: 'Viewer' },
					{ value: 'role_user', text: 'User' }
				],
				projects: [
					{ value: 'fpt', text: 'Fpt' },
					{ value: 'momo', text: 'Momo' },
					{ value: 'vinid', text: 'Vin ID' },
					{ value: 'trusting_social', text: 'Trusting Social' },
				],
				departments: [
					{ value: 'automation', text: 'Automation' },
					{ value: 'document_check', text: 'Document Check' },
					{ value: 'loan_booking', text: 'Loan Booking' }
				],
				headers: [
					{ text: '#', value: 'createdAt', width: '5px' },
					{ text: 'Username', value: 'username' },
					{ text: 'Project', value: 'projects' },
					{ text: 'Department', value: 'departments' },
					{ text: 'Role', value: 'authorities' },
					{ text: 'Enable', value: 'enabled' }
				]
			},
			Client: {
				...fnCopy(opt), url: '/v1/client',
				headers: [
					{ text: '#', value: 'createdAt', width: '5px' },
					{ text: 'Client Id', value: 'clientId' },
					{ text: 'Client Secret', value: 'secret' }
				]
			}
		}
	},
	actions: {
		// No mapActions
		fnSocket: ({ dispatch, state }) => {
			state.s.socket = io(`${hostSocket}?token=${state.s.userSession.token}`, { transports: ['websocket'] })
			state.s.socket.on('connect', () => console.log('Connected'))
			state.s.socket.on('disconnect', () => console.log('Disconnected'))
			state.s.socket.on('error', () => dispatch('fnRemoveUserSession'))
			state.s.socket.on('message', ({ action, data }) => {
				switch (action) {
					case 'CREATE':
						dispatch('fnFilterCreate', { model: 'Unassigned', data })
						break
					case 'UPDATE':
						if (data.assigned
							&& (data.assigned === state.s.userSession.user_name || state.s.userSession.authorities[0] !== 'role_user')) {
							dispatch('fnFilterCreate', { model: 'Assigned', data })
						} else dispatch('fnFilterDelete', { model: 'Assigned', data })

						if (!data.assigned) dispatch('fnFilterCreate', { model: 'Unassigned', data })
						else dispatch('fnFilterDelete', { model: 'Unassigned', data })
						break
					case 'DELETE':
						dispatch('fnFilterDelete', { model: 'Assigned', data })
						dispatch('fnFilterDelete', { model: 'Unassigned', data })
						break
				}
			})
		},

		fnSetUserSession: ({ dispatch, state }, user) => {
			state.s.userSession = user
			localStorage.setItem('userSession', JSON.stringify(state.s.userSession))
			dispatch('fnSocket')
		},

		fnGetUserSession: ({ dispatch, state }) => {
			if (!state.s.userSession && localStorage.getItem('userSession')) {
				dispatch('fnSetUserSession', JSON.parse(localStorage.getItem('userSession')))
			}
			return state.s.userSession
		},

		fnRemoveUserSession: ({ state }) => {
			state.s.http.get(state.s['Logout'].url, { headers: { Authorization: 'Bearer ' + state.s.userSession.token } })
			state.s.userSession = null
			state.s.socket.disconnect()
			localStorage.clear()
			router.push('/login')
		},

		fnHandleError: ({ state, dispatch }, { model, error }) => {
			state.s[model].isLoading = false
			if (!error.response || error.response.status === 401) dispatch('fnRemoveUserSession')
			else {
				const message = error.response.data['message'] || error.response.data['error_description']
				dispatch('fnToastr', { type: 'error', message })
			}
		},

		fnFilterCreate: ({ state }, { model, data }) => {
			const idx = state.s[model].list.findIndex(e => e.id === data.id)
			if (idx !== -1) Vue.set(state.s[model].list, idx, { ...state.s[model].list[idx], ...data })
			else {
				state.s[model].list.unshift(data)
				state.s[model].total += 1
				if (state.s[model].list.length > state.s[model].rowsPerPage) state.s[model].list.pop()
			}
		},

		fnFilterUpdate: ({ state }, { model, data }) => {
			const idx = state.s[model].list.findIndex(e => e.id === data.id)
			if (idx !== -1) Vue.set(state.s[model].list, idx, { ...state.s[model].list[idx], ...data })
		},

		fnFilterDelete: ({ dispatch, state }, { model, data }) => {
			state.s[model].list = state.s[model].list.filter(e => e.id !== data.id)
			state.s[model].total += -1
			if (state.s[model].list.length === 0) dispatch('fnRead', model)
		},

		fnLoadData: ({ state }, model) => {
			switch (model) {
				case 'Client':
					if (state.s[model].isCreate) {
						state.s[model].obj = {
							...state.s[model].obj, authorizedGrantTypes: 'client_credentials',
							accessTokenValidity: 2592000, refreshTokenValidity: 15552000
						}
					} else {
						state.s[model].obj = { ...state.s[model].obj, clientSecret: state.s[model].obj.secret }
					}
					break
			}
		},

		// mapActions
		fnToastr: ({ state }, { message, type }) => {
			state.s.snackbar = { ...state.s.snackbar, message, color: type, show: true }
		},

		fnLogout: ({ dispatch }) => dispatch('fnRemoveUserSession'),

		fnLogin: ({ dispatch, state }, model) => {
			state.s[model].isLoading = true
			state.s.http.post(state.s[model].url, state.s[model].obj).then(success => {
				state.s.http.get(state.s['Me'].url, { headers: { Authorization: 'Bearer ' + success.data.access_token } }).then(info => {
					dispatch('fnSetUserSession', { ...info.data, token: success.data.access_token })
					dispatch('fnCallResetView', model)
					router.push('/')
				})
			}).catch(error => dispatch('fnHandleError', { model, error }))
		},

		fnPagination: ({ dispatch, state }, { model, event }) => {
			const { sortBy, descending, page, rowsPerPage } = event

			if (
				state.s[model]._page !== page
				|| state.s[model].rowsPerPage !== rowsPerPage
				|| (state.s[model].sortBy !== sortBy && sortBy !== null)
				|| (state.s[model].descending !== descending && descending !== null)
			) {
				state.s[model].sortBy = sortBy
				state.s[model].descending = descending
				state.s[model]._sort = descending ? `${sortBy},desc` : `${sortBy},asc`
				state.s[model]._page = page
				state.s[model].rowsPerPage = rowsPerPage
				dispatch('fnRead', model)
			}

			return { sortBy, descending, page, rowsPerPage }
		},

		fnRead: ({ dispatch, state }, model) => new Promise(resolve => {
			state.s[model].isLoading = true
			const { rowsPerPage, _page, _sort, _search, _select } = state.s[model]
			let q = `page=${_page}&limit=${rowsPerPage}`

			if (_select) q += `&select=${_select}`
			if (_sort) q += `&sort=${_sort}`
			for (const i in _search) if (_search[i]) q += `&${i}=${_search[i]}`

			state.s.http.get(`${state.s[model].url}?${q}`, { headers: { Authorization: 'Bearer ' + state.s.userSession.token } }).then(success => {
				if (Array.isArray(success.data)) {
					state.s[model].list = success.data
					state.s[model].total = Number(success.headers['x-pagination-total']) || 0
				} else {
					state.s[model].obj = success.data
				}
				state.s[model].isLoading = false
				resolve(success.data)
			}).catch(error => dispatch('fnHandleError', { model, error }))
		}),

		fnCreate: ({ dispatch, state }, model) => new Promise(resolve => {
			state.s[model].isLoading = true
			let data = state.s[model].obj

			if (state.s[model].obj.file) {
				data = new FormData()
				for (const i in state.s[model].obj) {
					data.append(i, state.s[model].obj[i])
				}
			}

			state.s.http.post(state.s[model].url, data, { headers: { Authorization: 'Bearer ' + state.s.userSession.token } }).then(success => {
				if (!state.s[model].obj.notoastr) dispatch('fnToastr', { type: 'success', message: 'Created' })
				if (!state.s[model].obj.noreset) dispatch('fnCallResetView', model)
				if (model === 'ChangePassword') dispatch('fnRemoveUserSession')
				dispatch('fnFilterCreate', { model, data: success.data })
				resolve(success.data)
			}).catch(error => dispatch('fnHandleError', { model, error }))
		}),

		fnUpdate: ({ dispatch, state }, model) => new Promise(resolve => {
			state.s[model].isLoading = true
			const url = `${state.s[model].url}/${state.s[model].obj.id}`

			state.s.http.put(url, state.s[model].obj, { headers: { Authorization: 'Bearer ' + state.s.userSession.token } }).then(success => {
				if (!state.s[model].obj.notoastr) dispatch('fnToastr', { type: 'success', message: 'Updated' })
				if (!state.s[model].obj.noreset) dispatch('fnCallResetView', model)
				dispatch('fnFilterUpdate', { model, data: success.data })
				resolve(success.data)
			}).catch(error => dispatch('fnHandleError', { model, error }))
		}),

		fnDelete: ({ dispatch, state }, model) => new Promise(resolve => {
			state.s[model].isLoading = true
			const url = `${state.s[model].url}/${state.s[model].obj.id}`

			state.s.http.delete(url, { headers: { Authorization: 'Bearer ' + state.s.userSession.token } }).then(success => {
				if (!state.s[model].obj.notoastr) dispatch('fnToastr', { type: 'success', message: 'Deleted' })
				if (!state.s[model].obj.noreset) dispatch('fnCallResetView', model)
				dispatch('fnFilterDelete', { model, data: success.data })
				resolve(success.data)
			}).catch(error => dispatch('fnHandleError', { model, error }))
		}),

		fnCallResetView: ({ state }, model) => {
			state.s[model] = {
				...state.s[model], obj: {},
				isLoading: false, isCreate: false, isUpdate: false, isDelete: false, isForm: false
			}
		},

		fnCallListView: ({ dispatch }, model) => {
			dispatch('fnCallResetView', model)
			dispatch('fnRead', model)
		},

		fnCallCreateView: ({ dispatch, state }, model) => {
			dispatch('fnCallResetView', model)
			state.s[model].isCreate = true
			state.s[model].isForm = true
			dispatch('fnLoadData', model)
		},

		fnCallUpdateView: ({ dispatch, state }, { model, item }) => {
			dispatch('fnCallResetView', model)
			state.s[model].isUpdate = true
			state.s[model].isForm = true
			state.s[model].obj = fnCopy(item)
			dispatch('fnLoadData', model)
		},

		fnCallDeleteView: ({ dispatch, state }, { model, item }) => {
			dispatch('fnCallResetView', model)
			state.s[model].isDelete = true
			state.s[model].obj = fnCopy(item)
		}
	}
})