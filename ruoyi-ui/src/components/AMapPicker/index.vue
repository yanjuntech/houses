<template>
  <div class="amap-picker">
    <el-alert
      v-if="errorMsg"
      :title="errorMsg"
      type="error"
      :closable="false"
      show-icon
      style="margin-bottom: 10px;"
    >
      <span slot="desc">请在系统参数中配置地图 API Key</span>
    </el-alert>
    <div v-loading="loading" element-loading-text="地图加载中..." style="width: 100%;">
      <el-autocomplete
        v-model="searchText"
        placeholder="请输入地址搜索"
        :fetch-suggestions="querySearch"
        @select="handleSelect"
        style="width: 100%; margin-bottom: 10px;"
      />
      <div ref="mapContainer" class="amap-container" style="width: 100%; height: 400px;"></div>
    </div>
  </div>
</template>

<script>
import { getConfigKey } from '@/api/system/config'

export default {
  name: 'AMapPicker',
  props: {
    longitude: { type: Number, default: null },
    latitude: { type: Number, default: null },
    address: { type: String, default: '' }
  },
  data() {
    return {
      searchText: '',
      map: null,
      marker: null,
      placeSearch: null,
      autoComplete: null,
      geocoder: null,
      loading: false,
      errorMsg: ''
    }
  },
  mounted() {
    this.loadMapKey()
  },
  methods: {
    loadMapKey() {
      this.loading = true
      this.errorMsg = ''
      getConfigKey('sys.community.mapKeyAmap').then(response => {
        const key = response.msg || ''
        if (!key) {
          this.loading = false
          this.errorMsg = '高德地图 API Key 未配置'
          return
        }
        this.loadAMapScript(key)
      }).catch(() => {
        this.loading = false
        this.errorMsg = '获取地图配置失败'
      })
    },
    loadAMapScript(key) {
      if (window.AMap) {
        this.initMap()
        return
      }
      const script = document.createElement('script')
      script.src = 'https://webapi.amap.com/maps?v=2.0&key=' + key + '&plugin=AMap.PlaceSearch,AMap.AutoComplete,AMap.Geocoder'
      script.onload = () => {
        this.initMap()
      }
      script.onerror = () => {
        this.loading = false
        this.errorMsg = '高德地图脚本加载失败，请检查网络或 API Key 配置'
      }
      document.head.appendChild(script)
    },
    initMap() {
      if (!window.AMap) {
        this.loading = false
        this.errorMsg = '地图初始化失败'
        return
      }
      const lng = this.longitude || 116.397428
      const lat = this.latitude || 39.90923
      this.map = new AMap.Map(this.$refs.mapContainer, {
        zoom: 14,
        center: [lng, lat]
      })
      this.marker = new AMap.Marker({
        map: this.map,
        position: [lng, lat],
        draggable: true
      })
      this.geocoder = new AMap.Geocoder()
      this.autoComplete = new AMap.AutoComplete({ input: null })
      this.map.on('click', (e) => {
        const lnglat = e.lnglat
        this.marker.setPosition([lnglat.lng, lnglat.lat])
        this.reverseGeocode(lnglat.lng, lnglat.lat)
      })
      this.marker.on('dragend', (e) => {
        const lnglat = e.lnglat
        this.reverseGeocode(lnglat.lng, lnglat.lat)
      })
      if (this.address) this.searchText = this.address
      this.loading = false
    },
    reverseGeocode(lng, lat) {
      if (!this.geocoder) {
        this.$emit('select', { address: '', longitude: lng, latitude: lat })
        return
      }
      this.geocoder.getAddress([lng, lat], (status, result) => {
        if (status === 'complete' && result.info === 'OK') {
          const address = result.regeocode.formattedAddress
          this.searchText = address
          this.$emit('select', { address, longitude: lng, latitude: lat })
        } else {
          this.$emit('select', { address: '', longitude: lng, latitude: lat })
        }
      })
    },
    querySearch(queryString, cb) {
      if (!queryString || !this.autoComplete) {
        cb([])
        return
      }
      this.autoComplete.search(queryString, (status, result) => {
        if (status === 'complete' && result.tips) {
          const suggestions = result.tips
            .filter(t => t.location)
            .map(t => ({
              value: t.name + ' (' + t.district + ')',
              location: t.location,
              address: t.district + t.name
            }))
          cb(suggestions)
        } else {
          cb([])
        }
      })
    },
    handleSelect(item) {
      if (!item || !item.location) return
      const lng = item.location.lng
      const lat = item.location.lat
      this.map.setCenter([lng, lat])
      this.marker.setPosition([lng, lat])
      this.searchText = item.address
      this.$emit('select', { address: item.address, longitude: lng, latitude: lat })
    }
  }
}
</script>

<style scoped>
.amap-picker {
  width: 100%;
}
.amap-container {
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  overflow: hidden;
}
</style>
