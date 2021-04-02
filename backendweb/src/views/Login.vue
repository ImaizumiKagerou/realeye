<template>
  <div class="login-wrap">
    <div class="ms-login">
      <div class="ms-title">RealEye后台管理系统</div>
      <el-form ref="login" :model="param" :rules="rules" class="ms-content" label-width="0px">
        <el-form-item prop="username">
          <el-input v-model="param.username" placeholder="username">
            <template #prepend>
              <el-button icon="el-icon-user"></el-button>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input
              v-model="param.password"
              placeholder="password"
              type="password"
              @keyup.enter="submitForm()"
          >
            <template #prepend>
              <el-button icon="el-icon-lock"></el-button>
            </template>
          </el-input>
        </el-form-item>
        <div class="login-btn">
          <el-button type="primary" @click="submitForm()">登录</el-button>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script>
import {loginMethod} from "@/api/UserAPI";

export default {
  data() {
    return {
      param: {
        username: "admin",
        password: "123456"
      },
      rules: {
        username: [
          {required: true, message: "请输入用户名", trigger: "blur"}
        ],
        password: [
          {required: true, message: "请输入密码", trigger: "blur"}
        ]
      }
    };
  },
  created() {
    this.$store.commit("clearTags");
  },
  methods: {
    submitForm() {
      this.$refs.login.validate(valid => {
        console.log(valid);
        if (valid) {
          loginMethod(this.param.username, this.param.password)
              .then((res) => {
                if (res.data.code === 200) {
                  this.$message.success("登录成功");
                  localStorage.setItem("ms_username", this.param.username);
                  localStorage.setItem("jwtToken",res.data.data);
                  this.$router.push("/");
                } else {
                  this.$message.error("用户名或密码错误");
                }
              });
        } else {
          this.$message.error("请输入账号和密码");
          return false;
        }
      });
    }
  }
};
</script>

<style scoped>
.login-wrap {
  position: relative;
  width: 100%;
  height: 100%;
  background-image: url(../assets/img/login-bg.jpg);
  background-size: 100%;
}

.ms-title {
  width: 100%;
  line-height: 50px;
  text-align: center;
  font-size: 20px;
  color: #fff;
  border-bottom: 1px solid #ddd;
}

.ms-login {
  position: absolute;
  left: 50%;
  top: 50%;
  width: 350px;
  margin: -190px 0 0 -175px;
  border-radius: 5px;
  background: rgba(255, 255, 255, 0.3);
  overflow: hidden;
}

.ms-content {
  padding: 30px 30px;
}

.login-btn {
  text-align: center;
}

.login-btn button {
  width: 100%;
  height: 36px;
  margin-bottom: 10px;
}

.login-tips {
  font-size: 12px;
  line-height: 30px;
  color: #fff;
}
</style>
