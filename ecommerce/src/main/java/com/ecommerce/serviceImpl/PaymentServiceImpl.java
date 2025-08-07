package com.ecommerce.serviceImpl;

import com.ecommerce.entity.Order;
import com.ecommerce.entity.OrderProduct;
import com.ecommerce.entity.Product;
import com.ecommerce.exception.ResourceNotFoundException;
import com.ecommerce.repository.OrderRepository;
import com.ecommerce.repository.ProductRepository;
import com.ecommerce.service.EmailService;
import com.ecommerce.service.PaymentService;
import com.ecommerce.utils.RequestStatus;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    @Value("${stripe.api.key}")
    private String stripeKey;
    @Value("${stripe.webhook.secret}")
    private String stripeWebhookSecret;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final EmailService emailService;

    @Transactional
    @Override
    public String createCheckoutSession(Long orderId) throws StripeException {
        Stripe.apiKey = stripeKey;
        Order order = orderRepository.findById(orderId).orElseThrow(() ->
                new ResourceNotFoundException("Order not found with id:" + orderId));

        List<SessionCreateParams.LineItem> lineItems = new ArrayList<>();

        for (OrderProduct orderProduct : order.getOrderProducts()) {
            Product product = orderProduct.getProduct();

            SessionCreateParams.LineItem lineItem = SessionCreateParams.LineItem.builder()
                    .setQuantity((long) orderProduct.getQuantity())
                    .setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                            .setCurrency("usd")
                            .setUnitAmount((long) (product.getPrice() * 100))
                            .setProductData(SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                    .setName(product.getName())
                                    .build())
                            .build())
                    .build();

            lineItems.add(lineItem);
        }

        CustomerCreateParams customerParams = CustomerCreateParams.builder()
                .setEmail(order.getUser().getEmail()) // Email del cliente
                .setName(order.getUser().getName())  // Nombre del cliente
                .build();

        Customer customer = Customer.create(customerParams);

        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setCustomer(customer.getId()) // Usa el ID del cliente en lugar del email
                .setSuccessUrl("http://localhost:4200/success?session_id={CHECKOUT_SESSION_ID}&order_id=" + order.getId())
                .setCancelUrl("http://localhost:4200/cancel")
                .addAllLineItem(lineItems)
                .putMetadata("order_id", String.valueOf(order.getId()))
                .build();

        Session session = Session.create(params);
        return session.getUrl();
    }

    @Override
    public void handleStripeWebhook(String payload, String sigHeader) {
        try {
            Event event = Webhook.constructEvent(payload, sigHeader, stripeWebhookSecret);
            log.info("Webhook received: " + event.getType());
            if ("checkout.session.completed".equals(event.getType())) {
                log.info("Pago confirmado: checkout.session.completed");
                Session session = (Session) event.getData().getObject();

                Long orderId = Long.valueOf(session.getMetadata().get("order_id"));

                Order order = orderRepository.findById(orderId).orElseThrow(() ->
                        new ResourceNotFoundException("Order not found with id: " + orderId));

                if("paid".equals(session.getPaymentStatus())){
                    order.setStatus(RequestStatus.PAID);
                    updateStock(order);
                    orderRepository.save(order);
                    String orderDate = order.getCreatedAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
                    emailService.sendOrderConfirmation(order.getUser().getEmail(), order.getOrderProducts(), order.getTotal(), orderDate);

                    log.info("Order {} marked as PAID, stock updated, and confirmation email sent to {}", orderId, order.getUser().getEmail());

                }else {
                    order.setStatus(RequestStatus.REJECTED);
                    orderRepository.save(order);

                    log.info("Order {} marked as REJECTED", orderId);
                }
            }

            } catch(Exception e){
            log.error("Error processing Stripe webhook: {}", e.getMessage(), e);
            }
        }


    private void updateStock(Order order) {
        for (OrderProduct orderProduct : order.getOrderProducts()) {
            Product product = orderProduct.getProduct();
            product.setStock(product.getStock() - orderProduct.getQuantity());
            productRepository.save(product);
            log.info("Stock updated for product {}: {} remaining", product.getName(), product.getStock());
        }
    }

}