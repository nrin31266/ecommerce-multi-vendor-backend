package com.vanrin05.controller;

import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.Invoice;
import com.stripe.model.PaymentIntent;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/stripe")
@Slf4j
public class StripeController {
    @PostMapping("/webhook")
    public ResponseEntity<String> handleStripeWebhook(@RequestBody String payload, @RequestHeader("Stripe-Signature") String signature) {
        try {

            Event event = Webhook.constructEvent(payload, signature,
                    "whsec_6AI2Ej0AWGYipywDWGVPr0lKz0ovD90w");
            switch (event.getType()) {
                case "payment_intent.succeeded":
                    handlePaymentIntentSucceeded((PaymentIntent) event.getDataObjectDeserializer().getObject().get());
                    break;

                case "checkout.session.completed":
                    handleCheckoutSessionCompleted((Session) event.getDataObjectDeserializer().getObject().get());
                    break;

                case "invoice.payment_failed":
                    handleInvoicePaymentFailed((Invoice) event.getDataObjectDeserializer().getObject().get());
                    break;

                default:
                    log.info("Default: {}", event.getType());
            }

            return ResponseEntity.ok("Webhook received successfully");

        } catch (SignatureVerificationException e) {
            log.error("Signature verification failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Webhook signature verification failed");
        }
    }
    private void handlePaymentIntentSucceeded(PaymentIntent paymentIntent) {
        //TODO
    }

    private void handleCheckoutSessionCompleted(Session session) {

        Long paymentId = Long.valueOf(session.getMetadata().get("payment_id"));

    }

    private void handleInvoicePaymentFailed(Invoice invoice) {
        log.info("❌ Error payment: {}", invoice.getId());

    }
}
