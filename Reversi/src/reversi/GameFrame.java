package reversi;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

//ゲーム用フレーム（ウィンドウ）
public class GameFrame extends JFrame implements ActionListener, MouseListener, MouseWheelListener, WindowListener{
	
	private static final long serialVersionUID = 1L;
	static  String[] str = {"Turn Over", "Restrict", "Back"};  // ボタン用の文字列
	static  String[] mode = {"normal", "edit"};
	JButton[] bt = new JButton[str.length];
	JButton b = new JButton("Exit");       // ボタン生成;
    JComboBox<String> cb = new JComboBox<String>();    // コンボボックス生成
	
	Game game;             // ゲーム
	
	// コンストラクタ、第１引数：タイトル、第２引数：ペイントツールの参照
	public GameFrame(String title) {
		super(title);                    // スーパクラスのコンストラクタ呼び出し
		setSize(1220, 690);          
		setBackground(Color.white);      // 背景色を白とする
		Container cp = getContentPane();                // コンテナ設定
		cp.setBackground(getBackground());              // コンテナの背景設定
		cp.setLayout(new FlowLayout(FlowLayout.LEFT));  // 左側から並べるレイアウト
	
		JPanel pn = new JPanel();                       // パネル生成
		pn.setLayout(new FlowLayout(FlowLayout.LEFT));  // 左から並べるレイアウト
		
	    JLabel lb = new JLabel("Mode");  
	    lb.setForeground(Color.black);        // ラベルの文字色を黒とする
	    pn.add(lb);                          // パネルにラベルを付加
	    
	    cb.setEditable(false);               // コンボボックスの編集は不可とする
	    for (int i=0; i<mode.length; i++) {   
			cb.addItem(mode[i]);               
	    }
	    cb.addActionListener(this);  // コンボボックスにリスナ(アクション監視)付加
	    pn.add(cb);                 // パネルにコンボボックスを付加
		cp.add(pn);                          // コンテナにパネル追加
	    
		// ボタン
		pn = new JPanel();                       // パネル生成
		pn.setLayout(new FlowLayout(FlowLayout.LEFT));  // 左から並べるレイアウト
		for (int i=0; i<str.length; i++) {   // ボタン数分繰り返し
			bt[i] = new JButton(str[i]);       // ボタン生成
			bt[i].addActionListener(this);     // ボタンにリスナ（アクション監視)付加
			pn.add(bt[i]);                     // パネルにボタン追加
		}
		cp.add(pn);                          // コンテナにパネル追加
		
		pn = new JPanel();                       // パネル生成
		pn.setLayout(new FlowLayout(FlowLayout.LEFT));  // 左から並べるレイアウト
		b.addActionListener(this);     // ボタンにリスナ（アクション監視)付加
		pn.add(b);                     // パネルにボタン追加
		cp.add(pn);                          // コンテナにパネル追加
	
		setVisible(true);        // 見えるようにする
		game = new Game(new Paint(getGraphics()));  // ゲームのインスタンス生成
		addMouseListener(this);  // マウスリスナー（アクション監視）付加
		addMouseWheelListener(this);  // マウスホイールリスナー（アクション監視）付加
		addWindowListener(this);  // ウィンドウリスナー（アクション監視）付加
	}
	
	// アクションが発生した（メニューが選択された、ボタンが押された）場合の処理
	public void actionPerformed(ActionEvent evt) {
	
		if(evt.getSource() == cb) {
		    String str = (String)cb.getSelectedItem();  // 選択された項目の文字列を得る
		    int num = 0;
		    if(str == mode[0])
		    	num = 0;
		    else if(str == mode[1])
		    	num = 1;
		    game.setMode(num);
		}
		if (evt.getSource() == bt[0]) {   
			game.turn();
		}
		if (evt.getSource() == bt[1]) {   
			game.restrict();
		}
		if (evt.getSource() == bt[2]) {   
			game.wheel(1);
		}
		if (evt.getSource() == b) {   // Exitボタンが押された場合は
			dispose();       // フレームを消す
			System.exit(0);  // 終了
		}
	}
	
	public void mousePressed(MouseEvent evt) {
		int x = evt.getX();  // イベントが発生したところのＸ座標を記憶
		int y = evt.getY();  // イベントが発生したところのＹ座標を記憶
		
		int button = 0;	//クリックしたボタン　左クリック1、ホイール2、右クリック3
		if(evt.getButton()==MouseEvent.BUTTON1) {
			button = 1;
		}else if(evt.getButton()==MouseEvent.BUTTON2) {
			button = 2;
		}else if(evt.getButton()==MouseEvent.BUTTON3) {
			button = 3;
		}
		
		if (x>50 && x<450 && y>150 && y<550) {	//盤面をクリック
			int xPosition = (x - 50) / 50;
			int yPosition = (y - 150) / 50;
			game.clickBoard(xPosition, yPosition, button);	// ゲームのメソッド mousePressed() 呼び出し
		}
		if(x>215 && x<285 && y>565 && y<635) {	//ターン表示をクリック
			game.clickBoard(-1, -1, button);
		}
		if(x>550 && x<1190 && y>40 && y<680) {	//ミニボードパネルをクリック	
			game.clickPanel(((y-40)/80)*8 + ((x-550)/80)%8, button);
		}
		if(x<500 && button == 3) {	//画面左側を右クリック
			game.restrict();
		}
	}
	
	public void mouseWheelMoved(MouseWheelEvent evt) {
		game.wheel(evt.getWheelRotation());
	}
	
	public void windowActivated(WindowEvent arg0) {
		repaint();
	}
	
	public void windowClosing(WindowEvent arg0) {			//ウィンドウを閉じるとき終了
		System.exit(0);		
	}
	
	
	public void mouseReleased(MouseEvent evt) {
	}
	public void mouseClicked(MouseEvent evt) {
	}
	public void mouseEntered(MouseEvent evt) {
	}
	public void mouseExited(MouseEvent evt) {
	}
	public void windowDeactivated(WindowEvent arg0) {	
	}
	public void windowClosed(WindowEvent arg0) {		
	}
	public void windowDeiconified(WindowEvent arg0) {	
	}
	public void windowIconified(WindowEvent arg0) {	
	}
	public void windowOpened(WindowEvent arg0) {
	}
	
	//フレームを復元（再描画）する処理
	public void paint(Graphics gra) {
		super.paint(gra);    // スーパークラス（JFrame）の paint() 呼び出し
		if (game != null) {   // ゲームがあれば
			game.drawAll();     // ゲームの表示
		}
	}
	
	// メインプログラム
	public static void main(String[] args) {
		new GameFrame("Game");  // ゲーム用フレームのインスタンス生成
	}
}
