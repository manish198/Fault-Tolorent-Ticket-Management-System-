package ReplicaManagerOne;

import constants.Constants;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.HashMap;

public class ReplicaManagerOne {
    HashMap<Integer,String> requestSequenceMap=new HashMap<>();
    static int expectedSequence=0;
    public static void main(String[] args) throws IOException {
        while(true){
            System.out.println("Replica Manager One Started");
            recieveMulticstMessage();
        }
    }
    public static void recieveMulticstMessage() throws IOException {
        try{
            String recieved="";
            MulticastSocket multicastSocket=new MulticastSocket(Constants.multicastPort);
            byte[] recieveMessage=new byte[1024];
            InetAddress group=InetAddress.getByName("230.0.0.0");
            multicastSocket.joinGroup(group);
            while(true){
                //Recieve the request from the sequencer
                DatagramPacket packet=new DatagramPacket(recieveMessage,recieveMessage.length);
                multicastSocket.receive(packet);
                recieved=new String(packet.getData(),0,packet.getLength()).trim().toString();
                System.out.println("Repica One recieved... "+recieved);
                if("end".equals(recieved)) {
                    break;
                }
                System.out.println(recieved);

                //Send Response to the frontend
                String toFrontEnd=recieved+" Message From the Replica Manager 1. ";
                DatagramSocket toFrontEndSocket=new DatagramSocket();
                byte[] byteMessage=toFrontEnd.getBytes();
                InetAddress ia=InetAddress.getLocalHost();
                DatagramPacket packetToFrontend=new DatagramPacket(byteMessage,byteMessage.length,ia,Constants.listenReplicaOnePort);
                toFrontEndSocket.send(packetToFrontend);

                //Recieve the update from the Frontend
            }
            multicastSocket.leaveGroup(group);
            multicastSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
