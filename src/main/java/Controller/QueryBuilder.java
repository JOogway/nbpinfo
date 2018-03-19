package Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class QueryBuilder {
    @FXML
    private Label errorLog;

    private String baseOfQuery = "http://api.nbp.pl/api/exchangerates/rates/c/";
    private String fullQuery;

    QueryBuilder(String currency, String startDate, String endDate) {
        if (!startDate.equals("err")) {
            fullQuery = baseOfQuery + currency + "/" + startDate + "/" + endDate + "/" + "?format=json";
        } else {
            errorLog.setText("Something unexpected happened.");
        }
    }
    public String getFullQuery() {
        return fullQuery;
    }
}
