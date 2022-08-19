<template>
	<view class="page">
		<view class="add" v-if="checkPermission(['ROOT', 'MEETING:INSERT'])"
		@tap="toInsertRolePage()">
			<image src="../../static/icon-17.png" mode="widthFix" class="icon"></image>
			<text>添加权限</text>
		</view>
		<block v-for="dept in list" :key="dept.id">
			<view class="list-title">{{dept.deptName}}（{{ dept.count }}人）</view>
			<view class="item" v-for="member in dept.members" :key="member.userId">
				<view class="key">{{member.name}}</view>
				<view class="right" @tap="toRolePage(member.userId, member.role)"
				v-if="checkPermission(['ROOT', 'EMPLOYEE:UPDATE', 'DEPT:SELECT'])">
					<text>编辑权限</text>
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
			members: [],
			roleIndex: 0,
			roleList: [],
			roleId: null,
		};
	},
	onShow() {
		this.loadData(this)
	},
	methods: {
		loadData: function(ref) {
			ref.ajax(ref.url.searchUserRoleGroupByDept, "POST", {keyword: ref.keyword}, function(resp) {
				let result = resp.data.result
				ref.list = result
				console.log(ref.list)
			});
		},
		toRolePage: function(id, role) {
			uni.navigateTo({
				url: "../rolePage/rolePage?id=" + id + "&role=" + role
			})
		}
	}
};
</script>

<style lang = "less">
@import url('editRole.less');
</style>
