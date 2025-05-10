package com.cretf.backend.users.service.impl;

import com.cretf.backend.fileservice.FileUploadService;
import com.cretf.backend.product.dto.PropertyDTO;
import com.cretf.backend.product.entity.Files;
import com.cretf.backend.product.repository.FilesRepository;
import com.cretf.backend.product.service.PropertyService;
import com.cretf.backend.users.dto.DepositContractDTO;
import com.cretf.backend.users.dto.UsersDTO;
import com.cretf.backend.users.entity.DepositContract;
import com.cretf.backend.users.repository.DepositContractRepository;
import com.cretf.backend.users.service.DepositService;
import com.cretf.backend.users.service.DepositContractService;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.http.Method;
import org.apache.poi.xwpf.usermodel.*;
import org.modelmapper.ModelMapper;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPageMar;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSectPr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class DepositContractServiceImpl implements DepositContractService {
    private final DepositContractRepository depositContractRepository;
    private final FilesRepository filesRepository;
    private final FileUploadService fileUploadService;
    private final DepositService depositService;
    private final PropertyService propertyService;
    private final UserService userService;
    private final ModelMapper modelMapper;
    private final MinioClient minioClient;

    @Value("${minio.bucket}")
    private String bucket;

    @Autowired
    public DepositContractServiceImpl(
            DepositContractRepository depositContractRepository,
            FilesRepository filesRepository,
            FileUploadService fileUploadService,
            DepositService depositService,
            PropertyService propertyService,
            UserService userService,
            ModelMapper modelMapper,
            MinioClient minioClient
    ) {
        this.depositContractRepository = depositContractRepository;
        this.filesRepository = filesRepository;
        this.fileUploadService = fileUploadService;
        this.depositService = depositService;
        this.propertyService = propertyService;
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.minioClient = minioClient;
    }
    @Override
    public DepositContractDTO create(DepositContractDTO request) throws Exception {
        UsersDTO buyer = userService.getUserByUsername(request.getBuyer());
        UsersDTO seller = userService.getUserByUsername(request.getSeller());
        PropertyDTO propertyDTO = new PropertyDTO();
        propertyDTO.setPropertyId(request.getPropertyId());
        propertyDTO = propertyService.getOneProperty(propertyDTO);

        XWPFDocument document = new XWPFDocument();

        // Căn lề trang
        CTSectPr sectPr = document.getDocument().getBody().addNewSectPr();
        CTPageMar pageMar = sectPr.addNewPgMar();
        pageMar.setLeft(BigInteger.valueOf(1440));
        pageMar.setRight(BigInteger.valueOf(1440));
        pageMar.setTop(BigInteger.valueOf(1440));
        pageMar.setBottom(BigInteger.valueOf(1440));

        // Tiêu đề hợp đồng
        XWPFParagraph titleParagraph = document.createParagraph();
        titleParagraph.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun titleRun = titleParagraph.createRun();
        titleRun.setBold(true);
        titleRun.setFontSize(16);
        titleRun.setFontFamily("Times New Roman");
        titleRun.setText("HỢP ĐỒNG ĐẶT CỌC");
        titleRun.addBreak();

        XWPFParagraph introParagraph = document.createParagraph();
        XWPFRun introRun = introParagraph.createRun();
        introRun.setFontSize(12);
        introRun.setText("Hôm nay, ngày " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + ", chúng tôi gồm:");

        // Thông tin Bên bán
        XWPFParagraph sellerParagraph = document.createParagraph();
        XWPFRun sellerTitleRun = sellerParagraph.createRun();
        sellerTitleRun.setBold(true);
        sellerTitleRun.setText("BÊN A (BÊN BÁN):");
        sellerTitleRun.addBreak();
        XWPFRun sellerInfoRun = sellerParagraph.createRun();
        sellerInfoRun.setFontSize(12);
        sellerInfoRun.setText("Họ tên: " + seller.getUserDetailDTO().getFullName());
        sellerInfoRun.addBreak();
        sellerInfoRun.setText("Điện thoại: " + seller.getUserDetailDTO().getPhone());
        sellerInfoRun.addBreak();

        // Thông tin Bên mua
        XWPFParagraph buyerParagraph = document.createParagraph();
        XWPFRun buyerTitleRun = buyerParagraph.createRun();
        buyerTitleRun.setBold(true);
        buyerTitleRun.setText("BÊN B (BÊN MUA):");
        buyerTitleRun.addBreak();
        XWPFRun buyerInfoRun = buyerParagraph.createRun();
        buyerInfoRun.setFontSize(12);
        buyerInfoRun.setText("Họ tên: " + buyer.getUserDetailDTO().getFullName());
        buyerInfoRun.addBreak();
        buyerInfoRun.setText("Điện thoại: " + buyer.getUserDetailDTO().getPhone());
        buyerInfoRun.addBreak();

        // Điều khoản hợp đồng
        XWPFParagraph termsParagraph = document.createParagraph();
        XWPFRun termsRun = termsParagraph.createRun();
        //termsRun.setBold(true);
        termsRun.setText("Hai bên cùng thống nhất các điều khoản sau:");
        termsRun.addBreak();
        termsRun.setText("1. Bên A đồng ý bán cho Bên B bất động sản tại: " + propertyDTO.getAddressSpecific());
        termsRun.addBreak();
        termsRun.setText("   - Giá bán: " + propertyDTO.getPropertyPriceNewest().getValue() + " " + propertyDTO.getPropertyPriceNewest().getScaleUnit());
        termsRun.addBreak();
        termsRun.setText("2. Bên B đặt cọc cho Bên A số tiền: " + request.getDepositDTO().getValue() + "-" + request.getDepositDTO().getScaleUnit());
        termsRun.addBreak();
        termsRun.setText("   - Thời hạn đặt cọc đến: " + LocalDate.now().plusDays(request.getDepositDTO().getDueDate()).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        termsRun.addBreak();
        termsRun.setText("3. Hai bên cam kết sẽ ký hợp đồng mua bán tại văn phòng công chứng trước ngày: " + LocalDate.now().plusDays(request.getDepositDTO().getDueDate()).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        termsRun.addBreak();
        termsRun.setText("4. Nếu Bên B từ chối mua: tiền đặt cọc sẽ không được hoàn trả.");
        termsRun.addBreak();
        termsRun.setText("   Nếu Bên A từ chối bán: phải hoàn trả số tiền đặt cọc.");
        termsRun.addBreak();
        termsRun.setText("5. Hợp đồng được lập thành 2 bản, mỗi bên giữ 1 bản và có giá trị như nhau.");
        termsRun.addBreak();
        termsRun.addBreak();

        // Bảng chữ ký
        XWPFTable table = document.createTable(1, 2);
        table.removeBorders();
        table.setWidth("100%");

        XWPFTableCell cell1 = table.getRow(0).getCell(0);
        XWPFParagraph cell1Para = cell1.getParagraphArray(0);
        cell1Para.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun cell1Run = cell1Para.createRun();
        cell1Run.setBold(true);
        cell1Run.setText("BÊN BÁN");
        cell1Run.addBreak();
        cell1Run.addBreak();
        cell1Run.addBreak();
        cell1Run.setText(seller.getUserDetailDTO().getFullName());

        XWPFTableCell cell2 = table.getRow(0).getCell(1);
        XWPFParagraph cell2Para = cell2.getParagraphArray(0);
        cell2Para.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun cell2Run = cell2Para.createRun();
        cell2Run.setBold(true);
        cell2Run.setText("BÊN MUA");
        cell2Run.addBreak();
        cell2Run.addBreak();
        cell2Run.addBreak();
        cell2Run.setText(buyer.getUserDetailDTO().getFullName());

        // Chuyển document thành byte array
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        document.write(byteArrayOutputStream);
        document.close();
        byte[] docBytes = byteArrayOutputStream.toByteArray();

        // Tạo tên file và đường dẫn
        String fileId = UUID.randomUUID().toString();
        String fileName = "TTMB_" + fileId.substring(0, 6) + ".docx";
        String filePath = fileId + "/" + fileName;

        // Upload trực tiếp lên MinIO
        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(bucket)
                        .object(filePath)
                        .stream(new ByteArrayInputStream(docBytes), docBytes.length, -1)
                        .contentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document")
                        .build()
        );

        // Lưu metadata vào DB Files
        Files fileEntity = new Files();
        fileEntity.setFileId(fileId);
        fileEntity.setName(fileName);
        fileEntity.setPath(filePath);
        fileEntity.setType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");

        filesRepository.save(fileEntity);

        // Lấy URL tải xuống
        String downloadUrl = minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                        .bucket(bucket)
                        .object(filePath)
                        .method(Method.GET)
                        .expiry(24, TimeUnit.HOURS)
                        .build()
        );

        // Tạo entity DocTemplates
        DepositContract ennity = modelMapper.map(request, DepositContract.class);
        ennity.setFileId(fileId);
        ennity.setFileName(fileName);
        ennity.setDateCreated(new Date());
        ennity.setDownloadUrl(downloadUrl);
        ennity.setDepositId(request.getDepositDTO().getDepositId());
        Date dueDate = Date.from(LocalDate.now().plusDays(request.getDepositDTO().getDueDate()).atStartOfDay(ZoneId.systemDefault()).toInstant());
        ennity.setDueDate(dueDate);
        depositContractRepository.save(ennity);

        // Trả về DTO
        DepositContractDTO resultDTO = modelMapper.map(ennity, DepositContractDTO.class);

        return resultDTO;
    }
    @Override
    public List<DepositContractDTO> getAll() throws Exception {
        return depositContractRepository.findAll().stream()
                .map(item -> {return modelMapper.map(item, DepositContractDTO.class);})
                .collect(Collectors.toList());
    }
    @Override
    public DepositContractDTO getById(String templateId) {
        DepositContract depositContract = depositContractRepository.findById(templateId).orElseThrow(() -> new RuntimeException("Template not found"));
        return modelMapper.map(depositContract, DepositContractDTO.class);
    }
    @Override
    public List<DepositContractDTO> search(String keyword) {
        return depositContractRepository.findBySellerNameContainingOrBuyerNameContaining(keyword).stream()
                .map(item -> {
                    return modelMapper.map(item, DepositContractDTO.class);
                })
                .collect(Collectors.toList());
    }
    @Override
    public boolean delete(String templateId) throws Exception {
        Optional<DepositContract> template = depositContractRepository.findById(templateId);
        if(template.isPresent()){
            DepositContract depositContract = template.get();
            // Xóa file từ MinIO
            fileUploadService.deleteFile(depositContract.getFileId());

            // Xóa template từ DB
            depositContractRepository.delete(depositContract);
        }
        return template.isPresent();
    }
}
