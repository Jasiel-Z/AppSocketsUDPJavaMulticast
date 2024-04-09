import java.io.IOError;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class HiloChat implements Runnable{

    private MulticastSocket socket;
    private InetAddress grupo;
    private int puerto;


    public HiloChat(MulticastSocket socket, InetAddress grupo, int puerto ){
        this.socket = socket;
        this.grupo = grupo;
        this.puerto = puerto;
    }
    public void run(){
        byte[] buffer = new byte[1024];
        String linea;

        while (!MulticastUDPChat.haDejadoChat) {
            try{

                DatagramPacket mensajeEntrada = new DatagramPacket(buffer,0, buffer.length, grupo, puerto);
                socket.receive(mensajeEntrada);
                linea = new String(buffer, 0, mensajeEntrada.getLength());

                if(! linea.startsWith(MulticastUDPChat.nombreCliente)){
                    System.out.println(linea);
                }
            }catch(IOException exception){
                System.err.println("Comunicación y sockets cerrados");
            }
        }


    }
}
