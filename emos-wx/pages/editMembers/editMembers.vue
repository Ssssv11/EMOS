<template>
	<view class="page">
		<view class="add" v-if="checkPermission(['ROOT', 'MEETING:INSERT'])" 
		@tap="toInsertMemberPage()">
			<image src="../../static/icon-17.png" mode="widthFix" class="icon"></image>
			<text>添加员工</text>
		</view>
		<block v-for="dept in list" :key="dept.id">
			<view class="list-title">{{dept.deptName}}（{{ dept.count }}人）</view>
			<view class="item" v-for="member in dept.members" :key="member.userId">
				<view class="key">{{member.name}}</view>
				<view class="right" @tap="toEditMemberInfoPage(member.userId)"
				v-if="checkPermission(['ROOT', 'EMPLOYEE:UPDATE', 'DEPT:SELECT'])">
					<text>编辑</text>
				</view>
			</view>
		</block>
	</view>
</template>

<script>
export default {
	data() {
		return {
			list: [],
			members: []
		};
	},
	onShow() {
		this.loadData(this)
	},
	onLoad(options) {
		if(options.hasOwnProperty("members")) {
			let members = options.members
			this.members = members.split(",")
		}
	},
	methods: {
		loadData: function(ref) {
			ref.ajax(ref.url.searchUserGroupByDept, "POST", {keyword: ref.keyword}, function(resp) {
				let result = resp.data.result
				ref.list = result
			})
		},
		toEditMemberInfoPage: function(id) {
			uni.navigateTo({
				url: "../editMemberInfo/editMemberInfo?id=" + id
			})
		},
		toInsertMemberPage: function() {
			uni.navigateTo({
				url: "../insertMember/insertMember"
			})
		}
	}
};
</script>

<style lang = "less">
@import url('editMembers.less');
</style>
