import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.Timer;
import java.util.*;

public class World extends JPanel {
  private final static int W_GRIDCOUNT = Application.WIDTH / (2 * Node.WIDTH) - 1;
  private final static int H_GRIDCOUNT = Application.HEIGHT / (2 * Node.WIDTH) - 1;
  private static final int SPARSENESS = 200;
  private static final int NODE_SPARESNESS = 300;

  static final int LABEL_SPACE = 100;

  static int CONVERSION_TOKENS = 1;
  static double REWARD_TOKENS = 500;

  private static JLabel reward_label;
  private static JLabel conversion_label;

  private static boolean paused = true;

  private List<Node> nodes;

  public World() {
    setUpLabels();

    repaint();

    nodes = new ArrayList<Node>();

    mapGeneration(nodes);

    // Pick random enemy pieces
    pickEnemyPiece();
    pickEnemyPiece();
    pickEnemyPiece();

    this.repaint();

    ioSetUp();

    Run();
  }

  private void ioSetUp() {
    mouseListener();
    keyListener();
  }

  private void keyListener() {
    this.setFocusable(true);
    this.requestFocus();
    addKeyListener(new KeyListener() {
      @Override
      public void keyTyped(KeyEvent e) {

      }

      @Override
      public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
          paused = !paused;
        }
      }

      @Override
      public void keyReleased(KeyEvent e) {

      }
    });
  }

  /* Code for popUpWindow */
  private void implementMenu(MouseEvent e, final World world) {
    for (final Node node : nodes) {
      if (node.isHit(e)) {
        JPopupMenu selectReward;
        selectReward = new JPopupMenu("Upgrades");
        final String errorMessage = "More tokens needed";

        createPopUpOption(world, node, selectReward, "Bless the Gifted (50 points)", errorMessage
          , "Charisma Successful", Upgrade.CHARISMA);
        createPopUpOption(world, node, selectReward, "Punish the unfaithful (50 points)",
          errorMessage, "Infidelity Successful", Upgrade.INFIDELITY);
        createPopUpOption(world, node, selectReward, "Increase the Sacrifice (50 points)",
          errorMessage, "Rewards rate Successful", Upgrade.REWARDS);
        createPopUpOption(world, node, selectReward, "Holy hand grenade (200 points)",
          errorMessage, "Holy hand grenade Successful", Upgrade.BOMB);
        createPopUpOption(world, node, selectReward, "Martyr (-300 points)", errorMessage,
          "Martyr Successful", Upgrade.MATYR);
        createPopUpOption(world, node, selectReward, "Convert node (1 converion point)",
          errorMessage, "Convetion Successful", Upgrade.CONVERT);
        selectReward.show(e.getComponent(), e.getX(), e.getY());
      }
    }
  }

  private void createPopUpOption(final World world, final Node node, JPopupMenu selectReward,
                                 final String text, final String errorMessage,
                                 final String successMessage, final Upgrade type) {
    selectReward.add(new JMenuItem(new AbstractAction(text) {
      public void actionPerformed(ActionEvent e) {
        if (Upgrader.attemptUpgrade(node, type)) {
          JOptionPane.showMessageDialog(world, successMessage);
        } else {
          JOptionPane.showMessageDialog(world, errorMessage);
        }
      }
    }));
  }

  private void pickEnemyPiece() {
    Node node = getNodeOfType(Cult.NEUTRAL);
    node.setNextStatus(Cult.RED);
    node.setStatus(Cult.RED);
  }

  private void Run() {
    Timer time = new Timer();
    final World world = this;
    time.scheduleAtFixedRate(new TimerTask() {
      @Override
      public void run() {
        if (!paused) {
          updateNodes();

          enemyBomb();

          world.repaint();
        }
      }
    }, 1000, 1000);
  }

  private void enemyBomb() {
    Random random = new Random();

    int n = random.nextInt(60);
    if (n < 12) {

      Cult cult = Cult.RED;

      Node node = getNodeOfType(cult);

      if (node.getStatus() == Cult.RED) {
        node.bomb();
      }
    }
  }

  private Node getNodeOfType(Cult cult) {
    Random random = new Random();

    boolean search = true;

    while (search) {
      int i = random.nextInt(nodes.size());
      if (nodes.get(i).getStatus() == cult) {
        return nodes.get(i);
      }
    }

    return null;
  }

  private void updateNodes() {
    for (Node node : nodes) {
      if (node.getStatus() != Cult.NEUTRAL) {
        node.startConverting();
      }
    }

    for (Node node : nodes) {
      node.setStatus(node.getNextStatus());
    }
  }


  //Creating all possible nodes and their connections
  private void mapGeneration(List<Node> nodes) {
    gridInit(nodes);

    createConnections(nodes);

    makeSparse(nodes);
  }

  private void gridInit(List<Node> nodes) {
    for (int i = 0; i < H_GRIDCOUNT; i++) {
      for (int j = 0; j < W_GRIDCOUNT; j++) {
        float x = (j * 2) + 3 / 2;
        float y = (i * 2) + 1 / 2;
        nodes.add(new Node(Math.round(x * Node.WIDTH), Math.round(y * Node.WIDTH)));
      }

      for (int j = 0; j < W_GRIDCOUNT; j++) {
        float x = (j * 2) + 5 / 2;
        float y = (i * 2) + 3 / 2;
        nodes.add(new Node(Math.round(x * Node.WIDTH), Math.round(y * Node.WIDTH)));
      }
    }
  }

  private void createConnections(List<Node> nodes) {
    createHorizontalConnections(nodes);
    createDiagonalConnections(nodes);
  }

  private void createDiagonalConnections(List<Node> nodes) {
    for (int i = 0; i < W_GRIDCOUNT * (H_GRIDCOUNT - 1) * 2 + W_GRIDCOUNT; i++) {
      if (i % (W_GRIDCOUNT * 2) < W_GRIDCOUNT) {
        Node.link(nodes.get(i), nodes.get(i + W_GRIDCOUNT));
      } else if ((i + 1) % W_GRIDCOUNT != 0) {
        Node.link(nodes.get(i), nodes.get(i + W_GRIDCOUNT + 1));
      }
    }

    for (int i = 0; i < W_GRIDCOUNT * (H_GRIDCOUNT - 1) * 2 + W_GRIDCOUNT; i++) {
      if (i % (W_GRIDCOUNT * 2) < W_GRIDCOUNT) {
        if (i % W_GRIDCOUNT != 0) {
          Node.link(nodes.get(i), nodes.get(i + W_GRIDCOUNT - 1));
        }
      } else {
        Node.link(nodes.get(i), nodes.get(i + W_GRIDCOUNT));
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

  private void makeSparse(List<Node> nodes) {
    for (int i = 0; i < NODE_SPARESNESS; i++) {
      Random rand = new Random();
      int n = rand.nextInt(nodes.size());
      (nodes.get(n)).kill();
      nodes.remove(n);
    }

    for (int i = 0; i < SPARSENESS; i++) {
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

    if (paused) {
      g.setColor(Color.WHITE);

      g.drawString("PAUSED", Application.WIDTH / 2, 50);
    }

    reward_label.repaint();
  }

  public static void redrawLabel() {
    reward_label.setText("Reward Tokens = " + Math.round(REWARD_TOKENS));
    conversion_label.setText("Conversion tokens = " + CONVERSION_TOKENS);
  }

  private void setUpLabels() {
    reward_label = new JLabel();
    reward_label.setText("Reward tokens = " + REWARD_TOKENS);
    reward_label.setForeground(Color.WHITE);

    conversion_label = new JLabel();
    conversion_label.setText("Conversion tokens = " + CONVERSION_TOKENS);
    conversion_label.setForeground(Color.WHITE);

    add(reward_label);
    add(conversion_label);
  }

  private void mouseListener() {
    final World world = this;

    class myMouseListener implements MouseListener {

      public void mouseClicked(MouseEvent arg0) {
        implementMenu(arg0, world);
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
}
