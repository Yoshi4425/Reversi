package reversi;

public class Vector {

    public final int x;
    public final int y;

    public static final Vector[] vec = {new Vector(1,0), new Vector(1,1), new Vector(0,1), new Vector(-1,1), 
                                        new Vector(-1,0), new Vector(-1,-1), new Vector(0,-1), new Vector(1,-1)};

    public Vector (int xx, int yy) {
        x = xx;
        y = yy;
    }
}