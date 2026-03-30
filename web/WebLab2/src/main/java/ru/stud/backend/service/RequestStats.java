package ru.stud.backend.service;

public class RequestStats {
    private int total = 0;
    private int unFull = 0;
    private int unValid = 0;
    private int Valid = 0;


    public int getTotal() {
        return total;
    }

    public int getUnFull() {
        return unFull;
    }

    public int getUnValid() {
        return unValid;
    }

    public int getValid() {
        return Valid;
    }

    public void incrementTotal(){
        total++;
    }
    public void incrementUnFull(){
        unFull++;
    }
    public void incrementUnValid(){
        unValid++;
    }
    public void incrementValid(){
        Valid++;
    }
}
