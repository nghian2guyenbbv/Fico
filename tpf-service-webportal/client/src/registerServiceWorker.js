import { register } from "register-service-worker";

register(`${process.env.BASE_URL}service-worker.js`, {
  ready() {
    console.log('ready');
  },
  registered() {
    console.log('registered');
  },
  cached() {
    console.log("cached");
  },
  updatefound() {
    console.log("updatefound");
  },
  updated() {
    console.log("updated");
  },
  offline() {
    console.log("offline");
  },
  error(error) {
    console.error("Error during service worker registration:", error);
  }
});