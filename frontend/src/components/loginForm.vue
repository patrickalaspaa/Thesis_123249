<template>
  <div class="container">
    <div class="columns is-centered">
      <div class="column is-two-fifths">
        <div class="card">
          <div class="card-header">
            <h1 class="card-header-title is-centered">{{title}}</h1>
          </div>
          <div class="card-content">
            <div class="field">
              <label class="label">Username</label>
              <div class="control">
                <input class="input" v-model="username" id="username" type="text" name="username" placeholder="Enter username" v-on:keyup.enter.native="login">
            </div>
            </div>
            <div class="field">
              <label class="label">Password</label>
              <div class="control">
                <input class="input" v-model="password" id="password" type="password" name="password" placeholder="Enter password" v-on:keyup.enter.native="login">
              </div>
            </div>
            <div class="field is-grouped is-grouped-centered">
              <p class="control is-expanded">
                <a class="button is-primary is-fullwidth" @click="login">
                  Log In
                </a>
              </p>
              <!--
                <p class="control is-expanded">
                  <a class="button is-light is-fullwidth" @click="goToCreateUser()">
                    New User
                  </a>
                </p>
              -->
            </div>
            <div class="control" >
              <p v-if="info !== ''" class="has-text-danger has-text-weight-semibold">{{info}}</p>
              <p v-else class=""> </p>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { store } from '../store'
var consts = require('../const.js').default
export default {
  name: 'loginForm',
  data () {
    return {
      title: 'Log in',
      info: '',
      username: '',
      password: ''
    }
  },
  methods: {
    login: function () {
      function setCookie (name, value) {
        let date = new Date()
        date.setTime(date.getTime() + (5 * 24 * 60 * 60 * 1000))
        let expires = 'expires=' + date.toUTCString(date.setDate(date.getDate() + 7))
        let cookie = name + '=' + value + ';' + expires + ';path=/;'
        document.cookie = cookie
      }

      if (this.username && this.password) {
        this.axios.post(consts.endpoint.login, {
          username: this.username,
          password: this.password
        })
          .then((response) => {
            let session = response.data.session
            setCookie('session', session)
            store.commit('setSession', session)
            this.goTo(consts.path.spots)
          })
          .catch((error) => {
            if (error.response.status === 401) {
              this.info = 'Incorrect username or password'
              this.username = ''
              this.password = ''
              document.getElementById('username').focus()
            } else {
              console.log(error)
            }
          })
      } else {
        this.info = 'Insert username and password'
      }
    },
    goToCreateUser: function () {
      this.goTo(consts.path.editUser)
    },
    goTo: function (path) {
      this.$router.push(path)
    }
  }
}
</script>

<style>
  .new-user span{
    font-size: 1em;
  }
</style>
