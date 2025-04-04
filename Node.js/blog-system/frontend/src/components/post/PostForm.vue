<template>
  <div class="post-form">
    <h2 class="text-2xl font-bold mb-6 text-gray-800">{{ isEditing ? 'Edit Post' : 'Create Post' }}</h2>
    <form @submit.prevent="handleSubmit" class="space-y-6">
      <!-- Title -->
 <div>
        <label for="title" class="block text-sm font-medium text-gray-700 mb-2">Title</label>
        <input
          type="text"
          id="title"
          v-model="postData.title"
         class="w-full px-4 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-mint-500 transition"
          placeholder="Enter post title"
          required
        />
        <p v-if="errors.title" class="text-red-500 text-xs mt-1">{{ errors.title }}</p>
      </div>

      <!-- Content -->
      <div>
        <label for="content" class="block text-sm font-medium text-gray-700 mb-2">Content</label>
        <textarea
          id="content"
          v-model="postData.content"
          rows="6"
         class="w-full px-4 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-mint-500 transition"
          placeholder="Enter post content"
          required
        ></textarea>
        <p v-if="errors.content" class="text-red-500 text-xs mt-1">{{ errors.content }}</p>
      </div>

      <!-- Image (File Input) -->
       <div>
        <label for="image" class="block text-sm font-medium text-gray-700 mb-2">Image (Optional)</label>
        <!-- Preview existing image during editing -->
        <div v-if="isEditing && postData.image_url && !postData.image" class="mb-4">
          <img
            :src="postData.image_url"
            alt="Current Post Image"
            class="w-full max-h-64 object-cover rounded-md shadow-sm"
          >
          <p class="text-gray-500 text-sm mt-2">Upload a new image to replace the current one.</p>
        </div>
        <input
          type="file"
          id="image"
          @change="handleImageChange"
          accept=".jpg,.jpeg,.png,.gif"
          class="w-full px-4 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-mint-500 transition file:mr-4 file:py-2 file:px-4 file:rounded-md file:border-0 file:bg-mint-600 file:text-white file:hover:bg-mint-700"
        />
        <p v-if="errors.image" class="text-red-500 text-xs mt-1">{{ errors.image }}</p>
      </div>

      <!-- Status -->
      <div>
        <label for="status" class="block text-sm font-medium text-gray-700 mb-2">Status</label>
        <select
          id="status"
          v-model="postData.status"
        class="w-full px-4 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-mint-500 transition"
        >
          <option value="published">Published</option>
          <option value="draft">Draft</option>
        </select>
      </div>

      <!-- Submit/Cancel Buttons -->
      <div class="flex items-center justify-between">
        <button
          type="submit"
          :disabled="isSubmitting"
         class="px-6 py-2 bg-mint-600 text-white font-medium rounded-md hover:bg-mint-700 focus:outline-none focus:ring-2 focus:ring-mint-500 transition disabled:bg-gray-400 disabled:cursor-not-allowed"
        >
          {{ isSubmitting ? (isEditing ? 'Updating...' : 'Creating...') : (isEditing ? 'Update Post' : 'Create Post') }}
        </button>
        <button
          v-if="isEditing"
          type="button"
          @click="cancel"
          class="px-6 py-2 bg-gray-500 text-white font-medium rounded-md hover:bg-gray-600 transition"
        >
          Cancel
        </button>
      </div>
    </form>
    <p v-if="errorMessage" class="text-red-500 mt-4">{{ errorMessage }}</p>
  </div>
</template>

<script setup>
import { ref, onMounted, reactive, computed } from 'vue';
import { usePostsStore } from '@/stores/posts';
import { useRouter, useRoute } from 'vue-router';

const postsStore = usePostsStore();
const router = useRouter();
const route = useRoute();

const isEditing = computed(() => !!route.params.id);  // Determine if editing
const isSubmitting = ref(false);
const errorMessage = ref('');
const errors = reactive({
    title: '',
    content: '',
    image: ''
});

const postData = reactive({
  title: '',
  content: '',
  status: 'published',
  image: undefined, // Holds the File object
  image_url: ''
});


onMounted(async () => {
  if (isEditing.value) {
    try{
       await postsStore.fetchPost(route.params.id);
        // Populate form with existing post data
        Object.assign(postData, postsStore.currentPost); // Use Object.assign for reactivity
        postData.image = undefined; // Reset image, as it should be a File object

    } catch (error) {
       console.error("Failed to fetch post:", error);
      if (error.response && error.response.status === 404) {
        router.push('/404');
      } else {
        errorMessage.value = "Failed to load post. Please try again later.";
      }
    }

  }
});

const handleImageChange = (event) => {
  const file = event.target.files[0];
  if (file) {
     // Basic file type validation
    if (!file.type.startsWith('image/')) {
      errors.image = 'Please select an image file.';
      postData.image = null; // Clear invalid file
      return;
    }

    // File size validation (e.g., 5MB limit)
    const maxSize = 5 * 1024 * 1024; // 5MB in bytes
    if (file.size > maxSize) {
      errors.image = 'Image size should be less than 5MB.';
      postData.image = null;
      return;
    }
    errors.image = ''; // Clear previous error
    postData.image = file;  // Store the File object
  } else {
    postData.image = null;
  }
};

const handleSubmit = async () => {
  isSubmitting.value = true;
  errorMessage.value = '';
  Object.keys(errors).forEach(key => errors[key] = ''); // Clear previous errors

  try {
    const formData = new FormData();
    formData.append('title', postData.title);
    formData.append('content', postData.content);
    formData.append('status', postData.status);
    if (postData.image) {
      formData.append('image', postData.image);
    }

    if (isEditing.value) {
      await postsStore.updatePost(route.params.id, formData); // Pass FormData
    } else {
      await postsStore.createPost(formData); // Pass FormData
    }

    router.push('/'); // Redirect after success

  } catch (error) {
      console.error('Error submitting post:', error);
    if (error.response && error.response.data && error.response.data.errors) {
      const validationErrors = error.response.data.errors;
      validationErrors.forEach(err => {
        if(errors.hasOwnProperty(err.path)){
          errors[err.path] = err.msg;
        }
        if(err.path === 'image_url'){
            errors.image = err.msg;
        }
      });
     } else if (error.response && error.response.data && error.response.data.message) {
        // Handle general error message
        errorMessage.value = error.response.data.message;
      } else {
        // Fallback error message
        errorMessage.value = 'An error occurred. Please try again.';
      }
  } finally {
    isSubmitting.value = false;
  }
};

const cancel = () => {
    router.back();
};
</script>