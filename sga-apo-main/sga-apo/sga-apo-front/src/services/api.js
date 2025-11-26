import axios from 'axios';

const api = axios.create({
    baseURL: 'http://localhost:8080/api/v1', // URL do teu Back-end Java
    headers: {
        'Content-Type': 'application/json'
    }
});

// Interceptor: Antes de cada pedido, verifica se existe um token
// e adiciona-o ao cabeçalho "Authorization"
api.interceptors.request.use((config) => {
    const token = localStorage.getItem('token');
    
    if (token) {
        config.headers.Authorization = `Bearer ${token}`;
    }
    
    return config;
}, (error) => {
    return Promise.reject(error);
});

export default api;