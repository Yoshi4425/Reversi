package reversi;

public class Game {       // ゲームのクラス

	private int mode = 0;	//0で通常、1で編集モード
	
	private Board board;	//現在の盤面データ
	private Board[] stock = new Board[3];	//盤面データのストック
	private int miniP = -1;	//ミニボードの参照位置 0~2がストック、ミニボードは-1
	private int mark = -1;	
	
	private int[][] setBoard = new int[8][8];	//編集モードで操作する盤面
	private int setTurn;
	
	private Paint paint;
	
	public Game(Paint paint) {	
		this.paint = paint; //ペイントクラスを設定
		board = new Board(paint); //
		drawAll();
	}

	// 開始時に呼び出されるメソッド
	public void start() {
	}

	public void setMode(int n) {
		if(mode == n) return;	//変更なしなら何もしない
		mode = n;	//モード変更
		if(mode == 0) {	//通常モードになったら
			board = new Board(paint,setBoard,setTurn);
		}
		if(mode == 1)	{	//編集モードになったら
			stock[0] = board;
			stock[1] = null;
			stock[2] = null;
			setTurn = board.editMode(setBoard);
			mark=-1;
		}
		drawAll();
	}
	
	public void clickBoard(int x, int y, int bt) {
		if(mode == 0)	{//通常モード
			if(x < 0) return;
			
			if(mark != -1) 
				paint.mark(mark,false);
			if(bt == 1)
				board.clickBoard(x, y);
			
		}else {	//編集モード
			if(bt == 1)
				bt = 1;
			else if(bt == 2)
				bt = 0;
			else if(bt == 3)
				bt = -1;
			else 
				return;
			
			if(x==-1) {
				setTurn = bt;
			}else {
				if(setBoard[y][x] == bt)
					setBoard[y][x] = 0;
				else 
					setBoard[y][x] = bt;
			}		
			paint.draw(setBoard,setTurn,-1,-1);
		}
		if(board.checkMark(mark) && mark!=-1)
			paint.mark(mark,true);
	}
	
	public void clickPanel(int n, int bt) {
		if(mode == 0) {
			if(mark != -1) 
				paint.mark(mark,false);
			
			if(bt == 1) {
				if(miniP == -1)
					board.resetFrame();
				else
					paint.miniFrame(miniP+61, false);
				
				if(n <= 60) {	//プレイ履歴なら
					miniP = -1;
					board.clickPanel(n);
				}else if(n == miniP+61) {	//ストック部分なら ポインタと同じなら
					if(stock[miniP] != null) {
						board = stock[miniP].clone();
						board.drawBoard();
						board.drawMini();
						miniP = -1;
						mark = -1;
					}
				}
				else {
					miniP = n-61;
					if(stock[miniP] != null)
						stock[miniP].drawBoard();
					paint.miniFrame(miniP+61, true);
				}
			}
			if(bt == 3) {	
				if(n <= 60) {			
					if(board.checkMark(n))
						mark = n;
				}else {
					stock[n-61] = board.clone();
					stock[n-61].drawStock(n-61);
				}
			}
		}
		if(board.checkMark(mark) && mark!=-1)
			paint.mark(mark,true);
	}
	
	public void turn() {
		if(mode == 0) {
			board.drawBoard();
			board.turn();
		}
	}

	public void restrict() {
		if(mode == 0) {
			board.drawBoard();
			board.restrict();
		}
	}
	
	public void wheel(int dir) {
		if(mode == 0) {
			if(miniP == -1)
				board.resetFrame();
			else
				paint.miniFrame(miniP+61, false);
			
			board.wheel(dir);
		}else {
			setTurn = dir;
			paint.draw(setBoard,setTurn,-1,-1);
		}
	}
	
	public void drawAll() {
		if(mode == 0) {
			paint.normalMode();
			board.drawBoard();
			board.drawMini();
			for(int i=0; i<3; i++) {
				if(stock[i] != null)
					stock[i].drawStock(i);
			}
			if(mark != -1)
				paint.mark(mark, true);
		}
		if(mode == 1) {
			paint.editMode();
			paint.draw(setBoard,setTurn,-1,-1);
		}
	}

}