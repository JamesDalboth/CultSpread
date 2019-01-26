package main.java;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.WindowConstants;

public class Node extends JComponent {

  private final static int WIDTH = 20;

  private int x;
  private int y;

  private List<Node> connections;

  public Node(int x, int y) {
    this.x = x;
    this.y = y;
    this.connections = new ArrayList<>();
  }

  public void addConnection(Node connection) {
    (this.connections).add(connection);
  }

  public static void link(Node n1, Node n2) {
    n1.addConnection(n2);
    n2.addConnection(n1);
  }

  @Override
  public void paint(Graphics g) {
    super.paint(g);

    g.setColor(Color.BLACK);

    g.drawArc(x - WIDTH/2, y - WIDTH/2, WIDTH, WIDTH, 0, 360);
  }
}