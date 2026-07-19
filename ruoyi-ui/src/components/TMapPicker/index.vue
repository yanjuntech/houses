<template>
  <div class="tmap-picker">
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
      <div ref="mapContainer" class="tmap-container" style="width: 100%; height: 400px;"></div>
    </div>
  </div>
</template>

<script>
import { getConfigKey } from '@/api/system/config'

export default {
  name: 'TMapPicker',
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
      service: null,
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
      getConfigKey('sys.community.mapKeyTencent').then(response => {
        const key = response.msg || ''
        if (!key) {
          this.loading = false
          this.errorMsg = '腾讯地图 API Key 未配置'
          return
        }
        this.loadTMapScript(key)
      }).catch(() => {
        this.loading = false
        this.errorMsg = '获取地图配置失败'
      })
    },
    loadTMapScript(key) {
      if (window.TMap) {
        this.initMap()
        return
      }
      const script = document.createElement('script')
      script.src = 'https://map.qq.com/api/gljs?v=1.exp&key=' + key + '&libraries=service'
      script.onload = () => {
        this.initMap()
      }
      script.onerror = () => {
        this.loading = false
        this.errorMsg = '腾讯地图脚本加载失败，请检查网络或 API Key 配置'
      }
      document.head.appendChild(script)
    },
    initMap() {
      if (!window.TMap) {
        this.loading = false
        this.errorMsg = '地图初始化失败'
        return
      }
      const lng = this.longitude || 116.397428
      const lat = this.latitude || 39.90923
      const center = new TMap.LatLng(lat, lng)
      this.map = new TMap.Map(this.$refs.mapContainer, {
        zoom: 14,
        center: center
      })
      this.marker = new TMap.MultiMarker({
        map: this.map,
        geometries: [
          {
            id: 'picker',
            position: center
          }
        ]
      })
      if (TMap.service) {
        this.service = new TMap.service()
      }
      this.map.on('click', (e) => {
        const latLng = e.latLng
        if (!latLng) return
        const lat = latLng.getLat ? latLng.getLat() : latLng.lat
        const lng = latLng.getLng ? latLng.getLng() : latLng.lng
        this.marker.setGeometries([{ id: 'picker', position: new TMap.LatLng(lat, lng) }])
        this.reverseGeocode(lng, lat)
      })
      if (this.address) this.searchText = this.address
      this.loading = false
    },
    reverseGeocode(lng, lat) {
      if (!this.service) {
        this.$emit('select', { address: '', longitude: lng, latitude: lat })
        return
      }
      this.service
        .reverseGeocoder({ location: new TMap.LatLng(lat, lng) })
        .then((result) => {
          if (result && result.result && result.result.address) {
            const address = result.result.address
            this.searchText = address
            this.$emit('select', { address, longitude: lng, latitude: lat })
          } else {
            this.$emit('select', { address: '', longitude: lng, latitude: lat })
          }
        })
        .catch(() => {
          this.$emit('select', { address: '', longitude: lng, latitude: lat })
        })
    },
    querySearch(queryString, cb) {
      if (!queryString || !this.service) {
        cb([])
        return
      }
      this.service
        .suggestion({ keyword: queryString, region: '全国' })
        .then((result) => {
          if (result && result.data) {
            const suggestions = result.data
              .filter(t => t.location)
              .map(t => ({
                value: t.title + ' (' + t.address + ')',
                location: t.location,
                address: t.address + t.title
              }))
            cb(suggestions)
          } else {
            cb([])
          }
        })
        .catch(() => {
          cb([])
        })
    },
    handleSelect(item) {
      if (!item || !item.location) return
      const lng = item.location.lng
      const lat = item.location.lat
      const position = new TMap.LatLng(lat, lng)
      this.map.setCenter(position)
      this.marker.setGeometries([{ id: 'picker', position: position }])
      this.searchText = item.address
      this.$emit('select', { address: item.address, longitude: lng, latitude: lat })
    }
  }
}
</script>

<style scoped>
.tmap-picker {
  width: 100%;
}
.tmap-container {
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  overflow: hidden;
}
</style>
