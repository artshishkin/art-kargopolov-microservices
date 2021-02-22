package net.shyshkin.study.photoapp.api.accounts.ui.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    @GetMapping("/status/check")
    public String statusCheck() {
        return "Working";
    }

}
