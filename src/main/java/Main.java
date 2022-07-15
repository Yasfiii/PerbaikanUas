import frame.gitarViewFrame;
import helpers.Koneksi;

public class Main {
    public static void main(String[] args) {
        Koneksi.getConnection();
        gitarViewFrame viewFrame = new gitarViewFrame();
        viewFrame.setVisible(true);
    }
}
