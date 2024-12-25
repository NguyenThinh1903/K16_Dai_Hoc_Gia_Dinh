<template>
  <div class="register-container">
    <h2>Đăng ký tài khoản</h2>
    <form @submit.prevent="register">
      <input v-model="username" placeholder="Tên đăng nhập" />
      <input v-model="password" type="password" placeholder="Mật khẩu" />
      <input
        v-model="confirmPassword"
        type="password"
        placeholder="Xác nhận mật khẩu"
      />
      <button type="submit">Đăng ký</button>
    </form>
    <p v-if="error" class="error">{{ error }}</p>
  </div>
</template>

<script>
export default {
  data() {
    return {
      username: "", // Tên đăng nhập
      password: "", // Mật khẩu
      confirmPassword: "", // Xác nhận mật khẩu
      error: "", // Thông báo lỗi
      users: JSON.parse(localStorage.getItem("users")) || [] // Lấy danh sách người dùng từ localStorage
    };
  },
  methods: {
    register() {
      // Kiểm tra mật khẩu khớp
      if (this.password !== this.confirmPassword) {
        this.error = "Mật khẩu không khớp.";
        return;
      }

      // Kiểm tra tên đăng nhập đã tồn tại
      const existingUser = this.users.find(
        (user) => user.username === this.username
      );
      if (existingUser) {
        this.error = "Tên đăng nhập đã tồn tại.";
        return;
      }

      // Tạo người dùng mới
      const newUser = {
        id: this.users.length + 1,
        username: this.username,
        password: this.password
      };

      // Lưu người dùng mới vào localStorage
      this.users.push(newUser);
      localStorage.setItem("users", JSON.stringify(this.users));

      // Thông báo đăng ký thành công và điều hướng
      alert("Đăng ký thành công!");
      this.error = "";
      this.$router.push("/login");
    }
  }
};
</script>

<style scoped>
.register-container {
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
