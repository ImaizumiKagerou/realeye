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

export const GetCommunityData = (pageNum, pageSize, activeStatus)=>{
    return axios.get(
        store.state.base_url + "community/getList",
        {
            params: {
                pageNum,
                pageSize,
                activeStatus
            }
        }
    )
}

export const ChangeActive = (id)=>{
    return axios.get(
        store.state.base_url + "community/changeActive",
        {
            params: {
                id
            }
        }
    )
}

export const AddPrimeArticle = (title,content,preview) => {
    return axios.get(
        store.state.base_url + "communityPrime/addPrime",
        {
            params: {
                title,
                preview,
                content
            }
        }
    )
}

export const GetPrimeArticleData = (pageNum, pageSize, activeStatus)=>{
    return axios.get(
        store.state.base_url + "communityPrime/getList",
        {
            params: {
                pageNum,
                pageSize,
                activeStatus
            }
        }
    )
}

export const ChangePrimeActive = (id)=>{
    return axios.get(
        store.state.base_url + "communityPrime/changeActiveStatus",
        {
            params: {
                id
            }
        }
    )
}
