<template>
	<view class="page" v-if="checkPermission(['ROOT', 'MEETING:INSERT', 'MEETING:UPDATE'])">
		<view class="textarea-container">
			<textarea class="textarea" @blur="bindTextAreaBlur" placeholder="请输入消息" :value="text" confirm-type="done"></textarea>
		</view>
		<view class="members">
			<view class="number">接收者（{{ members.length }}人）</view>
			<view class="member">
				<view class="user" v-for="one in members" :key="one.id" @longpress="deleteMember(one.id)">
					<image :src="one.photo" mode="widthFix" class="photo"></image>
					<text class="name">{{one.name}}</text>
				</view>
				<view class="add">
					<image src="../../static/icon-19.png" mode="widthFix" 
					class="add-btn" @tap="toMembersPage()"></image>
				</view>
			</view>
		</view>
		<button class="btn" @tap="save">发送</button>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				members: [],
				text: "",
				respMems: []
			}
		},
		onShow() {
			let that = this
			let pages = getCurrentPages()
			let currPage = pages[pages.length - 1]
			if(!currPage.hasOwnProperty("finishMembers") || !currPage.finishMembers) {
			
			} else {
				let members = []
				for(let one of currPage.members){
					members.push(Number(one)) 
				}
				if(members.length != 0) {
					that.ajax(that.url.searchMembers, "POST", {members: JSON.stringify(members)}, function(resp) {
						let result = resp.data.result
						that.members = result
					})
				}
			}
		},
		methods: {
			toMembersPage: function() {
				let array = []
				for(let one of this.members){
					array.push(one.id)
				}
				uni.navigateTo({
					url:"../members/members?members=" + array.join(",")
				})
			},
			save: function() {
				let that = this
				for(let i = 0; i < that.members.length; i++) {
					that.respMems[i] = that.members[i].id
				}
				console.log(that.respMems)
				let data = {
					members: that.respMems,
					text: that.text
				}
				that.ajax(that.url.sendMessage, "POST", data, function(resp) {
					uni.showToast({
						icon: "success",
						title: "发送成功",
						complete: function() {
							setTimeout(function() {
								uni.navigateBack({
									
								})
							}, 2000)
						}
					})
				})
			},
			bindTextAreaBlur: function (e) {
				this.text = e.detail.value
			}
		}
	}
</script>

<style lang="less">
	@import url("sendMessage.less");
</style>
