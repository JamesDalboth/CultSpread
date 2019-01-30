import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Node extends JComponent {

  public final static int WIDTH = 50;
  protected List<Node> connections;
  private int x;
  private int y;
  private final static double INFIDELITY = 0.5;
  private final static double CHARISMA = 0.5;
  private final static double REWARDS = 1;
  private Cult status;
  private Cult nextStatus;

  private Specialty specialty = Specialty.NONE;

  private World world;

  public Node(int x, int y, World world) {
    this.x = x;
    this.y = y;

    this.world = world;

    this.connections = new ArrayList<Node>();

    this.status = Cult.NEUTRAL;
    this.nextStatus = this.status;
  }

  public static void link(Node n1, Node n2) {
    n1.addConnection(n2);
    n2.addConnection(n1);
  }

  public static void disableLink(Node n1, Node n2) {
    if (n1.connections.size() > 1 && n2.connections.size() > 1) {
      removeLink(n1, n2);
    }
  }

  private static void removeLink(Node n1, Node n2) {
    n1.removeConnection(n2);
    n2.removeConnection(n1);
  }

  public static double distance(int x1, int y1, int x2, int y2) {
    return Math.sqrt(Math.pow(x1 - x2, 2.0) + Math.pow(y1 - y2, 2.0));
  }

  public void addConnection(Node connection) {
    connections.add(connection);
  }

  public void removeConnection(Node connection) {
    connections.remove(connection);
  }

  public void startConverting() {
    if (status == Cult.BLUE) {
      double rewards = REWARDS;

      if (specialty == Specialty.CASH) {
        rewards += 1;
      }

      World.REWARD_TOKENS += rewards * 0.05;
    }

    Random random = new Random();
    int i = random.nextInt(connections.size());
    if (i == 0) {
      return;
    }

    Node node = connections.get(i);
    node.tryConversion(this);

    world.repaint();
  }

  public void tryConversion(Node attacker) {
    double infidelity = INFIDELITY;
    double charisma = CHARISMA;

    switch (specialty) {
      case PRISONER:
        infidelity -= 0.3;
        break;
      case PRIEST:
        charisma += 0.3;
        break;
    }
    double probability = (infidelity * charisma) * 100;
    Random rand = new Random();
    int n = rand.nextInt(100) + 1;
    if (n <= probability) {
      this.nextStatus = attacker.getStatus();
    }
  }


  public void matyr() {
    this.setNextStatus(Cult.RED);
    for (Node connection : this.connections) {
      connection.setNextStatus(Cult.RED);
      connection.setStatus(Cult.RED);
    }
  }

  public void bomb() {
    for (Node connection : this.connections) {
      connection.setNextStatus(this.getStatus());
      connection.setStatus(this.getStatus());
    }
  }


  public Cult getStatus() {
    return this.status;
  }

  public void setStatus(Cult status) {
    this.status = status;
  }

  public void paintNodes(Graphics g) {
    super.paint(g);

    switch (status) {
      case RED:
        g.setColor(Color.RED);
        break;
      case BLUE:
        g.setColor(Color.BLUE);
        break;
      case NEUTRAL:
        g.setColor(Color.WHITE);
        break;
    }

    g.fillArc(x - WIDTH / 2, y - WIDTH / 2 + World.LABEL_SPACE, WIDTH, WIDTH, 0, 360);

    g.setColor(Color.BLACK);
    g.drawArc(x - WIDTH / 2, y - WIDTH / 2 + World.LABEL_SPACE, WIDTH, WIDTH, 0, 360);

    g.setFont(new Font("TimesRoman", Font.BOLD, 20));

    switch (specialty) {
      case PRIEST:
        World.drawString(g, "S",x, y + World.LABEL_SPACE + 10);
        break;
      case PRISONER:
        World.drawString(g, "W",x, y + World.LABEL_SPACE + 10);
        break;
      case CASH:
        World.drawString(g, "C",x, y + World.LABEL_SPACE + 10);
        break;
    }
  }

  public void paintConnection(Graphics g) {
    super.paint(g);

    Graphics2D g2 = (Graphics2D) g;

    g2.setColor(Color.BLACK);
    g2.setStroke(new BasicStroke(5));

    for (Node node : connections) {
      int i = connections.indexOf(node);
      g2.drawLine(this.x, this.y + World.LABEL_SPACE, (node.x - this.x) / 2 + this.x,
        (node.y - this.y) / 2 + this.y + World.LABEL_SPACE
      );
    }

    switch (status) {
      case RED:
        g2.setColor(Color.RED);
        break;
      case BLUE:
        g2.setColor(Color.BLUE);
        break;
      case NEUTRAL:
        g2.setColor(Color.BLACK);
        break;
    }

    g2.setStroke(new BasicStroke(3));

    for (Node node : connections) {
      int i = connections.indexOf(node);
      g2.drawLine(this.x, this.y + World.LABEL_SPACE, (node.x - this.x) / 2 + this.x,
        (node.y - this.y) / 2 + this.y + World.LABEL_SPACE
      );
    }
  }

  public Cult getNextStatus() {
    return nextStatus;
  }

  public void setNextStatus(Cult nextStatus) {
    this.nextStatus = nextStatus;
  }

  public void kill() {
    Node node;
    while (connections.size() > 0) {
      node = connections.remove(0);
      Node.removeLink(this, node);
    }
  }

  public boolean isHit(MouseEvent e) {
    if (distance(e.getX(), e.getY(), this.x, this.y + World.LABEL_SPACE) < WIDTH / 2) {
      return true;
    }
    return false;
  }

  public Specialty getSpecialty() {
    return specialty;
  }

  public void setSpecialty(Specialty specialty) {
    this.specialty = specialty;
  }
}
