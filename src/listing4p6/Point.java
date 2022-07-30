package listing4p6;

import annotation.Immutable;

@Immutable
public class Point {
    public final int x, y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }
}

/*
Point is thread-safe because it is immutable.
Immutable values can be freely shared and published, so we no longer need to copy the locations when returning them.
 */
