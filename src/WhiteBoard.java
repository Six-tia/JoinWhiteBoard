import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import java.awt.AWTException;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.awt.event.ActionEvent;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Robot;

import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.border.LineBorder;

import org.json.JSONObject;

import javax.swing.JComboBox;
import java.awt.SystemColor;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.ImageIcon;

public class WhiteBoard {

	static DrawListener drawListener;
	public JFrame frame;

	public DefaultListModel<String> dlm;
	public JList list;
	public JTextArea ChatWindow;

	
	int x;
	int y;
	int width;
	int height;
	private WhiteBoard whiteBoard;
	static MyPanel canvas;

	Socket socket;
	private String ipp;
	private int portt;
	private String mName;
	public static int curX, curY;

	public Connect connect;

	// Create the application.
	public WhiteBoard(Connect connect) {

		// this.socket= socket;
		this.connect = connect;
//		System.out.println(connect);
//		ipp = ip;
//		portt = port;
//		unamee = uname;
//		this.connect = connect;
//		try {
//			this.socket = new Socket(ipp, portt);
//			socket = new Socket("10.13.191.123", 3006);
//			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
//			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));
//			//dicUI window = new dicUI(socket);
//			//window.initialize();
		initialize();

//			
//		}catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

	}

	public WhiteBoard(Connect connect, String username) {
		// TODO Auto-generated constructor stub
		this.connect = connect;
//		System.out.println(connect);
		this.mName = username;
		initialize();
		
	}

	// Initialize the contents of the frame.
	private void initialize() {

		frame = new JFrame();
		frame.setTitle(this.mName + " Whiteboard (Client)");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBounds(0, 0, 1205, 840);
		// tool Panel
		drawListener = new DrawListener(frame);
		JPanel toolPanel = new JPanel();
		toolPanel.setBackground(Color.lightGray);
		toolPanel.setBounds(0, 0, 1500, 40);
		toolPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
//		System.out.println("1");

		ImageIcon line = new ImageIcon(WhiteBoard.class.getResource("/Icon/line.png"));
		ImageIcon circle = new ImageIcon(WhiteBoard.class.getResource("/Icon/circle.png"));
		ImageIcon rect = new ImageIcon(WhiteBoard.class.getResource("/Icon/rect.png"));
		ImageIcon oval = new ImageIcon(WhiteBoard.class.getResource("/Icon/oval.png"));
		ImageIcon free = new ImageIcon(WhiteBoard.class.getResource("/Icon/free1.png"));
		ImageIcon eraser = new ImageIcon(WhiteBoard.class.getResource("/Icon/eraser.png"));
		ImageIcon more = new ImageIcon(WhiteBoard.class.getResource("/Icon/color.png"));
		ImageIcon shotScreen = new ImageIcon(WhiteBoard.class.getResource("/Icon/screenshot.png"));
		
		ImageIcon[] icons = { line, circle, rect, oval, free, eraser, shotScreen };

		// Tools Button
		String[] tools = { "Line", "Circle", "Rect", "Oval", "Free", "Eraser", "ShotScreen" };
		for (int i = 0; i < tools.length; i++) {
			// JButton button1 = new JButton(tools[i]);
			JButton button1 = new JButton("");
			button1.setActionCommand(tools[i]);
			// button1.setPreferredSize(new Dimension(75, 25));
			button1.setPreferredSize(new Dimension(60, 30));
			Image temp = icons[i].getImage().getScaledInstance(21, 21, icons[i].getImage().SCALE_DEFAULT);
			icons[i] = new ImageIcon(temp);
			button1.setIcon(icons[i]);
			button1.addActionListener(drawListener);
			toolPanel.add(button1);
		}

		// Text
		JButton text = new JButton("T");
		text.setFont(new Font(null, 0, 20));
		text.setPreferredSize(new Dimension(50, 30));
		text.addActionListener(drawListener);
		if (text != null) {
			toolPanel.add(text);
		}
		
		// thickness menu
		JLabel labelT = new JLabel("Size: ");
		toolPanel.add(labelT);

		int lenThickMenu = 30;
		String[] number = new String[lenThickMenu];
		for (int i = 0; i < lenThickMenu; i++) {
			number[i] = ((Integer) (i + 1)).toString();
		}
		JComboBox thickMenu = new JComboBox();
		thickMenu.setModel(new DefaultComboBoxModel(number));
		toolPanel.add(thickMenu);
		thickMenu.addActionListener(new ActionListener() {

			// check the commnd from coboBox value
			public void actionPerformed(ActionEvent e) {
				int preThick = drawListener.getThickness();
				int curThick = Integer.parseInt(thickMenu.getSelectedItem().toString());
				if (preThick != curThick) {
					drawListener.setThickness(curThick);
				} else {
//					System.out.println(preThick);
				}
			}
		});

		// Color Button
		Color[] color_array = { Color.WHITE, Color.GRAY, Color.BLACK, Color.RED, Color.RED.darker(), Color.ORANGE,
				Color.ORANGE.darker(), Color.YELLOW, Color.GREEN, Color.BLUE, Color.BLUE.darker(), Color.CYAN,
				Color.CYAN.darker(), Color.PINK, Color.PINK.darker(), Color.MAGENTA, Color.MAGENTA.darker() };
		for (int i = 0; i < color_array.length; i++) {
			JButton button2 = new JButton();
			button2.setBackground(color_array[i]);
			button2.setOpaque(true);
			button2.setBorderPainted(false);
			button2.setPreferredSize(new Dimension(20, 20));
			toolPanel.add(button2);
			DrawListener drawListener = new DrawListener();
			button2.addActionListener(drawListener);
		}
		frame.getContentPane().setLayout(null);

		// add tool panel to window
		frame.getContentPane().add(toolPanel);

		// canvas
		canvas = new MyPanel();
		canvas.setBorder(null);
		canvas.setBounds(106, 40, 950, 740);
		width = canvas.getWidth();
		height = canvas.getHeight();
		canvas.setBackground(Color.WHITE);
		canvas.setList(drawListener.getRecord());
		frame.getContentPane().add(canvas);
		canvas.setLayout(null);
		frame.setVisible(true);
		frame.setResizable(false);
		frame.addComponentListener(new ComponentAdapter() {
			public void componentMoved(ComponentEvent e) {
				Component comp = e.getComponent();
				curX = comp.getX()+116;
				curY = comp.getY()+80;
			}
		});

		canvas.addMouseListener(drawListener);
		canvas.addMouseMotionListener(drawListener);

		// get canvas and put it into draw listener
		drawListener.setG(canvas.getGraphics());

		// Chat Window
		ChatWindow = new JTextArea();
		// ChatWindow.setBounds(1060, 80, 140, 500);
		ChatWindow.setBackground(SystemColor.text);
		frame.getContentPane().add(ChatWindow);

		JScrollPane scrollPaneC = new JScrollPane(ChatWindow);
		scrollPaneC.setBounds(1060, 80, 140, 500);
		scrollPaneC.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		frame.getContentPane().add(scrollPaneC);

		// Color panel
		JButton btnMore = new JButton("");
		btnMore.setActionCommand("More");
		btnMore.setPreferredSize(new Dimension(27, 27));
		Image temp = more.getImage().getScaledInstance(22, 22, more.getImage().SCALE_DEFAULT);
		more = new ImageIcon(temp);
		btnMore.setIcon(more);

		toolPanel.add(btnMore);
		btnMore.addActionListener(drawListener);

		
		JLabel lblNewLabel = new JLabel("Chat Window");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(1060, 40, 140, 40);
		frame.getContentPane().add(lblNewLabel);

		JLabel label = new JLabel("Input Text");
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setBounds(1060, 580, 140, 40);
		frame.getContentPane().add(label);

		// InputWindow
		JTextArea InputWindow = new JTextArea();
		// InputWindow.setBounds(1060, 620, 140, 130);
		frame.getContentPane().add(InputWindow);

		JScrollPane ScrollPaneI = new JScrollPane(InputWindow);
		ScrollPaneI.setBounds(1060, 620, 140, 130);
		ScrollPaneI.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		frame.getContentPane().add(ScrollPaneI);

		JButton btnNewButton = new JButton("Send");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//				System.out.println("button");
				String a = InputWindow.getText();
				InputWindow.setText("");
				try {
					if (!a.trim().equals("")) {
//						System.out.println(a);
						String chat = "chat " + a;
						connect.out.writeUTF(chat);
						connect.out.flush();
					} else {
						JOptionPane.showMessageDialog(frame, "sending content cannot be null!");
					}
				} catch (Exception e1) {
//					e1.printStackTrace();
				}

			}
		});
//		System.out.println("3");
		btnNewButton.setBounds(1060, 750, 140, 30);
		frame.getContentPane().add(btnNewButton);

		JLabel lblNewLabel_1 = new JLabel("Online");
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1.setBounds(5, 40, 98, 40);
		frame.getContentPane().add(lblNewLabel_1);

		// jlist
		list = new JList();
		// list.setBounds(2, 80, 100, 670);
		frame.getContentPane().add(list);

		JScrollPane ScrollList = new JScrollPane(list);
		ScrollList.setBounds(2, 80, 100, 670);
		ScrollList.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		frame.getContentPane().add(ScrollList);

		// connect = new connect(socket, whiteBoard);
		try {
			connect.out.writeUTF("begin 11");
			connect.out.flush();
		} catch (Exception e1) {

		}

	}

	void setFrame(WhiteBoard whiteBoard) {
		this.whiteBoard = whiteBoard;
		// TODO Auto-generated method stub
	}

	public void addlist(String uname) {
		dlm.addElement(uname);
	}

	public MyPanel getPanel() {
		return canvas;
	}
}
