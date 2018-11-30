<template>
  <div class="container">
    <div class="card">
      <!-- Add navigation arrows for photo -->
      <div class="card-header">
        <h1 class="card-header-title">{{ spot.name }}</h1>
        <a class="card-header-icon no-decoration" @click="editSpot(spot.id)">
          <font-awesome-icon icon="edit" class=" is-size-5"/>
        </a>
      </div>

      <div class="card-image">
        <figure class="image is-5by5" @click="nextImage()">
            <img v-if="images.length > 0" :src="images[index].url"/>
            <img v-else src="../assets/Grey_full.png"/>
        </figure>
      </div>
      <div class="card-content">
        <div class="content">
          <p class="has-text-centered">{{ spot.type }}</p>
          <p class="has-text-centered">{{ spot.address }}</p>
          <a @click="getDirectionsLink(spot.lat, spot.lng)">
            <p class="has-text-centered">Directions</p>
          </a>
          <div class="card">
            <div card-header>
              <p class="card-header-title">Tags</p>
            </div>
            <div class=card-content>
              <div class="tags">
                <span class="tag is-info" v-for="obs in obstacles" v-bind:key="obs.obstacle">
                  {{obs.obstacle}}
                </span>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="card-footer" @click="postSpotRating()">
        <div v-if="updateRating" class="card-footer-item">
          <star-rate :disabled="false" v-if="rating !== undefined" v-model="rating" active-color="yellow" inactive-color="#ccc" hover-color="yellow"/>
        </div>
        <div v-else class="card-footer-item">
          <star-rate :disabled="false" v-if="rating !== undefined" v-model="rating" active-color="blue" inactive-color="#ccc" hover-color="blue"/>
        </div>
      </div>
      <input type="text" class="hidden" v-bind:value="spot.id"/>
    </div>
  </div>
</template>

<script>
import { store } from '../store'
import StarRate from 'vue-cute-rate'
var consts = require('../const.js').default
export default {
  name: 'spotView',
  components: {
    StarRate
  },
  data () {
    return {
      spot: {},
      obstacles: [],
      images: [],
      index: 0,
      personalRating: undefined,
      updateRating: false,
      // Rate display error
      rating: undefined,
      location: store.state.location
    }
  },
  async created () {
    this.getSpot()
    this.getSpotObstacles()
    this.getSpotPhotos()
    await this.getSpotRating()
  },
  computed: {
    getImageSrc: function () {
      return this.images[0]
    }
  },
  methods: {
    getSpot: function () {
      this.axios.get(consts.endpoint.spot + this.$route.params.id, consts.addRequestHeader(store.state.session))
        .then((response) => {
          this.spot = response.data[0]
        })
        .catch((response) => {
          consts.errorHandler(response)
        })
    },
    getSpotObstacles: function () {
      this.axios.get(consts.endpoint.spot + this.$route.params.id + '/obs', consts.addRequestHeader(store.state.session))
        .then((response) => {
          this.obstacles = response.data
        })
        .catch((response) => {
          consts.errorHandler(response)
        })
    },
    getSpotPhotos: function () {
      this.axios.get(consts.endpoint.spot + this.$route.params.id + '/photos', consts.addRequestHeader(store.state.session))
        .then((response) => {
          this.images = response.data
        })
        .catch((response) => {
          consts.errorHandler(response)
        })
    },
    getSpotRating: function () {
      this.axios.get(consts.endpoint.spot + this.$route.params.id + '/rating', consts.addRequestHeader(store.state.session))
        .then((response) => {
          // Separate personal ratings from avg
          const rating = response.data[0].rating
          const personal = response.data[0].personal
          this.rating = (rating !== undefined ? rating : 0)
          if (personal === 1) {
            this.updateRating = true
            this.personalRating = this.rating
          } else {
            this.updateRating = false
          }
        })
        .catch((response) => {
          consts.errorHandler(response)
        })
    },
    postSpotRating: function () {
      // change star color
      if (this.personalRating !== this.rating) {
        const postData = {
          rating: this.rating,
          update: this.updateRating
        }
        this.axios.post(
          consts.endpoint.spot + this.$route.params.id + '/rating', postData, consts.addRequestHeader(store.state.session)
        )
        this.personalRating = this.rating
        this.updateRating = true
      }
    },
    nextImage: function () {
      if (this.index < (this.images.length - 1)) {
        this.index = this.index + 1
      } else {
        this.index = 0
      }
    },
    editSpot: function (id) {
      this.$router.push(consts.path.editSpot + id)
    },
    getDirectionsLink: function (lat, lng) {
      const googleDirURL = 'https://www.google.com/maps/dir/'
      const origin = store.state.location.lat + ',' + store.state.location.lng + '/'
      const destination = lat + ',' + lng + '/'

      window.open(googleDirURL + origin + destination + 'am=t', '_blank').focus()
    }
  }
}
</script>

<style>
.no-decoration {
  decoration: none;
  color: #4a4a4a;
}
</style>
