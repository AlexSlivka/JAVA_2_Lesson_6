import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static final String IP_ADDRESS = "localhost";
    public static final int PORT = 8189;


    public static void main(String[] args) {
        Socket socket = null;
        Scanner scanner = new Scanner(System.in);

        try {
            socket = new Socket(IP_ADDRESS, PORT);
            System.out.println("Клиент подключен к серверу: " + socket.getRemoteSocketAddress());
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());

            Thread threadReader = new Thread(()->{
                try{
                    while(true){
                        out.writeUTF(scanner.nextLine());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            threadReader.setDaemon(true);
            threadReader.start();

            while(true){
                String str = in.readUTF();
                if (str.equals("/end")){
                    System.out.println("Потеряно соединение с сервером");
                    out.writeUTF("/end");
                    break;
                }
                else{
                    System.out.println("Сервер: " + str);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
             try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }


}
