package com.mgl.dotsandboxes.server.service.game;

import com.google.common.collect.ImmutableMap;
import com.mgl.dotsandboxes.server.model.game.Connection;
import com.mgl.dotsandboxes.server.model.game.Dot;
import org.junit.Assert;
import org.junit.Test;

public class GameServiceTest {

    @Test
    public void asConnection() throws Exception {
        ImmutableMap<GameService.HzOrVzLine, Connection> testData =
            ImmutableMap.<GameService.HzOrVzLine, Connection>builder()
                    .put(new GameService.HzOrVzLine(0, 0, true),
                            Connection.of(Dot.of(0, 0), Dot.of(1, 0)))
                    .put(new GameService.HzOrVzLine(0, 0, false),
                            Connection.of(Dot.of(0, 0), Dot.of(0, 1)))

                    .put(new GameService.HzOrVzLine(2, 3, false),
                            Connection.of(Dot.of(2, 3), Dot.of(2, 4)))
                    .put(new GameService.HzOrVzLine(3, 3, true),
                            Connection.of(Dot.of(3, 3), Dot.of(4, 3)))

                    .put(new GameService.HzOrVzLine(1, 0, true),
                            Connection.of(Dot.of(1, 0), Dot.of(2, 0)))
                    .put(new GameService.HzOrVzLine(1, 0, false),
                            Connection.of(Dot.of(1, 0), Dot.of(1, 1)))
                .build();

        testData.forEach((line, connection) -> {
            System.out.println("---");

            System.out.printf("Expected: [%d, %d, %s] => (%d, %d) <-> (%d, %d)%n",
                    line.getX(), line.getY(), line.hz ? "hz" : "vz",
                    connection.getX0(), connection.getY0(),
                    connection.getX1(), connection.getY1());

            Connection resultConnection = GameService.asConnection(line);

            System.out.printf("Result:   [%d, %d, %s] => (%d, %d) <-> (%d, %d)%n",
                    line.getX(), line.getY(), line.hz ? "hz" : "vz",
                    resultConnection.getX0(), resultConnection.getY0(),
                    resultConnection.getX1(), resultConnection.getY1());

            Assert.assertEquals(connection, resultConnection);
        });
    }

}
