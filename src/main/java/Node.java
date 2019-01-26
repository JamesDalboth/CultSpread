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

  private double infidelity;
  private double charisma;
  private int rewardsRate;
  private char status;


  private List<Node> connections;

  public Node(int x, int y) {
    this.x = x;
    this.y = y;
    this.connections = new ArrayList<>();
    this.infidelity = 0.5;
    this.charisma = 0.5;
    this.rewardsRate = 0;
    this.status = 'N';
  }

  public void addConnection(Node connection) {
    (this.connections).add(connection);
  }

  public static void link(Node n1, Node n2) {
    n1.addConnection(n2);
    n2.addConnection(n1);
  }

  public static void convert(Node n1, Node n2) {
      n2.tryConversion(n1);
  }

  public void tryConversion(Node attacker) {
      double probability = ((this.infidelity)*(attacker.getCharisma()))*100;
      int random = (int)(Math.random() * 100 + 1);
      if((random > 0) && (random <= probability)) {
          this.status = attacker.getStatus();
          this.infidelity = 0.3;
          this.charisma = 0.6;
          this.rewardsRate = 1;
      }
  }

  public double getCharisma() {
      return this.charisma;
  }

  public char getStatus() {
      return this.status;
  }

  @Override
  public void paint(Graphics g) {
    super.paint(g);

    g.setColor(Color.BLACK);

    g.drawArc(x - WIDTH/2, y - WIDTH/2, WIDTH, WIDTH, 0, 360);
  }
}
