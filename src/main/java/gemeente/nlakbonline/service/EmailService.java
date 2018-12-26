package gemeente.nlakbonline.service;

import gemeente.authorization.api.Account;
import gemeente.nlakbonline.controller.ContentConfiguration;
import gemeente.nlakbonline.controller.Page;
import gemeente.nlakbonline.controller.model.BankAutomaticPaymentInformation;
import gemeente.nlakbonline.domain.AkbDonation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Value("${content.confirmation.from}")
    private String from;

    @Value("${content.confirmation.subject:Confirmation of your donation}")
    private String subject;

    @Autowired
    private ManualPaymentInformationConfiguration manualPaymentInformationConfiguration;

    @Autowired
    private AutomaticPaymentInformationConfiguration automaticPaymentInformationConfiguration;

    @Autowired
    private ContentConfiguration contentConfiguration;

    public void sendConfirmation(final Account account, final AkbDonation akbDonation) {
        final Context ctx = new Context();
        ctx.setVariable("account", account);
        ctx.setVariable("donation", akbDonation);
        ctx.setVariable("manualPaymentInformation", manualPaymentInformationConfiguration);

        final Page page = contentConfiguration.getById("give-bank-automatic").get();
        final String paymentReason = page.getTitle();
        ctx.setVariable("automaticPaymentInformation", createBankAutomaticPaymentInformation(account.getRegistrationReferenceId(), paymentReason));
        ctx.setVariable("replyTo", from);
        ctx.setVariable("footer", page.getFooter());
        sendMessage(account.getEmailAddress(), subject, "akb-email-confirmation", ctx);
    }

    void sendMessage(
            String to, String subject, String template, final Context context) {

        // Prepare message using a Spring helper
        final MimeMessage mimeMessage = this.emailSender.createMimeMessage();
        try {
            final MimeMessageHelper message =
                    new MimeMessageHelper(mimeMessage, true, "UTF-8"); // true = multipart
            message.setSubject(subject);
            message.setFrom(from);
            message.setTo(to);

            // Create the HTML body using Thymeleaf
            final String htmlContent = this.templateEngine.process(template, context);
            message.setText(htmlContent, true); // true = isHtml

            // Add the inline image, referenced from the HTML code as "cid:${imageResourceName}"
            // final InputStreamSource imageSource = new ByteArrayResource(imageBytes);
            // message.addInline(imageResourceName, imageSource, imageContentType);

            // Send mail
            this.emailSender.send(mimeMessage);
        }
        catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    BankAutomaticPaymentInformation createBankAutomaticPaymentInformation(final String mandateReference, final String paymentReason) {
        final BankAutomaticPaymentInformation result = new BankAutomaticPaymentInformation();
        result.setCreditorId(automaticPaymentInformationConfiguration.getCreditorId());
        result.setCreditorName(automaticPaymentInformationConfiguration.getCreditorName());
        result.setDayOfMonth(automaticPaymentInformationConfiguration.getDayOfMonth());
        result.setMandateReference(automaticPaymentInformationConfiguration.getMandateReferencePrefix() + mandateReference + automaticPaymentInformationConfiguration.getMandateReferencePostfix());
        result.setPaymentReason(paymentReason);
        return result;
    }
}
