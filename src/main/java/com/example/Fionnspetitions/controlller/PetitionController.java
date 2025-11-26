package com.example.Fionnspetitions.controlller;



import com.example.Fionnspetitions.model.Petition;
import com.example.Fionnspetitions.model.Signature;
import com.example.Fionnspetitions.service.PetitionService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class PetitionController {

    private final PetitionService service;

    public PetitionController(PetitionService service) {
        this.service = service;
    }

    // Home redirect to list
    @GetMapping("/")
    public String home() {
        return "redirect:/petitions";
    }

    // LIST ALL
    @GetMapping("/petitions")
    public String list(Model model) {
        model.addAttribute("petitions", service.getAll());
        return "list";
    }

    // CREATE FORM
    @GetMapping("/petitions/new")
    public String createForm(Model model) {
        model.addAttribute("petition", new Petition());
        return "create";
    }

    // CREATE SUBMIT
    @PostMapping("/petitions")
    public String createSubmit(@ModelAttribute Petition petition) {
        service.create(petition);
        return "redirect:/petitions";
    }

    // SEARCH FORM
    @GetMapping("/petitions/search")
    public String searchForm() {
        return "search";
    }

    // SEARCH RESULTS
    @GetMapping("/petitions/search/results")
    public String searchResults(@RequestParam String query, Model model) {
        model.addAttribute("query", query);
        model.addAttribute("results", service.searchByTitle(query));
        return "results";
    }

    // VIEW ONE PETITION
    @GetMapping("/petitions/{id}")
    public String viewPetition(@PathVariable long id, Model model) {
        Petition petition = service.findById(id)
                .orElseThrow(() -> new RuntimeException("Petition not found"));
        model.addAttribute("petition", petition);
        model.addAttribute("signature", new Signature());
        return "view";
    }

    // SIGN PETITION
    @PostMapping("/petitions/{id}/sign")
    public String signPetition(@PathVariable long id,
                               @ModelAttribute Signature signature) {
        service.sign(id, signature);
        return "redirect:/petitions/" + id;
    }
}

