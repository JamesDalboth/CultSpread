import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JComponent;
import javax.swing.WindowConstants;

public class Node extends JComponent {

  private final static int WIDTH = 100;

  private int x;
  private int y;

  public Node(int x, int y) {
    this.x = x;
    this.y = y;
  }

  @Override
  public void paint(Graphics g) {
    super.paint(g);

    g.setColor(Color.BLACK);

    g.drawArc(x - WIDTH/2, y - WIDTH/2, WIDTH, WIDTH, 0, 360);
  }
}
