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
					src="https://ssssv-1311247406.cos.ap-chengdu.myqcloud.com/img/banner/swiper-4.jpg"></image>
			</swiper-item>
		</swiper>
		<view class="notify-container" @tap="toPage('消息提醒', '/pages/messageList/messageList')">
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
				<view class="nav-col" @tap="toPage('会议列表', '../meetingList/meetingList')">
					<image src="../../static/nav-5.png" mode="widthFix" class="nav-icon"></image>
					<text class="nav-name">会议列表</text>
				</view>
				<view class="nav-col" @tap="toPage('我的考勤', '../myCheckin/myCheckin')">
					<image src="../../static/nav-6.png" mode="widthFix" class="nav-icon"></image>
					<text class="nav-name">我的考勤</text>
				</view>
				<view class="nav-col" @tap="toPage('发送通知', '../sendMessage/sendMessage')">
					<image src="../../static/nav-9.png" mode="widthFix" class="nav-icon"></image>
					<text class="nav-name">发送通知</text>
				</view>
			</view>
		</view>
		<view class="calendar-container">
			<uni-calendar :insert="true" :lunar="true" :selected="calendar" 
					@monthSwitch="changeMonth" />
		</view>
		<view class="meeting-container" v-for="one in meetingList" :key="one.id">
			<view class="meeting">
				<view class="row">
					<text class="title">{{one.title}}</text>
					<text class="hours">时长：{{ one.hour == 0 ? 1 : one.hour }}小时</text>
				</view>
				<view class="row">
					<image src="../../static/icon-3.png" mode="widthFix" class="icon"></image>
					<text class="desc" space="emsp">日期：{{ one.date }} {{ one.start }}</text>
				</view>
				<view class="row">
					<image src="../../static/icon-4.png" mode="widthFix" class="icon"></image>
					<text class="desc">地点：{{ one.type == '线上会议' ? one.type : one.place }}</text>
				</view>
				<image :src="one.photo" class="photo"></image>
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
	import uniCalendar from '@/components/uni-calendar/uni-calendar.vue';
	export default {
		components:{
			uniPopup,
			uniPopupMessage,
			uniPopupDialog,
			uniCalendar,
		},
		data() {
			return {
				unreadRows: 0,
				lastRows: 0,
				timer: null,
				calendar: [],
				meetingPage: 1,
				meetingLength: 20,
				meetingList: [],
				isMeetingLastPage: false
			}
		},
		onLoad() {
			let that = this
			uni.$on("showMessage", function() {
				that.$refs.popupMsg.open()
			})
			that.ajax(that.url.refreshMessage, "GET", null, function(resp) {
				that.unreadRows = resp.data.unreadRows
				that.lastRows = resp.data.lastRows
				if(that.lastRows > 0) {
					uni.$emit("showMessage")
				}
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
			that.loadMeetingList(that)
			let date = new Date()
			that.loadMeetingInMonth(that,date.getFullYear(), date.getMonth() + 1)
		},
		onHide() {
			let that = this
			clearInterval(that.timer)
		},
		onReachBottom: function() {
			if(this.isMeetingLastPage) {
				return
			}
			this.meetingPage = this.meetingPage + 1
			this.loadMeetingList(this)
		},
		methods: {
			toPage: function(name, url) {
				uni.navigateTo({
					url: url
				})
			},
			loadMeetingList: function(ref) {
				let data = {
					page: ref.meetingPage,
					length: ref.meetingLength
				}
				ref.ajax(ref.url.searchMyMeetingListByPage, "POST", data, function(resp) {
					let result = resp.data.result
					if(result == null || result.length == 0) {
						ref.isMeetingLastPage = true
						ref.meetingPage = ref.meetingPage - 1
						if(ref.meetingPage > 1) {
							uni.showToast({
								icon: 'none',
								title: '已经到底了'
							});
						}
					}
					else{
						let list = []
						for(let one of result) {
							for(let meeting of one.list) {
								if(meeting.type == 1){
									meeting.type = "线上会议"
								}
								else if(meeting.type == 2) {
									meeting.type="线下会议"
								}
								if(meeting.status == 3){
									meeting.status = '未开始';
								}
								else if(meeting.status == 4) {
									meeting.status = '进行中';
								}
								list.push(meeting)
							}
						}
						ref.meetingList = list
					}
				})
			},
			loadMeetingInMonth: function(ref, year, month) {
				let data = {
					year: year,
					month: month
				}
				ref.ajax(ref.url.searchUserMeetingInMonth, "POST", data, function(resp) {
					ref.calendar = []
					for(let one of resp.data.result) {
						ref.calendar.push({date: one, info: "会议"})
					}
				})
			},
			changeMonth: function(e) {
				let that = this
				let year = e.year
				let month = e.month
				that.loadMeetingInMonth(that, year, month)
			}
		}
	}
</script>

<style lang="less">
	@import url("index.less");
</style>
