package com.rezilux.dinngdonngecommerceapi.web;

import com.rezilux.dinngdonngecommerceapi.entities.ShippingAddress;
import com.rezilux.dinngdonngecommerceapi.services.ShippingAddressService;
import com.rezilux.dinngdonngecommerceapi.web.exceptions.BadRequestAlertException;
import com.rezilux.dinngdonngecommerceapi.web.exceptions.NotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/api/shipping_addresses")
public class ShippingController {

    private final Logger logger = LoggerFactory.getLogger(ShippingController.class);
    private final ShippingAddressService shippingAddressService;

    public ShippingController(ShippingAddressService shippingAddressService) {
        this.shippingAddressService = shippingAddressService;
    }

    /**
     * {@code POST  /shipping_addresses} : Create a new shippingAddress.
     * @param shippingAddress to create
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new shippingAddress, or with status {@code 400 (Bad Request)} if the shippingAddress has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     * @return
     */
    @PostMapping
    public ResponseEntity<ShippingAddress> createShippingAddress(@Valid @RequestBody ShippingAddress shippingAddress) throws URISyntaxException, Exception {
        if (shippingAddress.getId() != null){
            throw   new BadRequestAlertException("A new address cannot already have an ID", "ShippingAddress", "id exists");
        }
        ShippingAddress result = shippingAddressService.addShippingAddress(shippingAddress);
        return ResponseEntity.created(new URI("api/shipping_addresses/" + result.getId()))
                .body(result);
    }

    /**
     * {@code GET  /shipping_addresses} : get all the shipping addresses.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of shipping addresses in body.
     */
    @GetMapping
    public ResponseEntity<Page<ShippingAddress>> getAllAddresses(Pageable pageable) {
        logger.debug("REST request to get a list of Addresses");
        Page<ShippingAddress> listAddress = shippingAddressService.findAll(pageable);
        return new ResponseEntity<>(listAddress, HttpStatus.OK);
    }

    /**
     * {@code GET  /shipping_addresses/connected-user} : get all the shipping addresses for the connected user.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of shipping addresses in body.
     */
    @GetMapping("/connected-user")
    public ResponseEntity<Page<ShippingAddress>> getAllAddressesForUser(Pageable pageable) {
        logger.debug("REST request to get a list of Addresses");
        Page<ShippingAddress> listAddress = shippingAddressService.findAllForUser(pageable);
        return new ResponseEntity<>(listAddress, HttpStatus.OK);
    }

    /**
     * {@code PUT  /shipping_addresses} : Updates an existing shippingAddress.
     *
     * @param shippingAddress the shippingAddress to update.
     * @param id the id of the shippingAddress to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated shippingAddress,
     * or with status {@code 400 (Bad Request)} if the shippingAddress is not valid,
     * or with status {@code 500 (Internal Server Error)} if the article couldn't be updated.
     * @throws BadRequestAlertException if the request data are incorrect.
     * @throws NotFoundException if the id of the shipping address does not exist.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ShippingAddress> updateShippingAddress(@Valid @RequestBody ShippingAddress shippingAddress, @PathVariable("id") Long id ) throws BadRequestAlertException, NotFoundException {
        logger.debug("REST rrequest to get a list of Address : {}", shippingAddress);
        if (shippingAddress.getId() == null) {
            throw new BadRequestAlertException("Invalid  id", "ShippingAddress", "idnull");
        }
        ShippingAddress result = shippingAddressService.update(shippingAddress, id);
        return ResponseEntity.ok()
                .body(result);
    }

    /**
     * {@code DELETE  /shipping_addresses/:id} : delete the "id" shippingAddress.
     *
     * @param id  the id of the shippingAddress to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     * @throws Exception if the id of the shippingAddress is incorrect.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShippindAddress(@PathVariable Long id) throws NotFoundException {
        logger.debug("REST request to get a list of Address : {}", id);
        shippingAddressService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
