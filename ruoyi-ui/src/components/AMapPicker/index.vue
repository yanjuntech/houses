<template>
  <div class="amap-picker">
    <el-autocomplete
      v-model="searchText"
      placeholder="请输入地址搜索"
      :fetch-suggestions="querySearch"
      @select="handleSelect"
      style="width: 100%; margin-bottom: 10px;"
    />
    <div ref="mapContainer" class="amap-container" style="width: 100%; height: 400px;"></div>
  </div>
</template>

<script>
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
      geocoder: null
    }
  },
  mounted() {
    this.loadAMapScript()
  },
  methods: {
    loadAMapScript() {
      if (window.AMap) {
        this.initMap()
        return
      }
      // eslint-disable-next-line no-console
      console.warn('[AMapPicker] 使用占位 API Key "YOUR_AMAP_KEY"，请替换为有效的高德地图 Key，否则地图无法加载。')
      const script = document.createElement('script')
      script.src = 'https://webapi.amap.com/maps?v=2.0&key=YOUR_AMAP_KEY&plugin=AMap.PlaceSearch,AMap.AutoComplete,AMap.Geocoder'
      script.onload = () => this.initMap()
      script.onerror = () => {
        // eslint-disable-next-line no-console
        console.error('[AMapPicker] 高德地图脚本加载失败，请检查网络或 API Key 配置。')
      }
      document.head.appendChild(script)
    },
    initMap() {
      if (!window.AMap) {
        // eslint-disable-next-line no-console
        console.error('[AMapPicker] window.AMap 未定义，初始化失败。')
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
      // 点击地图事件
      this.map.on('click', (e) => {
        const lnglat = e.lnglat
        this.marker.setPosition([lnglat.lng, lnglat.lat])
        this.reverseGeocode(lnglat.lng, lnglat.lat)
      })
      // 拖拽 marker 事件
      this.marker.on('dragend', (e) => {
        const lnglat = e.lnglat
        this.reverseGeocode(lnglat.lng, lnglat.lat)
      })
      // 初始 address 回填到搜索框
      if (this.address) this.searchText = this.address
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
