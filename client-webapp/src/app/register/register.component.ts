import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, FormControl, Validators } from '@angular/forms';
import { ApiClientService } from '../shared/api-client/api-client.service';
import { Router } from '@angular/router';
import { GlobalMessagesService } from '../shared/global-messages-service/global-messages.service';

@Component({
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent implements OnInit {

  registrationForm: FormGroup;

  constructor(private formBuilder: FormBuilder,
              private router: Router,
              private messages: GlobalMessagesService,
              private apiClient: ApiClientService) {}

  ngOnInit() {
    this.registrationForm = this.formBuilder.group({
      name: new FormControl('', Validators.compose([
        Validators.required, Validators.minLength(5)
      ])),
      email: new FormControl('', Validators.compose([
        Validators.required, Validators.minLength(3), Validators.pattern('.+@.+\..+')
      ]))
    });
  }

  submit(value: any, valid: boolean, data: any) {
    if (!valid) return;
    this.register(value.name, value.email);
  }

  private register(name: string, email: string) {
    const profile = {name, email};
    this.apiClient
      .register(profile)
      .subscribe(
        () => {
          this.messages.display({severity: 'success', summary: 'Registration OK'});
        },
        (error) => {
          this.messages.display({severity: 'error', summary: 'Error Registering',
                                 detail: error.toString()});
        },
        () => {
          this.router.navigateByUrl('/start');
        }
      );
  }}
