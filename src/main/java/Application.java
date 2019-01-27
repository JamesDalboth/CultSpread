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

}
