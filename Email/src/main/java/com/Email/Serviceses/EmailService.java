package com.Email.Serviceses;

import java.io.IOException;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.mail.MessagingException;
public interface EmailService {

    void sendEmailWithAttachment( String subject, String body, MultipartFile attachment,MultipartFile emailExcel) throws MessagingException, IOException;

}
