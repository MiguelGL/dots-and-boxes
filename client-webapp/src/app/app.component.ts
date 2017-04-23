import { Component } from '@angular/core';
import { ApiClientService } from './shared/api-client/api-client.service';
import { GlobalMessagesService } from './shared/global-messages-service/global-messages.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {

  constructor(public apiClient: ApiClientService,
              private messagesService: GlobalMessagesService) {}

  get messages() {
    return this.messagesService.messages;
  }

}
