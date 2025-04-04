// .\src\views\admin\UserManagementView.vue
<template>
  <div class="user-management">
    <h1 class="text-3xl font-bold mb-6">User Management</h1>
    <div v-if="loading" class="text-center">Loading users...</div>
    <div v-else-if="error" class="text-red-500">Error: {{ error.message }}</div>
    <div v-else>
      <table class="min-w-full divide-y divide-gray-200">
        <thead class="bg-gray-50">
          <tr>
            <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
              ID
            </th>
            <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
              Email
            </th>
            <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
              Role
            </th>
             <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
              Name
            </th>
            <th scope="col" class="relative px-6 py-3">
              <span class="sr-only">Actions</span>
            </th>
          </tr>
        </thead>
        <tbody class="bg-white divide-y divide-gray-200">
          <tr v-for="user in allUsers" :key="user.id">
            <td class="px-6 py-4 whitespace-nowrap">
              {{ user.id }}
            </td>
            <td class="px-6 py-4 whitespace-nowrap">
              {{ user.email }}
            </td>
            <td class="px-6 py-4 whitespace-nowrap">
              {{ user.role }}
            </td>
             <td class="px-6 py-4 whitespace-nowrap">
              {{ user.UserProfile?.name || 'N/A' }}
            </td>
            <td class="px-6 py-4 whitespace-nowrap text-right text-sm font-medium">
              <router-link :to="{ name: 'adminUserProfile', params: { userId: user.id } }" class="text-mint-600 hover:text-mint-900 mr-4">                View/Edit
              </router-link>
              <button @click="deleteUser(user.id)" class="text-red-600 hover:text-red-900">Delete</button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script setup>
import { onMounted, computed } from 'vue';
import { useUsersStore } from '@/stores/users';

const usersStore = useUsersStore();
const allUsers = computed(() => usersStore.allUsers);
const loading = computed(() => usersStore.loading);
const error = computed(() => usersStore.error);

onMounted(() => {
  usersStore.fetchAllUsers();
});

const deleteUser = async (userId) => {
  if (confirm('Are you sure you want to delete this user?')) {
      try {
        await usersStore.deleteAnyUser(userId);
        usersStore.fetchAllUsers(); // Refresh list after deletion
      } catch(error) {
        console.error("Failed to delete", error)
      }

  }
};
</script>