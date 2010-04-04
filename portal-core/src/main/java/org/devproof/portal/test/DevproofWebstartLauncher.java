/*
 * Copyright 2009-2010 Carsten Hufe devproof.org
 * 
 * Licensed under the Apache License, Version 2.0 (the "License")
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 *   
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.devproof.portal.test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DevproofWebstartLauncher extends JFrame {
	private static final long serialVersionUID = 1L;

	private final JettyWebstart jettyWebstart;
	private JButton startButton;
	private JButton stopButton;

	public static void main(String[] args) {
		new DevproofWebstartLauncher();
	}

	public DevproofWebstartLauncher() {
		super("Devproof Portal");
		jettyWebstart = new JettyWebstart();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		add(new JLabel(" Devproof Portal"), BorderLayout.NORTH);
		StringBuilder buf = new StringBuilder();
		buf.append("\n");
		buf.append("  Start the browser and open URL: http://localhost:8888/\n");
		buf.append("  Username: admin     Password: 12345\n\n");
		buf.append("  When closing this window, the server will be stopped.\n\n");
		buf.append("  Download the portal: http://portal.devproof.org\n\n");
		buf.append("       --- devproof.org ---");

		JTextArea hint = new JTextArea(buf.toString());
		hint.setOpaque(false);
		hint.setEditable(false);
		add(hint, BorderLayout.CENTER);

		JPanel bottomButtons = new JPanel();
		bottomButtons.add(startButton = new JButton("Start Server"), BorderLayout.WEST);
		bottomButtons.add(stopButton = new JButton("Stop Server"), BorderLayout.EAST);
		add(bottomButtons, BorderLayout.SOUTH);
		setLocation(400, 300);
		setSize(400, 200);
		startButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				startServer();
			}
		});

		stopButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				stopServer();
			}
		});
		setVisible(true);
		startServer();
	}

	private void startServer() {
		startButton.setEnabled(false);
		stopButton.setEnabled(true);
		jettyWebstart.startServer(8888);
	}

	private void stopServer() {
		startButton.setEnabled(true);
		stopButton.setEnabled(false);
		jettyWebstart.stopServer();
	}
}
