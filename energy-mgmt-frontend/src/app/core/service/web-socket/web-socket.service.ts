import { Injectable } from '@angular/core';
import { environment } from '../../../../environments/environment.development';
import {BehaviorSubject, Subject} from "rxjs";
import {ChatModel} from "../../../shared/models/chat.model";

@Injectable({
  providedIn: 'root'
})
export class WebSocketService {
  private webSocket: WebSocket | null = null;

  private messageSubject = new Subject<any>(); // Create a Subject to broadcast incoming messages
  public message$ = this.messageSubject.asObservable(); // Expose it as an Observable for subscription

  // BehaviorSubject for socket status
  private socketStatusSubject = new BehaviorSubject<boolean>(false); // Default: disconnected
  public socketStatus$ = this.socketStatusSubject.asObservable();

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
      console.warn("Socket connected, message sent");
      this.socketStatusSubject.next(true); // Emit "connected" status
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
      this.socketStatusSubject.next(false); // Emit "disconnected" or "error" status
    });
  }

  // Disconnect the socket
  disconnectSocket(): void {
    if (this.webSocket) {
      this.webSocket.onclose = (event) => {
        console.log("The connection has been closed successfully.");
        this.socketStatusSubject.next(false); // Emit "disconnected" status
      };
      this.webSocket.close();
      this.webSocket = null;
    }
  }

  // Send a message to the WebSocket server
  // designed to interact with Monitoring Microservice
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

  sendChatMessage(chatMessage: ChatModel) {
    if(this.webSocket && this.webSocket.readyState === WebSocket.OPEN) {
      console.warn(chatMessage);
      this.webSocket.send(JSON.stringify(chatMessage));
    } else {
      console.error("WebSocket is not open.");
    }
  }
}
