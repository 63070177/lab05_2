package com.example.lab05_2;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class SentenceConsumer {
    protected Sentence sentences;

    public SentenceConsumer(){
        this.sentences = new Sentence();
    }

    @RabbitListener(queues = "BadWordQueue")
    public void addBadSentence(String s){
        this.sentences.badSentence.add(s);
        System.out.println("In addBadSentence Method : " + this.sentences.badSentence);
    }

    @RabbitListener(queues = "GoodWordQueue")
    public void addGoodSentence(String s){
        this.sentences.goodSentence.add(s);
        System.out.println("In addGoodSentence Method : " + this.sentences.goodSentence);
    }

    @RabbitListener(queues = "GetQueue")
    public Sentence getSentence() {
        return this.sentences;
    }
}