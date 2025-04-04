<template>
  <div class="post-item bg-white rounded-lg shadow-md overflow-hidden hover:shadow-lg transition-shadow flex flex-col h-full">
    <!-- Image Container -->
    <div v-if="post.image_url" class="relative w-full h-48">
      <img
        :src="fullImageUrl"
        :alt="post.title"
        class="w-full h-full object-cover"
        @error="handleImageError"
      />
      <div v-if="imageError" class="absolute inset-0 bg-gray-100 flex items-center justify-center text-red-500">
        Image Error
      </div>
    </div>

    <div class="p-4 flex flex-col flex-grow">
      <!-- Title -->
      <div class="mb-2">
        <h2 class="text-lg font-semibold text-gray-800 hover:text-mint-500 transition-colors line-clamp-2">
          <router-link :to="{ name: 'post', params: { id: post.id } }">
            {{ post.title }}
          </router-link>
        </h2>
      </div>

      <!-- Date and Engagement Stats -->
      <div class="flex justify-between items-center mb-3">
        <!-- Date -->
        <span class="text-gray-600 text-sm flex items-center">
          <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 mr-1" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z"/>
          </svg>
          {{ formatDate(post.created_at) }}
        </span>

        <!-- Engagement Stats -->
        <div class="flex space-x-4">
          <span class="text-gray-600 text-sm flex items-center" title="Likes">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 mr-1" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2">
              <path stroke-linecap="round" stroke-linejoin="round" d="M14 10h4.764a2 2 0 011.789 2.894l-3.5 7A2 2 0 0115.263 21h-4.017c-.163 0-.326-.02-.485-.06L7 20m7-10V5a2 2 0 00-2-2h-.095c-.5 0-.905.405-.905.905 0 .714-.211 1.412-.608 2.006L7 11v9m7-10h-2M7 20H5a2 2 0 01-2-2v-6a2 2 0 012-2h2.5" />
            </svg>
            {{ post.likes }}
          </span>
          <span class="text-gray-600 text-sm flex items-center" title="Dislikes">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 mr-1" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2">
              <path stroke-linecap="round" stroke-linejoin="round" d="M10 14H5.236a2 2 0 01-1.789-2.894l3.5-7A2 2 0 018.736 3h4.018a2 2 0 01.485.06l3.76.94m-7 10v5a2 2 0 002 2h.095c.5 0 .905-.405 .905-.905 0-.714.211-1.412.608-2.006L17 13V4m-7 10h2m5-10h2a2 2 0 012 2v6a2 2 0 01-2 2h-2.5" />
            </svg>
            {{ post.dislikes }}
          </span>
          <span class="text-gray-600 text-sm flex items-center" title="Comments">
             <!-- Corrected Comment Icon -->
            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-4 h-4 mr-1">
              <path stroke-linecap="round" stroke-linejoin="round" d="M2.25 12.76c0 1.6 1.123 2.994 2.707 3.227 1.087.16 2.185.283 3.293.369V21l4.076-4.076a1.526 1.526 0 0 1 1.037-.443 48.282 48.282 0 0 0 5.68-.494c1.584-.233 2.707-1.626 2.707-3.228V6.741c0-1.602-1.123-2.995-2.707-3.228A48.394 48.394 0 0 0 12 3c-2.392 0-4.744.175-7.043.513C3.373 3.746 2.25 5.14 2.25 6.741v6.018Z" />
            </svg>
            {{ post.comment_count }}
          </span>
        </div>
      </div>

      <!-- Truncated Content -->
      <div class="text-gray-700 text-sm mb-3 h-16 overflow-hidden">
        <p class="line-clamp-3">{{ truncateContent(post.content) }}</p>
      </div>

      <!-- Footer: Read More -->
      <div class="flex items-center mt-auto pt-2 border-t border-gray-100">
        <router-link
          :to="{ name: 'post', params: { id: post.id } }"
          class="text-mint-500 hover:text-mint-600 font-medium transition-colors duration-200"
        >
          Read More →
        </router-link>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed, ref } from 'vue';
const API_BASE_URL = import.meta.env.VITE_API_URL || 'http://localhost:3000';
const props = defineProps({
  post: {
    type: Object,
    required: true,
  },
});

const fullImageUrl = computed(() => {
  if (!props.post.image_url) return '';
  if (props.post.image_url.startsWith('http')) {
    return props.post.image_url;
  }
  return `${API_BASE_URL}${props.post.image_url}`;
});

const formatDate = (dateString) => {
  const options = { year: 'numeric', month: 'short', day: 'numeric' };
  return new Date(dateString).toLocaleDateString(undefined, options);
};

const truncateContent = (content) => {
  if (!content) return '';
  return content.length > 120 ? content.substring(0, 120) + '...' : content;
};

const imageError = ref(false);
const handleImageError = () => {
  imageError.value = true;
}
</script>

<style scoped>
.line-clamp-2 {
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}
.line-clamp-3 {
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  overflow: hidden;
}
</style>