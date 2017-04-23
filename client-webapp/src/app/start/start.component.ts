import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, FormControl, Validators } from '@angular/forms';
import { ApiClientService } from '../shared/api-client/api-client.service';
import { GlobalMessagesService } from '../shared/global-messages-service/global-messages.service';
import { Router } from '@angular/router';
import { CustomValidators } from 'ng2-validation';

@Component({
  selector: 'app-start',
  templateUrl: './start.component.html',
  styleUrls: ['./start.component.scss']
})
export class StartComponent implements OnInit {

  newGameForm: FormGroup;

  constructor(private formBuilder: FormBuilder,
              private router: Router,
              private messages: GlobalMessagesService,
              private apiClient: ApiClientService) {}

  ngOnInit() {
    this.newGameForm = this.formBuilder.group({
      dimension: new FormControl('3', Validators.compose([
        Validators.required, CustomValidators.number, CustomValidators.range([2, 5])
      ])),
      computerStarts: new FormControl(false)
    });
  }

  startNewGame(value: any, valid: boolean, data: any) {
    this.apiClient.startNewGame(value.dimension, value.computerStarts ? 'COMPUTER' : 'HUMAN')
      .subscribe(
        (game) => {
          this.router.navigateByUrl(`/games/${game.gameId}`);
        },
        (error) => {
          this.messages.display({severity: 'error', summary: 'Error Starting Game',
            detail: error.toString()});
        },
        () => {
          this.messages.display({severity: 'success', summary: 'Game Started'});
        }
      );
  }

}
