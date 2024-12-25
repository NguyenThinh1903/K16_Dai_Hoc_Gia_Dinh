<template>
  <div v-if="cart.length === 0" class="empty-cart">
    <p>Giỏ hàng chưa có sản phẩm nào.</p>
  </div>
  <div v-else class="cart-container">
    <div class="cart-left">
      <table class="cart-table">
        <thead>
          <tr>
            <th><input type="checkbox" @change="toggleSelectAll" /></th>
            <th>HÌNH</th>
            <th>SẢN PHẨM</th>
            <th>ĐƠN GIÁ</th>
            <th>SỐ LƯỢNG</th>
            <th>THÀNH TIỀN</th>
            <th>XÓA</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="item in cart" :key="item.id">
            <td><input type="checkbox" v-model="item.selected" /></td>
            <td><img :src="item.image" alt="Product" class="product-img" /></td>
            <td>{{ item.name }}</td>
            <td>{{ item.price.toLocaleString() }} đ</td>
            <td>
              <button @click="decreaseQuantity(item)" class="btn-quantity">-</button>
              {{ item.quantity }}
              <button @click="increaseQuantity(item)" class="btn-quantity">+</button>
            </td>
            <td>{{ (item.price * item.quantity).toLocaleString() }} đ</td>
            <td><button @click="confirmDeleteProduct(item)" class="btn-remove">Xóa</button></td>
          </tr>
        </tbody>
      </table>
    </div>

    <div class="cart-right">
      <div class="summary">
        <p>Tổng sản phẩm: <strong>{{ totalQuantity }}</strong></p>
        <p>Tổng tiền (đã bao gồm VAT): <strong>{{ totalPrice.toLocaleString() }} đ</strong></p>
        <button class="btn-pay" :disabled="totalQuantity === 0" @click="showPaymentMethods">
          THANH TOÁN
        </button>
      </div>

      <div v-if="showPaymentModal" class="payment-modal">
        <h3>Chọn phương thức thanh toán:</h3>
        <ul>
          <li>
            <input type="radio" id="cash" value="Tiền mặt" v-model="paymentMethod" />
            <label for="cash">Tiền mặt</label>
          </li>
          <li>
            <input type="radio" id="bank" value="Chuyển khoản ngân hàng" v-model="paymentMethod" />
            <label for="bank">Chuyển khoản ngân hàng</label>
          </li>
          <li>
            <input type="radio" id="credit" value="Thẻ tín dụng" v-model="paymentMethod" />
            <label for="credit">Thẻ tín dụng</label>
          </li>
        </ul>
        <button @click="processPayment" class="btn-pay">
          XÁC NHẬN THANH TOÁN
        </button>
        <button @click="closePaymentModal" class="btn-cancel">HỦY</button>
      </div>
    </div>
  </div>
</template>

<script>
// Import dữ liệu giỏ hàng (cart) và danh sách sản phẩm (items)
import cart from "../data/cart";
import items from "../data/items";

export default {
  // Khai báo dữ liệu (data) của component
  data() {
    return {
      // Lấy dữ liệu giỏ hàng từ localStorage hoặc sử dụng dữ liệu mặc định (cart)
      cart: JSON.parse(localStorage.getItem("cart")) || cart,
      // Biến kiểm soát hiển thị modal thanh toán
      showPaymentModal: false,
      // Lưu phương thức thanh toán được chọn
      paymentMethod: "",
    };
  },
  // Định nghĩa các thuộc tính tính toán (computed)
  computed: {
    // Tính tổng giá trị các sản phẩm được chọn trong giỏ hàng
    totalPrice() {
      return this.cart.reduce(
        (total, item) =>
          total + (item.selected ? item.price * item.quantity : 0),
        0
      );
    },
    // Tính tổng số lượng sản phẩm được chọn trong giỏ hàng
    totalQuantity() {
      return this.cart.reduce(
        (total, item) => total + (item.selected ? item.quantity : 0),
        0
      );
    },
  },
  // Định nghĩa các phương thức (methods) của component
  methods: {
    // Tăng số lượng sản phẩm trong giỏ hàng
    increaseQuantity(item) {
      const stockItem = items.find((i) => i.id === item.id); // Tìm sản phẩm trong danh sách hàng hóa
      if (stockItem && item.quantity < stockItem.quality) {
        item.quantity++; // Tăng số lượng trong giỏ
        stockItem.quality--; // Giảm số lượng trong kho
        this.updateCartInLocalStorage(); // Cập nhật localStorage
      } else {
        alert("Sản phẩm này đã hết hàng!"); // Thông báo khi không còn hàng
      }
    },
    // Giảm số lượng sản phẩm trong giỏ hàng
    decreaseQuantity(item) {
      if (item.quantity > 1) {
        const stockItem = items.find((i) => i.id === item.id);
        item.quantity--; // Giảm số lượng trong giỏ
        stockItem.quality++; // Tăng số lượng trong kho
        this.updateCartInLocalStorage(); // Cập nhật localStorage
      } else {
        this.confirmDeleteProduct(item); // Xác nhận xóa nếu số lượng là 1
      }
    },
    // Hiển thị hộp thoại xác nhận xóa sản phẩm khỏi giỏ
    confirmDeleteProduct(item) {
      if (confirm("Bạn có chắc chắn muốn xóa sản phẩm này khỏi giỏ hàng?")) {
        this.deleteProduct(item); // Gọi phương thức xóa
      }
    },
    // Xóa sản phẩm khỏi giỏ hàng
    deleteProduct(item) {
      this.cart = this.cart.filter((x) => x.id !== item.id); // Loại bỏ sản phẩm khỏi danh sách
      this.updateCartInLocalStorage(); // Cập nhật localStorage
    },
    // Chọn hoặc bỏ chọn tất cả sản phẩm trong giỏ hàng
    toggleSelectAll(event) {
      const isChecked = event.target.checked; // Kiểm tra trạng thái checkbox
      this.cart.forEach((item) => {
        item.selected = isChecked; // Gán trạng thái cho tất cả sản phẩm
      });
      this.updateCartInLocalStorage(); // Cập nhật localStorage
    },
    // Hiển thị modal để chọn phương thức thanh toán
    showPaymentMethods() {
      this.showPaymentModal = true;
    },
    // Đóng modal thanh toán
    closePaymentModal() {
      this.showPaymentModal = false;
    },
    // Xử lý thanh toán
    processPayment() {
      if (!this.paymentMethod) {
        alert("Vui lòng chọn phương thức thanh toán!"); // Thông báo nếu chưa chọn phương thức
        return;
      }

      alert(
        `Thanh toán thành công qua phương thức: ${this.paymentMethod}!` // Thông báo thanh toán thành công
      );
      this.cart = this.cart.filter((item) => !item.selected); // Loại bỏ sản phẩm đã thanh toán
      this.updateCartInLocalStorage(); // Cập nhật localStorage
      this.showPaymentModal = false; // Đóng modal thanh toán
    },
    // Cập nhật giỏ hàng vào localStorage
    updateCartInLocalStorage() {
      localStorage.setItem("cart", JSON.stringify(this.cart));
    },
  },
};
</script>

<style scoped>
.cart-container {
  display: flex;
  justify-content: space-between;
  gap: 20px;
  padding: 20px;
  background-color: #f9f9f9;
}

.cart-left {
  flex: 2; /* Chiếm 2/3 không gian */
}

.cart-table {
  width: 100%;
  border-collapse: collapse;
  background-color: white;
}

.cart-table th,
.cart-table td {
  text-align: center;
  padding: 10px;
  border-bottom: 1px solid #ddd;
}

.cart-table th {
  background-color: #f1f1f1;
}

.product-img {
  width: 80px;
  height: auto;
}

.btn-quantity {
  background-color: #f0f0f0;
  border: 1px solid #ccc;
  padding: 5px 8px;
  cursor: pointer;
  margin: 0 5px;
}

.btn-remove {
  background-color: #ff6b6b;
  color: white;
  border: none;
  padding: 5px 10px;
  cursor: pointer;
  border-radius: 4px;
}

.btn-remove:hover {
  background-color: #e63946;
}

.cart-right {
  flex: 1; /* Chiếm 1/3 không gian */
  padding: 20px;
  background-color: white;
  border: 1px solid #ddd;
  border-radius: 8px;
}

.summary {
  margin-top: 10px;
}

.btn-pay {
  width: 100%;
  background-color: #007bff;
  color: white;
  border: none;
  padding: 10px;
  border-radius: 5px;
  cursor: pointer;
}

.btn-pay:disabled {
  background-color: #ddd;
  cursor: not-allowed;
}

.empty-cart {
  text-align: center;
  font-size: 16px;
  color: #888;
  padding: 50px;
}
.payment-modal {
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  background: white;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.2);
  z-index: 1000;
}

.payment-modal h3 {
  margin-bottom: 15px;
}

.payment-modal ul {
  list-style: none;
  padding: 0;
  margin-bottom: 15px;
}

.payment-modal li {
  margin-bottom: 10px;
}

.btn-cancel {
  background-color: #f44336;
  color: white;
  padding: 10px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  margin-left: 10px;
}
</style>