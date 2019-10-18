/**
 * @param {Array} value
 * @returns {Boolean}
 * @example see @/views/permission/directive.vue
 */
export default function checkPermission(value) {
  let roles = this.state.user && this.state.user.roles ? this.state.user.roles : []
  if (roles && roles instanceof Array && roles.length > 0) {
    const hasPermission = roles.some(role => {
      return value.includes(role)
    })
    if (!hasPermission) {
      return false
    }
    return true
  } else {
    this.$router.push('/')
  }
}
