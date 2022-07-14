package com.example.demo.controller;


import com.example.demo.dto.PaymentDTO;
import com.example.demo.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping(value = "/payment")
public class PaymentController {
    @Autowired
    PaymentService paymentService;


    @GetMapping(value = "/all")
    public ResponseEntity<List<PaymentDTO>> findAll()
    {
        List<PaymentDTO> paymentDTOList = paymentService.findAll();
        return new ResponseEntity<>(paymentDTOList, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<PaymentDTO> findById(@PathVariable("id") int id)
    {
        PaymentDTO paymentDTO = paymentService.findById(id);
        return new ResponseEntity<>(paymentDTO,HttpStatus.OK);
    }

    @GetMapping(value = "/name/{name}")
    public ResponseEntity<PaymentDTO> findByName(@PathVariable("name") String name)
    {
        PaymentDTO paymentDTO = paymentService.findByName(name);
        return new ResponseEntity<>(paymentDTO,HttpStatus.OK);
    }

    @PostMapping(value = "/{id}")
    public ResponseEntity<PaymentDTO> save(@RequestBody PaymentDTO paymentDTO)
    {
        return new ResponseEntity<>(paymentService.save(paymentDTO),HttpStatus.OK);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<PaymentDTO> edit(@RequestBody PaymentDTO paymentDTO)
    {
        return new ResponseEntity<>(paymentService.save(paymentDTO),HttpStatus.OK);
    }


    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable ("id") int id)
    {
        paymentService.delete(id);
    }
}
