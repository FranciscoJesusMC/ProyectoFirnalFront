package com.ecommerce.service;

import com.stripe.exception.StripeException;

public interface PaymentService {

    String createCheckoutSession(Long orderId) throws StripeException;

    void handleStripeWebhook(String payload,String sigHeader);


}
