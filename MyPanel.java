import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

import javax.swing.JPanel;

public class MyPanel extends JPanel{
	private ArrayList<String> recordList = new ArrayList<String>();
	
	public void setList(ArrayList<String> recordList)
	{
		this.recordList = recordList;
	}

	public void paint(Graphics gr) {
		super.paint(gr);//调用父类的paint（）
		// 循环遍历shapeArray数组
		Graphics2D g = (Graphics2D) gr;
		for (String line: recordList) {
			String[] record = line.split(" ");
			int x1, y1, x2, y2;
			int i;
			Color color, curColor;
			if (record[1].equals("!")) {
				continue;
			}
			switch (record[0]) {
			case "Color":
				int red = Integer.parseInt(record[1]);
				int green = Integer.parseInt(record[2]);
				int blue = Integer.parseInt(record[3]);
				color = new Color(red, green, blue);
				g.setColor(color);
				break;
			case "Line":
				x1 = Integer.parseInt(record[1]);
				y1 = Integer.parseInt(record[2]);
				x2 = Integer.parseInt(record[3]);
				y2 = Integer.parseInt(record[4]);
				g.setStroke(new BasicStroke(1));
				g.drawLine(x1, y1, x2, y2);
				break;
			case "Oval":
				x1 = Integer.parseInt(record[1]);
				y1 = Integer.parseInt(record[2]);
				x2 = Integer.parseInt(record[3]);
				y2 = Integer.parseInt(record[4]);
				g.setStroke(new BasicStroke(1));
				g.drawOval(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x1 - x2), Math.abs(y1 - y2));
				break;
			case "Circle":
				x1 = Integer.parseInt(record[1]);
				y1 = Integer.parseInt(record[2]);
				x2 = Integer.parseInt(record[3]);
				y2 = Integer.parseInt(record[4]);
				g.setStroke(new BasicStroke(1));
				int diameter = Math.min(Math.abs(x1 - x2), Math.abs(y1 - y2));
				g.drawOval(Math.min(x1, x2), Math.min(y1, y2), diameter, diameter);
				break;
			case "Rect":
				x1 = Integer.parseInt(record[1]);
				y1 = Integer.parseInt(record[2]);
				x2 = Integer.parseInt(record[3]);
				y2 = Integer.parseInt(record[4]);
				g.setStroke(new BasicStroke(1));
				g.drawRect(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x1 - x2), Math.abs(y1 - y2));
				break;
			case "Free":
				x1 = Integer.parseInt(record[1]);
				y1 = Integer.parseInt(record[2]);
				g.setStroke(new BasicStroke(1));
				i = 3;
				while (!record[i].equals("!")) {
					int dx = Integer.parseInt(record[i]);
					int dy = Integer.parseInt(record[i + 1]);
					x2 = x1 + dx;
					y2 = y1 + dy;
					g.drawLine(x1, y1, x2, y2);
					i += 2;
					x1 = x2;
					y1 = y2;
				}
				break;
			case "E1":
				x1 = Integer.parseInt(record[1]);
				y1 = Integer.parseInt(record[2]);
				curColor = g.getColor();
				g.setColor(Color.white);
				g.setStroke(new BasicStroke(10));
				i = 3;
				while (!record[i].equals("!")) {
					int dx = Integer.parseInt(record[i]);
					int dy = Integer.parseInt(record[i + 1]);
					x2 = x1 + dx;
					y2 = y1 + dy;
					g.drawLine(x1, y1, x2, y2);
					i += 2;
					x1 = x2;
					y1 = y2;
				}
				g.setColor(curColor);
				break;
			case "E2":
				x1 = Integer.parseInt(record[1]);
				y1 = Integer.parseInt(record[2]);
				curColor = g.getColor();
				g.setColor(Color.white);
				g.setStroke(new BasicStroke(15));
				i = 3;
				while (!record[i].equals("!")) {
					int dx = Integer.parseInt(record[i]);
					int dy = Integer.parseInt(record[i + 1]);
					x2 = x1 + dx;
					y2 = y1 + dy;
					g.drawLine(x1, y1, x2, y2);
					i += 2;
					x1 = x2;
					y1 = y2;
				}
				g.setColor(curColor);
				break;
			case "E3":
				x1 = Integer.parseInt(record[1]);
				y1 = Integer.parseInt(record[2]);
				curColor = g.getColor();
				g.setColor(Color.white);
				g.setStroke(new BasicStroke(20));
				i = 3;
				while (!record[i].equals("!")) {
					int dx = Integer.parseInt(record[i]);
					int dy = Integer.parseInt(record[i + 1]);
					x2 = x1 + dx;
					y2 = y1 + dy;
					g.drawLine(x1, y1, x2, y2);
					i += 2;
					x1 = x2;
					y1 = y2;
				}
				g.setColor(curColor);
				break;
			default:
				break;

			}
		}
	}
}
