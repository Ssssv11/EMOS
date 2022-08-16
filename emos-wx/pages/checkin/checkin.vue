<template>
	<view>
		<camera device-position="front" flash="auto" @error="error" class="camera" v-if="showCamera"></camera>
		<image :src="photoPath" mode="widthFix" class="image" v-if="showImage"></image>
		<view class="operate-container">
			<button type="primary" class="btn" @tap="clickBtn" :disabled="canCheckin">{{ btnText }}</button>
			<button type="warn" class="btn" @tap="afresh" :disabled="canCheckin">重拍</button>
		</view>
		<view class="notice-container">
			<text class="notice">注意事项</text>
			<text class="desc">拍照签到时必须要拍摄正面照片，侧面照片会导致无法识别。另外，拍照时请勿佩戴墨镜或帽子，避免影响拍照签到的准确度。</text>
		</view>
	</view>
</template>

<script>
	var QQMapWX = require('../../lib/qqmap-wx-jssdk.min.js');
	var qqmapsdk;
	export default {
		data() {
			return {
				canCheckin: false,
				photoPath: '',
				btnText: '拍照',
				showCamera: true,
				showImage: false
			}
		},
		onLoad: function() {
			qqmapsdk = new QQMapWX({
				key: "7C7BZ-2Z7K6-AL3ST-EHNKN-XKOP7-USBLO"
			})
		},
		methods: {
			clickBtn: function() {
				let that = this
				if (that.btnText == '拍照') {
					let ctx = uni.createCameraContext();
					ctx.takePhoto({
						quality: "high",
						success: function(resp) {
							console.log(resp.tempImagePath)
							that.photoPath = resp.tempImagePath
							that.showCamera = false
							that.showImage = true
							that.btnText = '签到'
						}
					})
				} else {
					uni.showLoading({
						title: "签到中"
					})
					setTimeout(function() {
						uni.hideLoading()
					}, 3000)
					uni.getLocation({
						type: "wgs84",

						success: function(res) {
							let latitude = res.latitude
							let longitude = res.longitude
							console.log(latitude)
							console.log(longitude)
							console.log("cg")
							qqmapsdk.reverseGeocoder({
								location: {
									latitude: latitude,
									longitude: longitude
								},
								success: function(resp) {
									console.log(resp.result)
									let address = resp.result.address
									let addressComponent = resp.result.address_component
									let nation = addressComponent.nation;
									let province = addressComponent.province;
									let city = addressComponent.city;
									let district = addressComponent.district;
								},
								fail: function(res) {
									console.log('fail', res)
								}
							})
						}
					})
				}
			},
			afresh: function() {
				let that = this
				that.showCamera = true
				that.showImage = false
				that.btnText = '拍照'
			}
		}
	}
</script>

<style lang="less">
	@import url("checkin.less");
</style>
