import { Component, OnInit } from '@angular/core';
import { ApiClientService } from '../shared/api-client/api-client.service';
import { Game, ScoringSummary } from '../shared/api-client/Game';
import { GlobalMessagesService } from '../shared/global-messages-service/global-messages.service';

@Component({
  selector: 'app-top-ten',
  templateUrl: './top-ten.component.html',
  styleUrls: ['./top-ten.component.scss']
})
export class TopTenComponent implements OnInit {

  scorings: ScoringSummary[] = [];

  constructor(private apiClient: ApiClientService,
              private messages: GlobalMessagesService) {}

  ngOnInit() {
    this.apiClient.loadTopTen()
      .subscribe(
        (scorings) =>
          this.scorings = scorings,
        (error) =>
          this.messages.display({severity: 'error', summary: 'Error Loading Top Ten',
            detail: error.toString()})
  );
  }

}
