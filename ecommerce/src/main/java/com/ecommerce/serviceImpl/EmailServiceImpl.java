package com.ecommerce.serviceImpl;

import com.ecommerce.entity.OrderProduct;
import com.ecommerce.entity.Photo;
import com.ecommerce.entity.Product;
import com.ecommerce.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Override
    public void sendOrderConfirmation(String to, List<OrderProduct> orderProducts, double total,String orderDate) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(to);
        helper.setSubject("Tu compra ha sido confirmada ✔");

        // Formatear la fecha de compra
        String formattedDate = orderDate;

        // Construir el contenido HTML del correo
        StringBuilder content = new StringBuilder();
        content.append("<h2>¡Gracias por tu compra!</h2>");
        content.append("<p>Tu pedido ha sido confirmado el <strong>").append(formattedDate).append("</strong>.</p>");
        content.append("<h3>Detalles del pedido:</h3>");
        content.append("<table border='1' cellpadding='10' cellspacing='0' style='border-collapse: collapse;'>");
        content.append("<tr><th>Imagen</th><th>Producto</th><th>Cantidad</th><th>Subtotal</th></tr>");

        for (OrderProduct orderProduct : orderProducts) {
            Product product = orderProduct.getProduct();

            String imageUrl = getRandomImage(product.getPhotos());

            content.append("<tr>")
                    .append("<td><img src='").append(imageUrl).append("' width='80' height='80'></td>")
                    .append("<td>").append(product.getName()).append("</td>")
                    .append("<td>").append(orderProduct.getQuantity()).append("</td>")
                    .append("<td>$").append(String.format("%.2f", orderProduct.getSubtotal())).append("</td>")
                    .append("</tr>");
        }

        content.append("</table>");
        content.append("<h3>Total: $").append(String.format("%.2f", total)).append("</h3>");
        content.append("<p>Si tienes alguna pregunta, contáctanos.</p>");
        content.append("<p><strong>¡Gracias por comprar con nosotros!</strong></p>");

        helper.setText(content.toString(), true);

        // Enviar el correo
        mailSender.send(message);
    }


    // Método para obtener una imagen aleatoria
    private String getRandomImage(List<Photo> imageUrls) {
        if (imageUrls == null || imageUrls.isEmpty()) {
            return "URL_DE_IMAGEN_POR_DEFECTO"; // Imagen por defecto si no hay imágenes
        }
        int randomIndex = ThreadLocalRandom.current().nextInt(imageUrls.size());
        return imageUrls.get(randomIndex).getSecureUrl();
    }
}
