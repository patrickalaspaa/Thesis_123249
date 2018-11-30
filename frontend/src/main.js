// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
import axios from 'axios'
import VueAxios from 'vue-axios'
import * as VueGoogleMaps from 'vue2-google-maps'
import { library } from '@fortawesome/fontawesome-svg-core'
import { faCheck, faTimes, faUpload, faEdit, faMapMarkedAlt, faListAlt, faPlusSquare, faSignOutAlt } from '@fortawesome/free-solid-svg-icons'
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'
import Vuex from 'vuex'
import 'es6-promise/auto'
import 'bulma/css/bulma.css'
import { store } from './store'
var consts = require('./const.js').default

if (store.state.session === undefined || store.state.session === '') {
  store.commit('setSessionFromCookie')
}

Vue.config.productionTip = false
Vue.use(VueAxios, axios)
Vue.use(Vuex)
Vue.use(VueGoogleMaps, {
  load: {
    key: consts.APIKey,
    libraries: 'places'
  }
})
library.add(faCheck)
library.add(faTimes)
library.add(faUpload)
library.add(faEdit)
library.add(faMapMarkedAlt)
library.add(faListAlt)
library.add(faPlusSquare)
library.add(faSignOutAlt)
Vue.component('font-awesome-icon', FontAwesomeIcon)

Vue.config.productionTip = false
/*
if (store.state.session === undefined || store.state.session === '') {
  store.commit('setSessionFromCookie')
}
store.dispatch('setLocation')
*/
/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  components: { App },
  template: '<App/>'
})
