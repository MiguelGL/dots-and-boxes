package com.mgl.dotsandboxes.server.model.game;

import com.google.common.base.Preconditions;
import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.Min;

@Data
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
// TODO: consider refactoring common stuff with Box
public class Dot {

    @Min(0)
    private final int x;

    @Min(0)
    private final int y;

    public static Dot of(int x, int y) {
        Preconditions.checkArgument(x >= 0, "X (%s) must be greater than zero");
        Preconditions.checkArgument(y >= 0, "Y (%s) must be greater than zero");
        return new Dot(x, y);
    }

}
