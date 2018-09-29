package invoice.ui.invoice.view;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.effects.JFXDepthManager;
import invoice.DataBaseHandler.DataBaseHandler;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class InvoiceViewController implements Initializable {

    ObservableList<item> list = FXCollections.observableArrayList();
    @FXML
    private TableView<item> itemView;
    @FXML
    private TableColumn<item, String> itemIdCol;
    @FXML
    private TableColumn<item, String> descriCol;
    @FXML
    private TableColumn<item, Double> quanCol;
    @FXML
    private TableColumn<item, Double> unitCol;
    @FXML
    private TableColumn<item, Double> lineCol;
    @FXML
    private Label cID;
    @FXML
    private Label cName;
    @FXML
    private Label cCompany;
    @FXML
    private Label cAddress;
    @FXML
    private Label cEmail;
    @FXML
    private Label iID;
    @FXML
    private Label iDes;
    @FXML
    private Label subTotal;
    @FXML
    private Label taxAmount;
    @FXML
    private Label total;
    @FXML
    private Label taxPacentage;
    @FXML
    private JFXTextField inId;
    @FXML
    private JFXTextField inDesc;
    @FXML
    private JFXDatePicker dueDate;
    @FXML
    private JFXTextField ciID;
    @FXML
    private JFXTextField taPa;
    @FXML
    private JFXTextField itemId;
    @FXML
    private JFXTextField itDesc;
    @FXML
    private JFXTextField itquan;
    @FXML
    private JFXTextField itUnit;
    @FXML
    private AnchorPane rootPane;
    DataBaseHandler handler;
    @FXML
    private VBox itemBox;
    invoie a1 = new invoie();
    @FXML
    private VBox invoiceBox;
    
    private Boolean inEdit = Boolean.FALSE;
   
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        JFXDepthManager.setDepth(itemBox, 1);
        JFXDepthManager.setDepth(invoiceBox, 2);
        handler = DataBaseHandler.getInstance();
        initemCol();      
        clearamount();
    }       
    
    @FXML
    private void addItem(ActionEvent event) {
        if(itemInputVaild()){
            String aitemId = itemId.getText();
            String aInvoiceId = inId.getText();
            String aDescripton = itDesc.getText();
            double aQuantity = Double.parseDouble(itquan.getText());
            double aUnitPrice = Double.parseDouble(itUnit.getText());
            double aLineTotal = aQuantity * aUnitPrice;
                    
            String qu = "INSERT INTO ITEM VALUES(" +
                    "'" + aitemId + "'," + 
                    "'" + aInvoiceId + "'," + 
                    "'" + aDescripton + "'," + 
                    "" + aQuantity + "," + 
                    "" + aUnitPrice + "," +
                    "" + aLineTotal + "" +
                    ")";
            System.out.println(qu);
            if(handler.execAction(qu)) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText(null);
                alert.setContentText("suceess");
                alert.showAndWait();
                aftereditui();
            }else { //Error
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setContentText("Failed");
                alert.showAndWait();
                return;
            }
            loadItem();
        }
    }

    @FXML
    public void cancel(ActionEvent event) {
        String qu1 = "DELETE FROM ITEM WHERE invoiceId = '" + a1.getInid() + "'";
        handler.execAction(qu1);
        String qu = "DELETE FROM INVOICE WHERE Inid = '" + a1.getInid() + "'";
        handler.execAction(qu);
        Stage stage = (Stage)rootPane.getScene().getWindow();
        stage.close();
        
    }

    @FXML
    private void amountCalculate(ActionEvent event) {
        double subtot = 0;
        double taxp = Double.parseDouble(taPa.getText());
        String qu = "SELECT lineTotal FROM ITEM WHERE invoiceId = '"+ inId.getText() + "'";
        ResultSet rs = handler.execQuery(qu);
        try {    
            while(rs.next()){
                subtot += rs.getDouble("lineTotal");
            }
        } catch (SQLException ex) {
            Logger.getLogger(InvoiceViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
        a1.setItPac(taxp);
        a1.setIsTotal(subtot);
        a1.setItAmount(subtot * taxp / 100);
        a1.setItotal(a1.getIsTotal() + a1.getItAmount());
        
        taxPacentage.setText(String.valueOf(a1.getItPac()));
        subTotal.setText(String.valueOf(a1.getIsTotal()));
        taxAmount.setText(String.valueOf(a1.getItAmount()));
        total.setText(String.valueOf(a1.getItotal()));
    }
    
    @FXML
    public void pri(){
        String qu = "INSERT INTO INVOICE(InId) VALUES('" + inId.getText() +"')";
        handler.execAction(qu); 
        qu= "SELECT INID FROM INVOICE WHERE InId = '"+inId.getText()+"'" ;
        ResultSet rs = handler.execQuery(qu);
        try{
            while(rs.next()){
                a1.setInid(rs.getString("InId"));
            }                
        }catch(SQLException ex){
            Logger.getLogger(InvoiceViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println(a1.getInid());
        iID.setText(a1.getInid());
    }

    @FXML
    private void addInvoice(ActionEvent event) {
        if(invoiceInputVaild()){
            String aiId = inId.getText();
            String aiDes = inDesc.getText();
            String aidDate = ((TextField)dueDate.getEditor()).getText();
            String aincId = ciID.getText();

            String qu = "UPDATE INVOICE \n"
//                    + "SET InId = '" + aiId + "'," 
                    + "SET description = '" + aiDes + "',"
                    + "cutomerID = '"   + aincId + "',"
                    + "duedate = '"     + aidDate + "',"
                    + "subTotal = "    + a1.getIsTotal() + ","
                    + "taxP = "        + a1.getItPac() + "," 
                    + "taxAmount = "   + a1.getItAmount() + ","
                    + "total = "       + a1.getItotal()+ "\n"
                    + "WHERE InId = '"      + aiId + "'" ;
            System.out.println(qu);
                if(handler.execAction(qu)) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText(null);
                    alert.setContentText("suceess");
                    alert.showAndWait();
                    Stage stage = (Stage)rootPane.getScene().getWindow();
                    stage.close(); 
                }else { //Error
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText(null);
                    alert.setContentText("Failed");
                    alert.showAndWait();  
                }
        }
    }

    @FXML
    private void addCustomer(ActionEvent event) {
        clearCustomerCache();
        String id = ciID.getText();
        String qu = "SELECT * FROM CUSTOMER WHERE id = '" + id + "'";
        ResultSet rs = handler.execQuery(qu);
        Boolean flag = false;
        try{
            while(rs.next()){
                String acName = rs.getString("title"); 
                String acCompany = rs.getString("author");
                String acAddress = rs.getString("isAvail");
                String acEmail = rs.getString("isAvail");
                int acPhone = rs.getInt("");
                flag = true;
                cID.setText(acName);
                cName.setText(acCompany);
                cCompany.setText(acAddress);
                cEmail.setText(acEmail);
                
            }
            if(!flag){
                cID.setText("No such Client available");                
            }
        }catch(SQLException ex){
            Logger.getLogger(InvoiceViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void deleteItem(ActionEvent event) {
        item stod = itemView.getSelectionModel().getSelectedItem();
        
        if(stod == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("No Selected Item");
            alert.setHeaderText("Please Select a Item for delete");
            alert.showAndWait();
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Invaild Fields");
        alert.setContentText("Are you sure you want to delete this Item" + stod.getItemDescription() + " ?" );
        Optional<ButtonType> answer = alert.showAndWait();
        if(answer.get() == ButtonType.OK){
            Boolean result = DataBaseHandler.getInstance().deleteItem(stod);
            
            if(result){
                Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                alert1.setTitle("Item Deleted");
                alert1.setContentText("Item " + stod.getItemID() +"was Deleted");
                alert1.showAndWait();
                list.remove(stod);
            }else{
                Alert alert1 = new Alert(Alert.AlertType.ERROR);
                alert1.setTitle("Failed");
                alert1.setContentText("some thing want wrong when deleting " + stod.getItemID() );
                alert1.showAndWait();
            }               
        }
        else{
            Alert alert1 = new Alert(Alert.AlertType.ERROR);
            alert1.setTitle("No Selected Item");
            alert1.setHeaderText("Please Select a Item for delete");
            alert1.showAndWait();
        }
    }

    @FXML
    private void editItem(ActionEvent event) {
        item etod = itemView.getSelectionModel().getSelectedItem();        
        if(etod == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("No Selected EDIT");
            alert.setHeaderText("Please Select a Item for eDiT");
            alert.showAndWait();
            return;
        }       
        editui(etod);
    }
    
    private void initemCol(){
        itemIdCol.setCellValueFactory(new PropertyValueFactory<>("itemID"));
        descriCol.setCellValueFactory(new PropertyValueFactory<>("itemDescription"));
        quanCol.setCellValueFactory(new PropertyValueFactory<>("itQuantity"));
        unitCol.setCellValueFactory(new PropertyValueFactory<>("itUnitPrice"));
        lineCol.setCellValueFactory(new PropertyValueFactory<>("itLineTotal")); 
    }

    @FXML
    private void updateItem(ActionEvent event) {
        if(itemInputVaild()){
            String eitemId = itemId.getText();
            String eDescripton = itDesc.getText();
            double eQuantity = Double.parseDouble(itquan.getText());
            double eUnitPrice = Double.parseDouble(itUnit.getText());
            double eLineTotal = eQuantity * eUnitPrice;
            
            if(inEdit){
               item Item = new item(eitemId, eDescripton, eQuantity, eUnitPrice, eLineTotal);
                if(handler.updateItem(Item)){
                    Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                    alert1.setTitle("Item Deleted");
                    alert1.setContentText("Item " + itemId.getText() +"was Updated");
                    alert1.showAndWait();
                    loadItem();
                    aftereditui();
                }
                else{
                    Alert alert1 = new Alert(Alert.AlertType.ERROR);
                    alert1.setTitle("Item not Edited");
                    alert1.setContentText("something went wrong while editing " + itemId.getText());
                    alert1.showAndWait();
                }           
            }
        }
    }

    public static class invoie{
        private String inid;
        private double itPac;
        private double isTotal;
        private double itAmount;
        private double itotal;

        public void setInid(String inid) {
            this.inid = inid;
        }

        public String getInid() {
            return inid;
        }
        public double getItPac() {
            return itPac;
        }

        public double getIsTotal() {
            return isTotal;
        }

        public double getItAmount() {
            return itAmount;
        }

        public double getItotal() {
            return itotal;
        }
        public void setItPac(double itPac) {
            this.itPac = itPac;
        }

        public void setIsTotal(double isTotal) {
            this.isTotal = isTotal;
        }

        public void setItAmount(double itAmount) {
            this.itAmount = itAmount;
        }

        public void setItotal(double itotal) {
            this.itotal = itotal;
        }
        
        
    }
                       
    public static class item{
        private final SimpleStringProperty itemID;
        private final SimpleStringProperty itemDescription;
        private final SimpleDoubleProperty itQuantity;
        private final SimpleDoubleProperty itUnitPrice;
        private final SimpleDoubleProperty itLineTotal;
        
        public item( String itemID, String itemDescription, double itQuantity, double itUnitPrice, double itLLineTotal){
            this.itemID = new SimpleStringProperty(itemID);
            this.itemDescription = new SimpleStringProperty(itemDescription);
            this.itQuantity = new SimpleDoubleProperty(itQuantity);
            this.itUnitPrice = new SimpleDoubleProperty(itUnitPrice);
            this.itLineTotal = new SimpleDoubleProperty(itLLineTotal);         
        }
        
        public String getItemID() {
            return itemID.get();
        }

        public String getItemDescription() {
            return itemDescription.get();
        }

        public double getItQuantity() {
            return itQuantity.get();
        }

        public double getItUnitPrice() {
            return itUnitPrice.get();
        }

        public double getItLineTotal() {
            return itLineTotal.get();
        }  
    }
    
    public void editui(item Item){
        itemId.setText(Item.getItemID());
        itDesc.setText(Item.getItemDescription());
        itquan.setText(String.valueOf(Item.getItQuantity()));
        itUnit.setText(String.valueOf(Item.getItUnitPrice()));
        itemId.setEditable(false);
        inEdit = Boolean.TRUE;
    }
    
    public void aftereditui(){
        itemId.setText("");
        itDesc.setText("");
        itquan.setText("");
        itUnit.setText("");
        itemId.setEditable(true);
    }
    
    public void clearamount(){
        taxPacentage.setText("");
        subTotal.setText("");
        taxAmount.setText("");
        total.setText("");
    }
    
    public void clearCustomerCache(){
        cID.setText("");
        cName.setText("");
        cCompany.setText("");
        cAddress.setText("");
        cEmail.setText("");
    }      
    
    private boolean itemInputVaild(){
        String error= "";
        
        if( itemId.getText() == null || itemId.getText().length() == 0 )
            error += "No valid Item ID\n";
        if( inId.getText() == null || inId.getText().length() == 0 )
            error += "No valid Invoice ID\n";
        if( itDesc.getText() == null || itDesc.getText().length() == 0 )
            error += "No valid Item Descripton\n";
        if( itquan.getText() == null || itquan.getText().length() == 0 )
            error += "No valid Item Quantity\n";
        else{
            try{
                Double.parseDouble(itquan.getText());
            }catch(NumberFormatException e){
                 error += "Not a Vaild Quantity\n";
            }
        }
        if( itUnit.getText() == null || itUnit.getText().length() == 0 )
            error += "No valid Item UnitPrice\n";
        else{
            try{
                Double.parseDouble(itUnit.getText());
            }catch(NumberFormatException e){
                 error += "Not a Vaild UnitPrice\n";
            }
        }
        if(error.length() == 0)
            return true;
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invaild Fields");
            alert.setHeaderText("Please Correct the Following Fields");
            alert.setContentText(error);
            alert.showAndWait();
            return false;
        }
    }

    private boolean invoiceInputVaild(){
        String invoiceError = "";
        
        if( inId.getText() == null || inId.getText().length() == 0 )
            invoiceError += "No valid Invoice ID\n";
        if( inDesc.getText() == null || inDesc.getText().length() == 0 )
            invoiceError += "No valid Invoice Descripton\n";
        if( ciID.getText() == null || ciID.getText().length() == 0 )
            invoiceError += "No valid Cilent ID\n";
        if( taPa.getText() == null || taPa.getText().length() == 0 )
            invoiceError += "No valid Item taxPacentage\n";
        else{
            try{
                Double.parseDouble(taPa.getText());
            }catch(NumberFormatException e){
                 invoiceError += "Not a Vaild taxPacentagee\n";
            }
        }
        if(invoiceError.length() == 0)
            return true;
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invaild Fields");
            alert.setHeaderText("Please Correct the Following Fields");
            alert.setContentText(invoiceError);
            alert.showAndWait();
            return false;
        }
    }
    
    private  void loadItem(){
        list.clear();
        String qu = "SELECT * FROM ITEM WHERE invoiceId = '" + inId.getText() + "'" ;
        ResultSet rs = handler.execQuery(qu);
        try {
            while(rs.next()){
                String litemId = rs.getString("itemId");
                String lDescripton = rs.getString("description");
                double lQuantity = rs.getDouble("quantity");
                double lUnitPrice = rs.getDouble("unitPrice");
                double lLineTotal = rs.getDouble("lineTotal");                
                list.add(new item(litemId, lDescripton, lQuantity, lUnitPrice, lLineTotal));
            }
        } catch (SQLException ex) {
            Logger.getLogger(InvoiceViewController.class.getName()).log(Level.SEVERE, null, ex);
        }   
        itemView.setItems(list);
    }
}