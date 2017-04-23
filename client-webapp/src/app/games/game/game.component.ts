import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Params, RouterLink, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { ApiClientService } from '../../shared/api-client/api-client.service';
import { Game, Connection, GameSnapshot } from '../../shared/api-client/Game';
import { GlobalMessagesService } from '../../shared/global-messages-service/global-messages.service';

export enum CellKind {
  LINE, DOT, BOX
}

export class Cell {

  clicked = false;

  constructor(public readonly cellKind: CellKind,
              public readonly isHz: boolean = false) {}

  get isLine() { return this.cellKind == CellKind.LINE; }
  get isDot() { return this.cellKind == CellKind.DOT; }
  get isBox() { return this.cellKind == CellKind.BOX; }

}

@Component({
  selector: 'app-game',
  templateUrl: './game.component.html',
  styleUrls:   ['./game.component.scss']
})
export class GameComponent implements OnInit {

  gameId: string;
  game: Game;

  cells: Cell[][] = [];

  constructor(private route: ActivatedRoute,
              private router: Router,
              private messages: GlobalMessagesService,
              private apiClient: ApiClientService) {}

  ngOnInit() {
    this.route.params
      .map(params => params['gameId'])
      .map(
        (gameId) => {
          this.gameId = gameId;
          return gameId;
        }
      )
      .flatMap(
        (gameId) =>
          this.apiClient.loadGame(gameId)
      )
      .subscribe(
        (game) =>
          this.onGameLoaded(game),
        (error) =>
          this.messages.display({severity: 'error', summary: 'Error Loading Game',
            detail: error.toString()}),
        () =>
          this.messages.display({severity: 'success', summary: 'Game Loaded'})
    );
  }

  private onGameLoaded(game: Game) {
    this.game = game;
    const tableDim = this.tableDimension;
    this.cells = new Array(tableDim);
    for (let row = 0; row < tableDim; row++) {
      this.cells[row] = new Array(tableDim);
      const cellsRow = this.cells[row];
      for (let col = 0; col < tableDim; col++) {
        const isHz = (row % 2 == 0);
        const isLine = isHz ? (col % 2 == 1) : (col % 2 == 0);
        cellsRow[col] = isLine
          ? new Cell(CellKind.LINE, isHz)
          : new Cell((row % 2 == 1) && (row % 2 == 1) ? CellKind.BOX : CellKind.DOT);
      }
    }
    if (this.lastTurn) {
      this.updateBoard();
    }
  }

  get tableDimension() {
    return this.dimension * 2 + 1;
  }

  get dimension() { return this.game.dimension; }

  get snapshot() { return this.game.currentSnapshot; }

  get lastTurn() {
    const turns = this.snapshot.turnResults;
    return turns[turns.length - 1];
  }

  get board() {
    return this.lastTurn.resultingBoard;
  }

  get state() {
    return (this.game) ? this.snapshot.state : null;
  }

  get winner() {
    return (this.game) ? this.snapshot.winner : null;
  }

  get score() {
    return (this.game) ? this.snapshot.score : null;
  }

  private toConnection(row: number, col: number): Connection {
    const x = Math.floor(col / 2);
    const y = Math.floor(row / 2);
    if (row % 2 == 0) {
      return {x0: x, y0: y, x1: x + 1, y1: y};
    } else {
      return {x0: x, y0: y, x1: x, y1: y + 1};
    }
  }

  lineClicked(row: number, col: number) {
    // console.log(row, col);
    // this.cells[row][col].clicked = !this.cells[row][col].clicked;

    this.apiClient.turn(this.gameId, this.toConnection(row, col))
      .subscribe(
        (gameSnapshot) =>
          this.onNewGameSnapshot(gameSnapshot),
        (error) =>
          this.messages.display({severity: 'error', summary: 'Error Playing',
            detail: error.toString()}),
        () =>
          this.messages.display({severity: 'success', summary: 'Played!'})
      );
  }

  private updateBoard() {
    const board = this.board;
    const tableDim = this.tableDimension;
    for (let row = 0; row < tableDim; row++) {
      for (let col = 0; col < tableDim; col++) {
        const cell = this.cells[row][col];
        if (cell.cellKind == CellKind.LINE) {
          if (cell.isHz) {
            const hz1 = Math.floor(row / 2);
            const hz2 = Math.floor(col / 2);
            cell.clicked = board.hzConnections[hz1][hz2];
          } else {
            const vt1 = Math.floor(col / 2);
            const vt2 = Math.floor(row / 2);
            cell.clicked = board.vtConnections[vt1][vt2];
          }
        }
        // console.log(row, col, '->', this.cells[row][col].clicked);
      }
    }
  }

  private onNewGameSnapshot(gameSnapshot: GameSnapshot) {
    this.game.currentSnapshot = gameSnapshot;
    this.updateBoard();
  }

  playAgain() {
    this.apiClient.startNewGame(this.game.dimension, 'HUMAN', this.gameId)
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
