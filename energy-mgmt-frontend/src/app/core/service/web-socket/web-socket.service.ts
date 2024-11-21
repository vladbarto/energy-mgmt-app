import {Injectable} from '@angular/core';
import {environment} from "../../../../environments/environment.development";

@Injectable({
  providedIn: 'root'
})
export class WebSocketService {

  constructor(
    private webSocket: WebSocket
  ) {
  }

  connectSocket() {
    // Connection opened
    this.webSocket.addEventListener("open", (event) => {
      this.webSocket.send("Hello Server1!");
      console.warn("socket connected, message sent");
    });

    this.webSocket.addEventListener("message", (event) => {
      console.log("Message from server:", event.data);
    });

    this.webSocket.addEventListener("error", (event) => {
      console.error("WebSocket error:", event);
    });
  }

  disconnectSocket() {
    this.webSocket.onclose = (event) => {
      console.log("The connection has been closed successfully.");
    }
  }

}
