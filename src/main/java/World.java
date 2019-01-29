import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.AbstractAction;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

public class World extends JPanel {
  public final static int W_GRIDCOUNT = Application.WIDTH / (2 * Node.WIDTH) - 1;
  public final static int H_GRIDCOUNT = Application.HEIGHT / (2 * Node.WIDTH) - 1;
  public static final int SPARSENESS = 0;
  public static final int NODE_SPARESNESS = 0;
  public static final int LABEL_SPACE = 100;
  public static double REWARD_TOKENS = 500;

  public static JLabel label;

  List<Node> nodes;
  static int placedNodes = 0;

  public World() {

    label = new JLabel();
    label.setText("Reward tokens = " + REWARD_TOKENS);
    label.setForeground(Color.WHITE);

    add(label);

    repaint();

    nodes = new ArrayList<Node>();

    mapGeneration(nodes);

    // Pick random enemy pieces
    pickEnemyPiece();
    pickEnemyPiece();
    pickEnemyPiece();

    this.repaint();
    mouseListener();
  }

  private void mouseListener() {
    final World world = this;

    class myMouseListener implements MouseListener {

      public void mouseClicked(MouseEvent arg0) {
        if (placedNodes < 1) {
          implementPlacement(arg0, world);
        } else {
          implementMenu(arg0, world);
        }
      }

      public void mousePressed(MouseEvent e) {

      }

      public void mouseReleased(MouseEvent e) {

      }

      public void mouseEntered(MouseEvent e) {

      }

      public void mouseExited(MouseEvent e) {

      }
    }

    myMouseListener mml = new myMouseListener();

    world.addMouseListener(mml);
  }

  /* Code for popUpWindow */
  private void implementMenu(MouseEvent e, final World world) {
    for (final Node node : nodes) {
      if(node.isHit(e)) {
        JPopupMenu selectReward;
        selectReward = new JPopupMenu("Upgrades");
        selectReward.add(new JMenuItem(new AbstractAction("Bless the Gifted (50)") {
          public void actionPerformed(ActionEvent e) {
            if(node.attemptUpgrade("Charisma")) {
              JOptionPane.showMessageDialog(world, "Charisma Successful");
            } else {
              JOptionPane.showMessageDialog(world, "More tokens needed");
            }
          }
        }));
        selectReward.add(new JMenuItem(new AbstractAction("Punish the unfaithful (50)") {
          public void actionPerformed(ActionEvent e) {
            if(node.attemptUpgrade("Infidelity")) {
              JOptionPane.showMessageDialog(world, "Infidelity Successful");
            } else {
              JOptionPane.showMessageDialog(world, "More tokens needed");
            }
          }
        }));
        selectReward.add(new JMenuItem(new AbstractAction("Increase the Sacrifice (50)") {
          public void actionPerformed(ActionEvent e) {
            if(node.attemptUpgrade("Reward Rate")) {
              JOptionPane.showMessageDialog(world, "Rewards rate Successful");
            } else {
              JOptionPane.showMessageDialog(world, "More tokens needed");
            }
          }
        }));
        selectReward.add(new JMenuItem(new AbstractAction("Holy hand grenade (200)") {
          public void actionPerformed(ActionEvent e) {
            if(node.attemptUpgrade("bomb")) {
              JOptionPane.showMessageDialog(world, "Holy hand grenade Successful");
            } else {
              JOptionPane.showMessageDialog(world, "More tokens needed");
            }
          }
        }));
        selectReward.add(new JMenuItem(new AbstractAction("Matyr (-300)") {
          public void actionPerformed(ActionEvent e) {
            if(node.attemptUpgrade("matyr")) {
              JOptionPane.showMessageDialog(world, "Matyr Successful");
            } else {
              JOptionPane.showMessageDialog(world, "More tokens needed");
            }
          }
        }));
        selectReward.show(e.getComponent(), e.getX(), e.getY());
      }
    }
  }

  private void pickEnemyPiece() {
    boolean search = true;
    while (search) {
      Random random = new Random();
      int i = random.nextInt(nodes.size());
      if (nodes.get(i).getStatus() == Cult.NEUTRAL) {
        nodes.get(i).setNextStatus(Cult.RED);
        nodes.get(i).setStatus(Cult.RED);
        search = false;
      }
    }
  }

  private void implementPlacement(MouseEvent e, World world) {
    if (placedNodes < 1) {
      for (Node node : nodes) {
        if (node.isHit(e)) {
          if (node.getStatus() == Cult.NEUTRAL) {
            node.setStatus(Cult.BLUE);
            node.setNextStatus(Cult.BLUE);
            world.placed();
            world.repaint();
          }
          return;
        }
      }
    }
  }

  public void placed() {
    placedNodes++;
    if (placedNodes == 1) {
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
          if (node.getStatus() != Cult.NEUTRAL) {
            node.startConverting();
          }
        }

        for (Node node : nodes) {
          node.setStatus(node.getNextStatus());
        }

        Random random = new Random();
        int n = random.nextInt(60);
        if (n < 12) {
          boolean search = true;

          Node node = null;

          while (search) {
            int i = random.nextInt(nodes.size());
            if (nodes.get(i).getStatus() == Cult.RED) {
              node = nodes.get(i);
              search = false;
            }
          }

          if (node.getStatus() == Cult.RED) {
            node.bomb();
          }
        }


        world.repaint();
      }
    }, 1000, 1000);
  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponents(g);

    g.setColor(Color.BLACK);
    g.fillRect(0, 0, Application.WIDTH, Application.HEIGHT + LABEL_SPACE);

    for (Node node : nodes) {
      node.paintConnection(g);
    }

    for (Node node : nodes) {
      node.paintNodes(g);
    }

    g.setColor(Color.WHITE);

    label.repaint();
  }

  //Creating all possible nodes and their connections
  private void mapGeneration(List<Node> nodes) {
    gridInit(nodes);

    createConnections(nodes);

    makeSpare(nodes);
  }

  private void createConnections(List<Node> nodes) {
    //createHorizontalConnections(nodes);
    createDiagonalConnections(nodes);
  }

  private void createDiagonalConnections(List<Node> nodes) {
    for (int i = 0; i < W_GRIDCOUNT * (H_GRIDCOUNT - 1) * 2 + W_GRIDCOUNT; i++) {
      if (i % (W_GRIDCOUNT * 2) < W_GRIDCOUNT) {
        Node.link(nodes.get(i), nodes.get(i + W_GRIDCOUNT));
      } else if ((i + 1) % W_GRIDCOUNT != 0){
        Node.link(nodes.get(i), nodes.get(i + W_GRIDCOUNT + 1));
      }
    }
  }

  private void createHorizontalConnections(List<Node> nodes) {
    for (int i = 1; i < nodes.size() - 1; i++) {
      if (i % W_GRIDCOUNT != 0) {
        Node.link(nodes.get(i), nodes.get(i - 1));
      }
    }
  }

  private void gridInit(List<Node> nodes) {
    for (int i = 0; i < H_GRIDCOUNT; i++) {
      for (int j = 0; j < W_GRIDCOUNT; j++) {
        float x = (j * 2) + 3/2;
        float y = (i * 2) + 1/2;
        nodes.add(new Node(Math.round(x * Node.WIDTH),Math.round(y * Node.WIDTH)));
      }

      for (int j = 0; j < W_GRIDCOUNT; j++) {
        float x = (j * 2) + 5/2;
        float y = (i * 2) + 3/2;
        nodes.add(new Node(Math.round(x * Node.WIDTH),Math.round(y * Node.WIDTH)));
      }
    }
  }

  private void makeSpare(List<Node> nodes) {
    for (int i = 0; i < NODE_SPARESNESS /*TEST VARIABLE*/; i++) {
      Random rand = new Random();
      int n = rand.nextInt(nodes.size());
      (nodes.get(n)).kill();
      nodes.remove(n);
    }

    for (int i = 0; i < SPARSENESS /*TEST VARIABLE*/; i++) {
      Random rand = new Random();
      int n = rand.nextInt(nodes.size());
      int s_con = ((nodes.get(n)).connections).size();
      if (s_con == 0) {
        break;
      }
      int con = rand.nextInt(s_con);

      Node.disableLink(nodes.get(n), ((nodes.get(n).connections).get(con)));
    }
  }

  public static void redrawLabel() {
    label.setText("Reward Tokens = " + Math.round(REWARD_TOKENS));
  }
}
