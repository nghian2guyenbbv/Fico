<template>
  <v-layout align-center justify-center fill-height>
    <title>{{ model }}</title>
    <v-flex xs12 sm4>
      <v-form v-model="s[model].isValid" @submit.prevent="fnLogin(model)">
        <v-card>
          <v-card-text class="primary--text text-xs-center display-1">Login</v-card-text>
          <v-card-text>
            <v-text-field label="Username" name="username" v-model="s[model].obj.username" :rules="[s.rules.required]" prepend-icon="person" />
            <v-text-field label="Password" name="password" v-model="s[model].obj.password" :rules="[s.rules.required]" prepend-icon="lock" type="password" />
          </v-card-text>
          <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn flat type="submit" color="primary" :loading="s[model].isLoading" :disabled="!s[model].isValid">{{ model }}</v-btn>
          </v-card-actions>
        </v-card>
      </v-form>
    </v-flex>
  </v-layout>
</template>

<script>
export default {
  data() {
    return { model: 'Login' }
  },
  created() {
    this.s.userSession && this.$router.push('/')
  }
}
</script>
