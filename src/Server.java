import java.io.*;
import java.net.*;
import java.sql.Date;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Time;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.DriverManager;
import java.util.*;


import java.time.LocalDate;
import java.time.LocalTime;
import classes.*;

public class Server {
    private static final int PORT = 12345;
    private static List<ClientHandler> clients = new ArrayList<>();

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server is listening on port " + PORT);

            while (true) {
                Socket socket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(socket);
                clients.add(clientHandler); // Добавляем нового клиента в список
                clientHandler.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Метод для уведомления всех клиентов о необходимости обновления данных
    public static void notifyClientsToUpdate() {
        for (ClientHandler client : clients) {
            //System.out.println("Отправляем с сервера запрос на оповещение на обновление клиенту" + client.getName());
            client.notifyToUpdate();
        }
    }
}

class ClientHandler extends Thread {
    private Socket socket;
    private Connection connection;

    private ObjectInputStream input;
    private ObjectOutputStream output;


    public ClientHandler(Socket socket) {
        this.socket = socket;
        try {
            this.connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/22var", "root", "Lapka");

            this.output = new ObjectOutputStream(socket.getOutputStream());
            this.input = new ObjectInputStream(socket.getInputStream());
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            String command;
            while ((command = (String) input.readObject()) != null) {
                switch (command) {
                    case "ADD_TICKET":
                        addTicket(input, output);
                        break;
                    case "GET_TICKET":
                        getTicket(input, output);
                        break;
                    case "LOAD_TICKETS":
                        loadTickets(output);
                        break;
                    case "DELETE_TICKET":
                        deleteTicket(input, output);
                        break;
                    default:
                        System.out.println("Unknown command: " + command);
                        break;
                    // Add other cases for different commands
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Метод для обновления данных у клиента
    public void updateTickets() throws IOException {
        // Вызываем метод загрузки билетов, передавая ObjectOutputStream для отправки данных клиенту
        loadTickets(new ObjectOutputStream(socket.getOutputStream()));
    }


    public void notifyToUpdate() {
        try {
            output.writeObject("UPDATE_TICKETS");
            System.out.println("Послали клиенту " + this.getName() + " команду UPDATE_TICKETS");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void loadTickets(ObjectOutputStream output) throws IOException {
        try {
            //System.out.println("попали в получение билетов");
            String query = "SELECT * FROM ticket";
            PreparedStatement statement = connection.prepareStatement(query);

            ResultSet resultSet = statement.executeQuery();
            List<Ticket> tickets = new ArrayList<>();

            while (resultSet.next()) {
                int id = resultSet.getInt("idticket");
                String number = resultSet.getString("number");
                double price = resultSet.getDouble("price");
                LocalDate dateDeparture = resultSet.getDate("date_departure").toLocalDate();
                LocalTime timeDeparture = resultSet.getTime("time_departure").toLocalTime();
                LocalDate dateOfSale = resultSet.getDate("date_of_sale").toLocalDate();
                int cabinNumber = resultSet.getInt("cabinnumber");
                int individualId = resultSet.getInt("individual_idindividual");
                int laborContractId = resultSet.getInt("laborcontract_idlaborcontract");
                int voyageId = resultSet.getInt("voyage_idvoyage");
                int serviceId = resultSet.getInt("service_idservice");
                int measureId = resultSet.getInt("measure_idmeasure");

                Ticket ticket = new Ticket(id, number, price, dateDeparture, timeDeparture, dateOfSale, cabinNumber,
                                           individualId, laborContractId, voyageId, serviceId, measureId);
                tickets.add(ticket);
                //System.out.println("добавлен билет в список билетов");
            }

            // Отправляем список билетов клиенту в качестве ответа
            //output.writeObject("TICKETS_LIST");
            output.writeObject(tickets);
        } catch (SQLException e) {
            e.printStackTrace();
            // В случае ошибки отправляем пустой список клиенту
            output.writeObject(Collections.emptyList());
            output.flush();
        }
    }

    private void deleteTicket(ObjectInputStream input, ObjectOutputStream output) throws IOException {
        try {
            String ticketNumber = (String) input.readObject();
            String query = "DELETE FROM ticket WHERE number = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, ticketNumber);
            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                Server.notifyClientsToUpdate();
                output.writeObject("DELETE_TICKET_SUCCESS");
            } else {
                output.writeObject("DELETE_TICKET_NOT_FOUND");
            }
            output.flush();


        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            output.writeObject("DELETE_TICKET_ERROR"); // Пример ошибки удаления
            output.flush();
        }
    }


    private void addTicket(ObjectInputStream input, ObjectOutputStream output) throws IOException {
        //System.out.println("попали в добавление билета");

        try {


            Ticket ticket = (Ticket) input.readObject();

            String query = "INSERT INTO ticket (number, price, date_departure, time_departure, date_of_sale, cabinnumber, individual_idindividual, laborcontract_idlaborcontract, voyage_idvoyage, service_idservice, measure_idmeasure) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, ticket.getNumber());
            statement.setDouble(2, ticket.getPrice());
            statement.setDate(3, Date.valueOf(ticket.getDateDeparture()));
            statement.setTime(4, Time.valueOf(ticket.getTimeDeparture()));
            statement.setDate(5, Date.valueOf(ticket.getDateOfSale()));
            statement.setInt(6, ticket.getCabinNumber());
            statement.setInt(7, ticket.getIndividualId());
            statement.setInt(8, ticket.getLaborContractId());
            statement.setInt(9, ticket.getVoyageId());
            statement.setInt(10, ticket.getServiceId());
            statement.setInt(11, ticket.getMeasureId());

            statement.executeUpdate();

            //System.out.println("Отправляем запрос серверу на обновление данных у всех клиентов");
            Server.notifyClientsToUpdate();

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            output.writeObject("ADD_TICKET_ERROR"); // Пример ошибки добавления
            output.flush();
        }
    }

    private void getTicket(ObjectInputStream input, ObjectOutputStream output) throws IOException {
        // Implement the logic to retrieve a ticket from the database
        //System.out.println("попали в получение билета");

        try {
            String ticketNumber = (String) input.readObject();

            //System.out.println("полученный номер: " + ticketNumber);

            String query = "SELECT * FROM ticket WHERE number = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, ticketNumber);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int id = resultSet.getInt("idticket");
                String number = resultSet.getString("number");
                double price = resultSet.getDouble("price");
                LocalDate dateDeparture = resultSet.getDate("date_departure").toLocalDate();
                LocalTime timeDeparture = resultSet.getTime("time_departure").toLocalTime();
                LocalDate dateOfSale = resultSet.getDate("date_of_sale").toLocalDate();
                int cabinNumber = resultSet.getInt("cabinnumber");
                int individualId = resultSet.getInt("individual_idindividual");
                int laborContractId = resultSet.getInt("laborcontract_idlaborcontract");
                int voyageId = resultSet.getInt("voyage_idvoyage");
                int serviceId = resultSet.getInt("service_idservice");
                int measureId = resultSet.getInt("measure_idmeasure");

                Ticket ticket = new Ticket(id, number, price, dateDeparture, timeDeparture, dateOfSale, cabinNumber,
                                           individualId, laborContractId, voyageId, serviceId, measureId);

                output.writeObject(ticket);
            } else {
                output.writeObject(null);
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            output.writeObject(null);
        }

    }

}
