<template>
  <div>
    <div v-for="(message, index) in chatMessages" :key="index">
      {{ message }}
    </div>
    <input
      v-model="inputMessage"
      @keyup.enter="sendMessage"
      placeholder="Type your message..."
    />
    <button @click="sendMessage">Send</button>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "ChatGptComponent", // Updated component name
  data() {
    return {
      inputMessage: "",
      chatMessages: [],
      key: "sk-proj-BOyyRCuyHX6BS8xKvQTiT3BlbkFJ5nXap9LrIndOaKCAEmkR",
    };
  },

  methods: {
    async sendMessage() {
      if (this.inputMessage.trim() === "") return "";
      try {
        const response = await axios.post(
          "https://api.openai.com/v1/engines/davinci-codex/completions",
          {
            prompt: this.inputMessage,
            max_tokens: 150,
          },
          {
            headers: {
              Authorization: `Bearer sk-proj-BOyyRCuyHX6BS8xKvQTiT3BlbkFJ5nXap9LrIndOaKCAEmkR`,
              "Content-Type": "application/json",
            },
          }
        );

        this.chatMessages.push(this.inputMessage);
        this.chatMessages.push(response.data.choices[0].text.trim());
        console.log(this.chatMessages);

        this.inputMessage = "";
      } catch (error) {
        console.error("Error sending message:", error);
      }
    },
  },
};
</script>
