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

public class Player1 {
	private Socket socket;
	private ServerSocket serverSocket;
	private BufferedWriter writer;
	private BufferedReader reader;
	private int foePickedCardValue;
	public Player1(ServerSocket serverSocket) {
		// TODO Auto-generated constructor stub
		try {
			this.serverSocket = serverSocket;
			this.socket = serverSocket.accept();
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
			
			while (socket.isConnected()) {
				try {
					String msg = reader.readLine();
				    if (msg.charAt(0)=='T') 
				    	this.foePickedCardValue = Integer.parseInt(msg.substring(10));
				    else
				    	Player1Controller.addMessage(msg.substring(1), vbox,false);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
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
