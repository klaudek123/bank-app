import { Component } from '@angular/core';

@Component({
  selector: 'app-news',
  templateUrl: './news.component.html',
  styleUrls: ['./news.component.css']
})
export class NewsComponent {
  messages = [
    { date: new Date(), subject: 'Zmiana regulaminu', sender: 'Bank', status: 'UNREAD', content: 'Informujemy o zmianie regulaminu...' },
    { date: new Date(), subject: 'Oferta kredytowa', sender: 'Bank', status: 'READ', content: 'Zapraszamy do skorzystania z naszej nowej oferty kredytowej...' },
  ];

  selectedMessage: any = null;
  filter: string = 'ALL'; 

  selectMessage(message: any) {
    this.selectedMessage = message;
    message.status = 'READ';
  }

  filteredMessages() {
    if (this.filter === 'UNREAD') {
      return this.messages.filter(message => message.status === 'UNREAD');
    } else if (this.filter === 'READ') {
      return this.messages.filter(message => message.status === 'READ');
    }
    return this.messages;
  }
}
