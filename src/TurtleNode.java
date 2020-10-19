import java.awt.*;

public class TurtleNode {
    private TurtleNode parent;
    private Point position;
    private int g, h, f;

    public TurtleNode(TurtleNode parent, Point position){
        this.parent = parent;
        this.position = position;
        this.g = 0;
        this.h = 0;
        this.g = 0;
    }

    public TurtleNode getParent() {
        return parent;
    }

    public void setParent(TurtleNode parent) {
        this.parent = parent;
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public int getG() {
        return g;
    }

    public void setG(int g) {
        this.g = g;
    }

    public int getH() {
        return h;
    }

    public void setH(int h) {
        this.h = h;
    }

    public int getF() {
        return f;
    }

    public void setF(int f) {
        this.f = f;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof TurtleNode)) {
            return false;
        }

        TurtleNode turtleNode = (TurtleNode) obj;

        return position.equals(turtleNode.position);
    }
}
