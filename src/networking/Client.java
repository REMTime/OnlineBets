package networking;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Calendar;

import display.ClientDisplay;
import logic.Match;
import logic.Ticket;

public class Client {

	private ArrayList<Match> matches;
	private ArrayList<Match> betMatches;
	private Socket socket;
	private float sum;
	private int ticketId;

	private ObjectInputStream in;
	private ObjectOutputStream out;
	
	public Ticket requestedTicket = null;
	
	ClientDisplay clientDisplay;
	
	public int requestedID;
	
	boolean isActive = true;

	public static void main (String[] args){
		new Client();
	}

	public void showMatchList(){
		int counter = 0;
		for(Match match : matches)
			System.out.println(++counter+". "+match + "\n");
	}

	public Client(){
		matches = new ArrayList<Match> ();
		betMatches = new ArrayList<Match> ();
		clientDisplay = new ClientDisplay(this);
		try {
			socket = new Socket("localhost",4444);
			in = new ObjectInputStream(socket.getInputStream());
			out = new ObjectOutputStream(socket.getOutputStream());
			getMatchList();	
			while(isActive){		
			}
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		try {
			in.close();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendTicket(){ 								 //trimite ticket catre server
		try {
			out.writeInt(betMatches.size());
			for(Match match : betMatches){
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
			out.writeFloat(sum);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void getMatchList(){                                      //primeste lista de meciuri de la server
		try {
			matches.clear();
			ticketId = in.readInt();
			int count = in.readInt();
			for(int i = 0 ; i < count ; ++i){
				String teamA = (String) in.readObject();
				float stakeA = (Float)  in.readFloat();
				String teamB = (String) in.readObject();
				float stakeB = (Float)  in.readFloat();
				
				float stakeDraw = (Float) in.readFloat();
				
				int scoreA = in.readInt();
				int scoreB = in.readInt();
				Calendar startTime = (Calendar) in.readObject();
				String userTeam = (String) in.readObject();
				boolean isOver = in.readBoolean();
				matches.add(new Match(teamA, stakeA, teamB, stakeB, stakeDraw, scoreA, scoreB, startTime, userTeam, isOver));
			}
			clientDisplay.refresh();
		} 
		catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
	}

	public Match getEqMatch(Match m){
		for(Match match : matches)
			if(match.equals(m))
				return match;
		return null;
	}
	
	public void sendRequest(int request){
		try {
			out.writeInt(request);
			out.flush();
			switch(request){
			case 1:
				getMatchList();
				break;
			case 2:
				sendTicket();
				break;
			case 3:
				checkID();
				getTicketInfo();   
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private Ticket getTicketInfo() {
		try {
			requestedTicket = (Ticket) in.readObject();
			if(requestedTicket == null)
				return requestedTicket;
		} 
		catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	private void checkID(){
		try {
			out.writeInt(requestedID);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public ArrayList<Match> getMatches() {
		return matches;
	}

	public ArrayList<Match> getBetMatches() {
		return betMatches;
	}

	public void setSum(float sum) {
		this.sum = sum;
	}
	
	public void setRequestedID(int id){
		requestedID = id;
	}

	public Ticket getRequestedTicket() {
		return requestedTicket;
	}
	
	public int getTicketId(){
		return ticketId;
	}
	
	public void resetRequestedTicket(){
		requestedTicket = null;
	}
}




