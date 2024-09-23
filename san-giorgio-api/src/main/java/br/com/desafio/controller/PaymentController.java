package br.com.desafio.controller;


import br.com.desafio.domain.model.PaymentModel;
import br.com.desafio.domain.usecase.ConfirmPaymentUseCase;
import br.com.desafio.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class PaymentController {

    //private final ConfirmPaymentUseCase confirmPaymentUseCase;

    private final PaymentService paymentService;



    @PutMapping(path = "/api/payment")
    public ResponseEntity<Payment> setPayment(Payment request) {
        return ResponseEntity.status(HttpStatus.OK).body(Payment.builder().build());
    }

    @PostMapping("/api/{sellerId}")
    public ResponseEntity<PaymentModel> processPayment(@PathVariable Long sellerId, @RequestBody PaymentModel payment) {
        PaymentModel paymentProcessed = paymentService.processPayment(sellerId, payment);
        return ResponseEntity.ok(paymentProcessed);
    }

}
