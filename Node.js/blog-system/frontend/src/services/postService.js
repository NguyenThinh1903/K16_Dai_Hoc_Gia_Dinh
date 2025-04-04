import api from './api';

const postService = {
  async getAllPosts(params = {}) {
    const response = await api.get('/api/posts', { params }); // Correct endpoint
    return response.data;
  },

  async getPost(id) {
      const response = await api.get(`/api/posts/${id}`);
      return response.data;
  },

    async createPost(postData) {
    //No change for endpoint, but postData is now sending correctly
    const response = await api.post('/api/posts', postData);
    return response.data;
  },

    async updatePost(id, postData) {
    // No change for endpoint, but postData is now sending correctly
    const response = await api.put(`/api/posts/${id}`, postData);
    return response.data;
  },

  async deletePost(id) {
    const response = await api.delete(`/api/posts/${id}`); // Correct endpoint
    return response.data;
  },

  async searchPosts(query, page = 1, limit = 10) {
    const response = await api.get('/api/posts/search', { // Correct endpoint
      params: { q: query, page, limit }
    });
    return response.data;
  },

    async updateLike(postId, likeType) {
    const response = await api.put(`/api/posts/${postId}/likes`, { like_type: likeType });
    return response.data;
  },

  async deleteLike(postId) {
    const response = await api.delete(`/api/posts/${postId}/likes`);
    return response.data;
  },
};

export default postService;