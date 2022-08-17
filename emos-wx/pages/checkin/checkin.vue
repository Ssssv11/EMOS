 <template>
 	<view>
		<map 
			:latitude="latitude" 
			:longitude="longitude" 
			:scale="scale" 
			:show-compass="true" 
			:show-location="true" 
			class="map"
		>
		</map>
 		<view class="operate-container">
 			<button type="primary" class="btn" @tap="clickBtn" :disabled="!canCheckin">签到</button>
 		</view>
 		<view class="notice-container">
 			<text class="notice">注意事项</text>
 			<text class="desc">签到时必须要要打开定位，否则无法签到。另外，签到时尽量靠近工作地点以确保签到的准确度。</text>
 		</view>
 	</view>
 </template>
 
 <script>
 	var QQMapWX = require('../../lib/qqmap-wx-jssdk.min.js');
 	var qqmapsdk;
 	export default {
 		data() {
 			return {
				canCheckin: true,
				latitude: 39.909,
				longitude: 116.3974,
				scale: 12,
				photoPath: ''
 			}
 		},
 		onLoad: function() {
 			qqmapsdk = new QQMapWX({
 				key: "7C7BZ-2Z7K6-AL3ST-EHNKN-XKOP7-USBLO"
 			});

 		},
		
		onReady() {
			let vm = this
			vm.map = uni.createMapContext('map2', this)
			vm.getLocation()
		},
		
 		onShow: function() {
 			let that = this
 			that.ajax(that.url.validCheckIn, "GET", null, function(resp) {
 				let msg = resp.data.msg
				console.log(msg)
 				if(msg != "可以考勤") {
 					that.canCheckin = false
 					setTimeout(function() {
 						uni.showToast({
 							title: msg,
 							icon: "none"
 						});
 					}, 1000)
 				}
 			})
 		},
		
 		methods: {
			getLocation() {
				let that = this
			    uni.getLocation({
					type: 'gcj02',
					geocode: true,//获取城市具体地址
					success: (resp) => {
						that.latitude = resp.latitude
						that.longitude = resp.longitude
					},
			    })
			},
			
			clickBtn: function() {
				let that = this
				uni.showLoading({
					title: "签到中"
				})
				setTimeout(function() {
					uni.hideLoading()
				}, 3000)
					
				qqmapsdk.reverseGeocoder({
					location: {
						latitude: that.latitude,
						longitude: that.longitude
					},
					success: function(resp) {
						console.log(resp.result)
						let address = resp.result.address
						let addressComponent = resp.result.address_component
						let nation = addressComponent.nation;
						let province = addressComponent.province;
						let city = addressComponent.city;
						let district = addressComponent.district;
						
						uni.request({
							url: that.url.checkin,
							header: {
								token: uni.getStorageSync("token")
							},
							method: 'POST',
							data: {
								address: address,
								country: nation,
								province: province,
								city: city,
								district: district
							},
							success: function(resp){
								console.log("success")
								console.log(resp)
								if(resp.statusCode == 200){
									let data = resp.data
									let code = data.code
									let msg = data.msg
									if(code == 200 || msg == "success"){
										uni.hideLoading()
										uni.showToast({
											title: "签到成功",
											complete: function() {
												uni.navigateTo({
													url: '../checkinResult/checkinResult'
												});
											}
										})
									}
								}
								else if(resp.statusCode == 500){
									uni.showToast({
										title: resp.data,
										icon: "none"
									})
								}
							},
						}
					)}
				})
			}
		}
 	}
 </script>
 
 <style lang="less">
 	@import url("checkin.less");
 </style>
 