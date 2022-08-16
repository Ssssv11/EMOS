import App from './App'
import Vue from 'vue'

Vue.config.productionTip = false
App.mpType = 'app'

const app = new Vue({
    ...App
})

app.$mount()

let baseUrl = "http://localhost:8999/emos-wx-api"
Vue.prototype.url = {
	register: baseUrl + "/user/register",
	login: baseUrl + "/user/login"
}

Vue.prototype.ajax = (url, method, data, fun) => {
	uni.request({
		"url": url,
		"method": method,
		"header": {
			token: uni.getStorageSync('token')
		},
		"data": data,
		success: (resp) => {
			if(resp.statusCode == 401) {
				uni.redirectTo({
					url: 'pages/login/login.vue'
				})
			} else if(resp.statusCode == 200 && resp.data.code == 200) {
				let data = resp.data
				if(data.hasOwnProperty("token")) {
					let token = data.token
					uni.setStorageSync("token", token)
				}
				fun(resp)
			} else {
				uni.showToast({
					icon: "none",
					title: resp.data
				})
			}
		}
	})
}