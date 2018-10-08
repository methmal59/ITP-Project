/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mail;

import com.sun.mail.util.MailSSLSocketFactory;
import java.io.File;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.util.Date;
import java.util.Properties;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 * FXML Controller class
 *
 * @author Bevan
 */
public class MailController implements Initializable {

    @FXML
    private TextField to;
    @FXML
    private TextField subject;
    @FXML
    private TextArea message;
    @FXML
    private Text FileName;
    private String path;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private File attach(ActionEvent event) {
    FileChooser fileChooser = new FileChooser();
		File selectedFile = fileChooser.showOpenDialog(null);

		if (selectedFile != null) {

			FileName.setText("File selected: " + selectedFile.getName());
                        path = selectedFile.getAbsolutePath();
		}
		else {
			FileName.setText("File selection cancelled.");
                        selectedFile = null;
		}
                return selectedFile;
    }
   

    @FXML
    private void email() throws GeneralSecurityException, AddressException, MessagingException{
        Properties props = new Properties();
        MailSSLSocketFactory sf = new MailSSLSocketFactory();
            sf.setTrustAllHosts(true);
            props.put("mail.imap.ssl.trust", "*");
            props.put("mail.imap.ssl.socketFactory", sf);
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable","true" );
            props.put("mail.smtp.host","smtp.1and1.com" );
            props.put("mail.smtp.port", "587" );
                
        final String username = "bevan@advantedgenow.com";
        final String password = "Bevan96076";

        Session session = Session.getDefaultInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
        Message msg = new MimeMessage(session);

         //-- Set the FROM and TO fields --
        msg.setFrom(new InternetAddress(username));
        msg.setRecipients(Message.RecipientType.TO,
                InternetAddress.parse(to.getText(), false));
        msg.setSubject(subject.getText());
        msg.setText(message.getText());
        
        
       
        BodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setText(message.getText());
        
        Multipart multipart = new MimeMultipart();
        
        multipart.addBodyPart(messageBodyPart);

         // Part two is attachment
        messageBodyPart = new MimeBodyPart();

        String filename = path;
        DataSource source = new FileDataSource(filename);
        messageBodyPart.setDataHandler(new DataHandler(source));
        messageBodyPart.setFileName(filename);
        multipart.addBodyPart(messageBodyPart);
        msg.setContent(multipart);
        msg.setSentDate(new Date());
        Transport.send(msg);

        System.out.println("Message sent.");
    }
    
}
