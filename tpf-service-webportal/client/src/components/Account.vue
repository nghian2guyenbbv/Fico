<template>
  <v-layout v-if="s.userSession" pt-1>
    <title>{{ model }}</title>
    <!-- List Data -->
    <v-flex xs12>
      <v-card flat>
        <v-toolbar flat color="white">
          <v-btn flat color="primary" @click="fnCallCreateView(model)"><v-icon>add</v-icon>Create</v-btn>
          <v-spacer />
          <v-text-field placeholder="Search username" v-model="s[model]._search['username']" @change="fnCallListView(model)" append-icon="search" />
        </v-toolbar>
        <v-card-text class="text-no-wrap">
          <v-data-table must-sort :headers="s[model].headers" :items="s[model].list" :total-items="s[model].total" 
            item-key="_id" :rows-per-page-items="s[model].pages" :pagination.sync="s[model].pagination" :loading="s[model].isLoading" 
            @update:pagination="fnPagination({model, event: $event})" :hide-actions="s[model].total<=s[model].rowsPerPage">
            <template slot="items" slot-scope="props">
              <td><v-menu offset-y>
                <v-icon slot="activator">more_horiz</v-icon>
                <v-list dense>
                  <v-list-tile @click="fnCallUpdateView({ model, item: props.item })">Edit</v-list-tile>
                  <v-list-tile @click="fnCallDeleteView({ model, item: props.item })" v-if="fnIsAuth(s.userSession, { authorities: ['role_root'] })">Delete</v-list-tile>
                </v-list>
              </v-menu></td>
              <td>{{ props.item.username }}</td>
              <td>{{ props.item.projects.join() }}</td>
              <td>{{ props.item.departments.join() }}</td>
              <td>{{ props.item.authorities }}</td>
              <td>{{ props.item.enabled }}</td>
            </template>
          </v-data-table>
        </v-card-text>
      </v-card>
    </v-flex>
    <!-- Create/Update/Clone -->
    <v-form v-model="s[model].isValid" @submit.prevent v-if="s[model].isForm">
      <v-dialog persistent v-model="s[model].isForm" scrollable max-width="500">
        <v-card>
          <v-card-text>
            <v-text-field label="Username*" name="username" v-model="s[model].obj.username" :rules="[s.rules.required]" />
            <v-text-field label="Email*" name="email" v-model="s[model].obj.email" :rules="[s.rules.email]" />
            <v-text-field :label="'Password'+(s[model].isCreate?'*':'')" name="password" v-model="s[model].obj.password" :rules="[s[model].isCreate && s.rules.minPass]"
              type="password" v-if="s[model].isCreate || fnIsAuth(s.userSession, { authorities: ['role_root'] })" />
            <v-autocomplete label="Role*" v-model="s[model].obj.authorities" :items="s[model].roles" :rules="[s.rules.required]" clearable dense />
            <v-autocomplete label="Department*" v-model="s[model].obj.departments" :items="departments" :rules="[s.rules.array]" clearable dense multiple />
            <v-autocomplete label="Project*" v-model="s[model].obj.projects" :items="projects" :rules="[s.rules.array]" clearable dense multiple />
            <v-checkbox label="Enable" v-model="s[model].obj.enabled" color="primary"></v-checkbox>
          </v-card-text>
          <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn flat v-if="s[model].isCreate" color="primary" :loading="s[model].isLoading" :disabled="!s[model].isValid" @click="fnCreate(model)">Create</v-btn>
            <v-btn flat v-if="s[model].isUpdate" color="primary" :loading="s[model].isLoading" :disabled="!s[model].isValid" @click="fnUpdate(model)">Update</v-btn>
            <v-btn flat @click="fnCallResetView(model)">Cancel</v-btn>
          </v-card-actions>
        </v-card>
      </v-dialog>
    </v-form>
    <!-- Delete -->
    <v-dialog persistent v-model="s[model].isDelete" scrollable max-width="500" v-if="s[model].isDelete">
      <v-card>
        <v-card-text class="text-xs-center display-1">Are you sure?</v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn flat color="primary" :loading="s[model].isLoading" @click="fnDelete(model)">Delete</v-btn>
          <v-btn flat @click="fnCallResetView(model)">Cancel</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-layout>
</template>

<script>
export default {
  data() {
    return {  model: 'Account' }
  },
  created() {
    this.fnCallListView(this.model)
  },
  computed: {
    departments() {
      if (!this.s.userSession.departments) return this.s[this.model].departments
      return this.s[this.model].departments.filter(i => this.s.userSession.departments.indexOf(i.value) !== -1)
    },
    projects() {
      if (!this.s.userSession.projects) return this.s[this.model].projects
      return this.s[this.model].projects.filter(i => this.s.userSession.projects.indexOf(i.value) !== -1)
    }
  }
}
</script>