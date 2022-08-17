<template>
	<view class="page">
		<swiper circular="true" interval="8000" duration="1000" class="swiper">
			<swiper-item>
				<image mode="widthFix"
					src="https://ssssv-1311247406.cos.ap-chengdu.myqcloud.com/img/banner/swiper-1.jpg"></image>
			</swiper-item>
			<swiper-item>
				<image mode="widthFix"
					src="https://ssssv-1311247406.cos.ap-chengdu.myqcloud.com/img/banner/swiper-2.jpg"></image>
			</swiper-item>
			<swiper-item>
				<image mode="widthFix"
					src="https://ssssv-1311247406.cos.ap-chengdu.myqcloud.com/img/banner/swiper-3.jpg"></image>
			</swiper-item>
			<swiper-item>
				<image mode="widthFix"
					src="https://ssssv-1311247406.cos.ap-chengdu.myqcloud.com/img/banner/swiper-4.jpg"></image>
			</swiper-item>
			<swiper-item>
				<image mode="widthFix"
					src="https://ssssv-1311247406.cos.ap-chengdu.myqcloud.com/img/banner/swiper-5.jpg"></image>
			</swiper-item>
		</swiper>
		<view class="notify-container">
			<view class="notify-title">
				<image src="../../static/icon-1.png" mode="widthFix" class="notify-icon"></image>
				消息提醒
			</view>
			<view class="notify-content">
				你有 {{ unreadRows }} 条未读消息
			</view>
			<image src="../../static/icon-2.png" mode="widthFix" class="more-icon"></image>
		</view>
		<view class="nav-container">
			<view class="nav-row">
				<view class="nav-col" @tap="toPage('在线签到', '../checkin/checkin')">
					<image src="../../static/nav-1.png" mode="widthFix" class="nav-icon"></image>
					<text class="nav-name">在线签到</text>
				</view>
				<view class="nav-col">
					<image src="../../static/nav-2.png" mode="widthFix" class="nav-icon"></image>
					<text class="nav-name">员工健康</text>
				</view>
				<view class="nav-col">
					<image src="../../static/nav-3.png" mode="widthFix" class="nav-icon"></image>
					<text class="nav-name">在线请假</text>
				</view>
				<view class="nav-col">
					<image src="../../static/nav-4.png" mode="widthFix" class="nav-icon"></image>
					<text class="nav-name">公务出差</text>
				</view>
			</view>
			<view class="nav-row">
				<view class="nav-col">
					<image src="../../static/nav-5.png" mode="widthFix" class="nav-icon"></image>
					<text class="nav-name">员工日报</text>
				</view>
				<view class="nav-col">
					<image src="../../static/nav-6.png" mode="widthFix" class="nav-icon"></image>
					<text class="nav-name">我的加班</text>
				</view>
				<view class="nav-col">
					<image src="../../static/nav-7.png" mode="widthFix" class="nav-icon"></image>
					<text class="nav-name">付款申请</text>
				</view>
				<view class="nav-col">
					<image src="../../static/nav-8.png" mode="widthFix" class="nav-icon"></image>
					<text class="nav-name">费用报销</text>
				</view>
			</view>
			<view class="nav-row">
				<view class="nav-col">
					<image src="../../static/nav-9.png" mode="widthFix" class="nav-icon"></image>
					<text class="nav-name">公告通知</text>
				</view>
				<view class="nav-col">
					<image src="../../static/nav-10.png" mode="widthFix" class="nav-icon"></image>
					<text class="nav-name">在线审批</text>
				</view>
				<view class="nav-col">
					<image src="../../static/nav-11.png" mode="widthFix" class="nav-icon"></image>
					<text class="nav-name">物品领用</text>
				</view>
				<view class="nav-col">
					<image src="../../static/nav-12.png" mode="widthFix" class="nav-icon"></image>
					<text class="nav-name">采购申请</text>
				</view>
			</view>
		</view>
		<uni-popup ref="popupMsg" type="top">
			<uni-popup-message type="success" :message="'接收到' + lastRows + '条消息'" :duration="2000"></uni-popup-message>
		</uni-popup>
	</view>
</template>

<script>
	import uniPopup from '@/components/uni-popup/uni-popup.vue';
	import uniPopupMessage from '@/components/uni-popup/uni-popup-message.vue';
	import uniPopupDialog from '@/components/uni-popup/uni-popup-dialog.vue';
	export default {
		components:{
			uniPopup,
			uniPopupMessage,
			uniPopupDialog,
		},
		data() {
			return {
				unreadRows: 0,
				lastRows: 0,
				timer: null
			}
		},
		onLoad() {
			let that = this
			uni.$on("showMessage", function() {
				that.$refs.popupMsg.open()
			})
		},
		onUnload() {
			uni.$off("showMessage")
		},
		onShow() {
			let that = this
			that.timer = setInterval(function() {
				that.ajax(that.url.refreshMessage, "GET", null, function(resp) {
					that.unreadRows = resp.data.unreadRows
					that.lastRows = resp.data.lastRows
					if(that.lastRows > 0) {
						uni.$emit("showMessage")
					}
				})
			}, 3000)
		},
		onHide() {
			let that = this
			clearInterval(that.timer)
		},
		methods: {
			toPage: function(name, url) {
				uni.navigateTo({
					url: url
				})
			}
		}
	}
</script>

<style lang="less">
	@import url("index.less");
</style>
