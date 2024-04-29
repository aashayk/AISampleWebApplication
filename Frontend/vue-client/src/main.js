import { createApp } from 'vue'
import App from './App.vue'
import UploadFile from './components/UploadFile.vue'
import EmployeeForm from './components/EmployeeForm.vue'
import { createRouter, createWebHistory } from 'vue-router';
 
const router = createRouter({
  history: createWebHistory(),
  routes: [
    { path: '/', component: EmployeeForm },
    { path: '/upload', component: UploadFile },
  ]
});
 
createApp(App).use(router).mount('#app');