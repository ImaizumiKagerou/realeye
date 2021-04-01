<template>
  <div id="main" class="main-bg">
    <div style="height: 80px;background-color: white"></div>
    <!--    <h1 style="color: black">会员登录</h1>-->
    <div class="sub-main-w3">
      <div class="image-style">
      </div>

      <div class="vertical-tab">
        <div id="section1" class="section-w3ls">
          <input id="option1" checked name="sections" type="radio">
          <label class="icon-left-w3pvt" for="option1"><span aria-hidden="true"
                                                             class="fa fa-user-circle"></span>登录</label>
          <article>
            <form action="#" method="post">
              <h3 class="legend">账号登录</h3>
              <div class="input">
                <span aria-hidden="true" class="fa fa-envelope-o"></span>
                <input v-model="logindata.username" name="username" placeholder="用户名" required type="text"/>
              </div>
              <div class="input">
                <span aria-hidden="true" class="fa fa-key"></span>
                <input v-model="logindata.password" name="password" placeholder="密码" required type="password" @keyup.enter="login"/>
              </div>
              <button class="btn submit" type="button" @click="login">登 录</button>
              <!--              <a class="bottom-text-w3ls" for="option3" href="#">忘记密码?</a>-->
            </form>
          </article>
        </div>
        <div id="section2" class="section-w3ls">
          <input id="option2" name="sections" type="radio">
          <label class="icon-left-w3pvt" for="option2"><span aria-hidden="true"
                                                             class="fa fa-pencil-square"></span>注册</label>
          <article>
            <form action="#" method="post">
              <h3 class="legend">注册帐户</h3>
              <div class="input">
                <span aria-hidden="true" class="fa fa-user-o"></span>
                <input v-model="registerdata.username" name="name" placeholder="用户名" required type="text"/>
              </div>
              <div class="input">
                <span aria-hidden="true" class="fa fa-key"></span>
                <input v-model="registerdata.password" name="password" placeholder="密码" required type="password"/>
              </div>
              <div class="input">
                <span aria-hidden="true" class="fa fa-key"></span>
                <input v-model="registerdata.confirmPassword" name="confirmPassword" placeholder="确认密码" required
                       type="password"/>
              </div>
              <button class="btn submit" type="button" @click="register">注 册</button>
            </form>
          </article>
        </div>
        <div id="section3" class="section-w3ls">
          <input id="option3" name="sections" type="radio">
          <label class="icon-left-w3pvt" for="option3"><span aria-hidden="true" class="fa fa-lock"></span>忘记密码?</label>
          <article>
            <form action="#" method="post">
              <h3 class="legend last">重置密码</h3>
              <p class="para-style">请在下面输入您的电子邮件地址，我们将给您发送一封带有说明的电子邮件。</p>
              <!--              <p class="para-style-2"><strong>需要帮助?</strong>了解更多关于如何 <a href="#">#</a></p>-->
              <div class="input">
                <span aria-hidden="true" class="fa fa-envelope-o"></span>
                <input v-model="forgetdata.email" name="email" placeholder="邮箱" required type="email"/>
              </div>
              <button class="btn submit last-btn" type="button">提交</button>
            </form>
          </article>
        </div>
      </div>

      <div class="clear"></div>
    </div>

  </div>
</template>

<script>
import {LoginAPIMethod, RegisterAPIMethod} from "@/api/UserAPI";

export default {
  name: "Login",
  data() {
    return {
      nowYear: new Date().getFullYear(),
      logindata: {
        username: "",
        password: ""
      },
      registerdata: {
        username: "",
        password: "",
        confirmPassword: ""
      },
      forgetdata: {
        email: "",
      }
    }
  },
  methods: {
    login() {
      LoginAPIMethod(this.logindata.username, this.logindata.password).then((res) => {
        if (res.data.code === 200) {
          this.$store.commit("setLoginTokenStorage",res.data.data);
          this.$router.push("/");
        } else {
          this.$message.error(res.data.message);
        }
      })
    },
    register() {
      if (!this.registerdata.password === this.registerdata.confirmPassword) {
        this.$message.error("两次密码不一致,请重新输入");
      }
      RegisterAPIMethod(this.registerdata.username, this.registerdata.password).then((res) => {
        if (res.data.code === 200) {
          this.$store.commit("setLoginTokenStorage",res.data.data);
          this.$router.push("/");
        } else {
          this.$message.error(res.data.message);
        }
      })
    },
  }
}
</script>

<style scoped src="../assets/loginvue/css/font-awesome.min.css"></style>
<style scoped src="../assets/loginvue/css/style.css"></style>
<style scoped>

</style>
