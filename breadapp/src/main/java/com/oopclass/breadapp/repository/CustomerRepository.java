/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oopclass.breadapp.repository;

import com.oopclass.breadapp.models.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Lenovo
 */
public interface CustomerRepository extends JpaRepository<Customer, Long> {

	//User findByEmail(String email);
}

