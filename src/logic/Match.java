package logic;

import java.io.Serializable;
import java.util.Calendar;

public class Match implements Serializable{

	private String teamA, teamB, winnerTeam="winnerTeam", userTeam ="userTeam";
	private int scoreA, scoreB;
	private float stakeA, stakeB, stakeDraw = 1;
	private Calendar startTime;
	boolean isOver = false;
	private int id;
	
	public Match(String teamA, float stakeA, String teamB, float stakeB, float stakeDraw, Calendar startTime){
		this.teamA = teamA;
		this.stakeA = stakeA;
		this.teamB = teamB;
		this.stakeB = stakeB;
		this.scoreA = 0;
		this.scoreB = 0;
		this.stakeDraw = stakeDraw;
		this.startTime = startTime;
	}
	
	public Match(String teamA, float stakeA, String teamB, float stakeB, float stakeDraw, int scoreA, int scoreB, Calendar startTime, String userTeam, boolean isOver){
		this.teamA = teamA;
		this.stakeA = stakeA;
		this.teamB = teamB;
		this.stakeB = stakeB;
		this.stakeDraw = stakeDraw;
		this.scoreA = scoreA;
		this.scoreB = scoreB;
		this.isOver = isOver;
		this.userTeam = userTeam;
		this.startTime = startTime;
	}
	
	@Override
	public String toString() {
		if(userTeam.equals(teamA))
		return 	"[x] "+teamA + " - " + teamB + "\n";
		else if(userTeam.equals(teamB)) 
			return teamA + " - "  + teamB +"[x]"+"\n";
		else
			return 	teamA  + " [x] " + teamB +"\n";
	}
																																										
	public float getStakeOfUserTeam() {
		if(userTeam.equals(teamA))
			return stakeA;
		
		else if(userTeam.equals(teamB))
			return stakeB;
		
		return stakeDraw;
	}
	
	public void setUserTeam(String userTeam){
		this.userTeam = userTeam;
	}
	
	public void setOver(){
		this.isOver = true;
	}
	
	public void setWinnerTeam(){
		if(scoreA > scoreB)
			winnerTeam = teamA;
		else if(scoreA < scoreB)
			winnerTeam = teamB;
		else winnerTeam = "draw";
	}
	public boolean ifWon() {
		setWinnerTeam();
		return userTeam.equals(winnerTeam);
	}

	public boolean isOver() {
		return isOver;
	}
	
	public void setScore(int scoreA, int scoreB) {
		this.scoreA = scoreA;
		this.scoreB = scoreB;
	}
	
	public void setId(int id){
		this.id = id;
	}
	
	public int getId(){
		return id;
	}
	
	public String getTeamA(){
		return teamA;
	}
	
	public String getTeamB(){
		return teamB;
	}

	public int getScoreA() {
		return scoreA;
	}
	
	public int getScoreB() {
		return scoreB;
	}

	public float getStakeA() {
		return stakeA;
	}
	
	public float getStakeB() {
		return stakeB;
	}

	public float getStakeDraw(){
		return stakeDraw;
	}
	
	public Calendar getStartTime(){
		return startTime;
	}
	
	public String getWinnerTeam() {
		return this.winnerTeam;
	}
	
	public String getUserTeam(){
		return this.userTeam;
	}
		
	public boolean equals(Match match){
		return this.getTeamA().equals(match.getTeamA()) && this.getTeamB().equals(match.getTeamB());
	}
}
