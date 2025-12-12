package com.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/")
public class ParcelController {

    @GetMapping
    public String index() {
        return "index";
    }

    @PostMapping("/sendParcel")
    public String sendParcel(
            @RequestParam("recipientName") String recipientName,
            @RequestParam("recipientAddress") String recipientAddress,
            @RequestParam("senderName") String senderName,
            @RequestParam("senderAddress") String senderAddress,
            @RequestParam("parcelContent") String parcelContent,
            Model model
    ) {
        // You can do something with the data, e.g., save it to a database
        // For now, let's just add it to the model and display a confirmation
        model.addAttribute("recipientName", recipientName);
        model.addAttribute("recipientAddress", recipientAddress);
        model.addAttribute("senderName", senderName);
        model.addAttribute("senderAddress", senderAddress);
        model.addAttribute("parcelContent", parcelContent);
        return "confirmation";
    }
}

