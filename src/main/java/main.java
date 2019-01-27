import java.awt.EventQueue;
import javax.swing.JFrame;

public class main {

  public static void main(String[] args) {
    EventQueue.invokeLater(new Runnable() {
      public void run() {
        Application app = new Application();
      }
    });
  }


}
