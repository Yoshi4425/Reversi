package reversi;

import java.awt.Graphics;
import java.awt.Color; 
import java.awt.Font;

public class Paint {
	private Graphics gra;
	private final Font num = new Font("IPAGothic", Font.PLAIN, 50);
	private final Font edit = new Font("IPAGothic", Font.PLAIN, 35);
	private final Font count = new Font("IPAGothic", Font.PLAIN, 22);
	private final Color max = new Color(255,50,0);
	private final Color min = new Color(0,150,255);
	private final Color restrict = new Color(255,70,255);
	
	public Paint(Graphics g) {
		gra = g;
		gra.setFont(num);
		gra.drawString("null", 200, 200);
	}
	
	// ゲーム（盤と駒）を表示するメソッド
	public void draw(int[][] board, int turn, int black, int white) {
		boardDraw();	// 盤を表示する
		for (int i = 0; i < 8; i++) {  
			for (int j = 0; j < 8; j++) {
				piece(j,i, board[i][j]);
			}                    
		}
		if(turn == 1) gra.setColor(Color.black);
		else gra.setColor(Color.white);
		gra.fillOval(225, 575, 50, 50);
		
		gra.setColor(Color.black);
		gra.setFont(num);
		gra.drawString(String.format("%2d",black), 75, 620);
		gra.drawString(String.format("%2d",white), 375, 620);
	} 
	
	//各マスを表示
	public void piece(int x, int y, int c) {
		if(c == 1)	//黒
			gra.setColor(Color.black);
		else if(c == -1)	//白
			gra.setColor(Color.white);
		else if(c == 2)	//おける位置
		    gra.setColor(Color.yellow);
		else if(c == 3)	//max
			gra.setColor(max);
		else if(c == 4)	//min
			gra.setColor(min);
		else if(c == 5)	
			gra.setColor(restrict);
		if(!(c == 0))	//空欄でなければ表示
			gra.fillOval(55+50*x, 155+50*y, 40, 40);
	}
		  

	// 盤を表示するメソッド
	private void boardDraw() {
		gra.setColor(Color.black);
		gra.fillRect(47, 147, 406, 406);
		gra.setColor(Color.green);
		gra.fillRect(50, 150, 400, 400);
		gra.fillRect(215, 565, 70, 70);
		gra.setColor(Color.black);
		for (int i = 1; i < 8; i++) {
			gra.drawLine(50, 150+50*i, 450, 150+50*i);
			gra.drawLine(50+50*i, 150, 50+50*i, 550);
		}
		gra.setColor(Color.cyan);
		gra.fillRect(60, 570, 140, 60);
		gra.fillRect(300, 570, 140, 60);

		gra.setColor(Color.white);
		gra.fillOval(310, 580, 40, 40);
		gra.setColor(Color.black);
		gra.fillOval(150, 580, 40, 40);
		gra.drawOval(310, 580, 40, 40);
     
		gra.drawRect(60, 570, 140, 60);
		gra.drawLine(140, 570, 140, 630);
		gra.drawRect(300, 570, 140, 60);
		gra.drawLine(360, 570, 360, 630);
	
		gra.drawRect(215, 565, 70, 70);
		gra.drawLine(500, 0, 500, 1000);
	}

	//小さい盤面を表示
	public void miniBoard(int[][] board, int index) {
		int miniX = (index%8) *80 +550;
		int miniY = (index/8) *80 +40;
		
		gra.setColor(Color.black);
		gra.fillRect(miniX+6, miniY+6, 68, 68);
		gra.setColor(Color.green);
		gra.fillRect(miniX+8, miniY+8, 64, 64);
		gra.setColor(Color.black);
		for (int i=2; i<9; i++) {
			gra.drawLine(miniX+8, miniY+8*i, miniX+72, miniY+8*i);
			gra.drawLine(miniX+8*i, miniY+8, miniX+8*i, miniY+72);
		}
		for(int i=0; i<8; i++) {
			for(int j=0; j<8; j++) {
				if(board[i][j] == 1)	{//黒
					gra.setColor(Color.black);
					gra.fillOval(miniX+9+8*j, miniY+9+8*i, 6, 6);
				}else if(board[i][j] == -1)	{//白
					gra.setColor(Color.white);
					gra.fillOval(miniX+9+8*j, miniY+9+8*i, 6, 6);
				}
			}
		}
	}
	
	public void miniBoard(int[][] board, int index, Vector ope) {
		miniBoard(board,index);
		gra.setColor(Color.red);
		gra.drawOval((index%8) *80 +559 +8*ope.x, (index/8) *80 +49 +8*ope.y, 6, 6);
	}
	
	public void resetMini() {	//ミニボードをリセット
		gra.setColor(Color.white);
		gra.fillRect(501,0,1000,600);
		gra.fillRect(501,0,450,680);
		gra.setColor(Color.black);
		gra.drawLine(950, 600, 1190, 600);
		gra.drawLine(950, 680, 1190, 680);
		gra.drawLine(950, 600, 950, 680);
		gra.drawLine(1030, 600, 1030, 680);
		gra.drawLine(1110, 600, 1110, 680);
		gra.drawLine(1190, 600, 1190, 680);
	}
	
	public void miniFrame(int index, boolean draw) {	//選択位置の表示 drawで表示、非表示を管理
		int miniX = (index%8) *80 +550;
		int miniY = (index/8) *80 +40;
		
		if(draw)
			gra.setColor(Color.red);
		else
			gra.setColor(Color.white);
		
		gra.drawRect(miniX+2, miniY+2, 76,76);
	}
	
	public void mark(int index, boolean draw) {	//マークの表示 drawで表示、非表示を管理
		int miniX = (index%8) *80 +550;
		int miniY = (index/8) *80 +40;
		
		if(draw)
			gra.setColor(Color.blue);
		else
			gra.setColor(Color.white);
		
		gra.drawRect(miniX+4, miniY+4, 72,72);
	}
	
	public void counter(int x, int y, int num) {	//コマンドでカウントした数字を表示
		gra.setColor(Color.black);
		gra.setFont(count);
		gra.drawString(String.format("%2d",num), 64+50*x, 184+50*y);
	}
	
	public void normalMode() {	//通常モードの背景
		resetMini();
		gra.setColor(Color.white);
		gra.fillRect(0, 100, 500, 900);
	}
	
	public void editMode() {	//編集モードの背景
		gra.setColor(Color.cyan);
		gra.fillRect(0, 100, 500, 900);
		gra.setColor(Color.white);
		gra.fillRect(500, 0, 1000, 1000);
		gra.setFont(edit);
		gra.setColor(Color.black);
		gra.drawString("編集モードでは盤面、",570,150);
		gra.drawString("ターンを自由に設定できます",570,200);
		gra.drawString("左クリックで黒、",570,300);
		gra.drawString("右クリックで白に設定",570,350);
		gra.drawString("空白にしたい場合は、",570,450);
		gra.drawString("黒であれば左クリック、",570,500);
		gra.drawString("白であれば左クリックして下さい",570,550);
	}
}
