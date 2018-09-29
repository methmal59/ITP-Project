/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package invoice.ui.invoice.main;

import com.jfoenix.controls.JFXTextField;
import com.jfoenix.effects.JFXDepthManager;
import invoice.DataBaseHandler.DataBaseHandler;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * FXML Controller class
 *
 * @author Bevan
 */
public class MainController implements Initializable {

    @FXML
    private HBox pay01;
    @FXML
    private HBox handOver1;
    @FXML
    private HBox pay02;
    @FXML
    private Label inde;
    @FXML
    private Label cid;
    @FXML
    private Label tam;
    @FXML
    private Label ta;
    @FXML
    private Label dd;
    @FXML
    private HBox handOver2;
    @FXML
    private JFXTextField sperson;
    @FXML
    private JFXTextField nic;
    @FXML
    private JFXTextField invoiceId;
    @FXML
    private JFXTextField dueid;
    @FXML
    private JFXTextField payInvoiceId;
    @FXML
    private JFXTextField pamount;
    @FXML
    private Label inde1;
    @FXML
    private Label cid1;
    @FXML
    private Label tam1;
    @FXML
    private Label ta1;
    @FXML
    private Label dd1;
    @FXML
    private Label amo;
    @FXML
    private Label rp;
    @FXML
    private Label nd;

    private DataBaseHandler handler;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        JFXDepthManager.setDepth(handOver1, 1);
        JFXDepthManager.setDepth(handOver2, 1);
        JFXDepthManager.setDepth(pay02, 1);
        JFXDepthManager.setDepth(pay01, 1);
        handler = DataBaseHandler.getInstance();
        // TODO
    } 
    @FXML
    private void loadAddInvoice(ActionEvent event) {
        loadWindow("/invoice/ui/invoice/view/invoiceView.fxml","Add New Invoice");
    }
    
    @FXML
    private void loadListInvoice(ActionEvent event) {
        loadWindow("/invoice/ui/invoice/listView/listInvoice.fxml","Add New Invoice");
    }
    
    private void loadWindow(String loc, String title){
        try{
            Parent parent = FXMLLoader.load(getClass().getResource(loc));
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle(title);
            stage.setScene(new Scene(parent));
            stage.show();
        }catch(IOException ex){
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }

    @FXML
    private void handoverSearch(ActionEvent event) {
        String id = invoiceId.getText();
        String qu = "SELECT * FROM INVOICE Where InId = '"+ id +"'" ;
        ResultSet rs = handler.execQuery(qu);
        Boolean flag = false;    
        try {
            while(rs.next()){
                inde.setText(rs.getString("InId"));
                cid.setText(rs.getString("cutomerID"));
                tam.setText(String.valueOf(rs.getDouble("taxAmount")));
                ta.setText(String.valueOf(rs.getDouble("total")));
                dd.setText(rs.getString("duedate")); 
                flag = true;
            }
            if(!flag ){
                inde.setText("No Such Invoice avaliable");
            }
        } catch (SQLException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void DueInvoiceSearch(ActionEvent event) {
    }

    @FXML
    private void payableinvoiceSearch(ActionEvent event) {
        String id = payInvoiceId.getText();
        String qu = "SELECT * FROM INVOICE Where InId = '"+ id +"'" ;
        ResultSet rs = handler.execQuery(qu);
        Boolean flag = false;    
        try {
            while(rs.next()){
                inde1.setText(rs.getString("InId"));
                cid1.setText(rs.getString("cutomerID"));
                tam1.setText(String.valueOf(rs.getDouble("taxAmount")));
                ta1.setText(String.valueOf(rs.getDouble("total")));
                dd1.setText(rs.getString("duedate")); 
                flag = true;
            }
            if(!flag ){
                inde.setText("No Such Invoice avaliable");
            }
        } catch (SQLException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void amountCalculate(ActionEvent event) {
    }
    
    
    
}
