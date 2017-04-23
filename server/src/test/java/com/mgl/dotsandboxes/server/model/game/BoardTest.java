package com.mgl.dotsandboxes.server.model.game;

import com.google.common.collect.ImmutableSet;
import org.junit.Assert;
import org.junit.Test;

import java.util.stream.IntStream;

public class BoardTest {

    @Test
    public void testValidBoardCoordinates() {
        Board board = Board.ofDimension(4);

        IntStream.rangeClosed(0, 4).forEach(x ->
                IntStream.rangeClosed(0, 4).forEach(y ->
                        Assert.assertTrue(board.isValidCoordinate(x, y))));
    }

    @Test
    public void testInvalidBoardCoordinates() {
        Board board = Board.ofDimension(4);

        Assert.assertFalse(board.isValidCoordinate(-1, 0));
        Assert.assertFalse(board.isValidCoordinate(0, -1));
        Assert.assertFalse(board.isValidCoordinate(6, 0));
        Assert.assertFalse(board.isValidCoordinate(0, 6));
    }

    @Test
    public void testAdjacentPoints() {
        Board board = Board.ofDimension(4);

        Assert.assertTrue(board.areDotsAdjacent(0, 0, 1, 0));
        Assert.assertTrue(board.areDotsAdjacent(0, 0, 0, 1));
        Assert.assertFalse(board.areDotsAdjacent(0, 0, 2, 0));
        Assert.assertFalse(board.areDotsAdjacent(0, 0, 0, 2));

        Assert.assertTrue(board.areDotsAdjacent(4, 0, 3, 0));
        Assert.assertTrue(board.areDotsAdjacent(4, 0, 4, 1));
        Assert.assertFalse(board.areDotsAdjacent(4, 0, 2, 0));
        Assert.assertFalse(board.areDotsAdjacent(4, 0, 4, 2));

        Assert.assertTrue(board.areDotsAdjacent(0, 4, 1, 4));
        Assert.assertTrue(board.areDotsAdjacent(0, 4, 0, 3));
        Assert.assertFalse(board.areDotsAdjacent(0, 4, 2, 4));
        Assert.assertFalse(board.areDotsAdjacent(0, 4, 0, 2));

        Assert.assertTrue(board.areDotsAdjacent(4, 4, 3, 4));
        Assert.assertTrue(board.areDotsAdjacent(4, 4, 4, 3));
        Assert.assertFalse(board.areDotsAdjacent(4, 4, 2, 4));
        Assert.assertFalse(board.areDotsAdjacent(4, 4, 4, 2));
    }

    @Test
    public void testAsciArt() {
        Board board = Board.ofDimension(4);

        String asciiBoard = board.toAsciiArt();

        String expected =
                "* * * * *\n"
              + "         \n"
              + "* * * * *\n"
              + "         \n"
              + "* * * * *\n"
              + "         \n"
              + "* * * * *\n"
              + "         \n"
              + "* * * * *\n";

        Assert.assertEquals(expected, asciiBoard);
    }

    @Test
    public void testConnectionHz() {
        Board board = Board.ofDimension(4)
                .withConnection(Connection.of(Dot.of(1, 3), Dot.of(2, 3)));

        String asciiBoard = board.toAsciiArt();

        String expected =
              "* * * * *\n"
            + "         \n"
            + "* * * * *\n"
            + "         \n"
            + "* * * * *\n"
            + "         \n"
            + "* *-* * *\n"
            + "         \n"
            + "* * * * *\n";

        Assert.assertEquals(expected, asciiBoard);

        // Revers dot orders, should get same result.
        board = Board.ofDimension(4)
                .withConnection(Connection.of(Dot.of(2, 3), Dot.of(1, 3)));

        asciiBoard = board.toAsciiArt();

        Assert.assertEquals(expected, asciiBoard);
    }

    @Test
    public void testConnectionVz() {
        Board board = Board.ofDimension(4)
                .withConnection(Connection.of(Dot.of(0, 0), Dot.of(0, 1)))
                .withConnection(Connection.of(Dot.of(4, 3), Dot.of(4, 4)));

        String asciiBoard = board.toAsciiArt();

        String expected =
              "* * * * *\n"
            + "|        \n"
            + "* * * * *\n"
            + "         \n"
            + "* * * * *\n"
            + "         \n"
            + "* * * * *\n"
            + "        |\n"
            + "* * * * *\n";

        Assert.assertEquals(expected, asciiBoard);

        board = Board.ofDimension(4)
                .withConnection(Connection.of(Dot.of(0, 1), Dot.of(0, 0)))
                .withConnection(Connection.of(Dot.of(4, 4), Dot.of(4, 3)));

        asciiBoard = board.toAsciiArt();

        Assert.assertEquals(expected, asciiBoard);
    }

    @Test
    public void testBoxesCalculation() {
        Board board = Board.ofDimension(4)
                .withConnection(Connection.of(Dot.of(0, 0), Dot.of(1, 0)))
                .withConnection(Connection.of(Dot.of(0, 0), Dot.of(0, 1)))
                .withConnection(Connection.of(Dot.of(1, 0), Dot.of(1, 1)))
                .withConnection(Connection.of(Dot.of(0, 1), Dot.of(1, 1)))

                .withConnection(Connection.of(Dot.of(0, 1), Dot.of(0, 2)))
                .withConnection(Connection.of(Dot.of(1, 1), Dot.of(1, 2)))
                .withConnection(Connection.of(Dot.of(0, 2), Dot.of(1, 2)))

                .withConnection(Connection.of(Dot.of(1, 1), Dot.of(2, 1)))
                .withConnection(Connection.of(Dot.of(1, 2), Dot.of(2, 2)))
                .withConnection(Connection.of(Dot.of(2, 1), Dot.of(2, 2)))

                .withConnection(Connection.of(Dot.of(3, 3), Dot.of(4, 3)))
                .withConnection(Connection.of(Dot.of(3, 3), Dot.of(3, 4)))
                .withConnection(Connection.of(Dot.of(3, 4), Dot.of(4, 4)))
                .withConnection(Connection.of(Dot.of(4, 3), Dot.of(4, 4)));

        String asciiBoard = board.toAsciiArt();

        String expected =
              "*-* * * *\n"
            + "| |      \n"
            + "*-*-* * *\n"
            + "| | |    \n"
            + "*-*-* * *\n"
            + "         \n"
            + "* * * *-*\n"
            + "      | |\n"
            + "* * * *-*\n";

        Assert.assertEquals(expected, asciiBoard);

        ImmutableSet<Box> expectedBoxes = ImmutableSet.of(
                Box.of(0, 0), Box.of(0, 1), Box.of(1, 1), Box.of(3, 3));

        Assert.assertEquals(expectedBoxes, board.calculateBoxes());
    }
}
