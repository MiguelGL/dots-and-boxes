export interface Connection {

  x0: number;
  y0: number;
  x1: number;
  y1: number;

}

export interface Box {

  x: number;
  y: number;

}

export interface Board {

  dimension: number;

  hzConnections: boolean[][];
  vtConnections: boolean[][];

}

export interface TurnResult {

  playerKind: string;

  moveId: number;

  resultingBoard: Board;

  boxes: Box[];

}

export interface GameSnapshot {

  state: string;

  score: number;

  winner?: string;

  turnResults: TurnResult[];

}

export interface Game {

  gameId: string;
  starterPlayer: string;

  dimension: number;

  currentSnapshot: GameSnapshot;

}

export interface ScoringSummary {

  gameId: string;
  score: number;

}
