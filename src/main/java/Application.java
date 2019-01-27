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

      g.setColor(Color.BLACK);
      g.fillRect(0,0, Application.WIDTH, Application.WIDTH);

      for (Node node : nodes) {
        if(node.isAlive()) {
          node.paint(g);
        }
      }
    }
  }

  //Creating all possible nodes and their connections
  private void nodegen(List<Node> nodes){

    ////////////////////////////////////////////////////////////////////////////
    for(int j = 0; j<(H_GRIDCOUNT/2)-1; j++){

      //Create first node in line 1
      nodes.add(new Node(10,10+40*j));
      int last = nodes.size()-1;
      if(last-1>=0) {
        if(last-16>=0) {
          Node.link(nodes.get(last), nodes.get(last - 16));
          if (last - 33 >= 0) {
            Node.link(nodes.get(last), nodes.get(last - 33));
          }
        }
      }
      //Create the middle of the nodes in the rest of the lines
      for(int i = 1; i<(W_GRIDCOUNT/3); i++){
        nodes.add(new Node(10+60*i,10+40*j));
        last = nodes.size()-1;
        if(last-1>=0) {
          Node.link(nodes.get(last), nodes.get(last - 1));
          if(last-16>=0) {
            Node.link(nodes.get(last), nodes.get(last - 16));
            if (last - 17 >= 0) {
              Node.link(nodes.get(last), nodes.get(last - 17));
              if (last - 33 >= 0) {
                Node.link(nodes.get(last), nodes.get(last - 33));
              }
            }
          }
        }
      }

      //Create the last node in Line 1
      nodes.add(new Node(10+60*(W_GRIDCOUNT/3),10+40*j));
      last = nodes.size()-1;
      if(last-1>=0) {
        Node.link(nodes.get(last), nodes.get(last - 1));
        if (last - 17 >= 0) {
          Node.link(nodes.get(last), nodes.get(last - 17));
          if (last - 33 >= 0) {
            Node.link(nodes.get(last), nodes.get(last - 33));
          }
        }
      }


      //Create first node in line 2
      nodes.add(new Node(40,30+40*j));
      last = nodes.size()-1;
      if(last-1>=0) {
        if(last-16>=0) {
          Node.link(nodes.get(last), nodes.get(last - 16));
          if (last - 17 >= 0) {
            Node.link(nodes.get(last), nodes.get(last - 17));
            if (last - 33 >= 0) {
              Node.link(nodes.get(last), nodes.get(last - 33));
            }
          }
        }
      }



      for(int i = 1; i<(W_GRIDCOUNT/3); i++){
        nodes.add(new Node(40+60*i,30+40*j));
        last = nodes.size()-1;
        if(last-1>=0) {
          Node.link(nodes.get(last), nodes.get(last - 1));
          if(last-16>=0) {
            Node.link(nodes.get(last), nodes.get(last - 16));
            if (last - 17 >= 0) {
              Node.link(nodes.get(last), nodes.get(last - 17));
              if (last - 33 >= 0) {
                Node.link(nodes.get(last), nodes.get(last - 33));
              }
            }
          }
        }
      }

    }

    for(int i = 0; i<250 /*TEST VARIABLE*/; i++){
      Random rand = new Random();
      int n = rand.nextInt(nodes.size());
      (nodes.get(n)).disableNode();
    }

    /////////////////////////////////////////////////////////////////////////////

    for(int i = 0; i<1500 /*TEST VARIABLE*/; i++){
      Random rand = new Random();
      int n = rand.nextInt(nodes.size());
      int s_con = ((nodes.get(n)).connections).size();
      int con = rand.nextInt(s_con);

      Node.disableLink2(nodes.get(n), ((nodes.get(n).connections).get(con)));

      for(int r = 0; r<nodes.size(); r++){
        Boolean del = true;
        if((nodes.get(r)).isAlive()){
          for(int s = 0; s<((nodes.get(r)).connections).size();s++){
            if(((nodes.get(r)).valid_connect).get(s)==true){
              del = false;
            }
          }
        }
        if(del){
          (nodes.get(r)).disableNode();
        }

      }

    }


  }

}
