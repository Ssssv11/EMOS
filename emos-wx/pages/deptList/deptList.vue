<template>
	<view class="page">
		<view class="add" v-if="checkPermission(['ROOT', 'MEETING:INSERT'])" 
		@tap="insertDept">
			<image src="../../static/icon-17.png" mode="widthFix" class="icon"></image>
			<text>添加部门</text>
		</view>
		<view class="block"></view>
		<block>
			<view class="item"  v-for="dept in list" :key="dept.deptId" @longpress="delDept(dept.deptId)">
				<view class="key">{{dept.deptName}}</view>
				<view class="right" @tap="editDept(dept.deptId)"
				v-if="checkPermission(['ROOT', 'DEPT:UPDATE'])">
					<text>编辑</text>
				</view>
			</view>
		</block>
		<uni-popup ref="popupUpdateDept" type="dialog">
			<uni-popup-dialog mode="input" title="编辑文字内容" placeholder="输入部门名" @confirm="finishUpdateDept"></uni-popup-dialog>
		</uni-popup>
		<uni-popup ref="popupInsertDept" type="dialog">
			<uni-popup-dialog mode="input" title="编辑文字内容" placeholder="输入部门名" @confirm="finishInsertDept"></uni-popup-dialog>
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
			list: [],
			deptName: "",
			deptId: null,
		};
	},
	onShow() {
		this.loadData(this)
	},
	methods: {
		loadData: function(resp) {
			let that = this
			that.ajax(that.url.getAllDept, "GET", null, function(resp) {
				that.list = resp.data.result
				console.log(that.list)
			})
		},
		editDept: function(id) {
			this.deptId = id
			this.$refs.popupUpdateDept.open()
		},
		finishUpdateDept: function(done, value) {
			if(value != null && value != ""){
				this.deptName = value
				let data = {
					deptId: this.deptId,
					deptName: this.deptName
				}
				this.ajax(this.url.updateDeptById, "POST", data, function(resp) {
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
				done()
			} else {
				uni.showToast({
					icon: "none",
					title: "部门"
				})
			}
		},
		insertDept: function() {
			this.$refs.popupInsertDept.open()
		},
		finishInsertDept: function(done, value) {
			if(value != null && value != ""){
				this.deptName = value
				this.ajax(this.url.insertDept, "POST", {deptName: this.deptName}, function(resp) {
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
				done()
			} else {
				uni.showToast({
					icon: "none",
					title: "部门"
				})
			}
		},
		delDept: function(id, value) {
			let that = this
			uni.vibrateShort({})
			uni.showModal({
				title:"提示信息",
				content:"是否删除这个部门？",
				success: function(resp) {
					if(resp.confirm) {
						let data={
							deptId: id
						}
						that.ajax(that.url.deleteDept, "POST", data, function(resp) {
							uni.showToast({
								icon:"success",
								title:"删除成功",
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
			})
		}
	}
};
</script>

<style lang = "less">
@import url('dept.less');
</style>
