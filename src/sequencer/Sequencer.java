package sequencer;

import constants.Constants;

import java.io.IOException;
import java.net.*;
import java.util.HashMap;

public class Sequencer {
    public static void main(String[] args) throws IOException {
        DatagramSocket socket=new DatagramSocket(Constants.sequencerPort);
        System.out.println("Sequencer Started");
        while(true){
            requestHandler(socket);
        }

    }
    public static void requestHandler(DatagramSocket socket) throws IOException {
        //revieve a request from the frontend
        byte[] byteMessage=new byte[1024];
        DatagramPacket dataPacket=new DatagramPacket(byteMessage,byteMessage.length);
        socket.receive(dataPacket);
        String request=new String(dataPacket.getData()).trim();
        String reply="Message recieved by sequencer "+request;
        multicast(request);
    }
    public static void multicast(String request) throws IOException {
        DatagramSocket multicastSocket=new DatagramSocket();
        InetAddress group=InetAddress.getByName("230.0.0.0");
        byte[] messageToSend=new byte[1024];
        messageToSend=request.getBytes();
        DatagramPacket packet=new DatagramPacket(messageToSend,messageToSend.length,group,Constants.multicastPort);
        multicastSocket.send(packet);
        multicastSocket.close();
    }
}
