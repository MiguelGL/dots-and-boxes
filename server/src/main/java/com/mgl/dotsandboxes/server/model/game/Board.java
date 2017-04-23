package com.mgl.dotsandboxes.server.model.game;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableSet;
import com.google.common.primitives.Booleans;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.Set;

import static com.google.common.base.Preconditions.checkArgument;

@Data
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public class Board {

    public static final int MIN_DIM = 2;

    @Min(MIN_DIM)
    private final int dimension;

    @NotNull
    // Do not expose our representation, only serialize.
    @Getter(AccessLevel.NONE)
    @JsonProperty
    private final boolean[][] hzConnections;

    @NotNull
    // Do not expose our representation, only serialize.
    @Getter(AccessLevel.NONE)
    @JsonProperty
    private final boolean[][] vtConnections;

    private Board(int dimension) {
        checkArgument(dimension >= MIN_DIM,
                "Dimension (%s) must be greater than %s", dimension, MIN_DIM);
        this.dimension = dimension;
        this.hzConnections = new boolean[dimension + 1][dimension];
        this.vtConnections = new boolean[dimension + 1][dimension];
    }

    public static Board ofDimension(int dimension) {
        return new Board(dimension);
    }

    public boolean isFurtherPlayable() {
        for (int i = 0; i < hzConnections.length; i++) {
            if (Booleans.contains(hzConnections[i], false)) {
                return true;
            }
        }
        for (int j = 0; j < vtConnections.length; j++) {
            if (Booleans.contains(vtConnections[j], false)) {
                return true;
            }
        }
        return false;
    }

    public boolean isValidCoordinate(int x, int y) {
        return (x >= 0 && x <= dimension + 1) && (y >= 0 && y <= dimension + 1);
    }

    private void ensureNotSameCoordinates(int x0, int y0, int x1, int y1) {
        checkArgument(x0 != x1 || y0 != y1,
                "Coordinates are the same: (%s, %s)", x0, y0);
    }

    private void ensureValidCoordinate(int x, int y) {
        checkArgument(isValidCoordinate(x, y),
                "Invalid coordinate for dim %s: (%s, %s)", dimension, x, y);
    }

    private void ensureDotsAreAdjacent(int x0, int y0, int x1, int y1) {
        checkArgument(areDotsAdjacent(x0, y0, x1, y1));
    }

    public boolean areDotsAdjacent(int x0, int y0, int x1, int y1) {
        ensureValidCoordinate(x0, y0);
        ensureValidCoordinate(x1, y1);
        ensureNotSameCoordinates(x0, y0, x1, y1);

        if (x0 == x1) {
            return Math.abs(y0 - y1) == 1;
        } else if (y0 == y1) {
            return Math.abs(x0 - x1) == 1;
        } else {
            return false;
        }
    }

    public String toAsciiArt() {
        StringBuilder sb = new StringBuilder();
        for (int row = 0; row < 2 * dimension + 1; row++) {
            for (int col = 0; col < 2 * dimension + 1; col++) {
                String content;
                if (row % 2 == 0 && col % 2 == 0) {
                    content = "*";
                } else if (row % 2 == 1 && col % 2 == 1) {
                    content = " ";
                } else if (row % 2 == 0) {
                    content = hzConnections[row / 2][col / 2] ? "-" : " ";
                } else if (col % 2 == 0) {
                    content = vtConnections[col / 2][row / 2] ? "|" : " ";
                } else {
                    throw new IllegalStateException(); // Should never get here.
                }
                sb.append(content);
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public boolean hasConnection(Connection connection) {
        if (connection.isSameX()) {
            int y = Math.min(connection.getY0(), connection.getY1());
            log.debug("has vt[{}, {}] {} connection?",
                    connection.getX0(), y, connection.asDotsString());
            return vtConnections[connection.getX0()][y];
        } else if (connection.isSameY()) {
            int x = Math.min(connection.getX0(), connection.getX1());
            log.debug("has hz[{}, {}] {} connection?",
                    connection.getY0(), x, connection.asDotsString());
            return hzConnections[connection.getY0()][x];
        } else {
            throw new IllegalStateException(); // Should never get here.
        }
    }

    public boolean isConnectionAvailable(Connection connection) {
        return !hasConnection(connection);
    }

    private static boolean[][] deepCopy(boolean[][] a) {
        boolean[][] result = new boolean[a.length][];
        for (int i = 0; i < a.length; i++) {
            result[i] = Arrays.copyOf(a[i], a[i].length);
        }
        return result;
    }

    public Board withConnection(Connection connection) {
        ensureDotsAreAdjacent(
                connection.getX0(), connection.getY0(),
                connection.getX1(), connection.getY1());
        Board board = new Board(
                dimension,
                deepCopy(hzConnections),
                deepCopy(vtConnections));
        if (connection.isSameX()) {
            int y = Math.min(connection.getY0(), connection.getY1());
            board.vtConnections[connection.getX0()][y] = true;
        } else if (connection.isSameY()) {
            int x = Math.min(connection.getX0(), connection.getX1());
            board.hzConnections[connection.getY0()][x] = true;
        } else {
            throw new IllegalStateException(); // Should never get here.
        }
        return board;
    }

    public Set<Box> calculateBoxes() {
        ImmutableSet.Builder<Box> builder = ImmutableSet.builder();
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                if (hzConnections[j][i] && hzConnections[j + 1][i]
                        && vtConnections[i][j] && vtConnections[i + 1][j]) {
                    builder.add(Box.of(i, j));
                }
            }
        }
        return builder.build();
    }

}
