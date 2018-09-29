package invoice.ui.invoice.listView;

import invoice.DataBaseHandler.DataBaseHandler;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class ListInvoiceController implements Initializable {

    ObservableList<invoice12> listinvoice = FXCollections.observableArrayList();
    @FXML
    private TableView<invoice12> invoiceView;
    @FXML
    private TableColumn<invoice12, String> inCol;
    @FXML
    private TableColumn<invoice12, String> desCol;
    @FXML
    private TableColumn<invoice12, String> cliCol;
    @FXML
    private TableColumn<invoice12, String> dueCol;
    @FXML
    private TableColumn<invoice12, String> totCol;
    @FXML
    private TableColumn<invoice12, Double> preparedtimeCol;

    DataBaseHandler handler;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        initCol();
        handler = DataBaseHandler.getInstance();
        loadinvoice();
    }    
    
    public void loadinvoice(){
        listinvoice.clear();
        String qu = "SELECT * FROM INVOICE" ;
        ResultSet rs = handler.execQuery(qu);
        try {
            while(rs.next()){
                String linvoiceId = rs.getString("InId");
                String ldescription = rs.getString("description");
                String lclientId = rs.getString("cutomerID");
                String ldurdate = rs.getString("duedate");
                Timestamp lpreparedTime = rs.getTimestamp("issueTime");
                double ltotal = rs.getDouble("total");
                listinvoice.add(new invoice12(linvoiceId, ldescription, lclientId, ldurdate, lpreparedTime.toGMTString(), ltotal));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ListInvoiceController.class.getName()).log(Level.SEVERE, null, ex);
        }
        invoiceView.setItems(listinvoice);
    }
    
    public void initCol(){
        inCol.setCellValueFactory(new PropertyValueFactory<>("invoiceId"));
        desCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        cliCol.setCellValueFactory(new PropertyValueFactory<>("clientId"));
        dueCol.setCellValueFactory(new PropertyValueFactory<>("durdate"));
        totCol.setCellValueFactory(new PropertyValueFactory<>("total"));
        preparedtimeCol.setCellValueFactory(new PropertyValueFactory<>("preparedTime"));
    }
    
    public static class invoice12{
        private final SimpleStringProperty invoiceId;
        private final SimpleStringProperty description;
        private final SimpleStringProperty clientId;
        private final SimpleStringProperty durdate;
        private final SimpleStringProperty preparedTime;
        private final SimpleDoubleProperty total;

        public String getInvoiceId() {
            return invoiceId.get();
        }

        public String getDescription() {
            return description.get();
        }

        public String getClientId() {
            return clientId.get();
        }

        public String getDurdate() {
            return durdate.get();
        }

        public String getPreparedTime() {
            return preparedTime.get();
        }

        public Double getTotal() {
            return total.get();
        }

        public invoice12(String invoiceId, String description, String clientId, String durdate, String preparedTime, Double total) {
            this.invoiceId = new SimpleStringProperty(invoiceId);
            this.description =  new SimpleStringProperty(description);
            this.clientId =  new SimpleStringProperty(clientId);
            this.durdate =  new SimpleStringProperty(durdate);
            this.preparedTime =  new SimpleStringProperty(preparedTime);
            this.total =  new SimpleDoubleProperty(total);
        }
    }
}
