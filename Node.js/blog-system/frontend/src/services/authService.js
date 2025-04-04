import api from './api';

const authService = {
  /**
   * Login as a regular user
   * @param {Object} credentials - User credentials
   * @param {string} credentials.email - User email
   * @param {string} credentials.password - User password
   * @returns {Promise<Object>} - Response with token
   */
  async login(credentials) {
    const response = await api.post('/api/auth/user-login', credentials); // Use correct endpoint
    return response.data;
  },

  /**
   * Login as the blog owner/admin
   * @param {Object} credentials - Owner credentials
   * @param {string} credentials.email - Owner email
   * @param {string} credentials.password - Owner password
   * @returns {Promise<Object>} - Response with token
   */
  async loginAsOwner(credentials) {
    const response = await api.post('/api/auth/login', credentials); // Use correct endpoint
    return response.data;
  },

  /**
   * Register a new user
   * @param {Object} userData - New user data
   * @param {string} userData.email - User email
   * @param {string} userData.password - User password
   * @returns {Promise<Object>} - Response with user data
   */
  async register(userData) {
    const response = await api.post('/api/register', userData); // Corrected endpoint
    return response.data;
  },

  /**
   * Get the current user's profile
   * @returns {Promise<Object>} - User profile data
   */
  async getUserProfile() {
    const response = await api.get('/api/profile'); // Corrected endpoint
    return response.data;
},
  /**
   * Update the current user's profile
   * @param {Object} profileData - Profile data to update
   * @param {string} [profileData.name] - User's name
   * @param {string} [profileData.avatar] - User's avatar URL
   * @param {string} [profileData.bio] - User's bio
   * @returns {Promise<Object>} - Updated profile data
   */
  async updateUserProfile(profileData) {
    const response = await api.patch('/api/profile', profileData); // Correct endpoint
    return response.data;
},

  /**
   * Delete the current user's account
   * @returns {Promise<Object>} - Response message
   */
  async deleteUserAccount() {
    const response = await api.delete('/api/profile'); // Correct endpoint
    return response.data;
}
};

export default authService;