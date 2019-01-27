import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.JComponent;

public class Node extends JComponent {

  public final static int WIDTH = 20;

  private int x;
  private int y;


  protected List<Node> connections;

  private double infidelity;
  private double charisma;
  private int rewardsRate;
  private Cult status;
  private Cult nextStatus;

  public Node(int x, int y) {
    this.x = x;
    this.y = y;

    this.connections = new ArrayList<Node>();

    this.infidelity = 0.5;
    this.charisma = 0.5;
    this.rewardsRate = 0;
    this.status = Cult.NEUTRAL;
    this.nextStatus = this.status;
  }

  public void addConnection(Node connection) {
    connections.add(connection);
  }

  public void removeConnection(Node connection) {
    connections.remove(connection);
  }

  public static void link(Node n1, Node n2) {
    n1.addConnection(n2);
    n2.addConnection(n1);
  }

  public static void disableLink(Node n1, Node n2) {
    //Delete connection if both nodes have more than 1 connection
    if(n1.connections.size() > 1 && n2.connections.size() > 1) {
      removeLink(n1, n2);
    }
  }

  private static void removeLink(Node n1, Node n2) {
    n1.removeConnection(n2);
    n2.removeConnection(n1);
  }

  public void startConverting() {
    Random random = new Random();
    int i = random.nextInt(connections.size());
    Node node = connections.get(i);
    node.tryConversion(this);
    if (status == Cult.BLUE) {
      World.REWARD_TOKENS += this.rewardsRate * 0.1;
    }
    World.redrawLabel();
  }

  public void tryConversion(Node attacker) {
    double probability = ((this.infidelity) * (attacker.getCharisma())) * 100;
    Random rand = new Random();
    int n = rand.nextInt(100) + 1;
    if (n <= probability) {
      this.nextStatus = attacker.getStatus();
      this.infidelity = 0.3;
      this.charisma = 0.6;
      this.rewardsRate = 1;
    }
  }

  public double getCharisma() {
    return this.charisma;
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
        g.setColor(Color.LIGHT_GRAY);
        break;
    }
    g.fillArc(x - WIDTH / 2, y - WIDTH / 2 + World.LABEL_SPACE, WIDTH, WIDTH, 0, 360);
  }

  public void paintConnection(Graphics g) {
    super.paint(g);

    switch (status) {
      case RED:
        g.setColor(Color.RED);
        break;
      case BLUE:
        g.setColor(Color.BLUE);
        break;
      case NEUTRAL:
        g.setColor(Color.LIGHT_GRAY);
        break;
    }

    for (Node node : connections) {
      int i = connections.indexOf(node);
      g.drawLine(this.x, this.y + World.LABEL_SPACE, (node.x - this.x) / 2 + this.x,
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

  public boolean attemptUpgrade(String upgrade) {
    if (this.status == Cult.BLUE) {
      if (upgrade == "Charisma") {
        if (World.REWARD_TOKENS >= 50) {
          this.charisma = this.charisma + 0.1;
          World.REWARD_TOKENS -= 50;
          World.redrawLabel();
          return true;
        } else {
          return false;
        }
      } else if (upgrade == "Infidelity") {
        if (World.REWARD_TOKENS >= 50) {
          this.infidelity = this.infidelity - 0.3;
          World.REWARD_TOKENS -= 50;
          World.redrawLabel();
          return true;
        } else {
          return false;
        }
      } else if (upgrade == "Reward Rate") {
        if (World.REWARD_TOKENS >= 50) {
          this.rewardsRate = this.rewardsRate + 2;
          World.REWARD_TOKENS -= 50;
          World.redrawLabel();
          return true;
        } else {
          return false;
        }
      } else if (upgrade == "bomb") {
        if (World.REWARD_TOKENS >= 200) {
          bomb();
          World.REWARD_TOKENS -= 200;
          World.redrawLabel();
          return true;
        } else {
          return false;
        }
      } else if (upgrade == "matyr") {
        matyr();
        World.REWARD_TOKENS += 300;
        World.redrawLabel();
        return true;
      } else {
        return false;
      }
    } else {
      return false;
    }
  }

  private void matyr() {
    this.setNextStatus(Cult.RED);
    for (Node node : connections) {
      node.setNextStatus(Cult.RED);
      node.setStatus(Cult.RED);
    }
  }

  public void bomb() {
    for (Node node : connections) {
      node.setNextStatus(getStatus());
      node.setStatus(getStatus());
    }
  }

  public void kill() {
    Node node;
    while (connections.size() > 0) {
      node = connections.remove(0);
      Node.removeLink(this, node);
    }
  }

  public boolean isHit(MouseEvent e) {
    if (distance(e.getX(), e.getY(), this.x, this.y + World.LABEL_SPACE) < WIDTH/2) {
      return true;
    }
    return false;
  }

  public static double distance(int x1, int y1, int x2, int y2) {
    return Math.sqrt(Math.pow(x1 - x2 , 2.0) + Math.pow(y1 - y2, 2.0));
  }
}
