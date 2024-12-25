<template>
  <div class="login-container">
    <h2>Đăng nhập</h2>
    <form @submit.prevent="login">
      <input v-model="username" placeholder="Tên đăng nhập" />
      <input v-model="password" type="password" placeholder="Mật khẩu" />
      <button type="submit">Đăng nhập</button>
    </form>
    <p v-if="error" class="error">{{ error }}</p>
  </div>
</template>

<script>
import eventBus from "@/eventBus";

export default {
  data() {
    return {
      username: "", // Tên đăng nhập
      password: "", // Mật khẩu
      error: "", // Thông báo lỗi
      users: JSON.parse(localStorage.getItem("users")) || [] // Lấy danh sách người dùng từ localStorage
    };
  },
  methods: {
    login() {
      // Kiểm tra người dùng trong danh sách
      const user = this.users.find(
        (u) => u.username === this.username && u.password === this.password
      );

      if (user) {
        // Lưu thông tin người dùng vào localStorage
        localStorage.setItem("currentuser", JSON.stringify(user));

        // Phát sự kiện đăng nhập thành công qua eventBus
        eventBus.emit("loginSuccess", user);

        // Thông báo và điều hướng
        alert("Đăng nhập thành công!");
        this.error = "";
        this.$router.push("/");  // Điều hướng tới trang chủ
      } else {
        this.error = "Tên đăng nhập hoặc mật khẩu không đúng.";
      }
    }
  }
};
</script>

<style scoped>
.login-container {
  max-width: 400px;
  margin: 0 auto;
  padding: 20px;
  background-color: #f9f9f9;
  border-radius: 8px;
  box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.1);
  text-align: center;
}

h2 {
  margin-bottom: 20px;
  color: #333;
}

form {
  display: flex;
  flex-direction: column;
  align-items: center;
}

input {
  width: 100%;
  max-width: 300px;
  padding: 12px;
  margin: 10px 0;
  border-radius: 4px;
  border: 1px solid #ddd;
  font-size: 16px;
  box-sizing: border-box;
}

button {
  width: 100%;
  max-width: 300px;
  padding: 12px;
  background-color: #4caf50;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 16px;
  margin-top: 10px;
  box-sizing: border-box;
}

button:hover {
  background-color: #45a049;
}

.error {
  color: red;
  font-size: 14px;
  margin-top: 10px;
}
</style>
