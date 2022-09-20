package com.example.lab05_2;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Route(value = "index2")
public class MyView2 extends HorizontalLayout {
    private TextField addWord, addSentence;
    private TextArea goodSentence, badSentence;
    ComboBox<String> goodWords, badWords;
    private Button addGoodbtn, addBadbtn, addSenbtn, showSenbtn;

    public MyView2(){
        addWord = new TextField("Add Word");
        goodWords = new ComboBox<>("Good Words");
        badWords = new ComboBox<>("Bad Words");
        addSentence = new TextField("Add Sentence");
        goodSentence = new TextArea("Good Sentences");
        badSentence = new TextArea("Bad Sentences");

        addGoodbtn = new Button("Add Good Word");
        addBadbtn = new Button("Add Bad Word");
        addSenbtn = new Button("Add Sentence");
        showSenbtn = new Button("Show Sentence");


        VerticalLayout left = new VerticalLayout();
        VerticalLayout right = new VerticalLayout();

        left.add(addWord, addGoodbtn, addBadbtn, goodWords, badWords);
        right.add(addSentence, addSenbtn, goodSentence, badSentence, showSenbtn);

        left.setSizeFull();
        right.setSizeFull();
        addWord.setWidth("100%");
        goodWords.setWidth("100%");
        badWords.setWidth("100%");
        addSentence.setWidth("100%");
        goodSentence.setWidth("100%");
        badSentence.setWidth("100%");
        addGoodbtn.setWidth("100%");
        addBadbtn.setWidth("100%");
        addSenbtn.setWidth("100%");
        showSenbtn.setWidth("100%");

        this.add(left, right);

        Word out = WebClient.create().get().uri("http://localhost:8080/getWords/").retrieve().bodyToMono(Word.class).block();
        this.goodWords.setItems(out.goodWords);
        this.badWords.setItems(out.badWords);

        addGoodbtn.addClickListener(event -> {
            String word = addWord.getValue();
            ArrayList<String> out1 = WebClient.create().post().uri("http://localhost:8080/addGood/"+word).retrieve().bodyToMono(ArrayList.class).block();
            this.goodWords.setItems(out1);
            this.goodWords.setPlaceholder(word);
            new Notification("Insert " + word + " to Good Word Lists Complete.", 3000).open();
            this.addWord.setValue("");
        });

        addBadbtn.addClickListener(event -> {
            String word = addWord.getValue();
            ArrayList<String> out2 = WebClient.create().post().uri("http://localhost:8080/addBad/"+word).retrieve().bodyToMono(ArrayList.class).block();
            this.badWords.setItems(out2);
            this.badWords.setPlaceholder(word);
            new Notification("Insert " + word + " to Bad Word Lists Complete.", 3000).open();
            this.addWord.setValue("");
        });

        addSenbtn.addClickListener(event -> {
            String sentence = addSentence.getValue();
            System.out.println(sentence);
            String out3 = WebClient.create().post().uri("http://localhost:8080/proof/"+sentence).retrieve().bodyToMono(String.class).block();
            if (out3 == null) {
                new Notification("Word Not Found", 3000).open();
            }else if (out3.length() > 0){
                new Notification(out3, 3000).open();
            }

            this.addSentence.setValue("");
        });

        showSenbtn.addClickListener(event -> {
            Sentence out4 = WebClient.create().get().uri("http://localhost:8080/getSentence").retrieve().bodyToMono(Sentence.class).block();
            if (out4 == null){
                this.goodSentence.setPlaceholder("[]");
                this.badSentence.setPlaceholder("[]");
            }else{
                this.goodSentence.setPlaceholder(out4.goodSentence.toString());
                this.badSentence.setPlaceholder(out4.badSentence.toString());
            }

        });
    }
}
