package com.yazlab.yazlabproject.api;

import com.yazlab.yazlabproject.entity.Metinler;
import com.yazlab.yazlabproject.entity.MetinlerDto;
import com.yazlab.yazlabproject.repo.MongoRepositoryy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping("/words")
public class WordsApi {

    @Autowired
    public MongoRepositoryy mongoRepositoryy;

    /*
    @PostConstruct
    public void init() {
        Metinler metinler = Metinler.builder()
                .eklenenMetinler(Arrays.asList("Masanın rengi","rengi siyah","siyah olsaydı guzel olurdu."))
                .build();
        mongoRepositoryy.save(metinler);
    }
    */
    @PostMapping("/ekle")
    public Metinler ekle(@RequestBody MetinlerDto metinlerDto) {

        List<String> gelenMetinler = metinlerDto.getEklenenMetinler();
        String id = metinlerDto.getId();

        Optional<Metinler> metinler = mongoRepositoryy.findById(id);

        boolean isMetinler = metinler.isPresent();

        // if present
        if (isMetinler) {
            List<String> eklenenMetinler = metinler.get().getEklenenMetinler();
            eklenenMetinler.addAll(gelenMetinler);
            return mongoRepositoryy.save(metinler.get());
        }

        // if not present
        if (!isMetinler) {
            Metinler newMetinler = Metinler.builder()
                    .eklenenMetinler(gelenMetinler)
                    .build();

            return mongoRepositoryy.save(newMetinler);
        }

        return null;
    }

    @PostMapping("/birlestir")
    public Metinler birlestir(@RequestBody MetinlerDto metinlerDto) {
        long start = System.currentTimeMillis();

        Optional<Metinler> metinler1 = mongoRepositoryy.findById(metinlerDto.getId());

        if (!metinler1.isPresent()) {
            throw new IllegalArgumentException("id not found");
        }

        Metinler metinler = metinler1.orElse(Metinler
                .builder().build());

        String[] eklenenBirlestirilmisMetinler = String.join(" ", metinler.getEklenenMetinler()).split(" ");
        ArrayList<String> arrayList = new ArrayList<>(Arrays.asList(eklenenBirlestirilmisMetinler));
        long count = arrayList.size();

        for (int i = 0; i < count; i++) {

            for (int j = i + 1; j < count; j++) {

                if(arrayList.get(i).equals(arrayList.get(j))){
                    for (int k = j-1; k >= i; k--) {
                        arrayList.remove(k);
                    }
                    j = i;
                }
                count = arrayList.size();
            }
        }
        String birlestirilmisAnlamliCumle = String.join(" ", arrayList);

        metinler.setBirlestirilmisAnlamliCumle(birlestirilmisAnlamliCumle);

        long end = System.currentTimeMillis();
        metinler.setResponseTime(end-start);

        return mongoRepositoryy.save(metinler);
    }

    @GetMapping("/all")
    public List<Metinler> getAllList() {
        return  mongoRepositoryy.findAll();
    }
}
