<template>
  <div class="create-post-view">
   <div class="bg-gradient-to-r from-mint-500 to-teal-600 text-white p-8 rounded-lg mb-8">
      <h1 class="text-3xl font-bold">Create New Post</h1>
     <p class="text-mint-100">Share your thoughts with the world</p>
    </div>

    <div class="bg-white rounded-lg shadow-md p-6">
      <PostForm @post-created="$router.push({ name: 'home' })" />
    </div>
  </div>
</template>

<script setup>
import PostForm from '@/components/post/PostForm.vue';
import { useRouter } from 'vue-router';

const router = useRouter();

</script>