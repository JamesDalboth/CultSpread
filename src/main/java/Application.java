import main.java.Node;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.util.Random;

public class Application extends JFrame {
  public final static int WIDTH = 1000;
  public final static int HEIGHT = 800;
  public final static int W_GRIDCOUNT = WIDTH/20;
  public final static int H_GRIDCOUNT = HEIGHT/20;


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



      nodegen(nodes);
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


  private void nodegen(List<Node> nodes){
    for(int j = 0; j<(H_GRIDCOUNT/2)-1; j++){
      for(int i = 0; i<=(W_GRIDCOUNT/3); i++){
        nodes.add(new Node(10+60*i,10+40*j));
      }
      for(int i = 0; i<(W_GRIDCOUNT/3); i++){
        nodes.add(new Node(40+60*i,30+40*j));
      }
    }

    for(int i = 0; i<250 /*TEST VARIABLE*/; i++){
      Random rand = new Random();
      int n = rand.nextInt(nodes.size());
      nodes.remove(n);
    }
  }

}
