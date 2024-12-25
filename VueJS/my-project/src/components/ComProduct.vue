<template>
  <main>  
    <div class="menu">
      <h2 class="menu-title"></h2>
      <div class="menu-carousel">
        <div class="menu-item">
          <router-link :to="{name: 'ProductDetail', params: {id}}"> 
            <img :src="product.image" alt="Pumpkin Spice Juice" class="menu-item-image">
            <div class="menu-item-info">
              <h3>{{ product.name }}</h3>
              <p>{{ product.price }}</p>
            </div>    
          </router-link>
        </div>
      </div>
    </div>  
  </main>
</template>

<script>
export default {
  props: ['product'], 
  data() {
    return {
      id: this.product.id
    };
  }
};
</script>

<style scoped>
.menu {
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
  gap: 20px;
}

.menu-carousel {
  display: flex;
  justify-content: center;
  align-items: center;
}

.menu-item {
  width: 300px;
  text-align: center;
  border: 1px solid #ddd;
  border-radius: 10px;
  padding: 16px;
  background-color: #f9f9f9;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

.menu-item-image {
  width: 100%;
  height: auto;
  border-radius: 8px;
  margin-bottom: 10px;
}

.menu-item-info h3 {
  font-size: 18px;
  color: #333;
  margin: 0;
}

.menu-item-info p {
  color: #555;
  font-size: 16px;
  margin: 5px 0 0;
}


.menu a {
  text-decoration: none; 
  color: inherit;
}
</style>
