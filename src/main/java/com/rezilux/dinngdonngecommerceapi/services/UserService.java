package com.rezilux.dinngdonngecommerceapi.services;

import com.rezilux.dinngdonngecommerceapi.entities.Profile;
import com.rezilux.dinngdonngecommerceapi.entities.User;
import com.rezilux.dinngdonngecommerceapi.repository.ProfileRepository;
import com.rezilux.dinngdonngecommerceapi.repository.UserRepository;
import com.rezilux.dinngdonngecommerceapi.web.PhoneAccountModel;
import com.rezilux.dinngdonngecommerceapi.web.exceptions.NotFoundException;
import com.rezilux.dinngdonngecommerceapi.web.exceptions.UserEmailAlreadyExist;
import com.rezilux.dinngdonngecommerceapi.web.exceptions.UserPhoneAlreadyExist;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.*;

@Service
@Transactional
public class UserService {

    @Value("${app.front-url}")
    private String url;
    //private static final long EXPIRE_TOKEN_AFTER_MINUTES = 30;
    private UserRepository userRepository;
    //private final BCryptPasswordEncoder passwordEncoder;
    private final ProfileRepository profilRepository;
    //private final VerificationCodeService verificationCodeService;
    //private final VerificationCodeRepository verificationCodeRepository;
    //private final MessageService messageService;
    private final ProfileRepository profileRepository;

    @Autowired
    public UserService(UserRepository userRepository, ProfileRepository profilRepository,
                        ProfileRepository profileRepository /*,
                       VerificationCodeService verificationCodeService, VerificationCodeRepository verificationCodeRepository,
                       MessageService messageService, BCryptPasswordEncoder passwordEncoder*/) {
        this.userRepository = userRepository;
        this.profilRepository = profilRepository;
        //this.passwordEncoder = passwordEncoder;
      /*  this.verificationCodeService = verificationCodeService;
        this.verificationCodeRepository = verificationCodeRepository;
        this.messageService = messageService;*/
        this.profileRepository = profileRepository;
    }

    public User connectedUser() {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        String login = "";
        if (authentication.getPrincipal() instanceof UserDetails) {
            UserDetails user = (UserDetails) authentication.getPrincipal();
            login = user.getUsername();
        }
        if (authentication.getPrincipal() instanceof String)
            login = (String) authentication.getPrincipal();
        return userRepository
                .connexion(login, true)
                .map(user -> user)
                .orElse(null);
    }

    public Optional<User> findOne(Long id) {
        return userRepository.findById(id);
    }

    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    public Page<User> findAll(Specification<User> specification, Pageable pageable) {
        return userRepository.findAll(specification, pageable);
    }

    public User registerWithPhoneNumber(PhoneAccountModel phoneAccountModel) throws UserPhoneAlreadyExist {
        User user = new User();
        if (!userRepository.findFirstByPhoneAndCreationStep(
                phoneAccountModel.getIndicative() + phoneAccountModel.getPhone(), 1).isPresent()) {
            if (userRepository.findFirstByPhoneAndCreationStep(
                    phoneAccountModel.getIndicative() + phoneAccountModel.getPhone(), 0).isPresent())
                user = userRepository.findFirstByPhoneAndCreationStep(
                        phoneAccountModel.getIndicative() + phoneAccountModel.getPhone(), 0).get();
            else {
                user.setCreationStep(0);
                user.setActive(false);
                user.setIdentification(generateUniquePersonIdentification());
                user.setPhone(phoneAccountModel.getIndicative() + phoneAccountModel.getPhone());
                user.setLogin(phoneAccountModel.getIndicative() + phoneAccountModel.getPhone());
                user = userRepository.save(user);
            }
            if (user.getProfiles().isEmpty()) {
                Profile profile = profilRepository.findDistinctByName("ROLE_USER").get();
                user.getProfiles().add(profile);
                user = userRepository.save(user);
            }
        } else {
            throw new UserPhoneAlreadyExist("Phone Number Already Taken");
        }
        return user;
    }

    public User completeRegistration(User user, Long id) throws Exception, UserEmailAlreadyExist {
        /* if (userRepository.findFirstByEmailAndCreationStep(user.getEmail(), 1).isPresent())
            throw new UserEmailAlreadyExist("User Email Already taken"); */
        return userRepository.findFirstByPhoneAndCreationStep(user.getPhone(), 0)
                .map((u) -> {
                    u.setEmail(user.getEmail());
                    u.setCreationStep(1);
                    u.setActive(true);
                    u.setLastName(user.getLastName());
                    u.setFirstName(user.getFirstName());
                    //u.setPassword(passwordEncoder.encode(user.getPassword()));
                    // Creation du code de verification pour l'utlisateur et envoie du code par dans sa boite mail
                    // verificationCodeService.save(u);

                    return userRepository.save(u);
                })
                .orElseThrow(() -> new Exception("Internal Server Error"));
    }

    public String generateUniquePersonIdentification() {
        String code = "";
        boolean unique = false;
        do {
            code = com.rezilux.dinngdonngecommerceapi.security.RandomString.getAlphaNumericString(12, true, true);
            unique = userRepository.findFirstByIdentification(code).isPresent();
        } while (unique);
        return code;
    }


    public Boolean forgotEmail(String email, String token) throws Exception {
        User user = userRepository.connexion(email, true).orElseThrow(() -> new Exception("Utilisateur Inconnu"));
        try {
            String subject = "Reinitialisation du mot de passe";
            String body = "Bonjour " + user.getFirstName() + " " + user.getLastName() + ". Pour reinitialiser votre" +
                    " mot de passe sur DNNG DONNG. en <a href='" + url + "/change-password/" + token + "'>cliquer ici</a>.<br/></br> afin de commencer.";
            //messageService.sendBlankEmail(subject, body, user.getEmail());
            user.setActive(true);
            userRepository.save(user);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public Boolean forgotPassword(String email, String password) throws Exception {
        User user = userRepository.connexion(email, true).orElseThrow(() -> new Exception("Utilisateur Inexistant"));
        //user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
        return true;
    }

    /*public Boolean updatePassword(String email, String password, String newpassword) throws Exception {
        User user = userRepository.connexion(email, true).orElseThrow(() -> new Exception("Utilisateur Inexistant"));
        if (passwordEncoder.matches(password, user.getPassword())) {
            user.setPassword(passwordEncoder.encode(newpassword));
            userRepository.save(user);
        } else {
            throw new Exception("Mot de passe Incorrect");
        }
        return true;
    }*/


    private String generatePassword() {
        String generatedString = RandomStringUtils.random(10, true, true);
        System.out.println(generatedString);
        return generatedString;
    }

    public User create(User u) {
        u.setActive(true);
        u.setCreationStep(1);
        String pass = generatePassword();
        //u.setPassword(passwordEncoder.encode(pass));
        u.setIdentification(generateUniquePersonIdentification());
        List<Profile> profileList = new ArrayList<>();
        for (Profile profile : u.getProfiles()) {
            //Profile p = profilRepository.getOne(profile.getId());
           Profile p = profileRepository.findDistinctByName(profile.getName()).orElse(null);
           if(p != null){
               profileList.add(p);
           }
        }
        u.setProfiles(profileList);
        u = userRepository.save(u);
        u.setLogin(u.getLastName() + " @@ " + u.getId());



        /*messageService.sendBlankEmail(
                "Creation de compte utilisateur",
                "Bonjour Mr/Mme " + u.getFirstName() + " " + u.getLastName() + ", \n" +
                        "Votre compte sur la plateforme <a href='http://yupay.rezilux.com'>DINNG DONNG</a> a été crée avec success. Voici vos identifiants. \n" +
                        "\n Login : " + u.getLogin() + "\n" +
                        " Mot de passe : " + pass
                , u.getEmail());*/
        return userRepository.save(u);
    }

    public User update(User user, Long id) {
        return findOne(id).map(u -> {
            u.setLastName(user.getLastName());
            u.setFirstName(user.getFirstName());
            u.setPhone(user.getPhone());
            u.setLogin(user.getLogin());
            u.setActive(user.getActive());
            u.setEmail(user.getEmail());

            return userRepository.save(u);
        }).orElseThrow(() -> new NotFoundException("User with #ID $" + id + " not found"));
    }

    public boolean delete(Long id) {
        return findOne(id).map(u -> {
            userRepository.delete(u);
            return true;
        }).orElseThrow(() -> new NotFoundException("User with #ID $" + id + " not found"));
    }

    /*public VerificationCode forgotPasswordMobile(String email) {
        System.out.println(email);
        return userRepository.connexion(email, true).map((verificationCodeService::save))
                .orElseThrow(() -> new NotFoundException("User with #EMAIL $" + email + " not found"));
    }*/

    /*public User checkVerificationCodeMobile(Map<String, String> body) {
        return verificationCodeService.findByCode(body.get("code")).map(verificationCode -> {
            if (verificationCode.getUser().getIdentification().equals(body.get("userId")));
            verificationCode.setStatus(true);
            return verificationCodeRepository.save(verificationCode).getUser();
        }).orElseThrow(() -> new NotFoundException("This code is invalid"));
    }*/
    
    public Page<Profile> findAllProfile(Pageable pageable){
        return profileRepository.findAll(pageable);
    }


    //Export Excel File
    public static ByteArrayInputStream userExcelReport(List<User> users) throws Exception{

        DateTimeFormatter formatter =
                DateTimeFormatter.ofLocalizedDateTime( FormatStyle.SHORT )
                        .withLocale( Locale.UK )
                        .withZone( ZoneId.systemDefault() );

        String[] columns = {"Nom", "Prénom", "Email", "Téléphone","Date de création", "Profiles"};
        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream();) {
                CreationHelper creationHelper = workbook.getCreationHelper();

                Sheet sheet = workbook.createSheet("Users");
                sheet.autoSizeColumn(columns.length);

                Font headerFont = workbook.createFont();
                headerFont.setBold(true);
                headerFont.setColor(IndexedColors.BLUE.getIndex());

                CellStyle cellStyle = workbook.createCellStyle();
                cellStyle.setFont(headerFont);

                // Row for header
                Row headerRow = sheet.createRow(0);

                // Header
                for(int col = 0; col < columns.length; col ++){
                    Cell cell = headerRow.createCell(col);
                    cell.setCellValue(columns[col]);
                    cell.setCellStyle(cellStyle);
                }

                CellStyle cellStyle1 = workbook.createCellStyle();
                cellStyle1.setDataFormat(creationHelper.createDataFormat().getFormat("#"));

                 String pName = "";
                int rowIndex = 1;
                for(User user : users) {
                    Row row = sheet.createRow(rowIndex++);
                    row.createCell(0).setCellValue(user.getLastName());
                    row.createCell(1).setCellValue(user.getFirstName());
                    row.createCell(2).setCellValue(user.getEmail());
                    row.createCell(3).setCellValue(user.getPhone());
                    /**
                     * cannot resolve method setCellValue(java.time.Instant):
                     * il ne prend pas des valeurs de type(time, instant) donc
                     * il faut le convertir en chaine (la methode [toString])
                     */
                    row.createCell(4).setCellValue(formatter.format(user.getCreatedDate()));
                    for (Profile p: user.getProfiles()){
                        pName+=p.getName()+",";
                        row.createCell(5).setCellValue(pName.substring(0, pName.length()-1));
                    }
                    /**
                     * on reinitialise la variable en cours de l'utilisateur
                     * pour qu'il prenne de nouvelle valeur
                     */
                    pName = "";
                   /* user.getProfiles().forEach(profile -> {
                        pName+=profile.getName();
                        row.createCell(4).setCellValue(pName);
                    });*/
                }
                workbook.write(outputStream);
                return new ByteArrayInputStream(outputStream.toByteArray());
             }
    }

    public Page<User> allProfilesByUser(Long id, Pageable pageable){
        return profileRepository.findAllProfileByUsers(id, pageable);
    }

    public Page<User> searchByUser(String lastName, String firstName, String phone, String email, Pageable pageable){
        return userRepository.searchByUser(lastName, firstName, phone, email,  pageable);
    }
}



// https://stackoverflow.com/questions/25229124/format-instant-to-string/27483371