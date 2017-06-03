package networking;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import display.ServerDisplay;
import logic.Match;
import logic.Ticket;

public class Server {

	ServerSocket serverSocket;
	public ArrayList <ServerConnection> connections;
	private static ServerDisplay serverDisplay;
	
	private static ArrayList <Match> matches;
	private ArrayList <Ticket> tickets;
	private float income;
	private int nrWinnerTickets;

	private Connection con;
	
	boolean isActive = true;

	public static void main(String[] args){	
		new Server();
	}
		
	public Server(){

		connections = new ArrayList<ServerConnection> ();

		tickets = new ArrayList <Ticket>();
		income = 0;
		nrWinnerTickets = 0;
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/pariuriDatabase", "root", "123456");
			matches = getMatchesFromDatabase();
			tickets  = getTicketsFromDatabase();
			if (con != null) {
				System.out.println("You made it, take control your database now!");
			} else {
				System.out.println("Failed to make connection!");
			}
		} catch (SQLException e1) {
			System.out.println("eroare sql");
			e1.printStackTrace();
		} catch (ClassNotFoundException e1) {
			System.out.println("Where is your MySQL JDBC Driver?");
			e1.printStackTrace();
		}

		serverDisplay = new ServerDisplay(this);
		serverDisplay.refresh();

			try {
				serverSocket = new ServerSocket(4444);
				while(isActive){

					Socket socket = serverSocket.accept();

					ServerConnection serverConnection = new ServerConnection(socket, this);
					serverConnection.start();
					connections.add(serverConnection);		
				}
			} 
			catch (IOException e) {
				e.printStackTrace();
			}
	}
	
	public void addMatchToDatabase(int id_meci){
		
		Match meci = matches.get(id_meci);
				
		String sql = "insert into meciuri "
				+ "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		PreparedStatement st;
		try {
			st = con.prepareStatement(sql);
			st.setInt(1, meci.getId());
			st.setString(2, meci.getTeamA());
			st.setFloat(3, meci.getStakeA());
			st.setString(4, meci.getTeamB());
			st.setFloat(5, meci.getStakeB());
			st.setFloat(6, meci.getStakeDraw());
			st.setInt(7, meci.getScoreA());
			st.setInt(8, meci.getScoreB());
			st.setTimestamp(9, new Timestamp(meci.getStartTime().getTimeInMillis()));
			st.setString(10,  meci.getWinnerTeam());
			st.setBoolean(11, meci.isOver());
			
			st.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void removeMatchFromDatabase(int id_meci){
		String sql = "delete from meciuri"
				   + "	where id_meci =" + id_meci;
		PreparedStatement ps;
		try {
			ps = con.prepareStatement(sql);
			ps.executeUpdate();
				
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
	}
	
	public void setScoreInDatabase(int id_meci, int scoreA, int scoreB, String winnerTeam, boolean isOver){
		String sql = "update meciuri"
				+ " set scoreA = " + scoreA + ", scoreB = " + scoreB + ", winnerTeam = ?, isOver = ?" + 
				" where id_meci = " + id_meci;
		PreparedStatement ps;
		try {
			ps = con.prepareStatement(sql);
			ps.setString(1, winnerTeam);
			ps.setBoolean(2, isOver);
			ps.executeUpdate();
				
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
	}
	
	public ArrayList<Match> getMatchesFromDatabase(){
		ArrayList<Match> meciuri = new ArrayList<Match>();
		Statement st;
		try {
			
			st = con.createStatement();
			ResultSet rez = st.executeQuery("Select * from meciuri");
			while(rez.next()){
				Calendar data = Calendar.getInstance();
				data.setTime(rez.getTimestamp(9));
				data.add(Calendar.MONTH, 1);
				Match m = new Match(rez.getString(2), rez.getFloat(3), rez.getString(4), rez.getFloat(5), rez.getFloat(6), rez.getInt(7), 
						            rez.getInt(8), data, rez.getString(10), rez.getBoolean(11));
				m.setId(rez.getInt(1));
				meciuri.add(m);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}	
		return meciuri;
	}
	
	public void addTicketToDatabase(int id_bilet){    //index din ArrayListul Tickets
		Ticket t = tickets.get(id_bilet);
		PreparedStatement ps;
		String sql1 = "insert into bilete (id_bilet, suma) "
				+ " values (?, ?)";
		try {
			ps = con.prepareStatement(sql1);
			ps.setInt(1, t.getId());
			ps.setFloat(2, t.getSum());
			ps.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		for(int i = 0; i<t.getMatches().size(); i++){
			PreparedStatement ps2;
			String sql2 = "insert into intermediar "
					+ " values (?, ?, ?)";
			try {
				ps2 = con.prepareStatement(sql2);
				int idMeci = -1;
				for(Match m : matches){
					if(m.getTeamA().equals(t.getMatches().get(i).getTeamA()) && m.getStartTime().equals(t.getMatches().get(i).getStartTime()))
						idMeci = m.getId();
				}
				ps2.setInt(1, idMeci);
				ps2.setInt(2, t.getId());
				ps2.setString(3, t.getMatches().get(i).getUserTeam());
				ps2.executeUpdate();
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void setValidatedInDatabase(int id_bilet, boolean winningTicket, boolean validated){   // indexul din tickets
		PreparedStatement ps;
		String sql = "update bilete"
				+ " set isWinner = ?, validated = ?"
				+ " where id_bilet = " + id_bilet;
		try {
			ps = con.prepareStatement(sql);
			ps.setBoolean(1, winningTicket);
			ps.setBoolean(2, validated);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<Ticket> getTicketsFromDatabase(){
		ArrayList<Ticket> bilete = new ArrayList<Ticket>();
		Statement st;
		try {
			st = con.createStatement();
			ResultSet rs = st.executeQuery("Select * from bilete");
			while(rs.next()){
				Ticket t = new Ticket();
				t.setId(rs.getInt(1));
				t.setSum(rs.getFloat(2));
				t.setWinningTicket(rs.getBoolean(3));
				t.setValidated(rs.getBoolean(4));
				
				Statement st2;
				st2 = con.createStatement();
				ResultSet rs2 = st2.executeQuery("Select * from intermediar"
						+ " where id_bilet = " + t.getId());
				while(rs2.next()){
					int id = rs2.getInt(1);
					for(Match m : matches){
						if(m.getId() == id){
							m.setUserTeam(rs2.getString("userTeam"));
							t.addMatchToTicket(m);
						}	
					}
				}
				bilete.add(t);				
			}
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return bilete;
	}
	
	public void ratio(){
		income = 0;
		nrWinnerTickets = 0;
		for(Ticket ticket : tickets)
			if(ticket.isValidated())
				if(ticket.isWinnerTicket())
				{
					income -= ticket.getWinSum();
					nrWinnerTickets++;
				}
				else
					income += ticket.getSum();
	}
	
	public int getNrWinnerTickets(){
		return nrWinnerTickets;
	}

	public float getIncome(){
		return income;
	}
	
	public ArrayList<Match> getMatches() {
		return matches;
	}

	public ArrayList<Ticket> getTickets() {
		return tickets;
	}

	public ServerDisplay getServerDisplay() {	
		return serverDisplay;
	}

	public Ticket getTicketById(int requestedID) {
		for(int i = 0 ; i < tickets.size() ; ++i)
			if(tickets.get(i).getId() == requestedID)
				return tickets.get(i);
		return null;
	}
}

