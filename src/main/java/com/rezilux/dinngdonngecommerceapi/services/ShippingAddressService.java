package com.rezilux.dinngdonngecommerceapi.services;

import com.rezilux.dinngdonngecommerceapi.entities.ShippingAddress;
import com.rezilux.dinngdonngecommerceapi.repository.ShippingAddressRepository;
import com.rezilux.dinngdonngecommerceapi.web.exceptions.NotFoundException;
/*import com.rezilux.yupay.entities.auth.User;
import com.rezilux.yupay.entities.ecommerce.ShippingAddress;
import com.rezilux.yupay.repositories.ecommerce.ShippingAddressRepository;
import com.rezilux.yupay.services.auth.UserService;
import com.rezilux.yupay.web.exceptions.NotFoundException;*/
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ShippingAddressService {
    //private final UserService userService;
    private final ShippingAddressRepository shippingAddressRepository;


    public ShippingAddressService(/*UserService userService, */ShippingAddressRepository shippingAddressRepository) {
        //this.userService = userService;
        this.shippingAddressRepository = shippingAddressRepository;
    }


    public ShippingAddress addShippingAddress(ShippingAddress shippingAddress) {

        for (ShippingAddress sa: AllShippingAddressByUser()) {
            sa.setDefault(false);
            shippingAddressRepository.save(sa);
        }

        //User user = userService.connectedUser();
        shippingAddress.setDefault(true);
        //shippingAddress.setUser(user);
        shippingAddressRepository.save(shippingAddress);
        return shippingAddress;
    }

    public Optional<ShippingAddress> findOne(Long id) {
        return shippingAddressRepository.findById(id);
    }

    public Page<ShippingAddress> findAll(Pageable pageable) {
        return shippingAddressRepository.findAll( pageable);
    }

    public Page<ShippingAddress> findAllForUser(Pageable pageable) {
        //return shippingAddressRepository.findAllByUser(userService.connectedUser(), pageable);
        return null;
    }

    private List<ShippingAddress> AllShippingAddressByUser() {
       // User user = userService.connectedUser();
        //return shippingAddressRepository.findAllByUser(user);
        return null;
    }

    public ShippingAddress update(ShippingAddress address, Long id) throws NotFoundException {

        if (address.isDefault()) {
            for (ShippingAddress sa: AllShippingAddressByUser()) {
                sa.setDefault(false);
                shippingAddressRepository.save(sa);
            }
        }

        return shippingAddressRepository.findById(id).map(shippingAddress -> {
            shippingAddress.setAddressLine(address.getAddressLine());
            shippingAddress.setCity(address.getCity());
            shippingAddress.setFullName(address.getFullName());
            shippingAddress.setPhone(address.getPhone());
            return shippingAddressRepository.save(shippingAddress);
        }).orElseThrow(() -> new NotFoundException("Shipping Address With #ID $" + id + " not found"));
    }

    public void delete(Long id) throws NotFoundException {
        shippingAddressRepository.delete(findOne(id)
                .orElseThrow(() -> new NotFoundException("Shipping Address With #ID $" + id + " not found")));
    }
}
