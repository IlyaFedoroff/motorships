import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.*;

import classes.Ticket;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import java.util.*;
import java.time.LocalDate;
import java.time.LocalTime;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.DatePicker;
import java.util.stream.IntStream;

public class MainController {



    private ObjectOutputStream output;
    private ObjectInputStream input;


    // TICKET

    @FXML
    private TableView<Ticket> ticketTable;

    //@FXML
    //private TableColumn<Ticket, Number> idColumn;

    @FXML
    private TableColumn<Ticket, String> numberColumn;

    @FXML
    private TableColumn<Ticket, Double> priceColumn;

    @FXML
    private TableColumn<Ticket, String> dateDepartureColumn;

    @FXML
    private TableColumn<Ticket, String> timeDepartureColumn;

    @FXML
    private TableColumn<Ticket, String> dateOfSaleColumn;

    @FXML
    private TableColumn<Ticket, Integer> cabinNumberColumn;

    @FXML
    private Button getTicketButton;



    @FXML
    private TextField numberField;
    @FXML
    private TextField priceField;
    @FXML
    private TextField cabinNumberField;
    @FXML
    private TextField individualIdField;
    @FXML
    private TextField laborContractIdField;
    @FXML
    private TextField voyageIdField;
    @FXML
    private TextField serviceIdField;
    @FXML
    private TextField measureIdField;

    @FXML
    private DatePicker dateDeparturePicker;

    @FXML
    private DatePicker dateOfSalePicker;

    @FXML
    private ComboBox<Integer> hoursComboBox;

    @FXML
    private ComboBox<Integer> minutesComboBox;

    @FXML
    private Button deleteButton;



    @FXML
    private void handleDeleteButtonAction(ActionEvent event) {
        String ticketNumber = numberField.getText();
        if (ticketNumber != null && !ticketNumber.isEmpty()) {
            deleteTicket(ticketNumber);
        } else {
            System.out.println("Ticket number is empty");
        }
    }

    private void deleteTicket(String ticketNumber) {
        try {
            output.writeObject("DELETE_TICKET");
            output.writeObject(ticketNumber);

            // обновляем после успешного удаления
            //loadTickets();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void initialize() {
        //createConn();
        // server-client
        try {
            System.out.println("Инициализация контроллера");
            Socket socket = new Socket("localhost", 12345);
            output = new ObjectOutputStream(socket.getOutputStream());
            input = new ObjectInputStream(socket.getInputStream());

            // Запускаем поток для постоянного слушания ответов от сервера
            ServerListener listener = new ServerListener(input, this);
            listener.start();



        } catch (IOException e) {
            e.printStackTrace();
        }

        // Populate hours (0-23)
        IntStream.range(0, 24).forEach(hoursComboBox.getItems()::add);
        // Populate minutes (0-59)
        IntStream.range(0, 60).forEach(minutesComboBox.getItems()::add);

        // Set default values
        hoursComboBox.setValue(LocalTime.now().getHour());
        minutesComboBox.setValue(LocalTime.now().getMinute());


        // Инициализация столбцов таблицы
        //idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        numberColumn.setCellValueFactory(new PropertyValueFactory<>("number"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        dateDepartureColumn.setCellValueFactory(new PropertyValueFactory<>("dateDeparture"));
        timeDepartureColumn.setCellValueFactory(new PropertyValueFactory<>("timeDeparture"));
        dateOfSaleColumn.setCellValueFactory(new PropertyValueFactory<>("dateOfSale"));
        cabinNumberColumn.setCellValueFactory(new PropertyValueFactory<>("cabinNumber"));

        // Загрузка билетов при запуске
        loadTickets();
    }



    public void loadTickets() {
        try {
            output.writeObject("LOAD_TICKETS");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    @FXML
    void getTicket(ActionEvent event) {
        try {
            System.out.println("попали в получение билета");
            output.writeObject("GET_TICKET");
            output.writeObject(numberField.getText());


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void addTicket(ActionEvent event) {
        try {
            System.out.println("попали в добавление билета");
            output.writeObject("ADD_TICKET");

            String number = numberField.getText();
            double price = Double.parseDouble(priceField.getText());
            //LocalDate dateDeparture = LocalDate.parse(dateDepartureField.getText());
            LocalDate dateDeparture = dateDeparturePicker.getValue();
            int hour = hoursComboBox.getValue();
            int minute = minutesComboBox.getValue();
            LocalTime timeDeparture = LocalTime.of(hour, minute);
            //LocalTime timeDeparture = LocalTime.parse(timeDepartureField.getText());
            LocalDate dateOfSale = dateOfSalePicker.getValue();
            //LocalDate dateOfSale = LocalDate.parse(dateOfSaleField.getText());
            int cabinNumber = Integer.parseInt(cabinNumberField.getText());
            int individualId = Integer.parseInt(individualIdField.getText());
            int laborContractId = Integer.parseInt(laborContractIdField.getText());
            int voyageId = Integer.parseInt(voyageIdField.getText());
            int serviceId = Integer.parseInt(serviceIdField.getText());
            int measureId = Integer.parseInt(measureIdField.getText());

            Ticket ticket = new Ticket(0, number, price, dateDeparture, timeDeparture, dateOfSale, cabinNumber,
                                       individualId, laborContractId, voyageId, serviceId, measureId);

            output.writeObject(ticket);

            // Load tickets after successful addition
            //loadTickets();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void updateTicketsList(List<Ticket> tickets) {
        ObservableList<Ticket> ticketList = FXCollections.observableArrayList(tickets);
        ticketTable.setItems(ticketList);
    }

    public void showTicketDetails(Ticket ticket) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("resources/foundticket.fxml"));
            Parent root = loader.load();

            FoundTicketController controller = loader.getController();
            controller.setTicket(ticket);

            Stage stage = new Stage();
            stage.setTitle("Найденный билет");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
