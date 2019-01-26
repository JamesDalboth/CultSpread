package main.java;

import org.w3c.dom.Node;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Application extends JFrame {
  public final static int WIDTH = 1000;
  public final static int HEIGHT = 800;

  public Application() {
    initUI();

    setVisible(true);
  }

  public void initUI() {
    JPanel panel = new World();
    setContentPane(panel);

    setSize(WIDTH, HEIGHT);
    setTitle("Cultio");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLocationRelativeTo(null);
  }

  private class World extends JPanel {
    List<Node> nodes;

    public World() {
      nodes = new ArrayList<Node>();

      nodes.add(new Node(400, 200));
    }

    @Override
    public void paintComponent(Graphics g) {
      super.paintComponents(g);

      g.setColor(Color.WHITE);
      g.fillRect(0,0, Application.WIDTH, Application.WIDTH);

      for (Node node : nodes) {
        node.paint(g);
      }
    }
  }
}
