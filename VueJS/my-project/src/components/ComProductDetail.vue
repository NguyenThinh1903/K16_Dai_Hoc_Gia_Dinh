<template>
  <main>
    <div class="product-details">

      <img :src="product.image" :alt="product.name" class="product-image">

      <div class="details-container">
        <h2 class="product-title">{{ product.name }}</h2>
        <p class="product-description" v-html="product.description"></p>
        <p class="product-price">{{ product.price }} VND</p>
        <p class="product-quality">Số lượng còn lại: {{ product.quality }}</p>

        <div v-if="product.quality === 0" class="out-of-stock">
          <p>Hết hàng</p>
        </div>

        <div v-else>
          <button v-if="isLoggedIn" @click="addToCart(product)" class="add-to-cart-btn">Thêm vào giỏ</button>
          <p v-else class="login-required">Vui lòng đăng nhập để thêm vào giỏ hàng</p>
        </div>
      </div>
    </div>
  </main>
</template>

<script>
import items from '../data/items';
import cart from '../data/cart';

export default {
  data() {
    return {
      product: {},
      cart,
    };
  },
  computed: {
    isLoggedIn() {
      return localStorage.getItem('currentuser') !== null;
    },
  },
  mounted() {
    const id = this.$route.params.id;
    console.log("Product ID:", id);
    this.product = items.find((item) => item.id == id);
  },
  methods: {
    addToCart(product) {
      if (!this.isLoggedIn) {
        alert('Vui lòng đăng nhập để thêm vào giỏ hàng.');
        return;
      }

      const existingProduct = this.cart.find((item) => item.id == product.id);
      if (existingProduct && product.quality > 0) {
        existingProduct.quantity++;
      } else if (product.quality > 0) {
        this.cart.push({ ...product, quantity: 1 });
      }
      product.quality--;

      // Lưu trạng thái giỏ hàng vào localStorage
      localStorage.setItem('cart', JSON.stringify(this.cart));
    },
  },
};

</script>
<style scoped>
.product-details {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 20px;
  margin: 20px;
}

.product-image {
  width: 250px;
  height: 250px;
  object-fit: cover;
  border: 2px solid #ddd;
  border-radius: 8px;
}

.details-container {
  max-width: 600px;
  padding: 20px;
}

.product-title {
  font-size: 28px;
  font-weight: bold;
  margin-bottom: 10px;
  color: #333;
}

.product-description {
  font-size: 16px;
  color: #555;
  margin-bottom: 10px;
  line-height: 1.5;
  text-align: justify;
  word-wrap: break-word;
  white-space: normal;
}

.product-price {
  font-size: 24px;
  font-weight: bold;
  color: #f77c00;
  margin-bottom: 20px;
}

.out-of-stock p {
  color: #ff0000;
  font-size: 18px;
  font-weight: bold;
}

.add-to-cart-btn {
  background-color: #fcb034;
  color: #fff;
  border: none;
  padding: 10px 20px;
  font-size: 16px;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.add-to-cart-btn:hover {
  background-color: #f77c00;
}

.login-required {
  color: #ff0000;
  font-size: 16px;
  margin-top: 10px;
}
</style>
