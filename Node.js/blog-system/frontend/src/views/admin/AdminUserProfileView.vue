// C:\thangs\NODE_JS\blog-system_backup\blog-system\frontend\src\views\admin\AdminUserProfileView.vue
<template>
  <div class="admin-user-profile">
    <h1 class="text-2xl font-bold mb-4">User Profile (Admin View)</h1>

    <div v-if="loading" class="text-center">Loading user profile...</div>
    <div v-else-if="error" class="text-red-500">Error: {{ error.message }}</div>
    <div v-else-if="user">
      <div class="mb-4">
        <label class="block text-gray-700 text-sm font-bold mb-2">User ID:</label>
        <p>{{ user.id }}</p>
      </div>
      <div class="mb-4">
        <label class="block text-gray-700 text-sm font-bold mb-2">Email:</label>
        <p>{{ user.email }}</p>
      </div>
       <div class="mb-4">
        <label class="block text-gray-700 text-sm font-bold mb-2">Role:</label>
        <p>{{ user.role }}</p>
      </div>
      <div class="mb-4">
        <label class="block text-gray-700 text-sm font-bold mb-2">Name:</label>
        <input v-if="isEditing" type="text" v-model="editedProfile.name" class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline" />
        <p v-else>{{ user.UserProfile?.name || 'Not set' }}</p>
      </div>
      <div class="mb-4">
        <label class="block text-gray-700 text-sm font-bold mb-2">Avatar URL:</label>
        <input v-if="isEditing" type="url" v-model="editedProfile.avatar" class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"/>
       <!-- Display the raw URL only if editing or no image -->
       <p v-if="isEditing || !fullAdminAvatarUrl">{{ user.UserProfile?.avatar || 'Not set' }}</p>
         <img v-if="!isEditing && fullAdminAvatarUrl" :src="fullAdminAvatarUrl" alt="User Avatar" class="mt-2 w-32 h-32 rounded-full object-cover" />
        <!-- Show a placeholder if no avatar -->
        <div v-if="!isEditing && !fullAdminAvatarUrl" class="mt-2 w-32 h-32 rounded-full bg-gray-200 flex items-center justify-center text-gray-400">
          No Avatar
        </div>
      </div>
      <div class="mb-4">
        <label class="block text-gray-700 text-sm font-bold mb-2">Bio:</label>
        <textarea v-if="isEditing" v-model="editedProfile.bio" rows="4" class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"></textarea>
        <p v-else>{{ user.UserProfile?.bio || 'Not set' }}</p>
      </div>
      <div>
        <button v-if="!isEditing" @click="startEditing" class="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline">
          Edit Profile
        </button>
        <div v-else>
          <button @click="saveProfile" class="bg-green-500 hover:bg-green-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline mr-2">
            Save Changes
          </button>
          <button @click="cancelEditing" class="bg-gray-500 hover:bg-gray-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline">
            Cancel
          </button>
        </div>
      </div>
    </div>
    <div v-else>User not found.</div>
     <p v-if="errorMessage" class="text-red-500 mt-2">{{ errorMessage }}</p>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, reactive } from 'vue';
import { useUsersStore } from '@/stores/users';
import { useRoute, useRouter } from 'vue-router';

// Define base URL (Make sure VITE_API_URL is set in your .env file)
const API_BASE_URL = import.meta.env.VITE_API_URL || 'http://localhost:3000';

const usersStore = useUsersStore();
const route = useRoute();
const router = useRouter();
const userId = route.params.userId;

const user = ref(null);
const loading = ref(false);
const error = ref(null);
const isEditing = ref(false);
const errorMessage = ref('');

const editedProfile = reactive({
  name: '',
  avatar: '',
  bio: '',
});

// Computed property to get the full avatar URL
const fullAdminAvatarUrl = computed(() => {
  const avatarPath = user.value?.UserProfile?.avatar;
  if (!avatarPath) {
    return ''; // No avatar path available
  }
  // Check if it's already an absolute URL
  if (avatarPath.startsWith('http://') || avatarPath.startsWith('https://')) {
    return avatarPath;
  }
  // Prepend base URL if it's a relative path
  return `${API_BASE_URL}${avatarPath}`;
});

onMounted(async () => {
  try {
    loading.value = true;
    user.value = await usersStore.fetchAnyUserProfile(userId);
     // Initialize editedProfile with current profile data
    initializeEditedProfile(); // Call the helper function
  } catch (err) {
    error.value = err;
    if (err.response && err.response.status === 404) {
      router.push('/404'); // Or some other error page
    }
  } finally {
    loading.value = false;
  }
});

// Helper function to initialize or reset the edit form
const initializeEditedProfile = () => {
  if (user.value && user.value.UserProfile) {
    editedProfile.name = user.value.UserProfile.name || '';
    editedProfile.avatar = user.value.UserProfile.avatar || ''; // Store the raw path for the input
    editedProfile.bio = user.value.UserProfile.bio || '';
  } else {
    // Reset if no profile data exists
    editedProfile.name = '';
    editedProfile.avatar = '';
    editedProfile.bio = '';
  }
};

const startEditing = () => {
  isEditing.value = true;

  initializeEditedProfile(); // Ensure form has latest data
};

const cancelEditing = () => {
  isEditing.value = false;

  initializeEditedProfile(); // Reset form to original values
};

const saveProfile = async () => {
 loading.value = true; // Indicate saving process
 errorMessage.value = ''; // Clear previous errors
  try {
   const updatedUserData = await usersStore.updateAnyUserProfile(userId, editedProfile);
    isEditing.value = false;

   // Update the local user ref with the potentially updated profile data from the response
   if (updatedUserData && user.value) {
       user.value.UserProfile = { ...user.value.UserProfile, ...updatedUserData }; // Merge updates
   }
   initializeEditedProfile(); // Re-sync edit form state
  } catch (error) {
    console.error('Error saving profile:', error);
    if (error.response && error.response.data && error.response.data.message) {
        // Handle general error message
        errorMessage.value = error.response.data.message;
     } else if (error.response?.data?.errors) {
        // Handle validation errors specifically
        errorMessage.value = error.response.data.errors.map(e => e.msg).join(', ');
      } else {
        // Fallback error message
        errorMessage.value = 'An error occurred. Please try again.';
      }
 } finally {
    loading.value = false; // End saving process
  }
};
</script>