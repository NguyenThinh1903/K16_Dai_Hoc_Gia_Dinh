<template>
  <div class="comment-item flex items-start space-x-3 py-4 border-b border-gray-100 last:border-b-0">
    <!-- Avatar Column -->
    <div class="flex-shrink-0">
      <img
        v-if="comment.user && fullAvatarUrl"
        :src="fullAvatarUrl"
        :alt="comment.user.profile?.name || 'User Avatar'"
        class="w-10 h-10 rounded-full object-cover bg-gray-200"
        @error="onAvatarError"
      />
      <!-- Placeholder for users without avatar or guest users -->
      <div v-else class="w-10 h-10 rounded-full bg-gray-300 flex items-center justify-center text-gray-500 font-semibold">
        <span v-if="comment.user?.profile?.name">{{ comment.user.profile.name[0].toUpperCase() }}</span>
        <svg v-else xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
           <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" />
        </svg>
      </div>
    </div>

    <!-- Comment Content Column -->
    <div class="flex-grow">
       <!-- Comment Header: Name and Date -->
      <div class="comment-header flex items-center mb-1 text-sm">
        <span class="font-semibold mr-2 text-gray-800">
          {{ comment.user?.profile?.name || 'Guest User' }}
        </span>
        <span class="text-gray-500">{{ formattedDate }}</span>
      </div>

      <!-- Comment Body -->
       <div v-if="isEditing" class="mt-2">
         <CommentForm
           :comment="comment"
           :isEditing="true"
           @comment-updated="handleCommentUpdated"
           @cancel-edit="cancelEditing"
          />
       </div>
      <p v-else class="comment-content text-gray-700 text-sm leading-relaxed">
        {{ comment.content }}
      </p>

      <!-- Actions (Edit/Delete) -->
      <div class="actions mt-2 space-x-3" v-if="canEditOrDelete && !isEditing">
          <button @click="startEditing" class="text-xs text-blue-600 hover:underline">Edit</button>
          <button @click="handleDelete" class="text-xs text-red-600 hover:underline">Delete</button>
      </div>
      <p v-if="deleteError" class="text-xs text-red-500 mt-1">{{ deleteError }}</p>
    </div>
  </div>
</template>

<script setup>
import { computed, ref } from 'vue';
import { useAuthStore } from '@/stores/auth';
import { useCommentsStore } from '@/stores/comments';
import CommentForm from './CommentForm.vue'; // Assuming CommentForm is in the same directory

// Define base URL - Make sure this is accessible, might need to import from a config file
const API_BASE_URL = import.meta.env.VITE_API_URL || 'http://localhost:3000';

const props = defineProps({
  comment: {
    type: Object,
    required: true,
  },
});

const emit = defineEmits(['comment-deleted', 'comment-updated']);

const authStore = useAuthStore();
const commentsStore = useCommentsStore();
const isEditing = ref(false);
const avatarLoadError = ref(false); // Track avatar loading errors
const deleteError = ref(''); // Track delete errors

// Construct full avatar URL
const fullAvatarUrl = computed(() => {
  const avatarPath = props.comment.user?.profile?.avatar;
  if (!avatarPath) return null; // No avatar set

  if (avatarPath.startsWith('http://') || avatarPath.startsWith('https://')) {
    return avatarPath; // Already absolute
  }
  // Prepend base URL if relative
  return `${API_BASE_URL}${avatarPath}`;
});

// Format date
const formattedDate = computed(() => {
  if (!props.comment.created_at) return '';
  try {
      // More robust date formatting
      return new Date(props.comment.created_at).toLocaleString(undefined, {
          year: 'numeric', month: 'short', day: 'numeric', hour: 'numeric', minute: '2-digit'
      });
  } catch (e) {
      console.error("Error formatting date:", e);
      return 'Invalid Date';
  }
});

// Handle avatar image loading error
const onAvatarError = () => {
  console.warn(`Failed to load avatar: ${fullAvatarUrl.value}`);
  avatarLoadError.value = true; // You could use this to show a different placeholder
  // For simplicity, we'll just let the existing placeholder logic handle it via v-else
};

// Determine if the current user/guest can edit or delete
const canEditOrDelete = computed(() => {
  // Admin can always edit/delete
  if (authStore.isAdmin) {
    return true;
  }
  // Logged-in user can edit/delete their own comments
  if (authStore.isAuthenticated && props.comment.user_id === authStore.user?.id) {
    return true;
  }
  // Add logic for guest edits/deletes if implemented (usually based on visitor_id and time limit)
  // Example (Needs backend support):
  // if (!props.comment.user_id && props.comment.visitor_id === localStorage.getItem('guestId') && /* check time limit */) {
  //   return true;
  // }
  return false;
});

const handleDelete = async () => {
  deleteError.value = ''; // Clear previous error
  if (confirm('Are you sure you want to delete this comment?')) {
    try {
      await commentsStore.deleteComment(props.comment.id);
      emit('comment-deleted', props.comment.id); // Notify parent
    } catch (error) {
      console.error('Error deleting comment', error);
      deleteError.value = error.response?.data?.message || 'Failed to delete comment.';
    }
  }
};

const startEditing = () => {
  isEditing.value = true;
};

const cancelEditing = () => {
    isEditing.value = false;
};

const handleCommentUpdated = (updatedCommentData) => {
    // Note: updatedCommentData might just be the { content: '...' } if the API only returns that on update.
    // The parent list usually refetches to get the full updated object including potentially edited timestamp.
    isEditing.value = false;
    // Emit the ID or the partial data, parent will likely refetch anyway
    emit('comment-updated', { id: props.comment.id, ...updatedCommentData });
};
</script>

<style scoped>
/* Add minor styles if needed, Tailwind handles most */
.comment-item:last-child {
  border-bottom: none;
}
</style>