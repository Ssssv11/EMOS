<template>
	<view>
		<image src="../../static/logo-2.png" mode="widthFix" class="register-img"></image>
		<view class="register-container">
			<input class="register-code" type="text" placeholder="输入邀请码" maxlength="6" v-model="registerCode" />
			<view class="register-desc">普通员工邀请码为 333333</view>
			<button class="register-btn" open-type="getUserInfo" @tap="register()">注册</button>
		</view>
		<view class="login-text">
			已有账号？
			<text class="login" @tap="toLogin()">登录</text>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				registerCode: ""
			}
		},
		methods: {
			toLogin: () => {
				uni.navigateTo({
					url: '/pages/login/login'
				})
			},
			register: function() {
				let that = this
				if (that.registerCode == null || that.registerCode.length == 0) {
					uni.showToast({
						icon: "none",
						title: "邀请码不能为空"
					})
					return
				} else if (/^[0-9]{6}$/.test(that.registerCode) == false) {
					uni.showToast({
						icon: "none",
						title: "邀请码为 6 位数字"
					})
					return
				}
				uni.login({
					provider: 'weixin',
					success: function(resp) {
						console.log(resp.code)
						let code = resp.code;
						uni.getUserInfo({
							provider: 'weixin',
							success: function(resp) {
								let nickName = resp.userInfo.nickName;
								let avatarUrl = resp.userInfo.avatarUrl;
								// console.log(nickName);
								// console.log(avatarUrl);
								let data = {
									code: code,
									nickname: nickName,
									photo: avatarUrl,
									registerCode: that.registerCode
								}
								that.ajax(that.url.register, "POST", data, function(resp) {
									let permission = resp.data.permission
									uni.setStorageSync("permission", permission)
									uni.switchTab({
										url: "/pages/index/index"
									})
								})
							}
						});
					}
				});
			}
		}
	}
</script>

<style lang="less">
	@import url("register.less");
</style>
