package display;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Calendar;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.MatteBorder;

import logic.Match;
import logic.Ticket;
import networking.Server;

public class ServerDisplay  {
	protected JFrame frame;
	protected JPanel panel;
	protected JPanel container;
	protected JPanel bottomPanel;
	protected JFrame ticketWindow;
	protected String title = "";
	protected JPanel matchesPanel;
	protected JPanel matchPanel;
	public Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	public final int width = 720;
	public final int height = 480;
	
	protected int x = (int)((screenSize.getWidth() - width)/2);
	protected int y = (int)((screenSize.getHeight() - height)/2);
	
	protected final int smallSize = (width/height)*15;
	protected final int mediumSize = (width/height)*20;
	protected final int largeSize = (width/height)*30;
	
	protected final Font smallFont = new Font("Arial",Font.BOLD, smallSize);
	protected final Font mediumFont = new Font("Arial",Font.BOLD, mediumSize);
	protected final Font largeFont = new Font("Arial",Font.BOLD, largeSize);
	
	JPanel ticketContainer;
	private Server server;

	public ServerDisplay(Server server) {
		this.server = server;
		title = "Pariuri Online - Server";
		initialize();
	}
	
	public ServerDisplay(){
		title = "Pariuri Online - Server";
		initialize();	
	}

	public void addMatch(Match match){
		matchPanel = new JPanel();
		matchPanel.setPreferredSize(new Dimension(width - 50, height / 6));
		matchPanel.setMaximumSize(new Dimension(width - 50, height / 6));
		matchPanel.setMinimumSize(new Dimension(width - 50, height / 6));
		matchPanel.setLayout(new BorderLayout());
		
		if(match.isOver())
			matchPanel.setBackground(Color.gray);
		else 
			matchPanel.setBackground(Color.green);
		
		matchPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		
		JPanel teamAPanel = new JPanel();
		teamAPanel.setLayout(new BorderLayout());
		teamAPanel.setBounds(0, 0, width / 2 - 50, height / 6);
		teamAPanel.setPreferredSize(new Dimension(width/2 - 50 , height / 6));
		teamAPanel.setBackground(matchPanel.getBackground());
		
		JLabel teamA = new JLabel(match.getTeamA());
		teamA.setFont(mediumFont);
		teamA.setHorizontalAlignment(JLabel.CENTER);
		
		JLabel scoreA = new JLabel(""+match.getScoreA());
		scoreA.setFont(largeFont);
		
		JLabel stakeA = new JLabel(""+match.getStakeA());
		stakeA.setFont(smallFont);
		
		teamAPanel.add(stakeA,BorderLayout.LINE_START);
		teamAPanel.add(teamA, BorderLayout.CENTER);
		teamAPanel.add(scoreA,BorderLayout.LINE_END);
		
		JPanel teamBPanel = new JPanel();
		teamBPanel.setLayout(new BorderLayout());
		teamBPanel.setBounds(0, 0, width / 2 - 50, height / 6);
		teamBPanel.setPreferredSize(new Dimension(width / 2 - 50 , height / 6));
		teamBPanel.setBackground(matchPanel.getBackground());
		
		JLabel teamB = new JLabel(match.getTeamB());
		teamB.setFont(mediumFont);
		teamB.setHorizontalAlignment(JLabel.CENTER);
		
		JLabel scoreB = new JLabel("" + match.getScoreB());
		scoreB.setFont(largeFont);
		
		JLabel stakeB = new JLabel("" + match.getStakeB());
		stakeB.setFont(smallFont);
		
		teamBPanel.add(scoreB,BorderLayout.LINE_START);
		teamBPanel.add(teamB,BorderLayout.CENTER);
		teamBPanel.add(stakeB,BorderLayout.LINE_END);
		
		JLabel middle = new JLabel("" + match.getStakeDraw());
		middle.setFont(smallFont);
		middle.setHorizontalAlignment(JLabel.CENTER);
		
		JLabel date = new JLabel("Id:" + match.getId() + ", Data: " + match.getStartTime().get(Calendar.HOUR_OF_DAY) + ":" + match.getStartTime().get(Calendar.MINUTE) + " " + match.getStartTime().get(Calendar.DATE) + "." + match.getStartTime().get(Calendar.MONTH));
		date.setFont(mediumFont);
		date.setHorizontalAlignment(JLabel.CENTER);
		
		matchPanel.add(date, BorderLayout.PAGE_START);
		matchPanel.add(teamAPanel, BorderLayout.LINE_START);
		matchPanel.add(middle, BorderLayout.CENTER);
		matchPanel.add(teamBPanel, BorderLayout.LINE_END);
		matchesPanel.add(matchPanel);
	}
	
	protected void initialize() {

		frame = new JFrame();
		frame.setBounds(0, 0, width, height);
		frame.setLocation(x, y);
		frame.setTitle(title);
		
		container = new JPanel();
		container.setBounds(0, 0, width, height);
		container.setBackground(Color.green);
		container.setLayout(new BorderLayout());
		
		panel = new JPanel();                                                        //panelul pentru butoane
		panel.setBounds(0, 0, width, height / 8);
		panel.setBackground(Color.orange);
		panel.setPreferredSize(new Dimension(width, height / 8));
		
		matchesPanel = new JPanel();
		matchesPanel.setBackground(Color.yellow);
		matchesPanel.setLayout(new BoxLayout(matchesPanel, BoxLayout.Y_AXIS));
		
		JScrollPane scroll = new JScrollPane(matchesPanel);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	    scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		matchesPanel.setPreferredSize(new Dimension((width - 25), server.getMatches().size() * height / 6));
		
		container.add(panel,BorderLayout.PAGE_START);
		container.add(scroll, BorderLayout.CENTER);
		
		bottomPanel = new JPanel();                                                               // Ratio
		server.ratio();
		JLabel l = new JLabel("Income: "+server.getIncome());
		JLabel ll = new JLabel(", Ratio: " + server.getNrWinnerTickets() + ":" + server.getTickets().size()); 
		
		bottomPanel.setBackground(Color.orange);
		l.setFont(largeFont);
		ll.setFont(largeFont);
		bottomPanel.setPreferredSize(new Dimension(width, height / 8));
		bottomPanel.add(l);
		bottomPanel.add(ll);
		container.add(bottomPanel,BorderLayout.PAGE_END);
													
		frame.getContentPane().add(container);																
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setVisible(true);
		
		frame.setLocation(100, 200);																
		ticketWindow = new JFrame("Ticket Database");											//frame-ul pentru tickets
		//ticketWindow.setAlwaysOnTop(true);
		ticketWindow.setBounds(0, 0, width / 3, height);
		ticketWindow.setVisible(true);
		ticketWindow.setLocation(frame.getX() + width, frame.getY());
		ticketWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ticketWindow.setResizable(false);
		
		ticketContainer = new JPanel();
		ticketContainer.setBackground(Color.lightGray);
		ticketContainer.setLayout(new BoxLayout(ticketContainer, BoxLayout.Y_AXIS));
		ticketWindow.add(ticketContainer);
		
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		
		JButton removeGame = new JButton("Remove Game");                         //remove game
		removeGame.setBackground(Color.red);
		removeGame.setFont(largeFont);
		removeGame.setPreferredSize(new Dimension(width, height));
		
		removeGame.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				JFrame window = new JFrame();
				window.setAlwaysOnTop(true);
				window.setLocationRelativeTo(frame);
				window.setSize(240, 120);
				window.setVisible(true);
				window.setLayout(new FlowLayout());
					
				JTextField remove = new JTextField("ID");
				remove.setMaximumSize(new Dimension(width / 4, 20));
				remove.setColumns(5);
				
				remove.addFocusListener(new FocusListener() {
					public void focusGained(FocusEvent e){
						remove.setText("");
					}
					public void focusLost(FocusEvent e){}
				});
				
				JButton button = new JButton("Remove!");
				button.setPreferredSize(new Dimension(100, 20));
				button.addActionListener(new ActionListener(){

					@Override
					public void actionPerformed(ActionEvent arg0) {
						int idToRemove = -1;
						for(Match m : server.getMatches()){
							try{
								if(m.getId() == Integer.parseInt(remove.getText())){
									idToRemove = server.getMatches().indexOf(m);
								}
							}
							catch(Exception e){
								return;
							}
						}
						if(idToRemove != -1)
						{
							server.getMatches().remove(idToRemove);
							server.removeMatchFromDatabase(Integer.parseInt(remove.getText()));
						}
						refresh();
						window.dispose();
					}
					
				});
				window.add(remove);
				window.add(button);
			}
		});
	
		JButton setScore = new JButton("Set Score");                       //set score
		setScore.setBackground(Color.yellow);
		setScore.setFont(largeFont);
		
		setScore.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				JFrame window = new JFrame();
				window.setVisible(true);
				window.setAlwaysOnTop(true);
				window.setLocationRelativeTo(frame);
				window.setSize(240, 120);
				window.setLayout(new FlowLayout());
				
				JTextField matchNumber = new JTextField("ID");
				matchNumber.setColumns(5);
				
				JTextField scoreA = new JTextField("scoreA");
				scoreA.setColumns(5);
				
				scoreA.addFocusListener(new FocusListener() {
					public void focusGained(FocusEvent e){
						scoreA.setText("");
					}
					public void focusLost(FocusEvent e){}
				});
				
				JTextField scoreB = new JTextField("scoreB");				
				scoreB.setColumns(5);
				
				scoreB.addFocusListener(new FocusListener() {
					public void focusGained(FocusEvent e){
						scoreB.setText("");
					}
					public void focusLost(FocusEvent e){}
				});
				
				JButton button = new JButton("Set score!");
				
				JRadioButton radio = new JRadioButton("Match ended");
				
				button.addActionListener(new ActionListener(){
					
					@Override
					public void actionPerformed(ActionEvent e){
						try{
							int index = -1;
							for(Match m : server.getMatches()){
								if(m.getId() == Integer.parseInt(matchNumber.getText())){
									index = server.getMatches().indexOf(m);
								}
							}
						
							server.getMatches().get(index).setScore(Integer.parseInt(scoreA.getText()), Integer.parseInt(scoreB.getText()));
						
							if(radio.isSelected())
							{
								server.getMatches().get(index).setOver();
								server.getMatches().get(index).setWinnerTeam();	
							}
						server.setScoreInDatabase(Integer.parseInt(matchNumber.getText()), Integer.parseInt(scoreA.getText()), Integer.parseInt(scoreB.getText()),server.getMatches().get(index).getWinnerTeam(), server.getMatches().get(index).isOver());
						}
						catch(Exception eSet){
							return;
						}
						window.dispose();
						window.setVisible(false);
						
						refresh();
					}
				});
				
				window.add(matchNumber);
				window.add(scoreA);
				window.add(scoreB);
				window.add(radio);
				window.add(button);
				
			}
			
		});
		setScore.setPreferredSize(new Dimension(width,height));
		
		JButton addGame = new JButton("Add New Game");                   //add game
		addGame.setPreferredSize(new Dimension(width,height));
		addGame.setBackground(Color.green);
		addGame.setFont(largeFont);
		
		addGame.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				JFrame window = new JFrame();
				window.setAlwaysOnTop(true);
				window.setBounds(0, 0, 320, 240);
				window.setLocationRelativeTo(frame);			
				window.setVisible(true);
				window.setLayout(new FlowLayout());
				
				JTextField teamA = new JTextField("teamA");
				teamA.setFont(mediumFont);
				JTextField teamB = new JTextField("teamB");
				teamB.setFont(mediumFont);
				JTextField stakeA = new JTextField("stakeA");
				stakeA.setFont(mediumFont);
				
				stakeA.addFocusListener(new FocusListener() {
					public void focusGained(FocusEvent e){
						stakeA.setText("");
					}
					public void focusLost(FocusEvent e){}
				});
				
				JTextField stakeB = new JTextField("stakeB");
				stakeB.setFont(mediumFont);
				
				stakeB.addFocusListener(new FocusListener() {
					public void focusGained(FocusEvent e){
						stakeB.setText("");
					}
					public void focusLost(FocusEvent e){}
				});
				
				JTextField stakeDraw = new JTextField("stakeDraw");
				stakeDraw.setFont(mediumFont);
				
				stakeDraw.addFocusListener(new FocusListener() {
					public void focusGained(FocusEvent e){
						stakeDraw.setText("");
					}
					public void focusLost(FocusEvent e){}
				});
				
				JTextField hour = new JTextField("hour");
				hour.setFont(mediumFont);
				
				hour.addFocusListener(new FocusListener() {
					public void focusGained(FocusEvent e){
						hour.setText("");
					}
					public void focusLost(FocusEvent e){}
				});
				
				JTextField minute = new JTextField("minute");
				minute.setFont(mediumFont);
				
				minute.addFocusListener(new FocusListener() {
					public void focusGained(FocusEvent e){
						minute.setText("");
					}
					public void focusLost(FocusEvent e){}
				});
				
				JTextField day = new JTextField("day");
				day.setFont(mediumFont);
				
				day.addFocusListener(new FocusListener() {
					public void focusGained(FocusEvent e){
						day.setText("");
					}
					public void focusLost(FocusEvent e){}
				});
				
				JTextField month = new JTextField("month");
				month.setFont(mediumFont);
				
				month.addFocusListener(new FocusListener() {
					public void focusGained(FocusEvent e){
						month.setText("");
					}
					public void focusLost(FocusEvent e){}
				});
				
				JButton button = new JButton("Add!");
				button.setFont(mediumFont);
				
				button.addActionListener(new ActionListener(){

					@Override
					public void actionPerformed(ActionEvent e) {
						try{
							Calendar startTime = Calendar.getInstance();
							startTime.set(0, Integer.parseInt(month.getText()), Integer.parseInt(day.getText()), Integer.parseInt(hour.getText()), Integer.parseInt(minute.getText()));
							int max1 = -1;
							for(Match m : server.getMatches()){
								if(m.getId() > max1){
									max1 = m.getId();
								}
							}						
							server.getMatches().add(new Match(	teamA.getText(),
																Float.parseFloat(stakeA.getText()),
																teamB.getText(),
																Float.parseFloat(stakeB.getText()),
																Float.parseFloat(stakeDraw.getText()),
																startTime
																));
							server.getMatches().get(server.getMatches().size() - 1).setId(max1 + 1);
							server.addMatchToDatabase(server.getMatches().size()-1);
						}
						catch(Exception eAdd){
							return;
						}
						refresh();
						window.dispose();
						window.setVisible(false);		
					}	
				});
				
				window.add(teamA);
				window.add(stakeA);
				window.add(teamB);
				window.add(stakeB);
				window.add(stakeDraw);
				window.add(hour);
				window.add(minute);
				window.add(day);
				window.add(month);
				window.add(button);
				
			}
		});
			
		panel.add(removeGame);
		panel.add(setScore);
		panel.add(addGame);
		panel.invalidate();
		panel.validate();

	}
	public void refresh(){
		
		matchesPanel.removeAll();
		
		for(Match match : server.getMatches())
			addMatch(match);
		
		matchesPanel.repaint();
		matchesPanel.invalidate();
		matchesPanel.validate();
		matchesPanel.setPreferredSize(new Dimension((width - 25), server.getMatches().size() * height / 6));
		ticketContainer.removeAll();
		
		JPanel p = new JPanel();
		p.setPreferredSize(new Dimension(ticketWindow.getWidth(), ticketWindow.getHeight()));
		p.setFont(smallFont);
		JScrollPane scrollTickets = new JScrollPane(p);
		scrollTickets.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	    scrollTickets.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		ticketContainer.add(scrollTickets);
		
		
		for(Ticket ticket : server.getTickets()){
			
			JPanel pn = new JPanel();   
			pn.setBorder(new MatteBorder(0, 0, 2, 0, Color.black));
			pn.setMaximumSize(new Dimension(width / 3, 25));
			JTextArea label = new JTextArea(ticket.toString());
			label.setEditable(false);
			label.setMaximumSize(new Dimension(width / 3, 70));
			label.setMinimumSize(new Dimension(width / 3, 70));
			pn.add(label);
			p.add(pn);
			
		}
		p.setPreferredSize(new Dimension(width/3, server.getTickets().size()*70));
		ticketWindow.add(ticketContainer);
		
		bottomPanel.removeAll();
		server.ratio();
		JLabel l = new JLabel("Income: "+server.getIncome());
		JLabel ll = new JLabel(", Ratio: " + server.getNrWinnerTickets() + ":" +server.getTickets().size()); 
		bottomPanel.setBackground(Color.orange);
		l.setFont(largeFont);
		ll.setFont(largeFont);
		bottomPanel.add(l);
		bottomPanel.add(ll);
		bottomPanel.invalidate();
		bottomPanel.validate();
		
		ticketContainer.repaint();
		ticketContainer.invalidate();
		ticketContainer.validate();
		
	}
}
