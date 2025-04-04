<template>
  <div class="register-form">
    <!-- Title changes based on context (handled by parent view) -->
    <h2>{{ isAdminRegistration ? 'Register New Admin' : 'Create an Account' }}</h2>
    <form @submit.prevent="handleSubmit">
      <div class="form-group">
        <label for="email">Email</label>
        <input
          id="email"
          v-model="email"
          type="email"
          placeholder="Enter email for the new user"
          required
        />
        <p v-if="errors.email" class="error-message">{{ errors.email }}</p>
      </div>

      <div class="form-group">
        <label for="password">Password</label>
        <input
          id="password"
          v-model="password"
          type="password"
          placeholder="Enter password (min 6 characters)"
          required
          minlength="6"
        />
        <p v-if="errors.password" class="error-message">{{ errors.password }}</p>
      </div>

      <div class="form-group">
        <label for="confirmPassword">Confirm Password</label>
        <input
          id="confirmPassword"
          v-model="confirmPassword"
          type="password"
          placeholder="Confirm password"
          required
        />
        <p v-if="passwordMismatch" class="error-message">Passwords do not match</p>
      </div>

      <p v-if="errorMessage" class="error-message">{{ errorMessage }}</p>
      <p v-if="successMessage" class="success-message">{{ successMessage }}</p>

      <!-- Admin Registration Checkbox REMOVED for admin context -->
      <!-- The logic now solely depends on isAdminRegistration computed property -->

      <button type="submit" :disabled="isSubmitting || passwordMismatch">
        {{ isSubmitting ? 'Registering...' : 'Register' }}
      </button>

      <!-- "Already have account?" link REMOVED for admin context -->
      <div class="actions" v-if="!isAdminRegistration">
        <router-link to="/login">Already have an account? Login</router-link>
      </div>
    </form>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue';
import { useRouter } from 'vue-router';
import { useAuthStore } from '@/stores/auth';
import { useUsersStore } from '@/stores/users';
import { useRoute } from 'vue-router';


const router = useRouter();
const authStore = useAuthStore();
const usersStore = useUsersStore();
const route = useRoute();

// Form data
const email = ref('');
const password = ref('');
const confirmPassword = ref('');
const isSubmitting = ref(false);
const errorMessage = ref('');
const successMessage = ref('');
const errors = ref({
  email: '',
  password: ''
});
// const isAdmin = ref(false); // REMOVED - No longer needed as checkbox is gone for admin context
const isAdminRegistration = computed(() => route.path === '/admin/register');


// Computed properties
const passwordMismatch = computed(() => {
  return password.value && confirmPassword.value && password.value !== confirmPassword.value;
});

const handleSubmit = async () => {
  // Reset error messages
  errors.value = {
    email: '',
    password: ''
  };
  errorMessage.value = '';
  successMessage.value = '';

  // Client-side validation
  if (passwordMismatch.value) {
    return;
  }

  try {
    isSubmitting.value = true;

    if (isAdminRegistration.value) {
      // Admin registration (always true on this route now)
      await usersStore.registerAdmin({
        email: email.value,
        password: password.value,
        // role: 'admin' is typically set on the backend for this route
      });
       successMessage.value = 'Admin user registered successfully! Redirecting...';
       // Redirect after a delay
       setTimeout(() => {
         router.push('/admin/users'); // Redirect to user list or dashboard
       }, 2000);

    } else {
        // Regular user registration
      await authStore.register({
        email: email.value,
        password: password.value
      });
       // Show success message and redirect after a delay
      successMessage.value = 'Registration successful! Redirecting to login...';
      setTimeout(() => {
          router.push('/login');
       }, 2000);
    }


  } catch (error) {
    console.error('Registration error:', error);

    // Handle validation errors
    if (error.response && error.response.data && error.response.data.errors) {
      const validationErrors = error.response.data.errors;
      validationErrors.forEach(err => {
        if (err.path === 'email') {
          errors.value.email = err.msg;
        } else if (err.path === 'password') {
          errors.value.password = err.msg;
        }
      });
    } else if (error.response && error.response.status === 409) {
      // Email already registered
      errors.value.email = 'Email already registered';
    } else if (error.response && error.response.data && error.response.data.message) {
      // Handle general error message
      errorMessage.value = error.response.data.message;
    } else {
      // Fallback error message
      errorMessage.value = 'Registration failed. Please try again.';
    }
  } finally {
    isSubmitting.value = false;
  }
};
</script>

<style scoped>
.register-form {
  max-width: 400px;
  margin: 0 auto;
  padding: 20px;

}

.form-group {
  margin-bottom: 15px;
}

label {
  display: block;
  margin-bottom: 5px;
  font-weight: bold;
}

input {
  width: 100%;
  padding: 8px;
  border: 1px solid #ddd;
  border-radius: 4px;
}

button {
  width: 100%;
  padding: 10px;
  /* Using mint color for consistency, adjust if needed */
  background-color: #34d399; /* Example mint color */
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  margin-top: 10px;
}

button:hover {
  background-color: #10b981; /* Darker mint */
}

button:disabled {
  background-color: #cccccc;
  cursor: not-allowed;
}

.error-message {
  color: red;
  font-size: 0.85rem;
  margin-top: 5px;
}

.success-message {
  color: green;
  font-size: 0.85rem;
  margin-top: 5px;
}

.actions {
  margin-top: 15px;
  text-align: center;
}

a {
  color: #2c3e50;
  text-decoration: none;
}

a:hover {
  text-decoration: underline;
}
</style>