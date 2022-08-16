<template>
	<view>
		<image src="../../static/logo-1.png" class="logo" mode="widthFix"></image>
		<view class="logo-title">
			EMOS在线办公系统
		</view>
		<view class="log-subtitle">Version alpha-1.0</view>
		<button class="login-btn" open-type="getUserInfo" @tap="login()">登录</button>
		<view class="register-text">
			没有账号？
			<text class="register" @tap="toRegister()">注册</text>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {

			}
		},
		methods: {
			toRegister: () => {
				uni.navigateTo({
					url: '/pages/register/register'
				})
			},
			login: function() {
				let that = this
				uni.login({
					provider: "weixin",
					success: function(resp) {
						let code = resp.code
						that.ajax(that.url.login, "POST", {
							"code": code
						}, function(resp) {
							let permission = resp.data.permission
							uni.setStorageSync("permission", permission)
							uni.switchTab({
								url: "/pages/index/index"
							})
						})
					},
					fail: function(e) {
						uni.showToast({
							icon: "none",
							title: "执行异常"
						})
					}
				})
			}
		}
	}
</script>

<style lang="less">
	@import url("login.less");
</style>
