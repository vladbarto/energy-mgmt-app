export interface ChatModel {
  id?: string;
  type: string; // enum ActionType in Backend
  transmitter: string;
  receiver: string;
  text: string;
  timestamp?: string;
  status?: string;
}
