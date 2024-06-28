package game;



import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class Player {
	private Socket socket;
	private BufferedWriter writer;
	private BufferedReader reader;
	private int foePickedCardValue;
	public Player(Socket s) {
		// TODO Auto-generated constructor stub
		try {
			this.socket = s;
			this.writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		}catch (Exception e) {
			e.printStackTrace();
			onException();
		}
	}
	private void onException() {
		try {
			if (writer!=null)
				writer.close();
			if (reader!=null)
				reader.close();
			if (socket!=null)
				socket.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void listenForIncomingMessages(VBox vbox) {
		new Thread(()->{
			try {
				reader.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			while (socket.isConnected()) {
				try {
					
						String msg = reader.readLine();
					
				    if (msg.charAt(0)=='T') 
				    	this.foePickedCardValue = Integer.parseInt(msg.substring(1));
				    else
				    	PlayerController.addMessage(msg.substring(1), vbox,false);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					break;
				}
			}
		}).start();

	}

	public void sendMsg(String text,boolean containsPickedCardInfo) {
		text = containsPickedCardInfo? "T"+ text : "F"+text;
		try {
			writer.write(text);
			writer.newLine();
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
			onException();
		}

	}
	public int getFoePickedCardValue() {
		return this.foePickedCardValue;
	}
}
