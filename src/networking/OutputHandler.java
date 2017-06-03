package networking;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import logic.Match;
import logic.Ticket;

public class OutputHandler extends Thread{
	private ObjectOutputStream out;
	private Socket socket;
	private ServerConnection serverConnection;
	boolean isActive = true;
	boolean isWorking = false;
	
	public OutputHandler(ServerConnection serverConnection, Socket socket){
		this.serverConnection = serverConnection;
		this.socket = socket;
	}
	
	public void sendListToClient(){             //trimite lista de meciuri la client
		try {
			out.writeInt(serverConnection.getServer().getTickets().size());
			out.writeInt(serverConnection.getServer().getMatches().size());
			for(Match match : serverConnection.getServer().getMatches()){
				match.setWinnerTeam();
				out.writeObject(match.getTeamA());
				out.writeFloat(match.getStakeA());
				out.writeObject(match.getTeamB());
				out.writeFloat(match.getStakeB());
				out.writeFloat(match.getStakeDraw());
				out.writeInt(match.getScoreA());
				out.writeInt(match.getScoreB());
				out.writeObject(match.getStartTime());
				out.writeObject(match.getUserTeam());
				out.writeBoolean(match.isOver());
			}
			out.flush();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		try {
			out = new ObjectOutputStream(socket.getOutputStream());
			sendListToClient();
			while(isActive){
			
			}
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendTicketInfo(int requestedID) {
		try {
			System.out.println(requestedID);
			out.writeObject(serverConnection.getServer().getTicketById(requestedID));
			boolean ok = true;
			Ticket t = serverConnection.getServer().getTicketById(requestedID);
			for(Match m : t.getMatches()){
				if(!m.isOver())
					ok = false;
			}
			if(ok){
				t.validate();
				serverConnection.getServer().setValidatedInDatabase(requestedID, t.isWinnerTicket(), true);
				System.out.println(t.isWinnerTicket());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
