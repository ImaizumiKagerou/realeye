import request from '../utils/request';

export const fetchData = query => {
    return request({
        url: './table.json',
        method: 'get',
        params: query
    });
};

export const formatDate = (row, column) => {
    // 获取单元格数据
    let data = row[column.property]
    if (data === null) {
        return null
    }
    let dt = new Date(data)
    return dt.getFullYear() + '-' + (dt.getMonth() + 1) + '-' + dt.getDate() + ' ' + dt.getHours() + ':' + dt.getMinutes() + ':' + dt.getSeconds()
};

export const formatDate2Date = (row, column) => {
    // 获取单元格数据
    let data = row[column.property]
    if (data === null) {
        return null
    }
    let dt = new Date(data)
    return dt.getFullYear() + '-' + (dt.getMonth() + 1) + '-' + dt.getDate();
};
