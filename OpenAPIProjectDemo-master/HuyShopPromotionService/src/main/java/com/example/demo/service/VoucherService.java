package com.example.demo.service;


import com.example.demo.dto.VoucherDTO;
import com.example.demo.entity.Voucher;
import com.example.demo.repository.VoucherRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VoucherService {

    @Autowired
    VoucherRepository voucherRepository;

    @Autowired
    private ModelMapper modelMapper;


    public List<VoucherDTO> findAll() {
        List<Voucher> voucherList = voucherRepository.findAll();
        List<VoucherDTO> voucherDTOList = voucherList.stream().map(item -> modelMapper.map(item, VoucherDTO.class)).collect(Collectors.toList());
        return voucherDTOList;
    }

    public VoucherDTO findById(int id) {
        for (Voucher request : voucherRepository.findAll()) {
            if (request.getId() == id) {
                VoucherDTO response = modelMapper.map(request, VoucherDTO.class);
                return response;
            }
        }
        return null;
    }

    public VoucherDTO findByName(String name) {
        for (Voucher request : voucherRepository.findAll()) {
            if (request.getName().equalsIgnoreCase(name)) {
                VoucherDTO response = modelMapper.map(request, VoucherDTO.class);
                return response;
            }
        }
        return null;
    }

    public VoucherDTO save(VoucherDTO voucherDTO) {
        Voucher request = modelMapper.map(voucherDTO, Voucher.class);
        Voucher voucher = voucherRepository.save(request);
        VoucherDTO response = modelMapper.map(voucher, VoucherDTO.class);
        return response;
    }

    public void delete(int id)
    {
        voucherRepository.deleteById(id);
    }
}


