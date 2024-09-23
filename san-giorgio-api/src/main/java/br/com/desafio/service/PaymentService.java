package br.com.desafio.service;

import br.com.desafio.domain.model.BillingModel;
import br.com.desafio.domain.model.PaymentModel;
import br.com.desafio.domain.model.SellerModel;
import br.com.desafio.repository.BillingRepository;
import br.com.desafio.repository.SellerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class PaymentService {
    @Autowired
    private  BillingRepository billingRepository;
    @Autowired
    private  SellerRepository sellerRepository;
    private final SqsClient sqsClient;

    @Value("${aws.sqs.queue.partial}")
    private String partialQueue;

    @Value("${aws.sqs.queue.total}")
    private String totalQueue;

    @Value("${aws.sqs.queue.excess}")
    private String excessQueue;

    @Autowired
    public PaymentService(BillingRepository billingRepository, SellerRepository sellerRepository, SqsClient sqsClient) {
        this.billingRepository = billingRepository;
        this.sellerRepository = sellerRepository;
        this.sqsClient = sqsClient;
    }


    public PaymentModel processPayment(Long sellerId, PaymentModel payment)  {

        Optional<SellerModel> seller = sellerRepository.findById(sellerId);
        Optional<BillingModel> billing = billingRepository.findById(payment.getBillingID());

        if (seller.isEmpty() || billing.isEmpty()) {
            throw new IllegalArgumentException("Seller or billing not found");
        }

        BigDecimal valueOrigin = billing.get().getValueOrigin();

        // Iterar sobre a lista de pagamentos
        for (BigDecimal valuePaid : payment.getValuePayment()){
            String status;

            if (valuePaid.compareTo(valueOrigin) < 0) {
                status = "PARTIAL";
                sendToQueueSQS(partialQueue, payment); }
            else if (valuePaid.compareTo(valueOrigin) == 0) {
                status = "TOTAL";
                sendToQueueSQS(totalQueue, payment);
            } else {
                status = "EXCEDENT";
                sendToQueueSQS(excessQueue, payment);
            }
            payment.setStatus(status);
        }

        return payment;
    }

    private void sendToQueueSQS(String queueUrl, PaymentModel payment) {
        sqsClient.sendMessage(SendMessageRequest.builder()
                .queueUrl(queueUrl)
                .messageBody(payment.toString())
                .build());
    }

}
