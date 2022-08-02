package com.rezilux.dinngdonngecommerceapi.services;

import com.rezilux.dinngdonngecommerceapi.entities.Merchant;
import com.rezilux.dinngdonngecommerceapi.repository.MerchantRepository;
import com.rezilux.dinngdonngecommerceapi.utils.FileUploadService;
/*import com.rezilux.yupay.repositories.auth.ProfileRepository;
import com.rezilux.yupay.repositories.auth.UserRepository;
import com.rezilux.yupay.repositories.ecommerce.MerchantRepository;
import com.rezilux.yupay.security.RandomString;
import com.rezilux.yupay.services.auth.UserService;*/

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class MerchantService {

    private final MerchantRepository merchantRepository;
    //private final UserService userService;
    private final FileUploadService fileUploadService;
    //private final ProfileRepository profilRepository;
    //private final UserRepository userRepository;

    public MerchantService(MerchantRepository merchantRepository, FileUploadService fileUploadService/*UserService userService,
                            ProfileRepository profilRepository,
                           UserRepository userRepository*/) {
        this.merchantRepository = merchantRepository;
        //this.userService = userService;
        this.fileUploadService = fileUploadService;
        //this.profilRepository = profilRepository;
        //this.userRepository = userRepository;
    }

    public Merchant create(Merchant merchant) throws Exception {
        String extension;
        String imagePath = "";
        if (merchant.getLogo().length() > 0) {
            extension = (merchant.getLogo().split("base64,")[0]).split("/")[1];
            String contentBase64 = "";
            for (String part: merchant.getLogo().split("base64,")) {
                if (!part.startsWith("data:image")) contentBase64 = part;
            }
            System.out.println(contentBase64);
            imagePath = fileUploadService.upload("images", contentBase64, extension.split(";")[0]);
            if (imagePath.length() == 0 && contentBase64.length() > 0) throw new Exception("Echec de l'upload de l'image");
        }
        merchant.setLogo(imagePath);
        merchant.setCode(generateUniqueMerchantCode());
        //merchant.setUser(userService.connectedUser());

/*
        profilRepository.findDistinctByName("ROLE_MERCHANT").map(profil -> {
            User user = userService.connectedUser();
            user.getProfiles().add(profil);
            userRepository.save(user);
            return true;
        }).orElseThrow(() -> new Exception("Profil #ROLE_MERCHAND non trouvé" ));
*/
        return merchantRepository.save(merchant);
    }

    public Merchant update(Merchant merchant, Long id) throws Exception  {
        String extension;
        String imagePath = "";
        if (!merchant.getLogo().startsWith("/images/")) {
            if (merchant.getLogo().length() > 0) {
                extension = (merchant.getLogo().split("base64,")[0]).split("/")[1];
                String contentBase64 = "";
                for (String part: merchant.getLogo().split("base64,")) {
                    if (!part.startsWith("data:image")) contentBase64 = part;
                }
                System.out.println(contentBase64);
                imagePath = fileUploadService.upload("images", contentBase64, extension.split(";")[0]);
                merchant.setLogo(imagePath);
                if (imagePath.length() == 0 && contentBase64.length() > 0) throw new Exception("Echec de l'upload de l'image");
            }
        }

        return merchantRepository.findById(id).map(c -> {
            c.setName(merchant.getName());
            c.setAddress(merchant.getAddress());
            c.setEmail(merchant.getEmail());
            c.setPhone(merchant.getPhone());
            c.setActive(merchant.getActive());

            c.setLogo(merchant.getLogo());

            return merchantRepository.save(c);
        }).orElse(null);
    }

    public Optional<Merchant> findOne(Long id) {
        return merchantRepository.findById(id);
    }

    public void delete(Long id) throws Exception {
        merchantRepository.delete(findOne(id).orElseThrow(() -> new Exception("Transport not found")));
    }

    public Page<Merchant> findAll(Pageable pageable) {
        return merchantRepository.findAll(pageable);
    }

    public String generateUniqueMerchantCode() {
        String code = "";
        Boolean unique = false;
        do {
            //code = RandomString.getAlphaNumericString(12, true, false);
            unique = merchantRepository.findDistinctByCode(code)
                    .map(merchant -> true)
                    .orElse(false);
        } while (unique);
        return code;
    }

    public Optional<Merchant> findByUtilisateurAndActiveTrue() {
        //return merchantRepository.findFirstByUserAndActiveTrue(userService.connectedUser());
        return null; //juste la coherence
    }

    public Page<Merchant> findAll(Specification<Merchant> spec, Pageable pageable) {
        return merchantRepository.findAll(spec, pageable);
    }

}
