<template>
  <div class="post-view">
    <div v-if="loading" class="flex justify-center py-16">
      <div class="animate-spin rounded-full h-12 w-12 border-t-2 border-b-2 border-indigo-500"></div>
    </div>

    <div v-else-if="error" class="bg-red-100 border-l-4 border-red-500 text-red-700 p-4 mb-6 rounded">
      <p class="font-medium">Error: {{ error.message }}</p>
    </div>

    <div v-else-if="post" class="post-details">
      <div class="mb-8">
        <!-- Image Display - UPDATED -->
        <div v-if="fullImageUrl" class="mb-6"> <!-- Use fullImageUrl in v-if -->
          <img
            :src="fullImageUrl"
            :alt="post.title"
            class="w-full max-h-[500px] object-cover rounded-lg shadow-lg transition-transform duration-300 hover:scale-[1.02]"
          >
        </div>
        
        <h1 class="text-4xl font-bold mb-4 text-gray-800">{{ post.title }}</h1>
        <div class="flex items-center text-gray-500 mb-6">
          <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 mr-2" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z" />
          </svg>
          <span>{{ new Date(post.created_at).toLocaleDateString('en-US', { year: 'numeric', month: 'long', day: 'numeric' }) }}</span>
        </div>
      </div>

      <div class="bg-white rounded-lg shadow-md p-6 mb-8">
        <div class="prose max-w-none text-gray-800" v-html="post.content"></div>
      </div>

      <!-- Like/Dislike Section -->
      <div class="bg-gray-50 rounded-lg border border-gray-200 p-6 mb-8">
         <h2 class="text-xl font-semibold mb-4 text-gray-700">What did you think?</h2>
        <div class="flex items-center space-x-4">
          <button
            @click="handleLike('like')"
            class="flex items-center px-4 py-2 rounded-full transition"
            :class="{
              'bg-green-500 text-white hover:bg-green-600': post.userLikeStatus === 'like',
              'bg-gray-100 text-gray-700 hover:bg-gray-200': post.userLikeStatus !== 'like',
            }"
          >
            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 mr-2" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M14 10h4.764a2 2 0 011.789 2.894l-3.5 7A2 2 0 0115.263 21h-4.017c-.163 0-.326-.02-.485-.06L7 20m7-10V5a2 2 0 00-2-2h-.095c-.5 0-.905.405-.905.905 0 .714-.211 1.412-.608 2.006L7 11v9m7-10h-2M7 20H5a2 2 0 01-2-2v-6a2 2 0 012-2h2.5" />
            </svg>
            <span>Like</span>
            <span class="ml-2 bg-white text-gray-700 rounded-full px-2 py-0.5 text-sm">{{ post.likes }}</span>
          </button>

          <button
            @click="handleLike('dislike')"
            class="flex items-center px-4 py-2 rounded-full transition"
            :class="{
              'bg-red-500 text-white hover:bg-red-600': post.userLikeStatus === 'dislike',
              'bg-gray-100 text-gray-700 hover:bg-gray-200': post.userLikeStatus !== 'dislike',
            }"
          >
            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 mr-2" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10 14H5.236a2 2 0 01-1.789-2.894l3.5-7A2 2 0 018.736 3h4.018a2 2 0 01.485.06l3.76.94m-7 10v5a2 2 0 002 2h.095c.5 0 .905-.405.905-.905 0-.714.211-1.412.608-2.006L17 13V4m-7 10h2m5-10h2a2 2 0 012 2v6a2 2 0 01-2 2h-2.5" />
            </svg>
            <span>Dislike</span>
            <span class="ml-2 bg-white text-gray-700 rounded-full px-2 py-0.5 text-sm">{{ post.dislikes }}</span>
          </button>

          <button
            v-if="post.userLikeStatus"
            @click="handleRemoveLike"
            class="flex items-center px-4 py-2 rounded-full bg-yellow-100 text-yellow-700 hover:bg-yellow-200 transition"
          >
            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 mr-1" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
            </svg>
            <span>Remove {{ post.userLikeStatus }}</span>
          </button>
        </div>
      </div>

      <!-- Admin Actions -->
      <div class="mb-8 flex justify-end" v-if="authStore.isAuthenticated && authStore.isAdmin">
        <div class="flex space-x-4">
          <router-link
            :to="{ name: 'editPost', params: { id: post.id }}"
            class="flex items-center px-4 py-2 bg-mint-600 text-white rounded-md hover:bg-mint-700 transition"
                      >
            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 mr-2" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z" />
            </svg>
            Edit Post
          </router-link>

          <button
            @click="deleteCurrentPost"
            class="flex items-center px-4 py-2 bg-white border border-red-500 text-red-500 rounded-md hover:bg-red-50 transition"
          >
            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 mr-2" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" />
            </svg>
            Delete Post
          </button>
        </div>
      </div>

      <!-- Comments Section -->
      <div class="bg-white rounded-lg shadow-md overflow-hidden">
        <div class="border-b border-gray-200 bg-gray-50 px-6 py-4">
          <h2 class="text-xl font-semibold text-gray-800">Comments</h2>
        </div>
        <div class="p-6">
          <CommentList
            :post-id="post.id"
            class="mb-6"
            @comment-added="refreshPostData"
            @comment-deleted="refreshPostData"
            @comment-updated="refreshPostData"
          />
          <div class="border-t pt-6">
            <CommentForm :post-id="post.id" @comment-added="refreshPostData" />          </div>
        </div>
      </div>
    </div>

    <div v-else class="bg-yellow-100 border-l-4 border-yellow-500 text-yellow-700 p-4 rounded">
      <p class="font-medium">Post not found.</p>
    </div>
  </div>
</template>

<script setup>
// Import computed
import { computed, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { usePostsStore } from '@/stores/posts';
import { useAuthStore } from '@/stores/auth';
import CommentList from '@/components/comments/CommentList.vue';
import CommentForm from '@/components/comments/CommentForm.vue';

// Define base URL
const API_BASE_URL = import.meta.env.VITE_API_URL || 'http://localhost:3000';

const route = useRoute();
const router = useRouter();
const postsStore = usePostsStore();
const authStore = useAuthStore();
const post = computed(() => postsStore.currentPost);
const loading = computed(() => postsStore.loading);
const error = computed(() => postsStore.error);

// NEW: Computed property for the full image URL
const fullImageUrl = computed(() => {
  // Make sure post and post.value exist before accessing image_url
  if (!post.value || !post.value.image_url) {
    return ''; // Return empty string if no image URL
  }

  // Check if it's already an absolute URL (starts with http)
  if (post.value.image_url.startsWith('http')) {
    return post.value.image_url;
  }

  // Otherwise, prepend the base URL
  return `${API_BASE_URL}${post.value.image_url}`;
});

const refreshPostData = () => {
   postsStore.fetchPost(route.params.id);
};
onMounted(async () => {
    try { // Add try...catch for better error handling during fetch
        await postsStore.fetchPost(route.params.id);
    } catch (fetchError) {
        console.error("Error fetching post in PostView:", fetchError);
        // Error is already handled by the template's v-else-if="error",
        // but you could add specific logic here if needed.
         if (fetchError.response && fetchError.response.status === 404) {
            router.push('/404'); // Redirect on 404
        }
    }
});

const deleteCurrentPost = async () => {
  if (confirm('Are you sure you want to delete this post? This action cannot be undone.')) {
    try {
      await postsStore.deletePost(route.params.id);
      router.push('/');
    } catch (error) {
      console.error('Error deleting post:', error);
      // Optionally display an error message to the user
    }
  }
};

const handleLike = async (likeType) => {
  try {
    await postsStore.updateLikeStatus(route.params.id, likeType);
     // No need to manually fetch, updateLikeStatus should trigger fetchPost in the store
  } catch (error) {
    console.error('Error updating like status:', error);
     // Optionally display an error message to the user
  }
};

const handleRemoveLike = async () => {
  try {
    await postsStore.deleteLikeStatus(route.params.id);
     // No need to manually fetch, deleteLikeStatus should trigger fetchPost in the store
  } catch (error) {
    console.error('Error removing like:', error);
     // Optionally display an error message to the user
  }
};

// This function can be used to refresh comments and potentially post data (like comment count)
// if the stores don't handle it automatically after add/delete/update
const handleCommentAdded = () => {
  // Re-fetch the post data to update comment count or other related info if necessary
   postsStore.fetchPost(route.params.id);
   // The CommentList component should ideally re-fetch itself via its props watcher
   // or you could emit an event from CommentForm up to here, then pass it down to CommentList
};

</script>

<style scoped>
img {
  transition: transform 0.3s ease;
}

/* Optional: Style the prose content if needed */
.prose :deep(p) { /* Example targeting paragraphs within v-html */
  @apply mb-4 leading-relaxed;
}
.prose :deep(h2) {
  @apply text-2xl font-semibold mt-6 mb-3;
}
/* Add more styles for other elements like lists, code blocks etc. */
</style>