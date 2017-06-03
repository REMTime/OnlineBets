package networking;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.Calendar;

import logic.Match;
import logic.Ticket;

public class InputHandler  extends Thread{

	private Socket socket;
	private ServerConnection serverConnection;
	private ObjectInputStream in;
	private boolean isActive = true;
	public InputHandler(ServerConnection serverConnection, Socket socket){
		this.serverConnection = serverConnection;
		this.socket = socket;
	}

	@Override
	public void run() {
		try {
			in = new ObjectInputStream(socket.getInputStream());
			while(isActive){
				while(in.available() == 0)
					sleep();
				int request = in.readInt();
				handleRequest(request);
			}	
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void handleRequest(int request) {                       
		switch(request){
		case 1:
			serverConnection.getOutputHandler().sendListToClient(); 
			break;
		case 2:														//primeste bilet de la client
			try {
				Ticket ticket = new Ticket();
				int count = in.readInt();
				for(int i = 0 ; i < count ; ++i){
					
					System.out.println("*");
					String teamA = (String) in.readObject();
					float stakeA = (Float)  in.readFloat();
					String teamB = (String) in.readObject();
					float stakeB = (Float)  in.readFloat();
					
					float stakeDraw = (Float) in.readFloat();
					
					int scoreA = in.readInt();
					int scoreB = in.readInt();
					Calendar startTime = (Calendar) in.readObject();
					String userTeam = (String)in.readObject();
					boolean isOver = in.readBoolean();
					
					ticket.addMatchToTicket(new Match(teamA, stakeA, teamB, stakeB, stakeDraw, scoreA, scoreB, startTime, userTeam, isOver));
					
				}
				float sum = (Float) in.readFloat();
				ticket.setSum(sum);
				int max1 = -1;
				for(Ticket t : serverConnection.getServer().getTickets()){
					if(max1 < t.getId())
						max1 = t.getId();
				}
				ticket.setId(max1 + 1);
				serverConnection.getServer().getTickets().add(ticket);
				serverConnection.getServer().addTicketToDatabase(serverConnection.getServer().getTickets().size() - 1);
				serverConnection.getServer().getServerDisplay().refresh();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		case 3:
			try {
				serverConnection.getOutputHandler().sendTicketInfo(in.readInt());
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		}
	}

	public void sleep(){
		try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
