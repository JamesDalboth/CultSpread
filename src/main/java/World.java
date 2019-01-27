import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JPanel;

public class World extends JPanel {

  List<Node> nodes;
  static int placedNodes = 0;
  public final static int W_GRIDCOUNT = Application.WIDTH / 20;
  public final static int H_GRIDCOUNT = Application.HEIGHT / 20;

  public World() {
    nodes = new ArrayList<Node>();

    System.out.println(nodes.size());;

    nodegen(nodes);

    pickEnemyPiece();
    pickEnemyPiece();

    this.repaint();

    selectStartingPieces();
  }

  private void pickEnemyPiece() {
    boolean search = true;
    while (search) {
      Random random = new Random();
      int i = random.nextInt(nodes.size());
      if (nodes.get(i).isAlive() && nodes.get(i).getStatus() == Color.LIGHT_GRAY) {
        nodes.get(i).setNextStatus(Color.RED);
        nodes.get(i).setStatus(Color.RED);
        search = false;
      }
    }
  }

  private void selectStartingPieces() {
    final World world = this;

    this.addMouseListener(new MouseListener() {
      public void mouseClicked(MouseEvent e) {

      }

      public void mousePressed(MouseEvent e) {
        if (placedNodes < 2) {
          for (Node node : nodes) {
            if (node.isHit(e)) {
              world.placed();
              world.repaint();
              return;
            }

          }
        }
      }

      public void mouseReleased(MouseEvent e) {

      }

      public void mouseEntered(MouseEvent e) {

      }

      public void mouseExited(MouseEvent e) {

      }
    });
  }

  public void placed() {
    placedNodes++;
    if (placedNodes == 2) {
      Run();
    }
  }

  private void Run() {
    Timer time = new Timer();
    final World world = this;
    time.scheduleAtFixedRate(new TimerTask() {
      @Override
      public void run() {
        for (Node node : nodes) {
          if (node.getStatus() != Color.LIGHT_GRAY) {
            node.startConverting();
          }
        }

        for (Node node : nodes) {
          node.setStatus(node.getNextStatus());
        }
        world.repaint();
      }
    }, 1000, 1000);
  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponents(g);

    g.setColor(Color.BLACK);
    g.fillRect(0, 0, Application.WIDTH, Application.WIDTH);

    for (Node node : nodes) {
      if (node.isAlive()) {
        node.paint(g);
      }
    }
  }

  //Creating all possible nodes and their connections
  private void nodegen(List<Node> nodes) {

    ////////////////////////////////////////////////////////////////////////////
    for (int j = 0; j < (H_GRIDCOUNT / 2) - 1; j++) {

      //Create first node in line 1
      nodes.add(new Node(10, 10 + 40 * j));
      int last = nodes.size() - 1;
      if (last - 1 >= 0) {
        if (last - 16 >= 0) {
          Node.link(nodes.get(last), nodes.get(last - 16));
          if (last - 33 >= 0) {
            Node.link(nodes.get(last), nodes.get(last - 33));
          }
        }
      }
      //Create the middle of the nodes in the rest of the lines
      for (int i = 1; i < (W_GRIDCOUNT / 3); i++) {
        nodes.add(new Node(10 + 60 * i, 10 + 40 * j));
        last = nodes.size() - 1;
        if (last - 1 >= 0) {
          Node.link(nodes.get(last), nodes.get(last - 1));
          if (last - 16 >= 0) {
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
      nodes.add(new Node(10 + 60 * (W_GRIDCOUNT / 3), 10 + 40 * j));
      last = nodes.size() - 1;
      if (last - 1 >= 0) {
        Node.link(nodes.get(last), nodes.get(last - 1));
        if (last - 17 >= 0) {
          Node.link(nodes.get(last), nodes.get(last - 17));
          if (last - 33 >= 0) {
            Node.link(nodes.get(last), nodes.get(last - 33));
          }
        }
      }

      //Create first node in line 2
      nodes.add(new Node(40, 30 + 40 * j));
      last = nodes.size() - 1;
      if (last - 1 >= 0) {
        if (last - 16 >= 0) {
          Node.link(nodes.get(last), nodes.get(last - 16));
          if (last - 17 >= 0) {
            Node.link(nodes.get(last), nodes.get(last - 17));
            if (last - 33 >= 0) {
              Node.link(nodes.get(last), nodes.get(last - 33));
            }
          }
        }
      }

      for (int i = 1; i < (W_GRIDCOUNT / 3); i++) {
        nodes.add(new Node(40 + 60 * i, 30 + 40 * j));
        last = nodes.size() - 1;
        if (last - 1 >= 0) {
          Node.link(nodes.get(last), nodes.get(last - 1));
          if (last - 16 >= 0) {
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

    for (int i = 0; i < 250 /*TEST VARIABLE*/; i++) {
      Random rand = new Random();
      int n = rand.nextInt(nodes.size());
      (nodes.get(n)).disableNode();
    }

    /////////////////////////////////////////////////////////////////////////////

    for (int i = 0; i < 1500 /*TEST VARIABLE*/; i++) {
      Random rand = new Random();
      int n = rand.nextInt(nodes.size());
      int s_con = ((nodes.get(n)).connections).size();
      int con = rand.nextInt(s_con);

      Node.disableLink2(nodes.get(n), ((nodes.get(n).connections).get(con)));

      for (Node node : nodes) {
        Boolean del = true;
        if ((node).isAlive()) {
          for (int s = 0; s < ((node).connections).size(); s++) {
            if (((node).valid_connect).get(s) == true) {
              del = false;
            }
          }
        }
        if (del) {
          (node).disableNode();
        }

      }

    }
  }
}
