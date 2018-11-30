<template>
  <div class="container">
    <div class="card">
      <div class="card-header">
    <p class="card-header-title is-centered">{{ title }}</p>
  </div>
  <div class="card-content"  v-if="submit">
    <div class="columns is-centered is-multiline">
      <div class="column is-1">
        <Stretch :background="spinnerColor"></Stretch>
      </div>
    </div>
  </div>
  <div class="card-content" v-else>
    <div class="columns is-centered is-multiline">
      <div class="column is-7">
        <div class="field">
        <label class="label">Search address</label>
        <!-- Add warning for missing geolocation  -->
        <div class="field has-addons">
          <div class="control is-expanded">
            <gmap-autocomplete class="input"
              @place_changed="setPlace">
            </gmap-autocomplete>
          </div>
          <div class="control">
            <a class="button is-info" @click="moveMarker">Search</a>
          </div>
        </div>
      </div>
      </div>
      <div class="column is-7">
        <gmap-map v-bind:center="center" v-bind:zoom="15" class="map" v-bind:options="options">
          <gmap-marker
            v-bind:key="marker.length"
            v-bind:position="marker.position"
            v-bind:draggable="true"
            @dragend="updateMarkerPosition"
          ></gmap-marker>
        </gmap-map>
      </div>
      <div class="column is-7">
        <div class="field">
          <label class="label" for="name">Spot Name</label>
          <input class="input" v-model="name" type="text" placeholder="Add Name" id="name" name="name" @click="removeElementHighlight($event.target, 'is-danger')"/>
        </div>
      </div>
      <div class="column is-7">
        <div class="field">
          <div id="file-input" class="file">
            <label class="file-label">
              <input class="file-input" type="file" id="spot-photo" name="spot-photo" accept="image/*" @change="uploadFile" @click="removeElementHighlight($event.target.parentNode.parentNode, 'is-danger')"/>
              <span class="file-cta">
                <span class="file-icon">
                  <font-awesome-icon icon="upload"/>
                </span>
                <span v-if="!uploading" class="file-label">
                  Choose Image
                </span>
                <Stretch v-else :background="spinnerColor"></Stretch>
              </span>
            </label>
          </div>
        </div>
        <div class="card" v-if="loadedPhotos.length > 0">
          <div class="card-content">
            <div class="tags">
              <span class="tag is-primary" v-for="photo in loadedPhotos" v-bind:key="photo.id">{{ extractName(photo.url) }}
                <!-- <button class="delete" @click="removePhoto(photo)"></button> -->
              </span>
            </div>
          </div>
        </div>
        <div class="card" v-if="photos.length > 0">
          <div class="card-content">
            <div class="tags">
              <span class="tag is-primary" v-for="photo in photos" v-bind:key="photo.id">{{ photo.name }}
                <button class="delete" @click="removePhoto(photo)"></button>
              </span>
            </div>
          </div>
        </div>
      </div>
      <div class="column is-7">
        <div class="field">
          <label class="label" for="type">Spot Type</label>
          <div class="select is-fullwidth">
            <select id="type" name="type" v-model="type"  @click="removeElementHighlight($event.target.parentNode, 'is-danger')">
              <option value="">Select Spot Type</option>
              <option v-for="type in types" v-bind:key="type.id" :value="type.id">
                {{ type.type }}
              </option>
            </select>
          </div>
        </div>
      </div>
      <div class="column is-7">
        <div id="obstacle-card" class="card">
          <div class="card-content">
            <div class="tags">
              <span class="tag is-light-gray" v-for="obs in obstacles"
                    v-bind:key="obs.id" v-bind:id="obs.id" @click="removeElementHighlight($event.target.parentNode.parentNode.parentNode, 'card-error'); addObstacle(obs.id)">
                {{ obs.obstacle }}
              </span>
            </div>
          </div>
        </div>
      </div>
      <div class="column is-7">
        <input id="id" v-model="id" class="hidden" value="-1"/>
      </div>
      </div>
      </div>
      <div class="card-footer">
        <div class="card-footer-item">
            <a class="button is-link is-fullwidth" @click="postSpot">
              <p>Submit</p>
            </a>
        </div>
        <div class="card-footer-item">
            <a class="button is-light is-fullwidth" >
              <p>Cancel</p>
            </a>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { store } from '../store'
import { Stretch } from 'vue-loading-spinner'
var consts = require('../const.js').default

export default {
  name: 'spotMap',
  components: {
    Stretch
  },
  data () {
    return {
      center: { lat: 60.155276, lng: 24.9240902 },
      types: {},
      marker: {},
      currentPlace: null,
      options: {
        streetViewControl: false,
        fullscreenControl: false,
        mapTypeControl: false,
        zoomControl: false
      },
      obstacles: [],
      checked: [],
      photos: [],
      loadedPhotos: [],
      name: '',
      type: '',
      id: -1,
      address: '',
      submit: false,
      uploading: false,
      spinnerColor: '#007AC9',
      title: 'Create Spot'
    }
  },
  created () {
    this.getObstacles()
    this.loadSpotTypes()
    let spotId = this.$route.params.id
    if (spotId) {
      // load data
      this.loadImages(spotId)
      this.loadSpotData(spotId)
    } else {
      this.center = store.state.location
      this.marker = {position: store.state.location}
    }
  },
  watch: {
    obstacles: function () {
      this.axios.get(consts.endpoint.spot + this.$route.params.id + '/obsid', consts.addRequestHeader(store.state.session))
        .then((response) => {
          let loadedObs = response.data
          loadedObs.forEach(
            (object) => {
              let id = object.obstacleid
              let element = document.getElementById(id)
              this.checked.push(id)
              element.classList.add('is-info')
              element.classList.remove('is-light-gray')
            }
          )
        })
        .catch((response) => {
          consts.errorHandler(response)
        })
    }
  },
  methods: {
    loadSpotData (spotId) {
      this.axios.get(consts.endpoint.spot + spotId, consts.addRequestHeader(store.state.session))
        .then((response) => {
          let data = response.data[0]
          this.id = data.id
          this.name = data.name
          this.address = data.address
          this.type = consts.spotType[data.type]
          this.center = {lat: data.lat, lng: data.lng}
          this.marker = {
            position: {
              lat: data.lat,
              lng: data.lng
            }
          }
          this.reverseGeocode()
        })
        .catch((response) => {
          consts.errorHandler(response)
        })
    },
    loadSpotObstacles (spotId) {
      this.axios.get(consts.endpoint.spot + spotId + '/obsid', consts.addRequestHeader(store.state.session))
        .then((response) => {
          this.loadedObs = response.data
        })
        .catch((response) => {
          consts.errorHandler(response)
        })
    },
    loadSpotTypes: function () {
      this.axios.get(consts.endpoint.loadTypes)
        .then((response) => {
          this.types = response.data
        })
        .catch((error) => {
          consts.errorHandler(error)
        })
    },
    loadImages: function (id) {
      this.axios.get(consts.endpoint.spot + id + '/photos', consts.addRequestHeader(store.state.session))
        .then((response) => {
          this.loadedPhotos = response.data
        })
        .catch((response) => {
          consts.errorHandler(response)
        })
    },
    extractName: function (url) {
      return url.substr(url.lastIndexOf('/') + 1)
    },
    setPlace (place) {
      this.currentPlace = place
      this.address = this.currentPlace.formatted_address
      this.moveMarker()
    },
    moveMarker () {
      if (this.currentPlace) {
        const marker = {
          lat: this.currentPlace.geometry.location.lat(),
          lng: this.currentPlace.geometry.location.lng()
        }
        this.marker = { position: marker }
        this.center = marker
      }
    },
    updateMarkerPosition: function (location) {
      this.marker.position.lat = location.latLng.lat()
      this.marker.position.lng = location.latLng.lng()

      this.reverseGeocode()
    },
    reverseGeocode: function () {
      let coords = this.marker.position.lat + ',' + this.marker.position.lng
      this.axios.get(consts.geocodeURL + coords)
        .then((response) => {
          this.currentPlace = response.data.results[0]
          this.address = this.currentPlace.formatted_address
        })
        .catch((response) => {
          consts.errorHandler(response)
        })
    },
    getObstacles: async function () {
      try {
        let response = await this.axios.get(consts.endpoint.obstacles, consts.addRequestHeader(store.state.session))
        this.obstacles = response.data
      } catch (error) {
        consts.errorHandler(error)
      }
    },
    postSpot: function () {
      if (this.formIsValid()) {
        // error
        let location = this.currentPlace.geometry.location
        let postData = {
          spot: {
            id: this.id,
            name: this.name,
            address: this.address,
            lat: location.lat,
            lng: location.lng,
            type: this.type
          },
          obstacles: this.checked,
          photos: this.photos
        }
        this.title = 'Loading'
        this.submit = true
        this.axios.post(consts.endpoint.spot, postData, consts.addRequestHeader(store.state.session))
          .then((response) => {
            this.$router.push(consts.path.spots + response.data.spot)
          })
          .catch((response) => {
            this.title = 'Create Spot'
            this.submit = false
            consts.errorHandler(response)
          })
      }
    },
    addObstacle: function (id) {
      let element = document.getElementById(id)
      // Bug: Something to do with ajax maybe
      if (this.checked.includes(id)) {
        let index = this.checked.indexOf(id)
        if (index > -1) {
          this.checked.splice(index, 1)
          element.classList.add('is-light-gray')
          element.classList.remove('is-info')
        }
      } else {
        this.checked.push(id)
        element.classList.add('is-info')
        element.classList.remove('is-light-gray')
      }
    },
    uploadFile: function (element) {
      let data = new FormData()
      let file = document.getElementById('spot-photo').files
      data.append('spot-photo', file[0], file[0].fileName)
      const header = {
        headers: {
          'accept': 'application/json',
          'Accept-Language': 'en-US,en;q=0.8',
          'Content-Type': `multipart/form-data; boundary=${data._boundary}`,
          session: store.state.session
        }
      }
      file.value = ''
      this.uploading = true
      this.axios.put(consts.endpoint.spot, data, header)
        .then((response) => {
          let photo = {
            id: response.data.id,
            name: response.data.name,
            extension: response.data.extension
          }
          this.photos.push(photo)
          this.uploading = false
        })
        .catch((response) => {
          consts.errorHandler(response)
          this.uploading = false
        })
    },
    removePhoto: function (photo) {
      let index = this.photos.indexOf(photo)
      if (index > -1) {
        this.photos.splice(index, 1)
      }
    },
    inputError: function (element) {
      element.classList.add('is-danger')
    },
    formIsValid: function () {
      let isValid = true

      if (this.checked.length < 1) {
        document.getElementById('obstacle-card').classList.add('card-error')
        isValid = false
      }
      if (this.photos.length < 1 && this.loadedPhotos.length < 1) {
        let fileElement = document.getElementById('file-input')
        this.inputError(fileElement)
        isValid = false
      }
      if (!this.name) {
        let nameElement = document.getElementById('name')
        this.inputError(nameElement)
        isValid = false
      }
      if (!this.address || (!this.marker.position.lat && !this.marker.position.lng)) {
        console.log('Cannot resolve geolocation')
        isValid = false
      }
      if (!this.type) {
        let typeElement = document.getElementById('type')
        this.inputError(typeElement.parentNode)
        isValid = false
      }
      return isValid
    },
    removeElementHighlight: function (element, className) {
      element.classList.remove(className)
    }
  }
}
</script>

<style>
.map {
  width: 100%;
  height: 400px;
}
.create-form-bottom {
  margin-top: 100px;
}
.li-cbox{
  list-style: none;
}
.div-cbox {
  width: 50%;
  margin-left: 25%;
}
.button {
  display: block;
}
.tag {
  cursor: pointer;
}
.is-light-gray {
  background-color: #e4e4e4 !important;
}
.card-error {
  border: solid 1px red;
}
</style>
