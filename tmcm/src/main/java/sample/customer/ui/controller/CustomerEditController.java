package sample.customer.ui.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import sample.common.exception.DataNotFoundException;
import sample.customer.application.CustomerService;
import sample.customer.domain.dto.CustomerDto;

@Controller
@RequestMapping("/customer/{customerId}")
@SessionAttributes("editCustomer")
public class CustomerEditController {

    @Autowired
    private CustomerService customerService;

    @GetMapping(value = "/edit")
    public String redirectToEntryForm(@PathVariable int customerId, Model model)
            throws DataNotFoundException {
        CustomerDto customerDto = customerService.findById(customerId);
        model.addAttribute("editCustomer", customerDto);

        return "redirect:/customer/{customerId}/enter";
    }

    @GetMapping(value = "/enter")
    public String showEntryForm() {
        return "customer/edit/enter";
    }

    @PostMapping(value = "/enter", params = "_event_proceed")
    public String verify(
            @Valid @ModelAttribute("editCustomer") CustomerDto customer,
            Errors errors) {
        if (errors.hasErrors()) {
            return "customer/edit/enter";
        }
        return "redirect:/customer/{customerId}/review";
    }

    @GetMapping(value = "/review")
    public String showReview() {
        return "customer/edit/review";
    }

    @PostMapping(value = "/review", params = "_event_revise")
    public String revise() {
        return "redirect:/customer/{customerId}/enter";
    }

    @PostMapping(value = "/review", params = "_event_confirmed")
    public String edit(@ModelAttribute("editCustomer") CustomerDto customerDto)
            throws DataNotFoundException {
        customerService.update(customerDto);
        return "redirect:/customer/{customerId}/edited";
    }

    @GetMapping(value = "/edited")
    public String showEdited(SessionStatus sessionStatus) {
        sessionStatus.setComplete();
        return "customer/edit/edited";
    }
}