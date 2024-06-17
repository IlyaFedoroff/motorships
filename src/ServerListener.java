import java.io.IOException;
import java.io.ObjectInputStream;

import classes.Ticket;
import javafx.application.Platform;

import java.util.List;

class ServerListener extends Thread {
    private ObjectInputStream input;
    private MainController controller;

    public ServerListener(ObjectInputStream input, MainController controller) {
        this.input = input;
        this.controller = controller;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Object response = input.readObject();
                if (response instanceof String) {
                    String message = (String) response;
                    handleServerResponse(message);
                } else if (response instanceof List) {
                    List<Ticket> tickets = (List<Ticket>) response;
                    controller.updateTicketsList(tickets);
                } else if (response instanceof Ticket) {
                    Ticket ticket = (Ticket) response;
                    Platform.runLater(() -> controller.showTicketDetails(ticket));
                } else {
                    System.err.println("Unexpected response type: " + response.getClass().getName());
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    private void handleServerResponse(String response) {
        Platform.runLater(() -> {
        if ("UPDATE_TICKETS".equals(response)) {
            // Load tickets again to reflect the changes
            controller.loadTickets();
        }
        });
    }
}
