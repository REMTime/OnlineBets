package display;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.MatteBorder;

import logic.Match;
import networking.Client;

public class ClientDisplay  {
	protected JFrame frame;
	protected JPanel panel;
	protected JPanel container;
	protected JFrame ticketWindow;
	protected String title = "";
	protected JPanel matchesPanel;
	public Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	public final int width = 720;
	public final int height = 480;
	
	protected int x = (int)((screenSize.getWidth() - width)/2);
	protected int y = (int)((screenSize.getHeight() - height)/2);
	
	protected final int smallSize = (width/height)*15;
	protected final int mediumSize = (width/height)*20;
	protected final int largeSize = (width/height)*30;
	
	protected final Font smallFont = new Font("Arial",Font.BOLD,smallSize);
	protected final Font mediumFont = new Font("Arial",Font.BOLD,mediumSize);
	protected final Font largeFont = new Font("Arial",Font.BOLD,largeSize);
	
	private Client client;
	private JPanel betMatches;
	private float stake;
	
	public ClientDisplay() {
		title = "Pariuri Online - Client";
		client = new Client();
		initialize();
	}
	
	public ClientDisplay(Client client){
		this.client = client;
		title = "Pariuri Online - Client";
		initialize();	
	}
	
	public void addMatch(Match match, int index){
		JPanel matchPanel = new JPanel();
		matchPanel.setPreferredSize(new Dimension(width - 50,height/6));
		matchPanel.setMaximumSize(new Dimension(width - 50,height/6));
		matchPanel.setLayout(new BorderLayout());
		
		if(match.isOver())
			matchPanel.setBackground(Color.gray);
		else matchPanel.setBackground(Color.green);
		
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
		
		JLabel stakeA = new JLabel("" + index + ")" +match.getStakeA());
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
		
		JLabel date = new JLabel("" + match.getStartTime().get(Calendar.HOUR_OF_DAY) + ":" + match.getStartTime().get(Calendar.MINUTE) + " " + match.getStartTime().get(Calendar.DATE) + "." + match.getStartTime().get(Calendar.MONTH));
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
		
		panel = new JPanel();
		panel.setBounds(0,0,width,height/8);
		panel.setBackground(Color.orange); 
		panel.setPreferredSize(new Dimension(width, height / 8));
		
		
		matchesPanel = new JPanel();
		matchesPanel.setBackground(Color.yellow);
		matchesPanel.setLayout(new BoxLayout(matchesPanel, BoxLayout.Y_AXIS));
		
		JScrollPane scroll = new JScrollPane(matchesPanel);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	    scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		matchesPanel.setPreferredSize(new Dimension((width - 25), 7 * height / 6));
	    	    
		container.add(panel,BorderLayout.PAGE_START);
		container.add(scroll, BorderLayout.CENTER);
		
		frame.add(container);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setVisible(true); 
		
		JFrame ticketFrame = new JFrame("My ticket");
		ticketFrame.setBounds(0, 0, width / 2, height);
		ticketFrame.setVisible(true);
		ticketFrame.setLocation(x + width, y);
		ticketFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ticketFrame.setResizable(false);
		
		JPanel ticketContainer = new JPanel();
		ticketContainer.setBackground(Color.lightGray);
		ticketContainer.setLayout(new BorderLayout());
		
		stake = 1;
		JLabel myTicket = new JLabel("My ticket (Current Stake: 1):");
		myTicket.setFont(mediumFont);
		
		JPanel bottomPanel = new JPanel();
		bottomPanel.setLayout(new BoxLayout(bottomPanel,BoxLayout.X_AXIS));
		bottomPanel.setBackground(Color.lightGray);
		JLabel label = new JLabel("Your sum: ");
		label.setFont(mediumFont);
		JTextField sum = new JTextField("");
		sum.setFont(mediumFont);
		sum.setColumns(10);
		JButton send = new JButton("Send");                                   //Send
		
		send.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				client.setSum((Float.parseFloat(sum.getText())));
				client.sendRequest(2);
				client.getBetMatches().clear();
				refresh();	
			}
		});
		
		send.setFont(mediumFont);
		bottomPanel.add(label);
		bottomPanel.add(sum);
		bottomPanel.add(send);
		ticketContainer.add(bottomPanel,BorderLayout.PAGE_END);
		
		betMatches = new JPanel();
		betMatches.setBounds(0, 50, width / 3, height - 100);
		betMatches.setBackground(Color.white);
		betMatches.setLayout(new BoxLayout(betMatches,BoxLayout.Y_AXIS));
		
		
		JScrollPane scrollBet = new JScrollPane(betMatches);
		scrollBet.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	    scrollBet.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		betMatches.setPreferredSize(new Dimension((width / 3), (height - 100)));
		
		ticketContainer.add(myTicket, BorderLayout.PAGE_START);
		ticketContainer.add(scrollBet,BorderLayout.CENTER);
		ticketFrame.add(ticketContainer);
		
		panel.setLayout(new BoxLayout(panel,BoxLayout.X_AXIS));
		JButton refresh = new JButton("Refresh game list");
		refresh.setBackground(Color.green);
		refresh.setFont(smallFont);
		refresh.setMaximumSize(new Dimension(width / 3, height / 8));
		
		refresh.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				client.sendRequest(1);	
			}
		});
		
		JButton addGame = new JButton("+Add game to ticket");           //add Game
		addGame.setFont(smallFont);
		addGame.setBackground(Color.yellow);
		addGame.setMaximumSize(new Dimension(width / 3, height / 8));
		
		addGame.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				
				JFrame window = new JFrame ();
				window.setBounds(0, 0, 320, 240);
				window.setAlwaysOnTop(true);
				window.setLocationRelativeTo(frame);
				window.setVisible(true);
				window.setResizable(false);
				window.setLayout(new FlowLayout());
				
				JTextField matchNumber = new JTextField("");
				matchNumber.setColumns(10);
				matchNumber.setBounds(width / 2, 10, 50, 50);
				
				JButton select = new JButton("Select Match NO");
				select.setBounds(width / 2 - 50, 60, 150, 50);
				select.addActionListener(new ActionListener(){

					@Override
					public void actionPerformed(ActionEvent e) {
						
					try{
						Match match = client.getMatches().get(Integer.parseInt(matchNumber.getText()) - 1);
						
						if(match.isOver()){
							window.dispose();
							window.setVisible(false);
							return;
						}
						else{
							JRadioButton teamA = new JRadioButton(match.getTeamA());
							JRadioButton teamB = new JRadioButton(match.getTeamB());
							JRadioButton x = new JRadioButton("X");

							teamA.setBounds(0, 200, width / 3, 100);
							teamA.setFont(largeFont);
						
							x.setBounds(width / 3, 200, width / 3, 100);
							x.setFont(largeFont);
						
							teamB.setBounds(2 * width / 3, 200, width / 3, 100);
							teamB.setFont(largeFont);
						
							JButton go = new JButton("Go");
							go.setBounds(width / 2 - 50, 300, 50, 50);
				
							teamA.addActionListener(new ActionListener(){

								@Override
								public void actionPerformed(ActionEvent e) {
									
									teamB.setSelected(false);
									x.setSelected(false);
									
								}	
							});
												
							teamB.addActionListener(new ActionListener(){

								@Override
								public void actionPerformed(ActionEvent e) {

									teamA.setSelected(false);
									x.setSelected(false);
									
								}
		
							});
						
							x.addActionListener(new ActionListener(){

								@Override
								public void actionPerformed(ActionEvent e) {

									teamA.setSelected(false);
									teamB.setSelected(false);
								}
							
							});
							
						
							go.addActionListener(new ActionListener(){

								@Override
								public void actionPerformed(ActionEvent e) {
									
									window.dispose();
									window.setVisible(false);
									
									if(teamA.isSelected())
											match.setUserTeam(teamA.getText());
									else if (teamB.isSelected())
										match.setUserTeam(teamB.getText());
									else match.setUserTeam("draw");
								
									stake *= match.getStakeOfUserTeam();
									client.getBetMatches().add(match);
									match.setOver();                                            
									refresh();	
									myTicket.setText("Ticket#" + client.getTicketId() + " (Current Stake: " + stake + "):");
								}
						
							});
						
						window.add(teamA);
						window.add(x);
						window.add(teamB);
						window.add(go);
						window.repaint();
						window.pack();
					}
				}
					catch(Exception e1){
						return;
					}
				}	
				});
			
				window.add(matchNumber);
				window.add(select);
				
			}
		});
		
		JButton check = new JButton("Check ticket");                              // check Ticket
		check.setFont(smallFont);
		check.setBackground(Color.cyan);
		check.setMaximumSize(new Dimension(width / 5, height / 8));
		
		JTextField ticketID = new JTextField();
		ticketID.setFont(largeFont);
		ticketID.setVisible(false);
		JButton go = new JButton("Go !");
		
		check.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				go.setVisible(true);
				ticketID.setVisible(true);
			}
		});

		go.setFont(largeFont);
		go.setVisible(false);
		
		go.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				go.setVisible(false);
				ticketID.setVisible(false);
				try{
					client.sendRequest(1);
					if(Integer.parseInt(ticketID.getText()) >= client.getTicketId())
						throw new Exception();
					client.setRequestedID(Integer.parseInt(ticketID.getText()));
					
					client.sendRequest(3);
					
				}
				catch(Exception e2){
					System.out.println("Exceptie");
					client.resetRequestedTicket();
				}
				JFrame window = new JFrame();
				window.setVisible(true);
				window.setAlwaysOnTop(true);
				window.setBounds(0, 0, 400, 400);
				window.setLocationRelativeTo(frame);
				window.setLayout(new BorderLayout());
				
				JPanel cont = new JPanel();
				
				cont.setLayout(new BorderLayout());
				cont.setPreferredSize(new Dimension(400, 400));
				
				JButton close = new JButton("Close");
				
				JLabel title = new JLabel("Ticket #" + ticketID.getText());
				title.setFont(largeFont);
				title.setBackground(Color.ORANGE);
				title.setHorizontalAlignment(JLabel.CENTER);
				
				close.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						window.dispose();
						window.setVisible(false);
					}
				});
				int state =  0; //0 - win 1-lose 2-game(s) not ready

				if(client.getRequestedTicket() == null || client.getRequestedTicket().isValidated()){
					JLabel notFound = new JLabel("Sorry, this ticket does not exist or has already been validated!");
					notFound.setFont(mediumFont);
					notFound.setForeground(Color.red);
					notFound.setHorizontalAlignment(JLabel.CENTER);
					cont.add(notFound, BorderLayout.CENTER);
				}
				else{
					JPanel matches = new JPanel();
					matches.setLayout(new BoxLayout(matches,BoxLayout.Y_AXIS));
					matches.setBackground(Color.gray);
					
					for(Match match : client.getRequestedTicket().getMatches()){
						Match tmp = client.getEqMatch(match);
						
						tmp.setWinnerTeam();
						System.out.println(tmp.getScoreA()+" !!! "+ tmp.getScoreB());
						
						String result = tmp.getWinnerTeam();
						System.out.println(result);
						if(!result.equals("draw"))
								result +=" won";
						
						JLabel l = new JLabel(match.toString()+", " + result + ", your pick: "+match.getUserTeam());
						if(!tmp.isOver()){
							JLabel lab = new JLabel(match.toString()+" - This game is not over yet!");
							lab.setForeground(Color.orange);
							matches.add(lab, BorderLayout.CENTER);
							state = 2;
						}
						else{
							
							if(match.getUserTeam().equals(tmp.getWinnerTeam()))
								l.setForeground(Color.green);
							else {
								l.setForeground(Color.red);
								state = 1;
							}
							matches.add(l, BorderLayout.CENTER);
						}
					}
					matchesPanel.setPreferredSize(new Dimension((width - 25), client.getMatches().size() * height / 6));
					cont.add(matches, BorderLayout.CENTER);
										
				JLabel bottom = new JLabel();
				if(state == 0){	
					bottom.setText("Congratulations! You won: " + client.getRequestedTicket().getWinSum());
					client.getRequestedTicket().validate();
				}	
				else if(state == 1){
					bottom.setText("Sorry, you lost!");
					client.getRequestedTicket().validate();
				}
				else bottom.setText(" - One ore more games aren't ready yet!");
			
				cont.add(bottom, BorderLayout.PAGE_END);
				}
				window.add(cont,BorderLayout.CENTER);
				window.add(close,BorderLayout.PAGE_END);
				window.add(title, BorderLayout.PAGE_START);
			}
		});
		
		panel.add(refresh);
		panel.add(addGame);
		panel.add(check);
		panel.add(ticketID);
		panel.add(go);
		
		panel.invalidate();
		panel.validate();
		panel.repaint();
	}
	public void refresh(){
		
		matchesPanel.removeAll();
		for(Match match : client.getMatches())
			{
				for(Match betM : client.getBetMatches()){
					if(match.equals(betM)){
						match.setOver();
					}
				}
				addMatch(match, client.getMatches().indexOf(match) + 1);
			}
		matchesPanel.repaint();
		matchesPanel.invalidate();
		matchesPanel.validate();
		matchesPanel.setPreferredSize(new Dimension((width - 25), client.getMatches().size() * height / 6));
		
		betMatches.removeAll();
		
		for(Match match : client.getBetMatches()){
			JPanel p = new JPanel();
			p.setMaximumSize(new Dimension(width / 3, 70));
			p.setBorder(new MatteBorder(0, 0, 2, 0, Color.black));
			JLabel label = new JLabel(match.toString());
			label.setPreferredSize(new Dimension(width/3,70));
			label.setBackground(Color.red);
			label.setHorizontalAlignment(SwingConstants.CENTER);
			p.setFont(smallFont);
			p.add(label);
			betMatches.add(p);
		}
		betMatches.repaint();
		betMatches.invalidate();
		betMatches.validate();
		betMatches.setPreferredSize(new Dimension((width / 3), 70 * client.getBetMatches().size()));
	}
}
