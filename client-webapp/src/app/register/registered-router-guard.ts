import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { GlobalMessagesService } from '../shared/global-messages-service/global-messages.service';
import { ApiClientService } from '../shared/api-client/api-client.service';

@Injectable()
export class RegisteredRouterGuard implements CanActivate {

  constructor(private router: Router,
              private apiClient: ApiClientService,
              private messagesService: GlobalMessagesService) {}

  canActivate(route: ActivatedRouteSnapshot,
              state: RouterStateSnapshot): Observable<boolean> | Promise<boolean> | boolean {
    if (this.apiClient.isRegistered) {
      return true;
    } else {
      this.messagesService.display({
        severity: 'info', summary: 'Not Registered',
        detail: 'You are not registered, redirecting to registration page.'
      })
      this.router.navigateByUrl('/register');
      return false;
    }
  }

}
