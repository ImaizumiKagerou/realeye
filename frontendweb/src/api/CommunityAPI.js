import axios from "axios";
import store from "@/store";

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

export const AddCommunity = (title, content) => {
    return axios.get(
        store.state.base_url + "community/add",
        {
            params: {
                title,
                content
            }
        }
    )
}

export const CommunityListMethod = (pageNum, pageSize) => {
    return axios.get(
        store.state.base_url + "community/list",
        {
            params: {
                pageNum,
                pageSize
            }
        }
    )
}

export const CommunityArticleInfoByIdMethod = (id) => {
    return axios.get(
        store.state.base_url + "community/info",
        {
            params: {
                id
            }
        }
    )
}

export const CommunityArticleCommentByIdMethod = (id) => {
    return axios.get(
        store.state.base_url + "community/getComment",
        {
            params: {
                id
            }
        }
    )
}

export const CommunityArticleReplySubmit = (communityId, content) => {
    return axios.get(
        store.state.base_url + "community/addComment",
        {
            params: {
                communityId,
                content
            }
        }
    )
}

export const AddLike = (id) => {
    return axios.get(
        store.state.base_url + "community/like",
        {
            params: {
                id
            }
        }
    )
}
