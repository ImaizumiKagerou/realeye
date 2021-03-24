package com.realeye.backend.utils.jwt.utils;

public class RedisKeyEnum {
    public enum KeyEnum {
        REDIS_DATA_DIC("数据字典", "data_dic"),
        REDIS_AUTH_KEY("登录权限", "auth_key");

        //用于获取说明，
        private String name;

        //获取数值
        private String index;

        KeyEnum(String name, String index) {
            this.name = name;
            this.index = index;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getIndex() {
            return index;
        }

        public void setIndex(String index) {
            this.index = index;
        }
    }
}
