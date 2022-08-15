<template>
	<view>
		<image src="../../static/logo-2.png" mode="widthFix" class="register-img"></image>
		<view class="register-container">
			<input class="register-code" type="text" placeholder="输入邀请码" maxlength="6" v-model="registerCode"/>
			<view class="register-desc">HR创建员工账号后会向您发送注册邀请码邮件</view>
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
				
			}
		},
		methods: {
			toLogin: () => {
				uni.navigateTo({
					url:'/pages/login/login'
				})
			},
			register: () => {
				uni.login({
					provider:'weixin',
					success: (resp) => {
						let code = resp.code;
						uni.getUserInfo({
							success: (resp) => {
								let nickname = resp.userInfo.nickName;
								let avatarUrl = resp.userInfo.avatarUrl;
							}
						})
					}
				})
			}
		}
	}
</script>

<style lang="less">
	@import url("register.less");
</style>
