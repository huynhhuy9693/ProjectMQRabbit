package com.example.demo.service;


import com.example.demo.entity.ProductEntity;
import com.example.demo.model.Product;

import com.example.demo.repository.ProductRepository;
import com.cloudinary.utils.ObjectUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import javax.transaction.Transactional;
import java.io.*;
import com.cloudinary.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 10,
        maxFileSize = 1024 * 1024 * 50,
        maxRequestSize = 1024 * 1024 * 100
)

@Service
public class ProductService {
    @Autowired
    private ProductRepository repository;
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    CartItemFeignClient cartItemFeignClient;

    private Logger logger = LoggerFactory.getLogger(ProductService.class);
    Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
            "cloud_name", "huyhuynh",
            "api_key", "186243663589179",
            "api_secret", "xIhyZNk6NYFIuMSHRhZP0srJL_k",
            "secure", true));

    private static  final String SAVE_PATH = "D:\\image";

    public List<Product> findAll()
    {
        List<ProductEntity> request = repository.findAll();
        List<Product> productList = request.stream().map(item->modelMapper.map(item, Product.class)).collect(Collectors.toList());
        return productList;
    }

    public Product findById(Long id)
    {
        for(ProductEntity request : repository.findAll())
        {
            if(request.getId()==id)
            {
                Product response = modelMapper.map(request, Product.class);
                return response;
            }
        }
        return null;
    }
    private static final String UPLOAD_DIR = "image";
    public Product save(Product product, HttpServletRequest httpServletRequest)  {
        ProductEntity request = modelMapper.map(product, ProductEntity.class);
        //upload file
        String save_dir="image";
        String app_path= httpServletRequest.getServletContext().getRealPath("");
        logger.debug("app_path : "+app_path);
        try{
            Part p = httpServletRequest.getPart("image");
            logger.debug("path : " +httpServletRequest.getPart("image"));
            String filename = extractFileName(p);
            logger.debug("file name : " +filename);
            String save_path = app_path+File.separator+save_dir;
            logger.debug("save_path :" +save_path);
            File f =new File(save_path);
            if(!f.exists())
            {
                f.mkdir();
            }
            File f1 = new File(save_path+"/"+filename);
            logger.debug("file : " +f1);
            FileOutputStream fos = new FileOutputStream(f1);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            String image = filename;
            byte b[] = new byte[p.getInputStream().available()];
            p.getInputStream().read(b);

            bos.write(b);
            bos.close();
            fos.close();

            Map upload = cloudinary.uploader().upload(f1, ObjectUtils.emptyMap());
            logger.debug("upload : "+upload);
            logger.debug("secure_url : " +upload.get("secure_url"));
            request.setImgUrl(upload.get("secure_url").toString());

        }catch (Exception e)
        {
            e.getCause().printStackTrace();
        }


        ProductEntity productEntity = repository.save(request);
        Product response = modelMapper.map(productEntity , Product.class);
        return response;

    }
    public void delete(Long id)
    {
        repository.deleteById(id);
    }

    public Product findByName(String name)
    {
        ProductEntity request = repository.findByName(name);
        Product response = modelMapper.map(request, Product.class);
        return response;
    }

    public Integer getQuantityById(Long id)
    {
        int quantity = repository.getQuantityById(id);
       return quantity;
    }


    public Integer updateProductQuantityForId(int quantityPresent,Long id)
    {
        int result = repository.updateProductQuantityForId(quantityPresent,id);
        return result;
    }

    public String encodeImage(String image, String savePath) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(image);
        byte[] data = fileInputStream.readAllBytes();
        String img_url =Base64.getEncoder().encodeToString(data);
        FileWriter fileWriter = new FileWriter(savePath);
        fileWriter.write(img_url);
        fileWriter.close();
        fileInputStream.close();
        return img_url;
    }

    public void decodeImage(String txtPath, String savePath) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(txtPath);
        fileInputStream.readAllBytes();
        byte[] data=Base64.getDecoder().decode(new String((fileInputStream.readAllBytes())));
        FileOutputStream fileOutputStream= new FileOutputStream(savePath);
        fileOutputStream.write(data);
        fileOutputStream.close();
        fileInputStream.close();

    }

    public Integer getDeliveryById(Long id)
    {
        int quantity = repository.getQuantityDeliveryById(id);
        return quantity;
    }

    @Transactional
    public Integer updateDeliveryForId(int delivery,Long id)
    {
        int result = repository.updateDelieveryForId(delivery,id);
        return result;
    }

    private String extractFileName(Part part) {
        // form-data; name="file"; filename="C:\file1.zip"
        // form-data; name="file"; filename="C:\Note\file2.zip"
        String contentDisp = part.getHeader("content-disposition");
        logger.debug("contentDisp : " +contentDisp );
        String[] items = contentDisp.split(";");
        for (String s : items) {
            if (s.trim().startsWith("filename")) {
                // C:\file1.zip
                // C:\Note\file2.zip
                String clientFileName = s.substring(s.indexOf("=") + 2, s.length() - 1);
                clientFileName = clientFileName.replace("\\", "/");
                int i = clientFileName.lastIndexOf('/');
                // file1.zip
                // file2.zip
                return clientFileName.substring(i + 1);
            }
        }
        return null;
    }

    private Object getFolderUpload() {
        File folderUpload = new File(System.getProperty("user.home") + "/Uploads");
        if (!folderUpload.exists()) {
            folderUpload.mkdirs();
        }
        return folderUpload;
    }

}
