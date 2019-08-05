<template>
  <v-app>
    <v-snackbar :timeout="s.snackbar.timeout" :color="s.snackbar.color" top v-model="s.snackbar.show">
      {{ s.snackbar.message }}<v-btn icon dark flat @click="s.snackbar.show=false"><v-icon>close</v-icon></v-btn>
    </v-snackbar>
    <span v-if="s.userSession">
      <v-toolbar flat app fixed clipped-left dense color="white">
        <v-btn icon @click="goto('/')" :color="path==='dashboard'?'primary':''"><v-icon>dashboard</v-icon></v-btn>
        <v-btn icon @click="goto('account')" :color="path==='account'?'primary':''" v-if="fnIsAuth(s.userSession, { authorities: ['role_root','role_admin'] })"><v-icon>person</v-icon></v-btn>
        <v-btn icon @click="goto('client')" :color="path==='client'?'primary':''" v-if="fnIsAuth(s.userSession, { authorities: ['role_root'] })"><v-icon>camera</v-icon></v-btn>
        <v-spacer />
        <v-toolbar-title class="hidden-xs-only">{{ s.userSession.user_name.split('@')[0] +','+ s.userSession.authorities[0] +','+ (s.userSession.departments && s.userSession.departments.join()) }}</v-toolbar-title>
        <v-spacer />
        <v-btn icon @click="fnCallUpdateView({ model: 'ChangePassword', item: {} })"><v-icon>lock</v-icon></v-btn>
        <v-btn icon @click="fnLogout"><v-icon>exit_to_app</v-icon></v-btn>
      </v-toolbar>
      <v-form v-model="s['ChangePassword'].isValid" @submit.prevent v-if="s['ChangePassword'].isForm">
        <v-dialog persistent v-model="s['ChangePassword'].isForm" scrollable max-width="500">
          <v-card>
            <v-card-text>
              <v-text-field label="Old Password" v-model="s['ChangePassword'].obj.oldPassword" :rules="[s.rules.required, s.rules.minPass]" prepend-icon="lock" type="password" />
              <v-text-field label="New Password" v-model="s['ChangePassword'].obj.newPassword" :rules="[s.rules.required, s.rules.minPass]" prepend-icon="lock" type="password" />
            </v-card-text>
            <v-card-actions>
              <v-spacer></v-spacer>
              <v-btn flat color="primary" :loading="s['ChangePassword'].isLoading" :disabled="!s['ChangePassword'].isValid" @click="s['ChangePassword'].obj.notoastr=true;fnCreate('ChangePassword')">Submit</v-btn>
              <v-btn flat @click="fnCallResetView('ChangePassword')">Cancel</v-btn>
            </v-card-actions>
          </v-card>
        </v-dialog>
      </v-form>
    </span>
    <v-content>
      <router-view></router-view>
    </v-content>
  </v-app>
</template>

<script>
export default {
  computed: {
    path() {
      return this.$route.name
    }
  },
  methods: {
    goto(name) {
      this.$router.push(name)
    }
  }
}
</script>

<style>
  ::-webkit-scrollbar { width: 3px; height: 5px; }
  ::-webkit-scrollbar-track { background: #f1f1f1; }
  ::-webkit-scrollbar-thumb { background: #888; }
  ::-webkit-scrollbar-thumb:hover { background: #555; }
</style>