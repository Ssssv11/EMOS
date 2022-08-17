<template>
	<view>
		会议
	</view>
</template>

<script>
	export default {
		data() {
			return {
				page: 1,
				length: 20,
				list: [
					{
						date: '2022年08月18日',
						list: [
							{ id: 1, date: '2022/08/18', start: '08:30', end: '10:30', type: '线上会议', name: 'Ninn', place: '网络会议室', status: '未开始',title:'年终大会', desc: '年终总结大会' },
							{ id: 2, date: '2021/08/08', start: '08:30', end: '10:30', type: '线上会议', name: 'Ninn', place: '网络会议室', status: '未开始',title:'年终大会', desc: '年终总结大会' }
						]
					},
					{
						date: '2021年08月19日',
						list: [{ id: 3, date: '2022/08/19', start: '08:30', end: '10:30', type: '线上会议', name: 'Ninn', place: '网络会议室', status: '未开始', title:'年终大会',desc: '年终总结大会' }]
					}
				],
				isLastPage: false
			}
		},
		methods: {
			
		}
	}
</script>

<style lang="less">
	@import url("meetingList.less");
</style>
