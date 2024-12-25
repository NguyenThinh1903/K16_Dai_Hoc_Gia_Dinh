package serverSocket;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private ServerSocket serverSocket;
    private List<ClientHandler> clientHandlers;

    // Khởi tạo server
    public Server(int port) throws IOException {
        try {
            serverSocket = new ServerSocket(port);
            clientHandlers = new ArrayList<>();
            System.out.println("Máy chủ đã khởi động trên cổng: " + port);
        } catch (IOException e) {
            System.out.println("Lỗi rồi: " + e.getMessage());
        }
    }

    // Phương thức chạy server
    public void chay() {
        while (true) {
            try {
                System.out.println("Máy chủ: Đang chờ máy trạm kết nối...");
                Socket incomingSocket = serverSocket.accept();
                System.out.println("Máy chủ: Đã có máy trạm " + incomingSocket.getRemoteSocketAddress() + " kết nối.");

                // Tạo một luồng mới để xử lý client này
                ClientHandler clientHandler = new ClientHandler(incomingSocket, this);
                clientHandlers.add(clientHandler);
                new Thread(clientHandler).start();

            } catch (IOException e) {
                System.out.println("Lỗi rồi: " + e.getMessage());
            }
        }
    }

    // Phương thức gửi tin nhắn đến tất cả các client
    public void broadcastMessage(String message, ClientHandler excludeClient) {
        for (ClientHandler clientHandler : clientHandlers) {
            if (clientHandler != excludeClient) {
                clientHandler.sendMessage(message);
            }
        }
    }

    // Phương thức để xóa client khi kết nối bị đóng
    public void removeClient(ClientHandler clientHandler) {
        clientHandlers.remove(clientHandler);
        System.out.println("Đã xóa máy trạm " + clientHandler.getClientSocket().getRemoteSocketAddress());
    }

    public static void main(String[] args) {
        try {
            Server server = new Server(12345); // Khởi tạo server trên cổng 12345
            server.chay();
        } catch (IOException e) {
            System.out.println("Lỗi khi khởi tạo server: " + e.getMessage());
        }
    }
}

// Lớp xử lý từng client kết nối đến server
class ClientHandler implements Runnable {
    private Socket clientSocket;
    private Server server;
    private BufferedWriter writer;
    private BufferedReader reader;

    public ClientHandler(Socket clientSocket, Server server) {
        this.clientSocket = clientSocket;
        this.server = server;
        try {
            OutputStream out = clientSocket.getOutputStream();
            writer = new BufferedWriter(new OutputStreamWriter(out));

            InputStream in = clientSocket.getInputStream();
            reader = new BufferedReader(new InputStreamReader(in));
        } catch (IOException e) {
            System.out.println("Lỗi khi tạo luồng cho client: " + e.getMessage());
        }
    }

    @Override
    public void run() {
        try {
            // Đọc tin nhắn từ client và gửi đến các client khác
            String message;
            while ((message = reader.readLine()) != null) {
                System.out.println("Máy trạm " + clientSocket.getRemoteSocketAddress() + ": " + message);
                server.broadcastMessage("Máy trạm " + clientSocket.getRemoteSocketAddress() + ": " + message, this);
            }
        } catch (IOException e) {
            System.out.println("Lỗi khi đọc tin nhắn từ client: " + e.getMessage());
        } finally {
            // Đóng kết nối khi client ngắt kết nối
            try {
                clientSocket.close();
            } catch (IOException e) {
                System.out.println("Lỗi khi đóng kết nối client: " + e.getMessage());
            }
            server.removeClient(this);
        }
    }

    // Phương thức gửi tin nhắn đến client
    public void sendMessage(String message) {
        try {
            writer.write(message);
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            System.out.println("Lỗi khi gửi tin nhắn đến client: " + e.getMessage());
        }
    }

    public Socket getClientSocket() {
        return clientSocket;
    }
}
