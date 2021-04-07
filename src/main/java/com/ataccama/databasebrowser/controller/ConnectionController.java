package com.ataccama.databasebrowser.controller;

import com.ataccama.databasebrowser.model.Connection;
import com.ataccama.databasebrowser.service.ConnectionService;
import com.ataccama.databasebrowser.service.DatabaseTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class ConnectionController {

    @Autowired
    private ConnectionService connectionService;

    @Autowired
    private DatabaseTypeService databaseTypeService;

    @GetMapping("/connectionForm")
    public String showAddConnectionForm(Model model) {
        model.addAttribute("connection", new Connection());
        model.addAttribute("databaseTypes", databaseTypeService.findAll());
        return "add-connection";
    }

    @PostMapping("/addConnection")
    public String addConnection(@Valid Connection connection, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "add-connection";
        }

        connectionService.save(connection);
        return "redirect:/index";
    }

    @GetMapping("/index")
    public String showConnectionList(Model model) {
        model.addAttribute("connections", connectionService.findAll());
        return "index";
    }

    @GetMapping("/edit/{id}")
    public String showUpdateConnectionForm(@PathVariable("id") long id, Model model) {
        Connection connection = connectionService.findById(id);

        model.addAttribute("connection", connection);
        return "update-connection";
    }

    @PostMapping("/update/{id}")
    public String updateConnection(@PathVariable("id") long id,
                                   @Valid Connection connection,
                                   BindingResult result,
                                   Model model) {
        if (result.hasErrors()) {
            connection.setId(id);
            return "update-connection";
        }

        connectionService.save(connection);
        return "redirect:/index";
    }

    @GetMapping("/delete/{id}")
    public String deleteConnection(@PathVariable("id") long id, Model model) {
        Connection connection = connectionService.findById(id);
        connectionService.delete(connection);
        return "redirect:/index";
    }
}
