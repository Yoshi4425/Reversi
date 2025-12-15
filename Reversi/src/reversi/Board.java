package reversi;

public class Board implements Cloneable{
	private int[/*y*/][/*x*/] board = new int[8][8];	//盤面 1で黒 -1で白 0でなにもない 2が置くことが可能な位置
	private int turn = 1;	//次に操作するプレイヤー
	private int[][] initialBoard = new int[8][8];	//初期の盤面
	private int initialTurn;	//初期のターン
	private Vector[] opeArray = new Vector[60];	//操作の記録 0~3はなし
	private int nowOpe = -1;
	private int maxOpe = -1;
	private Paint paint;
	
	public Board(Paint paint) {
		for(int i=0; i<8; i++) {
			for(int j=0; j<8; j++) {
				initialBoard[i][j] = 0;
			}
		}
		initialBoard[3][3] = -1;
		initialBoard[3][4] = 1;
		initialBoard[4][3] = 1;
		initialBoard[4][4] = -1;
		initialTurn = 1;
		
		initialBoard();
		this.paint = paint;
	}
	
	public Board(Paint paint, int[][] iniB, int iniT) {
		for(int i=0; i<8; i++) {
			for(int j=0; j<8; j++) {
				initialBoard[i][j] = iniB[i][j];
			}
		}
		initialTurn = iniT;
		initialBoard();
		this.paint = paint;
		drawBoard();
		paint.miniBoard(board,nowOpe+1);
	}
	
	private void initialBoard() {	//盤面の初期化
		for(int i=0; i<8; i++) {
			for(int j=0; j<8; j++) {
				board[i][j] = initialBoard[i][j];
			}
		}
		turn = initialTurn;
		checkTurn();
	}
	
	private boolean checkPoint(int x, int y) {	//ある位置に石をおけるかの確認
		if(board[y][x] == 0 || board[y][x] == 2){	//石がなければ
			for(int i=0; i<8; i++){	//全てのベクトルをチェック
				if(checkVector(x,y,i)) return true;	
			}
		}
		return false;
	}
	
	private boolean checkVector(int x, int y, int n) {	//あるベクトルに対してひっくり返る石があるか確認
		x += Vector.vec[n].x;
		y += Vector.vec[n].y;

		if(onBoard(x,y) && board[y][x] == -turn){	//直近の石が相手
			do{
				x += Vector.vec[n].x;	//ベクトル方向に進み
				y += Vector.vec[n].y;
				if(!(onBoard(x,y))) break;	//インデックスチェック
				if(board[y][x] == turn) return true;	//自分の石が来れば終了
			}while(board[y][x] == -turn);	//相手の石である限り繰り返す	
		}
		return false;
	}
	
	private boolean onBoard(int x, int y){	//インデックス内か確認
		return (x>=0 && x<8 && y>=0  && y<8);
	}

	private void changeBoard(int x, int y){	//盤面の変更
		int i,dx,dy;

		for(i=0; i<8; i++){	//全ベクトルについて
			if(checkVector(x,y,i)){	//置けるなら
				dx = x;
				dy = y;
				do{	//位置を変更しながら
					board[dy][dx] = turn;
					dx += Vector.vec[i].x;
					dy += Vector.vec[i].y;
				}while(board[dy][dx] == -turn);	//相手の石であれば繰り返し
			}
		}
	}
	
	public void drawBoard() {
		paint.draw(board, turn, count(1), count(-1));
	}
	
	public void drawMini() {
		paint.resetMini();
		int i=0;
		initialBoard();
		paint.miniBoard(board, i);
		while(i <= maxOpe) {
			changeBoard(opeArray[i].x, opeArray[i].y);
			turn *= -1;
			if(!(checkTurn())) {
				turn *= -1;
				checkTurn();
			}
			i++;
			paint.miniBoard(board, i, opeArray[i-1]);
		}
		paint.miniFrame(nowOpe+1, true);
	}
	
	public int editMode(int[][] setBoard) {	//現時点での盤面をコピーし、ターンを返す
		for(int i=0; i<8; i++) {
			for(int j=0; j<8; j++) {
				setBoard[i][j] = board[i][j];
				if(setBoard[i][j] == 2)
					setBoard[i][j] = 0;
			}
		}
		return turn;
	}
	
	
	//--------------ゲーム進行用メソット------------------------------------------------------------------
	public void clickBoard(int x, int y) {	//盤面をクリックされた時、座標に応じて確認、変更
		if(board[y][x] == 2){	//置けるなら
			
			if(maxOpe > nowOpe-1) {
				maxOpe = nowOpe;
				drawMini();
			}
			paint.miniFrame(nowOpe+1, false);
			nowOpe++;
			opeArray[nowOpe] = new Vector(x,y);
			maxOpe = nowOpe;		
			changeBoard(x,y);	//変更
			paint.miniBoard(board,nowOpe+1, opeArray[nowOpe]);
			paint.miniFrame(nowOpe+1, true);
			turn *= -1;	//ターン交代
			if(!(checkTurn())) {	//ターン確認　置けなければ再度交代
				turn *= -1;
				checkTurn();
			}
		}
		paint.draw(board, turn, count(1), count(-1));
	}
	
	private boolean checkTurn(){	//全ての位置について石を置けるか確認、一つでも置ければtrue
		boolean tf = false;
		for(int y=0; y<8; y++){
			for(int x=0; x<8; x++){
				if(checkPoint(x,y)) {	//石が置けるか確認
					board[y][x] = 2;
					tf = true;
				}else {
					if(board[y][x] == 2) 
						board[y][x] = 0;
				}
							
			}
		}
		return tf;
	}
	
	
	//--------------ミニボード操作------------------------------------------------------------------
	public void clickPanel(int n) {		//クリックしたパネルに対応した操作
		if(maxOpe < n-1) return;
		nowOpe = n-1;
		paint.miniFrame(nowOpe+1, true);
		int i=0;
		initialBoard();
		while(i <= nowOpe) {
			changeBoard(opeArray[i].x, opeArray[i].y);
			turn *= -1;
			if(!(checkTurn())) {
				turn *= -1;
				checkTurn();
			}
			i++;
		}
		drawBoard();
	}
	
	public void drawStock(int n) {	//ストックを描画 引数はストックのインデックス
		paint.miniBoard(board, n+61);
	}
	
	public void resetFrame() {	//フレームを非表示
		paint.miniFrame(nowOpe+1, false);
	}
	
	public boolean checkMark(int n) {	//マークを表示できるか判定
		return n-1 <= maxOpe;
	}
	
	public Board clone(){	//インスタンスをコピー
    	try{
    		Board cp=(Board)super.clone();
    		cp.board = new int[8][8];
    		cp.initialBoard = new int[8][8];
    		cp.opeArray = new Vector[60];
    		for(int i=0; i<8; i++) {
    			for(int j=0; j<8; j++) {
    				cp.board[i][j] = board[i][j];
    				cp.initialBoard[i][j] = initialBoard[i][j];
    			}
    		}
    		for(int i=0; i<60; i++) {
    			cp.opeArray[i] = opeArray[i];
    		}
    		return cp;
    	}catch(CloneNotSupportedException ex){
    		return null;
    	}
    }
	
	//--------------コマンド---------------------------------------------------------------------------
	private int[][] nowBoard() {	//現在の盤面をコピー
		int[][] now = new int[board.length][board[0].length];
		for(int i=0; i<8; i++){
			for(int j=0; j<8; j++){
				now[i][j] = board[i][j];
			}
		}
		return now;
	}

	private void back(int[][] now) {	//現在の盤面を復帰
		for(int i=0; i<8; i++){
			for(int j=0; j<8; j++){
				board[i][j] = now[i][j];
			}
		}
	}
	
	public void wheel(int dir) {	//ホイール操作
		if(dir == -1)
			clickPanel(nowOpe+2);
		else if(dir == 1) {
			if(nowOpe != -1) 
				clickPanel(nowOpe);
		}
		paint.miniFrame(nowOpe+1, true);
	}
	
	private int count(int n) {	//盤面のnの数を返す
		int num = 0;
		for(int i=0; i<8; i++){
			for(int j=0; j<8; j++){
				if(board[i][j] == n)
					num++;
			}
		}
		return num;
	}
	
	public void turn(){
		int max = 0;
		int min = 64;
		int num;
		int nowNum = count(turn) + 1;
		int[] array = new int[count(2)];
		int index = 0;
		int[][] now = nowBoard();

		for(int i=0; i<8; i++){
			for(int j=0; j<8; j++){
				if(board[i][j] == 2){
					changeBoard(j,i);
					num = count(turn);
					back(now);
					
					array[index] = num;
					index++;
					
					if(num > max)
							max = num;
					if(num < min)
							min = num;
				}
			}
		}
		
		index = 0;
		for(int i=0; i<8; i++){
			for(int j=0; j<8; j++){
				if(board[i][j] == 2){
					if(max!=min) {
						if(array[index] == max)
							paint.piece(j,i,3);
						if(array[index] == min)
							paint.piece(j,i,4);
					}
					paint.counter(j, i, array[index]-nowNum);
					index++;
				}	
			}
		}
	}

	public void restrict(){	//相手がおける数が最小の位置を表示
		int minCount = 64;
		int[][] now = nowBoard();
		int[] minArray = new int[count(2)];
		int index = 0;
		
		for(int i=0; i<8; i++){
			for(int j=0; j<8; j++){
				if(board[i][j] == 2){
					changeBoard(j,i);
					turn = -turn;
					checkTurn();
					int count=count(2);
					minArray[index] = count;
					index++;
					
					back(now);
					turn = -turn;
					if(minCount > count)
						minCount = count;
				}
			}
		}
		index = 0;
		for(int i=0; i<8; i++){
			for(int j=0; j<8; j++){
				if(board[i][j] == 2){
					if(minArray[index] == minCount)
						paint.piece(j,i,5);
					
					paint.counter(j, i, minArray[index]);
					index++;
				}	
			}
		}
	}
	
}
