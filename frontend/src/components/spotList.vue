<template>
  <div class="container">
    <div class="spot-list">
      <div class="columns is-multiline is-centered">
        <div class="column is-half" v-for="spot in spotList" v-bind:key="spot.id">
          <div class="card">
              <div class="card-header">
                <p class="card-header-title">{{spot.name}}</p>
                <a class="card-header-icon no-decoration" @click="editSpot(spot.id)">
                  <font-awesome-icon icon="edit" class=" is-size-5"/>
                </a>
              </div>
              <a class="no-decoration" @click="viewSpot(spot.id)" >
              <div class="card-image">
                <figure class="image is-5by5">
                  <img v-if="spot.cover" v-bind:src="'http://localhost:80/' + spot.id + '/' + spot.cover" :alt="spot.name"/>
                  <img v-else src="../assets/Grey_full.png"/>
                </figure>
              </div>
              <div class="card-content">
                <div class="content">
                  <p class="has-text-centered">{{spot.type}}</p>
                  <p class="has-text-centered">{{spot.address}}</p>
                </div>
              </div>
              </a>
              <div class="card-footer">
                <div class="card-footer-item">
                  <star-rate class="rating" :value="spot.rating" :disabled="true" active-color="blue" inactive-color="#ccc" hover-color="lightblue"/>
                </div>
              </div>
          </div>
        </div>
      </div>
    </div>
    <!--
    <nav class="pagination is-centered" role="navigation" aria-label="pagination">
      <a class="pagination-previous" @click="previousPage()">Previous</a>
      <a class="pagination-next" @click="nextPage()">Next page</a>
    </nav>
  -->
  </div>
</template>

<script>
import { store } from '../store'
import StarRate from 'vue-cute-rate'
var consts = require('../const.js').default

export default {
  name: 'spotList',
  components: {
    StarRate
  },
  data () {
    return {
      spotList: {},
      offset: 0
    }
  },
  beforeMount () {
    this.getSpotList()
  },
  methods: {
    getSpotList: function () {
      // this.axios.get(consts.endpointSpot + '?limit=10&offset=' + this.offset)
      this.axios.get(consts.endpoint.spot, consts.addRequestHeader(store.state.session))
        .then((response) => {
          this.spotList = response.data
        })
        .catch((response) => {
          consts.errorHandler(response)
        })
    },
    nextPage: function () {
      this.offset = this.offset + 10
      this.getSpotList()
    },
    previousPage: function () {
      if (this.offset > 10) {
        this.offset = this.offset - 10
      } else {
        this.offset = 0
      }
      this.getSpotList()
    },
    viewSpot: function (id) {
      this.$router.push(consts.path.spots + id)
    },
    editSpot: function (id) {
      this.$router.push(consts.path.editSpot + id)
    }
  }
}
</script>

<style>
.no-decoration {
  decoration: none;
  color: #4a4a4a;
}
.rating {
}
</style>
