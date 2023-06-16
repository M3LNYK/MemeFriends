package com.example.memefriends;

public class Friend {

    String name;
    int totalMemes, funyMemes, nfMemes;

    public Friend(String name, int totalMemes, int funyMemes, int nfMemes) {
        this.name = name;
        this.totalMemes = totalMemes;
        this.funyMemes = funyMemes;
        this.nfMemes = nfMemes;
    }

    public Friend(String name) {
        this.name = name;
        this.totalMemes = 0;
        this.funyMemes = 0;
        this.nfMemes = 0;
    }


}
