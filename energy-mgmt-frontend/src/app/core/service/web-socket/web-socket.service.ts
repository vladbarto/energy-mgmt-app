import { Injectable } from '@angular/core';
import { environment } from '../../../../environments/environment.development';
import {Subject} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class WebSocketService {
  private webSocket: WebSocket | null = null;
  private messageSubject = new Subject<any>(); // Create a Subject to broadcast incoming messages
  public message$ = this.messageSubject.asObservable(); // Expose it as an Observable for subscription

  constructor() {}

  // Connect socket with a specific URL
  connectSocket(socketUrl: string): void {
    // Ensure the WebSocket is not already connected
    if (this.webSocket) {
      console.warn("WebSocket is already open.");
      return;
    }

    this.webSocket = new WebSocket(socketUrl);

    // Connection opened
    this.webSocket.addEventListener("open", (event) => {
      this.sendMessage("OPEN_WS", "Hello WebSocket Server1!", "", "2024-11-22", 0.0);
      console.warn("Socket connected, message sent");
    });

    // Listen for messages and broadcast them to subscribers
    this.webSocket.addEventListener('message', (event) => {
      try {
        const data = JSON.parse(event.data);
        console.log('Message from server:', data);
        this.messageSubject.next(data); // Push the received data to the subscribers
      } catch (error) {
        console.error('Error parsing WebSocket message:', error);
      }
    });

    this.webSocket.addEventListener("error", (event) => {
      console.error("WebSocket error:", event);
    });
  }

  // Disconnect the socket
  disconnectSocket(): void {
    if (this.webSocket) {
      this.webSocket.onclose = (event) => {
        console.log("The connection has been closed successfully.");
      };
      this.webSocket.close();
      this.webSocket = null;
    }
  }

  // Send a message to the WebSocket server
  sendMessage(type: string, messageToSend: string, deviceId: string, date: string, mhec: number): void {
    if (this.webSocket && this.webSocket.readyState === WebSocket.OPEN) {
      let message = JSON.stringify({
        type: type,
        message: messageToSend,
        deviceId: deviceId,
        date: date,
        mhec: mhec
      });

      this.webSocket.send(message);
    } else {
      console.error("WebSocket is not open.");
    }
  }
}
