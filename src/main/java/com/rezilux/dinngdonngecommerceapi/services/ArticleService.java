package com.rezilux.dinngdonngecommerceapi.services;

import com.rezilux.dinngdonngecommerceapi.entities.Article;
import com.rezilux.dinngdonngecommerceapi.entities.Merchant;
import com.rezilux.dinngdonngecommerceapi.repository.ArticleRepository;
import com.rezilux.dinngdonngecommerceapi.repository.MerchantRepository;
import com.rezilux.dinngdonngecommerceapi.utils.FileUploadService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Transactional
@Service
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final FileUploadService fileUploadService;
    private final MerchantRepository merchantRepository;
    //private final UserService userService;


    public ArticleService(ArticleRepository articleRepository, FileUploadService fileUploadService,
                          MerchantRepository merchantRepository /*UserService userService*/) {
        this.articleRepository = articleRepository;
        this.fileUploadService = fileUploadService;
        this.merchantRepository = merchantRepository;
        //this.userService = userService;
    }

    public Article create(Article article) throws Exception {
        //Merchant merchant = merchantRepository
                //.findFirstByUserAndActiveTrue(userService.connectedUser())
                //.map(m -> m).orElseThrow(() -> new Exception("Vous n'etes pas un marchand"));
        String extension;
        String imagePath = "";
        if (article.getImage().length() > 0) {
            extension = (article.getImage().split("base64,")[0]).split("/")[1];
            String contentBase64 = "";
            for (String part: article.getImage().split("base64,")) {
                if (!part.startsWith("data:image")) contentBase64 = part;
            }
            System.out.println(contentBase64);
            imagePath = fileUploadService.upload("images", contentBase64, extension.split(";")[0]);
            if (imagePath.length() == 0 && contentBase64.length() > 0) throw new Exception("Echec de l'upload de l'image");
        }
        //article.setMerchant(merchant);
        article.setImage(imagePath);
        return articleRepository.save(article);
    }

    public Article update(Article article, Long id) throws Exception {
        String extension;
        String imagePath = "";
        if (article.getImage().length() > 0) {
            extension = (article.getImage().split("base64,")[0]).split("/")[1];
            String contentBase64 = "";
            for (String part: article.getImage().split("base64,")) {
                if (!part.startsWith("data:image")) contentBase64 = part;
            }
            System.out.println(contentBase64);
            imagePath = fileUploadService.upload("images", contentBase64, extension.split(";")[0]);
            article.setImage(imagePath);
            if (imagePath.length() == 0 && contentBase64.length() > 0) throw new Exception("Echec de l'upload de l'image");
        }
        return articleRepository.findById(id).map(a -> {
            a.setCategory(article.getCategory());
            a.setName(article.getName());
            a.setDescription(article.getDescription());
            a.setQuantity(article.getQuantity());
            a.setPrice(article.getPrice());
            a.setImage(article.getImage());
            return articleRepository.save(a);
        }).orElse(null);
    }

    public Optional<Article> findOne(Long id) {
        return articleRepository.findById(id);
    }

    public void delete(Long id) throws Exception {
        articleRepository.delete(findOne(id).orElseThrow(() -> new Exception("Transport not found")));
    }

    public Page<Article> findAll(Pageable pageable) {
        return articleRepository.findAll(pageable);
    }

    public Page<Article> findAllByMerchantId(Long merchantId, Pageable pageable) {
        return articleRepository.findAllByMerchantId(merchantId, pageable);
    }

    public Page<Article> findAll(Specification<Article> spec, Pageable pageable) {
        return articleRepository.findAll(spec, pageable);
    }

    public List<Article> findAllArticleByName(String name){
        return articleRepository.findAll();
    }

    public Page<Article> findArticleByCategory(Long id, Pageable pageable){
        return articleRepository.findArtilceByCategoryId(id, pageable);
    }

    //export Article in excel file
    public static ByteArrayInputStream articleExcelReport(List<Article> articles) throws Exception{

        DateTimeFormatter formatter =
                DateTimeFormatter.ofLocalizedDateTime( FormatStyle.SHORT )
                        .withLocale( Locale.UK )
                        .withZone( ZoneId.systemDefault() );

        String columns[] = {"Nom","Description","Quantité","Prix","Date de création","Catégorie","Marchand"};
        try(Workbook workbook = new XSSFWorkbook();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();){
            CreationHelper creationHelper = workbook.getCreationHelper();

            Sheet sheet = workbook.createSheet("Articles");
            sheet.autoSizeColumn(columns.length);

            Font headFont = workbook.createFont();
            headFont.setBold(true);
            headFont.setColor(IndexedColors.BLUE.getIndex());

            CellStyle cellStyle = workbook.createCellStyle();
            cellStyle.setFont(headFont);

            //Row for header
            Row headRow = sheet.createRow(0);

            //Header
            for(int col = 0; col<columns.length; col++){
                Cell cell = headRow.createCell(col);
                cell.setCellValue(columns[col]);
                cell.setCellStyle(cellStyle);
            }

            CellStyle cellStyle1 = workbook.createCellStyle();
            cellStyle1.setDataFormat(creationHelper.createDataFormat().getFormat("#"));

            int rowIndex = 1;
            for(Article a: articles){
                Row row = sheet.createRow(rowIndex++);
                row.createCell(0).setCellValue(a.getName());
                row.createCell(1).setCellValue(a.getDescription());
                row.createCell(2).setCellValue(a.getPrice());
                row.createCell(3).setCellValue(a.getQuantity());
                row.createCell(4).setCellValue(formatter.format(a.getCreatedDate()));
                row.createCell(5).setCellValue(a.getCategory().getName());
                row.createCell(6).setCellValue(a.getMerchant().getEmail());
            }
            workbook.write(outputStream);
            return new ByteArrayInputStream(outputStream.toByteArray());
        }
    }
}
