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
					<picker mode="date" :value="hiredate" @change="hiredateChange">
						<view class="uni-input">{{ hiredate }}</view>
					</picker>
				</view>
				<view class="item">
					<view class="key">所属部门</view>
					<picker :value="deptIndex" :range="deptList" :range-key="'deptName'" @change="deptChange">{{ deptList[deptIndex].deptName }}</picker>
				</view>
			</view>
			<button class="btn" @tap="save">添加</button>
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
				photo: "https://ssssv-1311247406.cos.ap-chengdu.myqcloud.com/img/header/QQ20220818-170127%402x.png",
				name: "",
				genderArray: ['男', '女'],
				genderIndex: 0,
				tel: "",
				email: "",
				hiredate: "",
				dept: "",
				deptList: [],
				deptIndex: 0,
				deptId: null,
				reqDeptId: 6
			}
		},
		onLoad (options) {
			this.id = options.id
		},
		onShow() {
			let that = this
			that.ajax(that.url.getAllDept, "GET", null, function(resp) {
				that.deptList = resp.data.result
			})
			let now = new Date()
			now.setTime(now.getTime() + 30 * 60 * 1000)
			that.hiredate = now.format("yyyy-MM-dd")
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
			hiredateChange: function(e) {
				this.hiredate = e.detail.value
			},
			deptChange: function(e) {
				this.dept = e.detail.deptChange
				this.deptIndex = e.detail.value
				for(let i = 0; i < this.deptList.length; i++) {
					let one = this.deptList[i]
					if(i == this.deptIndex) {
						this.reqDeptId = one.deptId
					}
				}
			},
			
			save: function() {
				let that = this
				if(
					that.checkBlank(that.name, "姓名")
				){
					return
				}	
				let data = {
					name: that.name,
					sex: Number(that.genderIndex) + 1,
					tel: that.tel,
					email: that.email,
					hiredate: that.hiredate,
					deptId: that.reqDeptId,
				}
				if(that.genderIndex == "1") {
					data.gender = that.gender
				}
				that.ajax(that.url.insertMember, "POST", data, function(resp) {
					uni.showToast({
						icon: "success",
						title: "添加成功",
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
	@import url("insertMember.less");
</style>
