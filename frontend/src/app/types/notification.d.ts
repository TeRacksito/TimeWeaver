export interface NotificationEvent {
  data: Object;
  type: NotificationType;
  read: boolean;
  createdAt: string;
}
