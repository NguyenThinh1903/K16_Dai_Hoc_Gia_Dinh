import { defineStore } from 'pinia';
import { ref, computed } from 'vue';
import commentService from '@/services/commentService';
import { useAuthStore } from './auth';

export const useCommentsStore = defineStore('comments', () => {
  const comments = ref([]);
  const loading = ref(false);
  const error = ref(null);
  const currentPage = ref(1);
  const totalPages = ref(1);
  const totalComments = ref(0);
  const currentComment = ref(null);

  const fetchComments = async (postId, page = 1, limit = 10) => {
    loading.value = true;
    error.value = null;
    try {
      const response = await commentService.getComments(postId, page, limit);
      comments.value = response.data.comments;
      currentPage.value = response.data.currentPage;
      totalPages.value = response.data.totalPages;
      totalComments.value = response.data.totalComments;
    } catch (err) {
      error.value = err;
    } finally {
      loading.value = false;
    }
  };

const addComment = async (postId, commentData) => {
  const authStore = useAuthStore();
  loading.value = true;
  error.value = null;
  try {
    // Optimistic update: Add the comment immediately to the local state
    const tempId = Date.now(); // Generate a temporary ID
    const newComment = {
      id: tempId,
      post_id: postId,
      content: commentData.content,
      created_at: new Date().toISOString(), // Use current time
      user_id: authStore.user ? authStore.user.id : null, // Get user ID
      visitor_id:  !authStore.user ? localStorage.getItem('guestId') : null, // Placeholder, adjust as needed
      // ... any other fields you need ...
    };

    comments.value.unshift(newComment); // Add to the beginning of the array
    totalComments.value += 1;  // Update total
    const response = await commentService.addComment(postId, commentData);
    // Replace the temporary comment with the one from the server
    const index = comments.value.findIndex(c => c.id === tempId);
    if (index !== -1) {
        comments.value[index] = response.data;
    }
    return response.data;
  } catch (err) {
      // Rollback: Remove the optimistically added comment
      const index = comments.value.findIndex(c => c.id === tempId);
      if (index !== -1) {
          comments.value.splice(index, 1);
          totalComments.value -= 1;
      }
    error.value = err;
    throw err; // Re-throw for component handling
  } finally {
    loading.value = false;
  }
};

  const updateComment = async (commentId, commentData) => {
    loading.value = true;
    error.value = null;
    try {
        const response = await commentService.updateComment(commentId, commentData);
        //No need to fetch comment
        return response.data
    } catch (error) {
        error.value = error;
        throw error;
    } finally {
        loading.value = false;
    }
  };

  const deleteComment = async (commentId) => {
    loading.value = true;
    error.value = null;
    try {
      await commentService.deleteComment(commentId);
      //No need to fetch comment

    } catch (err) {
      error.value = err;
    } finally {
      loading.value = false;
    }
  };

  const fetchComment = async(commentId) => {
    loading.value = true;
    error.value = null;
    try {
      const response = await commentService.getComment(commentId);
      currentComment.value = response.data;

    }catch (err) {
      error.value = err
    } finally {
      loading.value = false
    }
  };

  return {
    comments,
    loading,
    error,
    currentPage,
    totalPages,
    totalComments,
    currentComment,
    fetchComments,
    addComment,
    updateComment,
    deleteComment,
    fetchComment
  };
});