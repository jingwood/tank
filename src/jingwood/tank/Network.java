/*
 * Classic Tank Game
 *
 * MIT License
 * Copyright (c) 2004-2019 Jingwood, All right reserved.
 */

package jingwood.tank;

import java.net.*;
import java.io.*;

public class Network extends Thread {
	private ServerSocket sckListener;
	private Socket sckServer;
	private Socket sckConnector;
	private DataInputStream din;
	private DataOutputStream dout;

	// Status
	private int status = 0;
	public static final int ST_NOTHING = 0;
	public static final int ST_CONNECTING = 1;
	public static final int ST_CONNECTED = 2;
	public static final int ST_LISTENING = 3;
	public static final int ST_ACCEPTED = 4;

	public final int timeout = 4000;

	private final String S_VER = "ver";
	private final String S_VERSAME = "checkver=same";
	private final String S_VERDIFF = "checkver=diff";

	private final String C_VER = S_VER + "=" + WinMain.version;
	private final String C_VER_PARA = S_VERSAME.split("=")[0];
	private final String C_VER_SAME = S_VERSAME.split("=")[1];
	private final String C_VER_DIFF = S_VERDIFF.split("=")[1];

	public boolean P_VERSAME = false;

	public boolean E_VERDIFF = false;

	public Network() {
		super();
		start();
	}

	public void listen() {
		try {
			this.sckListener = new ServerSocket(4000);
			this.status = ST_LISTENING;
		} catch (IOException e) {
			this.status = ST_NOTHING;
		}
	}

	public void stoplisten() {
		try {
			this.sckListener.close();
			this.sckListener = null;
			this.status = ST_NOTHING;
		} catch (IOException ignore) {
		}
	}

	public void connect(String address) {
		this.status = ST_CONNECTING;
		try {
			this.sckConnector = new Socket(address, 4000);
			this.din = new DataInputStream(this.sckConnector.getInputStream());
			this.dout = new DataOutputStream(this.sckConnector.getOutputStream());
			this.status = ST_CONNECTED;
		} catch (IOException e) {
			this.status = ST_NOTHING;
		}
	}

	public void disconnect() {
		try {
			if (this.sckConnector != null) this.sckConnector.close();
			if (this.sckServer != null) this.sckServer.close();
			this.sckConnector = null;
			this.sckServer = null;
			this.din = null;
			this.dout = null;
			this.status = ST_NOTHING;
		} catch (IOException ignore) {
		}
	}

	public void run() {
		while (true) {
			switch (this.status) {
				case ST_NOTHING:
					break;
				case ST_CONNECTING:
					break;
				case ST_CONNECTED:
					client_send();
					System.out.println("waiting...");
					client_receive();
					break;
				case ST_LISTENING:
					try {
						this.sckServer = this.sckListener.accept();
						this.din = new DataInputStream(this.sckServer.getInputStream());
						this.dout = new DataOutputStream(this.sckServer.getOutputStream());
					} catch (IOException e) {
						listen();
					}
					this.status = ST_ACCEPTED;
					break;
				case ST_ACCEPTED:
					if (this.sckServer == null) {
						listen();
						break;
					}
					server_receive();
					break;
			}
			pause(100);
		}
	}

	public void pause(int value) {
		try {
			Thread.sleep(value);
		} catch (InterruptedException ignore) {
		}
	}

	public int getStatus() {
		return this.status;
	}

	public void client_send() {
		if (!P_VERSAME) send(this.C_VER);
	}

	public void client_receive() {
		String[] msg = receive();
		if (msg != null) {
			System.out.println(msg.length);
			for (int i = 0; i < msg.length; i++) {
				client_construe(msg[i]);
			}
		}
	}

	public void server_receive() {
		String[] msg = receive();
		if (msg == null) {
			for (int i = 0; i < msg.length; i++) {
				server_construe(msg[i]);
			}
		}
	}

	private void server_construe(String msg) {
		try {
			System.out.println("server construing " + msg + " ...");

			String para = msg.split("=")[0];
			String value = msg.split("=")[1];

			if (para == S_VER) {
				if (value == WinMain.version) {
					this.P_VERSAME = true;
					send(S_VERSAME);
					return;
				} else {
					send(S_VERDIFF);
					sckServer.close();
					this.sckServer = null;
					return;
				}
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	private void client_construe(String msg) {
		System.out.println("client construing " + msg + " ...");

		String para = msg.split("=")[0];
		String value = msg.split("=")[1];

		if (para == C_VER_PARA) {
			if (value == C_VER_SAME) {
				this.P_VERSAME = true;
				return;
			}
			if (value == C_VER_DIFF) {
				this.E_VERDIFF = true;
				disconnect();
				return;
			}
		}
	}

	public void send(String msg) {
		try {
			this.dout.writeUTF(msg + "\n");
		} catch (IOException e) {
			System.out.println(e);
			disconnected();
		}
	}

	public String[] receive() {
		try {
			return din.readUTF().split("\n");
		} catch (IOException e) {
			System.out.println(e);
			disconnected();
		}
		return null;
	}

	public void disconnected() {
		if (this.status == ST_ACCEPTED) {
			this.sckServer = null;
			this.status = ST_LISTENING;
		} else {
			this.disconnect();
		}
	}

}