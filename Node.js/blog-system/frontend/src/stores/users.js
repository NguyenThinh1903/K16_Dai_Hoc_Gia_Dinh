import { defineStore } from 'pinia';
import { ref, computed } from 'vue';
import userService from '@/services/userService';

export const useUsersStore = defineStore('users', () => {
  const userProfile = ref(null);
  const loading = ref(false);
  const error = ref(null);
  const allUsers = ref([]);

  const fetchUserProfile = async () => {
    loading.value = true;
    error.value = null;
    try {
      const response = await userService.getUserProfile();
      userProfile.value = response.data;
    } catch (err) {
      error.value = err;
      // Don't automatically log out here.  The authStore handles that.
    } finally {
      loading.value = false;
    }
  };

  const updateUserProfile = async (profileData) => {
    loading.value = true;
    error.value = null;
    try {
      const response = await userService.updateUserProfile(profileData);
      userProfile.value.UserProfile = response.data; // Update the store
       return response;
    } catch (err) {
      error.value = err;
      throw err;
    } finally {
      loading.value = false;
    }
  };

  //Admin functions
  const fetchAllUsers = async() => {
    loading.value = true;
    error.value = null;
    try {
      const response = await userService.getAllUsers();
      allUsers.value = response.data.users;
    } catch (error) {
      error.value = error;
    } finally {
      loading.value = false;
    }
  };

  const fetchAnyUserProfile = async (userId) => {
        loading.value = true;
        error.value = null;
        try {
            const response = await userService.getAnyUserProfile(userId);
            return response.data; // Return the data, don't store in this store
        } catch (err) {
            error.value = err;
            throw err; // Re-throw for component to handle
        } finally {
            loading.value = false;
        }
    };

    const updateAnyUserProfile = async (userId, profileData) => {
        loading.value = true;
        error.value = null;
        try {
            const response = await userService.updateAnyUserProfile(userId, profileData);
            return response.data;
        } catch (err) {
            error.value = err;
            throw err; // Re-throw for component handling
        } finally {
            loading.value = false;
        }
    };

    const deleteAnyUser = async (userId) => {
        loading.value = true;
        error.value = null;
        try {
            await userService.deleteAnyUser(userId);
            // Optionally, refresh user list (if you have a list)
        } catch (err) {
            error.value = err;
        } finally {
            loading.value = false;
        }
    };
    const registerAdmin = async (userData) => {
        loading.value = true;
        error.value = null;
        try {
            const response = await userService.registerAdmin(userData);
            return response.data; // Return response
        } catch (err) {
            error.value = err;
            throw err; // Re-throw for component
        } finally {
            loading.value = false;
        }
    };


  return {
    userProfile,
    loading,
    error,
    allUsers,
    fetchUserProfile,
    updateUserProfile,
    fetchAllUsers,
    fetchAnyUserProfile,
    updateAnyUserProfile,
    deleteAnyUser,
    registerAdmin
  };
});