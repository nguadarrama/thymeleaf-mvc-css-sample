package sample.customer.ui.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import sample.common.exception.DataNotFoundException;
import sample.customer.application.CustomerService;
import sample.customer.domain.dto.CustomerDto;

@Controller
public class CustomerListController {

    @Autowired
    private CustomerService customerService;
    
    private static int PAGE_SIZE = 3;

    @GetMapping(value = "/")
    public String home() {
        return "forward:/customer";
    }
    
    @GetMapping(value = "/customer")
    public String showAllCustomers(@RequestParam(value = "page", defaultValue = "0") String page, Model model) {
        //List<Customer> customers = customerService.findAll();
        PagedListHolder<CustomerDto> customers = new PagedListHolder<>(customerService.findAll());
        customers.setPage(Integer.parseInt(page));
        customers.setPageSize(PAGE_SIZE);
        model.addAttribute("customers", customers);
        return "customer/list";
    }

    @GetMapping(value = "/customer/{customerId}")
    public String showCustomerDetail(@PathVariable int customerId, Model model)
                                        throws DataNotFoundException{
        CustomerDto customerDto = customerService.findById(customerId);
        model.addAttribute("customer", customerDto);
        return "customer/detail";
    }
    
//    @ExceptionHandler(DataNotFoundException.class)
//    public String handleException() {
//        return "/customer/notfound";
//    }

}
