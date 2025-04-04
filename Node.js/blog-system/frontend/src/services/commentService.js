import api from './api';

const commentService = {
  async getComments(postId, page = 1, limit = 10) {
    const response = await api.get(`/api/posts/${postId}/comments`, { // Correct endpoint
      params: { page, limit },
    });
    return response.data;
  },

  async addComment(postId, commentData) {
    const response = await api.post(`/api/posts/${postId}/comments`, commentData); // Correct endpoint
    return response.data;
  },

  async updateComment(commentId, commentData) {
    const response = await api.put(`/api/comments/${commentId}`, commentData); // Correct endpoint
    return response.data;
  },

  async deleteComment(commentId) {
    const response = await api.delete(`/api/comments/${commentId}`); // Correct endpoint
    return response.data;
  },
  async getComment(commentId) {
    const response = await api.get(`/api/comments/${commentId}`);
    return response.data;
  },
};

export default commentService;