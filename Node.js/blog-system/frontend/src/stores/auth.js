
import { defineStore } from 'pinia';
import { ref, computed } from 'vue';
import authService from '@/services/authService';

export const useAuthStore = defineStore('auth', () => {
  // State
  const token = ref(localStorage.getItem('token') || null);
  const user = ref(null);
  const loading = ref(false);
  const error = ref(null); // Add error ref

  // Getters (computed properties)
  const isAuthenticated = computed(() => !!token.value);
  const isAdmin = computed(() => user.value && (user.value.role === 'admin' || user.value.role === 'owner'));
  const isOwner = computed(() => user.value && user.value.role === 'owner');

  // Actions
  async function login(credentials) {
    loading.value = true;
    error.value = null; // Reset error
    try {
      const response = await authService.login(credentials);
      setAuthData(response.token);
      await fetchUserProfile();
      return response;
    } catch (err) {
      error.value = err; // Store error
      throw err; // Re-throw for component handling
    } finally {
      loading.value = false;
    }
  }

  async function loginAsOwner(credentials) {
    loading.value = true;
    error.value = null;
    try {
      const response = await authService.loginAsOwner(credentials);
      setAuthData(response.token);
      await fetchUserProfile();
      return response;
     } catch (err) {
      error.value = err; // Store error
      throw err; // Re-throw for component handling
    } finally {
      loading.value = false;
    }
  }

  async function register(userData) {
    loading.value = true;
    error.value = null;
    try {
      const response = await authService.register(userData);
      return response;
    } catch(err){
      error.value = err;
      throw err; //Re throw the error
    } finally {
      loading.value = false;
    }
  }

  async function logout() {
    // Clear auth data
    localStorage.removeItem('token');
    localStorage.removeItem('guestId'); // Also remove guestId
    token.value = null;
    user.value = null;
  }

  async function fetchUserProfile() {
    if (!token.value) return null;

    loading.value = true;
    error.value = null;
    try {
      const userData = await authService.getUserProfile();
      user.value = userData.data;
      return userData;
    } catch (err) {
      error.value = err; // Store error
      // If there's an error fetching the profile (like an expired token)
      // clear the auth data
      if (err.response && err.response.status === 401) {
        logout();
      }
      throw err; // Re-throw for component handling
    } finally {
      loading.value = false;
    }
  }

  async function deleteUserAccount() {
      loading.value = true;
      error.value = null;
      try {
        await authService.deleteUserAccount();
        // No need to manually clear auth data here, as the logout action will be called.
      } catch(err) {
        error.value = err;
        throw err
      }
      finally {
          loading.value = false;
      }
    }

  // Helper function to set auth data
  function setAuthData(newToken) {
    localStorage.setItem('token', newToken);
    token.value = newToken;
  }

  // Initialize: Try to fetch user data if token exists
  if (token.value) {
    fetchUserProfile().catch(() => logout());
  }

  return {
    // State
    token,
    user,
    loading,
    error, // Expose error

    // Getters
    isAuthenticated,
    isAdmin,
    isOwner,

    // Actions
    login,
    loginAsOwner,
    register,
    logout,
    fetchUserProfile,
    deleteUserAccount
  };
});