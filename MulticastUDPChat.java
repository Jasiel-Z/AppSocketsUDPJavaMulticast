import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.util.Scanner;



public class MulticastUDPChat {
    
    public static String nombreCliente;
    public static volatile boolean haDejadoChat;

    @SuppressWarnings("deprecation")
    public static void main(String[] args){

        try{
            int puerto = 9000;
            InetAddress grupo = InetAddress.getByName("224.0.0.0");
            MulticastSocket socket = new MulticastSocket(puerto);
            
            Scanner scan = new Scanner(System.in);
            System.out.println("Ingrese su nombre: ");
            nombreCliente  = scan.nextLine();
            socket.joinGroup(grupo);

            Thread hiloChat = new Thread(new HiloChat(socket, grupo, puerto));
            hiloChat.start();


            System.out.println("Ahora puede enviar mensaje");

            byte[] buffer = new byte[1024]; 
            String linea;

            while (true) {
                linea = scan.nextLine();
                if(linea.equalsIgnoreCase("Adios")){
                    haDejadoChat = true;
                    linea = nombreCliente + ": ha cerrado el chat";
                    buffer = linea.getBytes();
                    DatagramPacket mensajeSalida = new DatagramPacket(buffer,buffer.length, grupo ,puerto);
                    socket.send(mensajeSalida);
                    socket.leaveGroup(grupo);
                    socket.close();
                }

                linea = nombreCliente + ": " + linea;
                buffer = linea.getBytes();
                DatagramPacket mensajeSalida2 = new DatagramPacket(buffer,buffer.length, grupo ,puerto);
                socket.send(mensajeSalida2);
            }
            
        }catch (SocketException exception) {
            System.err.println("Socket: "+  exception.getMessage());
        }catch (IOException exception) {
            System.err.println(exception.getMessage());
        }
    }




    

}













