<template>
  <div class="edit-post-view">
   <div class="bg-gradient-to-r from-mint-500 to-teal-600 text-white p-8 rounded-lg mb-8">
      <h1 class="text-3xl font-bold">Edit Post</h1>
     <p class="text-mint-100">Update your content</p>
    </div>

    <div class="bg-white rounded-lg shadow-md p-6">
      <PostForm />
    </div>
  </div>
</template>

<script setup>
import PostForm from '@/components/post/PostForm.vue';
</script>