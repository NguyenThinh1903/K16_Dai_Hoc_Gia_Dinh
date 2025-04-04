// .\src\views\HomeView.vue
<template>
  <div class="py-8">
    <!-- No more hero section -->

    <h1 class="text-3xl font-bold mb-6 text-gray-800 border-b pb-2">Latest Posts</h1>

    <PostList />

    <!-- Admin actions (styled differently) -->
    <div v-if="authStore.isAuthenticated && authStore.isAdmin" class="mt-12">
      <h2 class="text-xl font-semibold mb-4 text-gray-700">Admin Actions</h2>
      <div class="flex space-x-4">
        <router-link
          to="/create-post"
          class="bg-mint-500 hover:bg-mint-600 text-white font-medium py-2 px-4 rounded-md transition flex items-center"
        >
          <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 mr-2" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4" />
          </svg>
          Create New Post
        </router-link>
        <router-link
          to="/admin/dashboard"
          class="bg-gray-200 hover:bg-gray-300 text-gray-700 font-medium py-2 px-4 rounded-md transition flex items-center"
        >
          <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 mr-2" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 6h16M4 12h16M4 18h7" />
          </svg>
          Admin Dashboard
        </router-link>
      </div>
    </div>
  </div>
</template>

<script setup>
import { onMounted } from 'vue';
import PostList from '@/components/post/PostList.vue'
import { useAuthStore } from '@/stores/auth';

const authStore = useAuthStore();
</script>