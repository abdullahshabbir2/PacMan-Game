
import java.awt.*;
import java.util.*;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class PacMan_Level1_DFS extends JFrame {

    public static void main(String[] args) {
        PacMan_Level1_DFS p1 = new PacMan_Level1_DFS();
        p1.setVisible(true);//making JFrame visible

    }
    Scanner sc;

    //drawing maze
    //1 is blocked box
    //0 is open box
    private int[][] maze = {
        {1, 1, 1, 1, 1, 1},
        {1, 0, 0, 1, 0, 1},
        {1, 0, 0, 1, 0, 1},
        {1, 1, 0, 0, 0, 1},
        {1, 0, 0, 1, 0, 1},
        {1, 1, 1, 1, 1, 1}};

    private final ArrayList<Integer> path = new ArrayList<Integer>();
    private int pathIndex;

    public PacMan_Level1_DFS() {
        setTitle("PacMan Level 1");
        setSize(640, 480);//size of JFrame panel
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        sc = new Scanner(System.in);

        //taking data of position of food and pacman from user
        System.out.println("------LEVEL 1------");
        System.out.println("Enter x-axis for pacman:");
        int x_pac = sc.nextInt();
        System.out.println("Enter y-axis for pacman:");
        int y_pac = sc.nextInt();
        while (maze[x_pac][y_pac] == 1||x_pac>=maze[0].length||y_pac>=maze[0].length) {

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
        while (maze[x_food][y_food] == 1||x_food>=maze[0].length||y_food>=maze[0].length) {

            System.out.println("You can't select blocked box or index out of box");
            System.out.println("Enter x-axis for food again:");
            x_food = sc.nextInt();
            System.out.println("Enter y-axis for food again:");
            y_food = sc.nextInt();
        }

        maze[x_food][y_food] = 9;//marking food box with 9
        DepthFirst.FindPath(maze, x_pac, y_pac, path);//calling path finding method

        pathIndex = path.size() - 2;

        //game loop
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if (pathIndex != -2) {
                    update();
                    repaint();
                }

            }
        }, 100, 500);

    }

    public void update() {

        pathIndex -= 2;

        if (pathIndex == -2) {
            JOptionPane.showMessageDialog(null, "You won\n Going to Level 2");
            dispose();
            PacMan_Level2_AStar p2 = new PacMan_Level2_AStar();//making object of AStar class
            p2.setVisible(true);//setting visible so that AStar JFrame will run
        }

    }

    @Override
    public void paint(Graphics g) {

        try {
            g.translate(50, 50);

            //for setting the colour of the boxes
            for (int row = 0; row < maze.length; row++) {
                for (int col = 0; col < maze[0].length; col++) {
                    Color color;
                    switch (maze[row][col]) {
                        case 1:
                            color = Color.DARK_GRAY;
                            break;
                        case 9:
                            color=Color.YELLOW;//for food
                            break;
                        default:
                            color = Color.WHITE;
                    }
                    g.setColor(color);
                    g.fillRect(30 * col, 30 * row, 30, 30);
                    g.setColor(Color.BLACK);
                    g.drawRect(30 * col, 30 * row, 30, 30);
                }
            }

            // draw the ball on path
            int pathX = path.get(pathIndex);
            int pathY = path.get(pathIndex + 1);
            g.setColor(Color.BLUE);
            g.fillOval(pathX * 30, pathY * 30, 30, 30);
            
        } catch (Exception ex) {
            //if the path isn't found
            JOptionPane.showMessageDialog(null, "You Lost\nPath doesn't exist");
            System.exit(0);
        }
    }
}

class DepthFirst {

    public static boolean FindPath(int[][] maze, int row, int col, ArrayList<Integer> path) {

        //adding target box first
        if (maze[row][col] == 9) {
            path.add(col);
            path.add(row);
            return true;

        }

        //allowing open boxes only
        if (maze[row][col] == 0) {
            maze[row][col] = 2;

            // for checking up
            if (FindPath(maze, row + 0, col - 1, path)) {
                path.add(col);
                path.add(row);
                return true;
            }

            //for checking down
            if (FindPath(maze, row + 0, col + 1, path)) {
                path.add(col);
                path.add(row);
                return true;
            }

            //for checking left
            if (FindPath(maze, row - 1, col + 0, path)) {
                path.add(col);
                path.add(row);
                return true;
            }

            //for checking right
            if (FindPath(maze, row + 1, col + 0, path)) {
                path.add(col);
                path.add(row);
                return true;
            }
        }
        return false;
    }

}
