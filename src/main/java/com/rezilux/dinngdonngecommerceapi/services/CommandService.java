package com.rezilux.dinngdonngecommerceapi.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rezilux.dinngdonngecommerceapi.entities.Article;
import com.rezilux.dinngdonngecommerceapi.entities.Command;
import com.rezilux.dinngdonngecommerceapi.entities.DetailCommand;
import com.rezilux.dinngdonngecommerceapi.repository.CommandRepository;
import com.rezilux.dinngdonngecommerceapi.repository.DetailCommandRepository;
/*import com.rezilux.yupay.security.RandomString;
import com.rezilux.yupay.services.auth.UserService;*/
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;

@Service
@Transactional
public class CommandService {
    @Value("${app.madjindo-api-url}")
    String apiUrl;
    @Value("${app.madjindo-api-key}")
    String madjindoApiKey;
    @Value("${app.madjindo-auth-token}")
    String madjindoApiToken;
    @Value("${app.assets.api-url}")
    String apiAssetUrl;


    private final CommandRepository commandRepository;
    private final DetailCommandRepository detailCommandRepository;
    //private final UserService userService;
    //private final RestTemplate restTemplate;
    private final ArticleService articleService;

    public CommandService(CommandRepository commandRepository, DetailCommandRepository detailCommandRepository,
                          ArticleService articleService
                          /*RestTemplateBuilder restTemplateBuilder */) {
        this.commandRepository = commandRepository;
        this.detailCommandRepository = detailCommandRepository;
        //this.userService = userService;
        this.articleService = articleService;
        //this.restTemplate = restTemplateBuilder.build();
    }

    // Commands CRUD
    public Command create(Command command) throws InterruptedException {
        command.setDate(Timestamp.from(Instant.now()));
        command.setCode(generateUniqueCode());
        command.setStatus(0);
       // command.setClient(userService.connectedUser());
        command = commandRepository.save(command);
        return saveDetails(command, command.getDetails());
    }

    public Command saveDetails(Command command, Set<DetailCommand> details) throws InterruptedException {
        command.setTotal(
            details.stream()
                .mapToDouble(detail -> detail.getPrice() * detail.getQuantity()).sum()
        );
        List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
        List<DetailCommand> detailsMadjindo = new ArrayList<>();
        for (DetailCommand detail : details) {
            //c'est un optionnal c'est pour quoi on a ajouté .get() à la fin de la methode findOne
            Article article = articleService.findOne(detail.getArticle().getId()).get();
            detail.setCommand(command);
            Map<String, Object> model = new HashMap<>();
            model.put("image", apiAssetUrl + article.getImage());
            model.put("price", detail.getPrice());
            model.put("quantity", detail.getQuantity());
            model.put("name", article.getName());
            mapList.add(model);
            //System.out.println(detail.getArticle().getName());
            detail = detailCommandRepository.save(detail);
            if (article.getMerchant().getName().equalsIgnoreCase("Madjindo")) {
                detailsMadjindo.add(detail);
            }
        }
        try {
            if (detailsMadjindo.size() != 0) sendToMadjindo(detailsMadjindo, command);
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }
        /* Envoie du mail de confirmation de la commande */
        /* String subject = "Confirmation de la commande";
        mailThymeleafTemplate.sendEmail(command.getClient().getEmail(), subject, mapList, command); */
        return commandRepository.save(command);
    }

    // Envoie une requete http|https vers l'api de Madjindo pour les commandes des produits Madjindo
    @Async
    public void sendToMadjindo(List<DetailCommand> detailCommands, Command command) {
        System.out.println("Envoie des elements vers madjindo");
        Map<String, Object> cmd = new HashMap<>();
        cmd.put("modePaiement", "Livraison");
        cmd.put("dinngDonngCode", command.getCode());
        cmd.put("boutique", new HashMap<String, String>() {{
            //put("telephone", "+" + command.getClient().getPhone());
        }});
        System.out.println(command.getShippingAddress().getAddressLine());
        cmd.put("adresseLivraison", new HashMap<String, String>() {{
            put("addressLine", command.getShippingAddress().getAddressLine());
            put("fullName", command.getShippingAddress().getFullName());
            put("phone", command.getShippingAddress().getPhone());
            put("city", command.getShippingAddress().getCity());
            //put("clientId", command.getClient().getIdentification());
        }});
        List<Map<String, Object>> details = new ArrayList<>();
        detailCommands.forEach(detailCommand -> {
            Map<String, Object> detail = new HashMap<>();
            detail.put("categorie", new HashMap<String, String>() {{
                put("categorie", detailCommand.getArticle().getName());
            }});
            detail.put("prix", detailCommand.getPrice());
            detail.put("quantite", detailCommand.getQuantity());
            details.add(detail);
        });
        cmd.put("detailCommandeBoutiques", details);

        HttpHeaders headers = new HttpHeaders();
        // set `accept` header
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        // set custom header
        headers.set("Authorization", "Bearer " + madjindoApiToken);
        // headers.set("Api-Key", madjindoApiToken);
        try {
            // build the request
            ObjectMapper mapper = new ObjectMapper();
            String jsonString = mapper.writeValueAsString(cmd);
            HttpEntity<String> request = new HttpEntity<>(jsonString, headers);
            System.out.println(request.toString());
            System.out.println(request.getBody());
            /*ResponseEntity<Boolean> response = this.restTemplate.exchange(apiUrl + "/ext-api/commands", HttpMethod.POST, request, Boolean.class);
            if (response.getStatusCode() == HttpStatus.CREATED) {
                if (response.getBody() != null) {
                    command.setStatus(1);
                    commandRepository.save(command);
                }
                System.out.println("Envoie reussi");
            } else {
                command.setStatus(-1);
                commandRepository.save(command);
                System.out.println("Envoie ecouché");
            }*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public Command update(Command command, Long id) {
        return commandRepository.findById(id).map(c -> {
            c.setStatus(command.getStatus());
            c = commandRepository.save(c);
            try {
                return saveDetails(c, command.getDetails());
            } catch (InterruptedException e) {
                e.printStackTrace();
                return null;
            }
        }).orElse(null);
    }

    public Optional<Command> findOne(Long id) {
        return commandRepository.findById(id);
    }

    public void delete(Long id) throws Exception {
        commandRepository.delete(findOne(id).orElseThrow(() -> new Exception("Transport not found")));
    }

    public Page<Command> findAll(Pageable pageable) {
        return commandRepository.findAll(pageable);
    }

    public Page<Command> findAll(Specification<Command> spec, Pageable pageable) {
        return commandRepository.findAll(spec, pageable);
    }


  /*  public List<Command> findAllByUserId(Long id) {
        return commandRepository.findAllByClientId(id);
    }*/

    public String generateUniqueCode() {
        String code = "";
        boolean unique;
        do {
            //code = RandomString.getAlphaNumericString(10, true, false);
            unique = commandRepository.findDistinctByCode(code).isPresent();
        } while (unique);
        return code;
    }

   /* public Page<Command> searchByCommandParameter(String code, int status, String lastName, String firstName, Pageable pageable){
        return commandRepository.searchCommandByParam(code, status, lastName, firstName,  pageable);
    }*/
}
