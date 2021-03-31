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

export const loginMethod = (username, password) => {
    return axios.get(store.state.base_url + "admin/login",
        {
            params: {
                username,
                password
            }
        });
}

export const getUserManageData = (pageNum, pageSize, activeStatus, username) => {
    return axios.get(
        store.state.base_url + "user/getList",
        {
            params: {
                pageNum,
                pageSize,
                activeStatus,
                username
            }
        }
    )
}

export const changeActiveStatusById = (id)=>{
    axios.get(
        store.state.base_url + "user/changeActiveStatus?id="+id
    )
}
