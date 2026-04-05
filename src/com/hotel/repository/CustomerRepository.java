package com.hotel.repository;

import com.hotel.model.Customer;
import java.util.ArrayList;
import java.util.List;

// WEEK 7 - Extends Generic Repository<Customer>
// WEEK 8 - Uses List Interface
public class CustomerRepository extends Repository<Customer> {

    public Customer getCustomerById(int id) {
        for (Customer c : items) {
            if (c.getCustomerId() == id) {
                return c;
            }
        }
        return null;
    }

    public Customer getCustomerByName(String name) {
        for (Customer c : items) {
            if (c.getCustomerName().equalsIgnoreCase(name)) {
                return c;
            }
        }
        return null;
    }

    // WEEK 8 - getAllCustomers uses List interface
    public List<Customer> getAllCustomers() {
        return new ArrayList<>(items);
    }

    public int getNextId() {
        // WEEK 2 - Integer.MAX_VALUE is a Wrapper class constant
        int maxId = 0;
        for (Customer c : items) {
            if (c.getCustomerId() > maxId) {
                maxId = c.getCustomerId();
            }
        }
        return maxId + 1;
    }
}
