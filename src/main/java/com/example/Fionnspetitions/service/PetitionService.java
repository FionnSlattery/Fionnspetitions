package com.example.Fionnspetitions.service;


import com.example.Fionnspetitions.model.Petition;
import com.example.Fionnspetitions.model.Signature;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
public class PetitionService {

    private final List<Petition> petitions = new ArrayList<>();
    private final AtomicLong idCounter = new AtomicLong(1);

    public PetitionService() {
        // starter/random petitions (requirement)
        petitions.add(new Petition(idCounter.getAndIncrement(),
                "Save the local park",
                "Stop the council from selling the park to developers.",
                "Alice"));
        petitions.add(new Petition(idCounter.getAndIncrement(),
                "More bike lanes",
                "We want safe cycling routes in the city.",
                "Bob"));
    }

    public List<Petition> getAll() {
        return petitions;
    }

    public Petition create(Petition p) {
        p.setId(idCounter.getAndIncrement());
        petitions.add(p);
        return p;
    }

    public Optional<Petition> findById(long id) {
        return petitions.stream().filter(p -> p.getId() == id).findFirst();
    }

    public List<Petition> searchByTitle(String keyword) {
        if (keyword == null || keyword.isBlank()) return List.of();
        String lower = keyword.toLowerCase();
        return petitions.stream()
                .filter(p -> p.getTitle().toLowerCase().contains(lower))
                .collect(Collectors.toList());
    }

    public void sign(long petitionId, Signature sig) {
        findById(petitionId).ifPresent(p -> p.addSignature(sig));
    }
}

