<template>
	<view>
		<view class="attr">
			<view class="border-outer">
				<view class="border-inner">
					<image :src="photo" mode="widthFix" class="photo"></image>
				</view>
			</view>
			<view class="list">
				<view class="item" @tap="editName">
					<view class="key">姓名</view>
					<view class="value">{{ name }}</view>
				</view>
				<view class="item">
					<view class="key">性别</view>
					<picker :value="genderIndex" :range="genderArray" @change="genderChange">{{ genderArray[genderIndex] }}</picker>
				</view>
				<view class="item" @tap="editTel">
					<view class="key">电话</view>
					<view class="value">{{ tel }}</view>
				</view>
				<view class="item" @tap="editEmail">
					<view class="key">邮箱</view>
					<view class="value">{{ email }}</view>
				</view>
				<view class="item">
					<view class="key">入职时间</view>
					<view class="value">{{ hiredate }}</view>
				</view>
				<view class="item">
					<view class="key">所属部门</view>
					<view class="value">{{ dept }}</view>
				</view>
			</view>
			<button class="btn" @tap="save">保存</button>
		</view>
		<uni-popup ref="popupName" type="dialog">
			<uni-popup-dialog mode="input" title="编辑文字内容" placeholder="输入姓名" :value="name" @confirm="finishName"></uni-popup-dialog>
		</uni-popup>
		<uni-popup ref="popupTel" type="dialog">
			<uni-popup-dialog mode="input" title="编辑文字内容" placeholder="输入电话" :value="tel" @confirm="finishTel"></uni-popup-dialog>
		</uni-popup>
		<uni-popup ref="popupEmail" type="dialog">
			<uni-popup-dialog mode="input" title="编辑文字内容" placeholder="输入邮箱" :value="email" @confirm="finishEmail"></uni-popup-dialog>
		</uni-popup>
	</view>
</template>

<script>
	import uniPopup from '@/components/uni-popup/uni-popup.vue';
	import uniPopupMessage from '@/components/uni-popup/uni-popup-message.vue';
	import uniPopupDialog from '@/components/uni-popup/uni-popup-dialog.vue';
	export default {
		components: {
			uniPopup,
			uniPopupMessage,
			uniPopupDialog
		},
		data() {
			return {
				photo: "",
				name: "",
				genderArray: ['男', '女'],
				genderIndex: 0,
				tel: "",
				email: "",
				hiredate: "",
				dept: ""
			}
		},
		onShow() {
			let that = this
			that.ajax(that.url.searchUserInfo, "POST", {id: that.id}, function(resp) {
				let result = resp.data.result
				that.photo = result.photo;
				that.name = result.name;
				that.tel = result.tel;
				that.email = result.email;
				that.genderIndex = result.sex == "男" ? 0 : 1;
				that.hiredate = result.hiredate;
				that.dept = result.dept;
			})
		},
		methods: {
			genderChange: function(e) {
				this.genderIndex = e.detail.value
			},
			editName: function() {
				this.$refs.popupName.open()
			},
			finishName: function(done, value) {
				if(value != null && value != ""){
					this.name = value
					done()
				} else {
					uni.showToast({
						icon: "none",
						title: "姓名"
					})
				}
			},
			editTel: function() {
				this.$refs.popupTel.open()
			},
			finishTel: function(done, value) {
				if(value != null && value != ""){
					if(this.checkValidTel(value, "电话")) {
						return
					}
					this.tel = value
					done()
				} else {
					uni.showToast({
						icon: "none",
						title: "电话"
					})
				}
			},
			editEmail: function() {
				this.$refs.popupEmail.open()
			},
			finishEmail: function(done, value) {
				let email = this.email
				let that = this
				if(value != null && value != ""){
					if(this.checkValidEmail(value, "邮箱")) {
						return
					}
					that.email = value
					done()
				} else {
					uni.showToast({
						icon: "none",
						title: "邮箱"
					})
				}
			},
			save: function() {
				let that = this
				if(
					that.checkBlank(that.name, "姓名") || 
					that.checkValidTel(that.tel, "电话") || 
					that.checkValidEmail(that.email, "邮箱")
				){
					return
				}	
				let data = {
					name: that.name,
					sex: Number(that.genderIndex) + 1,
					tel: that.tel,
					email: that.email,
				}
				if(that.genderIndex == "1") {
					data.gender = that.gender
				}
				let url;
				that.ajax(that.url.updateUserInfo, "POST", data, function(resp) {
					uni.showToast({
						icon: "success",
						title: "保存成功",
						complete: function() {
							setTimeout(function() {
								uni.navigateBack({
									
								})
							}, 2000)
						}
					})
				})
			},
		}
	}
</script>

<style lang="less">
	@import url("myInfo.less");
</style>
