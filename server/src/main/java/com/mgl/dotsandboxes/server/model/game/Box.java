package com.mgl.dotsandboxes.server.model.game;

import com.google.common.base.Preconditions;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.Min;

import static lombok.AccessLevel.PACKAGE;

@Data
@RequiredArgsConstructor(access = PACKAGE)
// TODO: consider refactoring common stuff with Dot
public class Box {

    @Min(0)
    private final int x;

    @Min(0)
    private final int y;

    public static Box of(int x, int y) {
        Preconditions.checkArgument(x >= 0, "X (%s) must be greater than zero");
        Preconditions.checkArgument(y >= 0, "Y (%s) must be greater than zero");
        return new Box(x, y);
    }

}
