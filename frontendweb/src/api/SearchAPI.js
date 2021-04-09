import axios from "axios";
import store from "../store/index";

axios.interceptors.request.use(
    config => {
        // console.log(config)
        // 自定义header信息（比如token）
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

export const SearchAPIMethod = (pageNum,pageSize,keyword)=>{
    return axios.get(
        store.state.base_url+"search/main",
        {
            params:{
                pageNum,
                pageSize,
                keyword
            }
        }
    )
}

export const SearchAboutMethod = (stamp)=>{
    return axios.get(
        store.state.base_url+"search/stamp",
        {
            params:{
                stamp
            }
        }
    )
}
