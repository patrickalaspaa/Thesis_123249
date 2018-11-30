<template>
  <div class="container">
    <div id="create-form" class="columns is-centered">
      <div class="column is-two-fifths">
        <div class="card">
          <div class="card-header">
            <h1 class="card-header-title is-centered">Sign up</h1>
          </div>
          <div class="card-content"  v-if="submit">
            <div class="columns is-centered is-multiline">
              <div class="column is-1">
                <Stretch :background="spinnerColor"></Stretch>
              </div>
            </div>
          </div>
          <div class="card-content" v-else>
            <div class="field">
              <label class="label" for="">Email</label>
              <div class="columns">
                <div class="column is-11">
                  <div class="control">
                    <input class="input" v-model="email" id="email" name= "email" type="text" placeholder="example@mail.com" v-on:focusout="checkEmail">
                  </div>
                </div>
                  <div class="column">
                    <div class="control">
                      <font-awesome-icon v-if="validEmail" icon="check" class="check is-size-4"/>
                      <font-awesome-icon v-else icon="times" class="cross is-size-4"/>
                    </div>
                  </div>
                </div>
            </div>
            <div class="field">
              <label class="label" for="">Username</label>
              <div class="columns">
                <div class="column is-11">
                  <input class="input" v-model="username" id="username" name= "username" type="text" placeholder="choose username" v-on:focusout="checkUsername">
                </div>
                <div class="column">
                  <font-awesome-icon v-if="validUsername" icon="check" class="check is-size-4"/>
                  <font-awesome-icon v-else icon="times" class="cross is-size-4"/>
                </div>
              </div>
            </div>
            <div class="field">
              <label class="label" for="">Password</label>
              <div class="columns">
                <div class="column is-11">
                  <input class="input" v-model="password1" id="password1" name= "password1" type="password" placeholder="Insert password" v-on:keyup="checkPassword">
                </div>
                <div class="column">
                  <font-awesome-icon v-if="validPassword" icon="check" class="check is-size-4"/>
                  <font-awesome-icon v-else icon="times" class="cross is-size-4"/>
                </div>
              </div>
            </div>
            <div class="field">
              <label class="label" for="">Verify Password</label>
              <div class="columns">
                <div class="column is-11">
                  <input class="input" v-model="password2" id="password2" name= "password2" type="password" placeholder="Verify password" v-on:keyup="checkPassword">
                </div>
                <div class="column">
                  <font-awesome-icon v-if="validPassword" icon="check" class="check is-size-4"/>
                  <font-awesome-icon v-else icon="times" class="cross is-size-4"/>
                </div>
              </div>
            </div>
            <div class="field is-grouped is-grouped-centered">
              <p class="control is-expanded">
                <a class="button is-primary is-fullwidth" @click="postUser()">
                  Register
                </a>
              </p>
              <p class="control is-expanded">
                <a class="button is-light is-fullwidth" @click="goToLogin()">
                  Cancel
                </a>
              </p>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
var consts = require('../const.js').default
export default {
  name: 'createUser',
  data () {
    return {
      button: 'Sign up',
      username: '',
      email: '',
      password1: '',
      password2: '',
      validUsername: false,
      validPassword: false,
      validEmail: false,
      submit: false,
      spinnerColor: '#007AC9'
    }
  },
  methods: {
    postUser: function () {
      if (this.validEmail && this.validUsername && this.validPassword) {
        this.submit = true
        this.axios.post(consts.endpoint.person, {
          username: this.username,
          password: this.password1,
          email: this.email
        })
          .then((response) => {
            console.log(response)
            this.goToLogin()
          })
          .catch((error) => {
            this.submit = false
            consts.errorHandler(error)
          })
      }
    },
    checkPassword: function () {
      if (this.password1 === this.password2) {
        this.validPassword = true
      } else {
        this.validPassword = false
      }
    },
    checkUsername: function () {
      if (this.username.length > 0) {
        this.axios.post(consts.endpoint.checkUsername, {
          username: this.username
        })
          .then((response) => {
            this.validUsername = response.data.username
          })
          .catch((error) => {
            consts.errorHandler(error)
          })
      }
    },
    checkEmail: function () {
      //  Add regex to validate email format
      if (this.email.length > 0) {
        this.axios.post(consts.endpoint.checkEmail, {
          email: this.email
        })
          .then((response) => {
            this.validEmail = response.data.email
          })
          .catch((error) => {
            consts.errorHandler(error)
          })
      }
    },
    goToLogin: function () {
      this.$router.push(consts.path.login)
    }
  }
}
</script>

<style>
.cross{
  color: #cc0c00;
  vertical-align: middle;
  text-align: center;
}
.check{
  color: #00bc48;
  vertical-align: middle;
  text-align: center;
}
.icon-div{
  display: inline;
  min-width: 200px;
  padding-left: .5em;
}
</style>
