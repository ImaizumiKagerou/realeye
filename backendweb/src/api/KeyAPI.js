import axios from "axios";
import store from "../store/index";

axios.interceptors.request.use(
    config => {
        // console.log(config)
        // 自定义header信息（比如token）
        // console.log("请求拦截器添加userId-----------",sessionStorage.userId)
        if (!config.headers['jwtToken']) {
            config.headers['jwtToken'] = localStorage.getItem("jwtToken");
        }
        // console.log(config)
        return config;
    }, function (error) {
        // 对请求错误做些什么
        return Promise.reject(error);
    }
);

export const getKeyList = (pageNum,pageSize,activeStatus,username,apikey) =>{
    return axios.get(
        store.state.base_url + "apikey/getList",
        {
            params: {
                pageNum,
                pageSize,
                username,
                activeStatus,
                apikey
            }
        }
    )
}

export const addKey = (username,expireTime)=>{
    return axios.get(
        store.state.base_url + "apikey/saveKey",
        {
            params:{
                username,
                expireTime
            }
        }
    )
}
