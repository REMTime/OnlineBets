package networking;

import java.net.Socket;
import java.util.ArrayList;

import logic.Ticket;

public class ServerConnection extends Thread {

	private Socket socket;
	private Server server;

	boolean isActive = true;
	private InputHandler in;
	private OutputHandler out;

	ArrayList <Ticket> tickets;

	public ServerConnection(Socket socket, Server server){
		super("ServerConnectionThread");
		tickets = new ArrayList<Ticket>();
		this.socket = socket;
		this.server = server;
	}

	public void run(){
		in = new InputHandler(this,socket);
		out = new OutputHandler(this,socket);
		in.start();
		out.start();
	}

	public Server getServer(){
		return server;
	}

	public OutputHandler getOutputHandler(){
		return out;
	}
}
