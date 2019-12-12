import javax.swing.Icon;
import javax.swing.ImageIcon;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.awt.event.ActionEvent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JButton;

public class GameBoard extends JFrame{
	
	private final Icon OIcon=new ImageIcon(getClass().getResource("O.png"));
	private final Icon XIcon=new ImageIcon(getClass().getResource("X.png"));
	
	//Two dim array of JButtons
	
	private JButton square[][]=new JButton[3][3];
	//public static Random randBol=new Random();
	public static boolean playerTurn=true; //false-agent turn, true-player turn
	enum status {X,O,EMPTY}; //status that some square can have --> X player O agent
	private status boardStat[][]=new status[3][3]; // save the current status of the board
	private boolean full = false;
	private boolean gameEnd = false;
	
	
	public GameBoard() {
		super("AI Tic Tac Toe");
		setLayout(new GridLayout(3,3,7,7)); //sets a grid layout that contain 3*3 places to items
		BoardHandler handler = new BoardHandler();
		// init the game board
		for(int i=0;i<3;i++)
			for(int j=0;j<3;j++) {
				square[i][j]=new JButton();
				add(square[i][j]);
				square[i][j].addActionListener(handler);
				boardStat[i][j]=status.EMPTY;
				
			}
	}//constructor
	
	
	private class BoardHandler implements ActionListener{
		
		@Override
		public void actionPerformed(ActionEvent e) {
			int clickpos[]=getClickPostion(e);
			if(boardStat[clickpos[0]][clickpos[1]]==status.X || boardStat[clickpos[0]][clickpos[1]]==status.O)
			{
				JOptionPane.showMessageDialog(null,"put your player on empty square");
			}
			else if(playerTurn) {
				int[] clickAgent =new int[2];
				//player turn
				square[clickpos[0]][clickpos[1]].setIcon(OIcon);
				boardStat[clickpos[0]][clickpos[1]] = status.O;
				//check game board
				checkGame();
			//agent turn
				clickAgent=agent();
				boardStat[clickAgent[0]][clickAgent[1]] = status.X;			
				square[clickAgent[0]][clickAgent[1]].setIcon(XIcon);
				checkGame();
			}
			
		}

		private int[] getClickPostion(ActionEvent e) { //when the user play this func get his clicked location
			JButton sourceButton = (JButton)e.getSource(); //sourceButton holds the button that clicked as event
			int[] clicked=new int[2];
			for(int i=0;i<3;i++)
				for(int j=0;j<3;j++) {
					if(sourceButton==square[i][j]) {
						clicked[0]=i;
						clicked[1]=j;
						break;
					}
				}
			return clicked;
		}
		
		private status checkBord() { //we check every col and row and diagonal if there is a winer
			
			for(int i=0 ; i<3 ; i++) {
			if(boardStat[0][i] == boardStat[1][i] && boardStat[1][i] == boardStat[2][i] &&  boardStat[2][i]!= status.EMPTY) 
				return boardStat[0][i];
			}
			
			for(int i=0 ; i<3 ; i++) {
				if(boardStat[i][0] == boardStat[i][1] && boardStat[i][1] == boardStat[i][2] &&boardStat[i][2] != status.EMPTY) 
					return boardStat[i][0];
				
			}
			if(boardStat[0][0] == boardStat[1][1] && boardStat[1][1] == boardStat[2][2] && boardStat[2][2] != status.EMPTY)
			{
				return boardStat[0][0];
			}
			if(boardStat[2][0] == boardStat[1][1] && boardStat[1][1] == boardStat[0][2] && boardStat[0][2] != status.EMPTY)
			{
				return boardStat[2][0];
			}

			return status.EMPTY;
		}
	
		private int[] agent() { //this func uses the board to check which place i need to put the next X with the G and H functions
			int[][] Harr = new int[3][3];
			int[] ret = new int[2];
			int Hmax=0, row = 0, column = 0, flag =0;
			for(int i=0;i<3;i++) {
				for(int j=0;j<3;j++)
				{
					if(boardStat[i][j]==status.EMPTY)  //if the square is empty we calculete: f = h + g
					{
						Harr[i][j] = g(i,j) + h(i,j); 
						if(Harr[i][j] > Hmax)
						{
							Hmax=Harr[i][j];
							row=i;
							column=j;
							flag=1;
						}
						if(flag ==0) 
						{
							row=i;
							column=j;
						}
					}
					
				}
					
			}
			ret[0] = row;
			ret[1] = column;
			return ret;

		}
		
		private int g(int row , int column)   // in this function we are give to all empty square the greedy value
		{
			int countO = 0;
			int Fval=0;
				
				for(int i=0;i<3;i++) { // check row
				if(boardStat[row][i] ==  status.X)
					countO--;
				if(boardStat[row][i] ==  status.O)
					countO++;
				}
				if (countO == 2)
				{
					Fval = 100;
					return Fval;
				}
				countO=0;
				for(int j=0;j<3;j++) { // check column 
					if(boardStat[j][column] ==  status.X)
						countO--;
					if(boardStat[j][column] ==  status.O)
						countO++;
				}
				if (countO == 2)
				{
					Fval = 100;
					return Fval;
				}
				countO=0;
				// check diagonals
				if (row == column)
				{
					for(int i=0 ; i<3 ; i++)
					{
						if(boardStat[i][i] ==  status.X)
							countO--;
						if(boardStat[i][i] ==  status.O)
							countO++;
					}
					if (countO == 2)
					{
						Fval = 100;
						return Fval;
					}
					countO=0;
				}
				
				
				// check second diagonal
				if (row + column == 2)
				{
					for(int i=0, j=2 ; i<3 ; i++ , j--)
					{
						if(boardStat[i][j] ==  status.X)
							countO--;
						if(boardStat[i][j] ==  status.O)
							countO++;
					}
					if (countO == 2)
					{
						Fval = 100;
						return Fval;
					}
				}
				
				
				return Fval;
		}
				
		private int h(int row , int column) // in this function we are give to all empty square the huristic value
		{
			int countX = 0;
			int countO= 0;
			int Hval = 0;
			for(int i=0;i<3;i++) { // check row
				
				if(boardStat[row][i] ==  status.X)
					countX++;
				if(boardStat[row][i] ==  status.O)
					countO++;
			}
			
			if(countO == 0)
			{
				if (countX == 2)
				{
					Hval+=1000;
					return Hval;
				}
				else {
					Hval++;
				}
			}
			countX = 0;
			countO= 0;
				
				for(int j=0;j<3;j++) { // check column 
					
					if(boardStat[j][column] ==  status.X)
						countX++;
					if(boardStat[j][column] ==  status.O)
						countO++;
				}
				
				if(countO == 0)
				{
					if (countX == 2)
					{
						Hval+=1000;
						return Hval;
					}
					else {
						Hval++;
					}
				}
				countX = 0;
				countO= 0;
				
				// check main diagonal
				if (row == column)
				{
					for(int i=0 ; i<3 ; i++)
					{
						if(boardStat[i][i] ==  status.X)
							countX++;
						if(boardStat[i][i] ==  status.O)
							countO++;
					}
					if(countO == 0)
					{
						if (countX == 2)
						{
							Hval+=1000;
							return Hval;
						}
						else {
							Hval++;
						}
					}
					countX = 0;
					countO= 0;
				}
				
				
				
				// check second diagonal
				if (row + column == 2)
				{
					for(int i=0, j=2 ; i<3 ; i++ , j--)
					{
						if(boardStat[i][j] ==  status.X)
							countX++;
						if(boardStat[i][j] ==  status.O)
							countO++;
					}
					if(countO == 0)
					{
						if (countX == 2)
						{
							Hval+=1000;
							return Hval;
						}
						else {
							Hval++;
						}
					}
				}
				
			return Hval;
		}
		
		private void checkGame() // check the board game if there is a winer\loser or it is tie
		{
			if(checkBord()==status.O)
			{
				JOptionPane.showMessageDialog(null,"You win");
				gameEnd = true;
				setVisible(false);
				System.exit(0);
			}
			if(checkBord()==status.X)
			{
				JOptionPane.showMessageDialog(null,"You Lose, The AI win");
				gameEnd = true;
				setVisible(false);
				System.exit(0);
			}
			full=true;
			for(int i=0;i<3;i++) {
				for(int j=0;j<3;j++)
				{
					if(boardStat[i][j]==status.EMPTY)
						full=false;
				}
			}
			if(full && !(gameEnd)) {
				JOptionPane.showMessageDialog(null,"Tie");
				setVisible(false);
				System.exit(0);
			}
			
		}
	}
	
	
}
	
	

