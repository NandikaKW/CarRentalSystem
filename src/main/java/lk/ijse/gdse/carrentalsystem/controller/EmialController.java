package lk.ijse.gdse.carrentalsystem.controller;

import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class EmialController {

    @FXML
    private JFXTextArea txtBody;

    @FXML
    private JFXTextField txtSubject;

    private String customerEmail;

    public void setCustomerEmail(String email) {
        this.customerEmail = email;
    }

    @FXML
    void sendUsingGmailOnAction(ActionEvent event) {
        if (customerEmail == null || customerEmail.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Customer email is not set!").show();
            return;
        }

        final String FROM = "replace-your-email@gmail.com";
        final String PASSWORD = "replace-your-app-password";

        String subject = txtSubject.getText();
        String body = txtBody.getText();

        if (subject.isEmpty() || body.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Subject and body must not be empty!").show();
            return;
        }

        sendEmailWithGmail(FROM, customerEmail, PASSWORD, subject, body);
    }

    private void sendEmailWithGmail(String from, String to, String password, String subject, String messageBody) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setText(messageBody);
            Transport.send(message);

            new Alert(Alert.AlertType.INFORMATION, "Email sent successfully!").show();
        } catch (MessagingException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to send email.").show();
        }
    }
}
