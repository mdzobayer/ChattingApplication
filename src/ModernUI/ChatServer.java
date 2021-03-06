/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModernUI;

/**
 *
 * @author Zobayer
 */

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.swing.JOptionPane;

public class ChatServer {
    static Vector ClientSockets;
    static Vector LoginNames;
    
    ChatServer() throws IOException {
        ServerSocket server = new ServerSocket(10007);
        ClientSockets = new Vector();
        LoginNames = new Vector();
        
        while(true) {
            Socket client = server.accept();
            AcceptClient  acceptClient = new AcceptClient(client);
        }
    }
    
    public static void main(String [] args) throws IOException, UnknownHostException{
        ChatServer server = new ChatServer();
    }
    
    class AcceptClient extends Thread{
        Socket ClientSocket;
        DataInputStream din;
        DataOutputStream dout;
        AcceptClient(Socket client) throws IOException {
            ClientSocket = client;
            din = new DataInputStream(ClientSocket.getInputStream());
            dout = new DataOutputStream(ClientSocket.getOutputStream());
            //JOptionPane.showMessageDialog(null, "New Client");
            String LoginName = din.readUTF();
            
            LoginNames.add(LoginName);
            ClientSockets.add(ClientSocket);
            
            
            start();
        }
        public void run() {
            //JOptionPane.showMessageDialog(null, "New user");
            while(true) {
                try {
                    String msgFromClient = din.readUTF();
                    StringTokenizer st = new StringTokenizer(msgFromClient);
                    String LoginName = st.nextToken();
                    String MsgType = st.nextToken();
                    String Destination = st.nextToken();
                    
                    int lo = -1;
                    
                    String msg = "";
                    while(st.hasMoreElements()) {
                        msg = msg + " " + st.nextToken();
                    }
                    
                    if (MsgType.equals("LOGIN")) {
                        for (int i = 0; i < LoginNames.size(); ++i) {
                            Socket pSocket = (Socket) ClientSockets.elementAt(i);
                            DataOutputStream pOut = new DataOutputStream (pSocket.getOutputStream());
                            pOut.writeUTF(LoginName + " has logged in.");
                        }
                    }
                    else if (MsgType.equals("LOGOUT")) {
                        for (int i = 0; i < LoginNames.size(); ++i) {
                            if (LoginName.equals(LoginNames.elementAt(i)))
                                lo = i;
                        
                            Socket pSocket = (Socket) ClientSockets.elementAt(i);
                            DataOutputStream pOut = new DataOutputStream (pSocket.getOutputStream());
                            pOut.writeUTF(LoginName + " has logged out.");
                            
                        }
                        if (lo >= 0) {
                            LoginNames.removeElementAt(lo);
                            ClientSockets.removeElementAt(lo);
                        }
                    }
                    else {
                        if (Destination.equals("GROUP")){
                            for (int i = 0; i < LoginNames.size(); ++i) {
                                Socket pSocket = (Socket) ClientSockets.elementAt(i);
                                DataOutputStream pOut = new DataOutputStream (pSocket.getOutputStream());
                                pOut.writeUTF(LoginName + " : " + msg);
                            }
                        }
                        else {
                            for (int i = 0; i < LoginNames.size(); ++i){
                                if (Destination.equals(LoginNames.elementAt(i))) {
                                    Socket pSocket = (Socket) ClientSockets.elementAt(i);
                                    DataOutputStream pOut = new DataOutputStream (pSocket.getOutputStream());
                                    pOut.writeUTF(LoginName + " : " + msg);
                                }
                                if (LoginName.equals(LoginNames.elementAt(i))) {
                                    Socket pSocket = (Socket) ClientSockets.elementAt(i);
                                    DataOutputStream pOut = new DataOutputStream (pSocket.getOutputStream());
                                    pOut.writeUTF(LoginName + " : " + msg);
                                }
                            }
                        }
                    }
                    if (MsgType.equals("LOGOUT")) 
                        break;
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
