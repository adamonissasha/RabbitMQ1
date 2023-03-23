import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.URISyntaxException;
import java.rmi.server.ExportException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

//сервер считывает сообщение на странице, создает ключ, который будет названием файла, записывает это сообщение
// в файл и посылает в очередь сообщение о том, что этот файл нужно заархивировать
// (в сообщении долен быть ключ - имя файла + сам текст файла.).
// Данный файл нужно заархивировать и хакинуть в папку с архивами.
//UUID
@WebServlet("/producer")
public class ProducerController extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try {
            ConnectionFactory connectionFactory = new ConnectionFactory();
            connectionFactory.setPort(15672);
            connectionFactory.setUri("amqp://guest:guest@localhost:15672/");
            Connection connection = connectionFactory.newConnection();
            Channel channel = connection.createChannel();

            String msg = req.getParameter("message");
            String key = UUID.randomUUID().toString();
            // WorkWithFile workWithFile = new WorkWithFile();
            // workWithFile.writeToFile(msg, key);

            channel.basicPublish("", "Queue-1", null, key.getBytes());

            channel.close();
            connection.close();
            resp.sendRedirect("index.jsp");
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }

    }
}