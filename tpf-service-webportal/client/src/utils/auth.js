import Cookies from 'js-cookie'

export function getToken() {
  return Cookies.get('token')
}

export function setToken(token, expires) {
  return Cookies.set('token', token, { expires: expires })
}

export function removeToken() {
  return Cookies.remove('token')
}
