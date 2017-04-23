package com.mgl.dotsandboxes.server.model.game;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class Connection {

    @Valid @NotNull
    @Getter(AccessLevel.NONE)
    private final Dot coord0;

    @Valid @NotNull
    @Getter(AccessLevel.NONE)
    private final Dot coord1;

    @JsonCreator
    Connection(@JsonProperty("x0") int x0,
               @JsonProperty("y0") int y0,
               @JsonProperty("x1") int x1,
               @JsonProperty("y1") int y1) {
        this.coord0 = Dot.of(x0, y0);
        this.coord1 = Dot.of(x1, y1);
    }

    public static Connection of(Dot coord0, Dot coord1) {
        return new Connection(coord0, coord1);
    }

    public int getX0() {
        return coord0.getX();
    }

    public int getY0() {
        return coord0.getY();
    }

    public int getX1() {
        return coord1.getX();
    }

    public int getY1() {
        return coord1.getY();
    }

    @JsonIgnore
    public boolean isSameX() {
        return getX0() == getX1();
    }

    @JsonIgnore
    public boolean isSameY() {
        return getY1() == getY1();
    }

    public String asDotsString() {
        return String.format("(%d, %d) <-> (%d, %d)", getX0(), getY0(), getX1(), getY1());
    }

}
