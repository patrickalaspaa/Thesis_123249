<template>
  <div class="container">
    <!-- <div>
      <h2>Search and add a pin</h2>
      <label>
        <gmap-autocomplete
          @place_changed="setPlace">
        </gmap-autocomplete>
        <button @click="addMarker">Add</button>
      </label>
      <br/>
    </div>
    <br> -->
    <gmap-map id="gmap" v-bind:center="center" v-bind:zoom="15" class="map-view" v-bind:options="options">
      <gmap-marker
        v-bind:key="index"
        v-for="(m, index) in markers"
        v-bind:position="m.position"
        v-bind:title="m.position.name"
        @click="navigateToSpot(m.position.id)"></gmap-marker>
    </gmap-map>
  </div>
</template>

<script>
import { store } from '../store'
var consts = require('../const.js').default
export default {
  name: 'spotMap',
  data () {
    return {
      center: { lat: 60.155276, lng: 24.9240902 },
      markers: [],
      places: [],
      currentPlace: null,
      options: {
        streetViewControl: false,
        fullscreenControl: false,
        mapTypeControl: false,
        zoomControl: false
      }
    }
  },
  created () {
    this.center = store.state.location
    this.getSpotList()
  },
  mounted () {
    this.setMapHeight()
  },
  methods: {
    // receives a place object via the autocomplete component
    setMapHeight: function () {
      let body = document.getElementsByClassName('hero-body')[0]
      let height = (body.clientHeight - 32) + 'px'
      let map = document.getElementById('gmap')
      map.style.height = height
    },
    setPlace (place) {
      this.currentPlace = place
    },
    addMarker () {
      if (this.currentPlace) {
        const marker = {
          lat: this.currentPlace.geometry.location.lat(),
          lng: this.currentPlace.geometry.location.lng()
        }
        this.markers.push({ position: marker })
        this.places.push(this.currentPlace)
        this.center = marker
        this.currentPlace = null
      }
    },
    getSpotList: function () {
      this.axios.get(consts.endpoint.spot, consts.addRequestHeader(store.state.session))
        .then((response) => {
          let spots = response.data
          let markers = []

          for (let spot of spots) {
            let marker = {
              id: spot.id,
              name: spot.name,
              lat: spot.lat,
              lng: spot.lng
            }
            markers.push({position: marker})
          }
          this.markers = markers
        })
        .catch((response) => {
          consts.errorHandler(response)
        })
    },
    navigateToSpot: function (id) {
      this.$router.push(consts.path.spots + id)
    }
  }
}
</script>

<style>
#map-container {
  width: 100%;
}
.map-view {
  width:100%;
}

</style>
