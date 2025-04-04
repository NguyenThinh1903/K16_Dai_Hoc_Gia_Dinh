
<template>
  <div class="profile-view">
    <!-- Mint Themed Banner -->
    <div class="bg-mint-500 text-white p-8 rounded-t-lg shadow-md">
      <h1 class="text-3xl font-bold">My Profile</h1>
      <p class="text-mint-100">Manage your personal information</p>
    </div>

    <div v-if="loading" class="flex justify-center py-12">
      <div class="animate-spin rounded-full h-12 w-12 border-t-2 border-b-2 border-mint-500"></div>
    </div>

    <div v-else-if="error" class="bg-red-100 border-l-4 border-red-500 text-red-700 p-4 mb-6 rounded">
      <p class="font-medium">Error: {{ error.message }}</p>
      <p v-if="error.errors && Array.isArray(error.errors)">
        <span v-for="err in error.errors" :key="err.path" class="block text-sm">{{ err.msg }}</span>
      </p>
    </div>

    <div v-else-if="userProfile" class="bg-white rounded-b-lg shadow-md p-6">
      <div class="flex flex-col md:flex-row md:space-x-8">
        <div class="md:w-1/3 mb-6 md:mb-0">
          <div class="flex flex-col items-center">
            <!-- *** MODIFIED IMG TAG *** -->
            <img
              v-if="userProfile.UserProfile?.avatar && !avatarPreview" 
              :src="fullAvatarUrl"
              alt="User Avatar"
              class="w-40 h-40 rounded-full object-cover border-4 border-mint-100 shadow-md mb-4"
            />
            <img
              v-else-if="avatarPreview"
              :src="avatarPreview"
              alt="Avatar Preview"
              class="w-40 h-40 rounded-full object-cover border-4 border-mint-100 shadow-md mb-4"
            />
            <div v-else class="w-40 h-40 rounded-full bg-gray-200 flex items-center justify-center text-gray-400 text-5xl mb-4">
              <span>{{ userProfile.UserProfile?.name ? userProfile.UserProfile.name[0].toUpperCase() : '?' }}</span> <!-- Display initial or ? -->
            </div>
            <!-- End of Modified Section -->
            <h2 class="text-xl font-semibold text-gray-800">
              {{ userProfile.UserProfile?.name || 'Your Name' }}
            </h2>
            <p class="text-gray-500 text-sm">{{ userProfile.email }}</p>
            <span class="mt-2 px-3 py-1 text-xs font-medium rounded-full"
              :class="{
                'bg-purple-100 text-purple-800': userProfile.role === 'owner',
                'bg-indigo-100 text-indigo-800': userProfile.role === 'admin',
                'bg-mint-100 text-mint-800': userProfile.role === 'user'
              }">
              {{ userProfile.role }}
            </span>
          </div>
        </div>

        <div class="md:w-2/3">
          <div v-if="isEditing" class="space-y-4">
            <div>
              <label class="block text-gray-700 text-sm font-medium mb-2">Name:</label>
              <input
                type="text"
                v-model="editedProfile.name"
                class="w-full px-4 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-mint-500 transition"
                placeholder="Your name"
              />
            </div>

            <div>
              <label class="block text-gray-700 text-sm font-medium mb-2">Avatar:</label>
              <input
                type="file"
                @change="onAvatarChange"
                accept="image/*"
                class="w-full px-4 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-mint-500 transition file:mr-4 file:py-2 file:px-4 file:rounded-md file:border-0 file:bg-mint-600 file:text-white file:hover:bg-mint-700"
              />
               <p class="text-xs text-gray-500 mt-1">Upload a new image to change your avatar.</p>
            </div>

            <div>
              <label class="block text-gray-700 text-sm font-medium mb-2">Bio:</label>
              <textarea
                v-model="editedProfile.bio"
                rows="4"
                class="w-full px-4 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-mint-500 transition"
                placeholder="Tell us about yourself"
              ></textarea>
            </div>
          </div>

          <div v-else class="space-y-4">
            <div>
              <h3 class="text-lg font-medium text-gray-800 border-b border-gray-200 pb-1 mb-2">About Me</h3>
              <p class="text-gray-700 whitespace-pre-wrap">{{ userProfile.UserProfile?.bio || 'No bio added yet' }}</p> <!-- Added whitespace-pre-wrap -->
            </div>

            <div class="mt-4">
              <h3 class="text-lg font-medium text-gray-800 border-b border-gray-200 pb-1 mb-2">Contact Information</h3>
              <div class="flex items-center text-gray-700">
                <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 mr-2 text-gray-500" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 8l7.89 5.26a2 2 0 002.22 0L21 8M5 19h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v10a2 2 0 002 2z" />
                </svg>
                {{ userProfile.email }}
              </div>
            </div>
          </div>
        </div>
      </div>

      <div class="mt-8 border-t border-gray-200 pt-6" v-if="authStore.isAuthenticated && authStore.user.id === userProfile.id">
        <div v-if="!isEditing" class="flex flex-wrap gap-3">
          <button
            @click="startEditing"
            class="px-4 py-2 bg-mint-600 text-white rounded-md hover:bg-mint-700 transition flex items-center"
          >
            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 mr-1" fill="none" viewBox="0 0 24 24" stroke="currentColor" >
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z" />
            </svg>
            Edit Profile
          </button>

          <button
            @click="deleteAccount"
            class="px-4 py-2 bg-white border border-red-500 text-red-500 rounded-md hover:bg-red-50 transition flex items-center"
          >
            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 mr-1" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" />
            </svg>
            Delete Account
          </button>
        </div>

        <div v-else class="flex flex-wrap gap-3">
          <button
            @click="saveProfile"
            :disabled="isSaving"
            class="px-4 py-2 bg-green-600 text-white rounded-md hover:bg-green-700 transition flex items-center disabled:bg-gray-400 disabled:cursor-not-allowed"
          >
            <svg v-if="isSaving" class="animate-spin -ml-1 mr-3 h-5 w-5 text-white" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
              <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
              <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
            </svg>
            <svg v-else xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 mr-1" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7" />
            </svg>
            {{ isSaving ? 'Saving...' : 'Save Changes' }}
          </button>

          <button
            @click="cancelEditing"
            :disabled="isSaving" 
            class="px-4 py-2 bg-gray-500 text-white rounded-md hover:bg-gray-600 transition flex items-center disabled:opacity-50 disabled:cursor-not-allowed">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 mr-1" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
            </svg>
            Cancel
          </button>
        </div>
      </div>
    </div>

    <div v-else class="bg-yellow-100 border-l-4 border-yellow-500 text-yellow-700 p-4 rounded">
      <p class="font-medium">Profile not found.</p>
    </div>

    <p v-if="errorMessage" class="mt-4 bg-red-100 border-l-4 border-red-500 text-red-700 p-4 rounded">
      {{ errorMessage }}
    </p>
  </div>
</template>

<script setup>
// *** ADD computed TO IMPORTS ***
import { ref, computed, onMounted, reactive } from 'vue';
import { useUsersStore } from '@/stores/users';
import { useAuthStore } from '@/stores/auth';
import { useRouter } from 'vue-router';

// *** DEFINE API_BASE_URL ***
const API_BASE_URL = import.meta.env.VITE_API_URL || 'http://localhost:3000';

const usersStore = useUsersStore();
const authStore = useAuthStore();
const router = useRouter();

const userProfile = computed(() => usersStore.userProfile);
const loading = computed(() => usersStore.loading);
const error = computed(() => usersStore.error); // Use computed error
const isEditing = ref(false);
const errorMessage = ref('');
const selectedFile = ref(null);
const avatarPreview = ref(null);
const isSaving = ref(false); // Added saving state

const editedProfile = reactive({
  name: '',
  bio: '',
});

// *** ADD fullAvatarUrl COMPUTED PROPERTY ***
const fullAvatarUrl = computed(() => {
  const avatar = userProfile.value?.UserProfile?.avatar;
  if (!avatar) {
    return ''; // No avatar set
  }
  if (avatar.startsWith('http://') || avatar.startsWith('https://')) {
    return avatar; // Already an absolute URL
  }
  // Prepend base URL for relative paths
  return `${API_BASE_URL}${avatar}`;
});

onMounted(async () => {
  await usersStore.fetchUserProfile();
  // Initialize edit form *after* profile is loaded
  initializeEditedProfile();
});

// Helper function to initialize the edit form
const initializeEditedProfile = () => {
    if (userProfile.value && userProfile.value.UserProfile) {
        editedProfile.name = userProfile.value.UserProfile.name || '';
        editedProfile.bio = userProfile.value.UserProfile.bio || '';
    } else {
        editedProfile.name = '';
        editedProfile.bio = '';
    }
};

const startEditing = () => {
  isEditing.value = true;
  avatarPreview.value = null; // Reset preview when starting edit
  errorMessage.value = ''; // Clear previous errors
  initializeEditedProfile(); // Ensure form has latest data
};

const cancelEditing = () => {
  isEditing.value = false;
  avatarPreview.value = null; // Reset preview
  selectedFile.value = null; // Clear selected file
  errorMessage.value = ''; // Clear errors
  initializeEditedProfile(); // Reset form to original values
};

const onAvatarChange = (event) => {
    const file = event.target.files[0];
    errorMessage.value = ''; // Clear previous errors
    const maxSize = 5 * 1024 * 1024; // 5MB

    if (file) {
        // Basic validation
        if (!file.type.startsWith('image/')) {
            errorMessage.value = 'Please select an image file.';
            selectedFile.value = null;
            avatarPreview.value = null;
            event.target.value = ''; // Clear the input
            return;
        }
        if (file.size > maxSize) {
             errorMessage.value = 'Image size must be less than 5MB.';
            selectedFile.value = null;
            avatarPreview.value = null;
            event.target.value = ''; // Clear the input
            return;
        }

        selectedFile.value = file;
        // Create preview URL
        const reader = new FileReader();
        reader.onload = (e) => {
            avatarPreview.value = e.target.result;
        };
        reader.readAsDataURL(file);

    } else {
        selectedFile.value = null;
        avatarPreview.value = null;
    }
};

const saveProfile = async () => {
    isSaving.value = true; // Set saving state
    errorMessage.value = ''; // Clear previous errors

    const formData = new FormData();
    // Only append fields if they are not null/undefined, or handle on backend
    formData.append('name', editedProfile.name ?? '');
    formData.append('bio', editedProfile.bio ?? '');
    if (selectedFile.value) {
        formData.append('avatar', selectedFile.value);
    }

    try {
        const response = await usersStore.updateUserProfile(formData);
        // Update successful
        isEditing.value = false;
        selectedFile.value = null; // Clear selected file
        avatarPreview.value = null; // Clear preview
        // The store update should automatically refresh the view via computed properties
        // If the store doesn't update the nested UserProfile correctly, you might need:
        // await usersStore.fetchUserProfile(); // Re-fetch to be sure
         initializeEditedProfile(); // Refresh form state just in case
    } catch (err) { // Catch specific error from store
        console.error("Failed to save profile", err);
        // Extract specific error messages if available
        if (err.response?.data?.errors && Array.isArray(err.response.data.errors)) {
            errorMessage.value = err.response.data.errors.map(e => e.msg).join(', ');
        } else if (err.response?.data?.message) {
            errorMessage.value = err.response.data.message;
        } else {
            errorMessage.value = 'An error occurred while saving. Please try again.';
        }
    } finally {
        isSaving.value = false; // Reset saving state
    }
};

const deleteAccount = async () => {
  // Add confirmation
  if(confirm('Are you sure you want to delete your account? This action is permanent and cannot be undone.')) {
    isSaving.value = true; // Use saving state for delete as well
    errorMessage.value = '';
    try {
      await authStore.deleteUserAccount();
      // Logout happens automatically via interceptor or store logic usually
      // No need to call authStore.logout() here if the API call triggers it
      router.push('/login?message=Account deleted successfully'); // Redirect with message
    } catch (err) {
      console.error('Error deleting account', err);
       if (err.response?.data?.message) {
            errorMessage.value = err.response.data.message;
        } else {
            errorMessage.value = 'Failed to delete account. Please try again.';
        }
         isSaving.value = false; // Reset saving state only on error
    }
    // Don't reset isSaving here if successful, as we are navigating away
  }
};
</script>

<style scoped>
/* Add styles for the saving spinner or disabled states if needed */
</style>