import { defineStore } from 'pinia';
import { ref, computed } from 'vue';
import postService from '@/services/postService';
import { useAuthStore } from './auth'; // Import useAuthStore

export const usePostsStore = defineStore('posts', () => {
  const posts = ref([]);
  const currentPost = ref(null);
  const loading = ref(false);
  const error = ref(null);
  const currentPage = ref(1);
  const totalPages = ref(1);
  const totalPosts = ref(0);


  const fetchPosts = async (params = {}) => {
    loading.value = true;
    error.value = null;
    try {
      const response = await postService.getAllPosts(params);
      posts.value = response.data.posts;
      currentPage.value = response.data.currentPage;
      totalPages.value = response.data.totalPages;
      totalPosts.value = response.data.totalPosts;

    } catch (err) {
      error.value = err;
    } finally {
      loading.value = false;
    }
  }

  const fetchPost = async (id) => {
    loading.value = true;
    error.value = null;
    currentPost.value = null; // Reset currentPost
    try {
      const response = await postService.getPost(id);
      currentPost.value = response.data;

      // Check if the user has liked/disliked the post and set userLikeStatus
      if (response.data && response.data.userLikeStatus) {
            currentPost.value.userLikeStatus = response.data.userLikeStatus;
        } else {
            currentPost.value.userLikeStatus = null; // or 'none' if you prefer
        }


    } catch (err) {
      error.value = err;
       throw err; // Re-throw the error for the component to handle
    } finally {
      loading.value = false;
    }
  };

  const createPost = async (postData) => {
    const authStore = useAuthStore(); // Access authStore
      if (!authStore.isAuthenticated) {
        throw new Error('User must be logged in to create a post.');
      }
    loading.value = true;
    error.value = null;
    try {
      const response = await postService.createPost(postData);
      //  fetchPosts(); // Refresh the list after creating. Consider pagination.
       return response;
    } catch (err) {
      error.value = err;
      throw err; // Important for form handling
    } finally {
      loading.value = false;
    }
  };

  const updatePost = async (id, postData) => {
      const authStore = useAuthStore(); // Access authStore
      if (!authStore.isAuthenticated) {
        throw new Error('User must be logged in to update a post.');
      }
    loading.value = true;
    error.value = null;
    try {
      const response = await postService.updatePost(id, postData);
      //  fetchPosts();  // Refresh list.
       return response;
    } catch (err) {
      error.value = err;
       throw err; // Important for form handling
    } finally {
      loading.value = false;
    }
  };

  const deletePost = async (id) => {
      const authStore = useAuthStore(); // Access authStore
      if (!authStore.isAuthenticated || !authStore.isAdmin) {
           throw new Error('Only admin users can delete posts.');
        }
    loading.value = true;
    error.value = null;
    try {
      await postService.deletePost(id);
      fetchPosts(); // Refresh the list
    } catch (err) {
      error.value = err;
    } finally {
      loading.value = false;
    }
  };

  const searchPosts = async (query, page = 1, limit = 10) => {
    loading.value = true;
    error.value = null;
    try {
        const response = await postService.searchPosts(query, page, limit);
        posts.value = response.data.posts;
        currentPage.value = response.data.currentPage;
        totalPages.value = response.data.totalPages;
        totalPosts.value = response.data.totalPosts;
    } catch (err) {
        error.value = err;
    } finally {
        loading.value = false;
    }
};

  const updateLikeStatus = async(postId, likeType) => {
        const authStore = useAuthStore();
        // if (!authStore.isAuthenticated) { // Removed this, handeling guest users on the backend
        //     // Handle the case where user is not authenticated (guest user)
        //     // You might want to display a message or redirect to login
        //     console.log("User not authenticated. Cannot like/dislike.");
        //     return; // Or throw an error, depending on your needs
        // }

      try{
        const response = await postService.updateLike(postId, likeType);
        //Update currentPost like and dislike
        await fetchPost(postId);

      } catch (error) {
        console.error('Error updating like status:', error);
         throw error;
      }
  };

  const deleteLikeStatus = async (postId) => {
    const authStore = useAuthStore();
        // if (!authStore.isAuthenticated) { // Removed this
        //     console.log("User not authenticated. Cannot remove like/dislike.");
        //     return;
        // }
    try {
      const response = await postService.deleteLike(postId);
      //Update currentPost like and dislike
      await fetchPost(postId);

    } catch (error) {
        console.error('Error removing like:', error)
        throw error;
    }
  };

  return {
    posts,
    currentPost,
    loading,
    error,
    currentPage,
    totalPages,
    totalPosts,
    fetchPosts,
    fetchPost,
    createPost,
    updatePost,
    deletePost,
    searchPosts,
    updateLikeStatus,
    deleteLikeStatus
  };
});