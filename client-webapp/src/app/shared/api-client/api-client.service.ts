import { Injectable } from '@angular/core';
import { Http, Headers } from '@angular/http';
import { Observable } from 'rxjs';
import { PlayerProfile } from './PlayerProfile';
import { Game, Connection, GameSnapshot, ScoringSummary } from './Game';

@Injectable()
export class ApiClientService {

  private playerProfile?: PlayerProfile;
  private playerId?: string;

  constructor(private http: Http) {}

  private ensureRegistered() {
    if (!this.isRegistered) {
      throw new Error('Not registred');
    }
  }

  get isRegistered() { return !!this.playerId; }

  get playerDescription() {
    this.ensureRegistered();    return `${this.playerProfile.name} (${this.playerProfile.email})`;
  }

  register(profile: PlayerProfile): Observable<void> {
    return this.http
      .post('/dots-and-boxes-api/rest/players', profile)
      .map(response => {
        if (response.status === 200) {
          this.playerProfile = profile;
          this.playerId = response.json().playerId;
        } else {
          throw response;
        }
      });
  }

  startNewGame(dimension: number, whoStarts = 'HUMAN', asFollowUpTo = ''): Observable<Game> {
    const body = {dimension, whoStarts};
    const headers = new Headers({'X-Player-Id': this.playerId});
    return this.http
      .post('/dots-and-boxes-api/rest/games', body, {headers})
      .map(response => {
        if (response.status === 200) {
          return response.json() as Game;
        } else {
          throw response;
        }
      });
  }

  loadGame(gameId: string): Observable<Game> {
    const headers = new Headers({'X-Player-Id': this.playerId});
    return this.http
      .get(`/dots-and-boxes-api/rest/games/${gameId}`, {headers})
      .map(response => {
        if (response.status === 200) {
          return response.json() as Game;
        } else {
          throw response;
        }
      });
  }

  turn(gameId: string, connection: Connection): Observable<GameSnapshot> {
    const headers = new Headers({'X-Player-Id': this.playerId});
    return this.http
      .post(`/dots-and-boxes-api/rest/games/${gameId}/turns`, connection, {headers})
      .map(response => {
        if (response.status === 200) {
          return response.json() as GameSnapshot;
        } else {
          throw response;
        }
      });
  }

  loadTopTen(): Observable<ScoringSummary[]> {
    return this.http
      .get(`/dots-and-boxes-api/rest/${this.playerId}/scores/top-ten`)
      .map(response => {
        if (response.status === 200) {
          return response.json() as ScoringSummary[];
        } else {
          throw response;
        }
      });
  }
}
