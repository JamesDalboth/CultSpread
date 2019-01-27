package main.java;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Timer;
import java.util.TimerTask;

import main.java.Node;

import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
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

      boolean search = true;
      int i = 0;

      while (search) {
        Random random = new Random();
        i = random.nextInt(nodes.size());
        if(nodes.get(i).isAlive()) {
          search = false;
        }
      }

      nodes.get(i).setNextStatus(Color.BLUE);

      search = true;

      while (search) {
        Random random = new Random();
        i = random.nextInt(nodes.size());
        if(nodes.get(i).isAlive()) {
          search = false;
        }
      }

      nodes.get(i).setNextStatus(Color.RED);

      Timer time = new Timer();

      final World world = this;

        class myMouseListener implements MouseListener {

          @Override
          public void mouseClicked(MouseEvent arg0) {
            for (Node node : nodes) {
              if(node.distance(arg0.getX(), arg0.getY()) && node.isAlive()) {
                JPopupMenu selectReward;
                selectReward = new JPopupMenu("Upgrades");
                selectReward.add(new JMenuItem(new AbstractAction("Bless the Gifted") {
                  public void actionPerformed(ActionEvent e) {
                    if(node.attemptUpgrade("Charisma")) {
                      JOptionPane.showMessageDialog(world, "Charisma Successful");
                    } else {
                      JOptionPane.showMessageDialog(world, "More tokens needed");
                    }
                  }
                }));
                selectReward.add(new JMenuItem(new AbstractAction("Punish the unfaithful") {
                  public void actionPerformed(ActionEvent e) {
                    if(node.attemptUpgrade("Infidelity")) {
                      JOptionPane.showMessageDialog(world, "Infidelity Successful");
                    } else {
                      JOptionPane.showMessageDialog(world, "More tokens needed");
                    }
                  }
                }));
                selectReward.add(new JMenuItem(new AbstractAction("Increase the Sacrifice") {
                  public void actionPerformed(ActionEvent e) {
                    if(node.attemptUpgrade("Reward Rate")) {
                      JOptionPane.showMessageDialog(world, "Rewards rate Successful");
                    } else {
                      JOptionPane.showMessageDialog(world, "More tokens needed");
                    }
                  }
                }));
                selectReward.show(arg0.getComponent(), arg0.getX(), arg0.getY());
              }
            }
          }

          @Override
          public void mousePressed(MouseEvent e) {

          }

          @Override
          public void mouseReleased(MouseEvent e) {

          }

          @Override
          public void mouseEntered(MouseEvent e) {

          }

          @Override
          public void mouseExited(MouseEvent e) {

          }
        }

        myMouseListener mml = new myMouseListener();

        world.addMouseListener(mml);

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
  }

}
