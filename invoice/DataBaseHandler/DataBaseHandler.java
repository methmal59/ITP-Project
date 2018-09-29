/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package invoice.DataBaseHandler;

/**
 *
 * @author Bevan
 */
import invoice.ui.invoice.view.InvoiceViewController.item;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Alert;

public final class DataBaseHandler {
    private static DataBaseHandler handler = null;	
    private static final String DB_URL = "jdbc:mysql://35.200.129.195/dream";
    private static Connection conn = null;
    private static Statement stmt = null;
	
    private DataBaseHandler() {
        crateConnection();
        setupInvoiceTable();
        setupItemTable();
    }
    public void droptables(){
        String drop1 = "DROP TABLE INVOICE";
        String drop2 = "DROP TABLE ITEM";
        execAction(drop2);
        execAction(drop1);
    }
    
    public static DataBaseHandler getInstance(){
        if(handler == null){
            handler = new DataBaseHandler();
        }
        return handler;
    }
	
    void crateConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            conn = DriverManager.getConnection(DB_URL, "dream","dream@123" );
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
	
  void setupInvoiceTable() {
        String TABLE_NAME = "INVOICE";
        try {
            stmt = conn.createStatement();

            DatabaseMetaData dbm = conn.getMetaData();
            ResultSet tables = dbm.getTables(null, null, TABLE_NAME.toUpperCase(), null);

            if( tables.next()) {
                    System.out.println("Table " + TABLE_NAME + " already exits. Ready for go!!!");
            }else {
                stmt.execute("CREATE Table " + TABLE_NAME + "("
                            + "InId varchar(200) PRIMARY KEY,\n"
                            + "description varchar(200),\n"
                            + "cutomerID varchar(200),\n"
                            + "duedate varchar(200),\n"
                            + "issueTime timestamp  default CURRENT_TIMESTAMP,\n"                            
                            + "subTotal double,\n"
                            + "taxP double,\n"
                            + "taxAmount double,\n"
                            + "total double\n"                            
                            + ")");  
            }    
        } catch(SQLException ex) {
            System.err.println(ex.getMessage() + "--- INVOICE Table");
        }finally {
        }
    }
    
    public void setupItemTable(){
        String TABLE_NAME = "ITEM";
        try {
            stmt = conn.createStatement();

            DatabaseMetaData dbm = conn.getMetaData();
            ResultSet tables = dbm.getTables(null, null, TABLE_NAME.toUpperCase(), null);

            if( tables.next()) {
                    System.out.println("Table " + TABLE_NAME + " already exits. Ready for go!!!");
            }else {
                stmt.execute("CREATE Table " + TABLE_NAME + "("
                            + "itemId varchar(200) PRIMARY KEY,\n"
                            + "invoiceId varchar(200),\n"
                            + "description varchar(200),\n"
                            + "quantity double,\n"
                            + "unitPrice double,\n"
                            + "lineTotal double,\n"                            
                            + "CONSTRAINT FK_invoiceId FOREIGN KEY (invoiceId) REFERENCES INVOICE(InId)\n"
                            + ")");                                 
            }    
        } catch(SQLException ex) {
            System.err.println(ex.getMessage() + "--- ITEM Table");
        }finally {
        }
    }
    
    public ResultSet execQuery(String query) {
        ResultSet result;
        try {
            stmt = conn.createStatement();
            result = stmt.executeQuery(query);
        }catch ( SQLException ex ) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Occured");
            alert.setHeaderText("Exception at execQuery:dataHandler");
            alert.setContentText(ex.getMessage());
            alert.showAndWait();
            return null;
        }finally {
        }
        return result;
    }
    
    public boolean execAction(String qu) {
        try {
            stmt = conn.createStatement();
            stmt.execute(qu);
            return true;
        }catch ( SQLException ex ){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Occured");
            alert.setHeaderText("Exception at execQuery:dataHandler");
            alert.setContentText(ex.getMessage());
            alert.showAndWait();
            return false;
        }finally {
        }
    }
    
    public boolean deleteItem(item Item){
        try {
            String deleteState = "DELETE FROM ITEM WHERE itemId = ?";
            PreparedStatement stmt1 = conn.prepareStatement(deleteState);
            stmt1.setString(1,Item.getItemID());
            int res = stmt1.executeUpdate();
            System.out.println(res);
            return true;    
        } catch (SQLException ex) {
            Logger.getLogger(DataBaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    public boolean updateItem(item Item){
        try {
            String update = "UPDATE ITEM SET description = ?, quantity = ?, unitPrice = ?, lineTotal = ? WHERE itemId = ?";
            PreparedStatement stmt1 = conn.prepareStatement(update);
            stmt1.setString(1,Item.getItemDescription());            
            stmt1.setDouble(2,Item.getItQuantity());
            stmt1.setDouble(3,Item.getItUnitPrice());
            stmt1.setDouble(4,Item.getItLineTotal());
            stmt1.setString(5,Item.getItemID());
            int res = stmt1.executeUpdate();
            return (res>0);
        } catch (SQLException ex) {
            Logger.getLogger(DataBaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }    
}
