package com.example.lab05_2;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
public class WordPublisher {
    protected Word words;
    @Autowired
    private RabbitTemplate rabbit;

    public WordPublisher() {
        this.words = new Word();
        this.words.goodWords.add("happy");
        this.words.goodWords.add("enjoy");
        this.words.goodWords.add("like");
        this.words.badWords.add("fuck");
        this.words.badWords.add("olo");
    }

    @RequestMapping(value="/getWords", method = RequestMethod.GET)
    public Word getWords(){
        return this.words;
    }

    @RequestMapping(value = "/addBad/{word}", method = RequestMethod.POST)
    public ArrayList<String> addBadWord(@PathVariable("word") String s) {
        this.words.badWords.add(s);
        return this.words.badWords;
    }

    @RequestMapping(value = "/delBad/{word}", method = RequestMethod.GET)
    public ArrayList<String> deleteBadWord(@PathVariable("word") String s) {
        this.words.badWords.remove(s);
        return this.words.badWords;
    }

    @RequestMapping(value = "/addGood/{word}", method = RequestMethod.POST)
    public ArrayList<String> addGoodWord(@PathVariable("word") String s) {
        this.words.goodWords.add(s);
        return this.words.goodWords;
    }

    @RequestMapping(value = "/delGood/{word}", method = RequestMethod.GET)
    public ArrayList<String> deleteGoodWord(@PathVariable("word") String s) {
        this.words.goodWords.remove(s);
        return this.words.goodWords;
    }

    @RequestMapping(value = "/proof/{sentence}", method = RequestMethod.POST)
    public String proofSentence(@PathVariable("sentence") String s) {
        boolean bad = false;
        boolean good = false;
        String out = new String();
        for (String w : s.split(" ")) {
            bad = this.words.badWords.contains(w) || bad;
            good = this.words.goodWords.contains(w) || good;
        }
        System.out.println(good);
        System.out.println(bad);
         if (bad) {
            rabbit.convertAndSend("Direct", "bad", s);
            out = "Found Bad Word";
        }if (good) {
            rabbit.convertAndSend("Direct", "good", s);
            out = "Found Good Word";
        }if (bad && good) {
            rabbit.convertAndSend("Fanout", "", s);
            out = "Found Bad & Good Word";}
        return out;
    }

    @GetMapping("/getSentence")
    public Sentence getSentence() {
        return (Sentence) (rabbit.convertSendAndReceive("Direct", "get", ""));
    }

}
