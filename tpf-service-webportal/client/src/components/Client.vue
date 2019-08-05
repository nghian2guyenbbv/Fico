<template>
  <v-layout v-if="s.userSession" pa-1>
    <title>{{ model }}</title>
    <!-- List Data -->
    <v-flex xs12>
      <v-card flat>
        <v-toolbar flat color="white">
          <v-btn flat color="primary" @click="fnCallCreateView(model)"><v-icon>add</v-icon>Create</v-btn>
          <v-spacer />
          <v-text-field placeholder="Search client id" v-model="s[model]._search['clientId']" @change="fnCallListView(model)" append-icon="search" />
        </v-toolbar>
        <v-card-text class="text-no-wrap">
          <v-data-table must-sort :headers="s[model].headers" :items="s[model].list" :total-items="s[model].total" 
            item-key="id" :rows-per-page-items="s[model].pages" :pagination.sync="s[model].pagination" :loading="s[model].isLoading"
            @update:pagination="fnPagination({model, event: $event})" :hide-actions="s[model].total<=s[model].rowsPerPage">
            <template slot="items" slot-scope="props">
              <td><v-menu offset-y>
                <v-icon slot="activator">more_horiz</v-icon>
                <v-list dense>
                  <v-list-tile @click="fnCallUpdateView({ model, item: props.item })">Edit</v-list-tile>
                  <v-list-tile @click="fnCallDeleteView({ model, item: props.item })">Delete</v-list-tile>
                </v-list>
              </v-menu></td>
              <td>{{ props.item.clientId }}</td>
              <td>{{ props.item.secret }}</td>
            </template>
          </v-data-table>
        </v-card-text>
      </v-card>
    </v-flex>
    <!-- Create/Update/Clone -->
    <v-form v-model="s[model].isValid" @submit.prevent v-if="s[model].isForm">
      <v-dialog persistent v-model="s[model].isForm" scrollable max-width="600">
        <v-card>
          <v-card-text>
            <v-text-field label="Client ID*" name="clientId" v-model="s[model].obj.clientId" :rules="[s.rules.required]" />
            <v-text-field label="Client Secret*" name="clientSecret" v-model="s[model].obj.clientSecret" :rules="[s.rules.required]" />
            <v-text-field label="Access Token Validity" name="accessTokenValidity" v-model.number="s[model].obj.accessTokenValidity" type="number" />
            <v-text-field label="Refresh Token Validity" name="refreshTokenValidity" v-model.number="s[model].obj.refreshTokenValidity" type="number" />
            <v-text-field label="Authorized Grant Types" name="authorizedGrantTypes" v-model="s[model].obj.authorizedGrantTypes" />
            <v-text-field label="Scope" name="scope" v-model="s[model].obj.scope" />
            <v-text-field label="Auto Approve" name="autoapprove" v-model="s[model].obj.autoapprove" />
            <v-text-field label="Authorities" name="authorities" v-model="s[model].obj.authorities" />
            <v-text-field label="ResourceIds" name="resourceIds" v-model="s[model].obj.resourceIds" />
            <v-text-field label="Web Server Redirect Uri" name="webServerRedirectUri" v-model="s[model].obj.webServerRedirectUri" />
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
    return { model: 'Client' }
  },
  created() {
    this.fnCallListView(this.model)
  }
}
</script>