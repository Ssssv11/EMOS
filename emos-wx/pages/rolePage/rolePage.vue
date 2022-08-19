<template>
	<view>
		<view class="attr">
			<view class="title">权限列表</view>
			<view class="list">
				<radio-group @change="radioChange">
					<label class="item" v-for="(item, index) in items" :key="item.id">
						<view>
							<radio :value="item.id" :checked="index === current" :disabled="item.id == 0 || userId == 18"/>
						</view>
						<view>{{item.roleName}}</view>
					</label>
				</radio-group>
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
				current: 0,
				roleId: null,
				userId: null,
				role: ""
			}
		},
		onLoad (options) {
			this.userId = options.id
			this.role = options.role
		},
		onShow() {
			this.loadData(this)
		},
		methods: {
			radioChange: function(evt) {
				for (let i = 0; i < this.items.length; i++) {
					if (this.items[i].id == evt.detail.value) {
						this.current = i;
						this.roleId = this.items[i].id
						break;
					}
				}
			},
			loadData: function(ref) {
				ref.ajax(ref.url.selectRole, "GET", null, function(resp) {
					ref.items = resp.data.result
					console.log(ref.items)
					for(let i = 0; i < ref.items.length; i++) {
						if(ref.role == "[" + ref.items[i].id + "]") {
							ref.current = i
						}
					}
				})
			},
			save: function(resp) {
				let that = this
				let data = {
					userId: that.userId,
					role: that.roleId
				}
				console.log(that.role)
				that.ajax(that.url.updateUserRole, "POST", data, function(resp) {
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
	@import url("rolePage.less");
</style>
