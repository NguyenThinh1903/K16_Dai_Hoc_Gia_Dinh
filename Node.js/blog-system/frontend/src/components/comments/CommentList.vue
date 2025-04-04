// C:\thangs\NODE_JS\blog-system_backup\blog-system\frontend\src\components\comments\CommentList.vue
<template>
  <div class="comment-list">
    <!-- REMOVED: <h3 class="text-lg font-bold mb-3">Comments ({{ totalComments }})</h3> -->

    <div v-if="loading" class="text-center py-4 text-gray-500">Loading comments...</div>
    <div v-else-if="error" class="text-center py-4 text-red-500">Error loading comments.</div>
    <div v-else-if="comments.length === 0" class="text-center py-4 text-gray-500">No comments yet. Be the first!</div>
    <div v-else class="space-y-4"> 
      <CommentItem
        v-for="comment in comments"
        :key="comment.id"
        :comment="comment"
        @comment-deleted="handleCommentDeleted"
        @comment-updated="handleCommentUpdated"
      />
       <!-- Pagination -->
       <div class="pagination mt-6 flex justify-center items-center space-x-2" v-if="totalPages > 1">
          <button
            @click="loadPage(currentPage - 1)"
            :disabled="currentPage === 1"
            class="px-3 py-1 rounded bg-gray-200 hover:bg-gray-300 disabled:bg-gray-100 disabled:text-gray-400 disabled:cursor-not-allowed transition text-sm"
          >
            « Prev
          </button>
          <span class="text-gray-600 text-sm">
            Page {{ currentPage }} of {{ totalPages }}
          </span>
          <button
            @click="loadPage(currentPage + 1)"
            :disabled="currentPage === totalPages"
            class="px-3 py-1 rounded bg-gray-200 hover:bg-gray-300 disabled:bg-gray-100 disabled:text-gray-400 disabled:cursor-not-allowed transition text-sm"
          >
            Next »
          </button>
       </div>
    </div>
  </div>
</template>

<script setup>
import { computed, watch } from 'vue';
import CommentItem from './CommentItem.vue';
import { useCommentsStore } from '@/stores/comments';

const props = defineProps({
  postId: {
    type: [String, Number],
    required: true,
  },
});

const emit = defineEmits(['comments-loaded']); // Optional: emit when comments are loaded/updated

const commentsStore = useCommentsStore();
const comments = computed(() => commentsStore.comments);
const loading = computed(() => commentsStore.loading);
const error = computed(() => commentsStore.error); // Expose error state
const currentPage = computed(() => commentsStore.currentPage);
const totalPages = computed(() => commentsStore.totalPages);
// totalComments is no longer directly used in the template, but might be useful elsewhere
// const totalComments = computed(() => commentsStore.totalComments);

const loadPage = async (page) => {
    if (page >= 1 && page <= totalPages.value && page !== currentPage.value) {
        try {
          await commentsStore.fetchComments(props.postId, page);
          emit('comments-loaded'); // Emit after successful load
        } catch (err) {
          console.error("Failed to load comments page:", err);
        }
    }
};

// Fetch comments when the postId changes or component mounts
watch(() => props.postId, async (newPostId) => {
  if (newPostId) {
    try {
      await commentsStore.fetchComments(newPostId, 1); // Reset to page 1
      emit('comments-loaded'); // Emit after initial load
    } catch (err) {
       console.error("Failed to load initial comments:", err);
    }
  }
}, { immediate: true }); // Use immediate: true to run on initial load

// Handler for when a comment is deleted by CommentItem
const handleCommentDeleted = async (deletedCommentId) => {
  console.log('Comment deleted event caught in list:', deletedCommentId);
  try {
      // Re-fetch comments for the current page to update the list and count
      await commentsStore.fetchComments(props.postId, currentPage.value);
      emit('comments-loaded'); // Emit after update
  } catch (err) {
     console.error("Failed to refetch comments after delete:", err);
  }
};

// Handler for when a comment is updated by CommentItem
const handleCommentUpdated = async (updatedComment) => {
  console.log('Comment updated event caught in list:', updatedComment);
   try {
     // Find and update the comment in the local list for instant feedback
     const index = comments.value.findIndex(c => c.id === updatedComment.id);
     if (index !== -1) {
        // Use spread syntax or Object.assign for reactivity
        comments.value[index] = { ...comments.value[index], ...updatedComment };
     }
     // Optionally, still refetch if backend might have changed other things
     // await commentsStore.fetchComments(props.postId, currentPage.value);
     emit('comments-loaded'); // Emit after update
   } catch (err) {
     console.error("Failed to update comment locally or refetch:", err);
   }
};

</script>

<style scoped>
/* Add any specific styling for the comment list container if needed */
</style>