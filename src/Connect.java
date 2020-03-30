import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Connect {

	private Socket socket;
	public WhiteBoard board;
	public DataInputStream in = null;
	public DataOutputStream out = null;
	private String allow = "wait";
	public LoginClient client;
	private boolean kick = false;

	Connect(Socket socket) {
		try {
			this.socket = socket;
			// this.board = board;
			// System.out.println(board);

			out = new DataOutputStream(this.socket.getOutputStream());
			in = new DataInputStream(this.socket.getInputStream());
			// String q = string;

			// out.writeUTF(q);
			// String str1 = in.readUTF();
			// System.out.println(str1);
			// String inn = str1 + "\r\n";
//			board = whiteBoard;
//			System.out.println(inn);
//			board.textArea_1.setText(inn);
			// textArea_1.setText("gfyoueqbfhoueq");
//			System.out.println(textArea_1.getText());
			// textArea_1.append(inn);
		} catch (Exception e) {
//			System.out.println("exception in out");
		}

		// this.board = board;

//		System.out.println("in user whiteboard!1");

		// out.writeUTF(q);
		// System.out.println(in.readUTF());
//		String inn = Connect.in.readUTF() + "\r\n";
//		System.out.println(inn);
//		//textArea_1.setText(inn);
//		textArea_1.setText("gfyoueqbfhoueq");
//		System.out.println(textArea_1.getText());
		// textArea_1.append(inn);
		// JSONObject rspc = new JSONObject();
		// JSONObject rmsg;

	}

	String getAllow() {
		return allow;
	}
	
	public void resetAllow() {
		allow = "wait";
	}

	public void begin(JFrame frame) {

		try {
//			System.out.println("in connect");
			while (true) {
//				System.out.println("enter true");

				String s = in.readUTF();
				String[] ss = s.split(" ", 2);
				if (ss[0].equals("draw")) {
//					System.out.println(ss[1]);
					WhiteBoard.drawListener.update(ss[1]);
					WhiteBoard.canvas.repaint();
				}
				if (ss[0].equals("chat")) {
					LoginClient.whiteBoard.ChatWindow.append(ss[1] + "\r\n");
					LoginClient.whiteBoard.ChatWindow
							.setCaretPosition(LoginClient.whiteBoard.ChatWindow.getDocument().getLength());

				}
				if (ss[0].equals("userlist") && LoginClient.whiteBoard != null) {
//					System.out.println("in userlist");
					String[] uuser = ss[1].split(" ");
					LoginClient.whiteBoard.list.setListData(uuser);
				}
				if (ss[0].equals("delete")) {

					String[] uuser = ss[1].split(" ", 2);
					String[] clients = uuser[1].split(" ");
					JOptionPane.showMessageDialog(LoginClient.whiteBoard.frame, uuser[0] + " is kicked out!");
					LoginClient.whiteBoard.list.setListData(clients);

				}
				if (ss[0].equals("close")) {

				}
				if (ss[0].equals("kick"))
				{
					kick = true;
					JOptionPane.showMessageDialog(LoginClient.whiteBoard.frame, "You have been kicked out!!");
				}
				
				if (ss[0].equals("feedback")) {
//				     System.out.println("sonsoai aosdf a");
					if (ss[1].equals("no")) {
						// System.out.println("fuk3");
						allow = "no";
					} else if (ss[1].equals("yes")) {
						allow = "yes";
//						System.out.println("in userlistttt");
					} else if (ss[1].equals("rejected")) {
						allow = "rejected";
					}
				}
				if (ss[0].equals("clientout")) {
//					System.out.println("in clientout");
					String[] uuser = ss[1].split(" ", 2);

					JOptionPane.showMessageDialog(LoginClient.whiteBoard.frame, "user " + uuser[0] + " leaves!");
					String[] clients = uuser[1].split(" ");
					LoginClient.whiteBoard.list.setListData(clients);
				}
				if (ss[0].equals("new")) {
					WhiteBoard.canvas.removeAll();
					WhiteBoard.canvas.updateUI();
					WhiteBoard.drawListener.clearRecord();
				}

//				System.out.println("draw:");
				// board.textArea_1.append(s);
			}

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			try {
				if (!kick) {
					JOptionPane.showMessageDialog(LoginClient.whiteBoard.frame, "Server has been closed");
				}
			} catch (Exception e2) {
				System.out.println("frame close!");
			}

			System.out.println("No server now");
			System.exit(0);
//			e1.printStackTrace();
		}

//		System.out.println("in user whiteboard!2");
	}
}
