import java.awt.*;
import java.awt.geom.Point2D;
import java.util.*;
import java.util.List;

public class Turtle {
    private static final int SIZE = 10;
    private static final Scanner scanner = new Scanner(System.in);


    public Turtle() {
        for (int[] row : floor) {
            Arrays.fill(row, 0);
        }

        x = 0;
        y = 0;
        isDown = false;
        direction = 1;
    }

    private final int[][] floor = new int[SIZE][SIZE];
    private List<String> commands = new ArrayList<>();
    private int x, y;
    private boolean isDown;
    private int direction;

    public static void main(String[] args) {
        Turtle turtle = new Turtle();

        int[][] targetPoints = {{0, 1, 1, 0, 0, 0, 0, 0, 0, 1},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 1, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 1, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 1, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 1, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 1, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 1}};

//        int[] start = new int[]{0,0};
//        int[] end = new int[]{1, 6};
//
//        List<int[]> path = turtle.aStar(maze, start, end);
//
//        for (int[] coordinate : path){
//            System.out.println(Arrays.toString(coordinate));;
//        }

//        List<Point> targets = turtle.targetPointsCoordinates(maze);
//
//
//        targets.remove(new Point(0, 0));
//
//
//        for (Point point : targets){
//            System.out.print("[" + (int) point.getX() + " " + (int) point.getY() + "], ");
//        }
////        System.out.println(Arrays.toString(turtle.nearestPoints(new int[]{0, 9}, targets)));

        List<List<Point>> finalPath = turtle.findPath(targetPoints);

        for (List<Point> paths : finalPath){
            for (Point point : paths){
                System.out.print("[" + (int) point.getX() + " " + (int) point.getY() + "], ");
            }
            System.out.println();
        }
    }

    public List<Point> targetPointsCoordinates(int[][] targetPoints) {
        List<Point> targetPointsCoordinates = new ArrayList<>();

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (targetPoints[i][j] == 1){
                    targetPointsCoordinates.add(new Point(i, j));
                }
            }
        }
        return targetPointsCoordinates;
    }

    public Point nearestPoints(Point point, List<Point> targetPoints) {
        double minDistance = Double.POSITIVE_INFINITY;
        Point minPoint = null;

        for (Point currentPoint : targetPoints) {
            double distance = euclideanDistance(point, currentPoint);
            if (minDistance > distance) {
                minDistance = distance;
                minPoint = currentPoint;
            }
        }

        return minPoint;
    }

    public double euclideanDistance(Point point1, Point point2){
        return Math.sqrt(Math.pow((point1.getX() - point2.getX()), 2) + Math.pow((point1.getY() - point2.getY()), 2));
    }

    public List<List<Point>> findPath(int[][] targetPoints) {
        List<Point> targetPointsCoordinates = targetPointsCoordinates(targetPoints);
        List<List<Point>> totalPath = new ArrayList<>();
        Point previousCoordinate = targetPointsCoordinates.get(0);

        while (targetPointsCoordinates.size() > 0) {
            targetPointsCoordinates.remove(previousCoordinate);
            Point nearestPoint = nearestPoints(previousCoordinate, targetPointsCoordinates);

            if (nearestPoint == null)
                break;

            totalPath.add(aStar(targetPoints, previousCoordinate, nearestPoint));

            previousCoordinate = nearestPoint;
        }

//        for (List<Point> paths : totalPath){
//            for (Point point : paths){
//                System.out.print("[" + (int) point.getX() + " " + (int) point.getY() + "], ");
//            }
//            System.out.println();
//        }

        return totalPath;
    }

    public void makeCommands(int[][] targetPoints) {
        List<List<Point>> totalPath = findPath(targetPoints);
        for(List<Point> path : totalPath) {
            for (int i = 0; i < path.size() - 1; i++){
                if (path.size() == 2) {
                    int difference = (int) Math.abs(path.get(0).getX() - path.get(0).getX() + path.get(1).getY() - path.get(1).getY());
                    if (difference == 1){
                        commands.add(Integer.toString(2));
                    }

                }
            }
        }

        for (List<Point> paths : totalPath){
            for (Point point : paths){
                System.out.print("[" + (int) point.getX() + " " + (int) point.getY() + "], ");
            }
            System.out.println();
        }

        System.out.println();
        for(String command : commands){
            System.out.println(command);
        }

    }

    public List<Point> aStar(int[][] targetPoints, Point startPosition, Point endPosition){
        TurtleNode startNode = new TurtleNode(null, startPosition);
        TurtleNode endNode = new TurtleNode(null, endPosition);

        List<TurtleNode> openList = new ArrayList<>();
        List<TurtleNode> closedList = new ArrayList<>();

        openList.add(startNode);

        while (openList.size() > 0) {
            TurtleNode currentNode = openList.get(0);
            int currentIdx = 0;

            for (int i = 0; i < openList.size(); ++i) {
                if (openList.get(i).getF() < currentNode.getF()) {
                    currentNode = openList.get(i);
                    currentIdx = i;
                }
            }

            openList.remove(currentIdx);
            closedList.add(currentNode);

            if (currentNode.equals(endNode)) {
                List<Point> path = new ArrayList<>(1);
                TurtleNode current = currentNode;
                while (current != null) {
                    path.add(current.getPosition());
                    current = current.getParent();
                }

                Collections.reverse(path);

                return path;
            }

            List<TurtleNode> childrenNodes = new ArrayList<>();
            List<Point> possiblePoints = generateSteps(targetPoints, currentNode);

            for (Point position : possiblePoints) {
                TurtleNode newNode = new TurtleNode(currentNode, position);
                childrenNodes.add(newNode);
            }


            for (TurtleNode child : childrenNodes){
                for (TurtleNode closedChild : closedList){
                    if (child.equals(closedChild)){
                        continue;
                    }
                }

                child.setG(currentNode.getG() + 1);
                child.setH((int) (Math.pow((child.getPosition().getX() - endNode.getPosition().getX()), 2) + Math.pow((child.getPosition().getY() - endNode.getPosition().getY()), 2)));
                child.setF(child.getG() + child.getH());

                for (TurtleNode openNode : openList){
                    if (child.equals(openNode) && child.getG() > openNode.getG()){
                        continue;
                    }
                }

                openList.add(child);
            }
        }

        return new ArrayList<>();
    }

    public List<Point> generateSteps(int[][] targetPoints, TurtleNode currentNode){
        List<Point> possiblePoints = new ArrayList<>();

        for (int i = (int) currentNode.getPosition().getX() + 1; i < SIZE; i++){
            possiblePoints.add(new Point(i, (int) currentNode.getPosition().getY()));
        }

        for (int i = (int) currentNode.getPosition().getX() + 1; i >= 0; i--){
            possiblePoints.add(new Point(i, (int) currentNode.getPosition().getY()));
        }

        for (int i = (int) currentNode.getPosition().getY() + 1; i < SIZE; i++){
            possiblePoints.add(new Point((int) currentNode.getPosition().getX(), i));
        }

        for (int i = (int) currentNode.getPosition().getY() + 1; i >= 0; i--){
            possiblePoints.add(new Point((int) currentNode.getPosition().getX(), i));
        }

        return possiblePoints;
    }

    public void takeCommands() {
        String[] instruction;
        System.out.println("Welcome to Turtle Graphics!\n");
        System.out.println("1\t\tPen up\n2\t\tPen down\n3\t\tTurn right\n4\t\tTurn left\n" +
                "5,10\tMove forwards 10 spaces (replace 10 for a different number of spaces)\n" +
                "6\t\tDisplay the 20-by-20 array\n9\t\tEnd of data (sentinel)\n" +
                "\nPlease, enter your commands:");
        do {
            instruction = scanner.nextLine().split(",");
            switch (instruction[0]) {
                case "1":
                    setDown(false);
                    break;
                case "2":
                    setDown(true);
                    break;
                case "3":
                    turnRight();
                    break;
                case "4":
                    turnLeft();
                    break;
                case "5":
                    move(Integer.parseInt(instruction[1]));
                    break;
                case "6":
                    displayArray();
                    break;
                case "9":
                    break;
                default:
                    break;
            }
        } while(!instruction[0].equals("9"));
    }

    public void setDown(boolean isDown) {
        this.isDown = isDown;
    }

    public void move(int spaces) {
        int i;
        switch (direction) {
            case 1:
                for (i = 1; i <= spaces && y + i < SIZE; i++) {
                    if (isDown) {
                        floor[x][y + i] = 1;
                    }
                }

                y += i - 1;

                break;

            case 2:
                for (i = 1; i <= spaces && x + i < SIZE; i++) {
                    if (isDown) {
                        floor[x + i][y] = 1;
                    }
                }

                x += i - 1;
                break;

            case 3:
                for (i = 1; i <= spaces && y - i >= 0; i++) {
                    if (isDown) {
                        floor[x][y - i] = 1;
                    }
                }

                y -= i - 1;
                break;

            case 4:
                for (i = 1; i <= spaces && x - i >= 0; i++) {
                    if (isDown) {
                        floor[x - i][y] = 1;
                    }
                }

                x -= i - 1;
                break;

            default:
                break;
        }
    }

    public void displayArray() {
        for (int[] row : floor) {
            for (int cell : row) {
                if (cell == 1)
                    System.out.print("*  ");
                else
                    System.out.print("   ");
            }
            System.out.println();
        }
    }

    public void turnRight() {
        direction = direction > 3 ? 1 : direction + 1;
    }

    public void turnLeft() {
        direction = direction < 2 ? 4 : direction - 1;
    }

}
