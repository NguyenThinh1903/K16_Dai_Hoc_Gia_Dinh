<template>
  <div class="home-container">
    <h1>TẤT CẢ SÁCH</h1>
    <div class="products-grid">
      <Product 
        v-for="item in paginatedProducts" 
        :key="item.id" 
        :product="item" 
      />
    </div>
    
    <div class="pagination">
      <button 
        :disabled="currentPage === 1" 
        @click="changePage(1)">
        Trang đầu
      </button>
      <button 
        :disabled="currentPage === 1" 
        @click="changePage(currentPage - 1)">
        Trang trước
      </button>
      <span>Trang {{ currentPage }} / {{ totalPages }}</span>
      <button 
        :disabled="currentPage === totalPages" 
        @click="changePage(currentPage + 1)">
        Trang sau
      </button>
      <button 
        :disabled="currentPage === totalPages" 
        @click="changePage(totalPages)">
        Trang cuối
      </button>
    </div>
  </div>
</template>

<script>
import Product from './ComProduct.vue';
import items from '../data/items';

export default {
  name: 'ComHome',
  components: {
    Product
  },
  data() {
    return {
      products: items, // Danh sách sản phẩm
      currentPage: 1, // Trang hiện tại
      itemsPerPage: 6 // Số sản phẩm hiển thị mỗi trang
    };
  },
  computed: {
    totalPages() {
      // Tính tổng số trang
      return Math.ceil(this.products.length / this.itemsPerPage);
    },
    paginatedProducts() {
      // Tính toán danh sách sản phẩm hiển thị trong trang hiện tại
      const start = (this.currentPage - 1) * this.itemsPerPage;
      const end = start + this.itemsPerPage;
      return this.products.slice(start, end);
    }
  },
  methods: {
    changePage(newPage) {
      // Thay đổi trang
      if (newPage >= 1 && newPage <= this.totalPages) {
        this.currentPage = newPage;
      }
    }
  }
};
</script>

<style scoped>
.home-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
  text-align: center;
}

h1 {
  font-size: 26px;
  color: #333;
  margin-bottom: 20px;
}

.products-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 30px;
  justify-items: center;
  align-items: center;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 10px;
}

.pagination button {
  padding: 10px 20px;
  font-size: 16px;
  border: none;
  background-color: #007BFF;
  color: white;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.pagination button:hover {
  background-color: #0056b3;
}

.pagination button:disabled {
  background-color: #ccc;
  cursor: not-allowed;
}

.pagination span {
  font-size: 16px;
  color: #333;
}
</style>
