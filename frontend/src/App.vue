<template>
  <div id="app">
    <div class="hero is-fullheight">
      <div class="hero-head HFL-blue">
        <nav class="navbar" role="navigation" aria-label="main navigation">
          <div class="navbar-brand">
            <a class="navbar-item" @click="goToLogin()">
              <img src="./assets/HFL_full-logo-white.png" alt="" style="height: 4em;">
            </a>
            <a v-if="hrefCheck()" id="navbar-burger" role="button" class="navbar-burger" aria-label="menu" aria-expanded="false" @click="toggleNavigation()">
              <span aria-hidden="true"></span>
              <span aria-hidden="true"></span>
              <span aria-hidden="true"></span>
            </a>
          </div>
          <div v-if="hrefCheck()" id="navbar-menu" class="navbar-menu">
            <div class="navbar-start">
              <a class="navbar-item" @click="goToMap()">
                <font-awesome-icon icon="map-marked-alt" class=" is-size-3"/>
                <p class="nav-icon-text">Map</p>
              </a>
              <a class="navbar-item" @click="goToSpots()">
                <font-awesome-icon icon="list-alt" class=" is-size-3"/>
                <p class="nav-icon-text">List</p>
              </a>
              <a class="navbar-item" @click="goToCreateSpot()">
                <font-awesome-icon icon="plus-square" class=" is-size-3"/>
                <p class="nav-icon-text">Add spot</p>
              </a>
              <a class="navbar-item" @click="LogOut()">
                <font-awesome-icon icon="sign-out-alt" class=" is-size-3"/>
                <p class="nav-icon-text">Log out</p>
              </a>
            </div>
            <div class="navbar-end">

            </div>
          </div>
        </nav>
      </div>
      <div class="hero-body">
        <router-view/>
      </div>
      <div class="hero-foot">
      </div>
    </div>
  </div>
</template>

<script>
import { store } from './store'
var consts = require('./const.js').default

export default {
  name: 'App',
  created () {
    store.dispatch('setLocation')
  },
  methods: {
    toggleNavigation: function () {
      let burger = document.getElementById('navbar-burger')
      let menu = document.getElementById('navbar-menu')

      burger.classList.toggle('is-active')
      menu.classList.toggle('is-active')
    },
    push: function (path) {
      this.$router.push(path)
      this.toggleNavigation()
    },
    goToLogin: function () {
      this.push(consts.path.login)
    },
    goToMap: function () {
      this.push(consts.path.map)
    },
    goToCreateSpot: function () {
      this.push(consts.path.editSpot)
    },
    goToSpots: function () {
      this.push(consts.path.spots)
    },
    LogOut: function () {
      let date = new Date()
      let session = 'session="";'
      let expires = 'expires=' + date.toUTCString(date.setDate(date.getDate() - 7)) + ';'

      store.commit('setValidity', false)
      document.cookie = session + expires
      this.push(consts.path.login)
    },
    hrefCheck: function () {
      return window.location.pathname !== consts.path.login
    }
  }
}
</script>

<style>
#app {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  color: #2c3e50;
  background-color: #DEEDF5;
}
.hidden{
  visibility: hidden;
}
.navbar-item img {
  max-height: 5em;
}
.HFL-blue, .navbar-menu {
  background-color: #007AC9;
}
.card {
  border-radius: .75em;
}
.navbar-item, .navbar-link, .navbar-burger, .navbar-burger .is-active {
  color: white !important;
}
.navbar-burger {
  width: 4rem;
  height: 4rem;
}
.navbar-burger span {
  width: 2rem;
  height: 3px;
  left: calc(50% - 12px);
  border-radius: 2px;
}
.navbar-burger span:nth-child(1) {
    top: calc(50% - 10px);
}
.navbar-burger span:nth-child(2) {
    top: calc(50% - 2px);
}
.navbar-burger span:nth-child(3) {
    top: calc(50% + 6px);
}
.nav-icon-text {
  padding-left:1rem;
  font-weight: 900;
}
.navbar-item:hover {
  background-color: rgba(255, 255, 255, 0.2) !important;
}
@media only screen and (max-width: 1086px) {
  .navbar-start .navbar-item {
    border-top: 2px solid #0162A1;
    margin: 0 10px 0 10px;
  }
  .navbar-item img {
    max-height: 2em;
  }
  .is-active span:nth-child(1) {
      top: calc(50% - 10px);
  }
  .is-active span:nth-child(3) {
      top: calc(50%);
  }
  .nav-icon-text {
    display: inline;
    padding-left:1rem;
    font-weight: 900;
  }
  .hero-body {
    padding: 1rem .5rem;
  }
}
</style>
