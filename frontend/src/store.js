import Vue from 'vue'
import Vuex from 'vuex'
import axios from 'axios'
import VueAxios from 'vue-axios'

Vue.use(Vuex)
Vue.use(VueAxios, axios)

var consts = require('./const.js').default

export const store = new Vuex.Store({
  state: {
    session: '',
    valid: false,
    location: {lat: 0, lng: 0}
  },
  getters: {
    getSession: function () {
      return this.session
    },
    getCallHeader: function () {
      return consts.addRequestHeader(this.session)
    }
  },
  mutations: {
    setSession: function (context, session) {
      context.session = session
    },
    setValidity: function (context, validity) {
      context.valid = validity
    },
    setSessionFromCookie: function (context) {
      let sessionCookie = document.cookie
      if (sessionCookie !== undefined) {
        let cookieMap = consts.convertStringToMap(sessionCookie)
        context.session = cookieMap.session
      }
    },
    setLocation (context, location) {
      context.location = location
    }
  },
  actions: {
    setLocation: function (context) {
      navigator.geolocation.getCurrentPosition((position) => {
        let location = {
          lat: position.coords.latitude,
          lng: position.coords.longitude
        }
        context.commit('setLocation', location)
      })
    }
  }
})
