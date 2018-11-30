import Vue from 'vue'
import Router from 'vue-router'
import loginForm from '@/components/loginForm'
import createUser from '@/components/createUser'
import spotList from '@/components/spotList'
import spotMap from '@/components/spotMap'
import spotView from '@/components/spotView'
import createSpot from '@/components/createSpot'
import axios from 'axios'
import VueAxios from 'vue-axios'
import { store } from '../store'

var consts = require('../const.js').default

Vue.use(Router)
Vue.use(VueAxios, axios)

const router = new Router({
  mode: 'history',
  routes: [
    {
      path: consts.path.login,
      name: 'login',
      component: loginForm
    },
    {
      path: consts.path.editUser,
      name: 'createUser',
      component: createUser
    },
    {
      path: consts.path.spots,
      name: 'spotList',
      component: spotList
    },
    {
      path: consts.path.spots + ':id',
      name: 'spotView',
      component: spotView
    },
    {
      path: consts.path.editSpot,
      name: 'createSpot',
      component: createSpot
    },
    {
      path: consts.path.editSpot + ':id',
      name: 'editSpot',
      component: createSpot
    },
    {
      path: consts.path.map,
      name: 'spotMap',
      component: spotMap
    }
  ]
})

router.beforeEach((to, from, next) => {
  const session = store.state.session
  const validSession = store.state.valid
  const path = to.path

  let authenticate = async function authenticate (sessionId) {
    return axios.get(consts.endpoint.validate, consts.addRequestHeader(sessionId))
  }

  if ((path === consts.path.editUser || path === consts.path.login) && !validSession) {
    next()
  } else if (path === consts.path.login && validSession) {
    next(consts.path.spots)
  } else {
    if (!validSession && session) {
      authenticate(session)
        .then((response) => {
          store.commit('setValidity', true)
          next()
        })
        .catch((response) => {
          store.commit('setValidity', false)
          next(consts.path.login)
        })
    } else if (!validSession && !session) {
      next('/')
    } else {
      next()
    }
  }
})

export default router
