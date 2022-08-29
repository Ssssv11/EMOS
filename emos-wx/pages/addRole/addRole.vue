<template>
	<view>
		<view class="attr">
			<view class="header">
				<image src="../../static/icon-18.png" mode="widthFix" class="edit-icon"></image>
				<input type="text" class="title" v-model="title" placeholder="输入权限名称" placeholder-class="title-placeholder" />
			</view>
			<view class="list">
				<checkbox-group @change="checkboxChange">
					<label class="item" v-for="(item, index) in items" :key="item.id">
						<view>
							<checkbox :value="item.id" :disabled="item.id == 0"/>
						</view>
						<view>{{item.desc}}</view>
					</label>
				</checkbox-group>
			</view>
			<button class="btn" @tap="save">保存</button>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				items: [],
				roleId: null,
				role: "",
				roles: [],
				title: ""
			}
		},
		onLoad (options) {
			this.role = options.role
		},
		onShow() {
			this.loadData(this)
		},
		methods: {
			checkboxChange: function(e) {
				this.roles = e.detail.value
				console.log(this.roles)
			},
			loadData: function(ref) {
				ref.ajax(ref.url.searchAllPermission, "GET", null, function(resp) {
					ref.items = resp.data.result
					console.log(ref.items)
				})
			},
			save: function(resp) {
				let that = this
				if(that.checkBlank(that.title, "权限名称")) {
					return
				}
				let data = {
					roleName: that.title,
					permissions: that.roles
				}
				that.ajax(that.url.addRole, "POST", data, function(resp) {
					uni.showToast({
						icon: "success",
						title: "修改成功",
						complete: function() {
							setTimeout(function() {
								uni.navigateBack({
									
								})
							}, 2000)
						}
					})
				})
			}
		}
	}
</script>

<style lang="less">
	@import url("addRole.less");
</style>
