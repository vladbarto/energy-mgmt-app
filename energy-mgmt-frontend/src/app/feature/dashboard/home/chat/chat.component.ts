import {Component, DestroyRef, OnInit} from '@angular/core';
import {UserModel} from "../../../../shared/models/user.model";
import {Router} from "@angular/router";
import {WebSocketService} from "../../../../core/service/web-socket/web-socket.service";
import {environment} from "../../../../../environments/environment.development";
import {BrowserStorageService} from "../../../../core/service/browser-storage-mgmt/browser-storage.service";
import {ChatModel} from "../../../../shared/models/chat.model";
import {UserService} from "../../../../core/service/user/user.service";
import {takeUntilDestroyed} from "@angular/core/rxjs-interop";

@Component({
  selector: 'app-chat',
  templateUrl: './chat.component.html',
  styleUrl: './chat.component.css'
})
export class ChatComponent implements OnInit{

  protected loggedUser: UserModel = {admin: false, email: "", id: "", password: "", username: ""};
  protected message: string = "";
  protected messages: ChatModel[] = []; // Example messages array
  protected desiredUsername: string = "";
  protected user: UserModel = {admin: false, email: "", id: "", password: "", username: ""};
  protected transmittedIsTyping: boolean = false; // Tracks if the user is currently typing
  protected receivedIsTyping: boolean = false; // Tracks if the user is receiving a typing status from the other person

  constructor(
    private router: Router,
    private destroyRef: DestroyRef,
    private webSocketService: WebSocketService,
    private browserStorage: BrowserStorageService,
    private userService: UserService
  ) {
  }

  ngOnInit() {
    this.loggedUser = this.browserStorage.getUser();
    let currentUserId = this.loggedUser.id;

    // Connect to the WebSocket
    this.webSocketService.connectSocket(`${environment.CHAT_URL}`);

    // Listen for the WebSocket connection being established
    this.webSocketService.socketStatus$.subscribe((status) => {
      if (status === true) {
        // Send the "RECEIVED" notification only after the connection is open
        // when user connected to websocket, means that he has internet access, meaning that he received the messages (not implicitly seen them)
        let notifyReceived: ChatModel = {
          type: 'RECEIVED',
          transmitter: this.loggedUser.username!,
          receiver: '',
          text: ''
        };
        console.log("RECEIVED update sent!");
        this.webSocketService.sendChatMessage(notifyReceived);
      }
    });

    this.renderIsTyping();
    this.renderMessages();

    this.webSocketService.message$.subscribe((msg: ChatModel) => {
      if(msg.type === 'RECEIVE_MESSAGE' && this.chatIsOpen(msg)) {
        console.error('SEEN_SIGNAL sent')
        // Add the message to the array. If the sender is the current user, it's 'me', otherwise 'them'
        this.addMessage(msg);  // Adds the message to the UI

        // chat is open => I can send back a seen signal for transmitter
        let sendSeenSignal: ChatModel = {
          type: 'SEEN_SIGNAL',
          transmitter: msg.transmitter,
          receiver: msg.receiver,
          text: '',
          id: msg.id
        }
        this.webSocketService.sendChatMessage(sendSeenSignal);

      }
    })
  }

  logOut(): void {
    this.browserStorage.clearCookies();
    sessionStorage.removeItem('loggedUser');
    this.router.navigateByUrl('/auth/login');
  }

  protected sendMessage(): void {
    //this.webSocketService.sendChatMessage()
    if (this.message.trim()) { // Ensure the message isn't empty or just spaces
      console.log('Message sent:', this.message);

      let mesaj: ChatModel = {
        type: 'Some irrelevant type',
        transmitter: this.loggedUser.username!,
        receiver: this.user.username!,
        text: this.message,
        status: 'SENT'
      };

      if(mesaj.receiver != 'announcements') {
        this.addMessage(mesaj);
      }

      let chatMessage: ChatModel = {
        type: 'SEND_MESSAGE',
        transmitter: this.loggedUser.username!,
        receiver: this.user.username!,
        text: this.message,
      };

      this.webSocketService.sendChatMessage(chatMessage);

      // Clear the input box
      this.message = '';

      // Clear user isTyping session
      this.transmittedIsTyping = false;
      let stopTypingMessage: ChatModel = {
        type: 'STOP_TYPING',
        transmitter: this.loggedUser.username!,
        receiver: this.user.username!,
        text: '', // No actual text needed for stop typing notifications
      };

      this.webSocketService.sendChatMessage(stopTypingMessage);
    }
  }

  protected notifyTyping(): void {
    if (this.message.trim()) {
      if (!this.transmittedIsTyping) {
        this.transmittedIsTyping = true; // Only send the TYPING event once per typing session
        let typingMessage: ChatModel = {
          type: 'TYPING',
          transmitter: this.loggedUser.username!,
          receiver: this.user.username!,
          text: '', // No actual text needed for typing notifications
        };

        this.webSocketService.sendChatMessage(typingMessage);
      }
    } else {
      if (this.transmittedIsTyping) {
        this.transmittedIsTyping = false; // Reset typing state
        let stopTypingMessage: ChatModel = {
          type: 'STOP_TYPING',
          transmitter: this.loggedUser.username!,
          receiver: this.user.username!,
          text: '', // No actual text needed for stop typing notifications
        };

        this.webSocketService.sendChatMessage(stopTypingMessage);
      }
    }
  }

  addMessage(msg: ChatModel) {
    const chatMessage: ChatModel = {
      type: 'SEND_MESSAGE', // You might want to adjust the type based on the context
      transmitter: msg.transmitter,
      receiver: msg.receiver,
      text: msg.text,
      // timestamp: new Date().toISOString(), // Assuming you'd want to add a timestamp
      status: msg.status
    };

    console.log('Adding message:', chatMessage);
    this.messages.push(chatMessage);

    this.sortMessages();
  }

  private sortMessages(): void {
    this.messages.sort((a, b) => {
      const dateA = new Date(a.timestamp!).getTime();
      const dateB = new Date(b.timestamp!).getTime();
      return dateA - dateB; // Ascending order
    });
  }

  protected getUser() {
    this.userService.getByUsername(this.desiredUsername)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: foundUser => {
          this.user = foundUser;
          this.fetchMessages();
          console.log(foundUser);
        },
        error: err => console.error("Error when getting user with username [", this.desiredUsername, "]:", err)
      });
  }

  protected getAllUsers() {
    this.user = {admin: false, email: "", id: "", password: "", username: ""};
    this.userService.getAll();
  }

  protected renderIsTyping() {
    this.webSocketService.message$.subscribe({
      next: (msg: ChatModel) => {
        if (msg.type === 'TYPING' && msg.transmitter === this.user.username) {
          this.receivedIsTyping = true; // Set to true when the TYPING event is received
        } else if (msg.type === 'STOP_TYPING' && msg.transmitter === this.user.username) {
          this.receivedIsTyping = false; // Set to false when STOP_TYPING event is received
        }
      },
      error: (err) => console.error("Error receiving WebSocket message:", err),
    });
  }

  protected renderMessages() {
    this.webSocketService.message$.subscribe({
      next: (msg: ChatModel) => {
        console.log('Received WebSocket message:', msg);

        // Check the message type to decide how to handle it
        if (msg.type === 'SEND_MESSAGE' || msg.type === 'FETCH_MESSAGES') {
          // Add the message to the array. If the sender is the current user, it's 'me', otherwise 'them'
          this.addMessage(msg);  // Adds the message to the UI
        } else if (msg.type === 'SEEN_SIGNAL') {
          console.log('Processing SEEN_SIGNAL:', msg);

          // Update the status of the message with the matching ID
          this.messages = this.messages.map((message) => {
            if (message.id === msg.id) {
              return {
                ...message,
                status: 'SEEN', // Update the status
              };
            }
            return message; // Return unchanged messages
          });
        }
      },
      error: (err) => console.error("Error receiving WebSocket message:", err),
    });
  }


  protected fetchMessages() {
    let FetchMessagesRequest: ChatModel = {
      type: 'FETCH_MESSAGES',
      transmitter: this.loggedUser.username!,
      receiver: this.user.username!,
      text: '', // No actual text needed for stop typing notifications
    };

    this.messages = [];
    this.webSocketService.sendChatMessage(FetchMessagesRequest);
  }

  protected chatIsOpen(msg: ChatModel): boolean {
    // Check if the logged user is either the transmitter or the receiver
    const isUserInvolved = this.loggedUser.username === msg.transmitter || this.loggedUser.username === msg.receiver;

    // Check if the current chat (focused) is with the other party in the message
    const isChatFocused = this.user.username === msg.transmitter || this.user.username === msg.receiver;

    return (isUserInvolved && isChatFocused) || this.user.username === 'announcements';
  }

}
