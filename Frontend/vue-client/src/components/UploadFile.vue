<template>
    <div class="main-block">
        <div class="left-part">
            <form action="/">
                <h1>Upload Resume</h1>
                <div class="info">
                    <input v-bind:disabled="available" type="file" accept=".pdf" ref="fileInput" @change="handleFileChange">
                </div>
                <button v-bind:disabled="available" @click="uploadFile($event)">Upload Resume</button>
                <br/><br><button @click="goHome()">Go Home</button>
            </form>
        </div>
        <div v-if="available">
            <form action="/">
                <h1 v-if="available">Summary</h1>
                <div v-if="available">
                    <p v-for="(passage,index) in summary.split('\n')" :key="index">{{ passage }} </p>
                </div>
            </form>
        </div>
    </div>
</template>
 
<script>
 
import axios from 'axios';
import 'vue3-toastify/dist/index.css'
import {toast} from 'vue3-toastify'
 
export default {
    name: 'UploadFile',
    data() {
        return {
            file: null,
            summary: null,
            available: false,
            email: localStorage.getItem('email'),
            // email:'aashay@gmail.com',
        }
    },
    methods: {
        goHome(){
            localStorage.clear();
            this.email=null
            this.$router.push('/')
            
        },
        handleFileChange(event) {
            this.file = event.target.files[0];
            console.log(event.target.files[0]);
        },
        async uploadFile(event) {
            event.preventDefault();
            console.log(this.email);
            if (this.email === null) {
                console.error('uploadFile: this.email is null');
                return;
            }
 
            try {
                if (this.file === null) {
                    throw new Error('uploadFile: this.file is null');
                }

                if(!this.file.name.toLowerCase().endsWith('.pdf')){
                    toast.warning('Only pdf are allowed');
                    throw new Error("Only pdf files are allowed");
                }
 
                const formData = new FormData();
                formData.append('file', this.file);
 
                const response = await axios.post("http://localhost:8085/upload/"+this.email, formData);
 
                if (response.status === 200) {
                    toast.success('Resume uploaded succesfully !!')
                    this.available = true;
                    this.summary = response.data;
                    console.log(this.summary);
                    localStorage.clear();
                    this.email=null
                } else {
                    throw new Error(`uploadFile: Received status code ${response.status}`);
                }
            } catch (error) {
                console.error('uploadFile: Error:', error);
                this.error = error.message;
                this.message = '';
            }
        }
    },
    created(){
        console.log(this.email);
    },
 
}
</script>
 
<style>
html,
body {
    min-height: 100%;
    padding: 0;
    margin: 0;
    font-family: Roboto, Arial, sans-serif;
    font-size: 14px;
    color: #666;
}
 
h1 {
    margin: 0 0 20px;
    font-weight: 400;
    color: #1c87c9;
}
 
p {
    margin: 0 0 5px;
}
 
.main-block {
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    min-height: 100vh;
    background: #1c87c9;
}
 
form {
    padding: 25px;
    margin: 25px;
    box-shadow: 0 2px 5px #f5f5f5;
    background: #f5f5f5;
}
 
.fas {
    margin: 25px 10px 0;
    font-size: 72px;
    color: #fff;
}
 
.fa-envelope {
    transform: rotate(-20deg);
}
 
.fa-at,
.fa-mail-bulk {
    transform: rotate(10deg);
}
 
input,
textarea {
    width: calc(100% - 18px);
    padding: 8px;
    margin-bottom: 20px;
    border: 1px solid #1c87c9;
    outline: none;
}
 
input::placeholder {
    color: #666;
}
 
button {
    width: 100%;
    padding: 10px;
    border: none;
    background: #1c87c9;
    font-size: 16px;
    font-weight: 400;
    color: #fff;
}
 
button:hover {
    background: #2371a0;
}
 
@media (min-width: 568px) {
    .main-block {
        flex-direction: row;
    }
 
    .left-part,
    form {
        width: 50%;
    }
 
    .fa-envelope {
        margin-top: 0;
        margin-left: 20%;
    }
 
    .fa-at {
        margin-top: -10%;
        margin-left: 65%;
    }
 
    .fa-mail-bulk {
        margin-top: 2%;
        margin-left: 28%;
    }
}
</style>