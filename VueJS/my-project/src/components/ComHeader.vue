<template>
  <header>
    <div class="container">
  
      <div class="logo">
        <img src="/Images/book_logo.png" alt="Logo Book">
        <span class="logo-text">Book Store</span>
      </div>

      <nav>
        <ul>
          <li><router-link to="/">Trang Chủ</router-link></li>
          <li><router-link to="/gioithieu">Giới Thiệu</router-link></li>
          <li><router-link to="/lienhe">Liên Hệ</router-link></li>
          <li>
            <div class="dropdown">
              <button class="dropbtn">Tài Khoản</button>
              <div class="dropdown-content">
                <router-link to="/register">Đăng Ký</router-link>
                <router-link to="/login">Đăng Nhập</router-link>
              </div>
            </div>
          </li>
        </ul>
      </nav>


      <div class="search-container">
        <input 
          v-model="searchQuery" 
          type="text" 
          placeholder="Tìm kiếm sách..." 
          @input="filterItems" 
          @blur="clearSearch">
      </div>

      <div class="user-info">
        <p v-if="user">Xin chào, {{ user.username }}</p>
        <button v-if="user" @click="logout">Đăng xuất</button>
      </div>

      
      <div class="cta">
        <router-link to="/cart" class="cta-button">Giỏ Hàng</router-link>
      </div>
    </div>
   
      <div v-if="searchQuery && filteredItems.length > 0" class="search-results">
      <h3>Kết quả tìm kiếm:</h3>
      <div class="product-list">
        <div class="product-item" v-for="item in filteredItems" :key="item.id">
          <router-link :to="`/product/${item.id}`">
            <img :src="item.image" :alt="item.name" />
            <h3>{{ item.name }}</h3>
            <p>Giá: {{ item.price }} VNĐ</p>
          </router-link>
        </div>
      </div>
    </div>
  </header>
  <router-view />
</template>

<script>
import eventBus from "@/eventBus";
import items from '../data/items';

export default {
  // Khai báo dữ liệu của component
  data() {
    return {
      user: JSON.parse(localStorage.getItem("currentuser")) || null, // Lấy thông tin người dùng hiện tại từ localStorage hoặc null nếu chưa đăng nhập
      searchQuery: "", // Biến để lưu từ khóa tìm kiếm
      filteredItems: items, // Danh sách sản phẩm hiển thị, ban đầu là toàn bộ danh sách
    };
  },
  // Hook lifecycle `created`, thực thi ngay sau khi component được tạo
  created() {
    // Lắng nghe sự kiện "loginSuccess" từ eventBus, cập nhật thông tin người dùng
    eventBus.on("loginSuccess", (user) => {
      this.user = user;
    });
  },
  // Hook lifecycle `beforeUnmount`, thực thi trước khi component bị gỡ bỏ
  beforeUnmount() {
    // Hủy đăng ký sự kiện "loginSuccess" trên eventBus để tránh rò rỉ bộ nhớ
    eventBus.off("loginSuccess");
  },
  // Định nghĩa các phương thức của component
  methods: {
    // Xử lý đăng xuất
    logout() {
      localStorage.removeItem("currentuser"); // Xóa thông tin người dùng khỏi localStorage
      this.user = null; // Đặt lại trạng thái người dùng thành null
      this.$router.push("/"); // Chuyển hướng về trang chủ
    },
    // Lọc danh sách sản phẩm dựa trên từ khóa tìm kiếm
    filterItems() {
      const query = this.searchQuery.toLowerCase(); // Chuyển từ khóa tìm kiếm thành chữ thường để so sánh không phân biệt chữ hoa/thường
      this.filteredItems = items.filter(item => 
        item.name.toLowerCase().includes(query) // Lọc các sản phẩm có tên chứa từ khóa
      );
    },
    // Xóa từ khóa tìm kiếm và danh sách sản phẩm được lọc
    clearSearch() {
      this.searchQuery = ""; // Đặt lại từ khóa tìm kiếm thành chuỗi rỗng
      this.filteredItems = []; // Xóa danh sách sản phẩm được lọc
    },
  },
};
</script>


<style>
.container {
  display: flex;
  justify-content: center; 
  align-items: center;
  padding: 15px 30px;
  background-color: #fff;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  width: 100%;
}


.logo {
  display: flex;
  align-items: center;
}

.logo img {
  height: 50px;
  transition: transform 0.3s ease;
}

.logo-text {
  font-size: 24px;
  font-weight: bold;
  color: #000;
  margin-left: 10px;
}

nav {
  flex: 1;
  display: flex;
  justify-content: center;
}

nav ul {
  list-style: none;
  display: flex;
  margin: 0;
  padding: 0;
}

nav ul li {
  margin: 0 20px;
  display: flex; 
  align-items:center; 
  justify-content: center; 
}

nav ul li a {
  text-decoration: none;
  color: #000;
  font-weight: bold;
  transition: color 0.3s ease;
}

nav ul li a:hover {
  color: #fcb034;
}

.dropdown {
  display: flex; 
  align-items: center; 
}

.dropbtn {
  background-color: #fff;
  border: none;
  font-size: 16px;
  color: #000;
  cursor: pointer;
  padding: 8px 16px; 
  text-align: center;
  font-weight: bold;
  margin: 0; 
}

.dropbtn:hover {
  color: #fcb034;
}

.dropdown-content {
  display: none;
  position: absolute;
  background-color: #fff;
  min-width: 160px;
  box-shadow: 0px 8px 16px rgba(0, 0, 0, 0.2);
  z-index: 1;
}

.dropdown:hover .dropdown-content {
  display: block;
}

.dropdown-content a {
  color: black;
  padding: 12px 16px;
  text-decoration: none;
  display: block;
}

.dropdown-content a:hover {
  background-color: #fcb034;
  color: white;
}

.search-container {
  margin-left: 20px;
}

.search-container input {
  padding: 8px 12px;
  font-size: 16px;
  border-radius: 20px;
  border: 1px solid #ddd;
  width: 300px;
  box-sizing: border-box;
}

.search-container input:focus {
  outline: none;
  border-color: #fcb034;
}

.cta {
  margin-right: 30px;
}

.cta-button {
  padding: 10px 20px;
  background-color: #fcb034;
  color: #fff;
  text-decoration: none;
  border-radius: 25px;
  transition: background-color 0.3s ease, transform 0.3s ease;
}

.cta-button:hover {
  background-color: #ffa726;
  transform: scale(1.1);
}

.user-info {
  display: flex;
  align-items: center;
  margin-left: 20px;
  margin-right: 30px;
}

.user-info p {
  margin-right: 15px;
  font-size: 16px;
}

button {
  padding: 5px 15px;
  background-color: #fcb034;
  color: white;
  border: none;
  border-radius: 20px;
  cursor: pointer;
  font-size: 14px;
}

button:hover {
  background-color: #ffa726;
}

.search-results {
  margin-top: 20px;
  padding: 10px;
  background-color: #f9f9f9;
  border: 1px solid #ddd;
  border-radius: 5px;
}

.product-list {
  display: flex;
  flex-wrap: wrap;
}

.product-item {
  border: 1px solid #ccc;
  border-radius: 5px;
  margin: 10px;
  padding: 10px;
  width: 200px;
  text-align: center;
}

.product-item img {
  max-width: 100%;
  height: auto;
}
</style>