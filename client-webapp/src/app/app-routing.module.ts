import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { RegisterComponent } from './register/register.component';
import { StartComponent } from './start/start.component';
import { GameComponent } from './games/game/game.component';
import { TopTenComponent } from './top-ten/top-ten.component';
import { GamesComponent } from './games/games.component';
import { RegisteredRouterGuard } from './register/registered-router-guard';

const routes: Routes = [
  { path: '', pathMatch: 'full', redirectTo: '/start' },
  { path: 'register', pathMatch: 'full', component: RegisterComponent },
  { path: 'start', pathMatch: 'full', component: StartComponent,
    canActivate: [ RegisteredRouterGuard ] },
  { path: 'games', canActivate: [ RegisteredRouterGuard ], children: [
    { path: '', pathMatch: 'full', component: GamesComponent },
    { path: ':gameId', component: GameComponent } ]},
  { path: 'top-ten', pathMatch: 'full', component: TopTenComponent,
    canActivate: [ RegisteredRouterGuard ] },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
  providers: []
})
export class AppRoutingModule { }
