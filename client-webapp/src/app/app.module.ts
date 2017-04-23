import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';
import { AppRoutingModule } from './app-routing.module';

import { AppComponent } from './app.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { RegisterComponent } from './register/register.component';
import { StartComponent } from './start/start.component';
import { TopTenComponent } from './top-ten/top-ten.component';
import { GameComponent } from './games/game/game.component';
import { GamesComponent } from './games/games.component';
import { ApiClientService } from './shared/api-client/api-client.service';
import { GlobalMessagesModule } from './shared/global-messages-service/global-messages.module';
import { GrowlModule } from 'primeng/components/growl/growl';
import { MessagesModule } from 'primeng/components/messages/messages';
import { SharedModule } from 'primeng/components/common/shared';
import { RegisteredRouterGuard } from './register/registered-router-guard';

@NgModule({
  declarations: [
    AppComponent,
    RegisterComponent,
    StartComponent,
    TopTenComponent,
    GameComponent,
    GamesComponent
  ],
  imports: [
    // angular
    BrowserModule,
    FormsModule,
    HttpModule,
    ReactiveFormsModule,

    // ng-bootstrap
    NgbModule.forRoot(),

    // primeng
    SharedModule,
    MessagesModule,
    GrowlModule,

    // project own
    AppRoutingModule,
    GlobalMessagesModule
  ],
  providers: [
    ApiClientService,

    RegisteredRouterGuard
  ],
  bootstrap: [
    AppComponent
  ]
})
export class AppModule { }
