package com.example.demo.service;


import com.example.demo.dto.PaymentDTO;
import com.example.demo.dto.VoucherDTO;
import com.example.demo.entity.Payment;
import com.example.demo.entity.Voucher;
import com.example.demo.repository.PaymentRepository;
import com.example.demo.repository.VoucherRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PaymentService {
    @Autowired
    PaymentRepository paymentRepository;

    @Autowired
    private ModelMapper modelMapper;


    public List<PaymentDTO> findAll() {
        List<Payment> paymentList = paymentRepository.findAll();
        List<PaymentDTO> paymentDTOList = paymentList.stream().map(item -> modelMapper.map(item, PaymentDTO.class)).collect(Collectors.toList());
        return paymentDTOList;
    }

    public PaymentDTO findById(int id) {
        Optional<Payment> payment = paymentRepository.findById(id);
        PaymentDTO paymentDTO = modelMapper.map(payment,PaymentDTO.class);
        return paymentDTO;
    }

    public PaymentDTO findByName(String name) {
        Payment payment = paymentRepository.findByName(name);
        PaymentDTO paymentDTO = modelMapper.map(payment,PaymentDTO.class);
        return paymentDTO;
    }

    public PaymentDTO save(PaymentDTO paymentDTO) {
        Payment request = modelMapper.map(paymentDTO, Payment.class);
        Payment payment = paymentRepository.save(request);
        PaymentDTO response = modelMapper.map(payment, PaymentDTO.class);
        return response;
    }

    public void delete(int id)
    {
        paymentRepository.deleteById(id);
    }
}
