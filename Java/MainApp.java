import java.awt.Color;

public class MainApp extends javax.swing.JFrame {
	public MainApp() {
		initComponents(); // Initiate All Components
		getContentPane().setBackground(new Color(173, 216, 230)); // Set Background Color
	}

	private void initComponents() {
		title = new javax.swing.JLabel(); // Make our JLabel uwu
		serverBtn = new javax.swing.JButton(); // The button that will redirect you to the Server Activity
		clientBtn = new javax.swing.JButton(); // Same as above but for Client Activity
		cp = new javax.swing.JLabel(); // I Don't actually remember who you are.. Ask God.
		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE); // EXIT WHEN I CLOSE YOU, OKAY ?!
		setTitle("Anonybox R"); // I was about to put my crush's name here but it's ok
		setBackground(new java.awt.Color(0, 0, 204)); // Set the background "yeah boiii"
		setName("main_frame"); // God only knows what's this
		setResizable(false); // DON'T RESIZE IT OR I'LL KILL YOU !
		title.setFont(new java.awt.Font("Papyrus", 1, 24)); // Let's be a little fancy
		title.setHorizontalAlignment(javax.swing.SwingConstants.CENTER); // It's بديهيات يعتي
		title.setText("Anonybox R"); // Same comment as above
		serverBtn.setBackground(new java.awt.Color(66, 73, 255)); // Set the Button Color
		serverBtn.setText("I'm a User/Client"); // IF YOU ARE A CLIENT GO HERE YOU STUPID ASS !
		serverBtn.addActionListener(new java.awt.event.ActionListener() { // What if you clicked on this?
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				serverBtnActionPerformed(evt);
			}
		});
		clientBtn.setBackground(new java.awt.Color(0, 37, 255)); // Set the Button Color, Probably
		clientBtn.setText("I'm a Host/Server"); // Go host for your homies
		clientBtn.setName(""); // WHAT IN THE HELL IS THIS ?!
		clientBtn.addActionListener(new java.awt.event.ActionListener() { // Hmm, Let's go to the Server Activity
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				clientBtnActionPerformed(evt);
			}
		});
		cp.setForeground(new java.awt.Color(255, 51, 51)); // Uhhh, I remembered ! It's that copyright shitty thing
		cp.setHorizontalAlignment(javax.swing.SwingConstants.CENTER); // IT'S IN THE CENTER
		cp.setText("Anonybox R is made by Morad Abdelrasheed Mokhtar and protected under AnonyLicense"); // Pretending
																											// to be
																											// Facebook
		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane()); // ThE lAyOuT
		getContentPane().setLayout(layout); // SeT tHe LaYoUt
		layout.setHorizontalGroup( // OK IDEK WHAT'S THE IN THE MARS IS THIS SO IMMA SKIP THAT
				layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addComponent(title, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
								Short.MAX_VALUE)
						.addGroup(javax.swing.GroupLayout.Alignment.TRAILING,
								layout.createSequentialGroup().addGap(0, 79, Short.MAX_VALUE).addComponent(clientBtn)
										.addGap(194, 194, 194).addComponent(serverBtn).addGap(74, 74, 74))
						.addComponent(cp, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
								Short.MAX_VALUE));
		layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup().addComponent(title)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 38, Short.MAX_VALUE)
						.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(clientBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 31,
										javax.swing.GroupLayout.PREFERRED_SIZE)
								.addComponent(serverBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 31,
										javax.swing.GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 181, Short.MAX_VALUE)
						.addComponent(cp)));
		getAccessibleContext().setAccessibleDescription("");
		pack();
	}

	private void clientBtnActionPerformed(java.awt.event.ActionEvent evt) { // GO SEND SOME USELESS MESSAGES YOU LITTLE
																			// ASS !
		App.Server.main(new String[] { ("") });
		System.out.println("Server Application Initiated");
	}

	private void serverBtnActionPerformed(java.awt.event.ActionEvent evt) { // Hey king, you dropped this *Crown*
		App.Client.main(new String[] { ("") });
		System.out.println("Client Application Initiated");
	}

	public static void main(String args[]) {
		try {
			for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException ex) {
			java.util.logging.Logger.getLogger(MainApp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (InstantiationException ex) {
			java.util.logging.Logger.getLogger(MainApp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (IllegalAccessException ex) {
			java.util.logging.Logger.getLogger(MainApp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (javax.swing.UnsupportedLookAndFeelException ex) {
			java.util.logging.Logger.getLogger(MainApp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		}
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				new MainApp().setVisible(true);
			}
		});
	}

	private javax.swing.JButton clientBtn;
	private javax.swing.JLabel cp;
	private javax.swing.JButton serverBtn;
	private javax.swing.JLabel title;
}