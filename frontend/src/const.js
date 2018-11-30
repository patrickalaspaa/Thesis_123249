const baseURL = 'localhost:8000/'
const home = 'localhost:8080'
const googleAPIKey = ''

export default {
  geocodeURL: 'https://maps.googleapis.com/maps/api/geocode/json?key=' + googleAPIKey + '&latlng=',
  APIKey: googleAPIKey,
  endpoint: {
    login: baseURL + 'login',
    obstacles: baseURL + 'obstacles',
    spot: baseURL + 'spot/',
    person: baseURL + 'person',
    checkUsername: baseURL + 'person/check-username',
    checkEmail: baseURL + 'person/check-email',
    validate: baseURL + 'authentication',
    loadTypes: baseURL + 'spot/types'
  },
  spotType: {
    'Street': 1,
    'Plaza': 2,
    'Skatepark': 3,
    'Trails': 4,
    'Dirt': 5
  },
  path: {
    editSpot: '/new/spot/',
    editUser: '/new/user/',
    login: '/',
    spots: '/spots/',
    spotRating: '/spots/ratings',
    map: '/map/'
  },
  errorHandler: function (e) {
    const statusCode = e.response.status
    if (statusCode !== undefined && statusCode === 401) {
      window.location.href = home
    } else {
      console.log(e.response)
    }
  },
  addRequestHeader: function (session) {
    return {
      headers: {
        session: session
      }
    }
  },
  convertStringToMap: function (cookie) {
    let cookieMap = {}
    const cookieItems = cookie.split(';')
    cookieItems.forEach(function (cookieItem) {
      const splitItem = cookieItem.split('=')
      let key = splitItem[0].trim()
      let value = (splitItem.length === 2) ? splitItem[1].trim() : ''
      cookieMap[key] = value
    })
    return cookieMap
  }
}
