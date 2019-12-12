import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class TTTmain {

	public static void main(String[] args) {
		GameBoard board = new GameBoard();
		board.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		board.setSize(500, 500); // set frame size
		board.setVisible(true); // display frame
	}

}
