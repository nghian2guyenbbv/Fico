<template>
  <v-layout v-if="s.userSession" pt-1 justify-center wrap>
    <title>{{ model }}</title>
    <v-flex xs12>
      <v-card flat>
        <v-toolbar flat dense color="white">
          <v-toolbar-title>Assigned</v-toolbar-title>
          <v-spacer />
          <v-text-field placeholder="Search app" v-model="s['Assigned']._search['appId']" @change="fnCallListView('Assigned')" append-icon="search" />
        </v-toolbar>
        <v-card-text class="text-no-wrap">
          <v-data-table :headers="s['Assigned'].headers" :items="s['Assigned'].list" :total-items="s['Assigned'].total" item-key="_id" 
            :rows-per-page-items="s['Assigned'].pages" :loading="s['Assigned'].isLoading" :hide-actions="s['Assigned'].total<=s['Assigned'].rowsPerPage">
            <template slot="items" slot-scope="props">
              <td>{{ props.item.appId }}</td>
              <td>{{ props.item.partnerId }}</td>
              <td>{{ props.item.fullName }}</td>
              <td>{{ props.item.productCode }}</td>
              <td>{{ props.item.schemeCode }}</td>
              <td>{{ props.item.status }}</td>
              <td>{{ props.item.project }}</td>
              <td>{{ props.item.photos.length }}</td>
              <td>{{ props.item.assigned }}</td>
              <td>{{ props.item.createdAt | fDateTime }}</td>
            </template>
          </v-data-table>
        </v-card-text>
      </v-card>
      <v-card flat class="mt-2">
        <v-toolbar flat dense color="white">
          <v-toolbar-title>Unassigned</v-toolbar-title>
          <v-spacer />
          <v-text-field placeholder="Search app" v-model="s['Unassigned']._search['appId']" @change="fnCallListView('Unassigned')" append-icon="search" />
        </v-toolbar>
        <v-card-text class="text-no-wrap">
          <v-data-table :headers="s['Unassigned'].headers" :items="s['Unassigned'].list" :total-items="s['Unassigned'].total" item-key="_id" 
            :rows-per-page-items="s['Unassigned'].pages" :loading="s['Unassigned'].isLoading" :hide-actions="s['Unassigned'].total<=s['Unassigned'].rowsPerPage">
            <template slot="items" slot-scope="props">
              <td>{{ props.item.appId }}</td>
              <td>{{ props.item.partnerId }}</td>
              <td>{{ props.item.fullName }}</td>
              <td>{{ props.item.productCode }}</td>
              <td>{{ props.item.schemeCode }}</td>
              <td>{{ props.item.status }}</td>
              <td>{{ props.item.project }}</td>
              <td>{{ props.item.photos.length }}</td>
              <td><v-btn icon @click="fnAssign(props.item)"><v-icon>person</v-icon></v-btn></td>
              <td>{{ props.item.createdAt | fDateTime }}</td>
            </template>
          </v-data-table>
        </v-card-text>
      </v-card>
    </v-flex>
  </v-layout>
</template>

<script>
export default {
  data() {
    return { model: 'DashBoard' }
  },
  created() {
    this.s['Assigned']._search['assigned'] = this.s.userSession.user_name 
    this.fnCallListView('Assigned')
    this.fnCallListView('Unassigned')
  },
  computed: {
    departments() {
      if (!this.s.userSession.departments) return this.s['Account'].departments
      return this.s['Account'].departments.filter(i => this.s.userSession.departments.indexOf(i.value) !== -1)
    },
    projects() {
      if (!this.s.userSession.projects) return this.s['Account'].projects
      return this.s['Account'].projects.filter(i => this.s.userSession.projects.indexOf(i.value) !== -1)
    }
  },
  methods: {
    fnAssign(app) {
      this.s['App'].obj.assigned = this.s.userSession.user_name
      this.s['App'].obj.project = app.project
      this.s['App'].obj.id = app.uuid.split('_')[1]
      this.fnUpdate('App')
    },
    fnUnassign(app) {
      this.s['App'].obj.unassigned = app.assigned
      this.s['App'].obj.project = app.project
      this.s['App'].obj.id = app.uuid.split('_')[1]
      this.fnUpdate('App')
    }
  }
}
</script>
