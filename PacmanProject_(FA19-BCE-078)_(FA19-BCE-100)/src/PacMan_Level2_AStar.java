
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.JOptionPane;
import java.awt.Color;
import java.awt.Graphics;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JFrame;

public class PacMan_Level2_AStar extends JFrame {

    Scanner sc;
    private List<Node> open;
    private List<Node> closed;
    private List<Node> path;
    private int pathIndex = 0;

    //drawing maze
    //100 is blocked boxes
    //0 are open
    private static int[][] maze = {
        {100,100,100,100,100,100},
        {100,  0,  0,  0,  0,100},
        {100,  0,  0,100,100,100},
        {100,  0,  0,  0,  0,100},
        {100,  0,  0,  0,  0,100},
        {100,100,100,100,  0,  0},};

    private Node now;
    private int xstart;
    private int ystart;
    private int xend, yend;

    public static void main(String[] args) {

        PacMan_Level2_AStar p2 = new PacMan_Level2_AStar();
        p2.setVisible(true);
    }

    public PacMan_Level2_AStar() {
        setSize(400, 400); //setting size of JFrame
        setTitle("PacMan Level 2");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        sc = new Scanner(System.in);

        //taking data of position of food and pacman from user
        System.out.println("\n------LEVEL 2------");
        System.out.println("Enter x-axis for pacman:");
        int x_pac = sc.nextInt();
        System.out.println("Enter y-axis for pacman:");
        int y_pac = sc.nextInt();
        while (maze[x_pac][y_pac] == 100||x_pac>=maze[0].length||y_pac>=maze[0].length) {

            System.out.println("You can't select blocked box or index out of box");
            System.out.println("Enter x-axis for pacman again:");
            x_pac = sc.nextInt();
            System.out.println("Enter y-axis for pacman again:");
            y_pac = sc.nextInt();
        }
        System.out.println("Enter x-axis for food box:");
        int x_food = sc.nextInt();
        System.out.println("Enter y-axis for food box:");
        int y_food = sc.nextInt();
        while (maze[x_food][y_food] == 100||x_food>=maze[0].length||y_food>=maze[0].length) {

            System.out.println("You can't select blocked box or index out of box");
            System.out.println("Enter x-axis for food again:");
            x_food = sc.nextInt();
            System.out.println("Enter y-axis for food again:");
            y_food = sc.nextInt();
        }

        PacMan_Level2_AStar as = new PacMan_Level2_AStar(maze, x_pac, y_pac); //making object of class Astar
        path = as.findPathTo(x_food, y_food); //calling path finding method

        //game loop for printing ball again and again
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if (pathIndex < path.size() - 1) {
                    update();
                    repaint();
                    if (pathIndex == path.size() - 1) {
                        JOptionPane.showMessageDialog(null, "You won");
                        dispose();// automatically close the JFrame
                    }
                }
            }
        }, 1000, 500);
    }

    public void update() {
        if (pathIndex < path.size() - 1) {
            pathIndex++;
        }
    }

    @Override
    public void paint(Graphics g) {
        g.translate(50, 50); //allignment of the maze
        if (path != null) {

            for (Node temp : path) {
                System.out.print("[" + temp.x + ", " + temp.y + "] ");
                maze[temp.y][temp.x] = -1;
                
            }

            //for setting colour of the boxes
            for (int row = 0; row < maze.length; row++) {
                for (int col = 0; col < maze[0].length; col++) {
                    Color c = null;

                    switch (maze[row][col]) {
                        case 0:
                            c = Color.WHITE;
                            break;

                        case -1:
                            c = Color.WHITE;
                            break;
                        case 100:
                            c = Color.DARK_GRAY;
                            break;
                    }
                    g.setColor(c);
                    g.fillRect(30 * col, 30 * row, 30, 30);
                    g.setColor(Color.BLACK);
                    g.drawRect(30 * col, 30 * row, 30, 30);
                }

            }
            System.out.println();

            // draw the ball on path
            System.out.println(pathIndex);

            int pathX = path.get(pathIndex).x; //setting path index for ball
            int pathY = path.get(pathIndex).y;

            g.setColor(Color.BLUE); //printing ball 
            g.fillOval(pathX * 30, pathY * 30, 30, 30);

            g.setColor(Color.YELLOW); //for printing food
            g.fillOval(31 * path.get(path.size() - 1).x, 31 * path.get(path.size() - 1).y, 15, 15);
        }
        if (path == null) {
            JOptionPane.showMessageDialog(null, "You Lost");
            dispose();// automatically close the JFrame
        }
    }

    // Node class
    static class Node implements Comparable<Node> {

        public Node parent;
        public int x, y;
        public double g;
        public double h;

        //fully parametrized constructor
        Node(Node parent, int xpos, int ypos, double g, double h) {
            this.parent = parent;
            this.x = xpos;
            this.y = ypos;
            this.g = g;
            this.h = h;
        }

        // Compare by f value (g + h)
        @Override
        public int compareTo(Node o) {
            Node that = (Node) o;
            return (int) ((this.g + this.h) - (that.g + that.h));
        }
    }

    //paramterized constructor for AStar class
    PacMan_Level2_AStar(int[][] maze, int xstart, int ystart) {
        this.open = new ArrayList<>();
        this.closed = new ArrayList<>();
        this.path = new ArrayList<>();
        PacMan_Level2_AStar.maze = maze;
        this.now = new Node(null, xstart, ystart, 0, 0);
        this.xstart = xstart;
        this.ystart = ystart;
    }

    //will find path to target position or will return null
    //xend and yend are coordinates for target box
    public List<Node> findPathTo(int xend, int yend) {

        this.xend = xend;
        this.yend = yend;
        this.closed.add(this.now);
        addNeigborsToOpenList();
        while (this.now.x != this.xend || this.now.y != this.yend) {
            if (this.open.isEmpty()) { // Nothing to examine
                return null;
            }
            this.now = this.open.get(0); // get first node (lowest f score)
            this.open.remove(0); // remove it
            this.closed.add(this.now); // and add to the closed
            addNeigborsToOpenList();
        }
        this.path.add(0, this.now);
        while (this.now.x != this.xstart || this.now.y != this.ystart) {
            this.now = this.now.parent;
            this.path.add(0, this.now);
        }
        return this.path;
    }

    //will check for neibours
    private static boolean findNeighborInList(List<Node> array, Node node) {
        return array.stream().anyMatch((n) -> (n.x == node.x && n.y == node.y));
    }

    private double distance(int dx, int dy) {

        /*calculating manhattan distance for heuristic h means the value of h is
        being calculated by this formula*/
        return Math.abs(this.now.x + dx - this.xend) + Math.abs(this.now.y + dy - this.yend);

    }

    private void addNeigborsToOpenList() {
        Node node;
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                if (x != 0 && y != 0) {
                    continue;
                }
                node = new Node(this.now, this.now.x + x, this.now.y + y, this.now.g, this.distance(x, y));
                if ((x != 0 || y != 0) // not this.now
                        && this.now.x + x >= 0 && this.now.x + x < this.maze[0].length // check maze boundaries
                        && this.now.y + y >= 0 && this.now.y + y < this.maze.length
                        && this.maze[this.now.y + y][this.now.x + x] != -1 // check if square is walkable
                        && !findNeighborInList(this.open, node) && !findNeighborInList(this.closed, node)) { // if not already done
                    node.g = node.parent.g + 1.; // Horizontal/vertical cost = 1.0
                    node.g += maze[this.now.y + y][this.now.x + x]; // add movement cost for this square

                    this.open.add(node);
                }
            }
        }
        Collections.sort(this.open);
    }

}
