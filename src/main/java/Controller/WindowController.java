package Controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class WindowController implements Initializable {
    @FXML
    private ComboBox<String> currencyOptions;
    @FXML
    private DatePicker startDatePicker;
    @FXML
    private DatePicker endDatePicker;
    private String currencies[] = {"AUD", "CAD", "CZK", "DKK", "EUR", "HUF", "NOK",  "GBP", "SEK", "CHF","USD", "JPY", "XDR"};
    @FXML
    private Label avgBid;
    @FXML
    private Label standardAskDeviation;
    @FXML
    private Label errorLog;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        currencyOptions.setItems(FXCollections.observableArrayList(currencies));
        currencyOptions.setValue(currencies[0]);
    }

    private String getCurrencyKey() {
        return currencyOptions.getValue();
    }

    private String getStartDate() {
        LocalDate startDate = startDatePicker.getValue();
        if (startDate.isBefore(LocalDate.of(2002, 1, 1))) {
            return "err";
        } else {
            return startDate.toString();
        }
    }

    private String getEndDate() {
        LocalDate endDate = endDatePicker.getValue();
        if (endDate.isAfter(LocalDate.now())) {
            return "err";
        } else {
            return endDate.toString();
        }
    }

    @FXML
    public void onSubmitClicked() {
        if (getStartDate().equals("err")) {
            errorLog.setText("Error: Starting date is invalid");
        } else if (getEndDate().equals("err")) {
            errorLog.setText("Error: Ending date is invalid");
        } else if ((LocalDate.parse(getEndDate()).toEpochDay()) - LocalDate.parse(getStartDate()).toEpochDay() > 367) {
            errorLog.setText("Error: Date range is longer than 367 days");
            System.out.println((LocalDate.parse(getEndDate()).toEpochDay()) - LocalDate.parse(getStartDate()).toEpochDay());
        } else {
            QueryBuilder queryBuilder = new QueryBuilder(getCurrencyKey(), getStartDate(), getEndDate());
            DataFetcher dataFetcher = new DataFetcher(queryBuilder.getFullQuery());
            DecimalFormat decimalFormat = new DecimalFormat("#.####");
            avgBid.setText("Sredni kurs kupna: " + decimalFormat.format(dataFetcher.getAverageBid()));
            standardAskDeviation.setText("Odchylenie standardowe kursów sprzedaży: " + decimalFormat.format(dataFetcher.getStandardAskDeviation()));
            errorLog.setText("Data received");
        }
    }
    @FXML
    private void exit(){
        System.exit(0);
    }
    @FXML
    private void about(){
        errorLog.setText("Recruitment task for Currenda");
    }
    @FXML
    private void contact(){
        errorLog.setText("Email: mak.zieli@gmail.com    phone: 696914624");
    }
}
