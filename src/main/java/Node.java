import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.JComponent;

public class Node extends JComponent {

  private final static int WIDTH = 20;

  private int x;
  private int y;
  private Boolean alive;

  protected List<Node> connections;
  protected List<Boolean> valid_connect;
  private double infidelity;
  private double charisma;
  private int rewardsRate;
  private Color status;
  private Color nextStatus;

  public Node(int x, int y) {
    this.x = x;
    this.y = y;
    this.alive = true;

    this.connections = new ArrayList<Node>();
    this.valid_connect = new ArrayList<Boolean>();

    this.infidelity = 0.5;
    this.charisma = 0.5;
    this.rewardsRate = 0;
    this.status = Color.LIGHT_GRAY;
    this.nextStatus = this.status;
  }

  public boolean isAlive() {
    return this.alive;
  }

  public void addConnection(Node connection) {
    (this.connections).add(connection);
    (this.valid_connect).add(true);
  }

  public void removeConnection(Node connection) {
    int i = connections.indexOf(connection);
    (this.valid_connect).set(i, false);
  }

  public static void link(Node n1, Node n2) {
    n1.addConnection(n2);
    n2.addConnection(n1);
  }

  public static void disableLink(Node n1, Node n2) {
    n1.removeConnection(n2);
    n2.removeConnection(n1);
  }

  public void disableNode() {
    this.alive = false;
    for (int i = 0; i < (this.connections).size(); i++) {
      Node.disableLink(this, connections.get(i));
    }
  }

  public void startConverting() {
    Random random = new Random();
    int i = random.nextInt(connections.size());
    Node node = connections.get(i);
    if (node.isAlive()) {
      node.tryConversion(this);
    }
  }

  public static void convert(Node n1, Node n2) {
    n2.tryConversion(n1);
  }

  public void tryConversion(Node attacker) {
    double probability = ((this.infidelity) * (attacker.getCharisma())) * 100;
    int random = (int) (Math.random() * 100 + 1);
    if ((random > 0) && (random <= probability)) {
      this.nextStatus = attacker.getStatus();
      this.infidelity = 0.3;
      this.charisma = 0.6;
      this.rewardsRate = 1;
    }
  }

  public double getCharisma() {
    return this.charisma;
  }

  public Color getStatus() {
    return this.status;
  }

  public void setStatus(Color status) {
    this.status = status;
  }

  @Override
  public void paint(Graphics g) {
    super.paint(g);




    g.setColor(Color.WHITE);
    for (Node node : connections) {
      int i = connections.indexOf(node);
      if (this.valid_connect.get(i)) {
        g.drawLine(this.x, this.y, node.x, node.y);
      }
    }

    g.setColor(status);
    g.fillArc(x - WIDTH / 2, y - WIDTH / 2, WIDTH, WIDTH, 0, 360);
  }

  public Color getNextStatus() {
    return nextStatus;
  }

  public void setNextStatus(Color nextStatus) {
    this.nextStatus = nextStatus;
  }

  public boolean isHit(MouseEvent e) {
    if (distance(e.getX(), e.getY(), this.x, this.y) < WIDTH/2 && isAlive()) {
      if (getStatus() == Color.lightGray) {
        this.setNextStatus(Color.BLUE);
        this.setStatus(Color.BLUE);
        return true;
      }
    }
    return false;
  }

  public double distance(int x1, int y1, int x2, int y2) {
    return Math.sqrt(Math.pow(x1 - x2 , 2.0) + Math.pow(y1 - y2, 2.0));
  }
}
