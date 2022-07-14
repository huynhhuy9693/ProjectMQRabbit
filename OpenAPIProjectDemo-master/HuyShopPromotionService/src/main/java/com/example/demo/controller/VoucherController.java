package com.example.demo.controller;


import com.example.demo.dto.VoucherDTO;
import com.example.demo.service.VoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/promotion/voucher")
public class VoucherController {

    @Autowired
    VoucherService voucherService;


    @GetMapping(value = "/all")
    public ResponseEntity<List<VoucherDTO>> findAll()
    {
        List<VoucherDTO> voucherDTOList = voucherService.findAll();
        return new ResponseEntity<>(voucherDTOList, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<VoucherDTO> findById(@PathVariable("id") int id)
    {
        VoucherDTO voucherDTO = voucherService.findById(id);
        return new ResponseEntity<>(voucherDTO,HttpStatus.OK);
    }

    @GetMapping(value = "/name/{name}")
    public ResponseEntity<VoucherDTO> findByName(@PathVariable("name") String name)
    {
        VoucherDTO voucherDTO = voucherService.findByName(name);
        return new ResponseEntity<>(voucherDTO,HttpStatus.OK);
    }

    @PostMapping(value = "/{id}")
    public ResponseEntity<VoucherDTO> save(@RequestBody VoucherDTO voucherDTO)
    {
        return new ResponseEntity<>(voucherService.save(voucherDTO),HttpStatus.OK);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<VoucherDTO> edit(@RequestBody VoucherDTO voucherDTO)
    {
        return new ResponseEntity<>(voucherService.save(voucherDTO),HttpStatus.OK);
    }


    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable ("id") int id)
    {
        voucherService.delete(id);
    }
}
