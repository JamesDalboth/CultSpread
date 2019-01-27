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
  private Boolean alive;

  protected List<Node> connections;
  protected List<Boolean> valid_connect;

  public Node(int x, int y) {
    this.x = x;
    this.y = y;
    this.alive = true;

    this.connections = new ArrayList<Node>();
    this.valid_connect = new ArrayList<Boolean>();
  }

  public boolean isAlive(){
    return this.alive;
  }

  public void addConnection(Node connection) {
    (this.connections).add(connection);
    (this.valid_connect).add(true);
  }

  public void removeConnection(Node connection) {
    int i;
    for(i = 0; (this.connections).get(i)!=connection; i++){ }
    if(i==this.connections.size()){
      System.out.println("Somethings gone wrong ;(");
    }
    (this.valid_connect).set(i, false);
  }


  public static void link(Node n1, Node n2) {
    n1.addConnection(n2);
    n2.addConnection(n1);
  }

  public static void disableLink(Node n1, Node n2){
    n1.removeConnection(n2);
    n2.removeConnection(n1);
  }

  public void disableNode(){
    this.alive = false;
    for(int i = 0; i<(this.connections).size(); i++){
      disableLink(this, connections.get(i));
    }
  }

  @Override
  public void paint(Graphics g) {
    super.paint(g);

    g.setColor(Color.BLACK);

    g.drawArc(x - WIDTH/2, y - WIDTH/2, WIDTH, WIDTH, 0, 360);
  }
}
