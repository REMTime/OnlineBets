package logic;

import java.io.Serializable;
import java.util.ArrayList;

public class Ticket implements Serializable{

	private ArrayList <Match> matches;
	private float stake, sum;
	private int id;
	boolean validated, winningTicket = false;
	
	public Ticket(){
		matches = new ArrayList <Match> ();
		sum = 0;
		stake = 1;
	}
	
	public void setId(int id){
		this.id = id;
	}
	
	public int getId(){
		return id;
	}
	
	public void addMatchToTicket(Match match){
		matches.add(match);
	}

	public void setSum(float sum){
		this.sum = sum;
	}

	public float getWinSum(){
		for(Match match : matches)
			stake *= match.getStakeOfUserTeam();
		return stake*sum;
	}

	public String toString(){
		String output = "Bet id: "+id + "\n";
		for(Match match : matches)
			output += match;
		stake = 1;
		output += "You bet: " + sum + ", \tPotential win: " + getWinSum();
		stake = 1; 								
		return output+"\n";
	}

	public boolean isValidated(){
		return validated;
	}
	
	public boolean isWinnerTicket(){
		return winningTicket;
	}
	
	public void validate() {
			validated = true;
			boolean won = true;
			for(Match match : matches)
				if(!match.ifWon())
					won = false;
			winningTicket = won;
	}

	public float getSum() {
		return sum;
	}

	public boolean contains(Match match) {
		for(Match m : matches)
			if(m.getTeamA().equals(match.getTeamA()))
				return true;
		return false;
	}

	public ArrayList<Match> getMatches() {
		return matches;
	}

	public void reset() {
		matches.clear();
	}
	
	public void setValidated(boolean validated){    //only for database
		this.validated = validated;
	}
	
	public void setWinningTicket(boolean winningTicket){
		this.winningTicket = winningTicket;
	}
}
