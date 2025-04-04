<template>
  <div class="post-list">
    <div v-if="loading" class="text-center py-10">
      <div class="animate-spin rounded-full h-8 w-8 border-t-2 border-b-2 border-mint-500 mx-auto"></div>
      <p class="mt-2 text-gray-600">Loading posts...</p>
    </div>
    <div v-else-if="posts.length === 0 && route.query.search" class="text-center py-10 text-gray-600">
        No posts found matching your search criteria.
    </div>
    <div v-else-if="posts.length === 0" class="text-center py-10 text-gray-600">
        No posts available yet. Be the first to create one!
        <span v-if="authStore.isAdmin">
            <router-link to="/create-post" class="text-mint-600 hover:underline"> Create Post</router-link>
        </span>
    </div>
    <div v-else>
      <!-- Grid Layout -->
      <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
       <div v-for="post in posts" :key="post.id" class="w-full">
        <PostItem  :post="post" />
       </div>
      </div>

      <!-- Pagination -->
      <div class="pagination mt-8 flex justify-center items-center space-x-2" v-if="totalPages > 1">
        <button
          @click="loadPage(currentPage - 1)"
          :disabled="currentPage === 1"
          class="px-4 py-2 rounded bg-gray-200 hover:bg-gray-300 disabled:bg-gray-100 disabled:text-gray-400 disabled:cursor-not-allowed transition text-sm"
        >
          « Previous
        </button>
        <span class="text-gray-700 text-sm">
          Page {{ currentPage }} of {{ totalPages }}
        </span>
        <button
          @click="loadPage(currentPage + 1)"
          :disabled="currentPage === totalPages"
          class="px-4 py-2 rounded bg-gray-200 hover:bg-gray-300 disabled:bg-gray-100 disabled:text-gray-400 disabled:cursor-not-allowed transition text-sm"
        >
          Next »
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed, onMounted, ref, watch } from 'vue';
import PostItem from './PostItem.vue';
import { usePostsStore } from '@/stores/posts';
import { useAuthStore } from '@/stores/auth'; // Import auth store for create post link
import { useRoute } from 'vue-router';

const postsStore = usePostsStore();
const authStore = useAuthStore(); // Initialize auth store
const route = useRoute();

const posts = computed(() => postsStore.posts);
const loading = computed(() => postsStore.loading);
const currentPage = computed(() => postsStore.currentPage);
const totalPages = computed(() => postsStore.totalPages);


const loadPostsForCurrentRoute = (page = 1) => {
  const search = route.query.search || ''; // Get search term from route query
  postsStore.fetchPosts({ page, search });
};

const loadPage = (page) => {
    loadPostsForCurrentRoute(page);
};

// Watch for changes in the route's query parameters (specifically 'search')
watch(
    () => route.query.search,
    (newSearchTerm, oldSearchTerm) => {
        // Fetch posts only if the search term actually changes
        // Prevents re-fetching if other query params change
        if (newSearchTerm !== oldSearchTerm) {
            loadPostsForCurrentRoute(1); // Reset to page 1 on new search
        }
    },
    { deep: true } // Use deep watch if query might be complex, though likely not needed for just 'search'
);

onMounted(() => {
  loadPostsForCurrentRoute(1); // Load initial posts based on current route (including search)
});
</script>