<template>
  <div class="comment-form">
    <h4 class="text-md font-bold mb-2">{{ isEditing ? 'Edit Comment' : 'Add a Comment' }}</h4>
    <form @submit.prevent="handleSubmit">
      <div class="mb-4">
        <textarea
          v-model="commentData.content"
          rows="3"
          class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
          placeholder="Write your comment..."
          required
        ></textarea>
        <p v-if="errors.content" class="text-red-500 text-xs italic">{{ errors.content }}</p>
      </div>
       <p v-if="errorMessage" class="text-red-500 mt-2">{{ errorMessage }}</p>
      <div class="flex items-center justify-between">
        <button
          type="submit"
          :disabled="isSubmitting"
          class="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline"
        >
          {{ isSubmitting ? (isEditing ? 'Updating...' : 'Submitting...') : (isEditing ? 'Update Comment' : 'Submit Comment')}}
        </button>
         <button type="button" @click="cancel" v-if="isEditing" class="bg-gray-500 hover:bg-gray-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline">
          Cancel
        </button>
      </div>
    </form>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue';
import { useCommentsStore } from '@/stores/comments';
import { useRoute } from 'vue-router';
import { useAuthStore } from '@/stores/auth'; // Import AuthStore

const props = defineProps({
    postId: {
        type: [String, Number], // Allow both string and number
        required: false, // Make it optional
    },
    comment: {
      type: Object,
      required: false, // Make it optional, for adding new comments
    },
    isEditing: {
      type: Boolean,
      default: false,
    },
});
const emit = defineEmits(['comment-added','comment-updated']);

const commentsStore = useCommentsStore();
const route = useRoute();
const authStore = useAuthStore(); // Use AuthStore

const isSubmitting = ref(false);
const commentData = reactive({
  content: '',
});
const errors = reactive({
    content: ''
});
const errorMessage = ref('');

onMounted(() => {
  if (props.isEditing && props.comment) {
    // Populate form with existing comment data for editing
    commentData.content = props.comment.content;
  }
});

const handleSubmit = async () => {
  isSubmitting.value = true;
  errorMessage.value = '';
  Object.keys(errors).forEach(key => errors[key] = '');
  try {
    if (props.isEditing) {
        //Edit comment mode
        const updatedComment = await commentsStore.updateComment(props.comment.id, commentData);
        emit('comment-updated', updatedComment); // Emit event
    }else {
        // Add new comment mode
        const postId = props.postId || route.params.id;
        if (!postId) {
            throw new Error('Post ID is required to add a comment.');
        }
        const newComment = await commentsStore.addComment(postId, commentData);
        emit('comment-added', newComment); // Emit event
        commentData.content = ''; // Clear the form
    }

  } catch (error) {
     console.error('Error submitting comment:', error);
     if (error.response && error.response.data && error.response.data.errors) {
        const validationErrors = error.response.data.errors;
        validationErrors.forEach(err => {
            if(errors.hasOwnProperty(err.path)){
              errors[err.path] = err.msg;
            }
        });
    }  else if (error.response && error.response.data && error.response.data.message) {
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
    emit('cancel-edit');
};

</script>