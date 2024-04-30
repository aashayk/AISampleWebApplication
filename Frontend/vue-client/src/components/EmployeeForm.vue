<template>
  <div class="main-block">
      <div class="left-part">
          <i class="fas fa-envelope"></i>
          <i class="fas fa-at"></i>
          <i class="fas fa-mail-bulk"></i>
      </div>
      <form @submit.prevent="submitForm">
          <h1>Fill your details</h1>
          <div class="info">
              <input type="text" id="firstName" v-model="formData.firstName" class="fname" placeholder="First Name"
                  required>

              <input type="text" id="lastName" v-model="formData.lastName" placeholder="Last Name" required>

              <input type="number" id="age" v-model.number="formData.age" placeholder="Age" required>

              <input type="number" id="yearsOfExperience" v-model.number="formData.yearsOfExperience"
                  placeholder="Years Of Experience" required>

              <input type="email" id="email" v-model="formData.email" placeholder="Email" required>

              <input type="text" id="mobile" v-model="formData.mobile" placeholder="Mobile" required>

              <input type="text" id="designation" v-model="formData.designation" placeholder="Designation" required>
          </div>
          <button type="submit">Submit</button>
      </form>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  name: 'Employee-Form',
  data() {
      return {
          formData: {
              firstName: '',
              lastName: '',
              age: null,
              yearsOfExperience: null,
              email: '',
              mobile: '',
              designation: ''
          }
      }
  },
  methods: {
      submitForm() {
          axios.post(`http://localhost:8085/emp/submit`, this.formData)
              .then(response => {
                  console.log(response.data)
                  localStorage.setItem('email',response.data.email)
                  this.$router.push('/upload')

              })
              .catch(error => {
                  console.log(error)
              })
      }
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

.button {
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