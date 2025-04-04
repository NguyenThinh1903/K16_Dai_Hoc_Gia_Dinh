import api from './api';

const userService = {
  async getUserProfile() {
    const response = await api.get('/api/profile'); // Correct endpoint
    return response.data;
  },

  async updateUserProfile(profileData) {
      //No change needed
    const response = await api.patch('/api/profile', profileData); // Correct endpoint
    return response.data;
  },
  async deleteUserAccount() {
        const response = await api.delete('/api/profile'); // Correct endpoint
        return response.data;
  },
    //Admin functions
   async getAllUsers() {
        const response = await api.get('/api/admin/users');
        return response.data;
    },

    async getAnyUserProfile(userId) {
        const response = await api.get(`/api/users/${userId}/profile`);
        return response.data;
    },

    async updateAnyUserProfile(userId, profileData) {
        const response = await api.put(`/api/admin/users/${userId}/profile`, profileData);
        return response.data;
    },

    async deleteAnyUser(userId) {
        const response = await api.delete(`/api/admin/users/${userId}/profile`);
        return response.data;
    },
    async registerAdmin(userData) {
        const response = await api.post('/api/admin/register', userData);
        return response.data;
    },
};

export default userService;