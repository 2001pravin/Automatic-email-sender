// package com.Email.Serviceses;

// import java.io.IOException;
// import java.util.ArrayList;
// import java.util.List;
// import java.util.regex.Pattern;

// import org.apache.poi.ss.usermodel.Sheet;
// import org.apache.poi.ss.usermodel.Row;
// import org.apache.poi.ss.usermodel.Workbook;
// import org.apache.poi.xssf.usermodel.XSSFWorkbook;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.mail.javamail.JavaMailSender;
// import org.springframework.mail.javamail.MimeMessageHelper;
// import org.springframework.stereotype.Service;
// import org.springframework.web.multipart.MultipartFile;

// import jakarta.mail.MessagingException;
// import jakarta.mail.internet.MimeMessage;

// @Service
// public class EmailServiesImpl implements EmailService {

//     @Autowired
//     private JavaMailSender javaMailSender;

//     private static final Pattern EMAIL_REGEX = 
//         Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

//     @Override
//     public void sendEmailWithAttachment(String subject, String body, MultipartFile attachment, MultipartFile emailExcel) 
//             throws MessagingException, IOException {
       
//         List<String> emailAddress = extractEmailAddresses(emailExcel);
//         String[] toEmails = emailAddress.toArray(new String[0]);
       
//         MimeMessage mimeMessage = javaMailSender.createMimeMessage();
//         MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
//         mimeMessageHelper.setTo(toEmails);
//         mimeMessageHelper.setSubject(subject);
//         mimeMessageHelper.setText(body);
//         mimeMessageHelper.setFrom("pravin18092001@gmail.com");

//         if (attachment != null && !attachment.isEmpty()) {
//             mimeMessageHelper.addAttachment(attachment.getOriginalFilename(), attachment);
//         }
//         javaMailSender.send(mimeMessage);
//     }

//     // private List<String> extractEmailAddresses(MultipartFile emailExcel) throws IOException {
//     //     List<String> emailAddresses = new ArrayList<>();

//     //     try (Workbook workbook = new XSSFWorkbook(emailExcel.getInputStream())) {
//     //         Sheet sheet = workbook.getSheetAt(0);

//     //         for (Row row : sheet) {
//     //             String email = row.getCell(0).getStringCellValue().trim();
//     //             if (isValidEmail(email)) {
//     //                 emailAddresses.add(email);
//     //             } else {
//     //                 // Log or handle invalid email address
//     //                 System.out.println("Invalid email address: " + email);
//     //             }
//     //         }
//     //     }
//     private List<String> extractEmailAddresses(MultipartFile emailExcel) throws IOException {
//         List<String> emailAddresses = new ArrayList<>();

//         try (Workbook workbook = new XSSFWorkbook(emailExcel.getInputStream())) {
//             Sheet sheet = workbook.getSheetAt(0);

//             for (Row row : sheet) {
//                 String email = row.getCell(0).getStringCellValue().trim();
//                 if (isValidEmail(email)) {
//                     emailAddresses.add(email);
//                 } else {
//                     // Log invalid email address
//                     // logger.warn("Invalid email address found: {}", email);
//                     System.out.println("Invalid email address: " + email);
//                 }
//             }
//         }
//         return emailAddresses;
//     }

//     private boolean isValidEmail(String email) {
//         return EMAIL_REGEX.matcher(email).matches();
//     }
// }
package com.Email.Serviceses;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailServiesImpl implements EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailServiesImpl.class);

    @Autowired
    private JavaMailSender javaMailSender;

    private static final Pattern EMAIL_REGEX = 
        Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    @Override
    public void sendEmailWithAttachment(String subject, String body, MultipartFile attachment, MultipartFile emailExcel) 
            throws MessagingException, IOException {

        List<String> emailAddresses = extractEmailAddresses(emailExcel);
        if (emailAddresses.isEmpty()) {
            logger.warn("No valid email addresses found in the Excel file.");
            return; 
        }

        for (String recipientEmail : emailAddresses) {
            sendIndividualEmail(subject, body, attachment, recipientEmail);
            logger.info("email is send");
        }
    }

    private void sendIndividualEmail(String subject, String body, MultipartFile attachment, String recipientEmail) 
            throws MessagingException {
        
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
        mimeMessageHelper.setTo(recipientEmail);
        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.setText(body);
        mimeMessageHelper.setFrom("pravin18092001@gmail.com");

        if (attachment != null && !attachment.isEmpty()) {
            mimeMessageHelper.addAttachment(attachment.getOriginalFilename(), attachment);
        }

        try {
            javaMailSender.send(mimeMessage);
            logger.info("Email sent successfully to {}", recipientEmail);
        } catch (Exception e) {
            logger.error("Error sending email to {}: {}", recipientEmail, e.getMessage());
            throw new MessagingException("Error sending email", e);
        }
    }

    private List<String> extractEmailAddresses(MultipartFile emailExcel) throws IOException {
        List<String> emailAddresses = new ArrayList<>();

        try (Workbook workbook = new XSSFWorkbook(emailExcel.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                String email = row.getCell(0).getStringCellValue().trim();
                if (isValidEmail(email)) {
                    emailAddresses.add(email);
                } else {
                    // Log invalid email address
                    logger.warn("Invalid email address found: {}", email);
                }
            }
        }
        return emailAddresses;
    }

    private boolean isValidEmail(String email) {
        return EMAIL_REGEX.matcher(email).matches();
    }
}

