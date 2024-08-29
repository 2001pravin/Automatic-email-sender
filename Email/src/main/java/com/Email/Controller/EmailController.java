package com.Email.Controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.Email.Serviceses.EmailService;

import jakarta.mail.MessagingException;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;


@RestController
@RequestMapping("/api/email")
public class EmailController {
    @Autowired
    private EmailService emailService;
    
    @CrossOrigin(origins = "http://localhost:5173")
    @PostMapping("/send")
    public String SendEmail(@RequestParam("subject") String subject,
                            @RequestParam("body") String body,
                            @RequestParam("attachment") MultipartFile attachment,
                            @RequestParam("emailExcel") MultipartFile emailExcel) {
       
                                try {
                                    emailService.sendEmailWithAttachment(subject, body, attachment, emailExcel);
                                    return "Email sent Successfully!";
                                    } catch (MessagingException e) {
                                        return "Failed to send email: " + e.getMessage();
                                    } catch (IOException e) {
                                        return "Failed to process file: " + e.getMessage();
                                    }
        
    }
    

}
