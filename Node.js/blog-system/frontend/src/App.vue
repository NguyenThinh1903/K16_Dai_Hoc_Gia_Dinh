<template>
  <div class="app-container">
    <Header />
    <ContentContainer>
      <router-view />
    </ContentContainer>
    <Footer />
  </div>
</template>

<script setup>
import Header from './components/layout/Header.vue' // Corrected path
import Footer from './components/layout/Footer.vue'
import ContentContainer from './components/layout/ContentContainer.vue'
</script>

<style>
.app-container {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}
</style>