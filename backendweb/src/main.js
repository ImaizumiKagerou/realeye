import {createApp} from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import installElementPlus from './plugins/element'
import './assets/css/icon.css'
const app = createApp(App)
installElementPlus(app)

// global
import { quillEditor } from 'vue3-quill'
app.use(quillEditor)

app
    .use(store)
    .use(router)
    .mount('#app')
