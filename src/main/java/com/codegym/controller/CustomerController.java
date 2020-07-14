package com.codegym.controller;

import com.codegym.model.Customer;
import com.codegym.model.Province;
import com.codegym.sevice.CustomerService;
import com.codegym.sevice.ProvinceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Controller
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @Autowired
    private ProvinceService provinceService;

    @ModelAttribute("provinces")
    public Iterable<Province> provinces(){
        return provinceService.findAll();
    }

    @GetMapping("/")
    public ModelAndView listCustomers(@RequestParam("s") Optional<String> s,
                                      @PageableDefault(size = 5,direction = Sort.Direction.ASC, sort = "id") Pageable pageable){
        Page<Customer> customers;
        ModelAndView modelAndView = new ModelAndView("customer/list");

        if (s.isPresent()){
            customers= customerService.findAllByFirstNameContaining(s.get(),pageable);
            modelAndView.addObject("s",s.get());
        }else {
             customers = customerService.findAll(pageable);
        }

        modelAndView.addObject("customers",customers);
        return modelAndView;
    }

    @GetMapping("/create-customer")
    public  ModelAndView createCustomerForm(){
        ModelAndView modelAndView = new ModelAndView("customer/create");
        modelAndView.addObject("customer",new Customer());
        return modelAndView;
    }

    @PostMapping("/create-customer")
    public ModelAndView createCustomer(@ModelAttribute("customer") Customer customer){
        customerService.save(customer);
        ModelAndView modelAndView = new ModelAndView("customer/create");
        modelAndView.addObject("customer", new Customer());
        modelAndView.addObject("message","New customer created successful");
        return modelAndView;
    }

    @GetMapping("/edit-customer/{id}")
    public ModelAndView updateCustomerForm(@PathVariable Long id){
        Customer customer = customerService.findById(id);
        if (customer!= null){
            ModelAndView modelAndView = new ModelAndView("customer/edit");
            modelAndView.addObject("customer", customer);
            return modelAndView;
        }else {
            ModelAndView modelAndView = new ModelAndView("error-404");
            return modelAndView;
        }
    }

    @PostMapping("/edit-customer")
    public ModelAndView updateCustomer(@ModelAttribute("customer") Customer customer){
        customerService.save(customer);
        ModelAndView modelAndView = new ModelAndView("customer/edit");
        modelAndView.addObject("customer",customer);
        modelAndView.addObject("message","edit customer successfully");
        return modelAndView;
    }

    @GetMapping("/delete-customer/{id}")
    public ModelAndView deleteCustomerForm(@PathVariable Long id){
        ModelAndView modelAndView = new ModelAndView("customer/delete");
        modelAndView.addObject("customer",customerService.findById(id));
        return modelAndView;
    }

    @PostMapping("/delete-customer")
    public String deleteCustomer(@ModelAttribute("customer") Customer customer){
        customerService.remove(customer.getId());
        return "redirect:/";
    }


}
