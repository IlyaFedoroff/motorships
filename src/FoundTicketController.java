import classes.Ticket;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class FoundTicketController {

    @FXML
    private Button okButton;



    //@FXML
    //private Label ticketIdLabel;
    @FXML
    private Label ticketNumberLabel;
    @FXML
    private Label ticketPriceLabel;
    @FXML
    private Label dateDepartureLabel;
    @FXML
    private Label timeDepartureLabel;
    @FXML
    private Label dateOfSaleLabel;
    @FXML
    private Label cabinNumberLabel;
    @FXML
    private Label individualIdLabel;
    @FXML
    private Label laborContractIdLabel;
    @FXML
    private Label voyageIdLabel;
    @FXML
    private Label serviceIdLabel;
    @FXML
    private Label measureIdLabel;

    @FXML
    void closeWindow(ActionEvent event) {
        Stage stage = (Stage) okButton.getScene().getWindow();
        stage.close();
    }

    public void setTicket(Ticket ticket) {
        //ticketIdLabel.setText(String.valueOf(ticket.getId()));
        ticketNumberLabel.setText(ticket.getNumber());
        ticketPriceLabel.setText(String.valueOf(ticket.getPrice()));
        dateDepartureLabel.setText(ticket.getDateDeparture().toString());
        timeDepartureLabel.setText(ticket.getTimeDeparture().toString());
        dateOfSaleLabel.setText(ticket.getDateOfSale().toString());
        cabinNumberLabel.setText(String.valueOf(ticket.getCabinNumber()));
        individualIdLabel.setText(String.valueOf(ticket.getIndividualId()));
        laborContractIdLabel.setText(String.valueOf(ticket.getLaborContractId()));
        voyageIdLabel.setText(String.valueOf(ticket.getVoyageId()));
        serviceIdLabel.setText(String.valueOf(ticket.getServiceId()));
        measureIdLabel.setText(String.valueOf(ticket.getMeasureId()));
    }
}
